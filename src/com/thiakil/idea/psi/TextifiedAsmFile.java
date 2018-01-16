package com.thiakil.idea.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.thiakil.idea.TextifiedAsmFileType;
import com.thiakil.idea.TextifiedBytecodeLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by Thiakil on 16/01/2018.
 */
public class TextifiedAsmFile extends PsiFileBase {
	public TextifiedAsmFile(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, TextifiedBytecodeLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return TextifiedAsmFileType.INSTANCE;
	}

	@Override
	public String toString() {
		return "Textified bytecode file";
	}

	@Override
	public Icon getIcon(int flags) {
		return super.getIcon(flags);
	}
}
