/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Wojciech Galanciak <wojciech.galanciak@gmail.com> - bug 282804
 *******************************************************************************/
package org.eclipse.pde.runtime.core.model;

/**
 * Delta model objects are of type IBundle, IService, IExtension, IExtensionPoint
 */
public class ModelChangeDelta {

	public static final int ADDED = 0;
	public static final int UPDATED = 1;
	public static final int REMOVED = 2;
	public static final int STARTING = 3;
	public static final int STARTED = 4;
	public static final int STOPPING = 5;
	public static final int STOPPED = 6;
	public static final int RESOLVED = 7;
	public static final int UNRESOLVED = 8;

	// TODO SWITCH from HEAVY ModelChangeDelta carrying whole object, to LIGHT delta - name, type (Bundle/Service/Ext/ExtPt), id
	private ModelObject fObject;
	private int fFlag;

	public ModelChangeDelta() {
		// empty
	}

	public ModelChangeDelta(ModelObject object, int flag) {
		fObject = object;
		fFlag = flag;
	}

	public ModelObject getModelObject() {
		return fObject;
	}

	public int getFlag() {
		return fFlag;
	}

	public void setModelObject(ModelObject fObject) {
		this.fObject = fObject;
	}

	public void setFlag(int flag) {
		this.fFlag = flag;
	}
}
