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
public class EclipseProjectLink extends EclipseLinkObject {

	/**
	 * @param link
	 * @param name
	 * @param description
	 */
	public EclipseProjectLink(EclipseLinkModel model) {
		super(model, null);
	}

	/* (non-Javadoc)
	 * @see org.eclipsecon.browser.view.model.EclipseLinkObject#getType()
	 */
	public int getType() {
		return F_TYPE_PROJECT;
	}

}
