package com.vimukti.accounter.taxreturn.vat.response;

import net.n3.nanoxml.XMLElement;

public class DirectDebitInstructionStatus {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void toXML(XMLElement paymentRequestTypeElement) {
		XMLElement directDebitInstructionStatusElement = new XMLElement(
				"DirectDebitInstructionStatus");
		paymentRequestTypeElement.addChild(directDebitInstructionStatusElement);
		if (value != null) {
			directDebitInstructionStatusElement.setContent(value);
		}
	}

}
