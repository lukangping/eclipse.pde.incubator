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

package org.eclipse.browser.view.model;

/**
 * ComponentLink
 *
 */
public class ComponentLink extends LinkObject {

	/**
	 * @param link
	 * @param name
	 * @param description
	 */
	public ComponentLink(LinkModel model, LinkObject parent) {
		super(model, parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.browser.view.model.LinkObject#getType()
	 */
	public int getType() {
		return F_TYPE_COMPONENT;
	}

}
