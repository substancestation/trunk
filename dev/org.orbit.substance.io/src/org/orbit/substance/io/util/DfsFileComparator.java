package org.orbit.substance.io.util;

import java.io.IOException;
import java.util.Comparator;

import org.orbit.substance.io.DFile;

public class DfsFileComparator implements Comparator<Object> {

	protected boolean ASC = true;

	public DfsFileComparator() {
	}

	public boolean isASC() {
		return this.ASC;
	}

	public void setASC(boolean ASC) {
		this.ASC = ASC;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		if (obj1 instanceof DFile && obj2 instanceof DFile) {
			DFile f1 = (DFile) obj1;
			DFile f2 = (DFile) obj2;

			String type1 = "";
			String type2 = "";
			String name1 = "";
			String name2 = "";
			try {
				type1 = f1.isDirectory() ? "d" : "f";
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				type2 = f2.isDirectory() ? "d" : "f";
			} catch (IOException e) {
				e.printStackTrace();
			}
			name1 = f1.getName();
			name2 = f2.getName();

			if (type1.compareTo(type2) == 0) {
				if (this.ASC) {
					return name1.compareTo(name2);
				} else {
					return name2.compareTo(name1);
				}
			} else {
				return type1.compareTo(type2);
			}
		}
		return 0;
	}

}
