package com.vimukti.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class YesNoType {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void toXML(XMLElement element, String attribute) {
		if (value.equals("Yes")) {
			element.setAttribute(attribute, "Yes");
		} else {
			element.setAttribute(attribute, "No");
		}
	}
}
