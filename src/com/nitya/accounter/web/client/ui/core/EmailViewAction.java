package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.customers.EmailView;

public class EmailViewAction extends Action {

	public EmailViewAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		runAsync(data, id, isDependent);

	}

	private void runAsync(Object invoice, long themeId, Boolean boolean1) {
		EmailView emailView = new EmailView((ClientTransaction) data);
		emailView.setThemeId(themeId);

		MainFinanceWindow.getViewManager().showView(emailView, data,
				isDependent, EmailViewAction.this);
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
		// TODO Auto-generated method stub
		return "email";
	}

	@Override
	public String getHelpToken() {
		return "email-view";
	}

	@Override
	public String getText() {
		return messages.email();
	}

}
