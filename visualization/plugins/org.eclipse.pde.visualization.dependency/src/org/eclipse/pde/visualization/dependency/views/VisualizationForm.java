/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria IBM CAS, IBM Toronto
 * Lab
 ******************************************************************************/
package org.eclipse.pde.visualization.dependency.views;

import org.eclipse.mylyn.zest.core.viewers.GraphViewer;
import org.eclipse.pde.visualization.dependency.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * This class encapsulates the process of creating the form view in the PDE
 * visualization tool.
 * 
 * @author Ian Bull
 * 
 */
/* package */class VisualizationForm {

	/*
	 * These are all the strings used in the form. These can probably be
	 * abstracted for internationalization
	 */
	private static String Plugin_Dependency_Analysis = "Plug-in Dependency Analysis";
	private static String Controls = "Controls";
	private static String Show_Dependency_Path = "Show Dependency Path";
	private static String Filter_Enanled_Plugins = "Filter Enabled Plug-ins";
	private static String Exeuction_Environment = "Execution Environment";
	private static String Version_Number = "Show Bundle Version Numbers";
	private static String Execution_Environment_Instructions = "Show bunldes with execution environment:";
	private static String Java6 = "Java 6.0 and above";
	private static String Java5 = "Java 5.0 and above";
	private static String Java4 = "Java 4.0 and above";
	private static String Java3 = "Java 3.0 and above";
	private static String Java2 = "Java 2.0 and above";
	private static String Java1 = "Java 1.0 and above";

	/*
	 * These are strings and used to determine which radio button is selected
	 */
	public static String Show_All_Paths = "Show All Paths";
	public static String Show_Smart_Path = "Show Smart Path";
	public static String Show_Shortest_Path = "Show Shortest Path";

	/*
	 * Some parts of the form we may need access to
	 */
	private ScrolledForm form;
	private FormToolkit toolkit;
	private ManagedForm managedForm;
	private GraphViewer viewer;
	private PluginVisualizationView view;

	/*
	 * Some buttons that we need to access in local methods
	 */
	private Button showSmartPath = null;
	private Button showShortestPath = null;
	private Button showAllPaths = null;
	private Button dependencyAnalysis = null;
	private Button showVersionNumber = null;

	private String currentPathAnalysis = null;

	/**
	 * Creates the form.
	 * 
	 * @param toolKit
	 * @return
	 */
	VisualizationForm(Composite parent, FormToolkit toolkit, PluginVisualizationView view) {
		this.toolkit = toolkit;
		this.view = view;
		form = this.toolkit.createScrolledForm(parent);
		managedForm = new ManagedForm(this.toolkit, this.form);
		form.getBody().setLayout(new FillLayout());
		form.setText(Plugin_Dependency_Analysis);
		form.setImage(Activator.getDefault().getImageRegistry().get(Activator.REQ_PLUGIN_OBJ));
		createSash(form.getBody());
	}
	
	/**
	 * Creates the sashform to seperate the graph from the controls.
	 * 
	 * @param parent
	 */
	private void createSash(Composite parent) {
		SashForm sash = new SashForm(parent, SWT.NONE);
		sash.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		this.toolkit.paintBordersFor(parent);

		sash.setLayout(new GridLayout());
		createGraphSection(sash);
		createControlsSection(sash);
		sash.setWeights(new int[] { 10, 3 });
	}

	/**
	 * Creates the section of the form where the graph is drawn
	 * 
	 * @param parent
	 */
	private void createGraphSection(Composite parent) {
		
		Section section = this.toolkit.createSection(parent, Section.EXPANDED | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		section.setLayout(new FillLayout());
		Composite composite = this.toolkit.createComposite(section, SWT.NONE);
		composite.setLayout(new FillLayout());
		//viewer = new GraphViewer(composite, SWT.BORDER);
		viewer = new GraphViewer(composite, SWT.NONE);
		section.setClient(composite);
	}

	private void setDependencyPath(boolean enabled) {
		if (showAllPaths.getEnabled() != enabled) {
			showAllPaths.setEnabled(enabled);
		}
		if (showSmartPath.getEnabled() != enabled) {
			showSmartPath.setEnabled(enabled);
		}
		if (showShortestPath.getEnabled() != enabled) {
			showShortestPath.setEnabled(enabled);
		}

		if (!enabled) {
			showAllPaths.setSelection(false);
			showSmartPath.setSelection(false);
			showShortestPath.setSelection(false);
		} else {
			if (currentPathAnalysis == Show_All_Paths) {
				showAllPaths.setSelection(true);
			} else if (currentPathAnalysis == Show_Smart_Path) {
				showSmartPath.setSelection(true);
			} else if (currentPathAnalysis == Show_Shortest_Path) {
				showShortestPath.setSelection(true);
			}
		}
		view.setDependencyPath(enabled, currentPathAnalysis);
	}

	/**
	 * Creates the section holding the analysis controls.
	 * 
	 * @param parent
	 */
	private void createControlsSection(Composite parent) {
		Section controls = this.toolkit.createSection(parent, Section.TITLE_BAR | Section.EXPANDED);
		controls.setLayout(new FillLayout());
		controls.setText(Controls);
		Composite controlComposite = this.toolkit.createComposite(controls);
		controlComposite.setLayout(new GridLayout());
		
		showVersionNumber = this.toolkit.createButton(controlComposite, Version_Number, SWT.CHECK);
		showVersionNumber.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		showVersionNumber.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				view.showVersionNumber(showVersionNumber.getSelection());
			}
		});
		
		dependencyAnalysis = this.toolkit.createButton(controlComposite, Show_Dependency_Path, SWT.CHECK);
		dependencyAnalysis.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		dependencyAnalysis.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setDependencyPath(((Button) e.getSource()).getSelection());
			}
		});

		Section dependencyOptions = this.toolkit.createSection(controlComposite, Section.EXPANDED | Section.NO_TITLE);
		dependencyOptions.setLayout(new FillLayout());
		Composite dependencyOptionsComposite = this.toolkit.createComposite(dependencyOptions);
		dependencyOptionsComposite.setLayout(new TableWrapLayout());

		showSmartPath = this.toolkit.createButton(dependencyOptionsComposite, Show_Smart_Path, SWT.RADIO);
		showSmartPath.setLayoutData(new TableWrapData(TableWrapData.FILL));
		showSmartPath.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				currentPathAnalysis = Show_Smart_Path;
				view.setDependencyPath(dependencyAnalysis.getSelection(), currentPathAnalysis);
			}
		});

		showAllPaths = this.toolkit.createButton(dependencyOptionsComposite, Show_All_Paths, SWT.RADIO);
		showAllPaths.setLayoutData(new TableWrapData(TableWrapData.FILL));
		showAllPaths.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				currentPathAnalysis = Show_All_Paths;
				view.setDependencyPath(dependencyAnalysis.getSelection(), currentPathAnalysis);
			}
		});

		showShortestPath = this.toolkit.createButton(dependencyOptionsComposite, Show_Shortest_Path, SWT.RADIO);
		showShortestPath.setLayoutData(new TableWrapData(TableWrapData.FILL));
		showShortestPath.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				currentPathAnalysis = Show_Shortest_Path;
				view.setDependencyPath(dependencyAnalysis.getSelection(), currentPathAnalysis);
			}
		});

		currentPathAnalysis = Show_Smart_Path;

		setDependencyPath(false);
		dependencyOptions.setClient(dependencyOptionsComposite);

		Button filterEnabled = this.toolkit.createButton(controlComposite, Filter_Enanled_Plugins, SWT.CHECK);
		filterEnabled.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		
		createEEAnalysisSection(controlComposite);
		controls.setClient(controlComposite);
	}

	/**
	 * Creates the section holding the Execution Environment Controls
	 * 
	 * @param parent
	 */
	private void createEEAnalysisSection(Composite parent) {
		ExpandableComposite eeAnalysis = this.toolkit.createExpandableComposite(parent, Section.TITLE_BAR | Section.TWISTIE);
		eeAnalysis.setText(Exeuction_Environment);
		eeAnalysis.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Composite eeAnalysisControls = this.toolkit.createComposite(eeAnalysis);
		eeAnalysisControls.setLayout(new TableWrapLayout());
		this.toolkit.createLabel(eeAnalysisControls, Execution_Environment_Instructions, SWT.WRAP);
		Button ee6 = this.toolkit.createButton(eeAnalysisControls, Java6, SWT.RADIO);
		ee6.setLayoutData(new TableWrapData(TableWrapData.FILL));
		Button ee5 = this.toolkit.createButton(eeAnalysisControls, Java5, SWT.RADIO);
		ee5.setLayoutData(new TableWrapData(TableWrapData.FILL));
		Button ee4 = this.toolkit.createButton(eeAnalysisControls, Java4, SWT.RADIO);
		ee4.setLayoutData(new TableWrapData(TableWrapData.FILL));
		Button ee3 = this.toolkit.createButton(eeAnalysisControls, Java3, SWT.RADIO);
		ee3.setLayoutData(new TableWrapData(TableWrapData.FILL));
		Button ee2 = this.toolkit.createButton(eeAnalysisControls, Java2, SWT.RADIO);
		ee2.setLayoutData(new TableWrapData(TableWrapData.FILL));
		Button ee1 = this.toolkit.createButton(eeAnalysisControls, Java1, SWT.RADIO);
		ee1.setLayoutData(new TableWrapData(TableWrapData.FILL));
		eeAnalysis.setClient(eeAnalysisControls);
	}

	/**
	 * Gets the currentGraphViewern
	 * 
	 * @return
	 */
	public GraphViewer getGraphViewer() {
		return viewer;
	}

	/**
	 * Gets the form we created.
	 */
	public ScrolledForm getForm() {
		return form;
	}
	
	public ManagedForm getManagedForm() {
		return managedForm;
	}

}
