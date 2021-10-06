/**
 * 
 */
package com.nitya.accounter.mobile;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TimerTask;

import org.hibernate.Session;

import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.User;
import com.nitya.accounter.main.CompanyPreferenceThreadLocal;
import com.nitya.accounter.utils.SecureUtils;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileSession {

	public static final long SESSION_TIME_OUT_PERIOD = 24 * 60 * 60 * 1000;// 1day

	private static final String LAST_MESSAGE = "lastMessage";

	private Client client;
	private long companyID;
	private Map<Object, Object> attributes = new HashMap<Object, Object>();
	private Command currentCommand;
	private boolean isExpired;
	private Stack<UserMessage> commandStack = new Stack<UserMessage>();
	private TimerTask task;
	private User user;
	private String deviceName;

	private Session hibernateSession;

	private boolean isAuthenicated;

	private String lastReply = "";

	private String language;

	private byte[] d2;

	private String id;

	/**
	 * Creates new Instance
	 */
	public MobileSession() {
		id = SecureUtils.createID();
	}

	public String getUserEmail() {
		return getClient().getEmailId();
	}

	public void setAttribute(String name, Object value) {
		this.attributes.put(name, value);
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public Object getLast(RequirementType customer) {
		return attributes.get(customer);
	}

	public void setLast(RequirementType customer, Object obj) {
		this.attributes.put(customer, obj);
	}

	/**
	 * Returns the Current Processing Command
	 * 
	 * @return
	 */
	public Command getCurrentCommand() {
		return this.currentCommand;
	}

	/**
	 * Sets the CurrentCommand
	 * 
	 * @param command
	 */
	public void setCurrentCommand(Command command) {
		this.currentCommand = command;
	}

	public boolean isExpired() {
		return this.isExpired;
	}

	public void setExpired(boolean value) {
		this.isExpired = value;
	}

	public void await(final MobileMessageHandler mobileMessageHandler,
			final String networkId) {
		refresh();
		this.task = new TimerTask() {

			@Override
			public void run() {
				setExpired(true);
				mobileMessageHandler.logout(networkId);
			}
		};
		AccounterTimer.INSTANCE.schedule(task, SESSION_TIME_OUT_PERIOD);
	}

	public void refresh() {
		if (this.task != null) {
			task.cancel();
		}
	}

	public void refreshCurrentCommand() {
		// Set the Present Command From the stack
		while (!commandStack.isEmpty()) {
			UserMessage userMessage = commandStack.pop();
			if (userMessage == null) {
				continue;
			}
			Command pop = userMessage.getCommand();
			if (pop == null) {
				setCurrentCommand(null);
				setLastMessage(userMessage);
				commandStack.push(userMessage);
				return;
			}
			if (!pop.isDone()) {
				setCurrentCommand(pop);
				setLastMessage(userMessage);
				commandStack.push(userMessage);
				return;
			}
		}
		setCurrentCommand(null);
		setLastMessage(null);
	}

	/**
	 * @return
	 */
	public User getUser() {
		return user;
	}

	public Company getCompany() {
		return (Company) hibernateSession.get(Company.class, companyID);
	}

	/**
	 * @param session
	 */
	public void sethibernateSession(Session session) {
		this.hibernateSession = session;
	}

	public Session getHibernateSession() {
		return this.hibernateSession;
	}

	/**
	 * @return
	 */
	public UserMessage getLastMessage() {
		return (UserMessage) getAttribute(LAST_MESSAGE);
	}

	/**
	 * @param result
	 */
	public void setLastMessage(UserMessage userMessage) {
		setAttribute(LAST_MESSAGE, userMessage);
	}

	public long getCompanyID() {
		return companyID;
	}

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * @param company
	 *            the company to set
	 */
	public void setCompanyID(long company) {
		this.companyID = company;
	}

	public void addCommand(Command command, UserMessage userMessage) {
		if (currentCommand != command || command == null) {
			UserMessage lastMessage = getLastMessage();
			if (lastMessage != null && lastMessage != userMessage) {
				Command command2 = lastMessage.getCommand();
				if (command2 == null || !command2.isDone()) {
					commandStack.push(lastMessage);
				}
			}
			setCurrentCommand(command);
		}
	}

	/**
	 * Removes an Attribute from the Context
	 * 
	 * @param name
	 * @return
	 */
	public Object removeAttribute(String name) {
		return attributes.remove(name);
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void reloadObjects() {
		if (client != null) {
			client = (Client) hibernateSession
					.get(Client.class, client.getID());
		}
		if (user != null) {
			user = (User) hibernateSession.get(User.class, user.getID());
			AccounterThreadLocal.set(user);
			try {
				if (user.getSecretKey() != null) {
					EU.createCipher(user.getSecretKey(), d2, getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Company company = getCompany();
		if (company != null) {
			try {
				ClientCompanyPreferences preferences = new FinanceTool()
						.getCompanyManager().getClientCompanyPreferences(
								company);
				CompanyPreferenceThreadLocal.set(preferences);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isAuthenticated() {
		return isAuthenicated;
	}

	public void setAuthentication(boolean b) {
		isAuthenicated = b;
	}

	public String getLastReply() {
		return lastReply;
	}

	public void setLastReply(String lastReply) {
		this.lastReply = lastReply;
	}

	public void removeAllMessages() {
		commandStack.clear();
	}

	public boolean hasPendingCommands() {
		return !commandStack.isEmpty();
	}

	public void refreshLastMessage() {
		if (!commandStack.isEmpty()) {
			UserMessage pop = commandStack.pop();
			setCurrentCommand(pop.getCommand());
			setLastMessage(pop);
			return;
		}
		setLastMessage(null);
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public byte[] getD2() {
		return d2;
	}

	public void setD2(byte[] d2) {
		this.d2 = d2;
	}

	public String getId() {
		return id;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
}
