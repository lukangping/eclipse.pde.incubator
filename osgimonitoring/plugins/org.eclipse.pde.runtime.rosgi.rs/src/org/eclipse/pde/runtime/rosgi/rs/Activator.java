package org.eclipse.pde.runtime.rosgi.rs;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.eclipse.pde.internal.runtime.registry.rosgi.IRosgiRegistryHost;
import org.eclipse.pde.internal.runtime.registry.rosgi.RosgiRegistryHost;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IRemoteServiceContainerAdapter containerAdapter = (IRemoteServiceContainerAdapter) container
				.getAdapter(IRemoteServiceContainerAdapter.class); 
		serviceRegistration = containerAdapter.registerRemoteService(
				new String[] { IRosgiRegistryHost.class.getName() }, host, null); 
		
		System.out.println("IRosgiRegistryHost RemoteService registered");
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
