package com.nitya.accounter.web.client.ui.combo;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.ui.CoreUtils;

/**
 * this class is used to display all the currencies list
 * 
 */
public class CurrencyListCombo extends CustomCombo<ClientCurrency> {
	public CurrencyListCombo(String text) {
		super(text, false, 1,"currencyListCombo");

		initCombo(getCurrencyList());

	}

	public List<ClientCurrency> getCurrencyList() {
		return CoreUtils.getCurrencies(getCompany().getCurrencies());

	}

	protected String getColumnData(ClientCurrency object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	protected String getDisplayName(ClientCurrency object) {

		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientCurrency object, int col) {
		return object.getName();
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDefaultAddNewCaption() {
		// TODO Auto-generated method stub
		return " ";
	}

}
