package com.thiakil.idea.textified;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TextifiedAsmFileType extends LanguageFileType {
	public static final TextifiedAsmFileType INSTANCE = new TextifiedAsmFileType();
	
	private TextifiedAsmFileType(){
		super(TextifiedBytecodeLanguage.INSTANCE);
	}
	
	@NotNull
	@Override
	public String getName() {
		return "OW2 Asm Textified Bytecode";
	}
	
	@NotNull
	@Override
	public String getDescription() {
		return "Textified bytecode file";
	}
	
	@NotNull
	@Override
	public String getDefaultExtension() {
		return "ow-asm";
	}
	
	@Nullable
	@Override
	public Icon getIcon() {
		return TextifiedIcon.ICON;
	}
	
	public static class Factory extends FileTypeFactory{
		
		@Override
		public void createFileTypes(@NotNull FileTypeConsumer consumer) {
			consumer.consume(INSTANCE, INSTANCE.getDefaultExtension());
		}
	}
}
