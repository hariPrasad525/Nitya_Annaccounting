package com.vimukti.accounter.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import au.com.bytecode.opencsv.CSVParser;

public class TextCommandParser {

	private static final List<String> OMIT_KEYWORDS = Arrays
			.asList(new String[] { "report" });

	public static ArrayList<ITextData> parse(String text) throws IOException {
		ArrayList<ITextData> commands = new ArrayList<ITextData>();

		Scanner scanner = new Scanner(text);

		// Get First command
		String command = scanner.nextLine();
		command = prepareCommand(command);
		while (scanner.hasNextLine()) {
			// Get Next Command
			String nextLine = scanner.nextLine();
			if (nextLine == null || nextLine.isEmpty()) {
				continue;
			}
			nextLine = prepareCommand(nextLine);
			// Add Previous command, if current line is a command
			String first = getCommandType(nextLine);
			if (isCommand(first)) {
				// Add command to List
				commands.add(new TextDataImpl(getCommandType(command), command,
						parseCSV(command)));
				// set current command to previous command
				command = nextLine;
			} else {
				// If current line is not a command, then just append it to
				// previous
				command = command + nextLine;
			}
		}

		// Add Last command here
		String first = getCommandType(command);

		if (isCommand(first)) {
			commands.add(new TextDataImpl(first, command, parseCSV(command)));
		}

		return commands;
	}

	/**
	 * Removing the Omit Key Words
	 * 
	 * @param command
	 * @return
	 */
	private static String prepareCommand(String command) {
		if (command == null || command.isEmpty()) {
			return null;
		}
		String first = command.split(",")[0];
		if (OMIT_KEYWORDS.contains(first)) {
			// Remove first string
			command = command.replaceFirst(first, "");
			if (command.startsWith(",")) {
				command = command.replaceFirst(",", "");
			}
		}
		return command;
	}

	private static String[] parseCSV(String nextLine) throws IOException {
		CSVParser parser = new CSVParser(',');
		String[] parseLine = parser.parseLine(nextLine);
		return parseLine;
	}

	private static boolean isCommand(String first) {
		return CommandsFactory.getCommand(first) != null;
	}

	private static String getCommandType(String line) {
		if (line == null || line.isEmpty()) {
			return null;
		}
		return line.split(",")[0];

	}
}
