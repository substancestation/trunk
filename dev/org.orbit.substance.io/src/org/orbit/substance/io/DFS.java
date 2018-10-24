package org.orbit.substance.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.substance.api.Activator;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.dfs.DfsServiceMetadata;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.io.impl.DFSImpl;
import org.orbit.substance.io.util.DfsClientResolverImpl;
import org.orbit.substance.io.util.DfsVolumeClientResolverImpl;
import org.origin.common.resource.Path;

public abstract class DFS {

	protected static Map<String, DFS> dfsMap = new HashMap<String, DFS>();

	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public synchronized static DFS getDefault(String accessToken) {
		String dfsServiceUrl = Activator.getInstance().getProperty(SubstanceConstants.ORBIT_DFS_URL);
		String indexServiceUrl = Activator.getInstance().getProperty(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		DFS dfs = get(indexServiceUrl, dfsServiceUrl, accessToken);
		return dfs;
	}

	/**
	 * 
	 * @param dfsServiceUrl
	 * @param indexServiceUrl
	 * @param accessToken
	 * @return
	 */
	public synchronized static DFS get(String dfsServiceUrl, String indexServiceUrl, String accessToken) {
		String key = dfsServiceUrl + "|" + indexServiceUrl + "|" + accessToken;
		DFS dfs = dfsMap.get(key);
		if (dfs == null) {
			dfs = new DFSImpl(dfsServiceUrl, indexServiceUrl, accessToken);
			dfsMap.put(key, dfs);
		}
		return dfs;
	}

	protected String dfsServiceUrl;
	protected String indexServiceUrl;
	protected String accessToken;

	protected DfsClientResolver dfsClientResolver;
	protected DfsVolumeClientResolver dfsVolumeClientResolver;

	/**
	 * 
	 * @param dfsServiceUrl
	 * @param indexServiceUrl
	 * @param accessToken
	 */
	public DFS(String dfsServiceUrl, String indexServiceUrl, String accessToken) {
		if (dfsServiceUrl == null) {
			throw new IllegalArgumentException("dfsServiceUrl is null.");
		}
		if (indexServiceUrl == null) {
			throw new IllegalArgumentException("indexServiceUrl is null.");
		}

		this.dfsServiceUrl = dfsServiceUrl;
		this.indexServiceUrl = indexServiceUrl;
		this.accessToken = accessToken;

		this.dfsClientResolver = new DfsClientResolverImpl(indexServiceUrl);
		this.dfsVolumeClientResolver = new DfsVolumeClientResolverImpl(indexServiceUrl);
	}

	protected String getDfsServiceUrl() {
		return this.dfsServiceUrl;
	}

	protected String getIndexServiceUrl() {
		return this.indexServiceUrl;
	}

	protected String getAccessToken() {
		return this.accessToken;
	}

	public DfsClientResolver getDfsClientResolver() {
		return this.dfsClientResolver;
	}

	public DfsVolumeClientResolver getDfsVolumeClientResolver() {
		return this.dfsVolumeClientResolver;
	}

	public abstract DfsServiceMetadata getServiceMetadata() throws IOException;

	public abstract DFile[] listRoot() throws IOException;

	public abstract DFile[] listFiles(String parentFileId) throws IOException;

	public abstract DFile[] listFiles(Path parentPath) throws IOException;

	public abstract DFile getFileById(String fileId) throws IOException;

	public abstract DFile getFile(String pathString) throws IOException;

	public abstract DFile getFile(Path path) throws IOException;

	public abstract String getFileId(Path path) throws IOException;

	public abstract boolean exists(String fileId) throws IOException;

	public abstract boolean exists(Path path) throws IOException;

	public abstract boolean isDirectory(String fileId) throws IOException;

	public abstract FileMetadata mkdir(Path path) throws IOException;

	public abstract FileMetadata createNewFile(Path path, long size) throws IOException;

	public abstract FileMetadata create(Path path, InputStream inputStream) throws IOException;

	public abstract FileMetadata create(Path path, byte[] bytes) throws IOException;

	public abstract long getLength(String fileId) throws IOException;

	public abstract InputStream getInputStream(String fileId) throws IOException;

	public abstract OutputStream getOutputStream(String fileId) throws IOException;

	public abstract OutputStream getOutputStream(String fileId, long size) throws IOException;

	public abstract void setContents(String fileId, InputStream inputStream) throws IOException;

	public abstract void setContents(String fileId, byte[] bytes) throws IOException;

	public abstract boolean rename(String fileId, String newName) throws IOException;

	public abstract boolean delete(String fileId) throws IOException;

}

// public abstract String getDfsId() throws IOException;
