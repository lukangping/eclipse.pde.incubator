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

package org.eclipse.browser.view.model;

/**
 * ProjectLink
 *
 */
public class ProjectLink extends LinkObject {

	/**
	 * @param link
	 * @param name
	 * @param description
	 */
	public ProjectLink(LinkModel model) {
		super(model, null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.browser.view.model.LinkObject#getType()
	 */
	public int getType() {
		return F_TYPE_PROJECT;
	}

}
