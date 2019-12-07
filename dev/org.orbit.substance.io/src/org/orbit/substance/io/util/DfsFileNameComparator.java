package org.orbit.substance.io.util;

import java.io.IOException;
import java.util.Comparator;

import org.orbit.substance.io.DFile;

public class DfsFileNameComparator implements Comparator<DFile> {

	protected boolean ASC = true;

	public DfsFileNameComparator() {
	}

	public boolean isASC() {
		return this.ASC;
	}

	public void setASC(boolean ASC) {
		this.ASC = ASC;
	}

	@Override
	public int compare(DFile f1, DFile f2) {
		String name1 = "";
		String name2 = "";
		try {
			name1 = f1.getName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			name2 = f2.getName();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (this.ASC) {
			return name1.compareTo(name2);
		} else {
			return name2.compareTo(name1);
		}
	}

}
