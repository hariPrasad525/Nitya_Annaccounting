package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientCurrency;

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
