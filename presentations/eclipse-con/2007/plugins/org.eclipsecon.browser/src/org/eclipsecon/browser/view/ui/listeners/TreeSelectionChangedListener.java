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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipsecon.browser.view.model.EclipseLinkObject;
import org.eclipsecon.browser.view.ui.sections.EclipseProjectLinksSection;

/**
 * TreeSelectionChangedListener
 *
 */
public class TreeSelectionChangedListener implements ISelectionChangedListener {

	private EclipseProjectLinksSection fView;		

	/**
	 * @param view
	 */
	public TreeSelectionChangedListener(EclipseProjectLinksSection view) {
		fView = view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		EclipseLinkObject linkObject = getSelectedLinkObject();
		fView.getView().getOpenLinkAction().update(linkObject);	
		updateLinkDescriptionText(linkObject);
	}
	
	/**
	 * @return
	 */
	private EclipseLinkObject getSelectedLinkObject() {
		ISelection selection = fView.getTreeViewer().getSelection();
		Object object = ((IStructuredSelection)selection).getFirstElement();
		return (EclipseLinkObject)object;
	}	
	
	/**
	 * @param linkObject
	 */
	private void updateLinkDescriptionText(EclipseLinkObject linkObject) {
		fView.getLinkDescriptionTextField().setText(
				linkObject.getFieldDescription());
	}	
}
