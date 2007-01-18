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

package org.eclipsecon.browser.view.model;

/**
 * EclipseProjectLink
 *
 */
public class EclipseSubProjectLink extends EclipseLinkObject {

	/**
	 * @param link
	 * @param name
	 * @param description
	 */
	public EclipseSubProjectLink(EclipseLinkModel model, EclipseLinkObject parent) {
		super(model, parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipsecon.browser.view.model.EclipseLinkObject#getType()
	 */
	public int getType() {
		return F_TYPE_SUBPROJECT;
	}

}
