package org.orbit.substance.io;

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
	void onFileDeleted(DfsEvent event) throws Exception;

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	void onFileRefreshed(DfsEvent event) throws Exception;

}
