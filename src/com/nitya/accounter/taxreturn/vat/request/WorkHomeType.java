package com.nitya.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class WorkHomeType {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void toXML(XMLElement element, String attribute) {
		if (value.equals("Work")) {
			element.setAttribute(attribute, "Work");
		} else {
			element.setAttribute(attribute, "Home");
		}
	}
}
