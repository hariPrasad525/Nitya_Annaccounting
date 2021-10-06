package com.nitya.accounter.taxreturn.vat.request;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.XMLElement;

public class ContactDetailsStructure {

	private NameStructure name;
	private List<EmailStructure> emails = new ArrayList<EmailStructure>();
	private List<TelephoneStructure> telephones = new ArrayList<TelephoneStructure>();
	private List<TelephoneStructure> faxs = new ArrayList<TelephoneStructure>();

	public NameStructure getName() {
		return name;
	}

	public void setName(NameStructure name) {
		this.name = name;
	}

	public List<EmailStructure> getEmail() {
		return emails;
	}

	public void setEmail(List<EmailStructure> email) {
		this.emails = email;
	}

	public List<TelephoneStructure> getTelephone() {
		return telephones;
	}

	public void setTelephone(List<TelephoneStructure> telephone) {
		this.telephones = telephone;
	}

	public List<TelephoneStructure> getFax() {
		return faxs;
	}

	public void setFax(List<TelephoneStructure> fax) {
		this.faxs = fax;
	}

	public void toXML(XMLElement principalElement) {
		XMLElement contactDetailsStructureElement = new XMLElement(
				"ContactDetailsStructure");
		principalElement.addChild(contactDetailsStructureElement);
		if (name != null) {
			name.toXML(contactDetailsStructureElement);
		}
		if (emails != null) {
			for (EmailStructure email : emails) {
				email.toXML(contactDetailsStructureElement);
			}
		}
		if (telephones != null) {
			for (TelephoneStructure telephone : telephones) {
				telephone.toXML(contactDetailsStructureElement);
			}
		}
		if (faxs != null) {
			for (TelephoneStructure fax : faxs) {
				fax.toXML(contactDetailsStructureElement);
			}
		}
	}

}
