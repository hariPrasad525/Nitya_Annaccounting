package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public class InventoryTransactionUnitPriceColumn extends
		TextEditColumn<ClientInventoryAssemblyItem> {
	ICurrencyProvider currencyProvider;

	public InventoryTransactionUnitPriceColumn(
			ICurrencyProvider currencyProvider) {
		super(true);
		this.currencyProvider = currencyProvider;
	}

	@Override
	protected String getValue(ClientInventoryAssemblyItem row) {
		Double amount = getAmount(row);
		if (amount == null) {
			return "";
		}
		return DataUtils.getAmountAsStringInCurrency(amount, null);
	}

	protected Double getAmount(ClientInventoryAssemblyItem row) {
		Double amount = null;
		if (row.getInventoryItem() != 0) {
			amount = Accounter.getCompany().getItem(row.getInventoryItem())
					.getAverageCost();
		}
		return amount;
	}

	@Override
	public void setValue(ClientInventoryAssemblyItem row, String value) {
		try {
			Double amount = 0.0;
			if (value != null && !value.equals("")) {
				amount = DataUtils.getAmountStringAsDouble(JNSI
						.getCalcultedAmount(value));
			}
			setAmount(row, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setAmount(ClientInventoryAssemblyItem row, Double value) {
		getTable().update(row);
	}

	@Override
	protected void configure(TextBoxBase textBox) {
		super.configure(textBox);
		textBox.addStyleName("amount");
		textBox.addStyleName("unitPrice");
	}

	protected String getColumnNameWithCurrency(String name) {
		String currencyName = Accounter.getCompany().getPrimaryCurrency()
				.getFormalName();
		if (currencyProvider != null
				&& currencyProvider.getTransactionCurrency() != null) {
			currencyName = currencyProvider.getTransactionCurrency()
					.getFormalName();
		}
		return messages.nameWithCurrency(name, currencyName);
	}

	protected String getColumnNameWithBaseCurrency(String name) {
		String formalName = Accounter.getCompany().getPrimaryCurrency()
				.getFormalName();

		return messages.nameWithCurrency(name, formalName);
	}

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	protected String getColumnName() {
		return getColumnNameWithCurrency(messages.avarageCost());
	}

	@Override
	public String getValueAsString(ClientInventoryAssemblyItem row) {
		return " * " + getValue(row).toString() + "/unit";
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}
}
