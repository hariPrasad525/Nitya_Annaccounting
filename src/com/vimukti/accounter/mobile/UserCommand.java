package com.vimukti.accounter.mobile;

public class UserCommand {
	private String inputs;
	private String commandName;
	private String displayName;

	public UserCommand() {
		this("");
	}

	public UserCommand(String commandName) {
		this(commandName, commandName.isEmpty() ? commandName
				: MobileServerMessages.getMessage(commandName), "");
	}

	public UserCommand(String commandName, String displayName, String inputs) {
		this.setDisplayName(displayName);
		this.commandName = commandName;
		this.setInputs(inputs);
	}

	public UserCommand(String commandName, String inputs) {
		this(commandName, MobileServerMessages.getMessage(commandName), inputs);
	}

	public UserCommand(String commandName, long id) {
		this(commandName, String.valueOf(id));
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	@Override
	public String toString() {
		return displayName;
	}

	public String getInputs() {
		return inputs;
	}

	public void setInputs(String inputs) {
		this.inputs = inputs;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
