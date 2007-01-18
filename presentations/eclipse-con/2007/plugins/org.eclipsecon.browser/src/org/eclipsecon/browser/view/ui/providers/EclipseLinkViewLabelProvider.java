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

package org.eclipsecon.browser.view.ui.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipsecon.browser.view.BrowserPlugin;
import org.eclipsecon.browser.view.BrowserPluginImages;
import org.eclipsecon.browser.view.model.EclipseLinkObject;

/**
 * EclipseLinkViewLabelProvider
 *
 */
public class EclipseLinkViewLabelProvider extends LabelProvider {

	/**
	 * 
	 */
	public EclipseLinkViewLabelProvider() {
		// NO-OP
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		//
		if ((element instanceof EclipseLinkObject) == false) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJS_ERROR_TSK);
		}
		
		EclipseLinkObject linkObject = (EclipseLinkObject)element;
		//
		if (linkObject.getType() == EclipseLinkObject.F_TYPE_PROJECT) {
			return BrowserPlugin.getImage(
					BrowserPluginImages.F_IMAGE_PATH_PROJECT_NODE);
		} else if (linkObject.getType() == EclipseLinkObject.F_TYPE_SUBPROJECT) {
			return BrowserPlugin.getImage(
					BrowserPluginImages.F_IMAGE_PATH_SUBPROJECT_NODE);
		} else if (linkObject.getType() == EclipseLinkObject.F_TYPE_COMPONENT) {
			return BrowserPlugin.getImage(
					BrowserPluginImages.F_IMAGE_PATH_COMPONENT_NODE);
		} else {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJS_ERROR_TSK);
		}
		
	}
	
}
