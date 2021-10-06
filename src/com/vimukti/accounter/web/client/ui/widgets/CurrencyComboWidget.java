package com.vimukti.accounter.web.client.ui.widgets;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.combo.CurrencyCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class CurrencyComboWidget extends DynamicForm {

	private CurrencyChangeListener listener;
	private final CurrencyCombo currencyCombo;
	private final ClientCurrency baseCurrency;
	private ClientCurrency selectedCurrencyItem;
	private List<ClientCurrency> currencies;
	private boolean showFactorField = true;
	private final CurrencyFormItem currencyForm;
	private double factor = 1.0;

	public CurrencyComboWidget(List<ClientCurrency> currencies,
			ClientCurrency baseCurrency) {
		super("currencyComboWidget");
		this.currencies = currencies;
		this.baseCurrency = baseCurrency;

		currencyCombo = new CurrencyCombo(Global.get().messages().currency());
		currencyCombo.initCombo(currencies);

		if (baseCurrency != null) {
			currencyCombo.setSelected(baseCurrency.getFormalName());
		}
		currencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCurrency>() {

					@Override
					public void selectedComboBoxItem(ClientCurrency selectItem) {
						currencyChanged(selectItem);
					}
				});

		setStyleName("currencyTextBox");

		currencyForm = new CurrencyFormItem(messages.factor(),
				baseCurrency.getFormalName());

		currencyForm.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				String factorStr = currencyForm.getValue().toString();
				factorFieldChagned(Double.parseDouble(factorStr));
			}
		});

		add(currencyCombo);
		add(currencyForm);

		currencyForm.hide();

	}

	private void factorFieldChagned(double factor) {
		this.factor = factor;
		// when enter the negative factor then set the factor to 1.0
		if (factor <= 0.0) {
			factor = 1.0;
			this.factor = 1.0;
			currencyForm.setValue(factor);
		}
		if (listener != null) {
			ClientCurrency selectedValue = currencyCombo.getSelectedValue();
			listener.currencyChanged(selectedValue, factor);
		}
	}

	public void currencyChanged(ClientCurrency selectItem) {
		this.selectedCurrencyItem = selectItem;
		setShowFactorField(selectItem.equals(baseCurrency));

		showFactorField(isShowFactorField());
		if (listener != null) {
			listener.currencyChanged(selectedCurrencyItem, 1);
		}
	}

	private void showFactorField(boolean showFactorField) {
		// currencyForm.setDisabled(showFactorField);
		updateFactorFieldTitle(); // 1<SELCTED currency>=
		double factor = 1;/*
						 * factorFieldDisableStatus ? 1 : getFactorByRPC(
						 * selectItem, baseCurrency);
						 */
		currencyForm.setValue(String.valueOf(factor));

		// hide or show factor fields
		if (showFactorField) {
			currencyForm.hide();
		} else {
			currencyForm.show();
		}

	}

	private double getFactorByRPC(String foreginCurrencyCode,
			String baseCurrencyCode) {
		// TODO GET currency factor BY RPC
		return 0;
	}

	public void setListener(CurrencyChangeListener listener) {
		this.listener = listener;
	}

	public void setCurrencies(List<ClientCurrency> currencies) {
		this.currencies = currencies;
		currencyCombo.initCombo(this.currencies);
	}

	private void updateFactorFieldTitle() {
		ClientCurrency currency = currencyCombo.getSelectedValue();
		StringBuffer sb = new StringBuffer();
		sb.append(1).append(" ").append(currency.getFormalName()).append(" =");
		currencyForm.setTitle(sb.toString());
	}

	public void setSelectedCurrency(ClientCurrency clientCurrency) {
		if (clientCurrency == null) {
			return;
		}
		currencyCombo.setSelected(clientCurrency.getFormalName());
		setShowFactorField(clientCurrency.equals(baseCurrency));
		showFactorField(showFactorField);
		selectedCurrencyItem = clientCurrency;

	}

	public ClientCurrency getSelectedCurrency() {
		return currencyCombo.getSelectedValue();
	}

	public void setCurrencyFactor(double factor) {
		currencyForm.setValue(String.valueOf(factor));
	}

	public double getCurrencyFactor() {
		if (currencyForm.getValue().length() > 0)
			return Double.parseDouble(currencyForm.getValue().toString());
		else
			return 1;
	}

	public void setTabIndex(int index) {
		currencyCombo.setTabIndex(index);
	}

	public boolean isShowFactorField() {
		return showFactorField;
	}

	public void setShowFactorField(boolean showFactorField) {
		this.showFactorField = showFactorField;
	}

	public void disabledFactorField(boolean showFactorField) {
		if (showFactorField) {
			currencyForm.hide();
		} else {
			currencyForm.show();
		}
	}

	public void setEnabled(boolean isAccountDisabled, boolean isFormDisabled) {
		currencyCombo.setEnabled(isAccountDisabled);
		currencyForm.setEnabled(isFormDisabled);
	}

}
