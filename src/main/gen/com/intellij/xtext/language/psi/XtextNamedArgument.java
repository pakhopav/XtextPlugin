// This is a generated file. Not intended for manual editing.
package com.intellij.xtext.language.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface XtextNamedArgument extends PsiElement {

    @NotNull
    XtextDisjunction getDisjunction();

    @Nullable
    XtextREFERENCEParameterID getREFERENCEParameterID();

    @Nullable
    PsiElement getEqualsKeyword();

}
