package com.nitya.accounter.text.commands.transaction;

public class ReceiveCashCommand extends ReceivePaymentCommand {

	@Override
	public String getPaymentMethod() {
		return "Cash";
	}
}
