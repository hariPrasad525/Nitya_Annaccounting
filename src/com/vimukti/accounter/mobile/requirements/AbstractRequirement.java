package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public abstract class AbstractRequirement<T> extends Requirement {
	public static final String PROCESS_ATR = "processAttr";
	public static final String INPUT_ATTR = "input";
	public static final String VALUES = "values";
	public static final String ACTIONS = "actions";
	public static final String FIRST_MESSAGE = "firstMessage";

	public static final int INPUT_TYPE_NONE = 0;
	public static final int INPUT_TYPE_STRING = 1;
	public static final int INPUT_TYPE_NUMBER = 2;
	public static final int INPUT_TYPE_AMOUNT = 3;
	public static final int INPUT_TYPE_PASSWORD = 4;
	public static final int INPUT_TYPE_EMAIL = 5;
	public static final int INPUT_TYPE_PHONE = 6;
	public static final int INPUT_TYPE_URL = 7;
	public static final int INPUT_TYPE_DATE = 8;

	private AccounterMessages messages;
	private ClientCompanyPreferences preferences;
	private Company company;
	private long companyId;
	private List<String> firstMessages = new ArrayList<String>();

	public AbstractRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		messages = Global.get().messages();
	}

	public void addFirstMessage(Context context, String string) {
		firstMessages.add(string);
	}

	public void addFirstMessage(String string) {
		firstMessages.add(string);
	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result process(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		preferences = context.getPreferences();
		companyId = context.getCompany() == null ? 0 : context.getCompany()
				.getId();
		setCompany(context.getCompany());

		Result run = run(context, makeResult, list, actions);
		for (String s : firstMessages) {
			((List<String>) context.getAttribute(FIRST_MESSAGE)).add(s);
		}
		firstMessages.clear();
		return run;
	}

	public abstract Result run(Context context, Result makeResult,
			ResultList list, ResultList actions);

	protected Result show(Context context, String string, String displayValue,
			Object value) {
		Result result = context.makeResult();
		result.add(string);
		if (value instanceof ClientFinanceDate) {
			displayValue = String
					.valueOf(((ClientFinanceDate) value).getDate());
		}
		InputType inputType = getInputType();
		inputType.setValue(displayValue == null ? "" : displayValue);
		result.add(inputType);
		return result;
	}

	protected AccounterMessages getMessages() {
		return messages;
	}

	protected long getCompanyId() {
		return companyId;
	}

	protected long getNumberFromString(String string) {
		if (string.isEmpty()) {
			return 0;
		}
		if (string.charAt(0) != '#') {
			return 0;
		}
		string = string.substring(1);
		if (string.isEmpty()) {
			return 0;
		}
		return Long.parseLong(string);
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public abstract InputType getInputType();
}
