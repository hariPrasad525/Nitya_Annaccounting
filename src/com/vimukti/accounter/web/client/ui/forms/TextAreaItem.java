package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class TextAreaItem extends FormItem<String> {

	TextArea textArea;

	public TextAreaItem(String title, String styleName) {
		super(title, styleName);
		textArea = new TextArea();
		textArea.addStyleName("textArea");
		this.add(textArea);
	}


	public String getValue() {
		return textArea.getText();
	}

	public void setValue(String value) {
		this.textArea.setValue(value);
	}

	public String getDisplayValue() {
		return textArea.getText();

	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		textArea.setTitle(toolTip);
	}

	@Override
	public Widget getMainWidget() {
		return this.textArea;
	}

	public void setDisabled(boolean b) {
		if (b) {
			this.addStyleName("disable");
			this.textArea.addStyleName("disable");
		} else {
			textArea.removeStyleName("disable");
			this.removeStyleName("disable");
		}
		this.textArea.setEnabled(!b);

	}

	public void addClickHandler(ClickHandler handler) {

		this.textArea.addClickHandler(handler);
	}

	public void setTabIndex(int index) {
		textArea.setTabIndex(index);
	}

}
