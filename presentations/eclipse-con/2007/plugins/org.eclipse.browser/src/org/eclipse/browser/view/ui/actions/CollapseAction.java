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

import org.eclipse.browser.view.BrowserMessages;
import org.eclipse.browser.view.BrowserPlugin;
import org.eclipse.browser.view.BrowserPluginImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TreeViewer;

public class CollapseAction extends Action {

	private TreeViewer fTreeViewer;
	
	private Object fTreeObject;
	
	/**
	 * @param treeViewer
	 * @param treeObject
	 */
	public CollapseAction() {
		super(BrowserMessages.CollapseAction_actionNameCollapseAll, IAction.AS_PUSH_BUTTON);
		fTreeObject = null;
		fTreeViewer = null;
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		setToolTipText(BrowserMessages.CollapseAction_actionDescCollapseAll);
		setImageDescriptor(
				BrowserPlugin.getImageDescriptor(BrowserPluginImages.F_IMAGE_PATH_COLLAPSE));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		
		if (fTreeViewer == null) {
			return;
		} else if (fTreeObject == null) {
			fTreeViewer.collapseAll();
		} else {
			// Redraw modification needed to avoid flicker
			// Collapsing to a specific level does not work			
			fTreeViewer.getControl().setRedraw(false);
			fTreeViewer.collapseAll();
			fTreeViewer.expandToLevel(fTreeObject, 1);
			fTreeViewer.getControl().setRedraw(true);
		}
	}
	
	/**
	 * @param treeViewer
	 */
	public void setTreeViewer(TreeViewer treeViewer) {
		fTreeViewer = treeViewer;
	}

	/**
	 * @param treeObject
	 */
	public void setTreeObject(Object treeObject) {
		fTreeObject = treeObject;
	}
}
