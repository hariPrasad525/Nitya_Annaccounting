package com.nitya.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class IDAuthentication {
	/**
	 * Element cardinality 1..1
	 */
	private String senderId;
	/**
	 * Element cardinality 1..∞
	 */
	private List<Authentication> authentications = new ArrayList<Authentication>();

	public IDAuthentication() {
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public List<Authentication> getAuthentications() {
		return authentications;
	}

	public void setAuthentications(List<Authentication> authentications) {
		this.authentications = authentications;
	}

	public IXMLElement toXML() {
		XMLElement iDAuthenticationElement = new XMLElement("IDAuthentication");
		if (senderId != null) {
			XMLElement senderIdElement = new XMLElement("SenderID");
			senderIdElement.setContent(senderId);
			iDAuthenticationElement.addChild(senderIdElement);
		}
		if (authentications != null) {
			for (Authentication authentication : authentications) {
				iDAuthenticationElement.addChild(authentication.toXML());
			}
		}
		return iDAuthenticationElement;
	}
}
