package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DashBoardView;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.IPadDashBoard;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.AbstractView;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.customers.InvoiceListView;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;
import com.nitya.accounter.web.client.ui.win8portlets.Windows8DashBoard;

public class CompanyHomeAction extends Action {

	public CompanyHomeAction() {
		super();
		this.catagory = messages.company();
	}

	public CompanyHomeAction(IsSerializable editableObject,
			AccounterAsyncCallback<Object> callbackObject) {
		super();
		this.catagory = messages.company();
	}

	@Override
	public void run() {

		if (Accounter.isIpadApp()) {
			IPadDashBoard view = new IPadDashBoard();
			MainFinanceWindow.getViewManager()
					.showView(view, null, false, this);
		} else if (Accounter.isWin8App()) {
			Windows8DashBoard dashBoard = new Windows8DashBoard();
			MainFinanceWindow.getViewManager().showView(dashBoard, null, false,
					this);
		} else {
			AbstractView<?> view = null;
			if (isShowInvoiceList()) {
				view = (InvoiceListView) GWT.create(InvoiceListView.class);
			} else {
				view = (DashBoardView) GWT.create(DashBoardView.class);
			}
			MainFinanceWindow.getViewManager()
					.showView(view, null, false, this);
		}
	}

	private boolean isShowInvoiceList() {
		ClientUser user = Accounter.getUser();
		if (RolePermissions.CUSTOM.equals(user.getUserRole())
				&& (user.getPermissions().isOnlySeeInvoiceandBills() || user
						.getPermissions().isOnlyInvoiceAndPayments())) {
			return true;
		}
		return false;
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public String getHistoryToken() {
		if (isShowInvoiceList()) {
			return HistoryTokens.INVOICES;
		}
		return "dashBoard";
	}

	@Override
	public String getHelpToken() {
		return "company";
	}

	@Override
	public String getText() {
		if (isShowInvoiceList()) {
			return messages.invoices();
		}
		return messages.dashBoard();
	}

}
