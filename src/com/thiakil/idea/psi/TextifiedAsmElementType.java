package com.thiakil.idea.psi;

import com.intellij.psi.tree.IElementType;
import com.thiakil.idea.TextifiedBytecodeLanguage;

public class TextifiedAsmElementType extends IElementType {
	public TextifiedAsmElementType(String debugName){
		super(debugName, TextifiedBytecodeLanguage.INSTANCE);
	}
	
	/*@Override
	public String toString() {
		return "TextifiedAsmElementType."+super.toString();
	}*/
}
