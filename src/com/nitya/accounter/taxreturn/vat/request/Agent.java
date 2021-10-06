package com.nitya.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class Agent {
	private String agentId;
	private String company;
	private InternationalAddressStructure address;
	private ContactDetailsStructure contact;

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public InternationalAddressStructure getAddress() {
		return address;
	}

	public void setAddress(InternationalAddressStructure address) {
		this.address = address;
	}

	public ContactDetailsStructure getContact() {
		return contact;
	}

	public void setContact(ContactDetailsStructure contact) {
		this.contact = contact;
	}

	public void toXML(XMLElement iRheaderElement) {
		XMLElement agentElement = new XMLElement("Agent");
		iRheaderElement.addChild(agentElement);
		if (agentId != null) {
			XMLElement agentIdElement = new XMLElement("AgentID");
			agentIdElement.setContent(agentId);
			agentElement.addChild(agentIdElement);
		}
		if (company != null) {
			XMLElement companyElement = new XMLElement("Company");
			companyElement.setContent(company);
			agentElement.addChild(companyElement);
		}
		if (address != null) {
			address.toXML(agentElement);
		}
		if (contact != null) {
			contact.toXML(agentElement);
		}
	}

}
