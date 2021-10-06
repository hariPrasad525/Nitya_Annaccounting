package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.WidgetWithErrors;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class DoubleField extends TextItem {
	private double number;
	private WidgetWithErrors errorsWidget;

	public DoubleField(WidgetWithErrors errorsWidget, final String name) {
		super(name, "integerField");
		this.errorsWidget = errorsWidget;
		// ((TextBox)
		// getMainWidget()).setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		addBlurHandler(getBlurHandler());

		// setKeyPressHandler(new KeyPressListener() {
		//
		// @Override
		// public void onKeyPress(int keyCode) {
		// if (!((keyCode >= 48 && keyCode <= 57))) {
		// setValue("0");
		// }
		// }
		// });
	}

	public DoubleField(WidgetWithErrors errorsWidget, final String name,
			int maxLength) {
		super(name, "integerField", maxLength);
		this.errorsWidget = errorsWidget;
		addBlurHandler(getBlurHandler());
	}

	@Override
	public void setValue(String value) {
		super.setValue(value);
		if (value != null && !value.isEmpty())
			try {
				number = Long.parseLong(value);
			} catch (Exception e) {
			}
	}

	private BlurHandler getBlurHandler() {

		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			public void onBlur(BlurEvent event) {
				try {

					Double enteredNumber = 0.0;
					value = getValue();

					if (value == null)

						return;

					try {
						enteredNumber = Double.parseDouble(value.toString());
						if (enteredNumber != null) {
							setNumber(enteredNumber);
						}
						errorsWidget.clearError(this);
					} catch (Exception e) {
						if (value.toString().length() != 0) {
							DoubleField.this.highlight();
							// BaseView.errordata.setHTML("<li> "
							// + AccounterErrorType.INCORRECTINFORMATION
							// + ".");
							// BaseView.commentPanel.setVisible(true);
							errorsWidget
									.addError(
											this,
											Global.get()
													.messages()
													.pleaseEnter(
															messages.pleaseEnterValidNumber()));
						}
						// Accounter
						// .showError(AccounterErrorType.INCORRECTINFORMATION);

					}
				} catch (Exception e) {
					Accounter.showError(messages.invalidInfo());
				}

			}
		};
		return blurHandler;
	}

	public void setNumber(Double enteredNumber) {
		this.number = enteredNumber;
		if (enteredNumber != null)
			setValue(String.valueOf(enteredNumber));
		else
			setValue("");

	}

	/**
	 * @return the number
	 */
	public Double getNumber() {
		return number;
	}
}