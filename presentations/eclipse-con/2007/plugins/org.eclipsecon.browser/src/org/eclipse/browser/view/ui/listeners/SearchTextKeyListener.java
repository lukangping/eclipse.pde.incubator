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

package org.eclipse.browser.view.ui.listeners;


import org.eclipse.browser.view.ui.sections.SearchSection;
import org.eclipse.browser.view.util.GoogleSearchLinkManager;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

/**
 * SearchTextKeyListener
 *
 */
public class SearchTextKeyListener extends KeyAdapter {

	private static final int F_DEC_ESCAPE = 27;
	
	private SearchSection fSection;		
	
	/**
	 * @param view
	 */
	public SearchTextKeyListener(SearchSection section) {
		fSection = section;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		handleKeyReleasedTextSearch(e);
	}	
	
	/**
	 * @param e
	 */
	private void handleKeyReleasedTextSearch(KeyEvent e) {
		if ((e.character == '\r') ||
				(e.character == '\n')) {
			// ENTER
			handleEnterTypedSearch();
		} else if (e.character == F_DEC_ESCAPE) {
			// DEC 27: ESC
			fSection.getSearchTextField().setText(""); //$NON-NLS-1$
		}
	}	
	
	/**
	 * 
	 */
	private void handleEnterTypedSearch() {
		fSection.getView().getOpenLinkAction().update(
				GoogleSearchLinkManager.getGoogleSearchLink(
						fSection.getView().getModel(), 
						fSection.getSearchTextField().getText()));
		fSection.getView().getOpenLinkAction().run();				
	}	
	
}
