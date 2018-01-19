package com.thiakil.idea.virtualfiles;

import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Thiakil on 14/01/2018.
 */
abstract class BaseBytecodeVirtualFile extends VirtualFile {
	protected final VirtualFile classFile;
	private final String pseudoExtension;

	public BaseBytecodeVirtualFile(VirtualFile classFileIn, String extension){
		this.classFile = classFileIn;
		this.pseudoExtension = extension;
	}

	@NotNull
	@Override
	public String getName() {
		return this.classFile.getName()+"."+pseudoExtension;
	}

	@NotNull
	@Override
	public VirtualFileSystem getFileSystem() {
		return this.classFile.getFileSystem();
	}

	@NotNull
	@Override
	public String getPath() {
		return this.classFile.getPath();
	}

	@Override
	public boolean isWritable() {
		return false;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public boolean isValid() {
		return this.classFile.isValid();
	}

	@Override
	public VirtualFile getParent() {
		return this.classFile.getParent();
	}

	@Override
	public VirtualFile[] getChildren() {
		return null;
	}

	@NotNull
	@Override
	public OutputStream getOutputStream(Object requestor, long newModificationStamp, long newTimeStamp) throws IOException {
		throw new IOException("Unsupported");
	}

	protected abstract String getBuiltOutput() throws IOException;

	@NotNull
	@Override
	public byte[] contentsToByteArray() throws IOException {
		return getBuiltOutput().getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public long getTimeStamp() {
		return this.classFile.getTimeStamp();
	}

	@Override
	public long getLength() {
		try {
			return contentsToByteArray().length;
		} catch (IOException e){
			return 0;
		}
	}

	@Override
	public void refresh(boolean asynchronous, boolean recursive, @Nullable Runnable postRunnable) {
		this.classFile.refresh(asynchronous, recursive, postRunnable);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(contentsToByteArray());
	}

	@Override
	public long getModificationStamp() {
		return this.classFile.getModificationStamp();
	}

	@Nullable
	@Override
	public byte[] getBOM() {
		return CharsetToolkit.UTF8_BOM;
	}
}
