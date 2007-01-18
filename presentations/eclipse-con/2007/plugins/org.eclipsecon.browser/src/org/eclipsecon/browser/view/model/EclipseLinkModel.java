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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;


/**
 * EclipseProjectLinkModel
 *
 */
public class EclipseLinkModel {

	private URL fModelURL;
	
	private EclipseLinkObject fRoot;
	
	private SAXParserFactory fParserFactory;
	
	private SAXParser fParser;
	
	private EclipseLinkHandler fParserHandler;
	
	private EclipseLinkModelFactory fModelFactory;
	
	/**
	 * 
	 */
	public EclipseLinkModel(URL file) {
		fModelURL = file;
		
		initialize();
	}

	/**
	 * @return
	 */
	public EclipseLinkObject getRootLinkObject() {
		return fRoot;
	}
	
	/**
	 * 
	 */
	private void initialize() {
		fRoot = null;
		fParserFactory = null;
		fParser = null;
		fParserHandler = null;
	}
	
	/**
	 * @return
	 */
	private SAXParserFactory getSAXParserFactory() {
		if (fParserFactory == null) {
			fParserFactory = SAXParserFactory.newInstance();
			fParserFactory.setNamespaceAware(true);
			fParserFactory.setValidating(true);
		}
		return fParserFactory;
	}
	
	/**
	 * @return
	 */
	private SAXParser getSAXParser() throws ParserConfigurationException,
			SAXException {
		if (fParser == null) {
			fParser = getSAXParserFactory().newSAXParser();
		}
		return fParser;
	}

	/**
	 * @return
	 */
	private EclipseLinkHandler getSAXHandler() {
		if (fParserHandler == null) {
			fParserHandler = new EclipseLinkHandler(this);
		}
		return fParserHandler;
	}

	/**
	 * @return
	 */
	public EclipseLinkModelFactory getModelFactory() {
		if (fModelFactory == null) {
			fModelFactory = new EclipseLinkModelFactory(this);
		}
		return fModelFactory;
	}	
	
	/**
	 * @throws CoreException
	 */
	public void load() throws IOException, ParserConfigurationException, SAXException {
		
		InputStream stream = null;
		try {
			stream = new BufferedInputStream(fModelURL.openStream());
			SAXParser parser = getSAXParser();
			EclipseLinkHandler handler = getSAXHandler();
			parser.parse(stream, handler);
			fRoot = handler.getRootLinkObject();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}	
	} 
	
}
