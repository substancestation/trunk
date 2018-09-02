package org.orbit.substance.runtime.common.ws;

import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.editpolicy.ServiceAwareWSCommand;
import org.origin.common.rest.editpolicy.WSCommand;

public abstract class AbstractDfsVolumeWSCommand<SERVICE> extends ServiceAwareWSCommand<SERVICE> implements WSCommand {

	/**
	 * 
	 * @param serviceClass
	 */
	public AbstractDfsVolumeWSCommand(Class<SERVICE> serviceClass) {
		super(serviceClass);
	}

	protected String getAccessToken() {
		return OrbitTokenUtil.INSTANCE.getAccessToken(this.httpHeaders);
	}

}
