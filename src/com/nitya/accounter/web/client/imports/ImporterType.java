package com.nitya.accounter.web.client.imports;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class ImporterType {
	public static final int CUSTOMER = 1;

	public static final int VENDOR = 2;

	public static final int INVOICE = 3;

	public static final int ITEM = 4;

	public static final int CUSTOMER_PREPAYMENT = 5;

	public static final int VENDOR_PREPAYMENT = 6;

	public static final int ENTERBILL = 7;

	public static final int PAYBILL = 8;

	public static final int ACCOUNT = 9;

	private static Map<Integer, String> supportedImports = new HashMap<Integer, String>();

	static AccounterMessages messages = Global.get().messages();

	private static int parseInt;

	static {
		supportedImports.put(CUSTOMER, Global.get().Customers());
		supportedImports.put(VENDOR, Global.get().Vendors());
		// supportedImports.put(INVOICE, Global.get().messages().invoice());
		supportedImports.put(ITEM, Global.get().messages().items());
		supportedImports.put(ACCOUNT, Global.get().messages().Accounts());
		// supportedImports.put(CUSTOMER_PREPAYMENT,
		// messages.payeePrePayment(Global.get().Customer()));
		// supportedImports.put(VENDOR_PREPAYMENT,
		// messages.payeePrePayment(Global.get().Vendor()));
		// supportedImports.put(ENTERBILL, messages.enterBills());
		// supportedImports.put(PAYBILL, messages.payBills());
	}

	public static Map<Integer, String> getAllSupportedImporters() {
		return supportedImports;
	}

	public static int getTypeByName(String value) {
		for (Entry<Integer, String> entry : supportedImports.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return 0;
	}

}
