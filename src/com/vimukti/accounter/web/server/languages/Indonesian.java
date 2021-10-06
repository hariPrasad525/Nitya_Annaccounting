package com.vimukti.accounter.web.server.languages;

import java.util.Locale;

import com.ibm.icu.text.RuleBasedNumberFormat;

public class Indonesian implements Ilanguage {

	@Override
	public String getAmountAsString(double amount) {
		Locale l = new Locale("id");
		RuleBasedNumberFormat rbf = new RuleBasedNumberFormat(l,
				RuleBasedNumberFormat.SPELLOUT);
		String[] ruleSetNames = rbf.getRuleSetNames();
		String format = rbf.format(amount, ruleSetNames[5]);
		return format;
	}

}
