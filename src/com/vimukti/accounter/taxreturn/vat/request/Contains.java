package com.vimukti.accounter.taxreturn.vat.request;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.XMLElement;

public class Contains {
	private List<Reference> references = new ArrayList<Reference>();

	public List<Reference> getReferences() {
		return references;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public void toXML(XMLElement manifestElement) {
		XMLElement containsElement = new XMLElement("Contains");
		manifestElement.addChild(containsElement);
		if (references != null) {
			for (Reference reference : references) {
				reference.toXML(containsElement);
			}
		}
	}

}
