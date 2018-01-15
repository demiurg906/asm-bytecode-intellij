package com.thiakil.idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Thiakil on 14/01/2018.
 */
public class GroovifiedBytecodeEditorProvider extends BaseBytecodeEditorProvider {
	
	@NotNull
	@Override
	public String getEditorTypeId() {
		return "bytecode-asm-outline-groovified";
	}
	
	@Override
	protected VirtualFile getProxyVirtualFile(VirtualFile realFile, Project project) {
		return new GroovifiedVFile(realFile, project);
	}
	
	@Override
	protected String getTabName() {
		return "Groovified";
	}
}
