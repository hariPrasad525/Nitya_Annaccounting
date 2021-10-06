package com.vimukti.accounter.taxreturn.vat.response;

import net.n3.nanoxml.XMLElement;

public class VATDeclarationHeaderType {
	private VATPeriodType vATPeriodType;
	private String currencyCode;
	private String extensionPart;

	public VATPeriodType getvATPeriodType() {
		return vATPeriodType;
	}

	public void setvATPeriodType(VATPeriodType vATPeriodType) {
		this.vATPeriodType = vATPeriodType;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getExtensionPart() {
		return extensionPart;
	}

	public void setExtensionPart(String extensionPart) {
		this.extensionPart = extensionPart;
	}

	public void toXML(XMLElement vATDeclarationResponseElement) {
		XMLElement vATDeclarationHeaderTypeElement = new XMLElement(
				"VATDeclarationHeaderType");
		if (vATPeriodType != null) {
			vATPeriodType.toXML(vATDeclarationHeaderTypeElement);
		}
		if (currencyCode != null) {
			XMLElement currencyCodeElement = new XMLElement("CurrencyCode");
			currencyCodeElement.setContent(currencyCode);
			vATDeclarationHeaderTypeElement.addChild(currencyCodeElement);
		}
		if (extensionPart != null) {
			XMLElement extensionPartElement = new XMLElement("ExtensionPart");
			extensionPartElement.setContent(extensionPart);
			vATDeclarationHeaderTypeElement.addChild(extensionPartElement);
		}

	}

}
