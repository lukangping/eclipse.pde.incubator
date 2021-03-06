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
package org.eclipse.mylar.zest.core.internal.gefx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
public abstract class NonThreadedGraphicalViewer extends ScrollingGraphicalViewer {

	private List revealListeners = null;
	
	/**
	 * ThreadedGraphicalViewer constructor.
	 * @param parent The composite that this viewer will be added to.
	 */
	public NonThreadedGraphicalViewer(Composite parent)  {
		super();
		
		revealListeners = new ArrayList(1);
		
		// create the FigureCanvas
		createControl(parent);
		EditDomain ed = new DefaultEditDomain( null );
		ed.addViewer( this );
		setEditDomain( ed );
		hookControl();
		//getFigureCanvas().setScrollBarVisibility(FigureCanvas.NEVER);
		

		getControl().addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (!revealListeners.isEmpty()) {
					// Go through the reveal list and let everyone know that the view
					// is now available.  Remove the listeners so they are only called once!
					Iterator iterator = revealListeners.iterator();
					while (iterator.hasNext() ) {
						RevealListener reveallisetner =  (RevealListener) iterator.next();
						reveallisetner.revealed(getControl());
						iterator.remove();
					}
				}
			}
			
		});

		getControl().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
			}
		});
	
		((Canvas)getControl()).setBackground( ColorConstants.white );
		
		// Scroll-wheel Zoom
		setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.ScrollingGraphicalViewer#reveal(org.eclipse.gef.EditPart)
	 */
	public void reveal(EditPart part) {
		// @tag rootEditPart : Don't reveal root edit parts
		if  (!(part instanceof GraphRootEditPart))
			super.reveal(part);
	}
	
	private class MyRunnable implements Runnable{
		boolean isVisible;
		public void run() { isVisible = getControl().isVisible();}
	}
	
	/**
	 * Adds a reveal listener to the view.  Note:  A reveal listener will
	 * only every be called ONCE!!! even if a view comes and goes. There 
	 * is no remove reveal listener.
	 * @param revealListener
	 */
	public void addRevealListener( final RevealListener revealListener ) {
		
		MyRunnable myRunnable = new MyRunnable();
		PlatformUI.getWorkbench().getDisplay().syncExec( myRunnable);
		
		if ( myRunnable.isVisible ) 
			revealListener.revealed( (Composite)getControl() );
		else 
			revealListeners.add(revealListener);
	}
	
	/**
	 * Does some initializing of the viewer.
	 */
	protected abstract void configureGraphicalViewer();
		
	/**
	 * Sets the contents of the viewer and configures the graphical viewer.
	 * @param model
	 */
	public void setContents(Object model) {
		this.configureGraphicalViewer();
		super.setContents(model);
		//@tag zest.experimental.contents : publish a property change that the model has changed. This will allow linked viewers to update.
		setProperty(IZestViewerProperties.GRAPH_VIEWER_CONTENTS, model);
		
	}
	
	/**
	 * Updates the contents <b>without</b> configuring the graphical viewer.
	 * Only call this if the graphical viewer has already be configured.
	 * @param model
	 */
	public void updateContents(Object model) {
		super.setContents(model);
		//@tag zest.experimental.contents : publish a property change that the model has changed. This will allow linked viewers to update.
		setProperty(IZestViewerProperties.GRAPH_VIEWER_CONTENTS, model);
	}

	/**
	 * Gets the absolute size of the canvas.
	 * @return Dimension in absolute coords
	 */
	public Dimension getCanvasSize() {
		Dimension dim = new Dimension(getFigureCanvas().getSize());
		dim.shrink(getFigureCanvas().getBorderWidth(), getFigureCanvas().getBorderWidth());
		return dim;
	}
		
	/**
	 * Gets the translated size of the canvas.
	 * @return Dimension relative
	 */
	public Dimension getTranslatedCanvasSize() {
		Dimension dim = getCanvasSize();
		//mainCanvas.getViewport().translateToRelative(dim);
		return dim;
	}
}
