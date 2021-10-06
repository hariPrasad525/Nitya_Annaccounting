package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class Channel {
	/**
	 * 1..1
	 */
	private String uRI;
	/**
	 * 1..1
	 */
	private String name;
	/**
	 * 0..1
	 */
	private String product;
	/**
	 * 0..1
	 */
	private String version;

	public String getuRI() {
		return uRI;
	}

	public void setuRI(String uRI) {
		this.uRI = uRI;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public IXMLElement toXML() {
		XMLElement channelElement = new XMLElement("Channel");
		if (uRI != null) {
			XMLElement uriElement = new XMLElement("URI");
			uriElement.setContent(uRI);
			channelElement.addChild(uriElement);
		}

		if (name != null) {
			XMLElement nameElement = new XMLElement("Name");
			nameElement.setContent(name);
			channelElement.addChild(nameElement);
		}
		if (product != null) {
			XMLElement productElement = new XMLElement("Product");
			productElement.setContent(product);
			channelElement.addChild(productElement);
		}
		if (version != null) {
			XMLElement versionElement = new XMLElement("Version");
			versionElement.setContent(version);
			channelElement.addChild(versionElement);
		}
		return channelElement;
	}
}
