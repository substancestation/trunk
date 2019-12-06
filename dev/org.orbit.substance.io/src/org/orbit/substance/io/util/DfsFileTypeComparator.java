package org.orbit.substance.io.util;

import java.io.IOException;
import java.util.Comparator;

import org.orbit.substance.io.DFile;

public class DfsFileTypeComparator implements Comparator<DFile> {

	@Override
	public int compare(DFile f1, DFile f2) {
		boolean isDir1 = false;
		boolean isDir2 = false;
		try {
			isDir1 = f1.isDirectory();
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
		try {
			isDir2 = f2.isDirectory();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}

		if (isDir1 == isDir2) {
			return 0;
		}
		if (isDir1) {
			return -1;
		} else {
			return 1;
		}
	}

}
