package com.nitya.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class IRmark {
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

	public void toXML(XMLElement iRheaderElement) {
		XMLElement iRmarkElement = new XMLElement("IRmark");
		iRheaderElement.addChild(iRmarkElement);
		if (value != null) {
			iRmarkElement.setContent(value);
		}
		if (type != null) {
			iRmarkElement.setAttribute("Type", type);
		}
	}

}
