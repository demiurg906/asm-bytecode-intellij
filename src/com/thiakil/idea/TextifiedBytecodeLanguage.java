package com.thiakil.idea;

import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Thiakil on 14/01/2018.
 */
public class TextifiedBytecodeLanguage extends Language {

	public static final Language INSTANCE = new TextifiedBytecodeLanguage();

	private TextifiedBytecodeLanguage(){
		super("JAVA-ASM-Textified");
	}

	@Nullable
	@Override
	public Language getBaseLanguage() {
		return JavaLanguage.INSTANCE;
	}
}
