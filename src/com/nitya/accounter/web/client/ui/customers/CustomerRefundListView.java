package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.Lists.CustomerRefundsList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.TransactionsListView;
import com.nitya.accounter.web.client.ui.grids.CustomerRefundListGrid;

/**
 * 
 * @author Fernandez
 * @param <T>
 * 
 */
public class CustomerRefundListView extends
		TransactionsListView<CustomerRefundsList> implements IPrintableView {
	private int viewId;

	private static final int STATUS_NOT_ISSUED = 0;
	private static final int STATUS_ISSUED = 2;

	public CustomerRefundListView() {
		super(messages.issued());
	}

	@Override
	protected Action getAddNewAction() {
		return new CustomerRefundAction();
	}

	@Override
	protected String getAddNewLabelString() {
		if (Utility
				.isUserHavePermissions(ClientTransaction.TYPE_CUSTOMER_REFUNDS)) {
			return messages.addaNew(messages.customerRefund(Global.get()
					.Customer()));
		}
		return null;
	}

	@Override
	protected String getListViewHeading() {
		return messages.getCustomersRefundListViewHeading(Global.get()
				.Customer());
	}

	@Override
	protected void initGrid() {
		grid = new CustomerRefundListGrid(false);
		grid.init();

	}

	@Override
	public void initListCallback() {
		onPageChange(0, getPageSize());
	}

	@Override
	public void updateInGrid(CustomerRefundsList objectTobeModified) {
		// its not using any where

	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.notIssued());
		listOfTypes.add(messages.issued());
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		listOfTypes.add(messages.drafts());
		return listOfTypes;
	}

	private int checkViewType(String view) {
		if (view.equalsIgnoreCase(messages.notIssued())) {
			setViewId(STATUS_NOT_ISSUED);
		} else if (view.equalsIgnoreCase(messages.voided())) {
			setViewId(VIEW_VOIDED);
		} else if (view.equalsIgnoreCase(messages.issued())) {
			setViewId(STATUS_ISSUED);
		} else if (view.equalsIgnoreCase(messages.all())) {
			setViewId(TYPE_ALL);
		} else if (view.equalsIgnoreCase(messages.drafts())) {
			setViewId(VIEW_DRAFT);
		}

		return getViewId();
	}

	@Override
	protected void onPageChange(int start, int length) {
		setViewId(checkViewType(getViewType()));
		Accounter.createHomeService().getCustomerRefundsList(
				getStartDate().getDate(), getEndDate().getDate(), getViewId(),
				this);
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		checkViewType(text);
		onPageChange(start, getPageSize());
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// its not using any where

	}

	@Override
	protected String getViewTitle() {
		return messages.customerRefunds(Global.get().Customer());
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		Accounter.createExportCSVService().getCustomerRefundsListExportCsv(
				getStartDate().getDate(),
				getEndDate().getDate(),
				getViewId(),
				getExportCSVCallback(messages.customerRefund(Global.get()
						.Customer())));
	}

	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}
}
