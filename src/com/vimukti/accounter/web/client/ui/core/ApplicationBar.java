package com.vimukti.accounter.web.client.ui.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationBar implements IButtonBar {

	static HTMLPanel appBar;
	private static HashMap<Widget, Panel> map = new HashMap<Widget, Panel>();
	static HashSet<Widget> direct = new HashSet<Widget>();
	static HashSet<Button> permanent = new HashSet<Button>();

	private HTMLPanel getAppBar() {
		if (appBar == null) {
			appBar = HTMLPanel.wrap(Document.get().getElementById("appBar"));
		}
		return appBar;

	}

	public native void hideAppBar() /*-{
		var appBar = $wnd.document.getElementById("appBar").winControl;
		appBar.hide();
	}-*/;

	public native void showAppBar() /*-{
		var appBar = $wnd.document.getElementById("appBar").winControl;
		appBar.show();
	}-*/;

	public native void process(Element e) /*-{
		$wnd.WinJS.UI.process(e);
	}-*/;

	@Override
	public void addPermanent(Button btn) {
		btn.getElement().setAttribute("data-section", "selection");
		permanent.add(btn);
		addToAppBar(btn);
	}

	public void show() {
		boolean showAppBar = false;
		for (int x = 0; x < getAppBar().getWidgetCount(); x++) {
			Widget w = getAppBar().getWidget(x);
			if (direct.contains(w) || map.keySet().contains(w)
					|| permanent.contains(w)) {
				continue;
			}
			getAppBar().remove(w);
		}

		for (Widget w : direct) {
			showAppBar = true;
			addToAppBar((Button) w);
		}

		for (Entry<Widget, Panel> entry : map.entrySet()) {
			Widget key = entry.getKey();
			Panel parent = entry.getValue();
			if (!Boolean
					.valueOf(parent.getElement().getAttribute("data-group"))) {
				showAppBar = true;
			}
			addToAppBar((Button) key);
		}
		if (showAppBar) {
			showAppBar();
		} else {
			hideAppBar();
		}
	}

	private void addToAppBar(Button btn) {
		Element element = btn.getElement();

		element.removeAttribute("class");
		element.removeAttribute("type");
		element.removeAttribute("id");

		String title = btn.getTitle();
		String icon = element.getAttribute("data-icon");

		String label = btn.getText();
		for (int i = 0; i < element.getChildCount(); i++) {
			element.removeChild(element.getChild(i));
		}
		if (label == null || label.isEmpty()) {
			label = btn.getHTML();
		}

		element.setAttribute("data-win-control", "WinJS.UI.AppBarCommand");
		String section = element.getAttribute("data-section");
		if (section == null || section.isEmpty()) {
			section = "global";
		}

		element.setAttribute("data-win-options",
				"{id:'cmd" + label.replace(" ", "") + "',label:'" + label
						+ "',icon:'" + icon + "',section:'" + section
						+ "',tooltip:'" + title + "'}");

		element.setInnerHTML(" ");
		element.setInnerText("  ");

		HTMLPanel appBar = getAppBar();
		if (appBar.getWidgetIndex(btn) == -1) {
			appBar.add(btn);
			process(element);
		}
	}

	@Override
	public void add(Button btn) {
		direct.add(btn);
	}

	@Override
	public void add(Button btn, HorizontalAlignmentConstant alignment) {
		add(btn);
	}

	@Override
	public void addTo(HasWidgets w) {
		// NOTHING TO DO
	}

	@Override
	public Widget asWidget() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public void setDisabled(boolean disable) {
		// TODO Auto-generated method stub

	}

	public void removeButton(Panel parent, Button child) {
		remove(child);
		map.remove(child);
	}

	public void addButton(Panel parent, Button child) {
		map.put(child, parent);
	}

	@Override
	public void remove(Button widget) {
		if (widget == null) {
			return;
		}
		getAppBar().remove(widget);
	}

	@Override
	public void clear(Panel group) {
		HashSet<Widget> widgets = new HashSet<Widget>();
		for (Entry<Widget, Panel> entry : map.entrySet()) {
			if (group == entry.getValue()) {
				Widget widget = entry.getKey();
				widgets.add(widget);
			}
		}
		for (Widget widget : widgets) {
			getAppBar().remove(widget);
			map.remove(widget);
		}
	}

	@Override
	public void clear() {
		for (Widget widget : direct) {
			getAppBar().remove(widget);
		}
		direct.clear();

		for (Widget widget : map.keySet()) {
			getAppBar().remove(widget);
		}
		map.clear();

	}

	@Override
	public void clearDirectButtons() {
		for (Widget widget : direct) {
			getAppBar().remove(widget);
		}
		direct.clear();
	}

	@Override
	public void addPermanent(Panel parent, Button child) {
		addPermanent(child);
	}

	@Override
	public void remove() {
		getAppBar().clear();
		permanent.clear();
		direct.clear();
		map.clear();
		hideAppBar();
	}

}
