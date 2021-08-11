// This is a generated file. Not intended for manual editing.
package com.intellij.xtext.language.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.xtext.language.psi.XtextNamedArgument;
import com.intellij.xtext.language.psi.XtextREFERENCEAbstractRuleRuleID;
import com.intellij.xtext.language.psi.XtextRuleCall;
import com.intellij.xtext.language.psi.XtextVisitor;
import com.intellij.xtext.language.psi.impl.XtextPsiCompositeElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.xtext.language.psi.XtextTypes.L_ANGLE_BRACKET_KEYWORD;
import static com.intellij.xtext.language.psi.XtextTypes.R_ANGLE_BRACKET_KEYWORD;

public class XtextRuleCallImpl extends XtextPsiCompositeElementImpl implements XtextRuleCall {

    public XtextRuleCallImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull XtextVisitor visitor) {
        visitor.visitRuleCall(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof XtextVisitor) accept((XtextVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<XtextNamedArgument> getNamedArgumentList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, XtextNamedArgument.class);
    }

    @Override
    @NotNull
    public XtextREFERENCEAbstractRuleRuleID getREFERENCEAbstractRuleRuleID() {
        return findNotNullChildByClass(XtextREFERENCEAbstractRuleRuleID.class);
    }

    @Override
    @Nullable
    public PsiElement getLAngleBracketKeyword() {
        return findChildByType(L_ANGLE_BRACKET_KEYWORD);
    }

    @Override
    @Nullable
    public PsiElement getRAngleBracketKeyword() {
        return findChildByType(R_ANGLE_BRACKET_KEYWORD);
    }

}
