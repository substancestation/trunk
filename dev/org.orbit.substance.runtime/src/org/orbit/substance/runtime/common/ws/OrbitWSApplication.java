package org.orbit.substance.runtime.common.ws;

import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.service.WebServiceAware;

public class OrbitWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param webServiceAware
	 * @param feature
	 */
	public OrbitWSApplication(WebServiceAware webServiceAware, int feature) {
		super(webServiceAware, feature);

		// if (hasFeature(OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER)) {
		// register(OrbitAuthTokenRequestFilter.class);
		// }

		// if (hasFeature(OrbitFeatureConstants.SET_COOKIE_RESPONSE_FILTER)) {
		// register(OrbitSetCookieResponseFilter.class);
		// }
	}

}
