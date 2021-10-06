package com.vimukti.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class NameStructure {
	private String ttl;
	private String fore;
	private String sur;

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public String getFore() {
		return fore;
	}

	public void setFore(String fore) {
		this.fore = fore;
	}

	public String getSur() {
		return sur;
	}

	public void setSur(String sur) {
		this.sur = sur;
	}
	
	public void toXML(XMLElement element){
		XMLElement nameElement=new XMLElement("Name");
		element.addChild(nameElement);
		
		if(ttl!=null){
			XMLElement ttlElement=new XMLElement("Ttl");
			ttlElement.setContent(ttl);
			nameElement.addChild(ttlElement);
		}
		if(fore!=null){
			XMLElement foreElement=new XMLElement("Fore");
			foreElement.setContent(fore);
			nameElement.addChild(foreElement);
		}
		if(sur!=null){
			XMLElement surElement=new XMLElement("Sur");
			surElement.setContent(sur);
			nameElement.addChild(surElement);
		}
	}
}
