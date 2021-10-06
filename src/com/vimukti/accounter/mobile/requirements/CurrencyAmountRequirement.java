package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;

public abstract class CurrencyAmountRequirement extends AmountRequirement {

	public CurrencyAmountRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional2,
			boolean isAllowFromContext2) {
		super(requirementName, displayString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	protected void createRecord(ResultList list) {
		Double t = getValue();
		Record nameRecord = new Record(getName());
		nameRecord.add(getRecordName(), getDisplayValueWithCurrency(t));
		list.add(nameRecord);
	}

	private String getDisplayValueWithCurrency(Double value) {
		String symbol = null;
		if (getPreferences().isEnableMultiCurrency()) {
			Currency currency = getCurrency();
			if (currency != null) {
				symbol = currency.getSymbol();
			}
		}
		if (symbol == null) {
			symbol = getPreferences().getPrimaryCurrency().getSymbol();
		}
		return Global.get().toCurrencyFormat(value, symbol);
	}

	@Override
	public String getRecordName() {
		String formalName = null;
		if (getPreferences().isEnableMultiCurrency()) {
			Currency currency = getCurrency();
			if (currency != null) {
				formalName = currency.getFormalName();
			}
		}
		if (formalName == null) {
			formalName = getPreferences().getPrimaryCurrency().getFormalName();
		}
		return super.getRecordName() + "(" + formalName + ")";
	}

	protected abstract Currency getCurrency();
}
