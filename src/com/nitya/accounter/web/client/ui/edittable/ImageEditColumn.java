package com.nitya.accounter.web.client.ui.edittable;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;

public abstract class ImageEditColumn<T> extends EditColumn<T> {

	public abstract ImageResource getResource(T row);

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		Image image = (Image) widget;
		ImageResource resource = getResource(context.getRow());
		//For Image we will get null. We don't know why.
		if (image != null) {
			image.setResource(resource);
		}
	}

	@Override
	public IsWidget getWidget(RenderContext<T> context) {
		return new Image();
	}




}
