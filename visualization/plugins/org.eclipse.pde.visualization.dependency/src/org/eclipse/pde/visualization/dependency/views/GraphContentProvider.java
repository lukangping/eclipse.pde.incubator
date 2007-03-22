/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria IBM CAS, IBM Toronto
 * Lab
 ******************************************************************************/
package org.eclipse.pde.visualization.dependency.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylar.zest.core.viewer.IGraphEntityContentProvider;
import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * 
 * @author Ian Bull
 * 
 */
class GraphContentProvider implements IGraphEntityContentProvider {

	BundleDescription currentBundle = null;

	private BundleDescription[] getDependencies(BundleDescription bundle) {
		if (bundle != null) {
			return AnalysisUtil.getPrerequisites(new BundleDescription[] { currentBundle });
		}
		return new BundleDescription[0];
	}

	public Object[] getConnectedTo(Object entity) {
		BundleDescription e = (BundleDescription) entity;
		return AnalysisUtil.getDependencies(e);
	}

	public Object[] getElements(Object inputElement) {
		return getDependencies((BundleDescription) inputElement);

	}

	public double getWeight(Object entity1, Object entity2) {
		return 0;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		currentBundle = (BundleDescription) newInput;

	}

}