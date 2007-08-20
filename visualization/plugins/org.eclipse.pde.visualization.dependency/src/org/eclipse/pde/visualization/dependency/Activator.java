/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *     IBM CAS, IBM Toronto Lab
 *******************************************************************************/
package org.eclipse.pde.visualization.dependency;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.eclipse.core.runtime.Path;
/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.pde.visualization.dependency";
	
	public static final String FORWARD_ENABLED = "icons/obj16/forward_enabled.gif";
	public static final String BACKWARD_ENABLED = "icons/obj16/backward_enabled.gif";
	public static final String SNAPSHOT = "icons/obj16/snapshot.gif";
	public static final String SAVEEDIT = "icons/obj16/save_edit.gif";
	public static final String REQ_PLUGIN_OBJ ="icons/obj16/req_plugins_obj.gif";
	public static final String SEARCH_CANCEL ="icons/obj16/progress_rem.gif";
	
	public static final String PLUGIN_OBJ = "plugin_obj";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		addImage(FORWARD_ENABLED);
		addImage(BACKWARD_ENABLED);
		addImage(SNAPSHOT);
		addImage(SAVEEDIT);
		addImage(REQ_PLUGIN_OBJ);
		addImage(SEARCH_CANCEL);
		this.getImageRegistry().put(PLUGIN_OBJ, PDEPluginImages.DESC_PLUGIN_OBJ);
	}
	
	private void addImage(String imagePath) {
		String path = "$nl$/" + imagePath; //$NON-NLS-1$
		URL url = FileLocator.find(this.getBundle(), new Path(path), null);
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
		getImageRegistry().put(imagePath, imageDescriptor);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
