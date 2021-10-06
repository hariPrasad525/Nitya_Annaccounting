package com.vimukti.accounter.mobile;

import java.util.ArrayList;
import java.util.List;

public class Result {
	List<Object> resultParts = new ArrayList<Object>();
	private String cookie;
	private String title;
	private boolean hideCancel;
	private boolean showBack;

	private transient CommandList commandList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		String upperCase = title;
		if (title != null && title.length() >= 1) {
			upperCase = title.substring(0, 1).toUpperCase()
					+ title.substring(1);
		}
		this.title = upperCase;
	}

	public boolean isHideCancel() {
		return hideCancel;
	}

	public void setHideCancel(boolean hideCancel) {
		this.hideCancel = hideCancel;
	}

	public boolean isShowBack() {
		return showBack;
	}

	public void setShowBack(boolean showBack) {
		this.showBack = showBack;
	}

	private String nextCommand;

	/**
	 * Creates new Instance
	 */
	public Result() {
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	/**
	 * Creates new Instance
	 */
	public Result(String message) {
		resultParts.add(message);
	}

	public void add(String message) {
		this.resultParts.add(message);
	}

	public void add(ResultList list) {
		this.resultParts.add(list);
	}

	public void add(CommandList list) {
		this.resultParts.add(list);
	}

	public void add(UserCommand c) {
		if (commandList == null) {
			commandList = new CommandList();
			resultParts.add(commandList);
		}
		commandList.add(c);
	}

	/**
	 * Returns the ResiltParts
	 */
	public List<Object> getResultParts() {
		return resultParts;
	}

	public void addAll(int i, List<Object> resultParts2) {
		resultParts.addAll(i, resultParts2);
	}

	public String getNextCommand() {
		return nextCommand;
	}

	public void setNextCommand(String nextCommand) {
		this.nextCommand = nextCommand;
	}

	public void add(int i, String string) {
		resultParts.add(i, string);
	}

	public void add(InputType inputType) {
		resultParts.add(inputType);
	}

	public void remove(String string) {
		resultParts.remove(string);
	}
}
