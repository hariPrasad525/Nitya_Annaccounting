package com.nitya.accounter.taxreturn.vat.response;

import net.n3.nanoxml.XMLElement;

public class NotificationType {
	private NonEmptyToken nonEmptyToken;

	public NonEmptyToken getNonEmptyToken() {
		return nonEmptyToken;
	}

	public void setNonEmptyToken(NonEmptyToken nonEmptyToken) {
		this.nonEmptyToken = nonEmptyToken;
	}

	public void toXML(XMLElement vATDeclarationResponseTypeElement) {
		XMLElement informationNotificationElement = new XMLElement(
				"InformationNotification");
		vATDeclarationResponseTypeElement
				.addChild(informationNotificationElement);
		if (nonEmptyToken != null) {
			nonEmptyToken.toXML(informationNotificationElement);
		}
	}
}
