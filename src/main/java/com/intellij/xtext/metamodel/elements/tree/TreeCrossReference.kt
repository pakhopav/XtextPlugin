package com.intellij.xtext.metamodel.elements.tree

import com.intellij.xtext.metamodel.elements.emf.Assignment
import com.intellij.xtext.metamodel.elements.emf.EmfClassDescriptor

interface TreeCrossReference : TreeLeaf {
    override val assignment: Assignment
    val containerRuleName: String
    val targetType: EmfClassDescriptor
}