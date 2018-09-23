package org.orbit.substance.io;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.origin.common.io.IOUtil;

/**
 * A DFile output stream that uploads the content of a file on the fly.
 * 
 * @see com.osgi.example1.fs.client.api.FileRefOutputStream
 * 
 */
public class DFileOutputStream extends PipedOutputStream {

	/**
	 * 
	 * @param file
	 * @param size
	 * @throws IOException
	 */
	public DFileOutputStream(final DFile file, long size) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("file is null.");
		}
		if (!file.exists()) {
			throw new IOException("File doesn't exists.");
		}

		try {
			final DFS dfs = file.getDFS();
			final String fileId = file.getFileId();
			final String dfsServiceUrl = dfs.getDfsServiceUrl();
			final String accessToken = dfs.getAccessToken();
			final DfsClientResolver dfsClientResolver = dfs.getDfsClientResolver();
			final DfsVolumeClientResolver dfsVolumeClientResolver = dfs.getDfsVolumeClientResolver();

			final FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.getFile(dfsClientResolver, dfsServiceUrl, accessToken, fileId);
			if (fileMetadata.isDirectory()) {
				throw new IOException("File is directory.");
			}

			SubstanceClientsUtil.Dfs.allocateVolumes(dfsClientResolver, dfsServiceUrl, accessToken, fileId, size);

			final PipedInputStream pipeInput = new PipedInputStream();
			connect(pipeInput);
			new Thread() {
				@Override
				public void run() {
					try {
						SubstanceClientsUtil.DfsVolume.upload(dfsVolumeClientResolver, accessToken, fileMetadata, pipeInput);

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						IOUtil.closeQuietly(pipeInput, true);
					}
				}
			}.start();

		} catch (Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			throw new IOException(e);
		}
	}

}
