package org.orbit.substance.runtime.common.ws;

import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.editpolicy.ServiceAwareWSCommand;
import org.origin.common.rest.editpolicy.WSCommand;

public abstract class AbstractDfsCommand<SERVICE> extends ServiceAwareWSCommand<SERVICE> implements WSCommand {

	/**
	 * 
	 * @param serviceClass
	 */
	public AbstractDfsCommand(Class<SERVICE> serviceClass) {
		super(serviceClass);
	}

	protected String getAccessToken() {
		return OrbitTokenUtil.INSTANCE.getAccessToken(this.httpHeaders);
	}

	protected String getAccountId() {
		return OrbitTokenUtil.INSTANCE.getAccountId(this.httpHeaders, PlatformConstants.TOKEN_PROVIDER__ORBIT);
	}

}
