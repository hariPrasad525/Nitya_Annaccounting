/**
 * 
 */
package com.nitya.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Activation;
import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.IMActivation;
import com.nitya.accounter.core.IMUser;
import com.nitya.accounter.core.MobileCookie;
import com.nitya.accounter.mail.UsersMailSendar;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.mobile.AccounterChatServer;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.RequirementType;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.EmailRequirement;
import com.nitya.accounter.mobile.requirements.PasswordRequirement;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.utils.HexUtil;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.utils.SecureUtils;
import com.nitya.accounter.utils.Security;
import com.nitya.accounter.web.client.ui.UIUtils;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AuthenticationCommand extends AbstractBaseCommand {

	private static final String EMAIL_ID = "emailId";
	private static final String ACTIVATION = "activation";
	private static final String PASSWORD = "password";
	private static final String NEW_PASSWORD = "newPassword";
	private static final String CONFIRM_PASSWORD = "confirmPassword";
	private Context context;

	private Client client;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new EmailRequirement(EMAIL_ID, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), false, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				int networkType = context.getNetworkType();
				if (networkType == AccounterChatServer.NETWORK_TYPE_MOBILE) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				if (isValidEmailId((String) value)) {
					String validateEmailId = AuthenticationCommand.this
							.validateEmailId();
					if (validateEmailId != null) {
						addFirstMessage(validateEmailId);
						super.setValue(null);
					}
				}
			}
		});

		list.add(new StringRequirement(ACTIVATION, getMessages().pleaseEnter(
				getMessages().activationCode()),
				getMessages().activationCode(), false, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (!client.isActive() || client.isRequirePasswordReset()) {
					Result run = super.run(context, makeResult, list, actions);
					return run;
				}
				return null;
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				Activation activation = getActivation((String) value);
				if (activation == null) {
					addFirstMessage("Wrong activation code");
					super.setValue(null);
				} else {
					Session currentSession = HibernateUtil.getCurrentSession();
					Transaction beginTransaction = currentSession
							.beginTransaction();

					client.setActive(true);
					currentSession.saveOrUpdate(client);
					beginTransaction.commit();
				}
			}
		});

		list.add(new PasswordRequirement(PASSWORD, getMessages().pleaseEnter(
				getMessages().password()), getMessages().password(), false,
				true) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (!client.isRequirePasswordReset()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			public boolean isDone() {
				return EU.getKey(context.getIOSession().getId()) != null;
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				String validatePassword = AuthenticationCommand.this
						.validatePassword();
				if (validatePassword != null) {
					addFirstMessage(validatePassword);
					super.setValue(null);
				}
			}
		});

		list.add(new PasswordRequirement(NEW_PASSWORD, getMessages()
				.pleaseEnter(getMessages().newPassword()), getMessages()
				.newPassword(), false, true) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (client.isRequirePasswordReset()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new PasswordRequirement(CONFIRM_PASSWORD, getMessages()
				.pleaseEnter(getMessages().confirmPassword()), getMessages()
				.confirmPassword(), false, true) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (client.isRequirePasswordReset()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				String validatePassword = AuthenticationCommand.this
						.isBothPasswordsSame();
				if (validatePassword != null) {
					addFirstMessage(validatePassword);
					super.setValue(null);
				}
			}
		});
	}

	protected void setClient() {
		this.client = getClient((String) get(EMAIL_ID).getValue());
	}

	private void sendForgotPassWordMail() {
		Activation activation = getActivationByEmailId(client.getEmailId());
		String token = null;
		if (activation == null) {
			token = createUserActivationCode(client.getEmailId());
		} else {
			token = activation.getToken();
		}
		sendForgetPasswordLinkToUser(token);
	}

	private void sendForgetPasswordLinkToUser(String activationCode) {

		Session session = HibernateUtil.getCurrentSession();
		client.setRequirePasswordReset(true);

		session.saveOrUpdate(client);
		StringBuffer link = new StringBuffer("https://");
		link.append(ServerConfiguration.getMainServerDomain());
		link.append("/main/activation");
		System.out.println("@@@ ACTIVATION CODE::" + activationCode);
		UsersMailSendar.sendResetPasswordLinkToUser(link.toString(),
				activationCode, client);
	}

	private Activation getActivationByEmailId(String email) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			Query query = session.getNamedQuery("get.activation.by.emailid");
			query.setParameter("emailId", email);
			Activation val = (Activation) query.uniqueResult();
			return val;
		} catch (Exception e) {
		}
		return null;
	}

	protected String isBothPasswordsSame() {
		String password = get(NEW_PASSWORD).getValue();
		String confirm = get(CONFIRM_PASSWORD).getValue();
		Session hibernateSession = HibernateUtil.getCurrentSession();
		Transaction transaction = hibernateSession.beginTransaction();
		try {
			if (password.isEmpty() || confirm.isEmpty()) {
				return "Please enter a valid passowrd";
			}
			// compare if not equal send error message
			// otherwise
			if (!password.equals(confirm)) {
				return "Passwords not matched";
			}
			String emailId = get(EMAIL_ID).getValue();
			// getClient record with activation emailId

			// update password and set isActive true
			client.setPassword(HexUtil.bytesToHex(Security.makeHash(emailId
					+ password.trim())));
			client.setPasswordRecoveryKey(EU.encryptPassword(password.trim()));
			client.setRequirePasswordReset(false);

			// and save Client,
			hibernateSession.saveOrUpdate(client);
			transaction.commit();
			get(PASSWORD).setValue(password);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	protected String validatePassword() {
		String string = get(PASSWORD).getValue();
		// Written for open id sign up process.If user sign up in open
		// id
		// and trying to login in mobile.Then user don't have
		// password.For this situation we are checking null
		// password.client.getPassword() == null

		if (isWrongPassword()) {
			return "Entered password was wrong.Please enter correct password";
		}
		createMobileCookie(context.getNetworkId(), client);
		try {
			byte[] d2 = EU.generateD2(string, client.getEmailId(), context
					.getIOSession().getId());
			context.getIOSession().setD2(d2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean isWrongPassword() {
		String string = get(PASSWORD).getValue();
		String userName = get(EMAIL_ID).getValue();
		boolean wrongPassword = true;
		if (client == null) {
			return true;
		}
		if (client.getPassword() != null) {
			String password = HexUtil.bytesToHex(Security.makeHash(userName
					.toLowerCase() + string));
			String passwordWithWord = HexUtil.bytesToHex(Security
					.makeHash(userName.toLowerCase()
							+ Client.PASSWORD_HASH_STRING + string));
			if (client.getPassword().equals(password)) {
				client.setPassword(passwordWithWord);
				client.setPasswordRecoveryKey(EU.encryptPassword(string));
				Session currentSession = HibernateUtil.getCurrentSession();
				Transaction beginTransaction = currentSession
						.beginTransaction();
				currentSession.saveOrUpdate(client);
				beginTransaction.commit();
			}
			if (!client.getPassword().equals(passwordWithWord)) {
				wrongPassword = true;
			} else {
				wrongPassword = false;
			}
		}
		return wrongPassword;
	}

	protected String validateEmailId() {
		String emailId = get(EMAIL_ID).getValue();
		client = getClient(emailId);
		if (client == null) {
			return "There is no account found with given Email Id.Please enter valid accounter email.";
		}
		return null;
	}

	@Override
	public Result run(Context context) {
		this.context = context;
		Result login = showLoginButton(context);
		if (login != null) {
			return login;
		}
		Result run = new Result();
		String emailId = get(EMAIL_ID).getValue();
		String message = null;
		Object selection = context.getSelection("resendactivation");
		if (selection != null) {
			if (selection.equals("resendActiv")) {
				String activationCode = getUserActivationCode(client);
				if (activationCode == null) {
					Session session = HibernateUtil.getCurrentSession();
					session.getNamedQuery("delete.activation.by.emailId")
							.setString("emailId", emailId).executeUpdate();
					activationCode = createUserActivationCode(client
							.getEmailId());
				}
				sendUserActivationMail(client, activationCode);
				message = "Activation code has been sent to " + emailId;
			} else if (selection.equals("forgotPassword")) {
				sendForgotPassWordMail();
				message = "Activation code has been sent to " + emailId;
			}
		}
		run = super.run(context);
		if (message != null) {
			run.add(message);
		}
		emailId = get(EMAIL_ID).getValue();
		run.setShowBack(false);

		if (emailId != null) {
			if (client != null) {
				Record record;
				ResultList list = new ResultList("resendactivation");
				if (client.isRequirePasswordReset() || !client.isActive()) {
					record = new Record("resendActiv");
					record.add("Re-Send Activation Code");
					list.add(record);
				}
				if (client.isActive()) {
					record = new Record("forgotPassword");
					record.add(getMessages().forgottenPassword());
					list.add(record);
				}

				run.add(list);
			}
		}
		if (client != null && client.isActive()
				&& EU.getKey(context.getIOSession().getId()) != null) {
			markDone();
			Result makeResult = new Result();
			makeResult.add("You Successfully Logged.");
			makeResult.setNextCommand("selectCompany");
			makeResult.setCookie(context.getNetworkId());
			context.getIOSession().setClient(client);
			context.getIOSession().setAuthentication(true);
			return makeResult;
		}
		return run;
	}

	private String createUserActivationCode(String emailId) {
		String token = SecureUtils.createNumberID(10).toLowerCase().trim();
		Activation activation = new Activation();
		activation.setEmailId(emailId);
		activation.setToken(token);
		Session currentSession = HibernateUtil.getCurrentSession();
		activation.setSignUpDate(new Date());
		Transaction beginTransaction = currentSession.beginTransaction();
		currentSession.save(activation);
		beginTransaction.commit();
		return token;
	}

	private String getUserActivationCode(Client client) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			Query query = session.getNamedQuery("get.activation.by.emailid");
			query.setParameter("emailId", client.getEmailId());
			Activation val = (Activation) query.uniqueResult();
			if (val == null) {
				return null;
			}
			return val.getToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void sendUserActivationMail(Client client, String token) {
		UsersMailSendar.sendActivationMail(token, client);
	}

	private Result showLoginButton(Context context) {
		if (context.getNetworkType() != AccounterChatServer.NETWORK_TYPE_MOBILE) {
			return null;
		}

		Object attribute = context.getAttribute("isFirst");
		if (attribute == null) {
			context.setAttribute("isFirst", "");
			if (context.getNetworkType() == AccounterChatServer.NETWORK_TYPE_MOBILE) {
				String string = context.getString();
				MobileCookie mobileCookie = getMobileCookie(string);
				if (mobileCookie != null) {
					return null;
				}
			}
		}
		String name = (String) context.getAttribute("select");
		String selection = context.getSelection("authentication");
		Result makeResult = context.makeResult();
		if (name == null && selection == null
				&& context.getLast(RequirementType.STRING) == null) {
			ResultList list = new ResultList("authentication");
			Record record = new Record("signin");
			record.add("Signin");
			list.add(record);
			makeResult.add(list);
			CommandList commandList = new CommandList();
			commandList.add("signup");
			makeResult.add(commandList);
			return makeResult;
		}
		context.setAttribute("select", "procede");
		return null;
	}

	private void createMobileCookie(String cookie, Client client) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction beginTransaction = session.beginTransaction();
		int count = session.getNamedQuery("deleteClientmobilecookies")
				.setParameter("client", client).executeUpdate();
		System.out.println(client.getEmailId()
				+ "  ***** Client mobile cookies count ***** " + count);
		MobileCookie mobileCookie = new MobileCookie();
		mobileCookie.setCookie(cookie);
		mobileCookie.setClient(client);
		session.save(mobileCookie);
		beginTransaction.commit();
	}

	private MobileCookie getMobileCookie(String string) {
		Session session = HibernateUtil.getCurrentSession();
		return (MobileCookie) session.get(MobileCookie.class, string);
	}

	private Activation getActivation(String activationCode) {
		Session session = HibernateUtil.getCurrentSession();
		Activation val = (Activation) session
				.getNamedQuery("get.activation.by.token")
				.setString("token", activationCode).uniqueResult();
		return val;
	}

	private IMUser createIMUser(int networkType, String networkId, Client client) {
		Session currentSession = HibernateUtil.getCurrentSession();
		IMUser imUser = new IMUser();
		imUser.setClient(client);
		imUser.setNetworkId(networkId);
		imUser.setNetworkType(networkType);
		Transaction beginTransaction = currentSession.beginTransaction();
		currentSession.save(imUser);
		List<IMActivation> imActivationByNetworkId = getImActivationByNetworkId(networkId);
		for (IMActivation activation : imActivationByNetworkId) {
			currentSession.delete(activation);
		}
		beginTransaction.commit();
		return imUser;
	}

	private void sendActivationMail(String networkId, String emailId) {
		String activationCode = SecureUtils.createNumberID(10).toLowerCase()
				.trim();
		System.out.println("NetWorkID: " + networkId);
		System.out.println("EmailId: " + emailId);
		System.out.println("Activation Code: " + activationCode);

		UsersMailSendar.sendMobileActivationMail(activationCode, emailId);

		Session currentSession = HibernateUtil.getCurrentSession();
		IMActivation activation = new IMActivation();
		activation.setEmailId(emailId);
		activation.setNetworkId(networkId);
		activation.setTocken(activationCode);
		Transaction beginTransaction = currentSession.beginTransaction();
		currentSession.save(activation);
		beginTransaction.commit();
	}

	private List<IMActivation> getImActivationByNetworkId(String networkId) {
		Session session = HibernateUtil.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<IMActivation> activationList = (List<IMActivation>) session
				.getNamedQuery("activation.by.networkId")
				.setString("networkId", networkId).list();
		return activationList;
	}

	private IMActivation getImActivationByTocken(String string) {
		Session session = HibernateUtil.getCurrentSession();
		IMActivation activation = (IMActivation) session
				.getNamedQuery("activation.by.tocken")
				.setString("tocken", string.toLowerCase().trim())
				.uniqueResult();
		return activation;
	}

	private IMUser getIMUser(String networkId, int networkType) {
		Session session = HibernateUtil.getCurrentSession();
		IMUser user = (IMUser) session.getNamedQuery("imuser.by.networkId")
				.setString("networkId", networkId)
				.setInteger("networkType", networkType).uniqueResult();
		return user;
	}

	public Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId.toLowerCase().trim());
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}

	protected boolean isValidEmailId(String emailId) {
		if (emailId != null && UIUtils.isValidEmail(emailId)) {
			return true;
		}
		return false;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Welcome to accounter login";
	}

	@Override
	protected String getDetailsMessage() {
		return "";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(EMAIL_ID).setValue(context.getLast(RequirementType.STRING));
	}

	@Override
	public String getSuccessMessage() {
		return "Your Successfully Logged.";
	}

	@Override
	public String getFinishCommandString() {
		return null;
	}
}
