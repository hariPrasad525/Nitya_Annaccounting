package com.nitya.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class GatewayAdditions {
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
		XMLElement gatewayAdditions = new XMLElement("GatewayAdditions");
		if (value != null) {
			gatewayAdditions.setContent(value);

		}
		if (type != null) {
			gatewayAdditions.setAttribute("type", type);
		}
		return gatewayAdditions;
	}
}
