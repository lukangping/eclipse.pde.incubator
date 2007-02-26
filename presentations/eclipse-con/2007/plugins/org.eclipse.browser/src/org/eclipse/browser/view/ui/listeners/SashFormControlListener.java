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

package org.eclipse.browser.view.ui.listeners;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;

/**
 * SashFormControlListener
 *
 */
public class SashFormControlListener implements ControlListener {

	private int fCurrentHeight;
	
	private final float F_HEADER_HEIGHT_LIMIT = 30;

	private final float F_FOOTER_HEIGHT_LIMIT = 55;
	
	
	/**
	 * 
	 */
	public SashFormControlListener() {
		fCurrentHeight = 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
	 */
	public void controlMoved(ControlEvent e) {
		// NO-OP
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
	 */
	public void controlResized(ControlEvent e) {
		//
		if ((e.widget instanceof SashForm) == false) {
			return;
		}
		
		SashForm sashForm = (SashForm)e.widget;
		fCurrentHeight = sashForm.getClientArea().height;
		
		int header_weight = 1;
		int body_weight = 0;
		int footer_weight = 0;
		
		if (fCurrentHeight <= F_HEADER_HEIGHT_LIMIT) {
			// NO-OP
		} else if (fCurrentHeight <= (F_FOOTER_HEIGHT_LIMIT + F_HEADER_HEIGHT_LIMIT)) {
			header_weight = calculateWeight(F_HEADER_HEIGHT_LIMIT);
			footer_weight = calculateWeight(fCurrentHeight - F_HEADER_HEIGHT_LIMIT);
		} else {
			header_weight = calculateWeight(F_HEADER_HEIGHT_LIMIT);
			footer_weight = calculateWeight(F_FOOTER_HEIGHT_LIMIT);
			body_weight = calculateWeight(fCurrentHeight - F_FOOTER_HEIGHT_LIMIT - F_HEADER_HEIGHT_LIMIT);
		}
		
		sashForm.setWeights(new int[] {header_weight, body_weight, footer_weight});

	}

	/**
	 * @param limit
	 * @return
	 */
	private int calculateWeight(float limit) {
		if (fCurrentHeight == 0) {
			return 1;
		}
		return Math.round((limit / fCurrentHeight) * 100);
	}	
	
}
