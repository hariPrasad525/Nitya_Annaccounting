package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Portlet;

public class PortletColumn extends FlowPanel {

	private int index;
	private List<Portlet> portlets = new ArrayList<Portlet>();

	public PortletColumn(int columnIndex) {
		this.index = columnIndex;
		this.addStyleName("portletColumn");
	}

	public void refreshWidgets() {
		for (Portlet portlet : portlets) {
			if (!portlet.isInitializing()) {
				portlet.refreshWidget();
			}
		}
	}

	public void addPortlet(Portlet portlet) {
		this.portlets.add(portlet);
		this.add(portlet);
	}

	public List<Portlet> getPortlets() {
		update();
		return portlets;
	}

	/**
	 * 
	 */
	private void update() {
		this.portlets.clear();
		for (Widget child : getChildren()) {
			if (child instanceof Portlet) {
				Portlet portlet = (Portlet) child;
				// Update the index
				portlet.getConfiguration().column = this.index;
				this.portlets.add(portlet);
			}
		}
	}
}
