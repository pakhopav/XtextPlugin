package com.intellij.xtext.language.bridge.rules

import com.intellij.psi.PsiElement
import com.intellij.xtext.bridge.*
import com.intellij.xtext.language.psi.XtextGroup
import org.eclipse.emf.ecore.EObject

class XtextUnorderedGroupSuffix1BridgeRule : EmfBridgeRule {
    override fun findLiteralAssignment(pointer: PsiElement): LiteralAssignment? {
        return null
    }

    override fun findObjectAssignment(pointer: PsiElement): ObjectAssignment? {
        if (pointer is XtextGroup) {
            return object : ObjectAssignment {
                override fun assign(obj: EObject, toAssign: EObject) {
                    val feature = obj.eClass().eAllStructuralFeatures.firstOrNull { it.name == "elements" }
                    feature?.let {
                        Helper.esetMany(obj, it, toAssign)
                    }
                }
            }
        }
        return null
    }

    override fun findRewrite(pointer: PsiElement): Rewrite? {
        return null
    }

    override fun createObject(): EObject {
        return org.xtext.example.xtext.xtext.XtextFactory.eINSTANCE.create(org.xtext.example.xtext.xtext.XtextPackage.eINSTANCE.abstractElement)
    }

    override fun findAction(pointer: PsiElement): EObject? {
        return null
    }

}