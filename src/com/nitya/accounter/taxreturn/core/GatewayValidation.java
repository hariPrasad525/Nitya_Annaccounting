package com.nitya.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class GatewayValidation {
	/**
	 * 1..1
	 */
	private String processed;
	/**
	 * 1..1
	 */
	private String result;

	public String getProcessed() {
		return processed;
	}

	public void setProcessed(String processed) {
		this.processed = processed;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public IXMLElement toXML() {
		XMLElement gatewayValidationElement = new XMLElement(
				"GatewayValidation");
		if (processed != null) {
			XMLElement processedElement = new XMLElement("Processed");
			processedElement.setContent(processed);
			gatewayValidationElement.addChild(processedElement);
		}
		if (result != null) {
			XMLElement resultElement = new XMLElement("Result");
			resultElement.setContent(result);
			gatewayValidationElement.addChild(resultElement);
		}
		return gatewayValidationElement;
	}
}
