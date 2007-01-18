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
 * EclipseLinkModelFactory
 *
 */
public class EclipseLinkModelFactory {

	private EclipseLinkModel fModel;
	
	/**
	 * 
	 */
	public EclipseLinkModelFactory(EclipseLinkModel model) {
		fModel = model;
	}

	/**
	 * @param parent
	 * @return
	 */
	public EclipseLinkObject createProjectLink() {
		return new EclipseProjectLink(fModel);
	}	
	
	/**
	 * @param parent
	 * @return
	 */
	public EclipseLinkObject createComponentLink(EclipseLinkObject parent) {
		return new EclipseComponentLink(fModel, parent);
	}
	
	/**
	 * @param parent
	 * @return
	 */
	public EclipseLinkObject createSubProjectLink(EclipseLinkObject parent) {
		return new EclipseSubProjectLink(fModel, parent);
	}
}
