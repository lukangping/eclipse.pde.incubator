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

package org.eclipsecon.browser.view.ui.sections;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipsecon.browser.view.ui.EclipseLinksView;
import org.eclipsecon.browser.view.ui.actions.CollapseAction;
import org.eclipsecon.browser.view.ui.listeners.HandCursorDisposeListener;
import org.eclipsecon.browser.view.ui.listeners.TreeDoubleClickListener;
import org.eclipsecon.browser.view.ui.listeners.TreeManagerMenuListener;
import org.eclipsecon.browser.view.ui.listeners.TreeSelectionChangedListener;
import org.eclipsecon.browser.view.ui.providers.EclipseLinkViewContentProvider;
import org.eclipsecon.browser.view.ui.providers.EclipseLinkViewLabelProvider;

/**
 * EclipseProjectLinksSection
 *
 */
public class EclipseProjectLinksSection extends EclipseLinkSection {

	private TreeViewer fTreeViewer;
	
	private CollapseAction fCollapseAction;
	
	private Text fLinkDescriptionText;
	
	private ToolBar fSectionToolbar;

	private Cursor fHandCursor;	
	
	private MenuManager fTreeMenuManager;
	
	/**
	 * @param view
	 * @param parent
	 * @param toolkit
	 * @param style
	 * @param text
	 * @param description
	 */
	public EclipseProjectLinksSection(EclipseLinksView view, Composite parent,
			FormToolkit toolkit, int style, String text, String description) {
		super(view, parent, toolkit, style, text, description);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipsecon.browser.view.ui.EclipseLinkSection#createUI()
	 */
	public void createUI() {
		//
		Composite client = createUISectionContainer(getSection(), 1);
		//
		createActions();
		//
		createUITreeViewer(client);
		//
		createUIContextMenuTree();
		//
		createUISectionToolbar();
		//
		createUITextLinkDescription(client);
		
		getToolkit().paintBordersFor(client);
		getSection().setClient(client);
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipsecon.browser.view.ui.EclipseLinkSection#createListeners()
	 */
	public void createListeners() {
		//
		createListenersTreeViewer();
		//
		createListenersTreeMenuManager();
		//
		createListenersSectionToolbar();
	}

	/**
	 * 
	 */
	private void createActions() {
		//
		fCollapseAction = new CollapseAction();
	}	
	
	/**
	 * 
	 */
	private void createUITextLinkDescription(Composite parent) {
		int style = SWT.MULTI | SWT.READ_ONLY | SWT.WRAP;
		fLinkDescriptionText = getToolkit().createText(parent, "", style); //$NON-NLS-1$
		fLinkDescriptionText.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 10;
		data.heightHint = 30;
		fLinkDescriptionText.setLayoutData(data);		
	}

	/**
	 * 
	 */
	private void createUISectionToolbar() {
		
		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		fSectionToolbar = toolBarManager.createControl(getSection());
		fHandCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
		fSectionToolbar.setCursor(fHandCursor);
		fCollapseAction.setTreeViewer(fTreeViewer);
		fCollapseAction.setTreeObject(getView().getModel().getRootLinkObject());
		toolBarManager.add(fCollapseAction);
		toolBarManager.update(true);
		getSection().setTextClient(fSectionToolbar);
	}			
	
	/**
	 * @param parent
	 */
	private void createUITreeViewer(Composite parent) {
		
		fTreeViewer = new TreeViewer(parent, 
				SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		fTreeViewer.setContentProvider(new EclipseLinkViewContentProvider());
		fTreeViewer.setLabelProvider(new EclipseLinkViewLabelProvider());
		fTreeViewer.setComparator(null);
		fTreeViewer.setAutoExpandLevel(2);
		fTreeViewer.setInput(getView().getModel());		

		int style = GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL;
		GridData data = new GridData(style);
		data.heightHint = 150;
		fTreeViewer.getTree().setLayoutData(data);
		
	}

	/**
	 * 
	 */
	private void createUIContextMenuTree() {
		fTreeMenuManager = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		fTreeMenuManager.setRemoveAllWhenShown(true);
		Menu menu = fTreeMenuManager.createContextMenu(fTreeViewer.getControl());
		fTreeViewer.getControl().setMenu(menu);
	}
	
	/**
	 * @return
	 */
	public Cursor getHandCursor() {
		return fHandCursor;
	}

	/**
	 * @return
	 */
	public TreeViewer getTreeViewer() {
		return fTreeViewer;
	}
	
	/**
	 * @return
	 */
	public Text getLinkDescriptionTextField() {
		return fLinkDescriptionText;
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.SectionPart#setFocus()
	 */
	public void setFocus() {
		if (fTreeViewer != null) {
			fTreeViewer.getControl().setFocus();
		}
	}
	
	/**
	 * 
	 */
	private void createListenersTreeViewer() {
		//
		fTreeViewer.addDoubleClickListener(new TreeDoubleClickListener(this));
		// 
		fTreeViewer.addSelectionChangedListener(
				new TreeSelectionChangedListener(this));
	}
	
	/**
	 * 
	 */
	private void createListenersSectionToolbar() {
		// Cursor needs to be explicitly disposed
		fSectionToolbar.addDisposeListener(new HandCursorDisposeListener(this));		
	}

	/**
	 * 
	 */
	private void createListenersTreeMenuManager() {
		fTreeMenuManager.addMenuListener(new TreeManagerMenuListener(this));		
		// Listener must be added before registering the context menu; 
		// otherwise and error will show up in the log
		getView().getSite().registerContextMenu(fTreeMenuManager, fTreeViewer);		
	}	

}
