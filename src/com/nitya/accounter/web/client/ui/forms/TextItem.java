package com.nitya.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.nitya.accounter.web.client.ui.forms.TextBoxItem.KeyPressListener;

public class TextItem extends FormItem<String> {

	public TextBoxItem textBox;

	public TextItem(String title, String styleName) {
		super(title, styleName);
		textBox = new TextBoxItem();
		setPlaceholder(title, textBox);
		textBox.addStyleName("textitem");
		this.textBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				TextItem.this.showValidated();
			}
		});
		this.add(textBox);
	}

	public TextItem(String title, String styleName, int maxLength) {
		this(title, styleName);
		this.textBox.setMaxLength(maxLength);
	}

	public static void setPlaceholder(String text, TextBox box) {
		box.getElement().setAttribute("placeholder", text);
	}

	public static void removePlaceholder(TextBox box) {
		box.getElement().removeAttribute("placeholder");
	}

	@Override
	public void setTitle(String string) {
		super.setTitle(string);
	}

	@Override
	public String getValue() {
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
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		textBox.setTitle(toolTip);
	}

	@Override
	public void setValue(String value) {
		if (value != null) {
			this.textBox.setText(value);
		}
	}

	public void setKeyPressHandler(KeyPressListener keyPressListener) {
		this.textBox.setKeyPressHandler(keyPressListener);
	}

	@Override
	public void addBlurHandler(BlurHandler blurHandler) {
		textBox.addBlurHandler(blurHandler);

	}

	@Override
	public void addFocusHandler(
			com.google.gwt.event.dom.client.FocusHandler focusHandler) {
		this.textBox.addFocusHandler(focusHandler);
	};

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
	}

	public void setDefaultValue(int i) {
		// NOTHING TO DO.
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
	public FocusWidget getMainWidget() {
		return textBox;
	}

	@Override
	public void setEnabled(boolean b) {

		if (!b) {
			this.textBox.addStyleName("disable-TextField");
			this.addStyleName("disable");
			removePlaceholder(textBox);
		} else {
			this.textBox.removeStyleName("disable-TextField");
			this.removeStyleName("disable");
			this.textBox.setStyleName("gwt-TextBox");
		}
		this.textBox.setEnabled(b);

	}

	public void setTabIndex(int index) {
		textBox.setTabIndex(index);
	}
}