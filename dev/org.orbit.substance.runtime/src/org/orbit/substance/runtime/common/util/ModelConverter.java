package org.orbit.substance.runtime.common.util;

import org.orbit.substance.model.dfs.PathDTO;
import org.orbit.substance.runtime.model.dfs.Path;

public class ModelConverter {

	public static FILE_SYSTEM FILE_SYSTEM = new FILE_SYSTEM();

	public static class FILE_SYSTEM {
		/**
		 * 
		 * @param path
		 * @return
		 */
		public PathDTO toDTO(Path path) {
			String pathString = path.getPathString();

			PathDTO dto = new PathDTO();
			dto.setPathString(pathString);
			return dto;
		}
	}

}
