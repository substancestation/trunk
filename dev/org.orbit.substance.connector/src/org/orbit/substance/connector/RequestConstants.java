package org.orbit.substance.connector;

public interface RequestConstants {

	// ---------------------------------------------------------------
	// DFS metadata request constants
	// ---------------------------------------------------------------
	public static String LIST_ROOTS = "list_roots";
	public static String LIST_FILES = "list_files";
	public static String GET_FILE = "get_file";
	public static String EXISTS = "exists";
	public static String IS_DIRECTORY = "is_directory";
	public static String MKDIRS = "mkdirs";
	public static String CREATE_NEW_FILE = "create_new_file";
	public static String DELETE_FILE = "delete_file";
	public static String UPLOAD_FILE_TO_FILE = "upload_file_to_file";
	public static String UPLOAD_FILE_TO_DIRECTORY = "upload_file_to_directory";
	public static String DOWNLOAD_FILE = "download_file";

	// ---------------------------------------------------------------
	// DFS content request constants
	// ---------------------------------------------------------------
	public static String GET_FILE_CONTENT = "get_file_contant";
	public static String SET_FILE_CONTENT = "set_file_contant";

}
