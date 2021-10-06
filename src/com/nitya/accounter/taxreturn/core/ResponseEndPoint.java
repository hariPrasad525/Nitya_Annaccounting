package com.nitya.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class ResponseEndPoint {
	private String value;

	private String pollInterval;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPollInterval() {
		return pollInterval;
	}

	public void setPollInterval(String pollInterval) {
		this.pollInterval = pollInterval;
	}

	public IXMLElement toXML() {
		XMLElement responseEndPointElement = new XMLElement("ResponseEndPoint");
		if (value != null) {
			responseEndPointElement.setContent(value);
		}
		if (pollInterval != null) {
			responseEndPointElement.setAttribute("PollInterval", pollInterval);
		}
		return responseEndPointElement;
	}

}
