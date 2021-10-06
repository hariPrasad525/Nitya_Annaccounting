package com.vimukti.accounter.text;

import java.io.File;
import java.util.ArrayList;

public class CommandResponseImpl implements ITextResponse {

	private ArrayList<String> errors = new ArrayList<String>();
	private ArrayList<String> messages = new ArrayList<String>();
	private ArrayList<String> files = new ArrayList<String>();
	private ArrayList<ITextData> data = new ArrayList<ITextData>();

	@Override
	public void addError(String error) {
		errors.add(error);
	}

	@Override
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	@Override
	public void addMessage(String msg) {
		messages.add(msg);
	}

	@Override
	public void addFile(String path) {
		files.add(path);
	}

	@Override
	public void addFile(File file) {
		addFile(file.getAbsolutePath());
	}

	public void addData(ITextData data) {
		this.data.add(data);
	}

	/**
	 * @return the data
	 */
	public ArrayList<ITextData> getData() {
		return data;
	}

	/**
	 * @return the errors
	 */
	public ArrayList<String> getErrors() {
		return errors;
	}

	/**
	 * @return the messages
	 */
	public ArrayList<String> getMessages() {
		return messages;
	}

	/**
	 * @return the files
	 */
	public ArrayList<String> getFiles() {
		return files;
	}

}
