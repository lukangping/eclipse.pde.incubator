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

import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.mylyn.zest.core.viewers.EntityConnectionData;
import org.eclipse.mylyn.zest.core.viewers.GraphViewer;
import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * 
 * @author Ian Bull
 * 
 */
public class PathDependencyAnalysis extends AbstractVisualizationLabelProvider {

	public PathDependencyAnalysis(GraphViewer viewer) {
		super(viewer);
	}

	protected void calculateInterestingDependencies(HashSet interestingRels, HashSet interestingEntities) {

		if (this.getSelected() != null) {
			BundleDescription[] descriptions = AnalysisUtil.getAllCallers(this.getSelected(), AnalysisUtil.getPrerequisites(new BundleDescription[] { this.rootNode }));
			for (int i = 0; i < descriptions.length; i++) {
				for (int j = 0; j < descriptions.length; j++) {
					EntityConnectionData entityConnectionData = new EntityConnectionData(descriptions[i], descriptions[j]);
					interestingRels.add(entityConnectionData);
				}
			}
			interestingEntities.addAll(Arrays.asList(descriptions));
		}
	}

}
