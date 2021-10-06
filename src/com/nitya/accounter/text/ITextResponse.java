package com.nitya.accounter.text;

import java.io.File;

public interface ITextResponse {

	public void addError(String error);

	public boolean hasErrors();

	public void addMessage(String msg);

	public void addFile(String path);

	public void addFile(File file);

}
