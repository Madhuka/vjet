/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.mod.dbgp;

import java.net.URI;

import org.eclipse.dltk.mod.dbgp.exceptions.DbgpException;

public interface IDbgpSessionInfo {
	String getApplicationId();

	String getIdeKey();

	String getSession();

	String getThreadId();

	String getParentAppId();

	String getLanguage();

	URI getFileUri();
	
	DbgpException getError();
}
