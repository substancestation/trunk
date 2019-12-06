package org.orbit.substance.io.util;

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
		String name1 = f1.getName();
		String name2 = f2.getName();
		if (this.ASC) {
			return name1.compareTo(name2);
		} else {
			return name2.compareTo(name1);
		}
	}

}
