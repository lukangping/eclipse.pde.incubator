/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Wojciech Galanciak <wojciech.galanciak@gmail.com> - bug 282804, 283823
 *******************************************************************************/
package org.eclipse.pde.runtime.core.model;

import org.eclipse.core.runtime.IProgressMonitor;

public interface RegistryBackend {

	public boolean connect(IProgressMonitor monitor);

	public void disconnect();

	public void setEnabled(long id, boolean enabled);

	public void start(long id);

	public void stop(long id);

	public String[] diagnose(long id);

	public void initializeBundles(IProgressMonitor monitor);

	public void initializeExtensionPoints(IProgressMonitor monitor);

	public void setRegistryListener(BackendChangeListener listener);

	public void initializeServices(IProgressMonitor monitor);

	public void setURI(String uri);

	public String getURI();

}