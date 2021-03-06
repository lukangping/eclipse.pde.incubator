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
package org.eclipse.mylar.zest.core.internal.graphviewer.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.Viewport;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.policies.GraphXYLayoutEditPolicy;
import org.eclipse.mylar.zest.core.internal.viewers.figures.AspectRatioFreeformLayer;


/**
 * The EditPart associated with the GraphModel.  The view creates a FreeformLayer figure.
 *  
 * @author Chris Callendar
 */
public class GraphEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

	//@tag zest.bug.156286-Zooming.fix : add a zoom manager to the edit part.
	ZoomManager zoomer;
	
	public GraphEditPart() {
		super();
	}
	
	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			((GraphItem) getModel()).addPropertyChangeListener(this);
		}
	}	
	
	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((GraphItem) getModel()).removePropertyChangeListener(this);
		}
	}
	
	/**
	 * Sets the scale for the graph
	 * @param x
	 * @param y
	 */
	public void setScale( double x, double y ) {
		AspectRatioFreeformLayer aspectRatioFreeformLayer = (AspectRatioFreeformLayer) getFigure();
		aspectRatioFreeformLayer.setScale( x, y );
		//aspectRatioFreeformLayer.repaint();
		getCastedModel().fireAllPropertyChange(IGraphModelNode.FORCE_REDRAW, null, null);	
	}

	/**
	 * Gets the scale in the X Direction
	 * @return
	 */
	public double getXScale() {
		AspectRatioFreeformLayer aspectRatioFreeformLayer = (AspectRatioFreeformLayer) getFigure();
		return aspectRatioFreeformLayer.getWidthScale();
	}
	
	/**
	 * Getes the scale in the Y Direction
	 * @return
	 */
	public double getYScale() {
		AspectRatioFreeformLayer aspectRatioFreeformLayer = (AspectRatioFreeformLayer) getFigure();
		return aspectRatioFreeformLayer.getHeightScale();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		
		AspectRatioFreeformLayer aspectRatioScaledFigure = new AspectRatioFreeformLayer("root");
		aspectRatioScaledFigure.setScale(1.0, 1.0);
		aspectRatioScaledFigure.addLayoutListener(LayoutAnimator.getDefault());
		zoomer = new ZoomManager(aspectRatioScaledFigure, (Viewport)((GraphicalEditPart)getRoot()).getFigure());
		return aspectRatioScaledFigure;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE,  new GraphXYLayoutEditPolicy());
		
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		// these properties are fired when Nodes and connections are added into or removed from 
		// the LayoutDiagram instance and must cause a call of refreshChildren()
		// to update the diagram's contents.
		if (GraphModel.NODE_ADDED_PROP.equals(prop) || GraphModel.NODE_REMOVED_PROP.equals(prop)) {
			refreshChildren();
		} else if ( IGraphModelNode.HIGHLIGHT_PROP.equals( prop ) ) {
			if (((Boolean)evt.getNewValue()).booleanValue()) {
				setSelected(1);
			} else {
				setSelected(0);
			}
		}
	}
		
	private GraphModel getCastedModel() {
		return (GraphModel)getModel();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return getCastedModel().getNodes();
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#register()
	 */
	protected void register() {
		super.register();
		//@tag zest.bug.156286-Scaling.fix : set a property so that this can be zoomed on.
		getViewer().setProperty(ZoomManager.class.toString(), getZoomManager());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#unregister()
	 */
	protected void unregister() {
		super.unregister();
//		@tag zest.bug.156286-Scaling.fix : set a property so that this can be zoomed on.
		getViewer().setProperty(ZoomManager.class.toString(), null);
	}
	
	//@tag zest.bug.156286-Scaling.fix : get the zoom manager
	public ZoomManager getZoomManager() {
		return zoomer;
	}

}
