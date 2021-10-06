package com.nitya.accounter.taxreturn.vat.response;

import net.n3.nanoxml.XMLElement;

public class VATDeclarationResponse {

	private VATDeclarationHeaderType header;
	private VATDeclarationResponseType body;

	public VATDeclarationHeaderType getHeader() {
		return header;
	}

	public void setHeader(VATDeclarationHeaderType header) {
		this.header = header;
	}

	public VATDeclarationResponseType getBody() {
		return body;
	}

	public void setBody(VATDeclarationResponseType body) {
		this.body = body;
	}

	public void toXML(XMLElement element) {
		XMLElement vATDeclarationResponseElement = new XMLElement(
				"VATDeclarationResponse");
		element.addChild(vATDeclarationResponseElement);
		if (header != null) {
			header.toXML(vATDeclarationResponseElement);
		}
		if (body != null) {
			body.toXML(vATDeclarationResponseElement);
		}
	}
}
