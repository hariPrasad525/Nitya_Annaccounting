package com.nitya.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class TDSAcknowledgmentAction extends Action {

	public TDSAcknowledgmentAction() {
		super();
		this.catagory = messages.tds();
	}

	@Override
	public String getText() {
		return messages.tdsAcknowledgment();
	}

	@Override
	public void run() {
		TDSAcknowlegmentForm ackFormDialogue = new TDSAcknowlegmentForm();
		ViewManager.getInstance().showDialog(ackFormDialogue);
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "enterTDSAckNo";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
