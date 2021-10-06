package com.vimukti.accounter.web.client.ui.widgets;

import com.vimukti.accounter.web.client.core.ClientCurrency;

public interface CurrencyChangeListener {
	void currencyChanged(ClientCurrency currency, double factor);
}
