package org.eclipse.dltk.mod.utils;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.mod.utils.messages"; //$NON-NLS-1$
	public static String DeployHelper_failedToCreateFolderFor;
	public static String NatureExtensionManager_instantiantionError;
	public static String NatureExtensionManager_missingCategoryAttribute;
	public static String PlatformFileUtils_pathMustNotBeEmpty;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
