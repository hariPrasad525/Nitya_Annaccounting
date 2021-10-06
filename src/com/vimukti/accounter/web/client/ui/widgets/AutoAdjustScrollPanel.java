package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

public class AutoAdjustScrollPanel extends ScrollPanel {

	public AutoAdjustScrollPanel() {
		super();
		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				onLoad();
			}
		});
	}

	@Override
	protected void onLoad() {
//		setHeight("1px");
		new Timer() {

			@Override
			public void run() {
//				int height = getParent().getElement().getOffsetHeight();
//				setHeight(height + "px");

			}
		}.schedule(100);
	}

	public void reAlign() {
		onLoad();
	}

}
