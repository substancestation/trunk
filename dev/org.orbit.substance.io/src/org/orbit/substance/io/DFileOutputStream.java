package org.orbit.substance.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 */
public class DFileOutputStream extends PipedOutputStream {

	/**
	 * 
	 * @param file
	 * @throws IOException
	 */
	public DFileOutputStream(final DFile file) throws IOException {
		this(file, 0);
	}

	/**
	 * 
	 * @param file
	 * @param size
	 * @throws IOException
	 */
	public DFileOutputStream(final DFile file, final long size) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("file is null.");
		}
		if (!file.exists()) {
			throw new IOException("File doesn't exists.");
		}
		if (file.isDirectory()) {
			throw new IOException("File is directory.");
		}

		try {
			final DFS dfs = file.getDFS();
			final String fileId = file.getFileId();
			// final String dfsServiceUrl = dfs.getDfsServiceUrl();
			final String accessToken = dfs.getAccessToken();
			final DfsClientResolver dfsClientResolver = dfs.getDfsClientResolver();
			final DfsVolumeClientResolver dfsVolumeClientResolver = dfs.getDfsVolumeClientResolver();

			final PipedInputStream pipeInput = new PipedInputStream();
			connect(pipeInput);

			if (size > 0) {
				new Thread() {
					@Override
					public void run() {
						try {
							FileMetadata fileMetadata = SubstanceClientsUtil.DFS.allocateVolumes(dfsClientResolver, accessToken, fileId, size);
							SubstanceClientsUtil.DFS_VOLUME.upload(dfsVolumeClientResolver, accessToken, fileMetadata, pipeInput);

						} catch (Exception e) {
							e.printStackTrace();

						} finally {
							IOUtil.closeQuietly(pipeInput, true);
						}
					}
				}.start();

			} else {
				new Thread() {
					@Override
					public void run() {
						ByteArrayOutputStream byteOutput = null;
						ByteArrayInputStream byteInput = null;
						try {
							byteOutput = new ByteArrayOutputStream();
							IOUtil.copy(pipeInput, byteOutput);
							byte[] bytes = byteOutput.toByteArray();

							long length = bytes.length;
							FileMetadata fileMetadata = SubstanceClientsUtil.DFS.allocateVolumes(dfsClientResolver, accessToken, fileId, length);

							byteInput = new ByteArrayInputStream(bytes);
							SubstanceClientsUtil.DFS_VOLUME.upload(dfsVolumeClientResolver, accessToken, fileMetadata, byteInput);

						} catch (Exception e) {
							e.printStackTrace();

						} finally {
							IOUtil.closeQuietly(byteInput, true);
							IOUtil.closeQuietly(byteOutput, true);
							IOUtil.closeQuietly(pipeInput, true);
						}
					}
				}.start();
			}

		} catch (Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			throw new IOException(e);
		}
	}

}
