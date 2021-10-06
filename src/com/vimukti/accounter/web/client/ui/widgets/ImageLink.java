package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class ImageLink extends FlowPanel {

	private Anchor link;

	public ImageLink(String text, Image image) {
		addStyleName("imageLink");
		// Force the size of image to 16 x 16
		if (image != null) {
			image.setSize("16px", "16px");
			add(image);
		}
		this.link = new Anchor(text);
		add(link);

	}

	public void addClickHandler(ClickHandler handler) {
		link.addClickHandler(handler);
	}
}
