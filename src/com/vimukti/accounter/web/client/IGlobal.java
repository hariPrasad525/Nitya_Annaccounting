package com.vimukti.accounter.web.client;

import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.client.i18n.AccounterNumberFormat;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public interface IGlobal {

	public AccounterMessages messages();

	public AccounterMessages2 messages2();

	public ClientCompanyPreferences preferences();

	public String Customer();

	public String customer();

	public String Vendor();

	public String vendor();

	public String Location();

	public String toCurrencyFormat(double amount, String currencyCode);

	public String customers();

	public String vendors();

	public String Customers();

	public String Vendors();

	public DayAndMonthUtil getDayAndMonthUtil();

	public AccounterNumberFormat getFormater();

	public ClientFinanceDate stringAsFinanceDate(String date, String format);

	public String consultants();

}
