package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem.KeyPressListener;

public class CustomComboItem extends FormItem {

	@Override
	public void setFocus() {
		textBox.setFocus(true);
	}

	public TextBoxItem textBox;
	private StyledPanel downarrowpanel;
	private Widget mainWidget;
	private long selectedValue;

	public CustomComboItem(String title, String styleName) {
		super(title, styleName);
		ComboPanel panel = new ComboPanel("panel");

		textBox = new TextBoxItem();
		textBox.sinkEvents(0);
		panel.add(textBox);
		downarrowpanel = new StyledPanel("downarrow-button");
		panel.add(downarrowpanel);
		mainWidget = panel;
		mainWidget.addStyleName("customComboItem");
		this.add(mainWidget);
	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		String tooltip = toolTip.replace("  ", " ");
		textBox.setTitle(tooltip);
	}

	// public CustomComboItem(String title) {
	// this();
	// setTitle(title);
	// }

	@Override
	public Object getValue() {
		if (textBox.getText() == null)
			return "";
		return textBox.getText();
	}

	@Override
	public String getDisplayValue() {
		if (textBox.getText() == null)
			return "";
		return textBox.getText();

	}

	@Override
	public void setValue(Object value) {
		if (value != null)
			this.textBox.setValue(value.toString());

	}

	public void setKeyPressHandler(KeyPressListener keyPressListener) {
		this.textBox.setKeyPressHandler(keyPressListener);
	}

	@Override
	public void addFocusHandler(FocusHandler handler) {
		textBox.addFocusHandler(handler);
	}

	@Override
	public void addBlurHandler(BlurHandler blurHandler) {
		textBox.addBlurHandler(blurHandler);
	}

	public void setKeyBoardHandler(KeyPressHandler keyPressHandler) {
		textBox.addKeyPressHandler(keyPressHandler);
	}

	public void setKeyDownHandler(KeyDownHandler keyDownHandler) {
		textBox.addKeyDownHandler(keyDownHandler);
	}

	@Override
	public void addChangeHandler(ChangeHandler changeHandler) {
		textBox.addChangeHandler(changeHandler);
	}

	@Override
	public void addClickHandler(ClickHandler handler) {
		textBox.addClickHandler(handler);
		downarrowpanel.addDomHandler(handler, ClickEvent.getType());
	}

	public void setDefaultValue(int i) {
		// TODO Auto-generated method stub

	}

	// void helpimformationsetposition(){
	//
	// }

	public void setHint(String string) {

	}

	public void focusInItem() {
		this.textBox.setFocus(true);

	}

	@Override
	public Widget getMainWidget() {
		return mainWidget;
	}

	@Override
	public void setEnabled(boolean value) {
		super.setEnabled(value);
		if (!value) {
			this.addStyleName("disabled");
			this.textBox.addStyleName("disable-TextField");
		} else {
			this.removeStyleName("disabled");
			this.textBox.removeStyleName("disable-TextField");
		}
		this.textBox.setEnabled(value);
		((HasEnabled) this.mainWidget).setEnabled(value);
	}

	public void removeStyleName(String style) {
		textBox.removeStyleName(style);
	}

	public void setTabIndex(int index) {
		textBox.setTabIndex(index);
	}


	}
