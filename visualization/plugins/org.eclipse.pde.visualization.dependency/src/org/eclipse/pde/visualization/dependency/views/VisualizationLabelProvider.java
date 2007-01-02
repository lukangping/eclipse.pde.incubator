/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *     IBM CAS, IBM Toronto Lab
 *******************************************************************************/
package org.eclipse.pde.visualization.dependency.views;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * A content provider to represent the different types of label providers we have in pde.visualization
 * 
 * @author Ian Bull
 */
public interface VisualizationLabelProvider extends ILabelProvider {

	/**
	 * Sets the currently selected Object
	 * @param root The root node in this dependency
	 * @param currentSelection The currently selected node
	 */
	public void setCurrentSelection( Object root, Object currentSelection );
	
	/**
	 * Get all the interesting relationships.  These can be used to raise them
	 * to the front layer
	 * @return
	 */
	public Object[] getInterestingRelationships( );
	
	/**
	 * Sets a node to be pinned on this viewer
	 * @param pinnedNode
	 */
	public void setPinnedNode(BundleDescription pinnedNode);
}