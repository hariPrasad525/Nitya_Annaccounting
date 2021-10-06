package com.nitya.accounter.text;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.text.commands.ITextCommand;
import com.nitya.accounter.utils.HibernateUtil;

public class CommandProcessor {

	private Logger logger = Logger.getLogger(CommandProcessor.class);

	private static CommandProcessor processor;

	public static CommandProcessor getInstance() {
		if (processor == null) {
			processor = new CommandProcessor();
		}
		return processor;
	}

	public ArrayList<CommandResponseImpl> processCommands(CommandsQueue queue,
			Client client) {

		ArrayList<CommandResponseImpl> responses = new ArrayList<CommandResponseImpl>();

		while (queue.hasNext()) {
			CommandResponseImpl cResponse = new CommandResponseImpl();
			processCommand(queue, cResponse, client);
			responses.add(cResponse);
			if (!cResponse.hasErrors()) {
				cResponse.addMessage("Request successfull!!");
			}
		}

		return responses;
	}

	private void processCommand(CommandsQueue queue,
			CommandResponseImpl response, Client client) {

		// Take next
		ITextData data = queue.take();
		logger.info("Processing command - " + data.toString());
		Class<? extends ITextCommand> commandClass = CommandsFactory
				.getCommand(data.getType());
		ITextCommand command = null;
		try {
			command = commandClass.newInstance();
			CommandContext commandContext = new CommandContext();
			commandContext.put(CommandContext.CLIENT, client);
			command.setContext(commandContext);
		} catch (Exception e) {
			logger.info("Unable to Processing the Command", e);
			// Send response as Invalid command
			response.addError("Invalid command");
		}

		// Parse command
		parseData(command, data, response, queue);

		// Got errors in parsing, don't process this
		if (response.hasErrors()) {
			logger.info("Got errors after parsing command");
			return;
		}

		// PROCESS COMMAND
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			logger.info("Processing command");
			command.process(response);
			transaction.commit();
		} catch (Exception e) {
			logger.error("Error while processing command", e);
			// Add Error to response
			response.addError(e.getMessage());
			transaction.rollback();
			session.close();
		}
	}

	private void parseData(ITextCommand command, ITextData data,
			CommandResponseImpl response, CommandsQueue queue) {
		logger.info("Parsing Request" + data);
		Class<? extends ITextCommand> commandClass = CommandsFactory
				.getCommand(data.getType());
		if (commandClass != command.getClass()) {
			// If not same command, Just return
			queue.revertPrevious();
			return;
		}

		// Add Data to Response
		response.addData(data);

		// PARSE COMMAND DATA
		boolean isParseSuccess = command.parse(data, response);

		// If Has errors, just return
		if (response.hasErrors()) {
			return;
		}

		// Is Same as Previous, then parse next command
		if (isParseSuccess && queue.hasNext()) {
			parseData(command, queue.take(), response, queue);
		} else {
			// If not same as Previous, then just process it as first data
			// already read by this command
			data.reset();
			if (!isParseSuccess) {
				queue.revertPrevious();
			}
			// Add Data to Response
			response.getData().remove(data);
		}
	}
}
