package com.nitya.accounter.taxreturn.core;

import net.n3.nanoxml.XMLElement;

public class Header {
	/**
	 * Element cardinality 1..1
	 */
	private MessageDetails messageDatails = new MessageDetails();
	/**
	 * Element cardinality 0..1
	 */
	private SenderDetails senderDatails = new SenderDetails();

	public MessageDetails getMessageDatails() {
		return messageDatails;
	}

	public void setMessageDatails(MessageDetails messageDatails) {
		this.messageDatails = messageDatails;
	}

	public SenderDetails getSenderDatails() {
		return senderDatails;
	}

	public void setSenderDatails(SenderDetails senderDatails) {
		this.senderDatails = senderDatails;
	}

	public XMLElement toXML() {
		XMLElement element = new XMLElement("Header");
		if (messageDatails != null) {
			element.addChild(messageDatails.toXML());
		}
		if (senderDatails != null) {
			element.addChild(senderDatails.toXML());
		}
		return element;
	}
}
