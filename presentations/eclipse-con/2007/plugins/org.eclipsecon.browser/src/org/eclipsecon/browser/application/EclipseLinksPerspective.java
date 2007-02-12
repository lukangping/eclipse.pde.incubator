package org.eclipsecon.browser.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;
import org.eclipsecon.browser.view.BrowserPlugin;

public class EclipseLinksPerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		
		layout.addStandaloneView(BrowserPlugin.VIEW_ID, true, IPageLayout.LEFT, 1.0f, layout.getEditorArea());
		IViewLayout vLayout = layout.getViewLayout(BrowserPlugin.VIEW_ID);
		vLayout.setCloseable(false);
	}

}
