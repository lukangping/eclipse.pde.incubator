package org.eclipse.pde.internal.runtime.registry.rosgi;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.IRemoteServiceReference;
import org.eclipse.pde.runtime.core.model.BackendChangeListener;
import org.eclipse.pde.runtime.core.model.LocalRegistryBackend;
import org.eclipse.pde.runtime.rosgi.rs.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;



public class RosgiRegistryHost implements IRosgiRegistryHost {

	private static final String DEFAUT_CLIENT_HOST = "localhost:9279";
	private static final String DEFAULT_PROTOCOL = "r-osgi://";
	private static final String DEFAULT_CONTAINER_TYPE = "ecf.r_osgi.peer";
	
	private LocalRegistryBackend backend;
	private IContainer container;
	private ServiceTracker containerManagerServiceTracker;
	
	public RosgiRegistryHost() {
		backend = new LocalRegistryBackend();
		backend.connect(new NullProgressMonitor());
	}

	public String[] diagnose(long id) {
			return backend.diagnose(id);
	}

	public void initializeBundles() {
			backend.initializeBundles(new NullProgressMonitor());
	}

	public void initializeExtensionPoints() {
			backend.initializeExtensionPoints(new NullProgressMonitor());
	}

	public void initializeServices() {
			backend.initializeServices(new NullProgressMonitor());
	}

	public void setEnabled(long id, boolean enabled) {
			backend.setEnabled(id, enabled);
	}

	public void start(long id) {
			backend.start(id);
	}

	public void stop(long id) {
			backend.stop(id);
	}

	public void setBackendChangeListner(BackendChangeListener listener) {
			backend.setRegistryListener(listener);
	}

	public boolean connectRemoteBackendChangeListener() {
		try {
			IContainerManager containerManager = getContainerManagerService(); 
			container = containerManager.getContainerFactory().createContainer(DEFAULT_CONTAINER_TYPE);
			
			IRemoteServiceContainerAdapter containerAdapter = 
				(IRemoteServiceContainerAdapter) container.getAdapter(IRemoteServiceContainerAdapter.class);
			
			String target = DEFAULT_PROTOCOL;
			if (System.getProperty("client.host") != null)
				target += System.getProperty("client.host");
			else 
				target += DEFAUT_CLIENT_HOST;
			
			IRemoteServiceReference[] listenerReferences = 
				containerAdapter.getRemoteServiceReferences(
						IDFactory.getDefault().createID(container.getConnectNamespace(), target), 
						BackendChangeListener.class.getName(), null);
			
			Assert.isNotNull(listenerReferences);
			Assert.isTrue(listenerReferences.length > 0);
		
			IRemoteService remoteService = containerAdapter.getRemoteService(listenerReferences[0]);
			BackendChangeListener listener = (BackendChangeListener) remoteService.getProxy(); 
			setBackendChangeListner(listener);

			return true;

		} catch (Exception e) {
			disconnect();
		}

		return false;
	}

	public void disconnect() {
		if (container != null) {
			container.disconnect();
			container = null;
		}
		if (containerManagerServiceTracker != null) {
			containerManagerServiceTracker.close();
			containerManagerServiceTracker = null;
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
}
