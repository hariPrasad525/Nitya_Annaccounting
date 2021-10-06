package com.vimukti.accounter.taxreturn.vat.successresponse;

import net.n3.nanoxml.XMLElement;

public class IRmarkReceipt {
	private String signature;
	private MessageType message;

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public MessageType getMessage() {
		return message;
	}

	public void setMessage(MessageType message) {
		this.message = message;
	}

	public void toXML(XMLElement successResponseElement) {
		XMLElement iRmarkReceiptElement = new XMLElement("IRmarkReceipt");
		successResponseElement.addChild(iRmarkReceiptElement);
		if (signature != null) {
			XMLElement signatureElement = new XMLElement("Signature");
			signatureElement.setContent(signature);
			successResponseElement.addChild(signatureElement);
		}
		if (message != null) {
			message.toXML(successResponseElement);
		}
	}
}
