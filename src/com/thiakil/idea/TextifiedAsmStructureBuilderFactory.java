package com.thiakil.idea;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.thiakil.idea.psi.TextifiedAsmFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class TextifiedAsmStructureBuilderFactory implements PsiStructureViewFactory {
	@Nullable
	@Override
	public StructureViewBuilder getStructureViewBuilder(@NotNull PsiFile psiFile) {
		if (!(psiFile instanceof TextifiedAsmFile)){
			throw new IllegalArgumentException("psiFile");
		}
		return new TreeBasedStructureViewBuilder() {
			@NotNull
			@Override
			public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
				return new TextEditorBasedStructureViewModel(editor, psiFile) {
					@NotNull
					@Override
					public StructureViewTreeElement getRoot() {
						return new TextifiedTreeElement((TextifiedAsmFile)psiFile);
					}
				};
			}
		};
	}
	
	public static class TextifiedTreeElement extends PsiTreeElementBase<PsiElement> implements ColoredItemPresentation {
		
		protected TextifiedTreeElement(PsiElement psiElement) {
			super(psiElement);
		}
		
		protected TextifiedTreeElement(TextifiedAsmFile psiElement) {
			super(psiElement);
		}
		
		@NotNull
		@Override
		public Collection<StructureViewTreeElement> getChildrenBase() {
			PsiElement element = getElement();
			if (element == null)
				return Collections.emptyList();
			ArrayList<StructureViewTreeElement> result = new ArrayList<>();
			for (PsiElement child : element.getChildren()){
				if (!(child instanceof PsiWhiteSpace)){
					result.add(new TextifiedTreeElement(child));
				}
			}
			return result;
		}
		
		@Nullable
		@Override
		public String getPresentableText() {
			PsiElement element = getElement();
			ASTNode node = element != null ? element.getNode() : null;
			IElementType elementType = node != null ? node.getElementType() : null;
			if (element instanceof TextifiedAsmFile){
				return "Asm File: "+((TextifiedAsmFile) element).getVirtualFile().getPath();
			} else if (element instanceof LeafPsiElement) {
				return elementType + ": '" + element.getText() + "'";
			}
			else if (element instanceof PsiErrorElement) {
				return "PsiErrorElement: '" + ((PsiErrorElement)element).getErrorDescription() + "'";
			} else if (element instanceof ASTWrapperPsiElement){
				return elementType + ": '" + element.getText() + "'";
			}
			return element != null ? element.toString() : null;
		}
		
		@Nullable
		@Override
		public TextAttributesKey getTextAttributesKey() {
			return getElement() instanceof PsiErrorElement? CodeInsightColors.ERRORS_ATTRIBUTES : null;
		}
	}
}
