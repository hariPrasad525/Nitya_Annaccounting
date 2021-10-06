package com.nitya.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class TargetDetails {

	/**
	 * 0..8
	 */
	private List<String> organisations = new ArrayList<String>();

	public List<String> getOrganisations() {
		return organisations;
	}

	public void setOrganisations(List<String> organisations) {
		this.organisations = organisations;
	}

	public IXMLElement toXML() {
		XMLElement element = new XMLElement("TargetDetails");
		if (organisations != null) {
			for (String organisation : organisations) {
				if (organisation != null) {
					XMLElement organisationElement = new XMLElement(
							"Organisation");
					organisationElement.setContent(organisation);
					element.addChild(organisationElement);
				}
			}
		}
		return element;
	}
}
