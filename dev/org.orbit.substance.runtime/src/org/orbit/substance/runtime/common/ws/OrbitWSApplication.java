package org.orbit.substance.runtime.common.ws;

import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.service.IWebService;

public class OrbitWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param webService
	 * @param feature
	 */
	public OrbitWSApplication(IWebService webService, int feature) {
		super(webService, feature);

		// if (hasFeature(OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER)) {
		// register(OrbitAuthTokenRequestFilter.class);
		// }

		// if (hasFeature(OrbitFeatureConstants.SET_COOKIE_RESPONSE_FILTER)) {
		// register(OrbitSetCookieResponseFilter.class);
		// }
	}

}
