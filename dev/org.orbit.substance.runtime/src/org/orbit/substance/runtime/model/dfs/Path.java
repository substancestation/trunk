package org.orbit.substance.runtime.model.dfs;

import org.origin.common.util.StringUtil;

public class Path implements Comparable<Path> {

	public static final Path ROOT = new Path("/");

	// path separator
	public static final String SEPARATOR = "/";
	public static final char SEPARATOR_CHAR = '/';

	private static final String[] EMPTY_STRINGS = new String[] {};

	protected String pathString;

	/**
	 * 
	 * @param pathString
	 */
	public Path(String pathString) {
		if (pathString != null) {
			pathString = pathString.trim();
		}
		this.pathString = pathString;
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public Path(String parent, String child) {
		this(new Path(parent), new Path(child));
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public Path(Path parent, String child) {
		this(parent, new Path(child));
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public Path(String parent, Path child) {
		this(new Path(parent), child);
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public Path(Path parent, Path child) {
		String path1 = parent.getPathString();
		String path2 = child.getPathString();

		String newPath = path1;

		// path1 ends with "/" and path2 starts with "/" --- make sure there is only one "/" between then.
		if (path1.endsWith(SEPARATOR) && path2.startsWith(SEPARATOR)) {
			if (path2.length() > 1) {
				path2 = path2.substring(1);
			} else {
				// path2's length is either 1 or greater than 1. if not greater than 1, must be 1 --- the "/"
				path2 = "";
			}
		}
		if (!path1.endsWith(SEPARATOR) && !path2.startsWith(SEPARATOR)) {
			newPath += SEPARATOR;
		}

		newPath += path2;

		if (newPath != null) {
			newPath = newPath.trim();
		}
		this.pathString = newPath;
	}

	/**
	 * Get Path starting from path segment with specified start index (inclusive) and extend until the end of the path segment.
	 * 
	 * @param startIndex
	 *            start index of path segment, inclusive
	 * @return
	 */
	public Path getPath(int startIndex) {
		String pathString = getPathString(startIndex);
		if (pathString != null) {
			return new Path(pathString);
		}
		return null;
	}

	/**
	 * Get Path starting from path segment with specified start index (inclusive) and extend until the path segment with specified end index (exclusive).
	 * 
	 * @param startIndex
	 *            start index of path segment, inclusive
	 * @param endIndex,
	 *            end index of path segment, exclusive
	 * @return
	 */
	public Path getPath(int startIndex, int endIndex) {
		String pathString = getPathString(startIndex, endIndex);
		if (pathString != null) {
			return new Path(pathString);
		}
		return null;
	}

	/**
	 * Get the string path of this Path.
	 * 
	 * @return
	 */
	public String getPathString() {
		return this.pathString;
	}

	/**
	 * Get Path string starting from path segment with specified start index (inclusive) and extend until the end of the path segment.
	 * 
	 * @param startIndex
	 *            start index of path segment, inclusive
	 * @return
	 */
	public String getPathString(int startIndex) {
		if (isRoot() || isEmpty()) {
			return null;
		}

		String[] segments = getSegments();
		if (startIndex < 0 || startIndex >= segments.length) {
			System.err.println("Start index is invalid.");
			return null;
		}
		int endIndex = segments.length;
		return getPathString(startIndex, endIndex);
	}

	/**
	 * Get Path starting from path segment with specified start index (inclusive) and extend until the path segment with specified end index (exclusive).
	 * 
	 * @param startIndex
	 *            start index of path segment, inclusive
	 * @param endIndex,
	 *            end index of path segment, exclusive
	 * @return
	 */
	public String getPathString(int startIndex, int endIndex) {
		if (isRoot() || isEmpty()) {
			return null;
		}

		String[] segments = getSegments();

		if (startIndex < 0 || startIndex >= segments.length) {
			System.err.println("Start index is invalid.");
			return null;
		}
		if (endIndex <= startIndex || startIndex > segments.length) {
			System.err.println("End index is invalid.");
			return null;
		}

		StringBuilder sb = new StringBuilder();

		if (startIndex == 0 && this.pathString != null && this.pathString.startsWith(SEPARATOR)) {
			sb.append(SEPARATOR);
		}

		for (int i = startIndex; i < endIndex; i++) {
			String segment = segments[i];
			boolean isEndingSegment = (i == (endIndex - 1)) ? true : false;

			sb.append(segment);

			if (!isEndingSegment) {
				sb.append(SEPARATOR);
			}
		}

		return sb.toString();
	}

	/**
	 * Get the string path of the parent Path.
	 * 
	 * @return
	 */
	public String getParentPathString() {
		Path parentPath = getParent();
		if (parentPath != null) {
			return parentPath.getPathString();
		}
		return null;
	}

	/**
	 * Check whether a path is "/"
	 * 
	 * @return return true when the path is "/". return false otherwise.
	 */
	public boolean isRoot() {
		return (SEPARATOR.equals(this.pathString)) ? true : false;
	}

	/**
	 * Check whether a path is empty.
	 * 
	 * @return return true when the path has 0 path segments. return false when the path has 1 or more path segments.
	 */
	public boolean isEmpty() {
		return (getSegments().length == 0) ? true : false;
	}

	/**
	 * Get all the segments, separated by / in a path.
	 * 
	 * @return
	 */
	public String[] getSegments() {
		String[] segments = null;
		if (this.pathString != null) {
			String thePathString = this.pathString.trim();

			if (thePathString.isEmpty() || SEPARATOR.equals(thePathString)) {
				return EMPTY_STRINGS;
			}
			// remove starting "/"
			thePathString = StringUtil.removeStartingCharacters(thePathString, SEPARATOR);
			// remove ending "/"
			thePathString = StringUtil.removeEndingCharacters(thePathString, SEPARATOR);

			// split by "/"
			segments = thePathString.split(SEPARATOR);
		}
		if (segments == null) {
			segments = EMPTY_STRINGS;
		}
		return segments;
	}

	public String getLastSegment() {
		String[] segments = getSegments();
		if (segments != null && segments.length > 0) {
			return segments[segments.length - 1];
		}
		return null;
	}

	public Path getParent() {
		if (pathString.length() == 0) {
			// path is empty --- empty path --- doesn't have parent path.
			return null;
		}

		int lastSeparatorIndex = pathString.lastIndexOf(SEPARATOR_CHAR);
		if (lastSeparatorIndex == 0) {
			// path starts with "/" --- current path is contained by root path.
			return ROOT;
		}
		if (lastSeparatorIndex == -1) {
			// path does not contain "/" --- relative path --- doesn't have parent path.
			return null;
		}

		// now the lastSeparatorIndex must be greater than 1.
		return new Path(pathString.substring(0, lastSeparatorIndex));
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Path)) {
			return false;
		}
		Path that = (Path) o;
		return this.pathString.equals(that.pathString);
	}

	@Override
	public int hashCode() {
		return this.pathString.hashCode();
	}

	@Override
	public int compareTo(Path path) {
		return this.pathString.compareTo(path.pathString);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Path(pathString=").append(this.pathString);
		sb.append(")");
		return sb.toString();
	}

}
