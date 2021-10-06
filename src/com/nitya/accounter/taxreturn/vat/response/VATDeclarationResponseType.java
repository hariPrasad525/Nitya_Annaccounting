package com.nitya.accounter.taxreturn.vat.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.n3.nanoxml.XMLElement;

public class VATDeclarationResponseType {
	private Date paymentDueDate;
	private String vATDeclarationReference;
	private String receiptTimestamp;
	private PaymentNotificationType paymentNotification;
	private List<NotificationType> informationNotifications = new ArrayList<NotificationType>();
	private List<String> extensionParts = new ArrayList<String>();

	public Date getPaymentDueDate() {
		return paymentDueDate;
	}

	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public String getvATDeclarationReference() {
		return vATDeclarationReference;
	}

	public void setvATDeclarationReference(String vATDeclarationReference) {
		this.vATDeclarationReference = vATDeclarationReference;
	}

	public String getReceiptTimestamp() {
		return receiptTimestamp;
	}

	public void setReceiptTimestamp(String receiptTimestamp) {
		this.receiptTimestamp = receiptTimestamp;
	}

	public PaymentNotificationType getPaymentNotification() {
		return paymentNotification;
	}

	public void setPaymentNotification(
			PaymentNotificationType paymentNotification) {
		this.paymentNotification = paymentNotification;
	}

	public List<NotificationType> getInformationNotifications() {
		return informationNotifications;
	}

	public void setInformationNotifications(
			List<NotificationType> informationNotifications) {
		this.informationNotifications = informationNotifications;
	}

	public List<String> getExtensionParts() {
		return extensionParts;
	}

	public void setExtensionParts(List<String> extensionParts) {
		this.extensionParts = extensionParts;
	}

	public void toXML(XMLElement vATDeclarationResponseElement) {
		XMLElement vATDeclarationResponseTypeElement = new XMLElement("Body");
		vATDeclarationResponseElement
				.addChild(vATDeclarationResponseTypeElement);
		if (paymentDueDate != null) {
			XMLElement paymentDueDateElement = new XMLElement("PaymentDueDate");
			paymentDueDateElement.setContent(paymentDueDate.toString());
			vATDeclarationResponseTypeElement.addChild(paymentDueDateElement);
		}
		if (vATDeclarationReference != null) {
			XMLElement vATDeclarationReferenceElement = new XMLElement(
					"VATDeclarationReference");
			vATDeclarationReferenceElement.setContent(vATDeclarationReference);
			vATDeclarationResponseTypeElement
					.addChild(vATDeclarationReferenceElement);
		}
		if (receiptTimestamp != null) {
			XMLElement receiptTimestampElement = new XMLElement(
					"receiptTimestamp");
			receiptTimestampElement.setContent(receiptTimestamp);
			vATDeclarationResponseTypeElement.addChild(receiptTimestampElement);
		}
		if (paymentNotification != null) {
			paymentNotification.toXML(vATDeclarationResponseTypeElement);
		}
		if (informationNotifications != null) {
			for (NotificationType informationNotification : informationNotifications) {
				informationNotification
						.toXML(vATDeclarationResponseTypeElement);
			}
		}
		if (extensionParts != null) {
			for (String extensionPart : extensionParts) {
				XMLElement extensionPartElement = new XMLElement(
						"ExtensionPart");
				extensionPartElement.setContent(extensionPart);
				vATDeclarationResponseTypeElement
						.addChild(extensionPartElement);
			}
		}
	}

}
