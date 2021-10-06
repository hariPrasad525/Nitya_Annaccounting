package com.vimukti.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class TwoDigitDecimalType {
	private double value;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void toXML(XMLElement elevenPointTwoDigitDecimalType) {
		XMLElement twoDigitDecimalTypeElement = new XMLElement(
				"TwoDigitDecimalType");
		elevenPointTwoDigitDecimalType.addChild(twoDigitDecimalTypeElement);
		if (value != 0) {
			twoDigitDecimalTypeElement.setContent(Double.toString(value));
		}
	}

}
