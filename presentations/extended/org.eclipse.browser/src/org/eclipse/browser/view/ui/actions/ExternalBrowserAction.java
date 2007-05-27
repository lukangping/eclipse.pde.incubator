/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.browser.view.ui.actions;

import org.eclipse.browser.view.BrowserMessages;
import org.eclipse.browser.view.BrowserPlugin;
import org.eclipse.browser.view.BrowserPluginImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * ExternalBrowserAction
 *
 */
public class ExternalBrowserAction extends Action {

	private OpenLinkAction fOpenLinkAction;
	
	/**
	 * 
	 */
	public ExternalBrowserAction(OpenLinkAction action) {
		super(BrowserMessages.ExternalBrowserAction_actionNameExternalBrowser, IAction.AS_CHECK_BOX);
		fOpenLinkAction = action;
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		setToolTipText(BrowserMessages.ExternalBrowserAction_toolTipExternalBrowser);
		setImageDescriptor(
				BrowserPlugin.getImageDescriptor(BrowserPluginImages.F_IMAGE_PATH_EXTERNAL_BROWSER));
		//
		setChecked(fOpenLinkAction.getUseExternalBrowser());
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {

		boolean useExternal = fOpenLinkAction.getUseExternalBrowser();
		//
		if (useExternal) {
			useExternal = false;
		} else {
			useExternal = true;
		}
		//
		fOpenLinkAction.setUseExternalBrowser(useExternal);
	}

}
