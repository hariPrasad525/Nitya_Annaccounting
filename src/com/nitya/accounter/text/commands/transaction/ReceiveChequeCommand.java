package com.nitya.accounter.text.commands.transaction;

public class ReceiveChequeCommand extends ReceivePaymentCommand {

	@Override
	public String getPaymentMethod() {
		return "Cheque";
	}
}
