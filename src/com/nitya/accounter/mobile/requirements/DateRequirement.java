package com.nitya.accounter.mobile.requirements;

import java.text.SimpleDateFormat;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.InputType;
import com.nitya.accounter.mobile.MobileException;
import com.nitya.accounter.web.client.core.ClientFinanceDate;

public class DateRequirement extends SingleRequirement<ClientFinanceDate> {

	public DateRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional2,
				isAllowFromContext2);
		setDefaultValue(new ClientFinanceDate());
	}

	@Override
	protected String getDisplayValue(ClientFinanceDate value) {
		SimpleDateFormat format = new SimpleDateFormat(getPreferences()
				.getDateFormat());
		return format.format(value.getDateAsObject());
	}

	@Override
	protected ClientFinanceDate getInputFromContext(Context context)
			throws MobileException {
		String string = context.getString();
		try {
			return new ClientFinanceDate(Long.parseLong(string));
		} catch (Exception e) {
			try {
				return new ClientFinanceDate(string);
			} catch (Exception e1) {
				throw new MobileException(getMessages().wrongFormat(
						getMessages().date()));
			}
		}
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_DATE);
	}
}
