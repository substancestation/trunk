package org.orbit.substance.runtime.common.ws;

import org.origin.common.rest.server.AbstractJerseyWSApplication;

public class OrbitWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param feature
	 */
	public OrbitWSApplication(String contextRoot, int feature) {
		super(contextRoot, feature);

		// if (hasFeature(OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER)) {
		// register(OrbitAuthTokenRequestFilter.class);
		// }

		// if (hasFeature(OrbitFeatureConstants.SET_COOKIE_RESPONSE_FILTER)) {
		// register(OrbitSetCookieResponseFilter.class);
		// }
	}

}
