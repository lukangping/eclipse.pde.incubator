/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Wolfgang Schell <ws@jetztgrad.net> - bug 259348
 *     Wojciech Galanciak <wojciech.galanciak@gmail.com> - bug 282804
 *******************************************************************************/
package org.eclipse.pde.internal.runtime.registry;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.*;
import org.eclipse.pde.runtime.core.model.*;

public class RegistryBrowserContentProvider implements ITreeContentProvider {

	public boolean isInExtensionSet;

	private RegistryBrowser fRegistryBrowser;

	public class Folder {

		public static final int F_EXTENSIONS = 1;
		public static final int F_EXTENSION_POINTS = 2;
		public static final int F_IMPORTS = 3;
		public static final int F_LIBRARIES = 4;
		public static final int F_REGISTERED_SERVICES = 5;
		public static final int F_SERVICES_IN_USE = 6;
		public static final int F_PROPERTIES = 7;
		public static final int F_USING_BUNDLES = 8;
		public static final int F_FRAGMENTS = 9;
		public static final int F_IMPORTED_PACKAGES = 10;
		public static final int F_EXPORTED_PACKAGES = 11;

		private int id;
		private Object parent;

		public Folder(int id, Object parent) {
			this.id = id;
			this.parent = parent;
		}

		public int getId() {
			return id;
		}

		public Object getParent() {
			return parent;
		}

		public ModelObject[] getChildren() {
			Bundle bundle;

			switch (id) {
				case F_EXTENSION_POINTS :
					bundle = ((Bundle) parent);
					return fRegistryBrowser.getModel().getExtensionPoints(bundle);
				case F_EXTENSIONS :
					bundle = ((Bundle) parent);
					return fRegistryBrowser.getModel().getExtensions(bundle);
				case F_IMPORTS :
					return ((Bundle) parent).getImports();
				case F_LIBRARIES :
					return ((Bundle) parent).getLibraries();
				case F_REGISTERED_SERVICES :
					bundle = ((Bundle) parent);
					return fRegistryBrowser.getModel().getRegisteredServices(bundle);
				case F_SERVICES_IN_USE :
					bundle = ((Bundle) parent);
					return fRegistryBrowser.getModel().getServicesInUse(bundle);
				case F_PROPERTIES :
					return ((ServiceRegistration) parent).getProperties();
				case F_USING_BUNDLES :
					return fRegistryBrowser.getModel().getUsingBundles((ServiceRegistration) parent);
				case F_FRAGMENTS :
					return fRegistryBrowser.getModel().getFragments((Bundle) parent);
				case F_IMPORTED_PACKAGES :
					return ((Bundle) parent).getImportedPackages();
				case F_EXPORTED_PACKAGES :
					return ((Bundle) parent).getExportedPackages();
			}

			return null;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			result = prime * result + ((parent == null) ? 0 : parent.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			return ((obj instanceof Folder) && (((Folder) obj).id == id) && (((Folder) obj).parent.equals(parent)));
		}
	}

	public RegistryBrowserContentProvider(RegistryBrowser registryBrowser) {
		fRegistryBrowser = registryBrowser;
	}

	public void dispose() { // nothing to dispose
	}

	public Object[] getElements(Object element) {
		return getChildren(element);
	}

	public Object[] getChildren(Object element) {
		if (element instanceof RegistryModel) {
			RegistryModel model = (RegistryModel) element;

			switch (fRegistryBrowser.getGroupBy()) {
				case (RegistryBrowser.BUNDLES) :
					return model.getBundles();
				case (RegistryBrowser.EXTENSION_REGISTRY) :
					return model.getExtensionPoints();
				case (RegistryBrowser.SERVICES) :
					return model.getServiceNames();
			}

			return null;
		}

		if (element instanceof Extension)
			return ((Extension) element).getConfigurationElements();

		isInExtensionSet = false;
		if (element instanceof ExtensionPoint)
			return ((ExtensionPoint) element).getExtensions().toArray();

		if (element instanceof ConfigurationElement)
			return ((ConfigurationElement) element).getElements();

		if (element instanceof Bundle) {
			if (fRegistryBrowser.getGroupBy() != RegistryBrowser.BUNDLES) // expands only in Bundles mode
				return null;

			Bundle bundle = (Bundle) element;

			List folders = new ArrayList(9);

			folders.add(new Attribute(Attribute.F_LOCATION, bundle.getLocation()));
			if (bundle.getImports().length > 0)
				folders.add(new Folder(Folder.F_IMPORTS, bundle));
			if (bundle.getImportedPackages().length > 0)
				folders.add(new Folder(Folder.F_IMPORTED_PACKAGES, bundle));
			if (bundle.getExportedPackages().length > 0)
				folders.add(new Folder(Folder.F_EXPORTED_PACKAGES, bundle));
			if (bundle.getLibraries().length > 0)
				folders.add(new Folder(Folder.F_LIBRARIES, bundle));
			if (fRegistryBrowser.getModel().getExtensionPoints(bundle).length > 0)
				folders.add(new Folder(Folder.F_EXTENSION_POINTS, bundle));
			if (fRegistryBrowser.getModel().getExtensions(bundle).length > 0)
				folders.add(new Folder(Folder.F_EXTENSIONS, bundle));
			if (fRegistryBrowser.getModel().getRegisteredServices(bundle).length > 0)
				folders.add(new Folder(Folder.F_REGISTERED_SERVICES, bundle));
			if (fRegistryBrowser.getModel().getServicesInUse(bundle).length > 0)
				folders.add(new Folder(Folder.F_SERVICES_IN_USE, bundle));
			if (fRegistryBrowser.getModel().getFragments(bundle).length > 0)
				folders.add(new Folder(Folder.F_FRAGMENTS, bundle));

			return folders.toArray();
		}

		isInExtensionSet = false;

		if (element instanceof Folder) {
			Folder folder = (Folder) element;
			isInExtensionSet = folder.getId() == Folder.F_EXTENSIONS;
			ModelObject[] objs = folder.getChildren();
			if (folder.getId() == Folder.F_USING_BUNDLES) {
				ModelObject[] result = new ModelObject[objs.length];
				ILabelProvider labelProvider = (ILabelProvider) fRegistryBrowser.getAdapter(ILabelProvider.class);

				for (int i = 0; i < objs.length; i++) {
					result[i] = new Attribute(Attribute.F_BUNDLE, labelProvider.getText(objs[i]));
				}

				objs = result;
			}
			return objs;
		}
		if (element instanceof ConfigurationElement) {
			return ((ConfigurationElement) element).getElements();
		}

		if (element instanceof ExtensionPoint) {
			ExtensionPoint extensionPoint = (ExtensionPoint) element;
			Object[] objs = extensionPoint.getExtensions().toArray();
			return objs;
		}

		if (element instanceof ServiceName) {
			String[] classes = ((ServiceName) element).getClasses();
			return fRegistryBrowser.getModel().getServices(classes);
		}

		if (element instanceof ServiceRegistration) {
			ServiceRegistration service = (ServiceRegistration) element;

			List folders = new ArrayList();

			if (service.getProperties().length > 0)
				folders.add(new Folder(Folder.F_PROPERTIES, service));
			if (service.getUsingBundleIds().length > 0)
				folders.add(new Folder(Folder.F_USING_BUNDLES, service));

			return folders.toArray();
		}

		if (element instanceof Object[]) {
			return (Object[]) element;
		}

		return null;
	}

	public Object getParent(Object element) {
		if (!(element instanceof ModelObject)) {
			return null;
		}

		if (element instanceof Folder) {
			return ((Folder) element).getParent();
		}

		return null;
	}

	public boolean hasChildren(Object element) {
		// Bundle and ServiceRegistration always have children
		if (element instanceof Bundle)
			return true;
		if (element instanceof ServiceRegistration)
			return true;

		Object[] children = getChildren(element);
		return children != null && children.length > 0;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { // do nothing
	}

}
