package org.orbit.substance.runtime.dfs.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.dfs.service.FileSystemService;

public class FileSystemServiceTimer extends ServiceIndexTimer<FileSystemService> {

	protected FileSystemService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public FileSystemServiceTimer(IndexProvider indexProvider, FileSystemService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public synchronized FileSystemService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, FileSystemService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(SubstanceConstants.IDX__DFS__INDEXER_ID, SubstanceConstants.IDX__DFS__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, FileSystemService service) throws IOException {
		String dfsId = service.getDfsId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS__ID, dfsId);
		props.put(SubstanceConstants.IDX_PROP__DFS__NAME, name);
		props.put(SubstanceConstants.IDX_PROP__DFS__HOST_URL, hostURL);
		props.put(SubstanceConstants.IDX_PROP__DFS__CONTEXT_ROOT, contextRoot);
		props.put(SubstanceConstants.IDX_PROP__LAST_HEARTBEAT_TIME, now);

		return indexProvider.addIndexItem(SubstanceConstants.IDX__DFS__INDEXER_ID, SubstanceConstants.IDX__DFS__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, FileSystemService service, IndexItem indexItem) throws IOException {
		String dfsId = service.getDfsId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		Date now = new Date();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS__ID, dfsId);
		props.put(SubstanceConstants.IDX_PROP__DFS__NAME, name);
		props.put(SubstanceConstants.IDX_PROP__DFS__HOST_URL, hostURL);
		props.put(SubstanceConstants.IDX_PROP__DFS__CONTEXT_ROOT, contextRoot);
		props.put(SubstanceConstants.IDX_PROP__LAST_HEARTBEAT_TIME, now);

		indexProvider.setProperties(SubstanceConstants.IDX__DFS__INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(SubstanceConstants.IDX__DFS__INDEXER_ID, indexItemId);
	}

}
