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

package org.eclipse.browser.view.ui;

import java.net.URL;

import org.eclipse.browser.view.BrowserMessages;
import org.eclipse.browser.view.BrowserPlugin;
import org.eclipse.browser.view.BrowserPluginImages;
import org.eclipse.browser.view.model.LinkModel;
import org.eclipse.browser.view.ui.actions.ExtensibleOpenLinkAction;
import org.eclipse.browser.view.ui.actions.IExtensibleAction;
import org.eclipse.browser.view.ui.actions.OpenLinkAction;
import org.eclipse.browser.view.ui.listeners.FormTextHyperlinkListener;
import org.eclipse.browser.view.ui.listeners.SashFormControlListener;
import org.eclipse.browser.view.ui.sections.ProjectLinksSection;
import org.eclipse.browser.view.ui.sections.SearchSection;
import org.eclipse.browser.view.ui.sections.UsefulLinksSection;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.osgi.util.NLS;
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
	
	private OpenLinkAction fOpenLinkAction = new ExtensibleOpenLinkAction();

	private SashForm fSashForm;
	
	public EclipseBrowserView() {
	}

	public void createPartControl(Composite parent) {		
		if (createDefaultModel()) {
			createUI(parent);
			createListeners();
			contributeToActionBars();
		}
	}

	private void createUI(Composite parent) {
		createUIToolkit(parent);
		fBaseComposite = createUIComposite(parent, 3, 8, GridData.FILL_HORIZONTAL);
		createUISashForm();
		createUIHeader();
		createUIBody();
		createUIFooter();

		fToolkit.adapt(fSashForm, true, true);
	}

	private void createUIFooter() {
		Composite parent = createUIComposite(fSashForm, 0, 0, GridData.FILL_HORIZONTAL);
		createUISeparator(parent);
		createUIFormTextEclipseImage(parent);		
	}

	private void createUIBody() {
		ScrolledForm form = createUIScrolledForm(fSashForm, 0, 0, GridData.FILL_BOTH);
		fManagedForm = new ManagedForm(fToolkit, form);
		createUISectionProjectLinks(form.getBody());
		if (BrowserPlugin.getUsefulLinkManager().getUsefulLinksCount() > 0) {
			createUISectionUsefulLinks(form.getBody());
		}
		createUISectionSearch(form.getBody());
	}
	
	private void createUIHeader() {
		Form form = createUIForm(fSashForm, 0, 0, GridData.FILL_BOTH);
		form.setText(BrowserMessages.EclipseBrowserView_title);
	}

	private void createUISashForm() {
		fSashForm = new SashForm (fBaseComposite, SWT.VERTICAL);
		fSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	private void createListeners() {
		fEclipseProjectLinksSection.createListeners();
		fEclipseUsefulLinksSection.createListeners();
		fEclipseSearchSection.createListeners();
		createListenersFormTextEclipseImage();
		createListenersSashForm();
	}
	
	private void createListenersSashForm() {
		fSashForm.addControlListener(new SashFormControlListener());		
	}

	private void createListenersFormTextEclipseImage() {
		fEclipseImageFormText.addHyperlinkListener(
				new FormTextHyperlinkListener(this));
	}
	
	private void createUISectionUsefulLinks(Composite parent) {
		int style = Section.DESCRIPTION | ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE;
		fEclipseUsefulLinksSection = new UsefulLinksSection(this,
				parent, fToolkit, style, BrowserMessages.EclipseBrowserView_sectionTitleUsefulLinks,
				BrowserMessages.EclipseBrowserView_sectionDescUsefulLinks);
		fEclipseUsefulLinksSection.createUI();		
	}

	private void createUISectionProjectLinks(Composite parent) {
		int style = Section.DESCRIPTION | ExpandableComposite.TITLE_BAR;
		fEclipseProjectLinksSection = new ProjectLinksSection(this, parent, fToolkit, style, BrowserMessages.EclipseBrowserView_sectionTitleProjectLinks, BrowserMessages.EclipseBrowserView_sectionDescProjectLinks);
		fEclipseProjectLinksSection.createUI();
	}

	private void createUISectionSearch(Composite parent) {
		int style = Section.DESCRIPTION | ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE;
		fEclipseSearchSection = new SearchSection(this, parent,
				fToolkit, style, BrowserMessages.EclipseBrowserView_sectionTitleSearch,
				BrowserMessages.EclipseBrowserView_sectionDescSearch);
		fEclipseSearchSection.createUI();			
	}
	
	private void createUIToolkit(Composite parent) {
		fToolkit = new FormToolkit(parent.getDisplay());
		fToolkit.getColors().initializeSectionToolBarColors();
		fToolkit.getHyperlinkGroup().setHyperlinkUnderlineMode(
				HyperlinkGroup.UNDERLINE_HOVER);
	}

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
	
	private void createUISeparator(Composite parent) {
		Composite separator = fToolkit.createCompositeSeparator(parent);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = 1;
		separator.setLayoutData(data);		
	}
	
	private void createUIFormTextEclipseImage(Composite parent) {	
		fEclipseImageFormText = fToolkit.createFormText(parent, true);
		String text = "<form><p><br/><a href=\"eclipse.org\"><img href=\"eclipse\"/></a></p></form>"; //$NON-NLS-1$
		fEclipseImageFormText.marginHeight = 0;
		fEclipseImageFormText.setText(text, true, false);
		fEclipseImageFormText.setImage("eclipse", BrowserPlugin.getImage(BrowserPluginImages.F_IMAGE_PATH_ECLIPSE_LOGO)); //$NON-NLS-1$
		
	}
	
	private boolean createDefaultModel() {	
		URL url = BrowserPlugin.getDefault().getBundle().getEntry(F_DEFAULT_MODEL_FILE);
		// Ensure the model XML file was found in the bundle
		if (url == null) {
			String message = NLS.bind(
					BrowserMessages.EclipseBrowserView_errorMissingModelFile, 
					url.toString());
			BrowserPlugin.logException(BrowserMessages.EclipseBrowserView_errorModelLoad, message, new Exception(message));
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
			BrowserPlugin.logException(BrowserMessages.EclipseBrowserView_errorModelLoad, e.getMessage(), e);
			return false;
		}
		
		return true;
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		if (fOpenLinkAction instanceof IExtensibleAction) {
			Action[] actions = ((IExtensibleAction)fOpenLinkAction).getActions();
			if (actions.length > 1) {
				for (int i = 0; i < actions.length; i++)
					manager.add(actions[i]);
			}
			if (actions.length > 0) {
				actions[0].setChecked(true);
			}
		}
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(fOpenLinkAction);
	}

	/**
	 * Passing the focus request to the project links section
	 */
	public void setFocus() {
		fEclipseProjectLinksSection.setFocus();
	}

	public OpenLinkAction getOpenLinkAction() {
		return fOpenLinkAction;
	}
	
	public LinkModel getModel() {
		return fModel;
	}
	
	public ManagedForm getManagedForm() {
		return fManagedForm;
	}

	public void dispose() {
		fManagedForm.dispose();
		super.dispose();
	}
}
