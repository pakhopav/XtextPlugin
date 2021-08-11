// This is a generated file. Not intended for manual editing.
package com.intellij.xtext.language.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface XtextTerminalRule extends XtextAbstractRule {

    @NotNull
    List<XtextAnnotation> getAnnotationList();

    @NotNull
    XtextTerminalAlternatives getTerminalAlternatives();

    @Nullable
    XtextTypeRef getTypeRef();

    @NotNull
    XtextValidID getValidID();

    @NotNull
    PsiElement getColonKeyword();

    @Nullable
    PsiElement getFragmentKeyword();

    @Nullable
    PsiElement getReturnsKeyword();

    @NotNull
    PsiElement getSemicolonKeyword();

    @NotNull
    PsiElement getTerminalKeyword();

}
