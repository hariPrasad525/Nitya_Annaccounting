package com.nitya.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlValue;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class GovTalkErrors {
	/**
	 * 1..∞
	 */
	@XmlValue
	private List<GovTalkError> errors = new ArrayList<GovTalkError>();

	public GovTalkErrors() {
		getErrors().add(new GovTalkError());
	}

	public List<GovTalkError> getErrors() {
		return errors;
	}

	public void setErrors(List<GovTalkError> errors) {
		this.errors = errors;
	}

	public IXMLElement toXML() {
		XMLElement govTalkErrorsElement = new XMLElement("GovTalkErrors");
		if (errors != null) {
			for (GovTalkError error : errors) {
				govTalkErrorsElement.addChild(error.toXML());
			}
		}
		return govTalkErrorsElement;
	}
}
