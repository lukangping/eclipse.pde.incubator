package org.eclipse.pde.internal.runtime.registry.rosgi;

import java.net.InetAddress;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.content.IContentTypeManager.ISelectionPolicy;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.IRemoteServiceReference;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.eclipse.pde.runtime.core.model.BackendChangeListener;
import org.eclipse.pde.runtime.core.model.RegistryBackend;
import org.eclipse.pde.runtime.rosgi.rs.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.Remoting;

public class RosgiRegistryBackend implements RegistryBackend {
	private static final String DEFAULT_PORT = "9278";
	private static final String DEFAULT_PROTOCOL = "r-osgi://";
	private static final String DEFAULT_CONTAINER_TYPE = "ecf.r_osgi.peer";
	
	private IRemoteServiceRegistration remoteBackendChangeListenerService;
	private IRosgiRegistryHost remoteRosgiHost;
	private BackendChangeListener backendChangeListener;
	private ServiceTracker containerManagerServiceTracker;
	private IContainer container;
	
	private String serverURI;
	
	public boolean connect(IProgressMonitor monitor) {
		try {
			IContainerManager containerManager = getContainerManagerService(); 
			container = containerManager.getContainerFactory().createContainer(DEFAULT_CONTAINER_TYPE);
			IRemoteServiceContainerAdapter containerAdapter = 
				(IRemoteServiceContainerAdapter) container.getAdapter(IRemoteServiceContainerAdapter.class); 

			IRemoteServiceReference[] helloReferences = 
				containerAdapter.getRemoteServiceReferences(
						IDFactory.getDefault().createID(Activator.getDefault().getContainer().getConnectNamespace(), serverURI), 
						IRosgiRegistryHost.class.getName(), null);
			
			Assert.isNotNull(helloReferences);
			Assert.isTrue(helloReferences.length > 0);

			IRemoteService remoteService = containerAdapter.getRemoteService(helloReferences[0]);
			remoteRosgiHost = (IRosgiRegistryHost) remoteService.getProxy(); 
			registerBackendListener();
			
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
				throw new Exception("Unable to connect with server");
			}
			return true;

		} catch (Exception e) {
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

	private boolean registerBackendListener() {
		IRemoteServiceContainerAdapter containerAdapter = 
			(IRemoteServiceContainerAdapter) container.getAdapter(IRemoteServiceContainerAdapter.class); 
		
		remoteBackendChangeListenerService = containerAdapter.registerRemoteService(
				new String[] { BackendChangeListener.class.getName() }, backendChangeListener, null);
		
		System.out.println("BackendChangeListener RemoteService registered");
		
		return true;
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
}
