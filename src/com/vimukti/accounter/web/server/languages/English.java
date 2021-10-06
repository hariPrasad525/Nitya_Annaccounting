package com.vimukti.accounter.web.server.languages;

import java.util.Locale;

import com.ibm.icu.text.RuleBasedNumberFormat;

public class English implements Ilanguage {

	@Override
	public String getAmountAsString(double amount) {
		RuleBasedNumberFormat rbf = new RuleBasedNumberFormat(Locale.ENGLISH,
				RuleBasedNumberFormat.SPELLOUT);
		String[] ruleSetNames = rbf.getRuleSetNames();
		String format = rbf.format(amount, ruleSetNames[5]);
		return format;
	}

}
