package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class Key {

	private String value;

	private String type;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IXMLElement toXML() {
		XMLElement keyElement = new XMLElement("Key");
		if (value != null) {
			keyElement.setContent(value);
		}
		if (type != null) {
			keyElement.setAttribute("Type", type);
		}
		return keyElement;
	}
}
