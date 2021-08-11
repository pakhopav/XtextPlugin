// This is a generated file. Not intended for manual editing.
package com.intellij.xtext.language.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.xtext.language.psi.XtextPredicatedKeyword;
import com.intellij.xtext.language.psi.XtextVisitor;
import com.intellij.xtext.language.psi.impl.XtextPsiCompositeElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.xtext.language.psi.XtextTypes.*;

public class XtextPredicatedKeywordImpl extends XtextPsiCompositeElementImpl implements XtextPredicatedKeyword {

    public XtextPredicatedKeywordImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull XtextVisitor visitor) {
        visitor.visitPredicatedKeyword(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof XtextVisitor) accept((XtextVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getPredKeyword() {
        return findChildByType(PRED_KEYWORD);
    }

    @Override
    @NotNull
    public PsiElement getString() {
        return findNotNullChildByType(STRING);
    }

    @Override
    @Nullable
    public PsiElement getWeakPredKeyword() {
        return findChildByType(WEAK_PRED_KEYWORD);
    }

}
