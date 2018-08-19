package org.orbit.substance.runtime.dfs.filesystem.ws.command;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;

import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.editpolicy.ServiceAwareWSCommand;
import org.origin.common.rest.editpolicy.WSCommand;

public abstract class AbstractFileSystemCommand<SERVICE> extends ServiceAwareWSCommand<SERVICE> implements WSCommand {

	@Inject
	protected HttpHeaders httpHeaders;

	/**
	 * 
	 * @param serviceClass
	 */
	public AbstractFileSystemCommand(Class<SERVICE> serviceClass) {
		super(serviceClass);
	}

	protected HttpHeaders getHttpHeaders() {
		return this.httpHeaders;
	}

	protected String getAccessToken() {
		return OrbitTokenUtil.INSTANCE.getAccessToken(this.httpHeaders);
	}

}
