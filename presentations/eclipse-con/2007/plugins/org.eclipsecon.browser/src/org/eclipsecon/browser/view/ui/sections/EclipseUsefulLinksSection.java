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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipsecon.browser.view.BrowserPlugin;
import org.eclipsecon.browser.view.BrowserPluginImages;
import org.eclipsecon.browser.view.model.EclipseLinkObject;
import org.eclipsecon.browser.view.ui.EclipseLinksView;
import org.eclipsecon.browser.view.ui.listeners.FormTextHyperlinkListener;

/**
 * EclipseUsefulLinksSection
 *
 */
public class EclipseUsefulLinksSection extends EclipseLinkSection {
	
	private FormText fUsefulLinksFormText;	
	
	/**
	 * @param view
	 * @param parent
	 * @param toolkit
	 * @param style
	 * @param text
	 * @param description
	 */
	public EclipseUsefulLinksSection(EclipseLinksView view, Composite parent,
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
		createUIFormTextLinks(client);
		getToolkit().paintBordersFor(client);
		getSection().setClient(client);
	}

	/**
	 * @param parent
	 */
	private void createUIFormTextLinks(Composite parent) {
		EclipseLinkObject[] linkObjects = 
			BrowserPlugin.getUsefulLinkManager().getUsefulLinks(getView().getModel());
		StringBuffer formTextBuffer = new StringBuffer("<form>"); //$NON-NLS-1$
		//
		for (int i = 0; i < linkObjects.length; i++) {
			formTextBuffer.append("<li style=\"image\" value=\"world\" bindent=\"5\">"); //$NON-NLS-1$
			formTextBuffer.append("<a href=\""); //$NON-NLS-1$
			formTextBuffer.append(linkObjects[i].getFieldLink().hashCode());
			formTextBuffer.append("\">"); //$NON-NLS-1$
			formTextBuffer.append(linkObjects[i].getFieldName());
			formTextBuffer.append("</a></li>"); //$NON-NLS-1$
		}
		formTextBuffer.append("</form>"); //$NON-NLS-1$
		
		//		
		fUsefulLinksFormText = getToolkit().createFormText(parent, true);
		fUsefulLinksFormText.marginHeight = 2;		
		fUsefulLinksFormText.setText(formTextBuffer.toString(), true, false);
		fUsefulLinksFormText.setImage("world", BrowserPlugin.getImage(BrowserPluginImages.F_IMAGE_PATH_LINK)); //$NON-NLS-1$
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipsecon.browser.view.ui.EclipseLinkSection#createListeners()
	 */
	public void createListeners() {
		//
		createListenersFormTextLinks();
	}	
	
	/**
	 * 
	 */
	private void createListenersFormTextLinks() {
		//
		if (fUsefulLinksFormText == null) {
			return;
		}
		//
		fUsefulLinksFormText.addHyperlinkListener(
				new FormTextHyperlinkListener(getView()));
	}
	
}
