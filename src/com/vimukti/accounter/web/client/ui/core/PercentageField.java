package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.WidgetWithErrors;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class PercentageField extends TextItem {
	private Double percentage;
	private WidgetWithErrors errorsWidget;
	AccounterMessages messages = GWT.create(AccounterMessages.class);

	public PercentageField(WidgetWithErrors errorsWidget, final String name) {
		super(name,"percentageField");
		this.errorsWidget = errorsWidget;
		addBlurHandler(getBlurHandler());
		setPercentage(0.0);
		((TextBox) getMainWidget()).setTextAlignment(TextBoxBase.ALIGN_RIGHT);
	}

	public ChangeHandler getChangedHandler() {
		ChangeHandler handler = new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String strValue = getValue().toString();

				if (strValue.contains("%")) {
					strValue = strValue.replaceAll("%", "");
				}
				try {
					if (strValue.length() != 0) {
						Double enteredPercentageValue = Double
								.parseDouble(strValue);
						if (DecimalUtil.isLessThan(enteredPercentageValue, 0)
								|| DecimalUtil.isLessThan(
										enteredPercentageValue, 100)) {
							Accounter.showError(messages.invalidateEntry());
						}
						if (enteredPercentageValue != null) {
							setPercentage(enteredPercentageValue);
						}
					}
				} catch (Exception e) {
					Accounter.showError(messages.invalidEntry());
					setPercentage(0.0);
				}

			}
		};
		return handler;
	}

	private BlurHandler getBlurHandler() {

		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			public void onBlur(BlurEvent event) {

				String strValue = getValue().toString();

				if (strValue.contains("%")) {
					strValue = strValue.replaceAll("%", "");
				}
				try {
					if (strValue.length() != 0) {
						Double enteredPercentageValue = Double
								.parseDouble(strValue);
						if (enteredPercentageValue != null) {
							if (DecimalUtil.isLessThan(enteredPercentageValue,
									0)) {

								Accounter.showError(messages
										.cantenternegnumber());
								setPercentage(0.0);
							} else if (DecimalUtil.isGreaterThan(
									enteredPercentageValue, 100)) {

								Accounter.showError(messages
										.cantentermorethat100());

								setPercentage(0.0);
							} else {
								setPercentage(enteredPercentageValue);
								errorsWidget.clearError(this);
							}
						}
					} else {
						setPercentage(0.0);
					}

				} catch (Exception e) {
					setPercentage(0.0);
				}

			}
		};
		return blurHandler;
	}

	public void clearError() {
		errorsWidget.clearError(this);
	}

	/**
	 * @param percentage
	 *            the percentage to set
	 */
	public void setPercentage(Double percentage) {
		if (percentage == null)
			return;
		this.percentage = percentage;
		setValue(percentage + "%");
		// setValue(NumberFormat.getPercentFormat().format(percentage));
	}

	/**
	 * @return the percentage
	 */
	public Double getPercentage() {
		return percentage;
	}

}
