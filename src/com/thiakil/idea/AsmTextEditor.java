package com.thiakil.idea;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;

public class AsmTextEditor extends PsiAwareTextEditorImpl {

	private final String myName;
	
	public AsmTextEditor(@NotNull Project project, @NotNull VirtualFile file, TextEditorProvider provider, String name) {
		super(project, file, provider);
		this.myName = name;
	}

	@NotNull
	@Override
	public String getName() {
		return myName;
	}
	
	@Override
	public boolean canNavigateTo(@NotNull Navigatable navigatable) {
		return navigatable instanceof OpenFileDescriptor && ((OpenFileDescriptor) navigatable).getFile().equals(getFile()) && super.canNavigateTo(navigatable);
	}
}
