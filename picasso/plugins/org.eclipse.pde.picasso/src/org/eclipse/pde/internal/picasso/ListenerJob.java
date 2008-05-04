/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.internal.picasso;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.ui.progress.UIJob;

public class ListenerJob extends UIJob {

	public ListenerJob(String name) {
		super(name);
	}

	public IStatus runInUIThread(IProgressMonitor monitor) {
		getDisplay().addFilter(SWT.Show, new DebugFilter());
		return Status.OK_STATUS;
	}

}
