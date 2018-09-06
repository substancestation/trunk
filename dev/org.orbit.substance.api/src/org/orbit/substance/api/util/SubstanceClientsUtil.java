package org.orbit.substance.api.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfsvolume.DataBlockMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.model.dfs.FileContentAccess;
import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.model.dfs.Path;
import org.origin.common.io.ChecksumUtil;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;

public class SubstanceClientsUtil {

	public static Dfs Dfs = new Dfs();
	public static DfsVolume DfsVolume = new DfsVolume();

	public static class Dfs {
		public static FileMetadata[] EMPTY_FILES = new FileMetadata[0];

		/**
		 * 
		 * @param dfsClientResolver
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public ServiceMetadata getDfsMetadata(DfsClientResolver dfsClientResolver, String dfsServiceUrl, String accessToken) throws ClientException {
			ServiceMetadata metadata = null;
			DfsClient dfsClient = dfsClientResolver.resolve(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				metadata = dfsClient.getMetadata();
			}
			if (metadata == null) {
				metadata = new ServiceMetadataImpl();
			}
			return metadata;
		}

		/**
		 * 
		 * @param dfsClientResolver
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata[] listRoots(DfsClientResolver dfsClientResolver, String dfsServiceUrl, String accessToken) throws ClientException {
			FileMetadata[] rootFileMetadatas = null;
			DfsClient dfsClient = dfsClientResolver.resolve(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				rootFileMetadatas = dfsClient.listRoots();
			}
			if (rootFileMetadatas == null) {
				rootFileMetadatas = EMPTY_FILES;
			}
			return rootFileMetadatas;
		}

		/**
		 * 
		 * @param dfsClientResolver
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @param parentFileId
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata[] listFiles(DfsClientResolver dfsClientResolver, String dfsServiceUrl, String accessToken, String parentFileId) throws ClientException {
			FileMetadata[] memberFileMetadatas = null;
			DfsClient dfsClient = dfsClientResolver.resolve(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				memberFileMetadatas = dfsClient.listFiles(parentFileId);
			}
			if (memberFileMetadatas == null) {
				memberFileMetadatas = EMPTY_FILES;
			}
			return memberFileMetadatas;
		}

		/**
		 * 
		 * @param dfsClientResolver
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @param path
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata createNewFile(DfsClientResolver dfsClientResolver, String dfsServiceUrl, String accessToken, Path path) throws ClientException {
			FileMetadata fileMetadata = null;
			DfsClient dfsClient = dfsClientResolver.resolve(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				fileMetadata = dfsClient.createNewFile(path);
			}
			return fileMetadata;
		}

		/**
		 * 
		 * @param dfsClientResolver
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @param path
		 * @param localFile
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata createNewFile(DfsClientResolver dfsClientResolver, String dfsServiceUrl, String accessToken, Path path, java.io.File localFile) throws ClientException {
			FileMetadata fileMetadata = null;
			DfsClient dfsClient = dfsClientResolver.resolve(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				long size = localFile.length();
				fileMetadata = dfsClient.createNewFile(path, size);
			}
			return fileMetadata;
		}

		/**
		 * 
		 * @param dfsClientResolver
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @param parentFileId
		 * @param localFile
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata createNewFile(DfsClientResolver dfsClientResolver, String dfsServiceUrl, String accessToken, String parentFileId, java.io.File localFile) throws ClientException {
			FileMetadata fileMetadata = null;
			DfsClient dfsClient = dfsClientResolver.resolve(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				String fileName = localFile.getName();
				long size = localFile.length();
				fileMetadata = dfsClient.createNewFile(parentFileId, fileName, size);
			}
			return fileMetadata;
		}

		/**
		 * 
		 * @param dfsClientResolver
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @param fileId
		 * @param size
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata allocateVolumes(DfsClientResolver dfsClientResolver, String dfsServiceUrl, String accessToken, String fileId, long size) throws ClientException {
			FileMetadata file = null;
			DfsClient dfsClient = dfsClientResolver.resolve(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				file = dfsClient.allocateVolumes(fileId, size);
			}
			return file;
		}

		/**
		 * 
		 * @param dfsClientResolver
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @param fileId
		 * @param filePartsString
		 * @return
		 * @throws ClientException
		 */
		public boolean updateFileParts(DfsClientResolver dfsClientResolver, String dfsServiceUrl, String accessToken, String fileId, String filePartsString) throws ClientException {
			boolean isUpdated = false;
			DfsClient dfsClient = dfsClientResolver.resolve(dfsServiceUrl, accessToken);
			if (dfsClient != null && filePartsString != null) {
				isUpdated = dfsClient.updateFileParts(fileId, filePartsString);
			}
			return isUpdated;
		}

		/**
		 * 
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @return
		 */
		public DfsClient getDfsClient(String dfsServiceUrl, String accessToken) {
			DfsClient dfsClient = null;
			if (dfsServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, dfsServiceUrl);
				dfsClient = SubstanceClients.getInstance().getDfsClient(properties);
			}
			return dfsClient;
		}
	}

	public static class DfsVolume {
		public static final DataBlockMetadata[] EMPTY_DATA_BLOCKS = new DataBlockMetadata[0];
		public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

		/**
		 * 
		 * @param dfsVolumeClientResolver
		 * @param dfsVolumeServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public ServiceMetadata getDfsVolumeMetadata(DfsVolumeClientResolver dfsVolumeClientResolver, String dfsVolumeServiceUrl, String accessToken) throws ClientException {
			ServiceMetadata metadata = null;
			DfsVolumeClient dfsVolumeClient = dfsVolumeClientResolver.resolve(dfsVolumeServiceUrl, accessToken);
			if (dfsVolumeClient != null) {
				metadata = dfsVolumeClient.getMetadata();
			}
			if (metadata == null) {
				metadata = new ServiceMetadataImpl();
			}
			return metadata;
		}

		/**
		 * 
		 * @param dfsVolumeClientResolver
		 * @param dfsVolumeServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata[] listDataBlocks(DfsVolumeClientResolver dfsVolumeClientResolver, String dfsVolumeServiceUrl, String accessToken) throws ClientException {
			DataBlockMetadata[] datablocks = null;
			DfsVolumeClient dfsVolumeClient = dfsVolumeClientResolver.resolve(dfsVolumeServiceUrl, accessToken);
			if (dfsVolumeClient != null) {
				datablocks = dfsVolumeClient.getDataBlocks();
			}
			if (datablocks == null) {
				datablocks = EMPTY_DATA_BLOCKS;
			}
			return datablocks;
		}

		/**
		 * 
		 * @param dfsVolumeClientResolver
		 * @param dfsVolumeServiceUrl
		 * @param accessToken
		 * @param accountId
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata[] listDataBlocks(DfsVolumeClientResolver dfsVolumeClientResolver, String dfsVolumeServiceUrl, String accessToken, String accountId) throws ClientException {
			DataBlockMetadata[] datablocks = null;
			DfsVolumeClient dfsVolumeClient = dfsVolumeClientResolver.resolve(dfsVolumeServiceUrl, accessToken);
			if (dfsVolumeClient != null) {
				datablocks = dfsVolumeClient.getDataBlocks(accountId);
			}
			if (datablocks == null) {
				datablocks = EMPTY_DATA_BLOCKS;
			}
			return datablocks;
		}

		/**
		 * 
		 * @param dfsVolumeClientResolver
		 * @param dfsVolumeServiceUrl
		 * @param accessToken
		 * @param accountId
		 * @param blockId
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata getDataBlock(DfsVolumeClientResolver dfsVolumeClientResolver, String dfsVolumeServiceUrl, String accessToken, String accountId, String blockId) throws ClientException {
			DataBlockMetadata datablock = null;
			DfsVolumeClient dfsVolumeClient = dfsVolumeClientResolver.resolve(dfsVolumeServiceUrl, accessToken);
			if (dfsVolumeClient != null) {
				datablock = dfsVolumeClient.getDataBlock(accountId, blockId);
			}
			return datablock;
		}

		/**
		 * 
		 * @param dfsVolumeClientResolver
		 * @param dfsVolumeServiceUrl
		 * @param accessToken
		 * @param accountId
		 * @param capacity
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata createDataBlock(DfsVolumeClientResolver dfsVolumeClientResolver, String dfsVolumeServiceUrl, String accessToken, String accountId, long capacity) throws ClientException {
			DataBlockMetadata datablock = null;
			DfsVolumeClient dfsVolumeClient = dfsVolumeClientResolver.resolve(dfsVolumeServiceUrl, accessToken);
			if (dfsVolumeClient != null) {
				datablock = dfsVolumeClient.createDataBlock(accountId, capacity);
			}
			return datablock;
		}

		/**
		 * 
		 * @param dfsVolumeClientResolver
		 * @param dfsVolumeServiceUrl
		 * @param accessToken
		 * @param accountId
		 * @param blockId
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteDataBlock(DfsVolumeClientResolver dfsVolumeClientResolver, String dfsVolumeServiceUrl, String accessToken, String accountId, String blockId) throws ClientException {
			boolean succeed = false;
			DfsVolumeClient dfsVolumeClient = dfsVolumeClientResolver.resolve(dfsVolumeServiceUrl, accessToken);
			if (dfsVolumeClient != null) {
				succeed = dfsVolumeClient.deleteDataBlock(accountId, blockId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param dfsVolumeClientResolver
		 * @param accessToken
		 * @param fileMetadata
		 * @param localFile
		 * @return
		 * @throws ClientException
		 * @throws IOException
		 */
		public boolean uploadFile(DfsVolumeClientResolver dfsVolumeClientResolver, String accessToken, FileMetadata fileMetadata, File localFile) throws ClientException, IOException {
			if (fileMetadata == null) {
				throw new IllegalArgumentException("fileMetadata is null.");
			}
			if (localFile == null) {
				throw new IllegalArgumentException(" localFile is null.");
			}
			if (!localFile.exists()) {
				throw new IllegalArgumentException(" localFile doesn't exist.");
			}

			boolean isUploaded = false;

			String fileId = fileMetadata.getFileId();
			List<FilePart> fileParts = fileMetadata.getFileParts();
			if (fileParts.isEmpty()) {
				throw new ClientException(500, "File parts is empty. fileId='" + fileId + "'");
			}

			if (fileParts.size() == 1) {
				// The file has only one part.
				// - A data block has enough space to keep the whole file.
				// - Upload the file to a data block of a dfs volume.
				FilePart filePart = fileParts.get(0);

				byte[] partBytes = IOUtil.toByteArray(localFile);
				long partChecksum = ChecksumUtil.getChecksum(partBytes);

				boolean uploadSucceed = false;
				boolean uploadFailed = false;

				List<FileContentAccess> fileContentAccessList = filePart.getContentAccess();
				for (FileContentAccess fileContentAccess : fileContentAccessList) {
					try {
						String dfsId = fileContentAccess.getDfsId();
						String dfsVolumeId = fileContentAccess.getDfsVolumeId();
						String blockId = fileContentAccess.getDataBlockId();

						DfsVolumeClient dfsVolumeClient = dfsVolumeClientResolver.resolve(dfsId, dfsVolumeId, accessToken);
						if (dfsVolumeClient == null) {
							throw new IOException("DfsVolumeClient is not found. dfsId='" + dfsId + "', dfsVolumeId='" + dfsVolumeId + "'");
						}

						boolean currSucceed = dfsVolumeClient.uploadFile(null, blockId, fileId, partChecksum, localFile);
						if (currSucceed) {
							uploadSucceed = true;
						} else {
							uploadFailed = true;
						}
					} catch (IOException e) {
						throw e;
					}
				}

				isUploaded = (uploadSucceed && !uploadFailed) ? true : false;
				if (isUploaded) {
					filePart.setChecksum(partChecksum);
				}

			} else {
				// The file has multiple parts
				// - One data block (e.g. 100MB) doesn't have enough space to keep the whole file (e.g. 125MB or 275MB).
				// - Upload file parts to data blocks of various dfs volumes.
				FileInputStream fileInputStream = null;
				try {
					fileInputStream = new FileInputStream(localFile);
					byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

					for (FilePart filePart : fileParts) {
						int partId = filePart.getPartId();
						long startIndex = filePart.getStartIndex();
						long endIndex = filePart.getEndIndex();
						long currSize = endIndex - startIndex;
						long lengthLeft = currSize;

						ByteArrayOutputStream partOutputStream = null;
						try {
							partOutputStream = new ByteArrayOutputStream();

							int lengthToRead = buffer.length;
							if (lengthLeft < lengthToRead) {
								lengthToRead = (int) lengthLeft;
							}
							int read;
							while (lengthLeft > 0) {
								read = fileInputStream.read(buffer, 0, lengthToRead);
								if (read <= 0) {
									// end of stream
									break;
								}
								partOutputStream.write(buffer, 0, read);
								lengthLeft -= read;

								lengthToRead = buffer.length;
								if (lengthLeft < lengthToRead) {
									lengthToRead = (int) lengthLeft;
								}
							}

							byte[] partBytes = partOutputStream.toByteArray();
							long partChecksum = ChecksumUtil.getChecksum(partBytes);

							boolean uploadSucceed = false;
							boolean uploadFailed = false;

							List<FileContentAccess> fileContentAccessList = filePart.getContentAccess();
							for (FileContentAccess fileContentAccess : fileContentAccessList) {
								InputStream partInputStream = null;
								try {
									partInputStream = new ByteArrayInputStream(partBytes);

									String dfsId = fileContentAccess.getDfsId();
									String dfsVolumeId = fileContentAccess.getDfsVolumeId();
									String blockId = fileContentAccess.getDataBlockId();

									DfsVolumeClient dfsVolumeClient = dfsVolumeClientResolver.resolve(dfsId, dfsVolumeId, accessToken);
									if (dfsVolumeClient == null) {
										throw new IOException("DfsVolumeClient is not found. dfsId='" + dfsId + "', dfsVolumeId='" + dfsVolumeId + "'");
									}

									boolean currSucceed = dfsVolumeClient.uploadFile(null, blockId, fileId, partId, currSize, partChecksum, partInputStream);
									if (currSucceed) {
										uploadSucceed = true;
									} else {
										uploadFailed = true;
									}

								} catch (IOException e) {
									throw e;
								} finally {
									IOUtil.closeQuietly(partInputStream, true);
								}
							}

							isUploaded = (uploadSucceed && !uploadFailed) ? true : false;
							if (isUploaded) {
								filePart.setChecksum(partChecksum);
							}

						} finally {
							IOUtil.closeQuietly(partOutputStream, true);
						}
					} // FileParts loop

				} finally {
					IOUtil.closeQuietly(fileInputStream, true);
				}
			}

			return isUploaded;
		}

		/**
		 * 
		 * @param dfsVolumeServiceUrl
		 * @param accessToken
		 * @return
		 */
		public DfsVolumeClient getDfsVolumeClient(String dfsVolumeServiceUrl, String accessToken) {
			DfsVolumeClient dfsVolumeClient = null;
			if (dfsVolumeServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, dfsVolumeServiceUrl);
				dfsVolumeClient = SubstanceClients.getInstance().getDfsVolumeClient(properties);
			}
			return dfsVolumeClient;
		}
	}

}

// String dfsVolumeServiceUrl = DfsVolume.getDfsVolumeServiceUrl(dfsId, dfsVolumeId);
// if (dfsVolumeServiceUrl == null || dfsVolumeServiceUrl.isEmpty()) {
// throw new ClientException(500, "dfsVolumeServiceUrl is not found. dfsId='" + dfsId + "', dfsVolumeId='" + dfsVolumeId + "'");
// }
// DfsVolumeClient dfsVolumeClient = DfsVolume.getDfsVolumeClient(dfsVolumeServiceUrl, accessToken);

// String dfsVolumeServiceUrl = DfsVolume.getDfsVolumeServiceUrl(dfsId, dfsVolumeId);
// if (dfsVolumeServiceUrl == null || dfsVolumeServiceUrl.isEmpty()) {
// throw new ClientException(500, "dfsVolumeServiceUrl is not found. dfsId='" + dfsId + "', dfsVolumeId='" + dfsVolumeId + "'");
// }
// DfsVolumeClient dfsVolumeClient = DfsVolume.getDfsVolumeClient(dfsVolumeServiceUrl, accessToken);
// dfsVolumeClient.uploadFile(accountId, blockId, fileId, partId, checksum, inputStream);

// /**
// *
// * @param dfsServiceUrl
// * @param accessToken
// * @param parentFileId
// * @param localFiles
// * @return
// * @throws ClientException
// */
// public File uploadFile(String dfsServiceUrl, String accessToken, String parentFileId, List<java.io.File> localFiles) throws ClientException {
// File file = null;
// FileSystemClient dfsClient = getFileSystemClient(dfsServiceUrl, accessToken);
// if (dfsClient != null) {
// java.io.File localFile = (localFiles != null && !localFiles.isEmpty()) ? localFiles.get(0) : null;
// if (localFile != null && localFile.exists()) {
// file = dfsClient.uploadFile(parentFileId, localFile);
// }
// }
// return file;
// }

// long checksum = FileUtil.getChecksum(localFile);
