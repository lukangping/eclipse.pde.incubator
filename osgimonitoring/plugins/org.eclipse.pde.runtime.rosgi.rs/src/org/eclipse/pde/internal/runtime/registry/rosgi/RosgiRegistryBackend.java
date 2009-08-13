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

import java.net.InetAddress;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.IRemoteServiceReference;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.eclipse.ecf.remoteservice.util.tracker.IRemoteServiceTrackerCustomizer;
import org.eclipse.ecf.remoteservice.util.tracker.RemoteServiceTracker;
import org.eclipse.pde.runtime.core.model.BackendChangeListener;
import org.eclipse.pde.runtime.core.model.RegistryBackend;
import org.eclipse.pde.runtime.rosgi.rs.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import ch.ethz.iks.r_osgi.RemoteOSGiService;

public class RosgiRegistryBackend implements RegistryBackend, IRemoteServiceTrackerCustomizer {
	private static final String DEFAULT_PROTOCOL = "r-osgi://";
	private static final String DEFAULT_CONTAINER_TYPE = "ecf.r_osgi.peer";
	
	private BackendChangeListener backendChangeListener;
	private IRemoteServiceRegistration remoteBackendChangeListenerService;
	
	private IRosgiRegistryHost remoteRosgiHost;
	private IRemoteServiceReference hostServiceReference;
	
	private ServiceTracker containerManagerServiceTracker;
	private IContainer container;
	
	private RemoteServiceTracker remoteServiceTracker;
	private IRemoteServiceContainerAdapter containerAdapter;
	
	
	
	private String serverURI;
	
	public boolean connect(IProgressMonitor monitor) {
		try {
			IContainerManager containerManager = getContainerManagerService(); 
			if (container == null )container = containerManager.getContainerFactory().createContainer(DEFAULT_CONTAINER_TYPE);
			containerAdapter = (IRemoteServiceContainerAdapter) container.getAdapter(IRemoteServiceContainerAdapter.class); 

			IRemoteServiceReference[] hostReferences = 
				containerAdapter.getRemoteServiceReferences(
						IDFactory.getDefault().createID(Activator.getDefault().getContainer().getConnectNamespace(), serverURI), 
						IRosgiRegistryHost.class.getName(), null);
			
			hostServiceReference = hostReferences[0];
			remoteServiceTracker = new RemoteServiceTracker(containerAdapter, new ID[] {container.getID()}, hostServiceReference, this);
			remoteServiceTracker.open();
			
			Assert.isNotNull(hostReferences);
			Assert.isTrue(hostReferences.length > 0);

			IRemoteService remoteService = containerAdapter.getRemoteService(hostServiceReference);
			remoteRosgiHost = (IRosgiRegistryHost) remoteService.getProxy(); 
			
			remoteBackendChangeListenerService = containerAdapter.registerRemoteService(
					new String[] { BackendChangeListener.class.getName() }, backendChangeListener, null);
			
			String uri = DEFAULT_PROTOCOL + InetAddress.getLocalHost().getHostAddress() + ":";
			if (System.getProperty("ch.ethz.iks.r_osgi.port") != null) {
				remoteRosgiHost.setClientURI(uri + System.getProperty("ch.ethz.iks.r_osgi.port"));
			}
			else {
				ServiceReference rosgiReference = Activator.getDefault().getBundleContext().getServiceReference("ch.ethz.iks.r_osgi.RemoteOSGiService");
				RemoteOSGiService rosgi = (RemoteOSGiService) Activator.getDefault().getBundleContext().getService(rosgiReference);
				remoteRosgiHost.setClientURI(uri + rosgi.getListeningPort("r-osgi"));
			}
			
			if (remoteRosgiHost.connectRemoteBackendChangeListener() == false) {
				throw new Exception("Unable to connect with a server");
			}
			return true;

		} catch (Exception e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
			disconnect();
			return false;
		}
	}
	
	public void initializeBundles(IProgressMonitor monitor) {
		remoteRosgiHost.initializeBundles();
	}

	public void initializeExtensionPoints(IProgressMonitor monitor) {
		remoteRosgiHost.initializeExtensionPoints();
	}

	public void initializeServices(IProgressMonitor monitor) {
		remoteRosgiHost.initializeServices();
	}

	public void setEnabled(long id, boolean enabled) {
		remoteRosgiHost.setEnabled(id, enabled);
	}

	public void setRegistryListener(BackendChangeListener listener) {
		this.backendChangeListener = listener;
	}

	public void start(long id) {
		remoteRosgiHost.start(id);
	}

	public void stop(long id) {
		remoteRosgiHost.stop(id);
	}

	public String[] diagnose(long id) {
		return remoteRosgiHost.diagnose(id);
	}

	public void disconnect() {
		if (remoteRosgiHost != null) {
			remoteRosgiHost.disconnect();
			remoteRosgiHost = null;
		}
		if (remoteBackendChangeListenerService != null) {
			remoteBackendChangeListenerService.unregister();
			remoteBackendChangeListenerService = null;
		}
		
		if (containerManagerServiceTracker != null) {
			containerManagerServiceTracker.close();
			containerManagerServiceTracker = null;
		}
		if (container != null) {
			container.disconnect();
			container = null;
		}
	}
	
	private IContainerManager getContainerManagerService() {
		if (containerManagerServiceTracker == null) {
			BundleContext context = Activator.getDefault().getBundleContext();
			containerManagerServiceTracker = new ServiceTracker(context, IContainerManager.class.getName(), null);
			containerManagerServiceTracker.open();
		}
		return (IContainerManager) containerManagerServiceTracker.getService();
	}

	public void setURI(String uri) {
		this.serverURI = uri;
	}

	public String getURI() {
		return serverURI;
	}

	public IRemoteService addingService(IRemoteServiceReference reference) {
		if (reference.equals(hostServiceReference)) {
			return containerAdapter.getRemoteService(reference);
		}
		return null;
	}

	public void modifiedService(IRemoteServiceReference reference, IRemoteService remoteService) {
		// do nothing
	}

	public void removedService(IRemoteServiceReference reference, IRemoteService remoteService) {
		if (remoteBackendChangeListenerService != null && reference.equals(hostServiceReference)) {
			backendChangeListener.backendDisconnect();
			remoteRosgiHost = null;
			disconnect();
		}
			
	}
}
