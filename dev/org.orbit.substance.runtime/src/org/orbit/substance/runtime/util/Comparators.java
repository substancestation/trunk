package org.orbit.substance.runtime.util;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.dfsvolume.DfsVolumeMetadata;
import org.orbit.substance.runtime.SubstanceConstants;
import org.origin.common.util.BaseComparator;

public class Comparators {

	public static DfsVolumeIndexItemComparatorByVolumeId DfsVolumeIndexItemComparatorByVolumeId_ASC = new DfsVolumeIndexItemComparatorByVolumeId(BaseComparator.SORT_ASC);
	public static DfsVolumeIndexItemComparatorByVolumeId DfsVolumeIndexItemComparatorByVolumeId_DESC = new DfsVolumeIndexItemComparatorByVolumeId(BaseComparator.SORT_DESC);

	public static class DfsVolumeClientComparatorByFreeSpace extends BaseComparator<DfsVolumeClient> {
		protected Map<DfsVolumeClient, DfsVolumeMetadata> serviceMetadataMap;

		public DfsVolumeClientComparatorByFreeSpace(Map<DfsVolumeClient, DfsVolumeMetadata> serviceMetadataMap) {
			this.serviceMetadataMap = serviceMetadataMap;
		}

		@Override
		public int compare(DfsVolumeClient client1, DfsVolumeClient client2) {
			DfsVolumeMetadata metadata1 = this.serviceMetadataMap.get(client1);
			DfsVolumeMetadata metadata2 = this.serviceMetadataMap.get(client2);

			long freeSpace1 = 0;
			long freeSpace2 = 0;

			if (metadata1 != null) {
				long capacity1 = metadata1.getVolumeCapacity();
				long size1 = metadata1.getVolumeSize();
				freeSpace1 = capacity1 - size1;
			}

			if (metadata2 != null) {
				long capacity2 = metadata2.getVolumeCapacity();
				long size2 = metadata2.getVolumeSize();
				freeSpace2 = capacity2 - size2;
			}

			if (freeSpace1 > freeSpace2) {
				return -1;
			} else if (freeSpace2 > freeSpace1) {
				return 1;
			}
			return 0;
		}

	}

	public static class DfsVolumeIndexItemComparatorByVolumeId extends BaseComparator<IndexItem> {

		public DfsVolumeIndexItemComparatorByVolumeId(String sort) {
			super(sort);
		}

		@Override
		public int compare(IndexItem indexItem1, IndexItem indexItem2) {
			String dfsId1 = (String) indexItem1.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID);
			String dfsVolumeId1 = (String) indexItem1.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID);

			String dfsId2 = (String) indexItem2.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID);
			String dfsVolumeId2 = (String) indexItem2.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID);

			if (dfsId1 == null) {
				dfsId1 = "";
			}
			if (dfsVolumeId1 == null) {
				dfsVolumeId1 = "";
			}

			if (dfsId2 == null) {
				dfsId2 = "";
			}
			if (dfsVolumeId2 == null) {
				dfsVolumeId2 = "";
			}

			if (!dfsId1.equals(dfsId2)) {
				if (desc()) {
					return dfsId2.compareTo(dfsId1);
				} else {
					return dfsId1.compareTo(dfsId2);
				}
			}

			if (desc()) {
				return dfsVolumeId2.compareTo(dfsVolumeId1);
			} else {
				return dfsVolumeId1.compareTo(dfsVolumeId2);
			}
		}
	}

}

// /**
// *
// * @param list
// * @param comparator
// */
// public static void sort(List<IndexItem> list, Comparator<IndexItem> comparator) {
// if (list != null && list.size() > 1 && comparator != null) {
// Collections.sort(list, comparator);
// }
// }

// /**
// *
// * @param dfsVolumeClients
// * @param comparator
// */
// public static void sort(DfsVolumeClient[] dfsVolumeClients, Comparator<DfsVolumeClient> comparator) {
// if (dfsVolumeClients != null && dfsVolumeClients.length > 1 && comparator != null) {
// Arrays.sort(dfsVolumeClients, comparator);
// }
// }
