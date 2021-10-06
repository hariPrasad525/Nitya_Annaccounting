package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.PaymentDialogAction;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.ReceivedPaymentListGrid;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * 
 * @modified Fernandez
 * 
 */
public class ReceivedPaymentListView extends
		TransactionsListView<ReceivePaymentsList> implements IPrintableView {
	private int transactionType;

	public ReceivedPaymentListView() {
		super(messages.paid());
	}

	public ReceivedPaymentListView(int type) {
		super(messages.paid());
		this.transactionType = type;
	}

	@Override
	protected Action getAddNewAction() {
		if (transactionType == ClientTransaction.TYPE_RECEIVE_PAYMENT) {
			return new ReceivePaymentAction();
		} else if (transactionType == ClientTransaction.TYPE_CUSTOMER_PREPAYMENT) {
			return new CustomerPaymentsAction();
		}
		return new PaymentDialogAction();
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().getPermissions().getTypeOfPayBillsPayments() == RolePermissions.TYPE_YES) {
			return messages.addaNewPayment();
		}
		return null;
	}

	@Override
	protected String getListViewHeading() {
		if (transactionType == ClientTransaction.TYPE_CUSTOMER_PREPAYMENT) {
			return messages.payeePayments(Global.get().Customer());
		}
		return messages.getReceivedPaymentListViewHeading();
	}

	@Override
	public void onSuccess(PaginationList<ReceivePaymentsList> result) {
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
	public void updateInGrid(ReceivePaymentsList objectTobeModified) {
	}

	@Override
	protected void initGrid() {
		grid = new ReceivedPaymentListGrid(false, transactionType);
		grid.init();

	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.all());
		listOfTypes.add(messages.paid());
		// listOfTypes.add(OPEN);
		// listOfTypes.add(FULLY_APPLIED);
		listOfTypes.add(messages.voided());
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
		return messages.recievePayments();
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {
		int type = 0;
		if (getViewType().equalsIgnoreCase(messages.paid())) {
			type = ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (getViewType().equalsIgnoreCase(messages.voided())) {
			type = VIEW_VOIDED;
		} else if (getViewType().equalsIgnoreCase(messages.all())) {
			type = VIEW_ALL;
		}
		Accounter.createHomeService().getReceivePaymentsList(
				getStartDate().getDate(), getEndDate().getDate(),
				transactionType, start, length, type, this);
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	};

	@Override
	public void exportToCsv() {
		int type = 0;
		if (getViewType().equalsIgnoreCase(messages.paid())) {
			type = ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (getViewType().equalsIgnoreCase(messages.voided())) {
			type = VIEW_VOIDED;
		} else if (getViewType().equalsIgnoreCase(messages.all())) {
			type = VIEW_ALL;
		}
		Accounter.createExportCSVService().getReceivePaymentsListExportCsv(
				getStartDate().getDate(), getEndDate().getDate(),
				transactionType, type,
				getExportCSVCallback(getListViewHeading()));
	}
}
