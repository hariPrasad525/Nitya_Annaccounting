package com.vimukti.accounter.mobile.xtream;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.UserCommand;

public class JCommandList {
	int type;
	List<JCommand> commandNames = new ArrayList<JCommand>();

	public JCommandList() {
		type = 2;
	}

	public int addAll(CommandList object, int code) {
		for (UserCommand command : object) {
			JCommand jCommand = new JCommand();
			jCommand.set(command.getDisplayName(),
					String.valueOf((char) code++));
			commandNames.add(jCommand);
		}
		return code;
	}
}
