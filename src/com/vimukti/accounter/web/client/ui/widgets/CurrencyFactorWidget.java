package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class CurrencyFactorWidget extends DynamicForm {

	private CurrencyChangeListener listener;
	private final ClientCurrency baseCurrency;
	private ClientCurrency selectedCurrencyItem;
	private boolean showFactorField = true;
	private final CurrencyFormItem currencyForm;
	double factor = 1.0;

	public CurrencyFactorWidget(ClientCurrency baseCurrency) {
		super("CurrencyFactorWidget");
		this.baseCurrency = baseCurrency;
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

		add(currencyForm);
		currencyForm.hide();

	}

	private void factorFieldChagned(double factor) {
		this.factor = factor;
		if (factor <= 0.0) {
			factor = 1.0;
			this.factor = 1.0;
			currencyForm.setValue(factor);
		}
		if (listener != null) {
			listener.currencyChanged(selectedCurrencyItem, factor);
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
		if (selectedCurrencyItem == null) {
			selectedCurrencyItem = Accounter.getCompany().getPrimaryCurrency();
		}
		updateFactorFieldTitle(); // 1<SELCTED currency>=
		currencyForm.setValue(String.valueOf(factor));

		// hide or show factor fields
		if (showFactorField) {
			currencyForm.hide();
		} else {
			currencyForm.show();
			currencyForm.setRequired(true);
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

	private void updateFactorFieldTitle() {
		StringBuffer sb = new StringBuffer();
		sb.append(1).append(" ").append(selectedCurrencyItem.getFormalName())
				.append(" =");
		currencyForm.setTitle(sb.toString());
	}

	public void setSelectedCurrency(ClientCurrency clientCurrency) {
		if (clientCurrency == null) {
			return;
		}
		setShowFactorField(clientCurrency.equals(baseCurrency));
		selectedCurrencyItem = clientCurrency;
		showFactorField(showFactorField);
	}

	public void compareCurrency(ClientCurrency clientCurrency1,
			ClientCurrency clientCurrency2) {
		setShowFactorField(clientCurrency1.equals(clientCurrency2));

		if (!clientCurrency1.equals(baseCurrency)) {
			selectedCurrencyItem = clientCurrency1;
		} else {
			selectedCurrencyItem = clientCurrency2;
		}
		showFactorField(showFactorField);
	}

	public ClientCurrency getSelectedCurrency() {
		return selectedCurrencyItem == null ? baseCurrency
				: selectedCurrencyItem;
	}

	public void setCurrencyFactor(double factor) {
		this.factor = factor;
		currencyForm.setValue(String.valueOf(factor));
	}

	public double getCurrencyFactor() {
		return factor;
	}

	public void setTabIndex(int index) {
		// currencyCombo.setTabIndex(index);
	}

	public boolean isShowFactorField() {
		return showFactorField;
	}

	public void setShowFactorField(boolean showFactorField) {
		this.showFactorField = showFactorField;
	}

	public void setSelectedCurrencyFactorInWidget(ClientCurrency currency,
			long toDate) {
		setShowFactorField(currency.equals(baseCurrency));
		selectedCurrencyItem = currency;
		showFactorField(showFactorField);
		if (!showFactorField) {
			Accounter.createHomeService()
					.getMostRecentTransactionCurrencyFactor(
							Accounter.getCompany().getID(), currency.getID(),
							toDate, new AsyncCallback<Double>() {

								@Override
								public void onFailure(Throwable caught) {
									gotTodaysCurrencyFactor(1);
								}

								@Override
								public void onSuccess(Double result) {
									gotTodaysCurrencyFactor(result);
								}
							});
		} else {
			setCurrencyFactor(1);
		}
	}

	private void gotTodaysCurrencyFactor(double currencyFactor) {
		setCurrencyFactor(currencyFactor);
		if (listener != null) {
			listener.currencyChanged(selectedCurrencyItem, currencyFactor);
		}
	}

}
