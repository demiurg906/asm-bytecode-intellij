package com.thiakil.idea;

import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Thiakil on 14/01/2018.
 */
public class TextifiedBytecodeEditorProvider extends PsiAwareTextEditorProvider {
	@Override
	public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
		return file.getFileType() == JavaClassFileType.INSTANCE;
	}

	@NotNull
	@Override
	public String getEditorTypeId() {
		return super.getEditorTypeId()+"textified";
	}

	@NotNull
	@Override
	public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
		return new TextEditor(project, new TextifiedVFile(file), this);
	}
	
	@NotNull
	@Override
	public FileEditorPolicy getPolicy() {
		return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR;
	}

	public static class TextEditor extends PsiAwareTextEditorImpl{

		public TextEditor(@NotNull Project project, @NotNull VirtualFile file, TextEditorProvider provider) {
			super(project, file, provider);
		}

		@NotNull
		@Override
		public String getName() {
			return "Bytecode Outline";
		}
	}
}