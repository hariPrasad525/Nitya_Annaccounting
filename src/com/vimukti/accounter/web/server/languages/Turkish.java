package com.vimukti.accounter.web.server.languages;

import java.util.Locale;

import com.ibm.icu.text.RuleBasedNumberFormat;

public class Turkish implements Ilanguage {

	@Override
	public String getAmountAsString(double amount) {
		Locale l = new Locale("tr");
		RuleBasedNumberFormat rbf = new RuleBasedNumberFormat(l,
				RuleBasedNumberFormat.SPELLOUT);
		String[] ruleSetNames = rbf.getRuleSetNames();
		String format = rbf.format(amount, ruleSetNames[2]);
		return format;
	}

}
