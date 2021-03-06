package org.orbit.substance.io;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.origin.common.util.IOUtil;

/**
 * A DFile input stream that downloads the content of a file on the fly.
 *
 * @see com.osgi.example1.fs.client.api.FileRefInputStream
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 */
public class DFileInputStream extends PipedInputStream {

	/**
	 * 
	 * @param file
	 * @throws IOException
	 */
	public DFileInputStream(final DFile file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("file is null.");
		}
		if (!file.exists()) {
			throw new IOException("File doesn't exists.");
		}

		try {
			final DFS dfs = file.getDFS();
			final String fileId = file.getFileId();
			final String accessToken = dfs.getAccessToken();
			final DfsClientResolver dfsClientResolver = dfs.getDfsClientResolver();
			final DfsVolumeClientResolver dfsVolumeClientResolver = dfs.getDfsVolumeClientResolver();

			final FileMetadata fileMetadata = SubstanceClientsUtil.DFS.getFile(dfsClientResolver, accessToken, fileId);
			if (fileMetadata.isDirectory()) {
				throw new IOException("File is directory.");
			}

			final PipedOutputStream pipeOutput = new PipedOutputStream();
			connect(pipeOutput);
			new Thread() {
				@Override
				public void run() {
					try {
						SubstanceClientsUtil.DFS_VOLUME.download(dfsVolumeClientResolver, accessToken, fileMetadata, pipeOutput);

					} catch (Exception e) {
						e.printStackTrace();

					} finally {
						IOUtil.closeQuietly(pipeOutput, true);
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
