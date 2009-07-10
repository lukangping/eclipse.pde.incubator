/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Wojciech Galanciak <wojciech.galanciak@gmail.com> - bug 282804
 *******************************************************************************/
package org.eclipse.pde.internal.runtime.registry.model;

public class Bundle extends ModelObject {

	private static final long serialVersionUID = 1L;
	public static final int ACTIVE = org.osgi.framework.Bundle.ACTIVE;
	public static final int UNINSTALLED = org.osgi.framework.Bundle.UNINSTALLED;
	public static final int INSTALLED = org.osgi.framework.Bundle.INSTALLED;

	private String symbolicName;
	private String location;
	private boolean isEnabled;
	private BundlePrerequisite[] imports = new BundlePrerequisite[0];
	private String version;
	private int state;
	private long id;
	private BundleLibrary[] libraries = new BundleLibrary[0];
	private BundlePrerequisite[] importedPackages = new BundlePrerequisite[0];
	private BundlePrerequisite[] exportedPackages = new BundlePrerequisite[0];

	private String fragmentHost;
	private String fragmentHostVersion;

	public void setFragmentHost(String fragmentHost) {
		this.fragmentHost = fragmentHost;
	}

	public String getFragmentHost() {
		return fragmentHost;
	}

	public String getFragmentHostVersion() {
		return fragmentHostVersion;
	}

	public void setFragmentHostVersion(String fragmentHostVersion) {
		this.fragmentHostVersion = fragmentHostVersion;
	}

	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setImports(BundlePrerequisite[] imports) {
		if (imports == null)
			throw new IllegalArgumentException();

		this.imports = imports;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	public void setLibraries(BundleLibrary[] libraries) {
		if (libraries == null)
			throw new IllegalArgumentException();

		this.libraries = libraries;
	}

	public String getSymbolicName() {
		return symbolicName;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public BundlePrerequisite[] getImports() {
		return imports;
	}

	public BundleLibrary[] getLibraries() {
		return libraries;
	}

	public String getLocation() {
		return location;
	}

	public String getVersion() {
		return version;
	}

	public int getState() {
		return state;
	}

	public long getId() {
		return id;
	}

	public boolean equals(Object obj) {
		return (obj instanceof Bundle) && (id == ((Bundle) obj).id);
	}

	public int hashCode() {
		return (int) id;
	}

	public void setImportedPackages(BundlePrerequisite[] importedPackages) {
		this.importedPackages = importedPackages;
	}

	public BundlePrerequisite[] getImportedPackages() {
		return importedPackages;
	}

	public void setExportedPackages(BundlePrerequisite[] exportedPackages) {
		this.exportedPackages = exportedPackages;
	}

	public BundlePrerequisite[] getExportedPackages() {
		return exportedPackages;
	}
}
