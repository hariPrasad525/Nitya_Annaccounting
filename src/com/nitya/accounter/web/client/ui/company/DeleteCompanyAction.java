package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;

public class DeleteCompanyAction extends Action {

	public DeleteCompanyAction() {
		super();
		this.catagory = messages.settings();
	}

	@Override
	public void run() {
		long companyid = Accounter.getCompany().getID();
		Window.open("/main/deletecompany?companyId=" + companyid, "_parent", "");

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
		return "DeleteCompany";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		return messages.deletecompany();
	}

}
