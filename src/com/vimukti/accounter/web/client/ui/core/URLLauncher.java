package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.Window;

public class URLLauncher {

	public void launch(String url) {
		launch(url, false);
	}

	public void launch(String url, boolean newTab) {

		if (newTab) {
			Window.open(url, null, null);
		} else {
			Window.open(url, "_blank", null);
		}
	}

}
