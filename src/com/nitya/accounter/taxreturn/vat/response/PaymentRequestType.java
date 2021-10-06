package com.nitya.accounter.taxreturn.vat.response;

import net.n3.nanoxml.XMLElement;

public class PaymentRequestType {
	private DirectDebitInstructionStatus directDebitInstructionStatus;

	public DirectDebitInstructionStatus getDirectDebitInstructionStatus() {
		return directDebitInstructionStatus;
	}

	public void setDirectDebitInstructionStatus(
			DirectDebitInstructionStatus directDebitInstructionStatus) {
		this.directDebitInstructionStatus = directDebitInstructionStatus;
	}

	public void toXML(XMLElement paymentNotificationTypeElement) {
		XMLElement paymentRequestTypeElement = new XMLElement(
				"PaymentRequestType");
		paymentNotificationTypeElement.addChild(paymentRequestTypeElement);
		if (directDebitInstructionStatus != null) {
			directDebitInstructionStatus.toXML(paymentRequestTypeElement);
		}
	}
}
