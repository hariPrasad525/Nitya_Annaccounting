package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class TransactionQuantityColumn extends
		QuantityColumn<ClientTransactionItem> {

	@Override
	protected ClientQuantity getQuantity(ClientTransactionItem row) {
		return row.getQuantity();
	}

	@Override
	protected void setQuantity(ClientTransactionItem row,
			ClientQuantity quantity) {
		row.setQuantity(quantity);
		// TODO doubt, currencyConversion.
		double lt = quantity.getValue() * row.getUnitPrice();
		double disc = row.getDiscount();
		row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt * disc / 100))
				: lt);
		getTable().update(row);
	}

	@Override
	protected String getColumnName() {
		return messages.quantity();
	}

	@Override
	public String getValueAsString(ClientTransactionItem row) {
		return getColumnName()+" : "+getValue(row);
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}
}
