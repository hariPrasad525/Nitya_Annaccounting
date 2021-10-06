package com.nitya.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class TelephoneNumberType {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void toXML(XMLElement element) {
		XMLElement numberElement = new XMLElement("Number");
		numberElement.setContent(value);
		element.addChild(numberElement);
	}
}
