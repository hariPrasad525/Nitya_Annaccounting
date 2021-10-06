package com.vimukti.accounter.text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.EMailMessage;
import com.vimukti.accounter.mail.EMailSenderAccount;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.text.commands.ITextCommand;
import com.vimukti.accounter.text.commands.transaction.CompanyCommand;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class TextRequestProcessor {

	private Logger logger = Logger.getLogger(TextRequestProcessor.class);

	public static final String EMAIL_DOMAIL = "email.annaccounting.com";

	public static final String SIGNUP_EMAIL = "signup@email.annaccounting.com";

	private static TextRequestProcessor processor;

	public static TextRequestProcessor getInstance() {
		if (processor == null) {
			processor = new TextRequestProcessor();
		}
		return processor;
	}

	public void process(Mail request) {
		try {
			logger.info("Got request " + request.shortForm());
			executeRequest(request);
		} catch (Exception e) {
			logger.error("Error while processing request", e);
			sendResponse(request, TextReponses.internalErrorOccured());
		}

	}

	@SuppressWarnings("rawtypes")
	private void executeRequest(Mail request) throws IOException,
			AccounterException {
		logger.info("Processing request");
		String to = request.getTo();
		// Check it sign up request
		if (isSignup(to)) {
			signup(request);
		}

		Session session = HibernateUtil.getCurrentSession();

		// Check Client Existance
		Criteria criteria = session.createCriteria(Client.class);
		criteria.add(Restrictions.eq("emailId", request.getFrom()));
		Client client = (Client) criteria.uniqueResult();
		if (client == null) {
			logger.info("Client not yet signup" + request.getFrom());
			sendResponse(request, TextReponses.notYetSignedUp());
			return;
		}

		// Text body
		String textBody = request.getTextBody();

		logger.info("Parsing email body");
		ArrayList<ITextData> datas = TextCommandParser.parse(textBody);

		if (datas.isEmpty()) {
			return;
		}

		if (!hasCompanyCommand(datas)) {

			if (isSignup(to)) {
				return;
			}

			logger.info("Getting companies for user");
			Query query = session.getNamedQuery("get.users.by.unique.email")
					.setParameter("email", to);
			User user = (User) query.uniqueResult();
			if (user == null) {
				if (client.getUsers().isEmpty()) {
					logger.info("Don't have comapnies yet for this user");
					// No companies yet, Need to create a new company before
					// doing this operation
					sendResponse(request, TextReponses.donnotHaveCompaniesYet());
					return;
				}
			}

			AccounterThreadLocal.set(user);

			// Set Company Preferences to thread local
			ClientCompanyPreferences preferences = new FinanceTool()
					.getCompanyManager().getClientCompanyPreferences(
							user.getCompany());
			CompanyPreferenceThreadLocal.set(preferences);

			// Server Global
			Global.set(new ServerGlobal());
		}

		CommandsQueue queue = new CommandsQueue(datas);

		logger.info("Processing command");
		ArrayList<CommandResponseImpl> responses = CommandProcessor
				.getInstance().processCommands(queue, client);

		logger.info("Completed processing commands!");
		sendResponse(request, responses);
	}

	private boolean isSignup(String to) {
		return SIGNUP_EMAIL.equals(to);
	}

	private boolean hasCompanyCommand(ArrayList<ITextData> datas) {
		if (datas.isEmpty()) {
			return false;
		}
		Class<? extends ITextCommand> command = CommandsFactory
				.getCommand(datas.get(0).getType());
		return command == CompanyCommand.class;
	}

	private void sendResponse(Mail request,
			ArrayList<CommandResponseImpl> responses) {
		logger.info("Making response to send email");
		StringBuffer buff = new StringBuffer();
		ArrayList<File> files = new ArrayList<File>();
		for (CommandResponseImpl response : responses) {

			// Add input commands first
			ArrayList<ITextData> cData = response.getData();
			for (ITextData data : cData) {
				buff.append(data.toString());
				buff.append("\n");
			}

			// Then It's response
			ArrayList<String> errors = response.getErrors();

			// Add Errors first
			buff.append("\t");
			buff.append("Errors : ");
			if (errors.isEmpty()) {
				buff.append("No Errors");
			}
			for (String error : errors) {
				buff.append("\n\t\t");
				buff.append(error);
			}

			ArrayList<String> messages = response.getMessages();
			// Add then messages
			buff.append("\n\t");
			buff.append("Messages : ");
			if (messages.isEmpty()) {
				buff.append("No Messages");
			}
			for (String msg : messages) {
				buff.append("\n\t\t");
				buff.append(msg);
			}

			// Finally files
			for (String fileName : response.getFiles()) {
				files.add(new File(fileName));
			}

			// Finaly new line
			buff.append("\n\n");
		}
		sendResponse(request, buff.toString(), files.toArray(new File[] {}));
	}

	/**
	 * Signup User
	 * 
	 * @param request
	 * @throws AccounterException
	 */
	private void signup(Mail request) throws AccounterException {
		logger.info("Creating new client to signup");
		String from = request.getFrom();

		Session session = HibernateUtil.getCurrentSession();

		Transaction transaction = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Client.class);
			criteria.add(Restrictions.eq("emailId", from));
			Client client = (Client) criteria.uniqueResult();
			// Creating New Client
			StringBuffer buffer = new StringBuffer();
			if (client == null) {
				client = createNewClient(request);
				buffer.append(TextReponses.signupSuccssfullResponse());
			}

			for (User user : client.getUsers()) {
				// Generate UniqueIds to each users
				if (user.getUniqueId() != null) {
					continue;
				}
				Company company = user.getCompany();
				String tradingName = company.getTradingName();
				tradingName = tradingName.replace(" ", "");
				// Generating random UUID
				String random = UUID.randomUUID().toString();
				random = random.replace("-", "");
				String uniqueId = company.getTradingName() + random + "@"
						+ EMAIL_DOMAIL;
				user.setUniqueId(uniqueId);
				logger.info("Generated unique emailId " + uniqueId);
				session.saveOrUpdate(user);
				buffer.append(uniqueId);
				buffer.append("<br>");
			}
			if (!client.getUsers().isEmpty()) {
				buffer.append("You have been signed up successfully.");
				buffer.append("<br>");
				buffer.append(" You have companies,please use those company emails to furthur process for creating any customers,vendors and any transactions etc.");
			}
			// Save Client
			session.saveOrUpdate(client.getClientSubscription());
			session.saveOrUpdate(client);
			transaction.commit();
			sendResponse(request, buffer.toString());

		} catch (Exception e) {
			logger.error("Error while signing up", e);
			transaction.rollback();
			throw new AccounterException("Error while signing up");
		}

	}

	/**
	 * Creates new CLient
	 * 
	 * @param request
	 * @return
	 */
	private Client createNewClient(Mail request) {

		String from = request.getFrom();
		String fromName = request.getFromName();

		// Create new Client
		Client client = new Client();
		client.setEmailId(from);
		String[] split = fromName.split(" ");
		client.setFirstName(split[0]);
		if (split.length > 1) {
			client.setLastName(split[1]);
		}
		client.setFullName(fromName);
		client.setActive(true);

		// Create Subscription
		logger.info("Creating subscription");
		ClientSubscription clientSubscription = new ClientSubscription();
		clientSubscription.setCreatedDate(new Date());
		clientSubscription.setLastModified(new Date());
		clientSubscription.setSubscription(Subscription
				.getInstance(Subscription.PREMIUM_USER));
		clientSubscription.setDurationType(ClientSubscription.UNLIMITED_USERS);

		client.setClientSubscription(clientSubscription);
		return client;
	}

	private void sendResponse(Mail request, String message) {
		sendResponse(request, message, (File[]) null);
	}

	private void sendResponse(Mail request, String message, File... files) {
		logger.info("Sending response email for " + request.shortForm());
		EMailMessage msg = new EMailMessage();
		msg.setSubject(request.getSubject() + " Response");
		// msg.setFrom(request.getFrom());
		String replyTo = request.getReplyTo();
		if (replyTo == null || replyTo.isEmpty()) {
			replyTo = request.getFrom();
		}
		msg.setRecepeant(replyTo);
		msg.isPlain = true;

		// Set content
		msg.setContent(message);
		if (files != null) {
			// Add Attachments
			for (File file : files) {
				msg.setAttachment(file);
			}
		}

		// ArrayList<EMailMessage> messages = new ArrayList<EMailMessage>();
		// messages.add(msg);
		//
		// EMailJob job = new EMailJob();
		// job.setMessages(messages);
		//
		// // TODO HAS TO REMOVE THIS
		// EMailSenderAccount sender = sampleSendeAccount();
		// job.setSender(sender);
		//
		// logger.info("Adding email job to Queue");
		// EmailManager.getInstance().addJob(job);
		UsersMailSendar.sendResponseMail(msg);
	}

	private EMailSenderAccount sampleSendeAccount() {
		EMailSenderAccount sender = new EMailSenderAccount();
		sender.setOutGoingMailServer("smtp.gmail.com");
		sender.setPortNumber(587);
		sender.setProtocol("smtp");
		sender.setEnableTls(true);
		sender.setSslAutheticationRequired(true);
		sender.setSmtpAuthentication(true);
		sender.setSenderEmailID("***REMOVED***");
		sender.setAccountName("Anna Accounting Support");
		sender.setSenderPassword("vimukti***REMOVED***");
		return sender;
	}
}
