package com.vimukti.accounter.mail;

import java.util.ArrayList;
import java.util.List;

public class EMailJob {

	EMailSenderAccount sender;
	List<EMailMessage> messages = new ArrayList<EMailMessage>();
	String senderIdentityId;
	String createrIdentityID;
	private String campaignName;
	public String companyName;

	public EMailJob() {

	}

	public EMailJob(EMailMessage emailMsg, EMailSenderAccount emailAcc,
			String comapanyName) {
		this.messages.add(emailMsg);
		this.sender = emailAcc;
		this.companyName = comapanyName;
	}

	public EMailJob(EMailMessage emailMsg, EMailSenderAccount emailAcc) {
		this.messages.add(emailMsg);
		this.sender = emailAcc;
	}

	public EMailSenderAccount getSender() {
		return sender;
	}

	public void setSender(EMailSenderAccount sender) {
		this.sender = sender;
	}

	public List<EMailMessage> getMailMessages() {
		return this.messages;
	}

	public List<EMailMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<EMailMessage> messages) {
		this.messages = messages;
	}

	public String getSenderIdentityId() {
		return senderIdentityId;
	}

	public void setSenderIdentityId(String senderIdentityId) {
		this.senderIdentityId = senderIdentityId;
	}

	public String getCreaterIdentityID() {
		return createrIdentityID;
	}

	public void setCreaterIdentityID(String createrIdentityID) {
		this.createrIdentityID = createrIdentityID;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCampaignName() {
		return campaignName;
	}

}
