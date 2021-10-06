package com.vimukti.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class EmailStructure {
	private String email;
	private String type;
	private String preffered;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPreffered() {
		return preffered;
	}

	public void setPreffered(String preffered) {
		this.preffered = preffered;
	}

	public void toXML(XMLElement element) {
		XMLElement emailElement = new XMLElement("Email");
		if (email != null) {
			emailElement.setContent(email);
		}
		element.addChild(emailElement);
		if (type != null) {
			emailElement.setAttribute("Type", type);
		}
		if (preffered != null) {
			emailElement.setAttribute("Preffered", preffered);
		}
	}

}
