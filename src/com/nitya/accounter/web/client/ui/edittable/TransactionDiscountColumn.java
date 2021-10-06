package com.nitya.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBoxBase;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;

public class TransactionDiscountColumn extends
		AmountColumn<ClientTransactionItem> {

	public TransactionDiscountColumn(ICurrencyProvider currencyProvider) {
		super(currencyProvider, false);
	}

	@Override
	protected Double getAmount(ClientTransactionItem row) {
		return row.getDiscount();
	}

	@Override
	protected String getValue(ClientTransactionItem row) {
		Double amount = getAmount(row);
		if (amount == null) {
			return "";
		}
		return DataUtils.getAmountAsStringInCurrency(amount, null);
	}

	@Override
	public void setValue(ClientTransactionItem row, String value) {
		try {
			double amount = DataUtils.getAmountStringAsDouble(value);
			setAmount(row, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setAmount(ClientTransactionItem row, Double value) {
		row.setDiscount(value);
		// TODO doubt, currencyConversion.
		double lt = row.getQuantity().getValue() * row.getUnitPrice();
		double disc = row.getDiscount();
		row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt * disc / 100))
				: lt);
		getTable().update(row);
	}

	@Override
	public int getWidth() {
		return 55;
	}

	@Override
	protected void configure(TextBoxBase textBox) {
		super.configure(textBox);
		textBox.addStyleName("discount");
	}

	@Override
	protected String getColumnName() {
		return messages.discPerc();
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
