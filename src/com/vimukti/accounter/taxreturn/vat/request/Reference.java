package com.vimukti.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class Reference {
	private String nameSpace;
	private String schemaVersion;
	private String topElementName;

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getSchemaVersion() {
		return schemaVersion;
	}

	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	public String getTopElementName() {
		return topElementName;
	}

	public void setTopElementName(String topElementName) {
		this.topElementName = topElementName;
	}

	public void toXML(XMLElement containsElement) {
		XMLElement referenceElement = new XMLElement("Reference");
		containsElement.addChild(referenceElement);
		if (nameSpace != null) {
			XMLElement nameSpaceElement = new XMLElement("NameSpace");
			nameSpaceElement.setContent(nameSpace);
			referenceElement.addChild(nameSpaceElement);
		}
		if (schemaVersion != null) {
			XMLElement schemaVersionElement = new XMLElement("SchemaVersion");
			schemaVersionElement.setContent(nameSpace);
			referenceElement.addChild(schemaVersionElement);
		}
		if (topElementName != null) {
			XMLElement topElementNameElement = new XMLElement("TopElementName");
			topElementNameElement.setContent(topElementName);
			referenceElement.addChild(topElementNameElement);
		}
	}

}
