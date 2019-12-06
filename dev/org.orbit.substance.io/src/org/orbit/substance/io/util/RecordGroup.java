package org.orbit.substance.io.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class RecordGroup {

	private String owner;
	private Integer startRow;
	private Integer recordCount;

	public RecordGroup() {
	}

	public RecordGroup(String owner, Integer startRow, Integer recordCount) {
		this.owner = owner;
		this.startRow = startRow;
		this.recordCount = recordCount;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	// @Override
	// public String toString() {
	// return owner + " , " + startRow + "\n";
	// }

	public static void main(String a[]) {
		List<RecordGroup> list = new ArrayList<RecordGroup>();
		list.add(new RecordGroup("RECORD", 1, 6));
		list.add(new RecordGroup("RECORD", 7, 9));
		list.add(new RecordGroup("RECORD", 3, 4));
		list.add(new RecordGroup("ZONE", 3, 1));
		list.add(new RecordGroup("MODULE", 5, 6));
		list.add(new RecordGroup("ZONE", 14, 28));
		list.add(new RecordGroup("ZONE", 6, 30));
		list.add(new RecordGroup("MODULE", 1, 60));
		list.add(new RecordGroup("OFFICE", 2, 4));
		list.add(new RecordGroup("OFFICE", 8, 6));
		list.add(new RecordGroup("USER", 1, 6));
		list.add(new RecordGroup("USER", 9, 8));
		list.add(new RecordGroup("USER", 5, 7));
		list.add(new RecordGroup("OFFICE", 3, 1));

		RecordGroup[] array = list.toArray(new RecordGroup[list.size()]);

		Comparator<RecordGroup> comparator = new Comparator<RecordGroup>() {
			@Override
			public int compare(RecordGroup o1, RecordGroup o2) {
				if (o1.getOwner().compareTo(o2.getOwner()) == 0) {
					return o1.getStartRow() - o2.getStartRow();
				} else {
					return o1.getOwner().compareTo(o2.getOwner());
				}
			}
		};

		System.out.println("------------------------------------------------------");
		Collections.sort(list, comparator);
		System.out.println(list);

		System.out.println("------------------------------------------------------");
		Arrays.sort(array, comparator);
		for (RecordGroup record : array) {
			System.out.println(record.getOwner() + "," + record.getStartRow());
		}
	}

}
