/*******************************************************************************
 * Copyright 2005, 2012 CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria IBM CAS, IBM Toronto
 * Lab
 ******************************************************************************/
package org.eclipse.pde.internal.visualization.dependency.views;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

class GraphContentProvider implements IGraphEntityContentProvider {

	Object currentBundle = null;
	private boolean reverseBundleDependencies;
	private String[] symbolicNamesPatternsToMatch = new String[0];
	private String[] symbolicNamesPatternsToExclude = new String[0];
	private boolean hideFragments = false;

	private Object[] getDependencies(Object bundle) {
		if (bundle != null) {
			if (reverseBundleDependencies) {
				return filterBundles(DependencyUtil.getDependentBundles(bundle));
			}
			return filterBundles(AnalysisUtil.getPrerequisites(new Object[] { currentBundle }));
		}
		return new BundleDescription[0];
	}

	// Returns all entities that should be linked with the given entity
	public Object[] getConnectedTo(Object entity) {
		Object[] bundles = null;
		if (reverseBundleDependencies) {
			bundles = DependencyUtil.getConnectedBundles(entity, currentBundle);
		} else {
			bundles = AnalysisUtil.getDependencies(entity);
		}
		return filterBundles(bundles);
	}

	private Object[] filterBundles(Object[] bundles) {
		if (symbolicNamesPatternsToExclude.length == 0 && symbolicNamesPatternsToMatch.length == 0 && !hideFragments) {
			return bundles;
		}
		List filteredList = new ArrayList();
		for (int i = 0; i < bundles.length; i++) {
			Object object = bundles[i];
			if (object instanceof BundleDescription) {
				BundleDescription bundleDesc = (BundleDescription) object;
				if (keepBundle(bundleDesc)) {
					filteredList.add(object);
				}
			} else {
				// What type of object is it ??? Keep it in the list as long as we don't want to filter things we don't understand
				filteredList.add(object);
			}
		}
		return filteredList.toArray();
	}

	/**
	 * Keep bundle if at least one of symbolic names to match pattern is matched and if none of symbolicNamePatternsToExclude is matched 
	 * @param bundleDesc
	 * @return
	 */
	private boolean keepBundle(BundleDescription bundleDesc) {
		if (hideFragments && bundleDesc.getHost() != null) {
			// Hide fragments
			return false;
		}
		String symbolicName = bundleDesc.getSymbolicName();		
		if (symbolicNamesPatternsToMatch.length > 0) {
			boolean keepBundle = false;
			for (int i = 0; i < symbolicNamesPatternsToMatch.length; i++) {
				String patternToMatch = symbolicNamesPatternsToMatch[i];
				if (Pattern.matches(patternToMatch, symbolicName)) {
					keepBundle = true;
					break;
				}
			}
			if (!keepBundle) {
				// Non of symbolicNamesPatternsToMatch is matched
				return false;
			}
		}		
		for (int i = 0; i < symbolicNamesPatternsToExclude.length; i++) {
			String patternToExclude = symbolicNamesPatternsToExclude[i];
			if (Pattern.matches(patternToExclude, symbolicName)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Should match at least one of the patterns
	 * @param patternsToMatch
	 */
	public void setSymbolicNamesPatternsToMatch(String[] patternsToMatch) {
		if (patternsToMatch == null) {
			throw new IllegalArgumentException("patternsToMatch should not be null");
		}
		symbolicNamesPatternsToMatch = patternsToMatch;
	}

	public void setSymbolicNamesPatternsToExcludes(String[] patternsToExclude) {
		if (patternsToExclude == null) {
			throw new IllegalArgumentException("patternsToExclude should not be null");
		}
		symbolicNamesPatternsToExclude = patternsToExclude;
	}

	public void setHideFragments(boolean pHideFragments) {
		hideFragments = pHideFragments;
	}

	public Object[] getElements(Object inputElement) {
		return filterBundles(getDependencies(inputElement));

	}

	public double getWeight(Object entity1, Object entity2) {
		return 0;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		currentBundle = newInput;
	}

	public void setReverseBundleDependencies(boolean enable) {
		this.reverseBundleDependencies = enable;
	}

}