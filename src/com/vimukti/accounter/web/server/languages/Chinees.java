package com.vimukti.accounter.web.server.languages;

import java.util.Locale;

import com.ibm.icu.text.RuleBasedNumberFormat;

public class Chinees implements Ilanguage {

	@Override
	public String getAmountAsString(double amount) {
		Locale l = new Locale("zh");
		RuleBasedNumberFormat rbf = new RuleBasedNumberFormat(l,
				RuleBasedNumberFormat.SPELLOUT);
		String[] ruleSetNames = rbf.getRuleSetNames();
		String format = rbf.format(amount, ruleSetNames[3]);
		return format;
	}

}
