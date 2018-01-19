package com.thiakil.idea.virtualfiles;

import com.intellij.openapi.vfs.VirtualFile;
import org.objectweb.asm.idea.CustomASMifier;
import reloc.org.objectweb.asm.ClassReader;
import reloc.org.objectweb.asm.util.ASMifier;
import reloc.org.objectweb.asm.util.Textifier;
import reloc.org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Thiakil on 14/01/2018.
 */
public class AsmifiedVFile extends BaseBytecodeVirtualFile {
	public AsmifiedVFile(VirtualFile classFileIn){
		super(classFileIn, "asmified.java");
	}

	@Override//TODO cache
	protected String getBuiltOutput() throws IOException{
		byte[] contents = this.classFile.contentsToByteArray();
		ClassReader cr = new ClassReader(contents);
		StringWriter sw = new StringWriter();
		cr.accept(new TraceClassVisitor(null, new CustomASMifier(), new PrintWriter(sw)), 0);
		return sw.toString();
	}
}
