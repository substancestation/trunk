package org.orbit.substance.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * @see org.apache.tika.fork.MemoryURLConnection
 * @see com.sun.webkit.network.about.AboutURLConnection
 * 
 */
public class DfsURLConnection extends URLConnection {

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
		// String accessToken = this.url.getUserInfo();
		// String dfsId = this.url.getHost();
		// String path = this.url.getPath();

		this.isConnected = true;
	}

	@Override
	public int getContentLength() {
		try {
			connect();
			// return record.contentLength;
			return 1;
		} catch (IOException ex) {
			// returning -1 means 'unknown length'
			return -1;
		}
	}

	@Override
	public InputStream getInputStream() throws IOException {
		connect();
		return null;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		connect();
		return null;
	}

	@Override
	public long getLastModified() {
		return super.getLastModified();
	}

}
