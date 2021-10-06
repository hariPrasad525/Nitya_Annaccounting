package com.nitya.accounter.web.client.ui.win8;

import com.nitya.accounter.web.client.ui.core.URLLauncher;

public class Win8URLLauncher extends URLLauncher {

	public void launch(String url, boolean newTab) {
		launchURL(url);
	}

	protected native void launchURL(String url) /*-{
		var uri = new $wnd.Windows.Foundation.Uri(url);
		$wnd.Windows.System.Launcher.launchUriAsync(uri)
	}-*/;

}
