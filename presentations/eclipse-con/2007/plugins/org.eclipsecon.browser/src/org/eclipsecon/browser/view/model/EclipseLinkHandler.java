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

import java.io.StringReader;
import java.util.Stack;

import org.eclipsecon.browser.view.BrowserMessages;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * EclipseLinkHandler
 *
 */
public class EclipseLinkHandler extends DefaultHandler {

	public final static String F_ELEMENT_PROJECT = "project"; //$NON-NLS-1$
	
	public final static String F_ELEMENT_SUBPROJECT = "subproject"; //$NON-NLS-1$
	
	public final static String F_ELEMENT_COMPONENT = "component"; //$NON-NLS-1$
	
	public final static String F_ATTRIBUTE_NAME = "name"; //$NON-NLS-1$

	public final static String F_ATTRIBUTE_LINK = "link"; //$NON-NLS-1$

	public final static String F_ATTRIBUTE_DESCRIPTION = "description"; //$NON-NLS-1$
	
	private Stack fLinkObjectStack;
	
	private EclipseLinkObject fRoot;	
	
	private EclipseLinkModel fModel;
	
	/**
	 * 
	 */
	public EclipseLinkHandler(EclipseLinkModel model) {
		fModel = model;
		fLinkObjectStack = null;
	}
	
	/**
	 * @return
	 */
	public EclipseLinkObject getRootLinkObject() {
		return fRoot;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		fLinkObjectStack = new Stack();
		fRoot = null;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {

		EclipseLinkObject linkObject = null;
		EclipseLinkObject parent = null;
		
		// Get the attributes
		String attr_name = attributes.getValue(F_ATTRIBUTE_NAME);
		String attr_link = attributes.getValue(F_ATTRIBUTE_LINK);
		String attr_description = attributes.getValue(F_ATTRIBUTE_DESCRIPTION);
		// Ensure the attributes are all defined
		if ((attr_name == null) ||
				(attr_link == null) ||
				(attr_description == null)) {
			throw new SAXException(BrowserMessages.EclipseLinkHandler_errorAttributeMissing);
		}

		// Get the parent object
		// If the stack is empty, then we have a root node
		if (fLinkObjectStack.empty() == false) {
			parent = (EclipseLinkObject)fLinkObjectStack.peek();
		}
		// Build the model
		// Process project, subproject or component elements
		if (name.equals(F_ELEMENT_PROJECT)) {
			linkObject = fModel.getModelFactory().createProjectLink();
			fRoot = linkObject;
		} else if (name.equals(F_ELEMENT_SUBPROJECT)) {
			// Project element
			linkObject = fModel.getModelFactory().createSubProjectLink(parent);
			parent.addFieldChild(linkObject);
		} else if (name.equals(F_ELEMENT_COMPONENT)) {
			// Component element
			linkObject = fModel.getModelFactory().createComponentLink(parent);
			parent.addFieldChild(linkObject);
		} else {
			// Unrecognized element
			throw new SAXException(
					BrowserMessages.bind(BrowserMessages.EclipseLinkHandler_errorUnrecognizedElement, name));
		}		
		// Set the link object fields
		linkObject.setFieldName(attr_name);
		linkObject.setFieldLink(attr_link);
		linkObject.setFieldDescription(attr_description);
		
		// Add this object to the top of the stack
		// Children from future startElement events will be added as children
		// to this object
		fLinkObjectStack.push(linkObject);
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		// Remove the top object off the stack
		// We are done processing that object and its children
		fLinkObjectStack.pop();
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#resolveEntity(java.lang.String, java.lang.String)
	 */
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
		// Prevent the resolution of external entities in order to
		// prevent the parser from accessing the Internet
		// This will prevent huge workbench performance degradations and hangs
		return new InputSource(new StringReader("")); //$NON-NLS-1$
	}	
	
}
