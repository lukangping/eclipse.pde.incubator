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

package org.eclipsecon.browser.view.ui.listeners;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipsecon.browser.view.ui.sections.EclipseProjectLinksSection;

/**
 * TreeDoubleClickListener
 *
 */
public class TreeDoubleClickListener implements IDoubleClickListener {

	private EclipseProjectLinksSection fSection;		
	
	/**
	 * @param view
	 */
	public TreeDoubleClickListener(EclipseProjectLinksSection section) {
		fSection = section;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
	public void doubleClick(DoubleClickEvent event) {
		fSection.getView().getOpenLinkAction().run();
	}

}
