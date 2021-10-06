package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.utils.Security;

public class ForgotPasswordCommand extends AbstractCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), false, true) {
			@Override
			public void setValue(Object val) {
				String value = (String) val;
				if (value == null) {
					return;
				} else if (!isValidEmailId(value)) {
					setEnterString(getMessages().enterValidEmailForSignup());
					return;
				} else if (getClient(value) == null) {
					setEnterString(getMessages().notRegisteredWithThisEmailId());
					return;
				}
				setEnterString(getMessages().enterEmail());
				super.setValue(value);
			}
		});

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().emailSentToYou();
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		Client client = getContext().getIOSession().getClient();
		String emailId = get(EMAIL).getValue();
		client.setEmailId(emailId.toLowerCase());
		String password = "***REMOVED***";
		password = SecureUtils.createNumberID(10);
		sendPasswordMail(password, emailId);
		String passwordWithHash = HexUtil.bytesToHex(Security.makeHash(emailId
				+ Client.PASSWORD_HASH_STRING + password));
		client.setPassword(passwordWithHash);
		client.setPasswordRecoveryKey(EU.encryptPassword(password));

		Transaction beginTransaction = context.getHibernateSession()
				.beginTransaction();
		context.getHibernateSession().saveOrUpdate(client);
		beginTransaction.commit();
		markDone();
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		return null;
	}

	private void sendPasswordMail(String password, String emailId) {
		System.out.println("password " + password);
		Client client = getClient(emailId);
		UsersMailSendar.sendActivationMail(password, client);
	}

}
