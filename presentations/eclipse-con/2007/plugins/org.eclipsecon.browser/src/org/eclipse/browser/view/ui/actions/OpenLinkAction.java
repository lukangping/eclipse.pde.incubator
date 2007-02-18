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

package org.eclipse.browser.view.ui.actions;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.browser.view.BrowserMessages;
import org.eclipse.browser.view.BrowserPlugin;
import org.eclipse.browser.view.BrowserPluginImages;
import org.eclipse.browser.view.model.LinkObject;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

/**
 * OpenURLAction
 *
 */
public class OpenLinkAction extends Action {

	private boolean fUseExternalBrowser;
	
	private String fLink;
	
	private String fName;
	
	/**
	 * 
	 */
	public OpenLinkAction() {
		fUseExternalBrowser = false;
		fLink = null;
		fName = null;
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		setImageDescriptor(
				BrowserPlugin.getImageDescriptor(BrowserPluginImages.F_IMAGE_PATH_LINK));
		setText(BrowserMessages.OpenLinkAction_actionNameOpenLink);
	}
	
	/**
	 * 
	 */
	private void openURL(URL url) throws PartInitException {
		//
		if (fUseExternalBrowser) {
			openURLInExternalBrowser(url);
		} else {
			openURLInInternalBrowser(url);
		}		
	}
	
	/**
	 * 
	 */
	private void openURLInInternalBrowser(URL url) throws PartInitException{
		IWorkbenchBrowserSupport support = 
			PlatformUI.getWorkbench().getBrowserSupport();
		
		int style = IWorkbenchBrowserSupport.AS_EDITOR | 
			IWorkbenchBrowserSupport.STATUS;
		IWebBrowser browser = support.createBrowser(
				style,
				"org.eclipse.pde",  //$NON-NLS-1$
				fName, 
				fName);
		browser.openURL(url);
	}
	
	/**
	 * 
	 */
	private void openURLInExternalBrowser(URL url) throws PartInitException {
		IWorkbenchBrowserSupport support = 
			PlatformUI.getWorkbench().getBrowserSupport();

		IWebBrowser browser = support.getExternalBrowser();
		browser.openURL(url); 
	}
	
	/**
	 * @param linkObject
	 */
	public void setLink(String link) {
		fLink = link;
	}
	
	/**
	 * @param name
	 */
	public void setName(String name) {
		fName = name;
	}
	
	/**
	 * @param linkObject
	 */
	public void update(LinkObject linkObject) {
		if (linkObject == null) {
			setToolTipText(null);
			setLink(null);
			setName(null);
		} else {
			setToolTipText(BrowserMessages.bind(
					BrowserMessages.OpenLinkAction_toolTipOpenLink, 
					linkObject.getFieldName()));
			setLink(linkObject.getFieldLink());
			setName(linkObject.getFieldName());
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		//
		if ((fLink == null) ||
				(fLink.length() == 0)) {
			BrowserPlugin.logException(BrowserMessages.OpenLinkAction_errorLinkAction, BrowserMessages.OpenLinkAction_errorUrlNotSpecified, new Exception(fName));
			return;
		}
		//
		try {
			openURL(new URL(fLink));
		} catch (MalformedURLException e) {
			BrowserPlugin.logException(BrowserMessages.OpenLinkAction_errorLinkAction, BrowserMessages.OpenLinkAction_errorUrlMalformed, e);
		} catch (PartInitException e) {
			BrowserPlugin.logException(BrowserMessages.OpenLinkAction_errorLinkAction, BrowserMessages.OpenLinkAction_errorWorkbenchInitialization, e);
		}
	}
	
	/**
	 * @param useExternal
	 */
	public void setUseExternalBrowser(boolean useExternal) {
		fUseExternalBrowser = useExternal;
	}
	
	/**
	 * @return
	 */
	public boolean getUseExternalBrowser() {
		return fUseExternalBrowser;
	}
}
