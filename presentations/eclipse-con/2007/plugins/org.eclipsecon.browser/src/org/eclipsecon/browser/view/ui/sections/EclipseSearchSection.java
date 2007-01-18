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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipsecon.browser.view.BrowserMessages;
import org.eclipsecon.browser.view.BrowserPlugin;
import org.eclipsecon.browser.view.BrowserPluginImages;
import org.eclipsecon.browser.view.ui.EclipseLinksView;
import org.eclipsecon.browser.view.ui.listeners.FormTextHyperlinkListener;
import org.eclipsecon.browser.view.ui.listeners.SearchButtonSelectionListener;
import org.eclipsecon.browser.view.ui.listeners.SearchTextKeyListener;

/**
 * EclipseSearchSection
 *
 */
public class EclipseSearchSection extends EclipseLinkSection {

	private Text fSearchText;
	
	private Button fSearchButton;
	
	private FormText fGoogleImageFormText;		
	
	/**
	 * @param view
	 * @param parent
	 * @param toolkit
	 * @param style
	 * @param text
	 * @param description
	 */
	public EclipseSearchSection(EclipseLinksView view, Composite parent,
			FormToolkit toolkit, int style, String text, String description) {
		super(view, parent, toolkit, style, text, description);
	}

	/* (non-Javadoc)
	 * @see org.eclipsecon.browser.view.ui.EclipseLinkSection#createUI()
	 */
	public void createUI() {
		//
		Composite client = createUISectionContainer(getSection(), 4);
		//
		createUIFormTextGoogleImage(client);
		//
		createUITextSearch(client);
		//
		createUIButtonSearch(client);
		
		getToolkit().paintBordersFor(client);
		getSection().setClient(client);		
	}	
	
	/**
	 * @param parent
	 */
	private void createUIButtonSearch(Composite parent) {
		fSearchButton = getToolkit().createButton(parent, BrowserMessages.EclipseSearchSection_buttonNameSearch, SWT.PUSH);
		GridData data = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		fSearchButton.setLayoutData(data);
	}

	/**
	 * @param parent
	 */
	private void createUITextSearch(Composite parent) {
		fSearchText = getToolkit().createText(parent, "", SWT.NONE); //$NON-NLS-1$
		fSearchText.setTextLimit(256);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 10;
		fSearchText.setLayoutData(data);
	}

	/**
	 * @param parent
	 */
	private void createUIFormTextGoogleImage(Composite parent) {
		//fToolkit.createLabel(parent, "Google: ");
		fGoogleImageFormText = getToolkit().createFormText(parent, true);
		// Bug in forms
		String text = "<form><p><span color=\"default\">.</span><a href=\"google.com\"><img href=\"google\"/></a></p></form>"; //$NON-NLS-1$
		fGoogleImageFormText.marginHeight = 0;
		fGoogleImageFormText.setColor("default", Display.getCurrent().getSystemColor(SWT.COLOR_WHITE)); //$NON-NLS-1$
		fGoogleImageFormText.setText(text, true, false);
		fGoogleImageFormText.setImage("google", BrowserPlugin.getImage(BrowserPluginImages.F_IMAGE_PATH_GOOGLE)); //$NON-NLS-1$
		GridData data = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		fGoogleImageFormText.setLayoutData(data);
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipsecon.browser.view.ui.EclipseLinkSection#createListeners()
	 */
	public void createListeners() {
		//
		createListenersFormTextGoogleImage();
		//
		createListenersTextSearch();
		//
		createListenersButtonSearch();
	}

	/**
	 * 
	 */
	private void createListenersTextSearch() {
		fSearchText.addKeyListener(new SearchTextKeyListener(this));
	}

	/**
	 * 
	 */
	private void createListenersButtonSearch() {
		fSearchButton.addSelectionListener(
				new SearchButtonSelectionListener(this));
	}
	
	/**
	 * 
	 */
	private void createListenersFormTextGoogleImage() {
		fGoogleImageFormText.addHyperlinkListener(
				new FormTextHyperlinkListener(getView()));		
	}
	
	/**
	 * @return
	 */
	public Text getSearchTextField() {
		return fSearchText;
	}	

}
