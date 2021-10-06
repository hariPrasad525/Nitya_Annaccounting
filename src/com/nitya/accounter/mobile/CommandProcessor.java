package com.nitya.accounter.mobile;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.nitya.accounter.main.CompanyPreferenceThreadLocal;
import com.nitya.accounter.mobile.commands.NameSearchCommand;
import com.nitya.accounter.mobile.commands.NumberSearchCommand;
import com.nitya.accounter.utils.HibernateUtil;

/**
 * Handles the Request from User and Executes corresponding Command
 * 
 */
public class CommandProcessor {

	Logger log = Logger.getLogger(CommandProcessor.class);

	public static CommandProcessor INSTANCE = new CommandProcessor();

	public synchronized Result handleMessage(MobileSession session,
			UserMessage message) throws AccounterMobileException {

		try {
			switch (message.getType()) {
			case COMMAND:
				Result reply = processCommand(session, message);
				message.setResult(reply);
				break;
			case HELP:
				break;
			case NUMBER:
				processNumber(session, message);
				break;
			case NAME:
				processName(session, message);
				break;
			}

			if (message.getResult() == null) {
				Result result = new Result(
						"Sorry, We are unable to find the answer for '"
								+ message.getOriginalMsg() + "'");
				result.setNextCommand("menu");
				message.setResult(result);
			}

			session.setLastMessage(message);
			return message.getResult();
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AccounterMobileException) {
				throw (AccounterMobileException) e;
			}
			throw new AccounterMobileException(e);

		}
	}

	/**
	 * @param session
	 * @param userMessage
	 * @throws AccounterMobileException
	 */
	private void processName(MobileSession session, UserMessage userMessage)
			throws AccounterMobileException {
		NameSearchCommand command = new NameSearchCommand();
		Context context = getContext(session, userMessage);
		context.setInputs(userMessage.getInputs());
		Result result = command.run(context);
		userMessage.setCommand(command);
		userMessage.setResult(result);
	}

	/**
	 * @param session
	 * @param userMessage
	 * @throws AccounterMobileException
	 */
	private void processNumber(MobileSession session, UserMessage userMessage)
			throws AccounterMobileException {
		NumberSearchCommand command = new NumberSearchCommand();
		Context context = getContext(session, userMessage);
		context.setInputs(userMessage.getInputs());
		Result result = command.run(context);
		userMessage.setCommand(command);
		userMessage.setResult(result);
	}

	/**
	 * 
	 * Creates And Runs the Command
	 * 
	 * @param message
	 * @return
	 * @throws AccounterMobileException
	 */
	private Result processCommand(MobileSession session, UserMessage message)
			throws AccounterMobileException {
		Command command = message.getCommand();
		Context context = getContext(session, message);
		// Getting Last Result
		UserMessage lastMessage = session.getLastMessage();
		Result lastResult = lastMessage == null ? null : lastMessage
				.getResult();
		boolean isSelected = false;
		if (lastResult != null) {
			List<Object> resultParts = lastResult.getResultParts();
			Iterator<Object> iterator = resultParts.iterator();
			while (iterator.hasNext()) {
				Object next = iterator.next();
				if (next instanceof ResultList) {
					// Setting Selections
					isSelected = isSelected
							|| setSelections((ResultList) next,
									message.getInputs(), context);
				}
			}
		}
		context.removeSelection(command.getSelectionName());
		command.setSelectionName(null);

		context.setInputs(message.getInputs());
		if (!isSelected) {
			context.setString(message.getOriginalMsg());
		} else {
			context.setString("");
		}
		Result result = null;
		try {
			context.setPreferences(CompanyPreferenceThreadLocal.get());
			result = command.run(context);
			result = processResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			result = context.makeResult();
			result.setHideCancel(false);
			result.add("You got an Exception....@@@@@@@");
		}
		return result;
	}

	private Result processResult(Result result) {
		PatternResult patternResult = new PatternResult();
		patternResult.setNextCommand(result.getNextCommand());
		patternResult.setCookie(result.getCookie());
		patternResult.setHideCancel(result.isHideCancel());
		patternResult.setShowBack(result.isShowBack());
		patternResult.setTitle(result.getTitle());
		boolean isCommandList = false;
		for (Object obj : result.resultParts) {
			if (obj instanceof CommandList) {
				isCommandList = true;
			}
			patternResult.add(obj);
		}
		if (isCommandList) {
			return patternResult;
		}
		return result;
	}

	/**
	 * @param next
	 * @param inputs
	 * @param context
	 */
	private boolean setSelections(ResultList next, List<String> inputs,
			Context context) {
		boolean isSelected = false;
		ResultList resultList = (ResultList) next;
		for (Record record : resultList) {
			if (inputs.contains(record.getCode())) {
				isSelected = true;
				if (!resultList.isMultiSelection()) {
					context.putSelection(resultList.getName(),
							record.getObject());
					// FIXME GOT MULTIPLE SELECTION FROM USER EVEN
					// THOUGH SINGLE SELECTION ENABLED
					break;
				} else {
					context.putSelection(resultList.getName(),
							record.getObject());
				}
			}
		}
		return isSelected;
	}

	private Context getContext(MobileSession mSession, UserMessage message) {
		Session session = HibernateUtil.getCurrentSession();
		mSession.sethibernateSession(session);
		Context context = new Context(mSession);
		context.setNetworkId(message.getNetworkId());
		context.setNetworkType(message.getNetworkType());
		context.setCommandString(message.getCommandString());
		return context;
	}
}
