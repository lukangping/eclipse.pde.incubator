package org.eclipse.browser.application;

import org.eclipse.browser.view.BrowserPlugin;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		
		layout.addStandaloneView(BrowserPlugin.VIEW_ID, true, IPageLayout.LEFT, 1.0f, layout.getEditorArea());
		IViewLayout vLayout = layout.getViewLayout(BrowserPlugin.VIEW_ID);
		vLayout.setCloseable(false);
	}

}
