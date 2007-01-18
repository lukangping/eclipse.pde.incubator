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

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipsecon.browser.view.ui.sections.EclipseProjectLinksSection;

/**
 * TreeManagerMenuListener
 *
 */
public class TreeManagerMenuListener implements IMenuListener {

	private EclipseProjectLinksSection fSection;		
	
	/**
	 * @param view
	 */
	public TreeManagerMenuListener(EclipseProjectLinksSection section) {
		fSection = section;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager manager) {
		fillContextMenu(manager);
	}

	/**
	 * @param manager
	 */
	private void fillContextMenu(IMenuManager manager) {
		manager.add(fSection.getView().getOpenLinkAction());
		// Other plug-ins can contribute thier actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}	
	
}
