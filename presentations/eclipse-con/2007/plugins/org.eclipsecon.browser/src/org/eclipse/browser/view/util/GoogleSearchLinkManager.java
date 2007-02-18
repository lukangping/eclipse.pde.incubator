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

package org.eclipse.browser.view.util;

import org.eclipse.browser.view.BrowserMessages;
import org.eclipse.browser.view.model.LinkModel;
import org.eclipse.browser.view.model.LinkObject;

/**
 * ImageLinkManager
 *
 */
public class GoogleSearchLinkManager {

	private static final char F_PERCENTAGE = '%';

	private static final int F_DEC_LOWER_Z = 122;

	private static final int F_DEC_LOWER_A = 97;

	private static final int F_DEC_UPPER_Z = 90;

	private static final int F_DEC_UPPER_A = 65;

	private static final int F_DEC_NINE = 57;

	private static final int F_DEC_ZERO = 48;

	private static final int F_DEC_SPACE = 32;

	public final static String F_GOOGLE_QUERY_URL = "http://www.google.com/search?q="; //$NON-NLS-1$
	
	private LinkObject fGoogleSearchLink;
	
	private static GoogleSearchLinkManager fInstance;
	
	/**
	 * 
	 */
	protected GoogleSearchLinkManager() {
		fGoogleSearchLink = null;
	}

	/**
	 * @return
	 */
	public static GoogleSearchLinkManager getInstance() {
		if (fInstance == null) {
			fInstance = new GoogleSearchLinkManager();
		}
		return fInstance;
	}
	
	/**
	 * @param model
	 * @return
	 */
	public static LinkObject getGoogleSearchLink(LinkModel model, String searchText) {
		return getInstance().createGoogleSearchLink(model, searchText);
	}
	
	/**
	 * @param model
	 * @return
	 */
	public LinkObject createGoogleSearchLink(LinkModel model, String searchText) {
		
		// Assemble search query URL
		String full_url = F_GOOGLE_QUERY_URL + encodeSearchText(searchText);
		// Create a new google search link if none exists; otherwise, resue the
		// old one
		if (fGoogleSearchLink == null) {
			fGoogleSearchLink = model.getModelFactory().createComponentLink(null);
			fGoogleSearchLink.setFieldName(BrowserMessages.GoogleSearchLinkManager_linkNameGoogleSearch);
			fGoogleSearchLink.setFieldLink(full_url);
			fGoogleSearchLink.setFieldDescription(BrowserMessages.GoogleSearchLinkManager_linkDescriptionGoogleSearch);			
		} else {
			fGoogleSearchLink.setFieldLink(full_url);
		}
		return fGoogleSearchLink;
	}

	/**
	 * @param searchText
	 * @return
	 */
	private String encodeSearchText(String searchText) {
		StringBuffer buffer = new StringBuffer();
		char previous_char = F_DEC_SPACE;
		for (int i = 0; i < searchText.length(); i++) {
			char current_char = searchText.charAt(i);
			// Cannot use Character class for tests
			// See URL specification: http://www.ietf.org/rfc/rfc1738.txt
			// DEC 48 to 57:  0 to 9
			// DEC 65 to 90:  A to Z
			// DEC 97 to 122: a to z
			// DEC 32: space
			if ((current_char == F_DEC_SPACE) && (previous_char == F_DEC_SPACE)) {
				// Ignore consecutive spaces
			} else if (((current_char >= F_DEC_ZERO) && (current_char <= F_DEC_NINE)) ||
					(current_char >= F_DEC_UPPER_A) && (current_char <= F_DEC_UPPER_Z) ||
					(current_char >= F_DEC_LOWER_A) && (current_char <= F_DEC_LOWER_Z)) {
				// Append the alphanumeric character as is
				buffer.append(current_char);
			} else {
				// Encode the non-alphanumeric character into its hex value
				buffer.append(F_PERCENTAGE);
				buffer.append(Integer.toHexString(current_char));
			}
			
			previous_char = current_char;
		}
		return buffer.toString();
	}	
	
}
