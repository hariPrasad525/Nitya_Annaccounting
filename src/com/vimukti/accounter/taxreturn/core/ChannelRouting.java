package com.vimukti.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class ChannelRouting {
	/**
	 * 1..1
	 */
	private Channel channel = new Channel();
	/**
	 * 0..∞
	 */
	private List<ID> iDs = new ArrayList<ID>();
	/**
	 * 1..1
	 */
	private String timestamp;

	public ChannelRouting() {
		getiDs().add(new ID());
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public List<ID> getiDs() {
		return iDs;
	}

	public void setiDs(List<ID> iDs) {
		this.iDs = iDs;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public IXMLElement toXML() {
		XMLElement channelRoutingElement = new XMLElement("ChannelRouting");
		if (channel != null) {
			channelRoutingElement.addChild(channel.toXML());
		}
		if (iDs != null) {
			for (ID id : iDs) {
				channelRoutingElement.addChild(id.toXML());
			}
		}
		if (timestamp != null) {
			XMLElement timestampElement = new XMLElement("Timestamp");
			timestampElement.setContent(timestamp);
			channelRoutingElement.addChild(timestampElement);
		}
		return channelRoutingElement;
	}
}
