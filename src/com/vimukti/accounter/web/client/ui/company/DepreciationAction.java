package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class DepreciationAction extends Action {

	

	public DepreciationAction() {
		super();
		this.catagory = messages.company();
	}

	public DepreciationAction(Object editableObject,
			AccounterAsyncCallback<Object> callbackObject) {
		super();
		this.catagory = messages.fixedAssets();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().Depreciation();
	}

	@Override
	public void run() {
		DepreciationView view = new DepreciationView();

		MainFinanceWindow.getViewManager().showView(view, null, false, this);
	}

	@Override
	public String getHistoryToken() {
		return "depreciation";
	}

	@Override
	public String getHelpToken() {
		return "depreciation";
	}

	@Override
	public String getText() {
		return messages.depreciation();
	}

}
