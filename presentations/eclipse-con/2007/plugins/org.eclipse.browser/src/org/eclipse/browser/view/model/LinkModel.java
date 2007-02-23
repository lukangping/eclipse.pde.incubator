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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.CoreException;
import org.xml.sax.SAXException;


/**
 * EclipseProjectLinkModel
 *
 */
public class LinkModel {

	private URL fModelURL;
	
	private LinkObject fRoot;
	
	private SAXParserFactory fParserFactory;
	
	private SAXParser fParser;
	
	private LinkHandler fParserHandler;
	
	private LinkModelFactory fModelFactory;
	
	/**
	 * 
	 */
	public LinkModel(URL file) {
		fModelURL = file;
		
		initialize();
	}

	/**
	 * @return
	 */
	public LinkObject getRootLinkObject() {
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
	private LinkHandler getSAXHandler() {
		if (fParserHandler == null) {
			fParserHandler = new LinkHandler(this);
		}
		return fParserHandler;
	}

	/**
	 * @return
	 */
	public LinkModelFactory getModelFactory() {
		if (fModelFactory == null) {
			fModelFactory = new LinkModelFactory(this);
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
			LinkHandler handler = getSAXHandler();
			parser.parse(stream, handler);
			fRoot = handler.getRootLinkObject();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}	
	} 
	
}
