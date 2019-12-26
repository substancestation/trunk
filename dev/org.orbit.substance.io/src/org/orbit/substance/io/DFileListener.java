package org.orbit.substance.io;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 */
public interface DFileListener {

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	void onFileCreated(DfsEvent event) throws Exception;

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	void onFileModified(DfsEvent event) throws Exception;

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	void onFileRenamed(DfsEvent event) throws Exception;

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	void onFileMoved(DfsEvent event) throws Exception;

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	void onFileRefreshed(DfsEvent event) throws Exception;

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	void onFileDeleted(DfsEvent event) throws Exception;

}
