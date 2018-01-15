package com.thiakil.idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Thiakil on 14/01/2018.
 */
public class TextifiedBytecodeEditorProvider extends BaseBytecodeEditorProvider {
	

	@NotNull
	@Override
	public String getEditorTypeId() {
		return "bytecode-asm-outline-textified";
	}
	
	@Override
	protected VirtualFile getProxyVirtualFile(VirtualFile realFile, Project project) {
		return new TextifiedVFile(realFile);
	}
	
	@Override
	protected String getTabName() {
		return "Bytecode";
	}
}
