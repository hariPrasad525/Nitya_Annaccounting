/**
 * 
 */
package com.nitya.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ImageActionCell extends ActionCell<ImageResource> {

	/**
	 * Creates new Instance
	 */
	public ImageActionCell() {
		super("", null);
	}

	@Override
	public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
		if (value != null) {
			SafeHtml html = SafeHtmlUtils
					.fromTrustedString(AbstractImagePrototype.create(value)
							.getHTML());
			sb.append(html);
		}
	}

	@Override
	protected void onEnterKeyDown(
			com.google.gwt.cell.client.Cell.Context context, Element parent,
			ImageResource value, NativeEvent event,
			ValueUpdater<ImageResource> valueUpdater) {
		if (valueUpdater != null) {
			valueUpdater.update(value);
		}
	}
}
