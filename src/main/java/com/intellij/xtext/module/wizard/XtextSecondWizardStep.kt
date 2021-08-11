package com.intellij.xtext.module.wizard

import com.intellij.ide.util.PropertiesComponent
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.panel
import com.intellij.ui.layout.selected
import com.intellij.xtext.language.psi.XtextFile
import com.intellij.xtext.language.psi.XtextGrammar
import com.intellij.xtext.language.psi.XtextReferencedMetamodel
import com.intellij.xtext.module.XtextModuleBuilder
import com.intellij.xtext.module.XtextModuleBuilderHelper
import java.awt.Color
import java.util.jar.JarFile
import javax.swing.JLabel

class XtextSecondWizardStep(val context: WizardContext, val builder: XtextModuleBuilder) : ModuleWizardStep() {
    var myUsedGrammars = mutableListOf<XtextGrammarFileInfo>()
    var myImportedModels = mutableListOf<EcoreModelJarInfo>()
    var myXtextFile: XtextFile? = null

    //    var languageName = "LanguageName"
//    var languageExtension = "lang"
    var xtextFilePath = "..."
    var fromExistingXtext = false

    private val defaultProject = ProjectManager.getInstance().defaultProject
    private val helper = XtextModuleBuilderHelper(defaultProject)

    val useExistedGrammarCheckBox = JBCheckBox("Use existed .xtext file")
    val jarTable = ImportedJarsTable()
    val grammarsTable = UsedGrammarsTable(jarTable)
    val errorComment = JLabel("error").also {
        it.isVisible = false
        it.foreground = Color.RED
    }
    val languageNameTextField = JBTextField("LanguageName", 0)
    val languageExtensionTextField = JBTextField("lang", 0)

    init {
//        WriteAction.compute<Unit, RuntimeException> {
//            PsiDocumentManager.getInstance(defaultProject).commitAllDocuments()
//            FileDocumentManager.getInstance().saveAllDocuments()
//        }


    }

    private val contentPanel by lazy {
        panel {
            row("") {
                useExistedGrammarCheckBox()
            }
            row("") {
                val fileChooserDescriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor()
                textFieldWithBrowseButton(
                    { xtextFilePath },
                    { v -> xtextFilePath = v },
                    "",
                    context.project,
                    fileChooserDescriptor,
                    myFileChosen
                )
                    .enableIf(useExistedGrammarCheckBox.selected)
                    .commentComponent(errorComment, true)
            }
            row("Language Name:") {
                languageNameTextField()
                    .focused()
            }
            row("Language Extension:") {
//                textField(PropertyBinding({ languageExtension }, { v -> languageExtension = v }))
                languageExtensionTextField()
            }
            row("") {
                grammarsTable()
                jarTable()
            }

        }
    }

    init {
        useExistedGrammarCheckBox.addActionListener { e ->
            if (fromExistingXtext) fromExistingXtext = !fromExistingXtext
        }
        val pc = PropertiesComponent.getInstance()
        print("")
    }

    override fun getComponent() = contentPanel

    override fun updateDataModel() {
        builder.importedModels = myImportedModels
        builder.usedGrammars = myUsedGrammars
        builder.langName = languageNameTextField.text
        builder.langExtension = languageExtensionTextField.text
        builder.grammarFile = myXtextFile
    }

    override fun validate(): Boolean {
        updateStep()
        var valid = allTextFieldsFilled()
        if (fromExistingXtext) {
            valid = valid && allTalesFound()
        }
        return valid
//        return true
    }

    val myFileChosen: ((chosenFile: VirtualFile) -> String) = { file: VirtualFile ->
        myUsedGrammars.clear()
        myImportedModels.clear()
        var xtextFile: XtextFile? = null
        var correctFile = true
        try {
            xtextFile = PsiManager.getInstance(defaultProject).findFile(file) as XtextFile
        } catch (e: ClassCastException) {
            errorComment.isVisible = true
            languageNameTextField.isEnabled = true
            correctFile = false
        }
        if (correctFile && xtextFile != null) {
            errorComment.isVisible = false
            setLanguageTextField(helper.getGrammarName(xtextFile))
            languageNameTextField.isEnabled = false
            findUsedGrammars(xtextFile)
            findImportedModels(xtextFile)
            grammarsTable.setElements(myUsedGrammars)
            jarTable.setElements(myImportedModels)
            fromExistingXtext = true
            myXtextFile = xtextFile
        }
        getUiPath(file.path)
    }

    override fun updateStep() {
        myUsedGrammars = grammarsTable.getElements().toMutableList()
        myImportedModels = jarTable.getElements().toMutableList()
    }

    private fun setLanguageTextField(langName: String) {
//        languageName = langName
        languageNameTextField.text = langName
    }

    private fun allTalesFound(): Boolean {
        val grammarsFound = !myUsedGrammars.filter { it.file == null }.any()
        val jarsFound = !myImportedModels.filter { it.jarFile == null }.any()
        return grammarsFound && jarsFound
    }

    private fun allTextFieldsFilled(): Boolean {
        val langExtension = languageExtensionTextField.text
        val langName = languageNameTextField.text
        return helper.isValidGrammarName(langName) && helper.isValidGrammarExtension(langExtension)
    }

    private fun findUsedGrammars(grammarFile: XtextFile) {
        val grammar = PsiTreeUtil.findChildOfType(grammarFile, XtextGrammar::class.java) ?: return
        val usedGrammars = grammar.referenceGrammarGrammarIDList
        usedGrammars.forEach {
            val grammarName = it.text
            if (helper.containsInKnownGrammars(grammarName)) {
                val grammarFile = helper.getKnownGrammar(grammarName)!!
                this.myUsedGrammars.add(XtextGrammarFileInfo(grammarName, helper.getKnownGrammar(grammarName)))
                findUsedGrammars(grammarFile)
                findImportedModels(grammarFile)
            } else this.myUsedGrammars.add(XtextGrammarFileInfo(grammarName, null))
        }
    }

    private fun findImportedModels(grammarFile: XtextFile) {
        val importedModels = PsiTreeUtil.findChildrenOfType(grammarFile, XtextReferencedMetamodel::class.java)
        importedModels.forEach {
            val modelName = it.referenceePackageSTRING.text.replace("\"", "")
            if (helper.containsInKnownModels(modelName)) {
                val file = helper.getKnownModel(modelName)!!
                val jarFile = JarFile(file)
                addModel(EcoreModelJarInfo(modelName, path = file.path, jarFile = jarFile, file = file))
            } else {
                addModel(EcoreModelJarInfo(modelName))
            }
        }
    }

    private fun addModel(modelInfo: EcoreModelJarInfo) {
        if (!myImportedModels.map { it.uri }.contains(modelInfo.uri)) {
            myImportedModels.add(modelInfo)
        }
    }

    private fun getUiPath(path: String): String =
        FileUtil.getLocationRelativeToUserHome(FileUtil.toSystemDependentName(path.trim()), false)


}