package com.nitya.accounter.web.client.ui.forms;

import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.ui.DataUtils;

public class AmountLabel extends LabelItem {

	private Double doubleAmount;

	private ClientCurrency currency;

	public AmountLabel(final String name) {
		super(name, "AmountLabel");
		setTitle(name);
		setAmount(0.00D);
	}

	public AmountLabel(String name, ClientCurrency currency) {
		super(name, "AmountLabel");
		setTitle(name);
		this.currency = currency;
		setAmount(0.00D);
	}

	public void setAmount(Double amount) {

		this.doubleAmount = amount;
		setValue(amount == null ? "" : DataUtils.getAmountAsStringInCurrency(
				amount, currency != null ? currency.getSymbol() : null));

	}

	public void setCurrency(ClientCurrency currency) {
		this.currency = currency;
		setAmount(doubleAmount);
	}

	public Double getAmount() {
		return this.doubleAmount;
	}
}
