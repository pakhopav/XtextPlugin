// This is a generated file. Not intended for manual editing.
package com.intellij.xtext.language.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.xtext.language.psi.XtextAssignableAlternatives;
import com.intellij.xtext.language.psi.XtextParenthesizedAssignableElement;
import com.intellij.xtext.language.psi.XtextVisitor;
import com.intellij.xtext.language.psi.impl.XtextPsiCompositeElementImpl;
import org.jetbrains.annotations.NotNull;

import static com.intellij.xtext.language.psi.XtextTypes.L_BRACKET_KEYWORD;
import static com.intellij.xtext.language.psi.XtextTypes.R_BRACKET_KEYWORD;

public class XtextParenthesizedAssignableElementImpl extends XtextPsiCompositeElementImpl implements XtextParenthesizedAssignableElement {

    public XtextParenthesizedAssignableElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull XtextVisitor visitor) {
        visitor.visitParenthesizedAssignableElement(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof XtextVisitor) accept((XtextVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public XtextAssignableAlternatives getAssignableAlternatives() {
        return findNotNullChildByClass(XtextAssignableAlternatives.class);
    }

    @Override
    @NotNull
    public PsiElement getLBracketKeyword() {
        return findNotNullChildByType(L_BRACKET_KEYWORD);
    }

    @Override
    @NotNull
    public PsiElement getRBracketKeyword() {
        return findNotNullChildByType(R_BRACKET_KEYWORD);
    }

}
