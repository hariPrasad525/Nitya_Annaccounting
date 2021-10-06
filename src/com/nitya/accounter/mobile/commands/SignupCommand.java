package com.nitya.accounter.mobile.commands;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Transaction;

import com.nitya.accounter.core.Activation;
import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.ClientSubscription;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.IMUser;
import com.nitya.accounter.core.Subscription;
import com.nitya.accounter.core.User;
import com.nitya.accounter.mail.UsersMailSendar;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.mobile.AccounterChatServer;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.InputType;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.RequirementType;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.EmailRequirement;
import com.nitya.accounter.mobile.requirements.NameRequirement;
import com.nitya.accounter.mobile.requirements.PhoneRequirement;
import com.nitya.accounter.mobile.requirements.TermsAndCunditionsRequirement;
import com.nitya.accounter.utils.HexUtil;
import com.nitya.accounter.utils.SecureUtils;
import com.nitya.accounter.utils.Security;
import com.nitya.accounter.web.client.Global;

public class SignupCommand extends AbstractCommand {
	private static final String FIRST_NAME = "firstname";
	private static final String LAST_NAME = "lastname";
	private static final String SUBSCRIBED_NEWSLETTER = "subscribed";
	private static final String EMAIL = "email";
	private static final String PHONE = "phone";
	private static final String COUNTRY = "country";
	private static final String PASSOWRD = "password";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// Written for open id sign up process.If user sign up in open
		// id
		// and trying to login in mobile.Then user don't have
		// password.For this situation we are checking null
		list.add(new NameRequirement(FIRST_NAME, getMessages().pleaseEnter(
				getMessages().firstName()), getMessages().firstName(), false,
				true) {
			@Override
			public void setValue(Object value) {
				String v = (String) value;
				if (v == null || v.trim().length() <= 0 || v.isEmpty()) {
					setEnterString(getMessages().pleaseEnter(
							getMessages().firstName()));
					return;
				}
				v.replaceFirst(String.valueOf(v.charAt(0)),
						String.valueOf(v.charAt(0)).toUpperCase());
				if (v.trim().length() > 0) {
					super.setValue(v);
				}
			}
		});

		list.add(new NameRequirement(LAST_NAME, getMessages().pleaseEnter(
				getMessages().lastName()), getMessages().lastName(), false,
				true) {
			@Override
			public void setValue(Object value) {
				String v = (String) value;
				if (v == null || v.trim().length() <= 0 || v.isEmpty()) {
					setEnterString(getMessages().pleaseEnter(
							getMessages().lastName()));
					return;
				}
				v.replaceFirst(String.valueOf(v.charAt(0)),
						String.valueOf(v.charAt(0)).toUpperCase());
				if (v.trim().length() > 0) {
					super.setValue(v);
				}
			}
		});

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), false, true) {
			@Override
			public void setValue(Object val) {
				String value = (String) val;
				if (value == null) {
					return;
				} else if (!isValidEmailId(value)) {
					setEnterString("Enter a valid email address.");
					return;
				} else if (getClient(value) != null) {
					setEnterString(getMessages().enteredEmailAlreadyRegistred());
					return;
				}
				setEnterString(getMessages().pleaseEnter(getMessages().email()));
				super.setValue(value.toLowerCase().trim());
			}
		});

		list.add(new NameRequirement(PASSOWRD, getMessages().pleaseEnter(
				getMessages().password()), getMessages().password(), false,
				true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getNetworkType() == AccounterChatServer.NETWORK_TYPE_MOBILE) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			public InputType getInputType() {
				return new InputType(INPUT_TYPE_PASSWORD);
			}
		});

		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				false, true));

		list.add(new CountryRequirement(COUNTRY, getMessages().pleaseEnter(
				getMessages().country()), getMessages().country(), false, true,
				null));

		list.add(new BooleanRequirement(SUBSCRIBED_NEWSLETTER, true) {

			@Override
			protected String getFalseString() {
				return getMessages().notsubscribed();
			}

			@Override
			protected String getTrueString() {
				return getMessages().subscribed();
			}
		});

		list.add(new TermsAndCunditionsRequirement());
	}

	private void sendPasswordMail(String token, String emailId) {
		System.out.println("Password : " + token);
		Client client = getClient(emailId);
		UsersMailSendar.sendActivationMail(token, client);
	}

	protected String createActivation(String emailID, Context context) {
		String token = SecureUtils.createNumberID(10).toLowerCase().trim();
		Activation activation = new Activation();
		activation.setEmailId(emailID);
		activation.setToken(token);
		activation.setSignUpDate(new Date());
		saveEntry(activation, context);
		return token;
	}

	private void saveEntry(Object object, Context context) {
		Transaction beginTransaction = context.getHibernateSession()
				.beginTransaction();
		context.getHibernateSession().save(object);
		beginTransaction.commit();
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().account());
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Result result = new Result();
		Boolean boolean1 = get("TermsAndConditions").getValue();
		if (!boolean1) {
			result.add(getMessages().acceptThetermsAndCondition());
			return result;
		}
		Client client = new Client();
		client.setActive(false);
		client.setCreatedDate(new FinanceDate());
		client.setUsers(new HashSet<User>());
		String emailId = get(EMAIL).getValue();
		client.setEmailId(emailId.toLowerCase());

		String firstName = get(FIRST_NAME).getValue();
		client.setFirstName(firstName);

		String lastName = get(LAST_NAME).getValue();
		client.setLastName(lastName);

		client.setFullName(Global.get().messages()
				.fullName(firstName, lastName));

		String password = "***REMOVED***";
		if (context.getNetworkType() == AccounterChatServer.NETWORK_TYPE_MOBILE) {
			password = get(PASSOWRD).getValue();
			context.setLast(RequirementType.STRING, emailId);
		} else {
			password = SecureUtils.createNumberID(10);
		}
		String passwordWithHash = HexUtil.bytesToHex(Security.makeHash(emailId
				+ Client.PASSWORD_HASH_STRING + password));
		client.setPassword(passwordWithHash);
		client.setPasswordRecoveryKey(EU.encryptPassword(password));

		String phoneNumber = get(PHONE).getValue();
		client.setPhoneNo(phoneNumber);

		String country = get(COUNTRY).getValue();
		client.setCountry(country);

		Boolean isSubscribedToNewsLetter = get(SUBSCRIBED_NEWSLETTER)
				.getValue();
		client.setSubscribedToNewsLetters(isSubscribedToNewsLetter);
		ClientSubscription clientSubscription = new ClientSubscription();
		clientSubscription.setCreatedDate(new Date());
		clientSubscription.setSubscription(Subscription
				.getInstance(Subscription.PREMIUM_USER));
		clientSubscription.setDurationType(ClientSubscription.UNLIMITED_USERS);
		clientSubscription.setLastModified(new Date());
		saveEntry(clientSubscription, context);
		client.setClientSubscription(clientSubscription);
		saveEntry(client, context);

		if (context.getNetworkType() != AccounterChatServer.NETWORK_TYPE_MOBILE) {
			sendPasswordMail(password, emailId);
		}
		UsersMailSendar.sendActivationMail(createActivation(emailId, context),
				client);

		IMUser imUser = new IMUser();
		imUser.setClient(client);
		imUser.setNetworkId(context.getNetworkId());
		imUser.setNetworkType(context.getNetworkType());
		saveEntry(imUser, context);

		try {
			byte[] d2 = EU.generateD2(password, client.getEmailId(), context
					.getIOSession().getId());
			context.getIOSession().setD2(d2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createdSucessFullysentAnActivationCodetoYourMail();
	}

	@Override
	protected String getWelcomeMessage() {
		return "Sign Up Command is Activated";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		context.setString(null);
		if (string == null || string.isEmpty()) {
			return null;
		}
		if (string.contains("@")) {
			get(EMAIL).setValue(string);
		} else {
			get(FIRST_NAME).setValue(string);
		}
		return null;
	}
}
