package com.thiakil.idea.psi;

import com.intellij.psi.tree.IElementType;
import com.thiakil.idea.TextifiedBytecodeLanguage;

/**
 * Created by Thiakil on 15/01/2018.
 */
public class ASMToken {

	public static final IElementType KEYWORD = new IElementType("keyword", TextifiedBytecodeLanguage.INSTANCE);

	public static final IElementType DESC = new IElementType("desc", TextifiedBytecodeLanguage.INSTANCE);

	public static final IElementType IDENTIFIER = new IElementType("identifier", TextifiedBytecodeLanguage.INSTANCE);

	public static final IElementType CLASS_NAME = new IElementType("class name", TextifiedBytecodeLanguage.INSTANCE);

	public static final IElementType LABEL_DECLARATION = new IElementType("label declaration", TextifiedBytecodeLanguage.INSTANCE);

	public static final IElementType LABEL_REFERENCE = new IElementType("label declaration", TextifiedBytecodeLanguage.INSTANCE);

	public static final IElementType INSTRUCTION = new IElementType("instruction", TextifiedBytecodeLanguage.INSTANCE);

	public static final IElementType NUMERIC_LITERAL = new IElementType("numeric literal", TextifiedBytecodeLanguage.INSTANCE);

	public static final IElementType STRING_LITERAL = new IElementType("string literal", TextifiedBytecodeLanguage.INSTANCE);

}
