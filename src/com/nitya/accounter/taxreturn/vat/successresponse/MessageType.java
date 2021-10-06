package com.nitya.accounter.taxreturn.vat.successresponse;

import net.n3.nanoxml.XMLElement;

public class MessageType {
	private String value;
	private String lang;
	private String code;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void toXML(XMLElement successResponseElement) {
		XMLElement messageElement = new XMLElement("Message");
		if (value != null) {
			messageElement.setContent(value);
		}
		if (lang != null) {
			messageElement.setAttribute("lang", lang);
		}
		if (code != null) {
			messageElement.setAttribute("code", code);
		}
	}
}
