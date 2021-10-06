package com.nitya.accounter.web.client.ui.win8;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class IPadMenuAction extends Action {

	@Override
	public String getText() {
		return "Accounter Menu";
	}

	@Override
	public void run() {
		IpadMenuView view = new IpadMenuView();
		MainFinanceWindow.getViewManager().showView(view, data, isDependent,
				IPadMenuAction.this);
	}

	@Override
	public String getHistoryToken() {
		return HistoryTokens.ACCOUNTER_MENU;
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return "ipadmenu";
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

}
