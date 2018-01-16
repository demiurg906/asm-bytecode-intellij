package com.thiakil.idea.psi;

import com.intellij.psi.tree.IElementType;
import com.thiakil.idea.TextifiedBytecodeLanguage;

public class TextifiedAsmTokenType extends IElementType {
	public TextifiedAsmTokenType(String debugName){
		super(debugName, TextifiedBytecodeLanguage.INSTANCE);
	}
	
	@Override
	public String toString() {
		return "TextifiedAsmTokenType"+super.toString();
	}
}
