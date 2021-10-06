package com.vimukti.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class ThirteenDigitIntegerType {
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void toXML(XMLElement element) {
		XMLElement thirteenDigitIntegerTypeElement = new XMLElement(
				"ThirteenDigitIntegerType");
		element.addChild(thirteenDigitIntegerTypeElement);
		if (value != 0) {
			thirteenDigitIntegerTypeElement.setContent(Integer.toString(value));
		}
	}

}
