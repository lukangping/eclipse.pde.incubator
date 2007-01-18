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

package org.eclipsecon.browser.view.ui.listeners;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipsecon.browser.view.ui.sections.EclipseProjectLinksSection;

/**
 * HandCursorDisposeListener
 *
 */
public class HandCursorDisposeListener implements DisposeListener {

	private EclipseProjectLinksSection fSection;		

	/**
	 * @param view
	 */
	public HandCursorDisposeListener(EclipseProjectLinksSection section) {
		fSection = section;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent e) {
		Cursor handCursor = fSection.getHandCursor();
		if ((handCursor != null) &&
				(handCursor.isDisposed() == false)) {
			handCursor.dispose();
		}
	}

}
