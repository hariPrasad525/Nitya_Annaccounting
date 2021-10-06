package com.nitya.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class TDSForm16AAction extends Action {

	public TDSForm16AAction() {
		super();
		this.catagory = messages.tds();

	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vatAdjustment();
	}

	@Override
	public void run() {
		TdsForm16ACreationDialogue dialog = new TdsForm16ACreationDialogue();
		ViewManager.getInstance().showDialog(dialog);
	}

	@Override
	public String getHistoryToken() {
		return "TDSform16A";
	}

	@Override
	public String getHelpToken() {
		return null;
	}

	@Override
	public String getText() {
		return messages.challanDetails();
	}

}
