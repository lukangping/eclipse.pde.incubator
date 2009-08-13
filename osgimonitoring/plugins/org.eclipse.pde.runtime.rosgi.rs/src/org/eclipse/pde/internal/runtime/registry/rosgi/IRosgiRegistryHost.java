/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.internal.runtime.registry.rosgi;

public interface IRosgiRegistryHost {
	
	boolean connectRemoteBackendChangeListener();
	
	void setEnabled(long id, boolean enabled);

	void start(long id);

	void stop(long id);

	String[] diagnose(long id);

	void initializeBundles();

	void initializeExtensionPoints();

	void initializeServices();
	
	void disconnect();
	
	void setClientURI(String uri);
}
