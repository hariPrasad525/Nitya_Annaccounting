package com.nitya.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBoxBase;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.JNSI;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;

public abstract class AmountColumn<T> extends TextEditColumn<T> {

	ICurrencyProvider currencyProvider;
	private boolean isEnable = true;

	public AmountColumn(ICurrencyProvider currencyProvider,
			boolean updateFromGUI) {
		super(updateFromGUI);
		this.currencyProvider = currencyProvider;
	}

	@Override
	protected String getValue(T row) {
		Double amount = getAmount(row);
		if (amount == null) {
			return "";
		}
		return DataUtils.getAmountAsStringInCurrency(amount, null);
	}

	protected abstract Double getAmount(T row);

	@Override
	public void setValue(T row, String value) {
		try {
			Double amount = null;
			if (value != null && !value.equals("")) {
				amount = DataUtils.getAmountStringAsDouble(JNSI
						.getCalcultedAmount(value));
			}
			setAmount(row, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void setAmount(T row, Double value);

	@Override
	protected void configure(TextBoxBase textBox) {
		super.configure(textBox);
		textBox.addStyleName("amount");
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

	public void setEnable(T row, boolean b) {
		this.isEnable = b;
		if (!isEnable) {
			setAmount(row, 0D);
		}
	}

	@Override
	protected boolean isEnable() {
		return isEnable;
	}
}
