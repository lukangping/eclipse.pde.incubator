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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.browser.view.BrowserMessages;
import org.eclipse.browser.view.BrowserPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;


public class ExtensibleOpenLinkAction extends OpenLinkAction implements IExtensibleAction {
	
	private static String POINT_ID = "org.eclipse.browser.htmlBrowsers"; //$NON-NLS-1$
	private static String ATT_ID = "id"; //$NON-NLS-1$
	private static String ATT_NAME = "name"; //$NON-NLS-1$
	private static String ATT_CLASS = "class"; //$NON-NLS-1$
	
	private Map fBrowserMap;
	private String fCurrentBrowserId;
	
	class OpenBrowserAction extends Action {
		
		public OpenBrowserAction(String id, String name) {
			super(name, IAction.AS_RADIO_BUTTON);
			setId(id);
		}	
		
		public void setChecked(boolean checked) {
			if (checked)
				setCurrentBrowserId(getId());			
		}
		
	}
	
	public boolean isExtensible() {
		return true;
	}

	public void setCurrentBrowserId(String id) {
		fCurrentBrowserId = id;
	}
	
	public Action[] getActions() {
		if (fBrowserMap == null)
			loadElements();
		Map result = new TreeMap();
		Iterator iter = fBrowserMap.values().iterator();
		while (iter.hasNext()) {
			IConfigurationElement element = (IConfigurationElement)iter.next();
			String name = element.getAttribute(ATT_NAME);
			String id = element.getAttribute(ATT_ID);
			result.put(name, new OpenBrowserAction(id, name));
		}
		return (Action[]) result.values().toArray(new Action[result.size()]);
	}
	
	private void loadElements() {
		fBrowserMap = new HashMap();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(POINT_ID);
		for (int i = 0; i < elements.length; i++) {
			String id = elements[i].getAttribute(ATT_ID);
			if (id == null 
					|| elements[i].getAttribute(ATT_NAME) == null 
					|| elements[i].getAttribute(ATT_CLASS) == null)
				continue;
			fBrowserMap.put(id, elements[i]);
		}
		
	}
	
	public void run() {
		if (fBrowserMap == null)
			loadElements();
		
		Assert.isNotNull(fCurrentBrowserId);
		
		Object value = fBrowserMap.get(fCurrentBrowserId);
		try {
			if (value instanceof IHTMLBrowser) {
				((IHTMLBrowser)value).openURL(new URL(getLink()));
			} else if (value instanceof IConfigurationElement) {
				try {
					IConfigurationElement element = (IConfigurationElement)value;
					Object result = element.createExecutableExtension(ATT_CLASS);
					if (result instanceof IHTMLBrowser) {
						fBrowserMap.put(fCurrentBrowserId, result);
						((IHTMLBrowser)result).openURL(new URL(getLink()));
					} else {
						fBrowserMap.remove(fCurrentBrowserId);
					}
				} catch (CoreException e) {
				}			
			}
		} catch (MalformedURLException e) {
			BrowserPlugin.logException(BrowserMessages.OpenLinkAction_errorLinkAction, BrowserMessages.OpenLinkAction_errorUrlMalformed, e);			
		}
	}
}
