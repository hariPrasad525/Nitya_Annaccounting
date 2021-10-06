package com.nitya.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

public class LinkItem extends FormItem<String> {

	Hyperlink hyperlink;

	public LinkItem(String title, String styleName) {
		super(title, styleName);
		this.hyperlink = new Hyperlink();
		hyperlink.addStyleName("hyperLink");
		this.add(hyperlink);
	}

	public LinkItem() {
		this("","linkItem");
	}

	public void setLinkTitle(String title) {
		this.hyperlink.setText(title);

	}

	@Override
	public String getValue() {
		return hyperlink.getText();
	}

	@Override
	public String getDisplayValue() {
		return hyperlink.getText();

	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		hyperlink.setTitle(toolTip);
	}

	@Override
	public void addClickHandler(ClickHandler clickHandler) {
		this.hyperlink.addClickHandler(clickHandler);

	}

	@Override
	public void setValue(String value) {
		this.hyperlink.setText(value);
	}

	@Override
	public Widget getMainWidget() {
		return this.hyperlink;
	}

}
