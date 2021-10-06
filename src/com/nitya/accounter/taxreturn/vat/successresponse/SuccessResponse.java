package com.nitya.accounter.taxreturn.vat.successresponse;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.taxreturn.vat.request.YesNoType;

import net.n3.nanoxml.XMLElement;

public class SuccessResponse {
	private IRmarkReceipt iRmarkReceipt;
	private List<MessageType> messages = new ArrayList<MessageType>();
	private String acceptedTime;
	private String responseData;
	private YesNoType testInLive;

	public IRmarkReceipt getiRmarkReceipt() {
		return iRmarkReceipt;
	}

	public void setiRmarkReceipt(IRmarkReceipt iRmarkReceipt) {
		this.iRmarkReceipt = iRmarkReceipt;
	}

	public List<MessageType> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageType> messages) {
		this.messages = messages;
	}

	public String getAcceptedTime() {
		return acceptedTime;
	}

	public void setAcceptedTime(String acceptedTime) {
		this.acceptedTime = acceptedTime;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public YesNoType getTestInLive() {
		return testInLive;
	}

	public void setTestInLive(YesNoType testInLive) {
		this.testInLive = testInLive;
	}

	public void toXML(XMLElement element) {
		XMLElement successResponseElement = new XMLElement("SuccessResponse");
		element.addChild(successResponseElement);
		if (iRmarkReceipt != null) {
			iRmarkReceipt.toXML(successResponseElement);
		}
		if (messages != null) {
			for (MessageType message : messages) {
				message.toXML(successResponseElement);
			}
		}
		if (acceptedTime != null) {
			XMLElement acceptedTimeElement = new XMLElement("AcceptedTime");
			acceptedTimeElement.setContent(acceptedTime);
			successResponseElement.addChild(acceptedTimeElement);
		}
		if (responseData != null) {
			XMLElement responseDataElement = new XMLElement("ResponseData");
			responseDataElement.setContent(responseData);
			successResponseElement.addChild(responseDataElement);
		}
		if (testInLive != null) {
			testInLive.toXML(successResponseElement, "TestInLive");
		}
	}
}
