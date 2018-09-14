package org.orbit.substance.model;

public interface RequestConstants {

	// ---------------------------------------------------------------
	// File system service request constants
	// ---------------------------------------------------------------
	public static String LIST_ROOTS = "list_roots";
	public static String LIST_FILES_BY_PARENT_ID = "list_files_by_parent_id";
	public static String LIST_FILES_BY_PARENT_PATH = "list_files_by_parent_path";
	public static String GET_FILE_BY_ID = "get_file_id";
	public static String GET_FILE_BY_PATH = "get_file_by_path";
	public static String GET_FILE_ID_BY_PATH = "get_file_id_by_path";
	public static String FILE_ID_EXISTS = "file_id_exists";
	public static String PATH_EXISTS = "path_exists";
	public static String IS_DIRECTORY = "is_directory";
	public static String CREATE_DIRECTORY = "create_directory";
	public static String CREATE_NEW_FILE = "create_new_file";
	public static String ALLOCATE_VOLUMES = "allocate_volumes";
	public static String UPDATE_FILE_PARTS = "update_file_parts";
	public static String MKDIRS = "mkdirs";
	public static String MOVE_TO_TRASH_BY_ID = "move_to_trash_by_id";
	public static String MOVE_TO_TRASH_BY_PATH = "move_to_trash_by_path";
	public static String PUT_BACK_FROM_TRASH_BY_ID = "put_back_from_trash_by_id";
	public static String PUT_BACK_FROM_TRASH_BY_PATH = "put_back_from_trash_by_path";
	public static String EMPTY_TRASH = "empty_trash";
	public static String DELETE_BY_ID = "delete_by_id";
	public static String DELETE_BY_PATH = "delete_by_path";

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
	public static String UPDATE_DATA_BLOCK_SIZE_BY_DELTA = "update_data_block_size_by_delta";
	public static String DELETE_DATA_BLOCK = "delete_data_block";

	public static String LIST_FILE_CONTENTS = "list_file_contents";
	public static String FILE_CONTENT_EXISTS = "file_content_exists";
	public static String GET_FILE_CONTENT = "get_file_content";
	public static String DELETE_FILE_CONTENT = "delete_file_content";

}

// public static String GET_FILE_CONTENT = "get_file_content";
// public static String SET_FILE_CONTENT = "set_file_content";

