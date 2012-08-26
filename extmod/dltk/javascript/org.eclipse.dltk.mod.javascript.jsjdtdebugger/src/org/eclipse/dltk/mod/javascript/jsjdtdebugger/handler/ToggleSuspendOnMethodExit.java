package org.eclipse.dltk.mod.javascript.jsjdtdebugger.handler;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.dltk.mod.javascript.jsjdtdebugger.JavaScriptAndJdtDebuggerPlugin;
import org.eclipse.dltk.mod.javascript.jsjdtdebugger.preferences.IJavaScriptAndJdtDebuggerPreferenceConstants;

public class ToggleSuspendOnMethodExit extends
		AbstractTogglePreferenceKeyHandler {

	protected Preferences getPreferences() {
		return JavaScriptAndJdtDebuggerPlugin.getDefault()
				.getPluginPreferences();
	}

	protected String getKey() {
		return IJavaScriptAndJdtDebuggerPreferenceConstants.PREF_BREAK_ON_METHOD_EXIT;
	}

}