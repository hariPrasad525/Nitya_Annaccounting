package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class SenderDetails {
	/**
	 * Element cardinality 0..1
	 */
	private IDAuthentication iDAuthentication = new IDAuthentication();
	/**
	 * Element cardinality 0..1
	 */
	private long x509Certificate;
	/**
	 * Element cardinality 0..1
	 */
	private String emailAddress;

	public IDAuthentication getiDAuthentication() {
		return iDAuthentication;
	}

	public void setiDAuthentication(IDAuthentication iDAuthentication) {
		this.iDAuthentication = iDAuthentication;
	}

	public long getX509Certificate() {
		return x509Certificate;
	}

	public void setX509Certificate(long x509Certificate) {
		this.x509Certificate = x509Certificate;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public IXMLElement toXML() {

		XMLElement senderDatailsElement = new XMLElement("SenderDetails");
		if (iDAuthentication != null) {
			senderDatailsElement.addChild(iDAuthentication.toXML());
		}
		if (x509Certificate != 0) {
			XMLElement x509CertificateElement = new XMLElement(
					"x509Certificate");
			x509CertificateElement.setContent(Long.toString(x509Certificate));
			senderDatailsElement.addChild(x509CertificateElement);
		}
		if (emailAddress != null) {
			XMLElement emailAddressElement = new XMLElement("EmailAddress");
			emailAddressElement.setContent(emailAddress);
			senderDatailsElement.addChild(emailAddressElement);
		}

		return senderDatailsElement;
	}
}
