package com.nitya.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.ListDataProvider;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.IAccountable;

public class TransactionItemNameColumn extends
		Column<ClientTransactionItem, IAccountable> implements
		FieldUpdater<ClientTransactionItem, IAccountable> {

	private ListDataProvider<ClientTransactionItem> dataProvider;

	public TransactionItemNameColumn(ListDataProvider<ClientTransactionItem> dataProvider) {
		super(new ComboCell());
		this.setSortable(true);
		this.setFieldUpdater(this);
		this.dataProvider=dataProvider;
	}

	@Override
	public IAccountable getValue(ClientTransactionItem object) {
		return object.getAccountable();
	}

	@Override
	public void update(int index, ClientTransactionItem object,
			IAccountable value) {
		object.setAccountable(value);
		dataProvider.refresh();
	}

}
