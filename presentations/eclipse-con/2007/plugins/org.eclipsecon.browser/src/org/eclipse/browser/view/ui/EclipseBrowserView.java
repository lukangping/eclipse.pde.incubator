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

package org.eclipse.browser.view.ui;

import java.net.URL;

import org.eclipse.browser.view.BrowserMessages;
import org.eclipse.browser.view.BrowserPlugin;
import org.eclipse.browser.view.BrowserPluginImages;
import org.eclipse.browser.view.model.LinkModel;
import org.eclipse.browser.view.ui.actions.ExternalBrowserAction;
import org.eclipse.browser.view.ui.actions.OpenLinkAction;
import org.eclipse.browser.view.ui.listeners.FormTextHyperlinkListener;
import org.eclipse.browser.view.ui.listeners.SashFormControlListener;
import org.eclipse.browser.view.ui.sections.ProjectLinksSection;
import org.eclipse.browser.view.ui.sections.SearchSection;
import org.eclipse.browser.view.ui.sections.UsefulLinksSection;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.forms.HyperlinkGroup;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

/**
 * EclipseBrowserView
 *
 */
public class EclipseBrowserView extends ViewPart {
	
	private ProjectLinksSection fEclipseProjectLinksSection;
	
	private UsefulLinksSection fEclipseUsefulLinksSection;
	
	private SearchSection fEclipseSearchSection;

	private ManagedForm fManagedForm;
	
	private final static String F_DEFAULT_MODEL_FILE = "/data/eclipse_project_links.xml"; //$NON-NLS-1$
													   
	private FormToolkit fToolkit;
	
	private LinkModel fModel;

	private FormText fEclipseImageFormText;	

	private Composite fBaseComposite;
	
	private OpenLinkAction fOpenLinkAction;

	private ExternalBrowserAction fExternalBrowserAction;

	private SashForm fSashForm;
	
	/**
	 * 
	 */
	public EclipseBrowserView() {
		// NO-OP
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		//
		boolean isLoaded = false;
		
		isLoaded = createDefaultModel();
		
		if (isLoaded) {
			//		
			createActions();		
			//
			createUI(parent);
			//
			createListeners();
			//
			contributeToActionBars();
		}

	}

	/**
	 * @param parent
	 */
	private void createUI(Composite parent) {
		//
		createUIToolkit(parent);
		//
		fBaseComposite = createUIComposite(parent, 3, 8, GridData.FILL_HORIZONTAL);
		//
		createUISashForm();
		//
		createUIHeader();
		//
		createUIBody();
		//
		createUIFooter();

		fToolkit.adapt(fSashForm, true, true);
	}

	/**
	 * 
	 */
	private void createUIFooter() {
		Composite parent = createUIComposite(fSashForm, 0, 0, GridData.FILL_HORIZONTAL);
		//
		createUISeparator(parent);
		//
		createUIFormTextEclipseImage(parent);		
	}

	/**
	 * 
	 */
	private void createUIBody() {
		ScrolledForm form = createUIScrolledForm(fSashForm, 0, 0, GridData.FILL_BOTH);
		fManagedForm = new ManagedForm(fToolkit, form);
		//
		createUISectionProjectLinks(form.getBody());
		//
		if (BrowserPlugin.getUsefulLinkManager().getUsefulLinksCount() > 0) {
			createUISectionUsefulLinks(form.getBody());
		}
		//
		createUISectionSearch(form.getBody());
	}
	
	/**
	 * 
	 */
	private void createUIHeader() {
		Form form = createUIForm(fSashForm, 0, 0, GridData.FILL_BOTH);
		form.setText(BrowserMessages.EclipseLinksView_titleEclipseLinks);
	}

	/**
	 * 
	 */
	private void createUISashForm() {
		int style = SWT.VERTICAL;
		fSashForm = new SashForm (fBaseComposite, style);
		fSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	/**
	 * 
	 */
	private void createListeners() {
		//
		fEclipseProjectLinksSection.createListeners();
		//
		fEclipseUsefulLinksSection.createListeners();
		//
		fEclipseSearchSection.createListeners();
		//
		createListenersFormTextEclipseImage();
		// 
		createListenersSashForm();
	}
	
	/**
	 * 
	 */
	private void createListenersSashForm() {
		fSashForm.addControlListener(new SashFormControlListener());		
	}

	/**
	 * 
	 */
	private void createListenersFormTextEclipseImage() {
		fEclipseImageFormText.addHyperlinkListener(
				new FormTextHyperlinkListener(this));
	}
	
	/**
	 * 
	 */
	private void createUISectionUsefulLinks(Composite parent) {
		int style = Section.DESCRIPTION | ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE;
		fEclipseUsefulLinksSection = new UsefulLinksSection(this,
				parent, fToolkit, style, BrowserMessages.EclipseLinksView_sectionTitleUsefulLinks,
				BrowserMessages.EclipseLinksView_sectionDescUsefulLinks);
		fEclipseUsefulLinksSection.createUI();		
	}

	/**
	 * 
	 */
	private void createUISectionProjectLinks(Composite parent) {
		int style = Section.DESCRIPTION | ExpandableComposite.TITLE_BAR;
		fEclipseProjectLinksSection = new ProjectLinksSection(this, parent, fToolkit, style, BrowserMessages.EclipseLinksView_sectionTitleProjectLinks, BrowserMessages.EclipseLinksView_sectionDescProjectLinks);
		fEclipseProjectLinksSection.createUI();
	}

	/**
	 * @param body
	 */
	private void createUISectionSearch(Composite parent) {
		int style = Section.DESCRIPTION | ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE;
		fEclipseSearchSection = new SearchSection(this, parent,
				fToolkit, style, BrowserMessages.EclipseLinksView_sectionTitleSearch,
				BrowserMessages.EclipseLinksView_sectionDescSearch);
		fEclipseSearchSection.createUI();			
	}
	
	/**
	 * @param parent
	 */
	private void createUIToolkit(Composite parent) {
		fToolkit = new FormToolkit(parent.getDisplay());
		fToolkit.getColors().initializeSectionToolBarColors();
		fToolkit.getHyperlinkGroup().setHyperlinkUnderlineMode(
				HyperlinkGroup.UNDERLINE_HOVER);
	}

	/**
	 * @param parent
	 */
	private Composite createUIComposite(Composite parent, int marginWidth,
			int marginHeight, int style) {
		Composite composite = fToolkit.createComposite(parent);
		GridLayout layout = new GridLayout();
		layout.marginWidth = marginWidth;
		layout.marginHeight = marginHeight;		
		composite.setLayout(layout);
		GridData data = new GridData(style);
		composite.setLayoutData(data);	
		return composite;
	}
	
	/**
	 * @param parent
	 */
	private ScrolledForm createUIScrolledForm(Composite parent,
			int marginWidth, int marginHeight, int style) {
		ScrolledForm form = fToolkit.createScrolledForm(parent);
		GridLayout layout = new GridLayout();
		layout.marginWidth = marginWidth;
		layout.marginHeight = marginHeight;		
		form.getBody().setLayout(layout);
		GridData data = new GridData(style);		
		form.getBody().setLayoutData(data);	
		return form;
	}

	/**
	 * @param parent
	 */
	private Form createUIForm(Composite parent,
			int marginWidth, int marginHeight, int style) {
		Form form = fToolkit.createForm(parent);
		GridLayout layout = new GridLayout();
		layout.marginWidth = marginWidth;
		layout.marginHeight = marginHeight;		
		form.getBody().setLayout(layout);
		GridData data = new GridData(style);		
		form.getBody().setLayoutData(data);	
		return form;
	}	
	
	/**
	 * 
	 */
	private void createUISeparator(Composite parent) {
		Composite separator = fToolkit.createCompositeSeparator(parent);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = 1;
		separator.setLayoutData(data);		
	}
	
	/**
	 * @param parent
	 */
	private void createUIFormTextEclipseImage(Composite parent) {
	
		fEclipseImageFormText = fToolkit.createFormText(parent, true);
		String text = "<form><p><br/><a href=\"eclipse.org\"><img href=\"eclipse\"/></a></p></form>"; //$NON-NLS-1$
		fEclipseImageFormText.marginHeight = 0;
		fEclipseImageFormText.setText(text, true, false);
		fEclipseImageFormText.setImage("eclipse", BrowserPlugin.getImage(BrowserPluginImages.F_IMAGE_PATH_ECLIPSE_LOGO)); //$NON-NLS-1$
		
	}
	
	/**
	 * @return
	 */
	private boolean createDefaultModel() {
		
		URL url = BrowserPlugin.getDefault().getBundle().getEntry(F_DEFAULT_MODEL_FILE);
		// Ensure the model XML file was found in the bundle
		if (url == null) {
			String message = BrowserMessages.bind(
					BrowserMessages.EclipseLinksView_errorMissingModelFile, 
					url.toString());
			BrowserPlugin.logException(BrowserMessages.EclipseLinksView_errorModelLoad, message, new Exception(message));
			return false;
		}
		// Try to load the model
		try {
			// Resolve the URL into a path
			URL modelFile = FileLocator.resolve(url);
			// Create the model
			fModel = new LinkModel(modelFile);
			// Load the model
			fModel.load();			
		} catch (Exception e) {
			BrowserPlugin.logException(BrowserMessages.EclipseLinksView_errorModelLoad, e.getMessage(), e);
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 */
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	/**
	 * @param manager
	 */
	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(fOpenLinkAction);
		manager.add(fExternalBrowserAction);
	}
	
	/**
	 * @param manager
	 */
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(fOpenLinkAction);
		manager.add(fExternalBrowserAction);
	}

	/**
	 * 
	 */
	private void createActions() {
		//
		fOpenLinkAction = new OpenLinkAction();
		//
		fExternalBrowserAction = new ExternalBrowserAction(fOpenLinkAction);
	}

	/**
	 * Passing the focus request to the project links section
	 */
	public void setFocus() {
		fEclipseProjectLinksSection.setFocus();
	}

	/**
	 * @return
	 */
	public OpenLinkAction getOpenLinkAction() {
		return fOpenLinkAction;
	}
	
	/**
	 * @return
	 */
	public LinkModel getModel() {
		return fModel;
	}
	
	/**
	 * @return
	 */
	public ManagedForm getManagedForm() {
		return fManagedForm;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose() {
		super.dispose();
		fManagedForm.dispose();
	}
}