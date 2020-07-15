package org.orbit.substance.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.api.SubstanceAPIActivator;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.dfs.DfsServiceMetadata;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.io.impl.DFSImpl;
import org.orbit.substance.io.util.DfsClientResolverByDfsURL;
import org.orbit.substance.io.util.DfsVolumeClientResolverImpl;
import org.origin.common.resource.Path;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 */
public abstract class DFS {

	protected static Map<String, DFS> dfsMap = new HashMap<String, DFS>();

	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public synchronized static DFS getDefault(String accessToken) {
		String dfsServiceUrl = SubstanceAPIActivator.getInstance().getProperty(SubstanceConstants.ORBIT_DFS_URL);
		// String indexServiceUrl = SubstanceIOActivator.getInstance().getProperty(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		DFS dfs = get(dfsServiceUrl, accessToken);
		return dfs;
	}

	/**
	 * 
	 * @param dfsServiceUrl
	 * @param accessToken
	 * @return
	 */
	public synchronized static DFS get(String dfsServiceUrl, String accessToken) {
		String key = dfsServiceUrl + "|" + accessToken;
		DFS dfs = dfsMap.get(key);
		if (dfs == null) {
			dfs = new DFSImpl(dfsServiceUrl, accessToken);
			dfsMap.put(key, dfs);
		}
		return dfs;
	}

	// protected String dfsServiceUrl;
	// protected String indexServiceUrl;
	protected String accessToken;

	protected DfsClientResolver dfsClientResolver;
	protected DfsVolumeClientResolver dfsVolumeClientResolver;

	/**
	 * 
	 * @param dfsServiceUrl
	 * @param accessToken
	 */
	public DFS(String dfsServiceUrl, String accessToken) {
		if (dfsServiceUrl == null) {
			throw new IllegalArgumentException("dfsServiceUrl is null.");
		}
		// if (indexServiceUrl == null) {
		// throw new IllegalArgumentException("indexServiceUrl is null.");
		// }

		// this.dfsServiceUrl = dfsServiceUrl;
		this.accessToken = accessToken;

		this.dfsClientResolver = new DfsClientResolverByDfsURL(dfsServiceUrl);
		this.dfsVolumeClientResolver = new DfsVolumeClientResolverImpl();
	}

	// protected String getDfsServiceUrl() {
	// return this.dfsServiceUrl;
	// }

	// protected String getIndexServiceUrl() {
	// return this.indexServiceUrl;
	// }

	public String getAccessToken() {
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

	public abstract FileMetadata getFileMetadata(String pathString) throws IOException;

	public abstract FileMetadata getFileMetadata(Path path) throws IOException;

	public abstract DFile getFileById(String fileId) throws IOException;

	public abstract DFile getFile(String pathString) throws IOException;

	public abstract DFile getFile(Path path) throws IOException;

	public abstract boolean exists(Path path) throws IOException;

	public abstract boolean isDirectory(String fileId) throws IOException;

	public abstract FileMetadata mkdir(Path path, boolean createUniqueFolderIfExist) throws IOException;

	public abstract FileMetadata createNewFile(Path path, long size, boolean notifyEvent) throws IOException;

	public abstract FileMetadata create(Path path, InputStream inputStream) throws IOException;

	public abstract FileMetadata create(Path path, byte[] bytes) throws IOException;

	public abstract long getSize(String fileId) throws IOException;

	public abstract InputStream getInputStream(String fileId) throws IOException;

	/**
	 * Before writing data to the OutputStream, call SubstanceClientsUtil.DFS.allocateVolumes(dfsClientResolver, accessToken, fileId, size); first to allocate volumes.
	 * 
	 * @param fileId
	 * @return
	 * @throws IOException
	 */
	public abstract OutputStream getOutputStream(String fileId) throws IOException;

	public abstract OutputStream getOutputStream(String fileId, long size) throws IOException;

	public abstract void setContents(String fileId, InputStream inputStream) throws IOException;

	public abstract void setContents(String fileId, byte[] bytes) throws IOException;

	public abstract boolean rename(String fileId, String newName, boolean notifyEvent) throws IOException;

	public abstract boolean delete(String fileId) throws IOException;

	public abstract void addFileListener(DFileListener listener);

	public abstract void removeFileListener(DFileListener listener);

	public abstract List<DFileListener> getFileListeners();

	public abstract void notifyFileEvent(DFS dfs, int eventType, Object source, Object oldValue, Object newValue);

}

// public abstract String getDfsId() throws IOException;
// /**
// *
// * @param dfsServiceUrl
// * @param indexServiceUrl
// * @param accessToken
// * @return
// */
// public synchronized static DFS get(String dfsServiceUrl, String indexServiceUrl, String accessToken) {
// String key = dfsServiceUrl + "|" + indexServiceUrl + "|" + accessToken;
// DFS dfs = dfsMap.get(key);
// if (dfs == null) {
// dfs = new DFSImpl(dfsServiceUrl, indexServiceUrl, accessToken);
// dfsMap.put(key, dfs);
// }
// return dfs;
// }
