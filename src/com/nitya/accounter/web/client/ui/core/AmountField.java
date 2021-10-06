package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.JNSI;
import com.nitya.accounter.web.client.ui.WidgetWithErrors;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class AmountField extends TextItem {

	private Double doubleAmount;

	private ClientCurrency currency;

	private BlurHandler blurHandler;

	private WidgetWithErrors errorsWidget;

	private boolean showCurrency = true;

	private boolean allowNegative;

	public AmountField(final String name, WidgetWithErrors errorsViewGrid,
			ClientCurrency currency, String styleName) {
		super(name, styleName);
		this.getElement().addClassName("AmountField");
		this.errorsWidget = errorsViewGrid;
		if (showCurrency) {
			setTitle(name + "(" + currency.getFormalName() + ")");
		} else {
			setTitle(name);
		}
		addBlurHandler(getBlurHandler());

		// Set Default Values
		setKeyPressFilter("[0-9.]");
		setAmount(0.00D);
		this.currency = currency;
	}

	public AmountField(String name, WidgetWithErrors errorsViewGrid,
			ClientCurrency currency, boolean allowNegative) {
		this(name, errorsViewGrid, currency, "AmountField");
		this.allowNegative = allowNegative;
	}

	public AmountField(final String name, WidgetWithErrors errorsViewGrid) {
		super(name, "AmountField");
		this.errorsWidget = errorsViewGrid;
		setTitle(name);
		addBlurHandler(getBlurHandler());
		setKeyPressFilter("[0-9.]");
		setAmount(0.00D);
		((TextBox) getMainWidget()).setTextAlignment(TextBoxBase.ALIGN_RIGHT);
	}

	private void setKeyPressFilter(String string) {
	}

	protected BlurHandler getBlurHandler() {

		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			public void onBlur(BlurEvent event) {
				try {
					errorsWidget.clearError(this);
					value = getValue();
					if (value == null)
						return;

					Double amount = DataUtils.getAmountStringAsDouble(JNSI
							.getCalcultedAmount(value.toString()));
					if (!AccounterValidator.isAmountTooLarge(amount)
							&& (allowNegative || (!allowNegative && !AccounterValidator
									.isAmountNegative(amount)))) {
						setAmount(amount);
					}
					if (AmountField.this.blurHandler != null) {
						AmountField.this.blurHandler.onBlur(event);
					}
				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						// BaseView.errordata.setHTML("<li>" + e.getMessage());
						// BaseView.commentPanel.setVisible(true);
						// errorsWidget.addError(this, e.getMessage());
						// Accounter.showError(e.getMessage());
					}

					// else if (value.toString().length() != 0) {
					// Accounter
					// .showError(AccounterErrorType.INCORRECTINFORMATION);
					// }
					setAmount(0.00);
				}
			}
		};
		return blurHandler;
	}

	public void setAmount(Double amount) {

		this.doubleAmount = amount;
		if (amount != null) {
			setValue(DataUtils.getAmountAsStringInCurrency(amount, null));
		} else {
			setValue("");
		}

	}

	public Double getAmount() {
		return this.doubleAmount;
	}

	public void setBlurHandler(BlurHandler blurHandler) {
		this.blurHandler = blurHandler;
	}

	public void setCurrency(ClientCurrency currency) {
		if (currency == null) {
			return;
		}
		this.currency = currency;
		if (showCurrency) {
			String title2 = getTitle();
			String[] split = title2.split("\\(", 3);
			setTitle(split[0] + "(" + currency.getFormalName() + ")");
		} else {
			setTitle(getTitle());
		}
	}

	public ClientCurrency getCurrency() {
		return currency;
	}

	public void setShowCurrency(boolean showCurrency) {
		this.showCurrency = showCurrency;
	}

	public boolean isShowCurrency() {
		return showCurrency;
	}

	// private FocusHandler getFocusHandler() {
	// FocusHandler focusHandler = new FocusHandler() {
	// Object value = null;
	//
	// @Override
	// public void onFocus(FocusEvent event) {
	// try {
	//
	// value = ((TextItem) event.getSource()).getValue();
	//
	// if (value == null)
	// return;
	//
	// setValue(DataUtils.getAmountStringAsDouble(DataUtils
	// .removeDollar(value.toString())));
	//
	// } catch (Exception e) {
	// //
	// }
	//
	// }
	// };
	// return focusHandler;
	//
	// }
}
