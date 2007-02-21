package org.eclipse.browser.application;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class BrowserWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public BrowserWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new BrowserActionBarAdvisor(configurer);
    }
    
    /**
     * Standard procedure for setting size and title of RCP application
     */
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(400, 750));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
    }

}
