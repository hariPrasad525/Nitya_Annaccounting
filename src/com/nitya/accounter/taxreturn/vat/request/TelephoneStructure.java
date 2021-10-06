package com.nitya.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class TelephoneStructure {
	private TelephoneNumberType number;
	private String extension;
	private WorkHomeType type;
	private YesNoType mobile;
	private YesNoType preffered;

	public TelephoneNumberType getNumber() {
		return number;
	}

	public void setNumber(TelephoneNumberType number) {
		this.number = number;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public WorkHomeType getType() {
		return type;
	}

	public void setType(WorkHomeType type) {
		this.type = type;
	}

	public YesNoType getMobile() {
		return mobile;
	}

	public void setMobile(YesNoType mobile) {
		this.mobile = mobile;
	}

	public YesNoType getPreffered() {
		return preffered;
	}

	public void setPreffered(YesNoType preffered) {
		this.preffered = preffered;
	}

	public void toXML(XMLElement contactDetailsStructureElement) {
		XMLElement telephoneElement = new XMLElement("Telephone");
		contactDetailsStructureElement.addChild(telephoneElement);
		if (number != null) {
			number.toXML(telephoneElement);
		}
		if (extension != null) {
			XMLElement extensionElement = new XMLElement("Extension");
			extensionElement.setContent(extension);
			telephoneElement.addChild(extensionElement);
		}
		if (type != null) {
			type.toXML(telephoneElement, "Type");
		}
		if (mobile != null) {
			mobile.toXML(telephoneElement, "Mobile");
		}
		if (preffered != null) {
			preffered.toXML(telephoneElement, "Preffered");
		}
	}

}
