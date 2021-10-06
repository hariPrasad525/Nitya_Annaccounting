package com.vimukti.accounter.main;

import com.vimukti.accounter.web.client.i18n.AccounterNumberFormat;

public class ServerNumberFormatThred {
	private static ThreadLocal<AccounterNumberFormat> numberFormat = new ThreadLocal<AccounterNumberFormat>();

	public static AccounterNumberFormat get() {
		return numberFormat.get();
	}

	public static void set(AccounterNumberFormat format) {
		numberFormat.set(format);
	}
}
