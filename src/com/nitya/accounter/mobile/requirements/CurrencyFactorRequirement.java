package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.core.Currency;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.web.client.core.ClientCurrency;

public abstract class CurrencyFactorRequirement extends
		CurrencyAmountRequirement {

	public CurrencyFactorRequirement(String requirementName,
			String displayString, String recordName) {
		super(requirementName, displayString, recordName, false, true);
		setDefaultValue(1.0d);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		Currency currency = getCurrency();
		ClientCurrency primaryCurrency = getPreferences().getPrimaryCurrency();
		if (getPreferences().isEnableMultiCurrency()
				&& !currency.getFormalName().equalsIgnoreCase(
						primaryCurrency.getFormalName())) {
			return super.run(context, makeResult, list, actions);
		}
		return null;
	}

	@Override
	protected void createRecord(ResultList list) {
		Double t = getValue();
		Record nameRecord = new Record(getName());

		String currencyName = getCurrency().getFormalName();
		ClientCurrency primaryCurrency = getPreferences().getPrimaryCurrency();
		String val = "1" + currencyName + "=" + t + "  "
				+ primaryCurrency.getFormalName();
		nameRecord.add(getRecordName(), val);
		list.add(nameRecord);
	}

	@Override
	protected abstract Currency getCurrency();

}
