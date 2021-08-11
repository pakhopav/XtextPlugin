// This is a generated file. Not intended for manual editing.
package com.intellij.xtext.language.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.xtext.language.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.xtext.language.psi.XtextTypes.*;

public class XtextTerminalRuleImpl extends XtextAbstractRuleImpl implements XtextTerminalRule {

    public XtextTerminalRuleImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull XtextVisitor visitor) {
        visitor.visitTerminalRule(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof XtextVisitor) accept((XtextVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<XtextAnnotation> getAnnotationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, XtextAnnotation.class);
    }

    @Override
    @NotNull
    public XtextTerminalAlternatives getTerminalAlternatives() {
        return findNotNullChildByClass(XtextTerminalAlternatives.class);
    }

    @Override
    @Nullable
    public XtextTypeRef getTypeRef() {
        return findChildByClass(XtextTypeRef.class);
    }

    @Override
    @NotNull
    public XtextValidID getValidID() {
        return findNotNullChildByClass(XtextValidID.class);
    }

    @Override
    @NotNull
    public PsiElement getColonKeyword() {
        return findNotNullChildByType(COLON_KEYWORD);
    }

    @Override
    @Nullable
    public PsiElement getFragmentKeyword() {
        return findChildByType(FRAGMENT_KEYWORD);
    }

    @Override
    @Nullable
    public PsiElement getReturnsKeyword() {
        return findChildByType(RETURNS_KEYWORD);
    }

    @Override
    @NotNull
    public PsiElement getSemicolonKeyword() {
        return findNotNullChildByType(SEMICOLON_KEYWORD);
    }

    @Override
    @NotNull
    public PsiElement getTerminalKeyword() {
        return findNotNullChildByType(TERMINAL_KEYWORD);
    }

}
