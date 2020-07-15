package org.orbit.substance.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.resource.Path;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 */
public interface DFile extends IAdaptable {

	void resolveMetadata(boolean reset) throws IOException;

	URI toURI() throws IOException;

	DFS getDFS();

	Path getPath();

	DFile getParent() throws IOException;

	String getFileId() throws IOException;

	String getName();

	String getFileExtension() throws IOException;

	boolean exists() throws IOException;

	boolean isDirectory() throws IOException;

	boolean mkdir(boolean createUniqueFolderIfExist) throws IOException;

	boolean createNewFile() throws IOException;

	boolean create(InputStream inputStream) throws IOException;

	boolean create(byte[] bytes) throws IOException;

	long getSize() throws IOException;

	InputStream getInputStream() throws IOException;

	/**
	 * Before writing data to the OutputStream, call SubstanceClientsUtil.DFS.allocateVolumes(dfsClientResolver, accessToken, fileId, size); first to allocate volumes.
	 * 
	 * @return
	 * @throws IOException
	 */
	OutputStream getOutputStream() throws IOException;

	OutputStream getOutputStream(long size) throws IOException;

	void setContents(InputStream inputStream) throws IOException;

	void setContents(byte[] bytes) throws IOException;

	boolean rename(String newName) throws IOException;

	boolean delete() throws IOException;

	DFile[] listFiles() throws IOException;

	long getDateCreated() throws IOException;

	long getDateModified() throws IOException;

}
