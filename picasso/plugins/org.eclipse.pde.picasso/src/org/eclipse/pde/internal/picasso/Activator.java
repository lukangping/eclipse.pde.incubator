/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.internal.picasso;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.pde.picasso"; //$NON-NLS-1$
	public static final String OPTION_ID_PAINT = "paint"; //$NON-NLS-1$
	public static final String OPTION_ID_PAINT_EXTRA_COMPOSITE_MARGIN = "paint/extraCompositeMargin"; //$NON-NLS-1$
	public static final String OPTION_ID_PAINT_TOOL_TIP = "paint/toolTip"; //$NON-NLS-1$

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
		
		String paintOption = getDebugOption(OPTION_ID_PAINT);
		if (Boolean.parseBoolean(paintOption) == false) return;
		String paintToolTipOption = getDebugOption(OPTION_ID_PAINT_TOOL_TIP);
		boolean toolTip = Boolean.parseBoolean(paintToolTipOption);
		String paintExtraCompositeMarginOption = getDebugOption(OPTION_ID_PAINT_EXTRA_COMPOSITE_MARGIN);
		int extraCompositeMargin = Integer.parseInt(paintExtraCompositeMarginOption);
		UIJob job = new ListenerJob(Messages.ListenerJob_name, extraCompositeMargin, toolTip);
		job.schedule();
	}

	private String getDebugOption(String option) {
		return Platform.getDebugOption(PLUGIN_ID + '/' + option);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		
		// TODO we should remove those crazy listeners
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
