package com.nitya.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Uday Kumar
 * 
 */
@SuppressWarnings("unchecked")
public class ExpenseClaimsAction extends Action {

	
	int selectedTab;

	public ExpenseClaimsAction(int selectedTab) {
		super();
		this.selectedTab = selectedTab;
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	//
	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		ExpenseClaims view = new ExpenseClaims(selectedTab);
		try {
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, this);
		} catch (Exception e) {
		}

	}

	@Override
	public String getHistoryToken() {
		return "expenseClaims";
	}

	@Override
	public String getHelpToken() {
		return "expense-claims";
	}

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

	@Override
	public String getText() {
		return messages.expenseClaims();
	}
}
