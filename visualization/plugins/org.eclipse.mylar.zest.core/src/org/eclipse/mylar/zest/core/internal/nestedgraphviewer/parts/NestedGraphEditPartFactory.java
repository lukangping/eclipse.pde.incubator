/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.mylar.zest.core.internal.gefx.ZestRootEditPart;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.NonNestedProxyNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.ProxyConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedPane;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphConnectionEditPart;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPartFactory;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.ProxyConnectionEditPart;


/**
 * Creates the edit parts associated with the different models.
 * @author Chris Callendar
 */
public class NestedGraphEditPartFactory extends GraphEditPartFactory {

	private boolean enforceBounds = false;
	
	public NestedGraphEditPartFactory(ZestRootEditPart graphRootEditPart, boolean allowOverlap, boolean enforceBounds) {
		super( graphRootEditPart );
		this.enforceBounds = enforceBounds;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof NonNestedProxyNode){
			editPart = new NonNestedGraphProxyNodeEditPart();
		} else if (model instanceof ProxyConnection){
			editPart = new ProxyConnectionEditPart();
		} else if (model instanceof NestedGraphModelNode) {
			editPart = new NestedGraphNodeEditPart(enforceBounds);
		} else if (model instanceof NestedGraphModel) {
			editPart = new NestedGraphEditPart();
			//((NestedGraphModel)model).clearProxies();
			graphRootEditPart.setModelRootEditPart(editPart);
			
		} else if (model instanceof IGraphModelConnection) {
			editPart = new GraphConnectionEditPart();
		} else if ( model instanceof NestedPane ) {
			editPart = new NestedPaneAreaEditPart(((NestedPane)model).getPaneType(), ((NestedPane)model).isClosed());
		
		} else {
			editPart = super.createEditPart(context, model);
		}
		editPart.setModel(model);
		
		return editPart;
	}

}
