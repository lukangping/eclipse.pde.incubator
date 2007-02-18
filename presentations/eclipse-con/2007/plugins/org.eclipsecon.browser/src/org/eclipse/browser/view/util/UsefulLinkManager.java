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

package org.eclipse.browser.view.util;

import org.eclipse.browser.view.BrowserMessages;
import org.eclipse.browser.view.BrowserPlugin;
import org.eclipse.browser.view.model.LinkModel;
import org.eclipse.browser.view.model.LinkObject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * UsefulLinkManager
 *
 */
public class UsefulLinkManager {

	public static final String F_POINT_ID = "org.eclipse.browser.usefulLinks";	 //$NON-NLS-1$
	
	public static final String F_ELEMENT_LINK_OBJECT = "linkObject"; //$NON-NLS-1$
	
	public static final String F_ATTRIBUTE_NAME = "name"; //$NON-NLS-1$
	
	public static final String F_ATTRIBUTE_LINK = "link"; //$NON-NLS-1$
	
	public static final String F_ATTRIBUTE_DESCRIPTION = "description"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public UsefulLinkManager() {
		// NO-OP
	}

	/**
	 * @return
	 */
	public LinkObject[] getUsefulLinks(LinkModel model) {
		
		IConfigurationElement[] elements = getElementsFromRegistry();
		//
		LinkObject[] linkObjects = new LinkObject[elements.length];
		//
		for (int i = 0; i < elements.length; i++) {
			//
			if (elements[i].getName().equals(F_ELEMENT_LINK_OBJECT) ==  false) {
				continue;
			}
			
			linkObjects[i] = model.getModelFactory().createComponentLink(null);

			String name = elements[i].getAttribute(F_ATTRIBUTE_NAME);
			String link = elements[i].getAttribute(F_ATTRIBUTE_LINK);
			String description = elements[i].getAttribute(F_ATTRIBUTE_DESCRIPTION);
			//
			if ((name == null) ||
					(name.length() == 0)) {
				BrowserPlugin.logException(
						BrowserMessages.bind(
								BrowserMessages.UsefulLinkManager_errorRequiredAttributeMissing, 
								F_ATTRIBUTE_NAME), 
						BrowserMessages.bind(
								BrowserMessages.UsefulLinkManager_errorMissingAttribute, 
								F_ATTRIBUTE_NAME), 
						new Exception());
			}
			//
			if ((link == null) ||
					(link.length() == 0)) {
				BrowserPlugin.logException(
						BrowserMessages.bind(
								BrowserMessages.UsefulLinkManager_errorRequiredAttributeMissing, 
								F_ATTRIBUTE_LINK), 
						BrowserMessages.bind(
								BrowserMessages.UsefulLinkManager_errorMissingAttribute, 
								F_ATTRIBUTE_LINK), 
						new Exception());
			}
			//
			if ((description == null) ||
					(description.length() == 0)) {
				description = BrowserMessages.UsefulLinkManager_defaultMsgDescriptionNotSpecified;
			}
			//
			linkObjects[i].setFieldName(name);
			linkObjects[i].setFieldLink(link);
			linkObjects[i].setFieldDescription(description);				
		}
		
		return linkObjects;
	}

	/**
	 * @return
	 */
	private IConfigurationElement[] getElementsFromRegistry() {
		//
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		//
		IConfigurationElement[] elements = 
			registry.getConfigurationElementsFor(F_POINT_ID);
		return elements;
	}
	
	/**
	 * @return
	 */
	public int getUsefulLinksCount() {
		return getElementsFromRegistry().length;
	}
	
}
