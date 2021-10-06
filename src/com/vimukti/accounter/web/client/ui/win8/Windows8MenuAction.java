package com.vimukti.accounter.web.client.ui.win8;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class Windows8MenuAction extends Action {

	@Override
	public String getText() {
		return "Accounter Menu";
	}

	@Override
	public void run() {
		Windows8MenuView view = new Windows8MenuView();
		MainFinanceWindow.getViewManager().showView(view, data, isDependent,
				Windows8MenuAction.this);
	}

	@Override
	public String getHistoryToken() {
		return HistoryTokens.ACCOUNTER_MENU;
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
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
