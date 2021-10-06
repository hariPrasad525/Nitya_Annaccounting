package com.vimukti.accounter.taxreturn.vat.response;

import java.util.Date;

import net.n3.nanoxml.XMLElement;

public class DirectDebitPaymentStatusType {
	private Date collectionDate;

	public Date getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}

	public void toXML(XMLElement paymentNotificationTypeElement) {
		XMLElement directDebitPaymentStatusElement = new XMLElement(
				"DirectDebitPaymentStatus");
		if (directDebitPaymentStatusElement != null) {
			directDebitPaymentStatusElement.setContent(collectionDate
					.toString());
		}
		paymentNotificationTypeElement
				.addChild(directDebitPaymentStatusElement);
	}
}
