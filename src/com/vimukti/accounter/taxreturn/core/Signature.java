package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class Signature {

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public IXMLElement toXML() {
		XMLElement signatureElement = new XMLElement("Signature");
		if (value != null) {
			signatureElement.setContent(value);
		}
		return signatureElement;
	}

}
