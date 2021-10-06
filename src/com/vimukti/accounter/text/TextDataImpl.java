package com.vimukti.accounter.text;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.text.commands.utils.CommandUtils;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class TextDataImpl implements ITextData {

	private String type;
	private String[] data;

	// SET DEFAULT POSITION IS 1(AS COMMAND TYPE WILL BE AT FIRST)
	private int cursor = 1;
	private String actual;

	TextDataImpl(String type, String actual, String[] data) {
		this.type = type;
		this.actual = actual;
		this.data = data;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void reset() {
		// Make Current Index to Zero(0)
		cursor = 1;
	}

	private void increment() {
		cursor++;
	}

	private void decrement() {
		cursor--;
	}

	private String next() {
		if (!hasNext()) {
			return null;
		}

		String next = data[cursor];
		increment();
		return next;
	}

	@Override
	public boolean hasNext() {
		// If Current Index is less than data length
		return data.length != cursor;
	}

	@Override
	public String nextString(String defVal) {
		String next = next();
		if (next == null) {
			return defVal;
		}
		return next;
	}

	@Override
	public Long nextNumber(Long defVal) {
		String next = next();
		if (next == null) {
			return defVal;
		}
		return Long.valueOf(next);
	}

	@Override
	public FinanceDate nextDate(FinanceDate defVal) {
		String next = next();
		if (next == null) {
			return defVal;
		}

		return CommandUtils.getFinaceDate(next, defVal);
	}

	@Override
	public boolean isDate() {
		String next = next();
		decrement();
		if (next == null || next.isEmpty()) {
			return true;
		}
		FinanceDate nextDate = nextDate(null);
		decrement();
		return nextDate != null;
	}

	@Override
	public double nextDouble(double defVal) {
		Double next = nextDouble();
		if (next == null) {
			return defVal;
		}
		return next;
	}

	@Override
	public Double nextDouble() {
		String next = next();
		if (next == null || next.isEmpty()) {
			return null;
		}
		return getReformatedAmount(next);
	}

	private Double getReformatedAmount(String next) {

		next = next.trim();

		String amountCurrencySymbol = CoreUtils.getAmountCurrencySymbol(next);

		String amountCurrencyFormalName = CoreUtils
				.getAmountCurrencyFormalName(next);

		if (amountCurrencySymbol != null && !amountCurrencySymbol.isEmpty()) {
			next = next.replaceAll("\\" + amountCurrencySymbol, "");
		} else if (amountCurrencyFormalName != null
				&& !amountCurrencyFormalName.isEmpty()) {
			next = next.replaceAll(amountCurrencyFormalName, "");
		}
		next = next.replaceAll(" ", "");
		next = next.replaceAll(",", "");
		return getDoubleValue(next);
	}

	@Override
	public boolean isDouble() {
		String next = next();
		decrement();
		if (next == null || next.isEmpty()) {
			return true;
		}

		Double nextDouble = nextDouble();
		decrement();
		return nextDouble != null;
	}

	@Override
	public Address nextAddress(Address defVal) {
		String next = next();
		if (next == null) {
			return defVal;
		}
		// Parse Address Here
		Address address = new Address();
		address.setAddress1(next);
		return address;
	}

	@Override
	public Quantity nextQuantity(Quantity defVal) {
		String next = next();
		if (next == null) {
			return defVal;
		}
		String stringValue = next.split(" ")[0];
		Double value = getDoubleValue(stringValue);
		if (value == null) {
			return defVal;
		}

		// New Quantity
		Quantity quantity = new Quantity();
		quantity.setValue(value);

		// Set Default Units
		Company company = AccounterThreadLocal.getCompany();
		Unit defaultUnit = company.getDefaultMeasurement().getDefaultUnit();
		quantity.setUnit(defaultUnit);

		return quantity;
	}

	private Double getDoubleValue(String value) {
		try {
			return Double.parseDouble(value);
		} catch (RuntimeException e) {
			return null;
		}

	}

	@Override
	public boolean isQuantity() {
		String next = next();
		decrement();
		if (next == null) {
			return true;
		}
		Quantity nextQuantity = nextQuantity(null);
		decrement();
		return nextQuantity != null;
	}

	@Override
	public String toString() {
		return actual;
	}

	@Override
	public void forward(int position) {
		cursor += position;
	}

}
