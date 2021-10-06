package com.nitya.accounter.mail;

import java.util.ArrayList;


public class TestEmailJob extends EMailJob {

	public TestEmailJob(String userID, String companyName, EmailTest test) {
		EMailMessage msg = new EMailMessage();
		msg.setRecepeant(test.toEmail);
		msg.setContent(test.testContent);
		msg.setSubject(test.testSubject);

		setSender(test);
		setCampaignName("Test");
		setCreaterIdentityID(userID);

		ArrayList<EMailMessage> arrayList = new ArrayList<EMailMessage>();
		arrayList.add(msg);
		this.setMessages(arrayList);
		this.companyName = companyName;
	}
}
