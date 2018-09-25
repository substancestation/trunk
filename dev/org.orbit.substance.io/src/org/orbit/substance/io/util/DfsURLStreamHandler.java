package org.orbit.substance.io.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.osgi.service.url.AbstractURLStreamHandlerService;

/**
 * 
 * @see org.apache.tika.fork.MemoryURLStreamHandler
 * @see org.apache.felix.fileinstall.internal.JarDirUrlHandler (A URL handler that can jar a directory on the fly)
 * 
 */
public class DfsURLStreamHandler extends AbstractURLStreamHandlerService {

	public static final String PROTOCOL = "dfs";

	@Override
	protected void parseURL(URL url, String spec, int start, int limit) {
		super.parseURL(url, spec, start, limit);
	}

	@Override
	public URLConnection openConnection(URL url) throws IOException {
		return new DfsURLConnection(url);
	}

}
