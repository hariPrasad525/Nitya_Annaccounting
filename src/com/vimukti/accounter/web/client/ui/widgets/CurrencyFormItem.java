package com.vimukti.accounter.web.client.ui.widgets;

import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CurrencyFormItem extends TextItem {

	public CurrencyFormItem(String string, String formalName) {
		super(string,"CurrencyFormItem");
	}

	public void setValue(double factor) {
		super.setValue(Double.toString(factor));

	}

}
