/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Wojciech Galanciak - bug 282672
 *******************************************************************************/
package org.eclipse.pde.internal.runtime.registry.model;

import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.core.runtime.*;

/**
 * Produces RegistryModels for URLs.
 *
 */
public class RegistryModelFactory {

	public static final String LOCAL = "local"; //$NON-NLS-1$

	/**
	 * 
	 * @param uri
	 * @return never returns null
	 */
	public static RegistryModel getRegistryModel(String s) {
		if (s == null)
			throw new IllegalArgumentException();
		URI uri = null;
		try {
			uri = new URI(s);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (LOCAL.equals(uri.getScheme()))
			return new RegistryModel(new LocalRegistryBackend());

		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.pde.runtime.backends"); //$NON-NLS-1$
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getName().equals("backend")) { //$NON-NLS-1$
				if (uri.getScheme().equals(elements[i].getAttribute("scheme"))) { //$NON-NLS-1$
					try {
						Object obj = elements[i].createExecutableExtension("class"); //$NON-NLS-1$
						if (obj instanceof RegistryBackend) {
							RegistryBackend backend = (RegistryBackend) obj;
							return new RegistryModel(backend);
						}
					} catch (CoreException e) {
						System.out.println(e);
					}
				}
			}
		}

		throw new UnsupportedOperationException();
	}
}
