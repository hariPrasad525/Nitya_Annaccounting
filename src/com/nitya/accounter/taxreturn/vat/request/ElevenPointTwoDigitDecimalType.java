package com.nitya.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class ElevenPointTwoDigitDecimalType {
	private TwoDigitDecimalType twoDigitDecimalType;

	public TwoDigitDecimalType getTwoDigitDecimalType() {
		return twoDigitDecimalType;
	}

	public void setTwoDigitDecimalType(TwoDigitDecimalType twoDigitDecimalType) {
		this.twoDigitDecimalType = twoDigitDecimalType;
	}

	public void toXML(XMLElement vATDueOnOutputsElement) {
		XMLElement elevenPointTwoDigitDecimalType = new XMLElement(
				"ElevenPointTwoDigitDecimalType");
		vATDueOnOutputsElement.addChild(elevenPointTwoDigitDecimalType);
		if (twoDigitDecimalType != null) {
			twoDigitDecimalType.toXML(elevenPointTwoDigitDecimalType);
		}
	}

}
