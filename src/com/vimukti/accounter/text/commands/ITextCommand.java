package com.vimukti.accounter.text.commands;

import com.vimukti.accounter.text.CommandContext;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.web.client.exception.AccounterException;

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
