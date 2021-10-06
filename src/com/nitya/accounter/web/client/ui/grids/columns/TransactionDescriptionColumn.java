package com.nitya.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.view.client.ListDataProvider;
import com.nitya.accounter.web.client.core.ClientTransactionItem;

public class TransactionDescriptionColumn extends ColumnWithFieldUpdater<ClientTransactionItem, String> {

	private ListDataProvider<ClientTransactionItem> dataProvider;

	public TransactionDescriptionColumn(ListDataProvider<ClientTransactionItem> dataProvider) {
		super(new EditTextCell());
		this.dataProvider=dataProvider;
	}

	@Override
	public void update(int index, ClientTransactionItem object,
			String value) {
		object.setDescription(value);
		dataProvider.refresh();
	}

	@Override
	public String getValue(ClientTransactionItem object) {
		return object.getDescription();
	}

}
