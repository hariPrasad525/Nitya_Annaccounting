package com.nitya.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class ElevenPointTwoDigitNonNegativeDecimalType {

	private ElevenPointTwoDigitDecimalType elevenPointTwoDigitDecimalType;

	public ElevenPointTwoDigitDecimalType getElevenPointTwoDigitDecimalType() {
		return elevenPointTwoDigitDecimalType;
	}

	public void setElevenPointTwoDigitDecimalType(
			ElevenPointTwoDigitDecimalType elevenPointTwoDigitDecimalType) {
		this.elevenPointTwoDigitDecimalType = elevenPointTwoDigitDecimalType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement elevenPointTwoDigitNonNegativeDecimalTypeElement = new XMLElement(
				"ElevenPointTwoDigitNonNegativeDecimalType");
		vATDeclarationRequestElement
				.addChild(elevenPointTwoDigitNonNegativeDecimalTypeElement);
		if (elevenPointTwoDigitDecimalType != null) {
			elevenPointTwoDigitDecimalType
					.toXML(elevenPointTwoDigitNonNegativeDecimalTypeElement);
		}
	}
}
