package com.vimukti.accounter.text.commands.transaction;

public class PayCashCommand extends PayBillCommand {

	@Override
	public String getPaymentMethod() {
		return "Cash";
	}
}
