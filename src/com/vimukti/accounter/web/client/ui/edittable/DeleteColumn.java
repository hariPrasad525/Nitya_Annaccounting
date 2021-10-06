package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.vimukti.accounter.web.client.ui.Accounter;

public class DeleteColumn<T> extends ImageEditColumn<T> {

	@Override
	public ImageResource getResource(T row) {
		return Accounter.getFinanceImages().delete();
	}

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		Image image = new Image();
		image.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!getTable().isDisabled()) {
					getTable().delete(context.getRow());
				}
			}
		});
		return image;
	}

	@Override
	public int getWidth() {
		return 20;
	}


	@Override
	public int insertNewLineNumber() {
		return 0;
	}

	@Override
	public String getValueAsString(T row) {
		return "";
	}



}
