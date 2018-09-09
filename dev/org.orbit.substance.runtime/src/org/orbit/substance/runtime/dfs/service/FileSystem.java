package org.orbit.substance.runtime.dfs.service;

import java.io.IOException;
import java.util.List;

import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.model.dfs.Path;

public interface FileSystem {

	DfsService getFileSystemService();

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

	boolean exists(String parentFileId, String fileName) throws IOException;

	boolean isDirectory(Path path) throws IOException;

	FileMetadata createDirectory(Path path) throws IOException;

	FileMetadata createDirectory(String parentFileId, String fileName) throws IOException;

	FileMetadata createNewFile(Path path, long size) throws IOException;

	FileMetadata createNewFile(String parentFileId, String fileName, long size) throws IOException;

	FileMetadata allocateVolumes(String fileId, long size) throws IOException;

	boolean updateFileParts(String fileId, List<FilePart> fileParts) throws IOException;

	FileMetadata mkdirs(Path path) throws IOException;

	FileMetadata moveToTrash(String fileId) throws IOException;

	FileMetadata moveToTrash(Path path) throws IOException;

	FileMetadata putBackFromTrash(String fileId) throws IOException;

	FileMetadata putBackFromTrash(Path path) throws IOException;

	boolean delete(String fileId) throws IOException;

	boolean delete(Path path) throws IOException;

}
