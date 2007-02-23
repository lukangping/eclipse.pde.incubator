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
import org.eclipse.browser.view.model.LinkModel;
import org.eclipse.browser.view.model.LinkObject;

/**
 * ImageLinkManager
 *
 */
public class ImageLinkManager {

	private LinkObject fEclipseImageLink;
	
	private LinkObject fGoogleImageLink;
	
	private static ImageLinkManager fInstance;
	
	/**
	 * 
	 */
	protected ImageLinkManager() {
		fEclipseImageLink = null;
		fGoogleImageLink = null;
	}

	/**
	 * @return
	 */
	public static ImageLinkManager getInstance() {
		if (fInstance == null) {
			fInstance = new ImageLinkManager();
		}
		return fInstance;
	}
	
	/**
	 * @param model
	 * @return
	 */
	public static LinkObject getEclipseImageLink(LinkModel model) {
		return getInstance().createEclipseImageLink(model);
	}
	
	/**
	 * @param model
	 * @return
	 */
	public LinkObject createEclipseImageLink(LinkModel model) {
		if (fEclipseImageLink == null) {
			fEclipseImageLink = model.getModelFactory().createComponentLink(null);
			fEclipseImageLink.setFieldName(BrowserMessages.ImageLinkManager_linkNameEclipse);
			fEclipseImageLink.setFieldLink("http://www.eclipse.org/"); //$NON-NLS-1$
			fEclipseImageLink.setFieldDescription(BrowserMessages.ImageLinkManager_linkDescEclipse);				
		}
		return fEclipseImageLink;
	}

	/**
	 * @param model
	 * @return
	 */
	public static LinkObject getGoogleImageLink(LinkModel model) {
		return getInstance().createGoogleImageLink(model);
	}
	
	/**
	 * @param model
	 * @return
	 */
	public LinkObject createGoogleImageLink(LinkModel model) {
		if (fGoogleImageLink == null) {
			fGoogleImageLink = model.getModelFactory().createComponentLink(null);
			fGoogleImageLink.setFieldName(BrowserMessages.ImageLinkManager_linkNameGoogle);
			fGoogleImageLink.setFieldLink("http://www.google.com/"); //$NON-NLS-1$
			fGoogleImageLink.setFieldDescription(BrowserMessages.ImageLinkManager_linkDescGoogle);			
		}
		return fGoogleImageLink;
	}	

}
