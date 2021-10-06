/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.hibernate.Session;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.mobile.MobileAdaptor.AdaptorType;
import com.vimukti.accounter.mobile.UserMessage.Type;
import com.vimukti.accounter.mobile.commands.AuthenticationCommand;
import com.vimukti.accounter.mobile.commands.SelectCompanyCommand;
import com.vimukti.accounter.mobile.store.CommandsFactory;
import com.vimukti.accounter.mobile.store.PatternStore;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileMessageHandler extends Thread {
	private static MobileMessageHandler instance;
	private Map<String, MobileSession> sessions = new HashMap<String, MobileSession>();
	private LinkedBlockingQueue<MobileChannelContext> queue = new LinkedBlockingQueue<MobileChannelContext>();

	private MobileMessageHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		while (true) {
			try {
				MobileChannelContext take = queue.take();
				messageReceived(take);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void messageReceived(MobileChannelContext context) {
		if (context.getMessage().isEmpty()) {
			return;
		}
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		String processMessage;
		Session openSession = HibernateUtil.openSession();
		try {
			processMessage = processMessage(context.getNetworkId(),
					context.getMessage(), context.getAdaptorType(),
					context.getNetworkType(), null, context);
		} catch (AccounterMobileException e) {
			e.printStackTrace();
			processMessage = "Exception: " + e.getMessage();
		} finally {
			if (openSession.isOpen()) {
				openSession.close();
			}
		}
		context.send(processMessage);
	}

	private String processMessage(String networkId, String message,
			AdaptorType adaptorType, int networkType, Result oldResult,
			MobileChannelContext context) throws AccounterMobileException {
		try {
			MobileSession session = sessions.get(networkId);
			if (session == null || session.isExpired()) {
				sessions.remove(networkId);
				if (networkType != AccounterChatServer.NETWORK_TYPE_MOBILE) {
					message = networkId;
				}
				String[] split2 = message.split(",");
				String deviceName = split2.length > 1 ? split2[1] : "";

				String[] split = split2[0].split(" ");
				String cookie = split.length > 0 ? split[0] : "";
				String language = split.length > 1 ? split[split.length - 1]
						: "";
				message = cookie;
				session = sessions.get(cookie);
				if (session != null && !session.isExpired()) {
					session.setLanguage(language);
					session.setDeviceName(deviceName);
					networkId = cookie;
					context.changeNetworkId(cookie);
					// context.setNetworkId(cookie);
					return session.getLastReply();
				} else {
					session = new MobileSession();
					session.setDeviceName(deviceName);
					sessions.put(networkId, session);
					session.setLanguage(language);
					context.setNetworkId(networkId);
				}
			}
			ServerLocal.set(getLocal(session.getLanguage()));
			Global.set(new ServerGlobal());

			session.sethibernateSession(HibernateUtil.getCurrentSession());
			session.reloadObjects();

			MobileAdaptor adoptor = getAdaptor(adaptorType);
			UserMessage userMessage = preProcess(session, message, networkId,
					networkType);

			Command command = userMessage.getCommand();
			session.addCommand(command, userMessage);

			Result result = getCommandProcessor().handleMessage(session,
					userMessage);
			if (oldResult != null) {
				List<Object> resultParts = oldResult.getResultParts();
				for (int i = resultParts.size(); i > 0; i--) {
					result.resultParts.add(0, resultParts.get(i - 1));
				}
				String cookie = oldResult.getCookie();
				if (result.getCookie() == null) {
					result.setCookie(cookie);
				}
				result.setHideCancel(oldResult.isHideCancel());
			}
			boolean hasNextCommand = true;
			if (command != null && command.isDone()) {
				session.refreshCurrentCommand();
				String nextCommand = result.getNextCommand();
				if (nextCommand == null) {
					session.refreshLastMessage();
					UserMessage lastMessage = session.getLastMessage();
					if (lastMessage != null) {
						lastMessage.setResult(lastMessage.getLastResult());
						return processMessage(networkId, null, adaptorType,
								networkType, result, context);
					} else {
						hasNextCommand = false;
					}
				}
			}

			String nextCommand = result.getNextCommand();
			if (nextCommand != null) {
				result.setNextCommand(null);
				return processMessage(networkId, nextCommand, adaptorType,
						networkType, result, context);
			}

			if (!hasNextCommand) {
				return processMessage(networkId, "back", adaptorType,
						networkType, result, context);
			}

			Command currentCommand = session.getCurrentCommand();
			if (currentCommand == null) {
				result.setHideCancel(true);
			} else {
				if (currentCommand instanceof AuthenticationCommand) {
					if (currentCommand.getAttribute("select") == null) {
						result.setHideCancel(true);
					}
				}
			}
			String commandString = userMessage.getCommandString();
			result.setTitle(commandString == null ? null : MobileServerMessages
					.getMessage(commandString));
			String postProcess = adoptor.postProcess(result);
			session.setLastReply(postProcess);
			if (session.isExpired()) {
				sessions.remove(networkId);
			} else {
				session.await(this, networkId);
			}
			return postProcess;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterMobileException(e);
		}
	}

	private Locale getLocal(String language) {
		if (language.length() == 2) {
			return new Locale(language);
		}

		Locale[] availableLocales = Locale.getAvailableLocales();
		for (Locale locale : availableLocales) {
			String lang = "";
			try {
				language = locale.getISO3Language();
			} catch (Exception e) {
				language = "eng";
			}
			if (lang.equalsIgnoreCase(language)) {
				return locale;
			}
		}
		return Locale.getDefault();
	}

	/**
	 * PreProcess the UserMessage
	 * 
	 * @param userMessage
	 * @return
	 * @throws AccounterMobileException
	 */
	private UserMessage preProcess(MobileSession session, String clientMessage,
			String networkId, int networkType) throws AccounterMobileException {
		String message = clientMessage;
		UserMessage userMessage = new UserMessage(message, networkId,
				networkType);

		if (message == null || message.isEmpty()) {
			userMessage = session.getLastMessage();
			message = userMessage.getOriginalMsg();
			clientMessage = message;
		}
		Command command = null;

		if (command == null) {
			command = session.getCurrentCommand();
		}

		// ****** SELECT PATTERNS AND COMMANDS *******//
		Company company = session.getCompany();
		Result result = PatternStore.INSTANCE.find(clientMessage,
				session.isAuthenticated(), company);
		if (result != null) {
			result.setShowBack(session.getLastMessage() != null);
			userMessage.setType(Type.HELP);
			userMessage.setResult(result);
			userMessage.setCommandString(message);
			userMessage.setOriginalMsg(message);
			return userMessage;
		} else {
			String commandString = message;
			Command matchedCommand = null;
			if (session.isAuthenticated() || message.equalsIgnoreCase("cancel")) {
				CommandResult commandResult = findCommand(message);
				commandString = commandResult.commandString;
				matchedCommand = commandResult.command;
			}

			if (matchedCommand != null) {
				message = message.replaceAll(commandString.trim(), "").trim();
				userMessage.setOriginalMsg(message);
				command = matchedCommand;
				userMessage.setCommandString(commandString.trim());
			}
		}

		// ******* CHECKING BACK FUNCTION *******//
		if (command == null && message.equalsIgnoreCase("back")) {
			session.refreshLastMessage();
			UserMessage lastMessage2 = session.getLastMessage();
			if (lastMessage2 != null) {
				message = lastMessage2.getCommandString();
			} else {
				message = "menu";
			}
			result = PatternStore.INSTANCE.find(message,
					session.isAuthenticated(), company);
			if (result != null) {
				if (lastMessage2 != null) {
					Result lastResult2 = lastMessage2.getResult();
					result.setShowBack(lastResult2 != null ? lastResult2
							.isShowBack() : false);
					lastMessage2.setResult(result);
					return lastMessage2;
				}

				result.setShowBack(session.getLastMessage() != null);
				userMessage.setType(Type.HELP);
				userMessage.setResult(result);
				userMessage.setCommandString(message);
				userMessage.setOriginalMsg(message);
				return userMessage;
			} else {
				lastMessage2.setResult(lastMessage2.getLastResult());
				message = lastMessage2.getOriginalMsg();
				clientMessage = message;
				command = session.getCurrentCommand();
			}

		}

		// ***** CHECKING AUTHENTICATION *******//
		if (command == null && !session.isAuthenticated()) {
			command = new AuthenticationCommand();
			userMessage.setCommandString("login");
			if (networkType != AccounterChatServer.NETWORK_TYPE_MOBILE) {
				userMessage.setOriginalMsg("");// To know it is first
				message = "";
			}
		}

		// ***** CHECKING COMOANY SELECTION *****//
		if (command == null) {
			long companyId = session.getCompanyID();
			if (companyId == 0) {
				session.removeAllMessages();
				command = new SelectCompanyCommand();
				userMessage.setCommandString("selectCompany");
			}
		}

		// **** CHECKING LAST MESSAGE ******//
		UserMessage lastMessage = session.getLastMessage();
		Result lastResult = lastMessage == null ? null : lastMessage
				.getResult();
		if (lastMessage != null && userMessage.getCommandString() == null) {
			userMessage.setCommandString(lastMessage.getCommandString());
		}
		if (lastResult instanceof PatternResult) {
			PatternResult patternResult = (PatternResult) lastResult;

			String commandString = getPatternResultString(
					patternResult.getCommands(), clientMessage);

			if (commandString != null) {
				result = PatternStore.INSTANCE.find(commandString,
						session.isAuthenticated(), company);
				if (result != null) {
					result.setShowBack(session.getLastMessage() != null);
					userMessage.setType(Type.HELP);
					userMessage.setResult(result);
					userMessage.setCommandString(commandString);
					userMessage.setOriginalMsg(commandString);
					return userMessage;
				}
			}
		}

		if (lastResult instanceof PatternResult) {
			PatternResult patternResult = (PatternResult) lastResult;
			Command selectCommand = getCommand(patternResult.getCommands(),
					message, userMessage);
			if (selectCommand != null) {
				command = selectCommand;
			}
		}

		// **** CREATING USER MESSAGE ******//
		userMessage.setLastResult(lastResult);

		if (command != null) {
			userMessage.setType(Type.COMMAND);
			userMessage.setCommand(command);
			userMessage.setInputs(message.split(" "));
			return userMessage;
		}

		if (message.startsWith("#")) {
			userMessage.setType(Type.NUMBER);
			userMessage.setInputs(message.replaceAll("#", "").split(" "));
			return userMessage;
		}

		// TODO Check for isName
		if (!message.contains(" ")) {
			userMessage.setType(Type.NAME);
			userMessage.setInputs(message.split(" "));
		}

		userMessage.setInputs(new String[] { message });

		return userMessage;
	}

	private CommandResult findCommand(String message) {
		String commandString = message;
		message = message.trim();
		Command matchedCommand = null;
		if (!message.isEmpty()) {
			String[] split = message.split(" ");
			commandString = split[0];

			// while (!commandString.isEmpty()) {
			// matchedCommand =
			// CommandsFactory.INSTANCE.getCommand(commandString);
			// if (matchedCommand != null) {
			// break;
			// }
			// int lastIndexOf = commandString.lastIndexOf(" ");
			// if (lastIndexOf > 0) {
			// commandString = commandString.substring(0, lastIndexOf);
			// }
			// if (lastIndexOf < 0) {
			// break;
			// }
			// }
			matchedCommand = CommandsFactory.INSTANCE.getCommand(commandString);
		}
		CommandResult result = new CommandResult();
		result.command = matchedCommand;
		result.commandString = commandString;
		result.input = message.replace(commandString, "").trim();
		return result;
	}

	private String getPatternResultString(CommandList commands, String input) {
		// Getting the First Character of the Input
		if (input == null || input.isEmpty() || input.length() > 1) {
			return null;
		}
		char ch = input.charAt(0);
		// Finding the Command for Input
		int index = ch - 97;// If ch is number
		if (index < 0) {
			return null;
		}
		if (index >= commands.size()) {
			return null;
		}
		UserCommand userCommand = commands.get(index);
		String commandString = userCommand.getCommandName();
		if (commandString == null) {
			return null;
		}
		return commandString;
	}

	private Command getCommand(CommandList commands, String input,
			UserMessage userMessage) {
		// Getting the First Character of the Input
		if (input == null || input.isEmpty() || input.length() > 1) {
			return null;
		}
		char ch = input.charAt(0);
		// Finding the Command for Input
		int index = ch - 97;// If ch is number
		if (index < 0) {
			return null;
		}
		if (index >= commands.size()) {
			return null;
		}
		// Rao
		UserCommand userCommand = commands.get(index);
		CommandResult findCommand = findCommand(userCommand.getCommandName());
		Command command = findCommand.command;
		if (command == null) {
			return null;
		}
		userMessage.setOriginalMsg(findCommand.input + userCommand.getInputs());
		userMessage.setCommandString(findCommand.commandString);
		return command;
	}

	/**
	 * Returns the Adaptor of given Type
	 * 
	 * @param chat
	 * @return
	 */
	private MobileAdaptor getAdaptor(AdaptorType type) {
		if (type.equals(AdaptorType.CHAT)) {
			return MobileChatAdaptor.INSTANCE;
		} else if (type.equals(AdaptorType.MOBILE)) {
			return MobileApplicationAdaptor.INSTANCE;
		} else {
			return null;
		}
	}

	/**
	 * Returns the Command Processor
	 */
	private CommandProcessor getCommandProcessor() {
		return CommandProcessor.INSTANCE;
	}

	public void logout(String networkId) {
		sessions.remove(networkId);
	}

	public synchronized void putMessage(MobileChannelContext context) {
		try {
			queue.put(context);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static MobileMessageHandler getInstance() {
		if (instance == null) {
			instance = new MobileMessageHandler();
			instance.start();
		}
		return instance;
	}
}

class CommandResult {
	Command command;
	String commandString;
	String input;
}
