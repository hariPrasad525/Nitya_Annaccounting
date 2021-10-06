package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class EmailField extends TextItem {
	private String text;

	public EmailField(final String name) {
		super(name, "emailField");
	}

	public EmailField(final String name, int maxLength) {
		super(name, "emailField", maxLength);
	}

	private BlurHandler getBlurHandler() {
		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			public void onBlur(BlurEvent event) {

				String emailText = ((TextBox) event.getSource()).toString();
				if (!UIUtils.isValidEmail(emailText)) {
					setText("");
					try {
						throw new Exception(messages.invalidCharactersGiven());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else
					setEmail(emailText);
			}
		};
		return blurHandler;
	}

	public void setEmail(String emailText) {
		if (emailText == null)
			emailText = "";
		this.text = emailText;
		setValue(emailText);

	}

	public void setText(String text) {
		this.text = text;
		setValue(text);
	}

	public String getEmail() {

		return this.text;

	}

	public String getText() {
		return text;
	}

}
