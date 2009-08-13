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
package org.eclipse.pde.runtime.rosgi.rs;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.eclipse.pde.internal.runtime.registry.rosgi.IRosgiRegistryHost;
import org.eclipse.pde.internal.runtime.registry.rosgi.RosgiRegistryHost;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import ch.ethz.iks.r_osgi.RemoteOSGiService;

public class Activator extends Plugin {

	private RosgiRegistryHost host;
	private IContainer container;
	private IRemoteServiceRegistration serviceRegistration;
	private ServiceTracker containerManagerServiceTracker;
	private static Activator plugin;
	private BundleContext context;
	
	public static final String PLUGIN_ID = "org.eclipse.pde.runtime.rosgi.rs";
	
	public Activator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		this.context = context;
		initializeHost();
	}
	
	public boolean initializeHost() {
		host = new RosgiRegistryHost(); 
		IContainerManager containerManager = getContainerManagerService(); 
		try {
			container = containerManager.getContainerFactory().createContainer("ecf.r_osgi.peer");
		} catch (ContainerCreateException e) {
			getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
		}
		IRemoteServiceContainerAdapter containerAdapter = (IRemoteServiceContainerAdapter) container
				.getAdapter(IRemoteServiceContainerAdapter.class); 
		serviceRegistration = containerAdapter.registerRemoteService(
				new String[] { IRosgiRegistryHost.class.getName() }, host, null); 
		
		ServiceReference rosgiReference = Activator.getDefault().getBundleContext().getServiceReference("ch.ethz.iks.r_osgi.RemoteOSGiService");
		RemoteOSGiService rosgi = (RemoteOSGiService) Activator.getDefault().getBundleContext().getService(rosgiReference);
		getLog().log(new Status(IStatus.INFO, PLUGIN_ID, "org.eclipse.pde.runtime.core is listening on r-osgi protocol, port " + rosgi.getListeningPort("r-osgi")));
		return true;
	}
	
	private IContainerManager getContainerManagerService() {
		if (containerManagerServiceTracker == null) {
			containerManagerServiceTracker = new ServiceTracker(context,
					IContainerManager.class.getName(), null);
			containerManagerServiceTracker.open();
		}
		return (IContainerManager) containerManagerServiceTracker.getService();
	}
	
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		if (serviceRegistration != null) {
			serviceRegistration.unregister();
			serviceRegistration = null;
		}
		if (container != null) {
			container.disconnect();
			container = null;
		}
		if (containerManagerServiceTracker != null) {
			containerManagerServiceTracker.close();
			containerManagerServiceTracker = null;
		}
		super.stop(context);
	}
	
	public BundleContext getBundleContext() {
		return context;
	}

	public static Activator getDefault() {
		return plugin;
	}
	
	public IContainer getContainer() {
		return container;
	}

}
