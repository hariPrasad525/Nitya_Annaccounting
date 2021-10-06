package com.nitya.accounter.main;

import com.nitya.accounter.web.client.i18n.AccounterNumberFormat;

public class ServerNumberFormatThred {
	private static ThreadLocal<AccounterNumberFormat> numberFormat = new ThreadLocal<AccounterNumberFormat>();

	public static AccounterNumberFormat get() {
		return numberFormat.get();
	}

	public static void set(AccounterNumberFormat format) {
		numberFormat.set(format);
	}
}
