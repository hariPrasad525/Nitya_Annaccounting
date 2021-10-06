package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TransactionVatCodeColumn extends Column<ClientTransactionItem, ClientTAXCode>
		implements FieldUpdater<ClientTransactionItem, ClientTAXCode> {

	public TransactionVatCodeColumn() {
		super(new VatComboCell());
		this.setSortable(true);
		this.setFieldUpdater(this);
	}

	@Override
	public void update(int index, ClientTransactionItem object,
			ClientTAXCode value) {
		object.setTaxCode(value.getID());
	}

	@Override
	public ClientTAXCode getValue(ClientTransactionItem object) {
		return getCompany().getTAXCode(object.getTaxCode());
	}

	private ClientCompany getCompany() {
		return Accounter.getCompany();
	}

}
