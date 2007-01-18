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

package org.eclipsecon.browser.view;

import org.eclipse.osgi.util.NLS;

/**
 * BrowserMessages
 *
 */
public class BrowserMessages extends NLS {
	
	private static final String BUNDLE_NAME = "org.eclipsecon.browser.view.messages"; //$NON-NLS-1$

	public static String CollapseAction_actionDescCollapseAll;

	public static String CollapseAction_actionNameCollapseAll;

	public static String EclipseLinkHandler_errorAttributeMissing;

	public static String EclipseLinkHandler_errorUnrecognizedElement;

	public static String EclipseLinksView_errorMissingModelFile;

	public static String EclipseLinksView_errorModelLoad;

	public static String EclipseLinksView_sectionDescProjectLinks;

	public static String EclipseLinksView_sectionDescSearch;

	public static String EclipseLinksView_sectionDescUsefulLinks;

	public static String EclipseLinksView_sectionTitleProjectLinks;

	public static String EclipseLinksView_sectionTitleSearch;

	public static String EclipseLinksView_sectionTitleUsefulLinks;

	public static String EclipseLinksView_titleEclipseLinks;

	public static String EclipseSearchSection_buttonNameSearch;

	public static String ExternalBrowserAction_actionNameExternalBrowser;

	public static String ExternalBrowserAction_toolTipExternalBrowser;

	public static String FormTextHyperlinkListener_errorCannotResolveHref;

	public static String GoogleSearchLinkManager_linkDescriptionGoogleSearch;

	public static String GoogleSearchLinkManager_linkNameGoogleSearch;

	public static String ImageLinkManager_linkDescEclipse;

	public static String ImageLinkManager_linkDescGoogle;

	public static String ImageLinkManager_linkNameEclipse;

	public static String ImageLinkManager_linkNameGoogle;

	public static String OpenLinkAction_actionNameOpenLink;

	public static String OpenLinkAction_errorLinkAction;

	public static String OpenLinkAction_errorUrlMalformed;

	public static String OpenLinkAction_errorUrlNotSpecified;

	public static String OpenLinkAction_errorWorkbenchInitialization;

	public static String OpenLinkAction_toolTipOpenLink;

	public static String UsefulLinkManager_errorRequiredAttributeMissing;

	public static String UsefulLinkManager_errorMissingAttribute;

	public static String UsefulLinkManager_defaultMsgDescriptionNotSpecified;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, BrowserMessages.class);
	}

	/**
	 * 
	 */
	private BrowserMessages() {
		// NO-OP
	}
}
