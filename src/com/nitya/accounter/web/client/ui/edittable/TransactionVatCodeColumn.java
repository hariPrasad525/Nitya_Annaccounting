package com.nitya.accounter.web.client.ui.edittable;

import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.ui.Accounter;

public abstract class TransactionVatCodeColumn extends
		ComboColumn<ClientTransactionItem, ClientTAXCode> {
	private final AbstractDropDownTable<ClientTAXCode> taxCodeTable = new TaxCodeTable(
			getTaxCodeFilter(), isSales());
	private final ClientCompany company;

	public TransactionVatCodeColumn() {
		company = Accounter.getCompany();
	}

	protected abstract ListFilter<ClientTAXCode> getTaxCodeFilter();

	protected abstract boolean isSales();

	@Override
	protected ClientTAXCode getValue(ClientTransactionItem row) {
		return company.getTAXCode(row.getTaxCode());
	}

	@Override
	public AbstractDropDownTable<ClientTAXCode> getDisplayTable(
			ClientTransactionItem row) {
		return taxCodeTable;
	}

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	protected String getColumnName() {
		return messages.taxCode();
	}

	@Override
	protected void setValue(ClientTransactionItem row, ClientTAXCode newValue) {
		if (newValue != null)
			row.setTaxCode(newValue.getID());
		getTable().update(row);
	}
}
