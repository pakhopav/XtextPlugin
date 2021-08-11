// This is a generated file. Not intended for manual editing.
package com.intellij.xtext.language.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.xtext.language.psi.XtextCrossReference;
import com.intellij.xtext.language.psi.XtextCrossReferenceableTerminal;
import com.intellij.xtext.language.psi.XtextTypeRef;
import com.intellij.xtext.language.psi.XtextVisitor;
import com.intellij.xtext.language.psi.impl.XtextPsiCompositeElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.xtext.language.psi.XtextTypes.*;

public class XtextCrossReferenceImpl extends XtextPsiCompositeElementImpl implements XtextCrossReference {

    public XtextCrossReferenceImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull XtextVisitor visitor) {
        visitor.visitCrossReference(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof XtextVisitor) accept((XtextVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public XtextCrossReferenceableTerminal getCrossReferenceableTerminal() {
        return findChildByClass(XtextCrossReferenceableTerminal.class);
    }

    @Override
    @NotNull
    public XtextTypeRef getTypeRef() {
        return findNotNullChildByClass(XtextTypeRef.class);
    }

    @Override
    @NotNull
    public PsiElement getLSquareBracketKeyword() {
        return findNotNullChildByType(L_SQUARE_BRACKET_KEYWORD);
    }

    @Override
    @Nullable
    public PsiElement getPipeKeyword() {
        return findChildByType(PIPE_KEYWORD);
    }

    @Override
    @NotNull
    public PsiElement getRSquareBracketKeyword() {
        return findNotNullChildByType(R_SQUARE_BRACKET_KEYWORD);
    }

}
