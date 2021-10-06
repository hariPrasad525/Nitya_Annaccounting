package com.vimukti.accounter.taxreturn.vat.request;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.XMLElement;

public class InternationalAddressStructure {
	private List<String> lines = new ArrayList<String>();
	private String postCode;
	private String country;

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void toXML(XMLElement element) {
		XMLElement addressElement = new XMLElement("Address");
		element.addChild(addressElement);
		if (lines != null) {
			for (String line : lines) {
				XMLElement lineElement = new XMLElement("Line");
				lineElement.setContent(line);
				addressElement.addChild(lineElement);
			}
		}
		if (postCode != null) {
			XMLElement postCodeElement = new XMLElement("PostCode");
			postCodeElement.setContent(postCode);
			addressElement.addChild(postCodeElement);
		}
		if (country != null) {
			XMLElement countryElement = new XMLElement("Country");
			countryElement.setContent(country);
			addressElement.addChild(countryElement);
		}

	}
}
