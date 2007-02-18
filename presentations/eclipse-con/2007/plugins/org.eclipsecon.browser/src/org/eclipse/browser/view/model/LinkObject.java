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

package org.eclipse.browser.view.model;

import java.util.ArrayList;
import java.util.List;

/**
 * EclipseProjectLinkObject
 *
 */
public abstract class LinkObject {

	public final static int F_TYPE_PROJECT = 0x01;	
	
	public final static int F_TYPE_SUBPROJECT = 0x02;
	
	public final static int F_TYPE_COMPONENT = 0x04;
	
	private ArrayList fFieldChildren;
	
	private String fFieldLink;

	private String fFieldDescription;
	
	private String fFieldName;
	
	private LinkObject fParent;
	
	private LinkModel fModel;
	
	/**
	 * @param link
	 * @param name
	 */
	public LinkObject(LinkModel model, LinkObject parent) {
		fModel = model;
		fParent = parent;
		fFieldChildren = new ArrayList();
	}
	
	/**
	 * @param child
	 */
	public void addFieldChild(LinkObject child) {
		fFieldChildren.add(child);
	}
	
	/**
	 * @return
	 */
	public List getFieldChildren() {
		return fFieldChildren;
	}
	
	/**
	 * @return
	 */
	public String getFieldName() {
		return fFieldName;
	}
	
	/**
	 * @return
	 */
	public String getFieldLink() {
		return fFieldLink;
	}
	
	/**
	 * @return
	 */
	public String getFieldDescription() {
		return fFieldDescription;
	}

	/**
	 * @param name
	 */
	public void setFieldName(String name) {
		fFieldName = name;
	}
	
	/**
	 * @param link
	 */
	public void setFieldLink(String link) {
		fFieldLink = link;
	}
	
	/**
	 * @param description
	 */
	public void setFieldDescription(String description) {
		fFieldDescription = description;
	}	
	
	/**
	 * @return
	 */
	public LinkObject getParent() {
		return fParent;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return fFieldName;
	}
	
	/**
	 * @return
	 */
	public boolean hasChildren() {
		if (fFieldChildren.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return
	 */
	public LinkModel getModel() {
		return fModel;
	}
	
	/**
	 * @return
	 */
	public abstract int getType();
	
}
