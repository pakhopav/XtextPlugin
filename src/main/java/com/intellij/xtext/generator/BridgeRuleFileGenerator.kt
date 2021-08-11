package com.intellij.xtext.generator

import com.intellij.xtext.metamodel.MetaContext
import com.intellij.xtext.metamodel.elements.emf.AssignmentType
import com.intellij.xtext.metamodel.elements.emf.EmfClassDescriptor
import com.intellij.xtext.metamodel.elements.names.NameGenerator
import com.intellij.xtext.metamodel.elements.tree.TreeLeaf
import com.intellij.xtext.metamodel.elements.tree.TreeNode.Companion.filterNodesInSubtree
import com.intellij.xtext.metamodel.elements.tree.TreeParserRule
import com.intellij.xtext.metamodel.elements.tree.TreeRule
import com.intellij.xtext.metamodel.elements.tree.TreeRuleCall
import java.io.FileOutputStream
import java.io.PrintWriter
import kotlin.test.assertNotNull

class BridgeRuleFileGenerator(extension: String, val context: MetaContext, rootPath: String) :
    com.intellij.xtext.generator.AbstractGenerator(extension, rootPath) {


    fun generateEmfBridgeRuleFile(treeRule: TreeParserRule) {
        val file = createOrFindFile("$extensionCapitalized${treeRule.name}BridgeRule.kt", myGenDir + "/bridge/rules")
        val out = PrintWriter(FileOutputStream(file))
        out.println("package $packageDir.bridge.rules")

        out.println(
            """
            |import $packageDir.psi.*
            |import com.intellij.psi.PsiElement
            |import com.intellij.xtextLanguage.xtext.bridge.*
            |import org.eclipse.emf.ecore.EObject
            |import org.eclipse.emf.ecore.EDataType
            |import org.eclipse.emf.ecore.EStructuralFeature
            |import kotlin.test.assertNotNull
            |
            |class ${extensionCapitalized}${treeRule.name}BridgeRule : EmfBridgeRule {
        """.trimMargin("|")
        )
        generateLiteralAssignmentMethod(treeRule, out)
        generateObjectAssignmentMethod(treeRule, out)
        generateRewriteMethod(treeRule, out)
        generateFactoryMethod(treeRule, out)
        generateActionMethod(treeRule, out)
        out.print("\n}")
        out.close()
    }


    private fun generateLiteralAssignmentMethod(rule: TreeRule, out: PrintWriter) {
        out.println("    override fun findLiteralAssignment(pointer: PsiElement): LiteralAssignment? {")
        var elseWord = ""
        val literalAssignments = context.findLiteralAssignmentsInRule(rule)
        literalAssignments.forEach { node ->
            val assignableType =
                if (node is TreeRuleCall) context.getRuleByName(node.getBnfName()).returnType else EmfClassDescriptor.STRING
            val psiElementTypeName = node.getPsiElementTypeName()
            assertNotNull(psiElementTypeName)


            out.print(
                """
                |        ${elseWord}if (
            """.trimMargin("|")
            )
            out.println("pointer.node.elementType == ${extensionCapitalized}Types.$psiElementTypeName) {")
            out.println(
                """
                |            return object : LiteralAssignment {
                |                override fun assign(obj: EObject, literal: PsiElement): EStructuralFeature {
                |                    val feature = obj.eClass().eAllStructuralFeatures.firstOrNull { it.name == "${node.assignment!!.featureName}" }
                |                    assertNotNull(feature)
                |                    val ePackage = ${assignableType.getPackageInstanceString()}
                |                    val classifier = ePackage.getEClassifier("${assignableType.className}") as EDataType
                |                    val value = ePackage.eFactoryInstance.createFromString(classifier, literal.text)
                                    
            """.trimMargin("|")
            )
            when (node.assignment!!.type) {
                AssignmentType.EQUALS -> {
                    out.println("                    obj.eSet(feature, value)")
                }
                AssignmentType.PLUS_EQUALS -> {
                    out.println(
                        """
                        |                   feature?.let {
                        |                       Helper.esetMany(obj, it, value)
                        |                   }
                    """.trimMargin("|")
                    )
                }
                AssignmentType.QUESTION_EQUALS -> {
                    out.println("                    obj.eSet(feature, true)")
                }
            }
            out.println(
                """
                |                    return feature
                |                }
                |            }
                |        }
            """.trimMargin("|")
            )
            if (elseWord.isEmpty()) elseWord = "else "
        }
        out.println(
            """
            |        return null
            |    }
        """.trimMargin("|")
        )
    }


    private fun generateObjectAssignmentMethod(rule: TreeRule, out: PrintWriter) {
        out.println("    override fun findObjectAssignment(pointer: PsiElement): ObjectAssignment? {")
        var elseWord = ""
        val objectAssignments = context.findObjectAssignmentsInRule(rule)
        objectAssignments.forEach { node ->
            val psiElementClassName = "$extensionCapitalized${NameGenerator.toGKitClassName(node.getBnfName())}"
            out.print(
                """
                |        ${elseWord}if (
            """.trimMargin("|")
            )
            out.println("pointer is $psiElementClassName) {")

            out.println(
                """
                |            return object : ObjectAssignment {
                |                override fun assign(obj: EObject, toAssign: EObject) {
                |                    val feature = obj.eClass().eAllStructuralFeatures.firstOrNull { it.name == "${node.assignment!!.featureName}" }
            """.trimMargin("|")
            )

            when (node.assignment!!.type) {
                AssignmentType.EQUALS -> {
                    out.println("                    obj.eSet(feature, toAssign)")
                }
                AssignmentType.PLUS_EQUALS -> {
                    out.println(
                        """
                        |                   feature?.let {
                        |                       Helper.esetMany(obj, it, toAssign)
                        |                   }
                    """.trimMargin("|")
                    )
                }
                AssignmentType.QUESTION_EQUALS -> {
                    out.println("                    obj.eSet(feature, true)")
                }
            }
            out.println(
                """
                |                }
                |            }
                |        }
            """.trimMargin("|")
            )
            if (elseWord.isEmpty()) elseWord = "else "
        }
        out.println(
            """
            |        return null
            |    }
        """.trimMargin("|")
        )
    }


    private fun generateRewriteMethod(rule: TreeRule, out: PrintWriter) {
        out.println("   override fun findRewrite(pointer: PsiElement): Rewrite? {")
        var elseWord = ""
        val nodesWithRewrite = rule.filterNodesInSubtree { it is TreeLeaf && it.rewrite != null }.map { it as TreeLeaf }
        nodesWithRewrite.forEach { node ->
            val newObjectType = node.rewrite!!.newObjectType
            assertNotNull(newObjectType)
            val psiElementType = node.getPsiElementTypeName()
            out.println(
                """
                |       ${elseWord}if (pointer.node.elementType == ${extensionCapitalized}Types.${psiElementType}) {
                |           return object : Rewrite {
                |               override fun rewrite(obj: EObject): EObject {
                |                   val temp = ${newObjectType.getFactoryInstanceString()}.create(${newObjectType.getPackageInstanceString()}.${
                    NameGenerator.toPropertyName(
                        newObjectType.className
                    )
                })
                |                   val feature = temp.eClass().eAllStructuralFeatures.firstOrNull { it.name == "${node.rewrite!!.assignment.featureName}" }
            """.trimMargin("|")
            )
            when (node.rewrite!!.assignment.type) {
                AssignmentType.EQUALS -> {
                    out.println(
                        """
                        |                   temp.eSet(feature, obj)
                        |                   return temp
                    """.trimMargin("|")
                    )
                }
                AssignmentType.PLUS_EQUALS -> {
                    out.println(
                        """
                        |                   feature?.let {
                        |                       Helper.esetMany(temp, it, obj)
                        |                   }
                        |                   return temp
                    """.trimMargin("|")
                    )
                }
                AssignmentType.QUESTION_EQUALS -> {
                    out.println(
                        """
                        |                   temp.eSet(feature, true)
                        |                   return temp
                    """.trimMargin("|")
                    )
                }
            }
            out.println(
                """
                |               }
                |           }
                |       }
            """.trimMargin("|")
            )
            if (elseWord.isEmpty()) elseWord = "else "
        }
        out.println(
            """
            |       return null
            |   }
        """.trimMargin("|")
        )

    }

    private fun generateFactoryMethod(rule: TreeRule, out: PrintWriter) {
        out.println(
            """
            |    override fun createObject(): EObject {
            |       return ${rule.returnType.getFactoryInstanceString()}.create(${rule.returnType.getPackageInstanceString()}.${
                NameGenerator.toPropertyName(
                    rule.returnType.className
                )
            })
            |    }
            |
        """.trimMargin("|")
        )
    }


    private fun generateActionMethod(rule: TreeRule, out: PrintWriter) {
        out.println(
            """
            |    override fun findAction(pointer: PsiElement): EObject? {
        """.trimMargin("|")
        )
        val nodesWithSimpleAction =
            rule.filterNodesInSubtree { it is TreeLeaf && it.simpleAction != null }.map { it as TreeLeaf }
        var elseWord = ""
        nodesWithSimpleAction.forEach { node ->
            val newObjectType = node.simpleAction!!
            val psiElementType = node.getPsiElementTypeName()
            out.println(
                """
            |        ${elseWord}if (pointer.node.elementType == ${extensionCapitalized}Types.${psiElementType}){
            |            return ${newObjectType.getFactoryInstanceString()}.create(${newObjectType.getPackageInstanceString()}.${
                    NameGenerator.toPropertyName(
                        newObjectType.className
                    )
                })
            |        }
        """.trimMargin("|")
            )
            elseWord = "else "
        }
        out.println(
            """
            |        return null
            |    }
        """.trimMargin("|")
        )
    }

}