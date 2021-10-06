package com.nitya.accounter.web.client.ui.widgets;

import com.nitya.accounter.web.client.core.ClientCurrency;

public interface CurrencyChangeListener {
	void currencyChanged(ClientCurrency currency, double factor);
}
