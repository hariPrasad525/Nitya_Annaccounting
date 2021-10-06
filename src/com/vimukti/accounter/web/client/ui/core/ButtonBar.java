package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class ButtonBar implements IButtonBar {
	private List<Widget> widgets = new ArrayList<Widget>();

	FlowPanel buttonBar;

	public ButtonBar() {
		buttonBar = new FlowPanel();
		buttonBar.addStyleName("button_bar");
	}

	public void add(Button widget) {
		this.add(widget, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	public void remove(Button widget) {
		buttonBar.remove(widget);
	}

	public void add(Button widget, HorizontalAlignmentConstant alignment) {
		if (widget == null || buttonBar.getWidgetIndex(widget) >= 0) {
			return;
		}
		buttonBar.add(widget);
		// this.setCellHorizontalAlignment(widget, alignment);
		this.widgets.add(widget);
	}

	public void setDisabled(boolean disable) {
		for (Widget widget : widgets) {
			if (widget instanceof Button) {
				((Button) widget).setEnabled(!disable);
				if (disable) {
					widget.addStyleName("disable");
				} else {
					widget.removeStyleName("disable");
				}
			}
		}
	}

	@Override
	public void addTo(HasWidgets w) {
		w.add(buttonBar);
	}

	@Override
	public void removeButton(Panel parent, Button child) {
		if (child == null) {
			return;
		}
		parent.remove(child);
	}

	@Override
	public void addButton(Panel parent, Button child) {
		if (child == null) {
			return;
		}
		parent.add(child);
	}

	@Override
	public Widget asWidget() {
		return buttonBar;
	}

	@Override
	public void clear(Panel group) {
		group.clear();
	}

	@Override
	public void clear() {
		buttonBar.clear();
	}

	@Override
	public void addPermanent(Button btn) {
		add(btn);
	}

	@Override
	public void clearDirectButtons() {
		for (Widget w : widgets) {
			buttonBar.remove(w);
		}
		widgets.clear();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPermanent(Panel parent, Button child) {
		parent.add(child);
	}

	public void remove() {
		// TODO Auto-generated method stub

	}
}
