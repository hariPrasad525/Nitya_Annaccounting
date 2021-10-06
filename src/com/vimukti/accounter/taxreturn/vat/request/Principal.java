package com.vimukti.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class Principal {
	private ContactDetailsStructure contact;

	public ContactDetailsStructure getContact() {
		return contact;
	}

	public void setContact(ContactDetailsStructure contact) {
		this.contact = contact;
	}

	public void toXML(XMLElement iRheaderElement) {
		XMLElement principalElement = new XMLElement("Principal");
		iRheaderElement.addChild(principalElement);
		if (contact != null) {
			contact.toXML(principalElement);
		}
	}
}
