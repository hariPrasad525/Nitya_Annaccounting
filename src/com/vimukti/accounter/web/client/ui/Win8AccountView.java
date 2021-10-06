package com.vimukti.accounter.web.client.ui;

public class Win8AccountView extends NewAccountView {
	@Override
	public StyledPanel getTopHLay() {
		return mainVLay;
	}

	@Override
	protected void addMainPanel() {
		mainVLay.add(commentsForm);
		this.add(mainVLay);
	}
}
