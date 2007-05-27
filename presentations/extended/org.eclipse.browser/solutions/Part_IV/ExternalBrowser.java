package com.example.xyz;

import java.net.URL;

import org.eclipse.browser.view.ui.actions.IHTMLBrowser;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

public class ExternalBrowser implements IHTMLBrowser {

	public ExternalBrowser() {
	}

	public void openURL(URL url) {
		try {
			IWorkbenchBrowserSupport support = 
				PlatformUI.getWorkbench().getBrowserSupport();
			IWebBrowser browser = support.getExternalBrowser();
			browser.openURL(url);
		} catch (PartInitException e) {
		}
	}

}
