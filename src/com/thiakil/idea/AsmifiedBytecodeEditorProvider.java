package com.thiakil.idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.thiakil.idea.virtualfiles.AsmifiedVFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Thiakil on 14/01/2018.
 */
public class AsmifiedBytecodeEditorProvider extends BaseBytecodeEditorProvider {

	@NotNull
	@Override
	public String getEditorTypeId() {
		return "bytecode-asm-outline-asmified";
	}
	
	@Override
	protected VirtualFile getProxyVirtualFile(VirtualFile realFile, Project project) {
		return new AsmifiedVFile(realFile);
	}
	
	@Override
	protected String getTabName() {
		return "Asmified";
	}
}
