package org.orbit.substance.api.dfsvolume;

import java.io.IOException;
import java.util.Comparator;

public interface DfsVolumeClientResolver {

	/**
	 * 
	 * @param dfsId
	 * @param dfsVolumeId
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	String getDfsVolumeServiceUrl(String dfsId, String dfsVolumeId, String accessToken) throws IOException;

	/**
	 * 
	 * @param dfsId
	 * @param accessToken
	 * @param comparator
	 * @return
	 * @throws IOException
	 */
	DfsVolumeClient[] resolve(String dfsId, String accessToken, Comparator<?> comparator) throws IOException;

	/**
	 * 
	 * @param dfsId
	 * @param dfsVolumeId
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	DfsVolumeClient resolve(String dfsId, String dfsVolumeId, String accessToken) throws IOException;

	/**
	 * 
	 * @param dfsVolumeServiceUrl
	 * @param accessToken
	 * @return
	 */
	DfsVolumeClient resolve(String dfsVolumeServiceUrl, String accessToken);

}
