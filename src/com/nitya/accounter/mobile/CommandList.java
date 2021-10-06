package com.nitya.accounter.mobile;

import java.util.ArrayList;

public class CommandList extends ArrayList<UserCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean add(String e) {
		UserCommand command = new UserCommand(e);
		return super.add(command);
	}
}
