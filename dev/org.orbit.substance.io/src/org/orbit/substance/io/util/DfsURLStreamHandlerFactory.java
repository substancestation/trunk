package org.orbit.substance.io.util;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.url.URLStreamHandlerService;

/* 
 * Example:
 * URL url1 = new URL("dfs://dfs1/path/to/file.txt");
 * URL url2 = new URL("dfs://<accessToken>@dfs1/path/to/file.txt");
 * 
 * @see org.apache.tika.fork.MemoryURLStreamHandlerFactory
 * @see org.apache.catalina.webresources.TomcatURLStreamHandlerFactory
 */
public class DfsURLStreamHandlerFactory implements URLStreamHandlerFactory {

	public static DfsURLStreamHandlerFactory INSTANCE = new DfsURLStreamHandlerFactory();

	protected ServiceRegistration<?> urlHandlerRegistration;

	public void register() {
		URL.setURLStreamHandlerFactory(this);
	}

	public void register(BundleContext bundleContext) {
		register();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("url.handler.protocol", DfsURLStreamHandler.PROTOCOL);
		this.urlHandlerRegistration = bundleContext.registerService(URLStreamHandlerService.class.getName(), new DfsURLStreamHandler(), props);
	}

	public void unregister(BundleContext bundleContext) {
		if (this.urlHandlerRegistration != null) {
			this.urlHandlerRegistration.unregister();
			this.urlHandlerRegistration = null;
		}
	}

	@Override
	public URLStreamHandler createURLStreamHandler(String protocol) {
		if (DfsURLStreamHandler.PROTOCOL.equals(protocol)) {
			return new DfsURLStreamHandler();
		}
		return null;
	}

}
