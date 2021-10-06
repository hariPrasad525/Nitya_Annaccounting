package com.nitya.accounter.mobile.store;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.mobile.MobileServerMessages;
import com.nitya.accounter.mobile.PatternResult;
import com.nitya.accounter.mobile.UserCommand;

public abstract class Output {

	public void add(PatternResult result, Company company) {
		if (this instanceof Text) {
			result.add(getMessage(((Text) this).text));
		} else {
			PCommand command = (PCommand) this;
			if (command.title == null) {
				command.title = command.value;
			}
			if (command.value == null) {
				command.value = command.title;
			}
			UserCommand comd = new UserCommand();
			comd.setDisplayName(getMessage(command.title));
			comd.setCommandName(command.value);
			if (command.condition == null
					|| result.checkCondition(command.condition, company)) {
				result.add(comd);
			}
		}
	}

	private String getMessage(String key) {
		return MobileServerMessages.getMessage(key);
	}
}

class Text extends Output {
	String text;
}

class PCommand extends Output {
	String condition;
	String title;
	String value;
}