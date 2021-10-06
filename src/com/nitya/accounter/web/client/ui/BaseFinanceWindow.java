package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class BaseFinanceWindow extends FlowPanel {

	private BaseFinanceWindow parent;
	protected ArrayList<IWindowListener> listeners = new ArrayList<IWindowListener>();

	public BaseFinanceWindow(BaseFinanceWindow parent) {

		this.parent = parent;
		// if (parent != null);
		// createWindowControls();
	}

	public BaseFinanceWindow() {

	}

	// public void createWindowControls() {
	//
	// setCanDragResize(true);
	// setKeepInParentRect(true);
	//
	// addCloseClickHandler(new CloseClickHandler() {
	// public void onCloseClick(CloseClientEvent event) {
	// destroy();
	// }
	// });
	//
	// }

	public void addWindowListener(IWindowListener listener) {
		listeners.add(listener);
	}

	public void removeWindowListener(IWindowListener listener) {
		listeners.remove(listener);
	}

	public void fireWindowCloseEvent() {

		IWindowListener[] listenerList = listeners
				.toArray(new IWindowListener[listeners.size()]);
		for (IWindowListener listener : listenerList)
			listener.windowClosed();
	}

	public void windowClosed() {
		// closeWindow();
	}

	public void windowOpened() {

	}

}
