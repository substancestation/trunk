package org.orbit.substance.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.orbit.substance.model.dfs.Path;

public interface DFile {

	DFS getDFS();

	DFile getParent() throws IOException;

	String getFileId();

	Path getPath();

	String getName();

	boolean exists() throws IOException;

	boolean isDirectory() throws IOException;

	boolean mkdir() throws IOException;

	boolean createNewFile() throws IOException;

	boolean create(InputStream inputStream) throws IOException;

	boolean create(byte[] bytes) throws IOException;

	InputStream getInputStream() throws IOException;

	OutputStream getOutputStream(long size) throws IOException;

	void setContents(InputStream inputStream) throws IOException;

	void setContents(byte[] bytes) throws IOException;

	boolean rename(String newName) throws IOException;

	boolean delete() throws IOException;

}
