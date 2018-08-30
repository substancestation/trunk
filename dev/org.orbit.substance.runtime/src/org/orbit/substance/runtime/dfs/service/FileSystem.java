package org.orbit.substance.runtime.dfs.service;

import java.io.IOException;

import org.orbit.substance.runtime.model.dfs.FileMetadata;
import org.orbit.substance.runtime.model.dfs.Path;

public interface FileSystem {

	FileSystemService getFileSystemService();

	String getDfsId();

	String getAccountId();

	FileMetadata[] listRoots() throws IOException;

	FileMetadata[] listFiles(String parentFileId) throws IOException;

	FileMetadata[] listFiles(Path parentPath) throws IOException;

	FileMetadata[] listTrashFiles() throws IOException;

	FileMetadata getFile(String fileId) throws IOException;

	FileMetadata getFile(Path path) throws IOException;

	Path getPath(String fileId) throws IOException;

	boolean exists(String fileId) throws IOException;

	boolean exists(Path path) throws IOException;

	boolean isDirectory(Path path) throws IOException;

	FileMetadata createNewFile(Path path) throws IOException;

	FileMetadata mkdirs(Path path) throws IOException;

	FileMetadata moveToTrash(String fileId) throws IOException;

	FileMetadata moveToTrash(Path path) throws IOException;

	FileMetadata putBackFromTrash(String fileId) throws IOException;

	FileMetadata putBackFromTrash(Path path) throws IOException;

	boolean delete(String fileId) throws IOException;

	boolean delete(Path path) throws IOException;

}
