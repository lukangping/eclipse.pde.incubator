package org.eclipse.pde.internal.runtime.registry.rosgi;

public interface IRosgiRegistryHost {
	
	public boolean connectRemoteBackendChangeListener();
	
	public void setEnabled(long id, boolean enabled);

	public void start(long id);

	public void stop(long id);

	public String[] diagnose(long id);

	public void initializeBundles();

	public void initializeExtensionPoints();

	public void initializeServices();
	
	public void disconnect();
	
	public void setClientURI(String uri);
}
