package com.nitya.accounter.mobile.commands;

import java.util.List;

import com.nitya.accounter.mail.UsersMailSendar;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.web.client.core.ClientEmailAccount;

public class TestEmailAccountCommand extends AbstractCommand {

	private static final String EMAIL_ADDRESS = "emailAddress";
	private ClientEmailAccount emailAccount;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		String[] arr = string.split(",");
		emailAccount = new ClientEmailAccount();
		emailAccount.setEmailId(arr[0]);
		emailAccount.setPassword(arr[1]);
		emailAccount.setSmtpMailServer(arr[2]);
		emailAccount.setPortNumber(Integer.valueOf(arr[3]));
		if (arr[4].equals("true")) {
			emailAccount.setSSL(true);
		} else {
			emailAccount.setSSL(true);
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().testEmailAccount();
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().testEmailAccount();
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages()
				.ATestEmailIsSentToGivenEmailIdKindlyCheckYourEmailAndProceedIfYouGot();
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringRequirement(EMAIL_ADDRESS, getMessages()
				.pleaseEnter(getMessages().emailAddress()), getMessages()
				.emailAddress(), false, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String recipient = get(EMAIL_ADDRESS).getValue();
		try {
			UsersMailSendar.sendTestMail(emailAccount, recipient);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
