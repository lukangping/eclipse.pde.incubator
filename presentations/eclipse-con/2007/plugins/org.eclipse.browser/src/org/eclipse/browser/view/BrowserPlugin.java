/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.browser.view;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.browser.view.util.UsefulLinkManager;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BrowserPlugin extends AbstractUIPlugin {
	
	public static String PERSPECTIVE_ID = "org.eclipse.browser.perspective"; //$NON-NLS-1$
	public static String VIEW_ID = "org.eclipse.browser.view"; //$NON-NLS-1$

	private static BrowserPlugin plugin;
	
	private UsefulLinkManager fUsefulLinkManager;
	
	/**
	 * The constructor
	 */
	public BrowserPlugin() {
		fUsefulLinkManager = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
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
	public static BrowserPlugin getDefault() {
		return plugin;
	}

	/**
	 * @return
	 */
	public static UsefulLinkManager getUsefulLinkManager() {
		return getDefault().createUsefulLinkManager();
	}
	
	/**
	 * @return
	 */
	public UsefulLinkManager createUsefulLinkManager() {
		if (fUsefulLinkManager == null) {
			fUsefulLinkManager = new UsefulLinkManager();
		}
		return fUsefulLinkManager;
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(getPluginID(), path);
	}

	/**
	 * @param imagePath
	 * @return
	 */
	public static Image getImage(String imagePath) {
		ImageRegistry registry = getDefault().getImageRegistry();
		
		Image image = registry.get(imagePath);
		
		if (image != null) {
			return image;
		}
		
		ImageDescriptor descriptor = getImageDescriptor(imagePath);
			
		if (descriptor == null) {
			return null;
		}
		
		image = descriptor.createImage();
		registry.put(imagePath, image);
		
		return image;
	}		
	
	/**
	 * @return
	 */
	public static String getPluginID() {
		return getDefault().getBundle().getSymbolicName();
	}
	
	/**
	 * @return
	 */
	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window == null) {
			return null;
		}
		return window.getShell();
	}
	
	/**
	 * @return
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}	

	/**
	 * @param title
	 * @param message
	 * @param e
	 */
	public static void openErrorDialog(String title, String message, IStatus status) {
		ErrorDialog.openError(
				getActiveWorkbenchShell(), 
				title,
				message,
				status);
	}	
	
	/**
	 * @param title
	 * @param message
	 * @param e
	 */
	public static void logException(final String title,
			String message, Throwable e) {
		if (e instanceof InvocationTargetException) {
			e = ((InvocationTargetException) e).getTargetException();
		}
		IStatus status = null;
		if (e instanceof CoreException) {
			status = ((CoreException) e).getStatus();
		} else {
			if (message == null) {
				message = e.getMessage();
			}
			if (message == null) {
				message = e.toString();
			}
			status = new Status(IStatus.ERROR, getPluginID(), IStatus.OK,
					message, e);
		}
		ResourcesPlugin.getPlugin().getLog().log(status);
		Display display = getStandardDisplay();
		final IStatus fstatus = status;
		display.asyncExec(new Runnable() {
			public void run() {
				openErrorDialog(title, null, fstatus);
			}
		});
	}	

	/**
	 * Returns the standard display to be used. The method first checks, if the
	 * thread calling this method has an associated disaply. If so, this display
	 * is returned. Otherwise the method returns the default display.
	 */
	public static Display getStandardDisplay() {
		Display display;
		display = Display.getCurrent();
		if (display == null)
			display = Display.getDefault();
		return display;
	}	
	
}
