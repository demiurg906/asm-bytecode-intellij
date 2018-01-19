package com.thiakil.idea.virtualfiles;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.objectweb.asm.idea.GroovifiedTextifier;
import org.objectweb.asm.idea.config.ASMPluginComponent;
import org.objectweb.asm.idea.config.GroovyCodeStyle;
import reloc.org.objectweb.asm.ClassReader;
import reloc.org.objectweb.asm.util.Textifier;
import reloc.org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Thiakil on 14/01/2018.
 */
public class GroovifiedVFile extends BaseBytecodeVirtualFile {
	private final GroovyCodeStyle myCodeStyle;
	public GroovifiedVFile(VirtualFile classFileIn, Project projectIn){
		super(classFileIn, "groovified.java");
		this.myCodeStyle = projectIn.getComponent(ASMPluginComponent.class).getCodeStyle();
	}

	@Override//TODO cache
	protected String getBuiltOutput() throws IOException{
		byte[] contents = this.classFile.contentsToByteArray();
		ClassReader cr = new ClassReader(contents);
		StringWriter sw = new StringWriter();
		cr.accept(new TraceClassVisitor(null, new GroovifiedTextifier(myCodeStyle), new PrintWriter(sw)), 0);
		return sw.toString();
	}
}
