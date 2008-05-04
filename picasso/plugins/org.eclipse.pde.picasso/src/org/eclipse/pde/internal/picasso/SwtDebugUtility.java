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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;

/**
 * SwtDebugUtility.java
 */
public final class SwtDebugUtility extends Object {
	//
	// Static Fields
	//

	private static final int BUFFER_SIZE = 2048;

	private static final Color[] COLORS = new Color[] {
		SwtDebugUtility.getSystemColor(SWT.COLOR_RED),
		SwtDebugUtility.getSystemColor(SWT.COLOR_BLUE),
		SwtDebugUtility.getSystemColor(SWT.COLOR_YELLOW),
		SwtDebugUtility.getSystemColor(SWT.COLOR_GREEN),
		SwtDebugUtility.getSystemColor(SWT.COLOR_CYAN),
		SwtDebugUtility.getSystemColor(SWT.COLOR_MAGENTA),
		SwtDebugUtility.getSystemColor(SWT.COLOR_DARK_RED),
		SwtDebugUtility.getSystemColor(SWT.COLOR_DARK_BLUE),
		SwtDebugUtility.getSystemColor(SWT.COLOR_DARK_YELLOW),
		SwtDebugUtility.getSystemColor(SWT.COLOR_DARK_GREEN),
		SwtDebugUtility.getSystemColor(SWT.COLOR_DARK_CYAN),
		SwtDebugUtility.getSystemColor(SWT.COLOR_DARK_MAGENTA)
	};

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");  //$NON-NLS-1$

	//
	// Static Methods
	//

	private static void createCompositeToolTip(StringBuffer buffer, Composite composite) {
		Layout layout = composite.getLayout();
		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("  layout=");  //$NON-NLS-1$

		if (layout instanceof GridLayout) { // $codepro.audit.disable disallowInstanceof
			GridLayout gridLayout = (GridLayout) layout;
			SwtDebugUtility.createGridLayoutToolTip(buffer, gridLayout);
		} else if (layout instanceof FormLayout) { // $codepro.audit.disable disallowInstanceof
			FormLayout formLayout = (FormLayout) layout;
			SwtDebugUtility.createFormLayoutToolTip(buffer, formLayout);
		} else if (layout instanceof FillLayout) { // $codepro.audit.disable disallowInstanceof
			FillLayout fillLayout = (FillLayout) layout;
			SwtDebugUtility.createFillLayoutToolTip(buffer, fillLayout);
		} else if (layout instanceof RowLayout) { // $codepro.audit.disable disallowInstanceof
			RowLayout rowLayout = (RowLayout) layout;
			SwtDebugUtility.createRowLayoutToolTip(buffer, rowLayout);
		} else {
			buffer.append(layout);
		}
	}

	private static void createFillLayoutToolTip(StringBuffer buffer, FillLayout layout) {
		buffer.append("FormLayout");  //$NON-NLS-1$

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginHeight=");  //$NON-NLS-1$
		buffer.append(layout.marginHeight);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginWidth=");  //$NON-NLS-1$
		buffer.append(layout.marginWidth);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    spacing=");  //$NON-NLS-1$
		buffer.append(layout.spacing);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    type=");  //$NON-NLS-1$
		buffer.append(layout.type);
		buffer.append(" (");  //$NON-NLS-1$
		buffer.append(SwtDebugUtility.getLayoutTypeText(layout.type));
		buffer.append(')');
	}

	private static void createFormDataToolTip(StringBuffer buffer, FormData data) {
		buffer.append("FormData");  //$NON-NLS-1$

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    bottom=");  //$NON-NLS-1$
		buffer.append(data.bottom);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    height=");  //$NON-NLS-1$
		buffer.append(data.height);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    left=");  //$NON-NLS-1$
		buffer.append(data.left);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    right=");  //$NON-NLS-1$
		buffer.append(data.right);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    top=");  //$NON-NLS-1$
		buffer.append(data.top);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    width=");  //$NON-NLS-1$
		buffer.append(data.width);
	}

	private static void createFormLayoutToolTip(StringBuffer buffer, FormLayout layout) {
		buffer.append("FormLayout");  //$NON-NLS-1$

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginBottom=");  //$NON-NLS-1$
		buffer.append(layout.marginBottom);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginHeight=");  //$NON-NLS-1$
		buffer.append(layout.marginHeight);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginLeft=");  //$NON-NLS-1$
		buffer.append(layout.marginLeft);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginRight=");  //$NON-NLS-1$
		buffer.append(layout.marginRight);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginTop=");  //$NON-NLS-1$
		buffer.append(layout.marginTop);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginWidth=");  //$NON-NLS-1$
		buffer.append(layout.marginWidth);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    spacing=");  //$NON-NLS-1$
		buffer.append(layout.spacing);
	}

	private static void createGridDataToolTip(StringBuffer buffer, GridData gridData) {
		buffer.append("GridData");  //$NON-NLS-1$

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    exclude=");  //$NON-NLS-1$
		buffer.append(gridData.exclude);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    grabExcessHorizontalSpace=");  //$NON-NLS-1$
		buffer.append(gridData.grabExcessHorizontalSpace);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    grabExcessVerticalSpace=");  //$NON-NLS-1$
		buffer.append(gridData.grabExcessVerticalSpace);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    heightHint=");  //$NON-NLS-1$
		buffer.append(gridData.heightHint);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    horizontalAlignment=");  //$NON-NLS-1$
		buffer.append(SwtDebugUtility.getAlignmentText(gridData.horizontalAlignment));

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    horizontalIndent=");  //$NON-NLS-1$
		buffer.append(gridData.horizontalIndent);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    horizontalSpan=");  //$NON-NLS-1$
		buffer.append(gridData.horizontalSpan);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    minimumHeight=");  //$NON-NLS-1$
		buffer.append(gridData.minimumHeight);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    minimumWidth=");  //$NON-NLS-1$
		buffer.append(gridData.minimumWidth);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    verticalAlignment=");  //$NON-NLS-1$
		buffer.append(SwtDebugUtility.getAlignmentText(gridData.verticalAlignment));

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    verticalIndent=");  //$NON-NLS-1$
		buffer.append(gridData.verticalIndent);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    verticalSpan=");  //$NON-NLS-1$
		buffer.append(gridData.verticalSpan);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    widthHint=");  //$NON-NLS-1$
		buffer.append(gridData.widthHint);
	}

	private static void createGridLayoutToolTip(StringBuffer buffer, GridLayout layout) {
		buffer.append("GridLayout");  //$NON-NLS-1$

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    horizontalSpacing=");  //$NON-NLS-1$
		buffer.append(layout.horizontalSpacing);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    makeColumnsEqualWidth=");  //$NON-NLS-1$
		buffer.append(layout.makeColumnsEqualWidth);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginBottom=");  //$NON-NLS-1$
		buffer.append(layout.marginBottom);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginHeight=");  //$NON-NLS-1$
		buffer.append(layout.marginHeight);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginLeft=");  //$NON-NLS-1$
		buffer.append(layout.marginLeft);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginRight=");  //$NON-NLS-1$
		buffer.append(layout.marginRight);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginTop=");  //$NON-NLS-1$
		buffer.append(layout.marginBottom);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginWidth=");  //$NON-NLS-1$
		buffer.append(layout.marginWidth);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    numColumns=");  //$NON-NLS-1$
		buffer.append(layout.numColumns);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    verticalSpacing=");  //$NON-NLS-1$
		buffer.append(layout.verticalSpacing);
	}

	private static void createRowDataToolTip(StringBuffer buffer, RowData data) {
		buffer.append("RowData");  //$NON-NLS-1$

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    exclude=");  //$NON-NLS-1$
		buffer.append(data.exclude);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    height=");  //$NON-NLS-1$
		buffer.append(data.height);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    width=");  //$NON-NLS-1$
		buffer.append(data.width);
	}

	private static void createRowLayoutToolTip(StringBuffer buffer, RowLayout layout) {
		buffer.append("RowLayout");  //$NON-NLS-1$

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    fill=");  //$NON-NLS-1$
		buffer.append(layout.fill);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    justify=");  //$NON-NLS-1$
		buffer.append(layout.justify);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginBottom=");  //$NON-NLS-1$
		buffer.append(layout.marginBottom);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginHeight=");  //$NON-NLS-1$
		buffer.append(layout.marginHeight);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginLeft=");  //$NON-NLS-1$
		buffer.append(layout.marginLeft);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginRight=");  //$NON-NLS-1$
		buffer.append(layout.marginRight);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginTop=");  //$NON-NLS-1$
		buffer.append(layout.marginTop);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    marginWidth=");  //$NON-NLS-1$
		buffer.append(layout.marginWidth);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    pack=");  //$NON-NLS-1$
		buffer.append(layout.pack);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    spacing=");  //$NON-NLS-1$
		buffer.append(layout.spacing);

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    type=");  //$NON-NLS-1$
		buffer.append(layout.type);
		buffer.append(" (");  //$NON-NLS-1$
		buffer.append(SwtDebugUtility.getLayoutTypeText(layout.type));
		buffer.append(')');

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("    wrap=");  //$NON-NLS-1$
		buffer.append(layout.wrap);
	}

	private static void createToolTip(StringBuffer buffer, Control control, int childIndex) {
		if (childIndex != 0) {
			buffer.append(childIndex);
			buffer.append(". ");  //$NON-NLS-1$
		}

		buffer.append(control);
		buffer.append(SwtDebugUtility.getHashCodeText(control));

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("  parent=");  //$NON-NLS-1$
		Control parent = control.getParent();
		buffer.append(parent);
		buffer.append(SwtDebugUtility.getHashCodeText(parent));

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("  border width=");  //$NON-NLS-1$
		buffer.append(control.getBorderWidth());

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("  bounds=");  //$NON-NLS-1$
		buffer.append(control.getBounds());

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("  data=");  //$NON-NLS-1$
		buffer.append(control.getData());

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("  font=");  //$NON-NLS-1$
		Font font = control.getFont();
		FontData[] fontData = font.getFontData();
		buffer.append(fontData [ 0 ]);  // Always just one on Windows.

		if (control instanceof Composite) { // $codepro.audit.disable disallowInstanceof
			Composite composite = (Composite) control;
			SwtDebugUtility.createCompositeToolTip(buffer, composite);
		}

		Object layoutData = control.getLayoutData();
		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("  layout data=");  //$NON-NLS-1$

		if (layoutData instanceof GridData) { // $codepro.audit.disable disallowInstanceof
			GridData gridData = (GridData) layoutData;
			SwtDebugUtility.createGridDataToolTip(buffer, gridData);
		} else if (layoutData instanceof FormData) { // $codepro.audit.disable disallowInstanceof
			FormData formData = (FormData) layoutData;
			SwtDebugUtility.createFormDataToolTip(buffer, formData);
		} else if (layoutData instanceof RowData) { // $codepro.audit.disable disallowInstanceof
			RowData rowData = (RowData) layoutData;
			SwtDebugUtility.createRowDataToolTip(buffer, rowData);
		} else {
			buffer.append(layoutData);
		}

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("  location=");  //$NON-NLS-1$
		buffer.append(control.getLocation());

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("  size=");  //$NON-NLS-1$
		buffer.append(control.getSize());

		buffer.append(SwtDebugUtility.LINE_SEPARATOR);
		buffer.append("  style=");  //$NON-NLS-1$
		buffer.append(control.getStyle());
	}

	public static void decorate(Control control) {
		SwtDebugUtility.decorate(control, 0);
	}

	public static void decorate(Control control, int marginWidth) {
		if (control == null) throw new IllegalArgumentException("The argument 'control' must not be null");  //$NON-NLS-1$
		StringBuffer buffer = new StringBuffer(SwtDebugUtility.BUFFER_SIZE);
		SwtDebugUtility.decorate(buffer, control, marginWidth, 0, 0);
	}

	private static void decorate(StringBuffer buffer, Control control, int marginWidth, int colorIndex, int childIndex) {
		int count = SwtDebugUtility.COLORS.length;
		int index = colorIndex == count ? 0 : colorIndex;

		Color color = SwtDebugUtility.COLORS [ index ];
		control.setBackground(color);

		SwtDebugUtility.createToolTip(buffer, control, childIndex);
		String tip = SwtDebugUtility.getBufferValue(buffer);

		control.setToolTipText(tip);

		if (control instanceof Composite) { // $codepro.audit.disable disallowInstanceof
			Composite composite = (Composite) control;
			SwtDebugUtility.padComposite(composite, marginWidth);
			Control[] children = composite.getChildren();
			Control child;

			for (int i = 0; i < children.length; i++) {
				child = children [ i ];
				SwtDebugUtility.decorate(buffer, child, marginWidth, index + 1, i + 1);
			}
		}
	}

	private static String getAlignmentText(int alignment) {
		String text = null;

		switch (alignment) {
			case GridData.CENTER:
				text = "GridData.CENTER";  //$NON-NLS-1$
				break;
			case GridData.END:
				text = "GridData.END";  //$NON-NLS-1$
				break;
			case SWT.BEGINNING:  // Same value as GridData.BEGINNING
				text = "SWT.BEGINNING";  //$NON-NLS-1$
				break;
			case SWT.BOTTOM:
				text = "SWT.BOTTOM";  //$NON-NLS-1$
				break;
			case SWT.CENTER:
				text = "SWT.CENTER";  //$NON-NLS-1$
				break;
			case SWT.END:
				text = "SWT.END";  //$NON-NLS-1$
				break;
			case SWT.FILL:  // Same value as GridData.FILL
				text = "SWT.FILL";  //$NON-NLS-1$
				break;
			case SWT.LEFT:
				text = "SWT.LEFT";  //$NON-NLS-1$
				break;
			case SWT.RIGHT:
				text = "SWT.RIGHT";  //$NON-NLS-1$
				break;
			case SWT.TOP:
				text = "SWT.TOP";  //$NON-NLS-1$
				break;
			default:
				text = Integer.toString(alignment);
				break;
		}

		return text;
	}

	private static String getBufferValue(StringBuffer buffer) {
		String value = buffer.toString();
		buffer.setLength(0);
		return value;
	}

	private static String getHashCodeText(Object object) {
		long hashCode = object.hashCode();
		String hexString = Long.toHexString(hashCode);

		StringBuffer buffer = new StringBuffer(15);
		buffer.append('(');
		buffer.append(hexString);
		buffer.append(')');

		String result = SwtDebugUtility.getBufferValue(buffer);
		return result;
	}

	private static String getLayoutTypeText(int type) {
		String text;

		switch (type) {
			case SWT.HORIZONTAL:
				text = "SWT.HORIZONTAL";  //$NON-NLS-1$
				break;
			case SWT.VERTICAL:
				text = "SWT.VERTICAL";  //$NON-NLS-1$
				break;
			default:
				text = Integer.toString(type);
				break;
		}

		return text;
	}

	private static Color getSystemColor(int id) {
		Device device = Display.getDefault();
		Color color = device.getSystemColor(id);
		return color;
	}

	private static void padComposite(Composite composite, int marginWidth) {
		Layout compositeLayout = composite.getLayout();
		if (compositeLayout == null) return;  // Early return.

		if (compositeLayout instanceof GridLayout) {
			GridLayout layout = (GridLayout) compositeLayout;
			layout.marginWidth += marginWidth;
			layout.marginHeight += marginWidth;
		} else if (compositeLayout instanceof RowLayout) {
			RowLayout layout = (RowLayout) compositeLayout;
			layout.marginWidth += marginWidth;
			layout.marginHeight += marginWidth;
		} else if (compositeLayout instanceof FillLayout) {
			FillLayout layout = (FillLayout) compositeLayout;
			layout.marginWidth += marginWidth;
			layout.marginHeight += marginWidth;
		} else if (compositeLayout instanceof FormLayout) {
			FormLayout layout = (FormLayout) compositeLayout;
			layout.marginWidth += marginWidth;
			layout.marginHeight += marginWidth;
		}
	}

	public static void setBackground(Control control, int id) {
		Color color = SwtDebugUtility.getSystemColor(id);
		control.setBackground(color);
	}

	//
	// Constructors
	//

	private SwtDebugUtility() {
		super();
	}
}
