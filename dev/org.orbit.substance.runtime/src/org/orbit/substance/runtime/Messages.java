package org.orbit.substance.runtime;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BASE_NAME = "org.orbit.substance.runtime.messages"; //$NON-NLS-1$

	static {
		NLS.initializeMessages(BASE_NAME, Messages.class);
	}

	private Messages() {
	}

	public static String ACCOUNT_AND_BLOCK_NOT_MATCH;

}
