package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.user.cellview.client.Column;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;

public class TransactionVatColumn extends Column<ClientTransactionItem, Double> {

	public TransactionVatColumn() {
		super(new AmountCell());
		setSortable(true);
	}

	@Override
	public Double getValue(ClientTransactionItem object) {
		return object.getVATfraction();
	}

}
