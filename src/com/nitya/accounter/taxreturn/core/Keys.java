package com.nitya.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class Keys {
	/**
	 * 0..∞ type+
	 */
	private List<Key> keys = new ArrayList<Key>();

	public Keys() {
	}

	public List<Key> getKeys() {
		return keys;
	}

	public void setKeys(List<Key> keys) {
		this.keys = keys;
	}

	public IXMLElement toXML() {
		XMLElement keysElement = new XMLElement("Keys");
		if (keys != null) {
			for (Key key : keys) {
				keysElement.addChild(key.toXML());
			}
		}
		return keysElement;
	}

}
