package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.customers.CurrencyGroupListView;

public class CurrencyCombo extends CustomCombo<ClientCurrency> {

	public CurrencyCombo(String title) {
		super(title, "currencyCombo");
		List<ClientCurrency> currency = new ArrayList<ClientCurrency>(
				getCompany().getCurrencies());
		initCombo(currency);
		if (!currency.isEmpty()) {
			setSelected(getCompany().getPrimaryCurrency().getFormalName());
		}
	}

	public CurrencyCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1, "currencyCombo");
		List<ClientCurrency> currency = new ArrayList<ClientCurrency>(
				getCompany().getCurrencies());
		initCombo(currency);
	}

	@Override
	protected String getDisplayName(ClientCurrency object) {

		if (object != null)
			return object.getFormalName() != null ? object.getFormalName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientCurrency object, int col) {
		switch (col) {
		case 0:
			return object.getFormalName();
		}
		return null;

	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.currency();
	}

	@Override
	public void onAddNew() {
		CurrencyGroupListView groupListDialog = new CurrencyGroupListView();
		groupListDialog.setVisible(false);
		groupListDialog.setCallback(createAddNewCallBack());
		groupListDialog.ShowAddEditDialog(null);

	}

}
