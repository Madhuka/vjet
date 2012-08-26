package org.eclipse.dltk.mod.debug.ui;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.IOConsole;

public class ScriptDebugConsole extends IOConsole {
	private ILaunch launch;

	public ILaunch getLaunch() {
		return launch;
	}

	public void setLaunch(ILaunch launch) {
		this.launch = launch;
	}

	public ScriptDebugConsole(String name, ImageDescriptor imageDescriptor,
			String encoding) {
		super(name, null, imageDescriptor, encoding, true);

		this.addPatternMatchListener(new ScriptDebugConsoleTraceTracker());
	}

	public void matcherFinished() {
		super.matcherFinished();
	}

	public void partitionerFinished() {
		super.partitionerFinished();
	}

}
