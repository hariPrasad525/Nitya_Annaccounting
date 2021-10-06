package com.nitya.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LabelItem extends FormItem<String> {

	private Label label;

	public LabelItem(String title, String styleName) {
		super(title, styleName);
		label = new Label();
		label.getElement().setClassName("label");
		this.add(label);
	}

	@Override
	public Widget getMainWidget() {
		return label;
	}

	@Override
	public void setValue(String value) {
		this.label.setText(value);
		super.setValue(value);
	}

	public void addStyleName(String style) {
		label.addStyleName(style);

	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		label.setTitle(toolTip);
	}

	public void setWidth(String width) {
		label.setWidth(width);
	}

	public void addClickHandler(ClickHandler handler) {
		label.addClickHandler(handler);
	}

}
