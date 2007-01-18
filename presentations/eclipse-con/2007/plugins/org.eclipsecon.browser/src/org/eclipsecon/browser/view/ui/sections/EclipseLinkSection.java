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

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipsecon.browser.view.ui.EclipseLinksView;

/**
 * EclipseLinkSection
 *
 */
public abstract class EclipseLinkSection extends SectionPart {

	private EclipseLinksView fView;	
	
	private FormToolkit fToolkit;	
	
	public EclipseLinkSection(EclipseLinksView view, Composite parent, 
			FormToolkit toolkit, int style,
			String text, String description) {
		super(parent, toolkit, style);
		fToolkit = toolkit;
		fView = view;
		initialize(fView.getManagedForm());
		customizeUISection(text, description);
	}
	
	/**
	 * 
	 */
	public abstract void createUI();
	
	/**
	 * 
	 */
	public abstract void createListeners();

	/**
	 * @param text
	 * @param description
	 */
	private void customizeUISection(String text, String description) {
		getSection().clientVerticalSpacing = 4;
		getSection().marginHeight = 3;
		getSection().marginWidth = 5; 
		getSection().setText(text);
		getSection().setDescription(description);
		getSection().setExpanded(true);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		getSection().setLayoutData(data);
	}		

	/**
	 * @param parent
	 * @param columns
	 * @return
	 */
	protected Composite createUISectionContainer(Composite parent, int columns) {
		Composite container = fToolkit.createComposite(parent);
		GridLayout layout = new GridLayout(columns, false);
		container.setLayout(layout);
		return container;		
	}		
	
	/**
	 * @return
	 */
	public EclipseLinksView getView() {
		return fView;
	}		
	
	/**
	 * @return
	 */
	public FormToolkit getToolkit() {
		return fToolkit;
	}
}
