/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.runtime.core;

import org.eclipse.osgi.util.NLS;

public class PDERuntimeMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.pde.runtime.core.pderuntimeresources";//$NON-NLS-1$

	public static String MessageHelper_missing_optional_required_bundle;
	public static String MessageHelper_missing_required_bundle;
	public static String MessageHelper_missing_imported_package;
	public static String MessageHelper_missing_host;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, PDERuntimeMessages.class);
	}

}
