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

import java.net.URL;

import org.eclipse.browser.view.BrowserMessages;
import org.eclipse.browser.view.BrowserPlugin;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

public class InternalBrowser implements IHTMLBrowser {

	public void openURL(URL url) {
		try {
			IWorkbenchBrowserSupport support = 
				PlatformUI.getWorkbench().getBrowserSupport();
			
			int style = IWorkbenchBrowserSupport.AS_EDITOR | 
				IWorkbenchBrowserSupport.STATUS;
			IWebBrowser browser = support.createBrowser(
					style,
					"org.eclipse.pde",  //$NON-NLS-1$
					url.toString(), 
					url.toString());
			browser.openURL(url);
		} catch (PartInitException e) {
			BrowserPlugin.logException(BrowserMessages.OpenLinkAction_errorLinkAction, BrowserMessages.OpenLinkAction_errorWorkbenchInitialization, e);
		}
	}

}
