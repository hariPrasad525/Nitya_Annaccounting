package com.nitya.accounter.web.server.languages;

import java.util.Locale;

import com.ibm.icu.text.RuleBasedNumberFormat;

public class Arabic implements Ilanguage {

	@Override
	public String getAmountAsString(double amount) {
		Locale l = new Locale("ar");
		RuleBasedNumberFormat rbf = new RuleBasedNumberFormat(l,
				RuleBasedNumberFormat.SPELLOUT);
		String[] ruleSetNames = rbf.getRuleSetNames();
		String format = rbf.format(amount, ruleSetNames[4]);
		return format;
	}

}
