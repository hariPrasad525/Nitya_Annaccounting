package com.vimukti.accounter.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vimukti.accounter.web.client.AbstractGlobal;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.client.i18n.AccounterNumberFormat;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.i18n.ServerSideMessages;

public class ServerGlobal extends AbstractGlobal {

	AccounterMessages messages = ServerSideMessages
			.get(AccounterMessages.class);
	AccounterMessages2 messages1 = ServerSideMessages
			.get(AccounterMessages2.class);

	public ServerGlobal() throws IOException {
	}

	@Override
	public ClientCompanyPreferences preferences() {
		return CompanyPreferenceThreadLocal.get();
	}

	@Override
	public AccounterMessages messages() {
		return messages;
	}

	@Override
	public AccounterNumberFormat getFormater() {
		AccounterNumberFormat accounterNumberFormat = ServerNumberFormatThred
				.get();
		String decimalCharacter = preferences().getDecimalCharacter();
		char decimalChar = 0;
		if (decimalCharacter != null && !decimalCharacter.isEmpty()) {
			decimalChar = decimalCharacter.charAt(0);
		}
		String digitGroupCharacter = preferences().getDigitGroupCharacter();
		char digitGroupChar = 0;
		if (digitGroupCharacter != null && !digitGroupCharacter.isEmpty()) {
			digitGroupChar = digitGroupCharacter.charAt(0);
		}
		if (accounterNumberFormat == null) {
			accounterNumberFormat = new AccounterNumberFormat(preferences()
					.getCurrencyFormat(), preferences().getDecimalNumber(),
					true, decimalChar, digitGroupChar);
			ServerNumberFormatThred.set(accounterNumberFormat);
		}
		return accounterNumberFormat;
	}

	@Override
	public DayAndMonthUtil getDayAndMonthUtil() {
		return LocalInfoCache.get(ServerLocal.get());
	}

	@Override
	public ClientFinanceDate stringAsFinanceDate(String date, String format) {
		if (date == null) {
			return null;
		}
		date = date.trim();
		if (date.isEmpty()) {
			return null;
		}
		SimpleDateFormat simpleDateformat = new SimpleDateFormat(format);
		try {
			Date parse = simpleDateformat.parse(date);
			return new ClientFinanceDate(parse);
		} catch (Exception e) {
			// TODO
		}
		return null;

	}

	@Override
	public AccounterMessages2 messages2() {
		return messages1;
	}
}
