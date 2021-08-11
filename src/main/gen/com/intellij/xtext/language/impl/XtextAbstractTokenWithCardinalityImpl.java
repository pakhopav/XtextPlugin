// This is a generated file. Not intended for manual editing.
package com.intellij.xtext.language.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.xtext.language.psi.XtextAbstractTerminal;
import com.intellij.xtext.language.psi.XtextAbstractTokenWithCardinality;
import com.intellij.xtext.language.psi.XtextAssignment;
import com.intellij.xtext.language.psi.XtextVisitor;
import com.intellij.xtext.language.psi.impl.XtextPsiCompositeElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.xtext.language.psi.XtextTypes.*;

public class XtextAbstractTokenWithCardinalityImpl extends XtextPsiCompositeElementImpl implements XtextAbstractTokenWithCardinality {

    public XtextAbstractTokenWithCardinalityImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull XtextVisitor visitor) {
        visitor.visitAbstractTokenWithCardinality(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof XtextVisitor) accept((XtextVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public XtextAbstractTerminal getAbstractTerminal() {
        return findChildByClass(XtextAbstractTerminal.class);
    }

    @Override
    @Nullable
    public XtextAssignment getAssignment() {
        return findChildByClass(XtextAssignment.class);
    }

    @Override
    @Nullable
    public PsiElement getAsteriskKeyword() {
        return findChildByType(ASTERISK_KEYWORD);
    }

    @Override
    @Nullable
    public PsiElement getPlusKeyword() {
        return findChildByType(PLUS_KEYWORD);
    }

    @Override
    @Nullable
    public PsiElement getQuesMarkKeyword() {
        return findChildByType(QUES_MARK_KEYWORD);
    }

}
