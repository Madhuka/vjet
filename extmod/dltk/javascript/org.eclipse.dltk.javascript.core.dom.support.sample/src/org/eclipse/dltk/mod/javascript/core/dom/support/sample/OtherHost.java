package org.eclipse.dltk.mod.javascript.core.dom.support.sample;

import org.mozilla.mod.javascript.ScriptableObject;

public class OtherHost extends ScriptableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void jsFunction_GoodBuy() {
		System.out.println("Good buy");
	}

	public String jsGet_property0() {
		return "Property";
	}

	public String getClassName() {
		return "OtherHost";
	}
}
