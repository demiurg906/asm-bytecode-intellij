package com.thiakil.idea.mixin;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import com.thiakil.idea.impl.TextifiedAsmMethodDeclarationImpl;
import com.thiakil.idea.impl.TextifiedAsmMethodIdentifierImpl;
import com.thiakil.idea.psi.TextifiedAsmMethodIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Thiakil on 17/01/2018.
 */
public abstract class MethodDeclarationMixin extends ASTWrapperPsiElement /*implements PsiNamedElement*/ {
	public MethodDeclarationMixin(@NotNull ASTNode node) {
		super(node);
	}

	/*@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
		return findNotNullChildByClass(TextifiedAsmMethodIdentifierImpl.class).getIdentifier().getNode().;
	}*/

	public abstract TextifiedAsmMethodIdentifier getMethodIdentifier();

	public String getName(){
		return getMethodIdentifier().getText();
	}
}
