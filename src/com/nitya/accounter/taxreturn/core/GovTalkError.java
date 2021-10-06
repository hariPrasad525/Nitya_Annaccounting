package com.nitya.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class GovTalkError {
	/**
	 * 1..1
	 */
	private String raisedBy;
	/**
	 * 0..1
	 */
	private int number;
	/**
	 * 1..1
	 */
	private String type;
	/**
	 * 0..∞
	 */
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 0..∞
	 */
	private String location;

	public GovTalkError() {
	}

	public String getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IXMLElement toXML() {
		XMLElement govTalkErrorElement = new XMLElement("GovTalkError");
		if (raisedBy != null) {
			XMLElement raisedByElement = new XMLElement("RaisedBy");
			raisedByElement.setContent(raisedBy);
			govTalkErrorElement.addChild(raisedByElement);
		}
		if (number != 0) {
			XMLElement numberElement = new XMLElement("Number");
			numberElement.setContent(Integer.toString(number));
			govTalkErrorElement.addChild(numberElement);
		}
		if (type != null) {
			XMLElement typeElement = new XMLElement("Type");
			typeElement.setContent(type);
			govTalkErrorElement.addChild(typeElement);
		}
		if (text != null) {
			XMLElement textElement = new XMLElement("Text");
			textElement.setContent(text);
			govTalkErrorElement.addChild(textElement);
		}
		if (location != null) {
			XMLElement locationElement = new XMLElement("Location");
			locationElement.setContent(location);
			govTalkErrorElement.addChild(locationElement);
		}
		return govTalkErrorElement;
	}

}
