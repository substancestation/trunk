package org.orbit.substance.io.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.substance.io.Activator;
import org.orbit.substance.io.DFS;
import org.orbit.substance.io.DFile;

/**
 * 
 * @see org.apache.tika.fork.MemoryURLConnection
 * @see com.sun.webkit.network.about.AboutURLConnection
 * 
 */
public class DfsURLConnection extends URLConnection {

	protected DFile file;
	protected boolean isConnected;

	/**
	 * 
	 * @param url
	 */
	protected DfsURLConnection(URL url) {
		super(url);
	}

	@Override
	public void connect() throws IOException {
		this.file = DfsIndexUtil.getFile(this.url);
		this.isConnected = (this.file != null) ? true : false;
	}

	@Override
	public int getContentLength() {
		int length = 0;
		try {
			connect();
			if (this.isConnected) {
				length = (int) this.file.getLength();
			}
		} catch (IOException ex) {
			// means 'unknown length'
			length = -1;
		}
		return length;
	}

	@Override
	public long getContentLengthLong() {
		long length = 0;
		try {
			connect();
			if (this.isConnected) {
				length = this.file.getLength();
			}
		} catch (IOException ex) {
			// means 'unknown length'
			length = -1;
		}
		return length;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		InputStream inputStream = null;

		connect();
		if (this.isConnected) {
			if (this.file.exists() && !this.file.isDirectory()) {
				inputStream = file.getInputStream();
			}
		}

		return inputStream;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		OutputStream outputStream = null;

		connect();
		if (this.isConnected) {
			if (this.file.exists() && !this.file.isDirectory()) {
				outputStream = file.getOutputStream();
			}
		}

		return outputStream;
	}

	@Override
	public long getLastModified() {
		return super.getLastModified();
	}

}
