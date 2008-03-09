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

package org.eclipse.browser.view.ui.listeners;

import org.eclipse.browser.view.model.LinkObject;
import org.eclipse.browser.view.ui.sections.ProjectLinksSection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * TreeSelectionChangedListener
 *
 */
public class TreeSelectionChangedListener implements ISelectionChangedListener {

	private ProjectLinksSection fView;		

	/**
	 * @param view
	 */
	public TreeSelectionChangedListener(ProjectLinksSection view) {
		fView = view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		LinkObject linkObject = getSelectedLinkObject();
		fView.getView().getOpenLinkAction().update(linkObject);	
		updateLinkDescriptionText(linkObject);
	}
	
	/**
	 * @return
	 */
	private LinkObject getSelectedLinkObject() {
		ISelection selection = fView.getTreeViewer().getSelection();
		Object object = ((IStructuredSelection)selection).getFirstElement();
		return (LinkObject)object;
	}	
	
	/**
	 * @param linkObject
	 */
	private void updateLinkDescriptionText(LinkObject linkObject) {
		fView.getLinkDescriptionTextField().setText(
				linkObject.getFieldDescription());
	}	
}
