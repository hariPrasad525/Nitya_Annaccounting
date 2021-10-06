package com.vimukti.accounter.taxreturn.vat.response;

import net.n3.nanoxml.XMLElement;

public class NonEmptyToken {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void toXML(XMLElement element) {
		XMLElement nonEmptyTokenElement = new XMLElement("Narrative");
		nonEmptyTokenElement.setContent(value);
		element.addChild(nonEmptyTokenElement);
	}
}
