package com.nitya.accounter.text.commands;

import com.nitya.accounter.text.CommandContext;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.web.client.exception.AccounterException;

public interface ITextCommand {
	public static final String EMAIL_DOMAIL = "email.annaccounting.com";;

	/**
	 * If given data is not type of this command, then return false otherwise
	 * true
	 * 
	 * @param data
	 * @param respnse
	 * @return
	 */
	boolean parse(ITextData data, ITextResponse respnse);

	void process(ITextResponse respnse) throws AccounterException;

	void setContext(CommandContext context);

}
