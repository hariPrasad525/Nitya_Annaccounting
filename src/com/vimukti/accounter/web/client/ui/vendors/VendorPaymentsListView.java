package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.VendorPaymentsListGrid;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * @modified by Ravi kiran.G
 * 
 */

public class VendorPaymentsListView extends TransactionsListView<PaymentsList>
		implements IPrintableView {

	private int transactionType;
	private int viewType;

	public VendorPaymentsListView() {
		super(messages.notIssued());
	}

	public VendorPaymentsListView(int transactionType) {
		super(messages.notIssued());
		this.transactionType = transactionType;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Action getAddNewAction() {
		return new PayBillsAction();
	}

	@Override
	protected String getAddNewLabelString() {
		if (getPreferences().isKeepTrackofBills()
				&& Accounter.getUser().getPermissions()
						.getTypeOfPayBillsPayments() == RolePermissions.TYPE_YES) {
			return messages.addaNew(messages.payBill());
		}
		return null;
	}

	@Override
	protected String getListViewHeading() {
		return messages.payeePaymentList(Global.get().Vendor());
	}

	@Override
	public void updateInGrid(PaymentsList objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new VendorPaymentsListGrid(false, transactionType);
		grid.init();
		grid.setViewType(messages.notIssued());
	}

	@Override
	public void onSuccess(PaginationList<PaymentsList> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), grid.getTableRowCount(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.sort(10, false);
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());

	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.notIssued());
		listOfTypes.add(messages.issued());
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
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

	}

	@Override
	protected String getViewTitle() {
		return messages.payeePayments(Global.get().Vendor());
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {
		checkViewType();
		Accounter.createHomeService().getVendorPaymentsList(
				getStartDate().getDate(), getEndDate().getDate(), start,
				length, viewType, this);
	}

	public void checkViewType() {
		if (viewSelect.getSelectedValue()
				.equalsIgnoreCase(messages.notIssued())) {
			this.viewType = ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase(
				messages.issued())) {
			this.viewType = ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase(
				messages.voided())) {
			this.viewType = VIEW_VOIDED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase(
				messages.all())) {
			this.viewType = TYPE_ALL;
		}
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
		Accounter.createExportCSVService().getVendorPaymentsListExportCsv(
				getStartDate().getDate(),
				getEndDate().getDate(),
				viewType,
				getExportCSVCallback(messages.payeePayments(Global.get()
						.Vendor())));
	}
}
