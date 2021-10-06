package com.nitya.accounter.text.commands.transaction;

public class PayChequeCommand extends PayBillCommand {

	@Override
	public String getPaymentMethod() {
		return "Cheque";
	}
}
