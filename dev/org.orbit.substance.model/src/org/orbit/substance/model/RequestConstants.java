package org.orbit.substance.model;

public interface RequestConstants {

	// ---------------------------------------------------------------
	// File system service request constants
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
	// File content service request constants
	// ---------------------------------------------------------------
	public static String GET_META_DATA = "get_metadata";

	public static String LIST_ALL_DATA_BLOCKS = "list_all_data_blocks";
	public static String LIST_DATA_BLOCKS = "list_data_blocks";
	public static String DATA_BLOCK_EXISTS = "data_block_exists";
	public static String GET_DATA_BLOCK = "get_data_block";
	public static String CREATE_DATA_BLOCK = "create_data_block";
	public static String DELETE_DATA_BLOCK = "delete_data_block";

	public static String LIST_FILE_CONTENTS = "list_file_contents";
	public static String FILE_CONTENT_EXISTS = "file_content_exists";
	public static String GET_FILE_CONTENT = "get_file_content";
	public static String DELETE_FILE_CONTENT = "delete_file_content";

}

// public static String GET_FILE_CONTENT = "get_file_content";
// public static String SET_FILE_CONTENT = "set_file_content";
