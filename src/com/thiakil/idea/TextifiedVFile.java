package com.thiakil.idea;

import com.intellij.openapi.vfs.VirtualFile;
import reloc.org.objectweb.asm.ClassReader;
import reloc.org.objectweb.asm.util.Textifier;
import reloc.org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Thiakil on 14/01/2018.
 */
public class TextifiedVFile extends BaseBytecodeVirtualFile {
	public TextifiedVFile(VirtualFile classFileIn){
		super(classFileIn, "textified.java");
	}

	@Override//TODO cache
	protected String getBuiltOutput() throws IOException{
		byte[] contents = this.classFile.contentsToByteArray();
		ClassReader cr = new ClassReader(contents);
		StringWriter sw = new StringWriter();
		cr.accept(new TraceClassVisitor(null, new Textifier(), new PrintWriter(sw)), 0);
		return sw.toString();
	}
}
