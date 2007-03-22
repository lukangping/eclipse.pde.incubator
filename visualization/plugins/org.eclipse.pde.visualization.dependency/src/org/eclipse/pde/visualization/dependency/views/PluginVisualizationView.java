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

import java.util.Stack;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.viewer.AbstractZoomableViewer;
import org.eclipse.mylar.zest.core.viewer.IGraphEntityContentProvider;
import org.eclipse.mylar.zest.core.viewer.IZoomableWorkbenchPart;
import org.eclipse.mylar.zest.core.viewer.StaticGraphViewer;
import org.eclipse.mylar.zest.core.viewer.ZoomContributionViewItem;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.LayoutStyles;
import org.eclipse.mylar.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.mylar.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.ui.wizards.PluginSelectionDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */
public class PluginVisualizationView extends ViewPart implements IZoomableWorkbenchPart {

	private FormToolkit toolKit = null;
	private Form form = null;
	private StaticGraphViewer viewer;
	private Action focusDialogAction;
	private Action focusAction;
	private Action pinAction;
	private Action unPinAction;
	private Action historyAction;
	private Stack historyStack;
	private Object currentNode = null;
	private VisualizationLabelProvider currentLabelProvider;
	private IGraphEntityContentProvider contentProvider;
	private Object pinnedNode = null;
	private ZoomContributionViewItem contextZoomContributionViewItem;
	private ZoomContributionViewItem toolbarZoomContributionViewItem;

	/**
	 * The constructor.
	 */
	public PluginVisualizationView() {
		historyStack = new Stack();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		toolKit = new FormToolkit(parent.getDisplay());
		VisualizationForm visualizationForm = new VisualizationForm(parent, toolKit, this);
		viewer = visualizationForm.getGraphViewer();
		form = visualizationForm.getForm();

		this.contentProvider = new GraphContentProvider();
		this.currentLabelProvider = new HighlightDependencyLableProvider(this.viewer);
		viewer.setContentProvider(this.contentProvider);
		viewer.setLabelProvider(this.currentLabelProvider);
		viewer.setInput(null);
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		viewer.setLayoutAlgorithm(new CompositeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING, new LayoutAlgorithm[] { new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING) }));

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				PluginVisualizationView.this.selectionChanged(((IStructuredSelection) event.getSelection()).getFirstElement());
			}
		});
		toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
		contextZoomContributionViewItem = new ZoomContributionViewItem(this);

		makeActions();
		hookContextMenu();
		fillToolBar();
	}

	/**
	 * Enable dependency path in the view. This will highlight all the nodes
	 * from the selected node to the root.
	 * 
	 * @param dependencyPath
	 */
	void setDependencyPath(boolean dependencyPath, String dependencyPathType) {
		// If the viewer has not been created, return
		if (viewer == null) {
			return;
		}

		if (dependencyPath) {
			// If dependencyPath is set to true set the
			// ShortestPathDependencyAnalyis label provider

			if (dependencyPathType == VisualizationForm.Show_Smart_Path) {
				this.currentLabelProvider = new SmartPathDependencyAnalysis(this.viewer);
			}
			if (dependencyPathType == VisualizationForm.Show_All_Paths) {
				this.currentLabelProvider = new PathDependencyAnalysis(this.viewer);
			}
			if (dependencyPathType == VisualizationForm.Show_Shortest_Path) {
				this.currentLabelProvider = new ShortestPathDependencyAnalysis(this.viewer);
			}

			viewer.setLabelProvider(this.currentLabelProvider);

		} else if (!dependencyPath && !(currentLabelProvider instanceof HighlightDependencyLableProvider)) {
			this.currentLabelProvider = new HighlightDependencyLableProvider(this.viewer);
			viewer.setLabelProvider(this.currentLabelProvider);

		}

		// Set the pinned node in case we have one from the previous content
		// provdier
		this.currentLabelProvider.setPinnedNode((BundleDescription) pinnedNode);
		if (viewer.getSelection() != null) {
			viewer.setSelection(viewer.getSelection());
			this.currentLabelProvider.setCurrentSelection(currentNode, ((IStructuredSelection) viewer.getSelection()).getFirstElement());
		}
	}

	/**
	 * Handle the select changed. This will update the view whenever a selection
	 * occurs.
	 * 
	 * @param selectedItem
	 */
	private void selectionChanged(Object selectedItem) {
		currentLabelProvider.setCurrentSelection(currentNode, selectedItem);
		viewer.update(contentProvider.getElements(currentNode), null);
	}

	/**
	 * Set the toolbar
	 */
	private void fillToolBar() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().add(toolbarZoomContributionViewItem);

		fillLocalToolBar(bars.getToolBarManager());

	}

	/**
	 * Add the actions to the tool bart
	 * 
	 * @param toolBarManager
	 */
	private void fillLocalToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(historyAction);
	}

	/**
	 * Update the view to focus on a particular bundle. If record history is set
	 * to true, and bundle does not equal the current bundle, then the current
	 * bundle will be saved on the history stack
	 * 
	 * @param bundle
	 * @param recordHistory
	 */
	private void focusOn(BundleDescription bundle, boolean recordHistory) {
		viewer.setInput(bundle);
		if (currentNode != null && recordHistory && currentNode != bundle) {
			historyStack.push(currentNode);
			historyAction.setEnabled(true);
		}
		currentNode = bundle;
		viewer.setSelection(new StructuredSelection(bundle));
		// When we load a new model, remove any pinnedNode;
		this.currentLabelProvider.setPinnedNode(null);
		this.pinnedNode = null;
	}

	/**
	 * Make the actions that can be called on this viewer. This currently
	 * includes: - Focus on ... - Focus on Selected Node - History action
	 */
	private void makeActions() {
		focusDialogAction = new Action() {
			public void run() {
				PluginSelectionDialog dialog = new PluginSelectionDialog(viewer.getControl().getShell(), true, false);
				dialog.create();
				if (dialog.open() == Window.OK) {
					IPluginModelBase pluginModelBase = (IPluginModelBase) dialog.getFirstResult();
					focusOn(pluginModelBase.getBundleDescription(), true);
				}
			}
		};
		// @tag action : Focus on ... Action
		focusDialogAction.setText("Focus On ...");
		focusDialogAction.setToolTipText("Focus on a plugin");

		historyAction = new Action() {
			public void run() {
				if (historyStack.size() > 0) {
					Object o = historyStack.pop();
					focusOn((BundleDescription) o, false);
					if (historyStack.size() <= 0) {
						historyAction.setEnabled(false);
					}
				}
			}
		};
		// @tag action : History action
		historyAction.setText("Back");
		historyAction.setToolTipText("Previous Plugin");
		historyAction.setEnabled(false);
	}

	/**
	 * Create the make focus on current selection action
	 * 
	 * @param objectToFocusOn
	 */
	private void makeFocusAction(final Object objectToFocusOn) {
		// @tag action : Focus on Current Selection action
		focusAction = new Action() {
			public void run() {
				focusOn((BundleDescription) objectToFocusOn, true);
			}
		};
		focusAction.setText("Focus On \'" + ((BundleDescription) objectToFocusOn).getName() + "\'");
		focusAction.setToolTipText("Focus on a plugin");
	}

	private void makeUnPinAction() {
		unPinAction = new Action() {
			public void run() {
				unPin();
			}
		};
		unPinAction.setText("Deselect pinned node");
		unPinAction.setToolTipText("Deselect: " + this.pinnedNode);
		unPinAction.setChecked(true);
	}

	private void makePinAction(final BundleDescription objectToPin) {
		pinAction = new Action() {
			public void run() {
				pinNode(objectToPin);
			}
		};
		pinAction.setText("Pin selection of " + objectToPin.getName());
		pinAction.setToolTipText("Toggle pin selected node");
	}

	private void unPin() {
		pinNode(null);
	}

	private void pinNode(Object objectToPin) {
		this.currentLabelProvider.setPinnedNode((BundleDescription) objectToPin);
		this.pinnedNode = objectToPin;
		this.currentLabelProvider.setCurrentSelection(this.currentNode, ((IStructuredSelection) viewer.getSelection()).getFirstElement());
		this.viewer.update(contentProvider.getElements(currentNode), null);
	}

	/**
	 * Creates the context menu for this view.
	 */
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		fillContextMenu(menuMgr);

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				PluginVisualizationView.this.fillContextMenu(manager);

			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);

	}

	/**
	 * Add the items to the context menu
	 * 
	 * @param manager
	 */
	private void fillContextMenu(IMenuManager manager) {
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		if (((IStructuredSelection) viewer.getSelection()).size() > 0) {
			makeFocusAction(((IStructuredSelection) viewer.getSelection()).getFirstElement());
			manager.add(focusAction);

		}
		manager.add(focusDialogAction);
		// Other plug-ins can contribute there actions here
		if (((IStructuredSelection) viewer.getSelection()).size() > 0 || this.pinnedNode != null) {
			manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		}
		if (this.pinnedNode != null) {
			makeUnPinAction();
			manager.add(unPinAction);
		}
		if (((IStructuredSelection) viewer.getSelection()).size() > 0) {
			makePinAction((BundleDescription) ((IStructuredSelection) viewer.getSelection()).getFirstElement());
			manager.add(pinAction);
		}

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(historyAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(contextZoomContributionViewItem);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		form.setFocus();
	}

	/**
	 * Dispose the form
	 */
	public void dispose() {
		form.dispose();
		super.dispose();
	}

	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}
}