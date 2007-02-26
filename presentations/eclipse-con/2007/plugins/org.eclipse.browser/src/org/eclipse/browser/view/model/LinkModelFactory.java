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
 * LinkModelFactory
 *
 */
public class LinkModelFactory {

	private LinkModel fModel;
	
	/**
	 * 
	 */
	public LinkModelFactory(LinkModel model) {
		fModel = model;
	}

	/**
	 * @param parent
	 * @return
	 */
	public LinkObject createProjectLink() {
		return new ProjectLink(fModel);
	}	
	
	/**
	 * @param parent
	 * @return
	 */
	public LinkObject createComponentLink(LinkObject parent) {
		return new ComponentLink(fModel, parent);
	}
	
	/**
	 * @param parent
	 * @return
	 */
	public LinkObject createSubProjectLink(LinkObject parent) {
		return new SubProjectLink(fModel, parent);
	}
}
