package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.EmailAccount;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientEmailAccount;

public class NewEmailAccountCommand extends AbstractCommand {

	private static final String EMAIL_ID = "emailId";
	private static final String PASSWORD = "password";
	private static final String SMPT_MAIL_SERVER = "smptmailserver";
	private static final String PORT_NUMBER = "portnumber";
	private static final String IS_SSL = "isssl";

	private ClientEmailAccount emailAccount;

	@Override
	protected String getWelcomeMessage() {
		return emailAccount.getID() == 0 ? getMessages().creating(
				getMessages().emailAccount()) : getMessages().updating(
				getMessages().emailAccount());
	}

	@Override
	protected String getDetailsMessage() {
		return emailAccount.getID() == 0 ? getMessages().readyToCreate(
				getMessages().emailAccount()) : getMessages().readyToUpdate(
				getMessages().emailAccount());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_SSL).setDefaultValue(true);
	}

	@Override
	public String getSuccessMessage() {
		return emailAccount.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().emailAccount()) : getMessages()
				.updateSuccessfully(getMessages().emailAccount());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new StringRequirement(EMAIL_ID, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), false, true));

		list.add(new StringRequirement(PASSWORD, getMessages().pleaseEnter(
				getMessages().password()), getMessages().password(), false,
				true));

		list.add(new StringRequirement(SMPT_MAIL_SERVER, getMessages()
				.pleaseEnter(getMessages().smtpMailServer()), getMessages()
				.smtpMailServer(), false, true));

		list.add(new NumberRequirement(PORT_NUMBER, getMessages().pleaseEnter(
				getMessages().portNumber()), getMessages().portNumber(), false,
				true));

		list.add(new BooleanRequirement(IS_SSL, true) {

			@Override
			protected String getTrueString() {
				return getMessages().isSslTrue();
			}

			@Override
			protected String getFalseString() {
				return getMessages().isSslFalse();
			}
		});

		list.add(new CommandsRequirement("TestEmailAccountCommand") {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				return super.run(context, makeResult, list, actions);
			}

			@Override
			public String onSelection(String value) {
				String email = get(EMAIL_ID).getValue();
				String password = get(PASSWORD).getValue();
				String smtpserver = get(SMPT_MAIL_SERVER).getValue();
				String port = String.valueOf(get(PORT_NUMBER).getValue());
				boolean value2 = (Boolean) get(IS_SSL).getValue();
				String bool = null;
				if (value2) {
					bool = "true";
				} else {
					bool = "false";
				}
				StringBuffer buffer = new StringBuffer();
				buffer.append(email);
				buffer.append(",");
				buffer.append(password);
				buffer.append(",");
				buffer.append(smtpserver);
				buffer.append(",");
				buffer.append(port);
				buffer.append(",");
				buffer.append(bool);

				return "testEmailAccount " + buffer.toString();
			}

			@Override
			protected boolean canAddToResult() {
				return false;
			}

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().testEmailAccount());
				return list;
			}
		});

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().emailAccount()));
				return "emailAccountList";
			}
			EmailAccount account = CommandUtils.getEmailAccount(
					context.getCompany(), string);
			if (account == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().emailAccount()));
				return "emailAccountList " + string;
			}
			emailAccount = (ClientEmailAccount) CommandUtils
					.getClientObjectById(account.getID(),
							AccounterCoreType.EMAIL_ACCOUNT, context
									.getCompany().getId());
			get(EMAIL_ID).setValue(emailAccount.getEmailId());
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(EMAIL_ID).setValue(string);
			}
			emailAccount = new ClientEmailAccount();
		}
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		emailAccount.setEmailId((String) get(EMAIL_ID).getValue());
		emailAccount.setPassword((String) get(PASSWORD).getValue());
		emailAccount.setSmtpMailServer((String) get(SMPT_MAIL_SERVER)
				.getValue());
		String value = get(PORT_NUMBER).getValue();
		emailAccount.setPortNumber(Integer.valueOf(value));
		emailAccount.setSSL((Boolean) get(IS_SSL).getValue());
		create(emailAccount, context);
		markDone();
		return null;
	}

}
