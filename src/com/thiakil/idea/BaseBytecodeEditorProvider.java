package com.thiakil.idea;

import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.idea.ShowBytecodeOutlineAction;

public abstract class BaseBytecodeEditorProvider extends PsiAwareTextEditorProvider {
	
	@Override
	public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
		if (file.getFileType() == JavaClassFileType.INSTANCE){
			return true;
		}
		if (file.getFileType() != JavaFileType.INSTANCE || DumbService.isDumb(project)){
			return false;
		}
		return findCompiledClass(file, project) != null;
	}
	
	protected VirtualFile findCompiledClass(VirtualFile fileIn, Project project){
		PsiFile psiFileIn = PsiManager.getInstance(project).findFile(fileIn);
		if (psiFileIn == null || !(psiFileIn instanceof PsiClassOwner))
			return null;
		if (!fileIn.isInLocalFileSystem() && !fileIn.isWritable()){//maybe library file
			final PsiClass[] psiClasses = ((PsiClassOwner) psiFileIn).getClasses();
			if (psiClasses.length > 0) {
				return psiClasses[0].getOriginalElement().getContainingFile().getVirtualFile();
			}
			return null;
		} else {
			final Module module = ModuleUtil.findModuleForFile(fileIn, project);
			if (module == null)
				return null;
			final CompilerModuleExtension cme = CompilerModuleExtension.getInstance(module);
			if (cme == null)
				return null;
			final VirtualFile[] outputDirectories = cme.getOutputRoots(true);
			
			/*CompilerManager compilerManager = CompilerManager.getInstance(project);
			final CompileScope compileScope = compilerManager.createFilesCompileScope(new VirtualFile[]{fileIn});
			if (!compilerManager.isUpToDate(compileScope))
				return null;*/
			VirtualFile target = ShowBytecodeOutlineAction.findClassFile(outputDirectories, psiFileIn);
			if (target != null && target.getTimeStamp() > fileIn.getTimeStamp())
				return target;
			return null;
		}
	}
	
	@NotNull
	@Override
	public FileEditorPolicy getPolicy() {
		return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR;
	}
	
	@NotNull
	@Override
	public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
		if (file.getFileType() == JavaClassFileType.INSTANCE)
			return new AsmTextEditor(project, getProxyVirtualFile(file, project), this, getTabName());
		else
			return new AsmTextEditor(project, getProxyVirtualFile(findCompiledClass(file, project), project), this, getTabName());
	}
	
	protected abstract VirtualFile getProxyVirtualFile(VirtualFile realFile, Project project);
	
	protected abstract String getTabName();
}
