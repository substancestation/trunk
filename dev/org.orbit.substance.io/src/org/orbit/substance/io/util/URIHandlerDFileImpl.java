package org.orbit.substance.io.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import org.orbit.substance.io.DFile;
import org.origin.common.resource.impl.URIConverterImpl;
import org.origin.common.resource.impl.URIHandlerImpl;

public class URIHandlerDFileImpl extends URIHandlerImpl {

	public static URIHandlerDFileImpl INSTANCE = new URIHandlerDFileImpl();

	public static final String PROTOCOL = "dfs";

	public void register() {
		if (!URIConverterImpl.DEFAULT_HANDLERS.contains(URIHandlerDFileImpl.INSTANCE)) {
			URIConverterImpl.DEFAULT_HANDLERS.add(URIHandlerDFileImpl.INSTANCE);
		}
	}

	public void unregister() {
		if (URIConverterImpl.DEFAULT_HANDLERS.contains(URIHandlerDFileImpl.INSTANCE)) {
			URIConverterImpl.DEFAULT_HANDLERS.remove(URIHandlerDFileImpl.INSTANCE);
		}
	}

	/**
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	protected DFile getFile(URI uri) throws IOException {
		return DfsIndexUtil.getFile(uri);
	}

	@Override
	public boolean isSupported(URI uri) {
		return (PROTOCOL.equalsIgnoreCase(uri.getScheme())) ? true : false;
	}

	@Override
	public boolean exists(URI uri, Map<?, ?> options) {
		boolean exists = false;
		try {
			DFile file = getFile(uri);
			if (file != null) {
				exists = file.exists();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return exists;
	}

	@Override
	public boolean createNewResource(URI uri, Map<Object, Object> options) throws IOException {
		boolean succeed = false;
		DFile file = getFile(uri);
		if (file != null) {
			succeed = file.createNewFile();
		}
		return succeed;
	}

	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		DFile file = getFile(uri);
		InputStream inputStream = null;
		if (file != null) {
			inputStream = file.getInputStream();
		}
		return inputStream;
	}

	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		final DFile file = getFile(uri);
		if (!file.exists()) {
			file.createNewFile();
		}
		OutputStream outputStream = null;
		if (file != null) {
			outputStream = file.getOutputStream();
		}
		return outputStream;
	}

	@Override
	public boolean delete(URI uri, Map<?, ?> options) throws IOException {
		boolean succeed = false;
		DFile file = getFile(uri);
		if (file != null) {
			succeed = file.delete();
		}
		return succeed;
	}

}
