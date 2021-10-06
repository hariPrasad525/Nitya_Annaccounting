package com.vimukti.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class Manifest {
	private Contains contains;

	public Contains getContains() {
		return contains;
	}

	public void setContains(Contains contains) {
		this.contains = contains;
	}

	public void toXML(XMLElement iRheaderElement) {
		XMLElement manifestElement = new XMLElement("Manifest");
		iRheaderElement.addChild(manifestElement);
		if (contains != null) {
			contains.toXML(manifestElement);
		}
	}

}
