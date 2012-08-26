package org.eclipse.dltk.mod.console.ui.internal;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.mod.console.ui.internal.messages"; //$NON-NLS-1$
	public static String HTTPConsoleHyperlink_failedToInitializeBrowserFor;
	public static String HTTPConsoleHyperlink_failedToOpenInvalidUri;
	public static String ScriptConsoleViewer_scriptConsoleCommandHandler;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
