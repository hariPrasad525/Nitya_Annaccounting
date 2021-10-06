package com.nitya.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Lists.PaymentsList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.SelectPaymentTypeDialog;
import com.nitya.accounter.web.client.ui.banking.WriteChecksAction;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.PayRollActions;
import com.nitya.accounter.web.client.ui.core.TransactionsListView;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.grids.PaymentsListGrid;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class PaymentListView extends TransactionsListView<PaymentsList>
		implements IPrintableView {
	private int checkType;
	// private static String DELETED = "Deleted";

	private static final int STATUS_NOT_ISSUED = 0;
	private static final int STATUS_ISSUED = 2;

	public static final int TYPE_CUSTOMER_CHECKS = 1;
	public static final int TYPE_VENDOR_CHECKS = 2;
	public static final int TYPE_WRITE_CHECKS = 3;

	public static final int TYPE_PAY_RUNS = 4;
	public static final int TYPE_PAY_EMPLOYEES = 5;

	private int type = 0;

	public PaymentListView() {
		super(messages.notIssued());
	}

	public PaymentListView(int checkType) {
		super(
				(checkType == PaymentListView.TYPE_PAY_RUNS || checkType == PaymentListView.TYPE_PAY_EMPLOYEES) ? messages
						.all() : messages.notIssued());
		this.checkType = checkType;
	}

	@Override
	protected Action getAddNewAction() {
		if (checkType == TYPE_PAY_RUNS) {
			return PayRollActions.newPayRunAction();
		} else if (checkType == PaymentListView.TYPE_PAY_EMPLOYEES) {
			return PayRollActions.newPayEmployeeAction();
		}
		if ((Accounter.getUser().canDoBanking() || Accounter.getUser()
				.canDoInvoiceTransactions())
				&& !(checkType == 0 || checkType == TYPE_ALL)) {
			return new WriteChecksAction();
		} else if ((checkType == 0 || checkType == TYPE_ALL)
				&& Accounter.getUser().canDoInvoiceTransactions()) {
			ViewManager.getInstance().showDialog(new SelectPaymentTypeDialog());
		} else {
			return null;
		}
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (checkType == TYPE_PAY_RUNS) {
			return messages.addaNew(messages.payrun());
		} else if (checkType == PaymentListView.TYPE_PAY_EMPLOYEES) {
			return messages.addaNew(messages.payEmployee());
		} else if ((Accounter.getUser().canDoBanking() || Accounter.getUser()
				.canDoInvoiceTransactions())
				&& !(checkType == 0 || checkType == TYPE_ALL)) {
			return messages.writeCheck();
		} else if ((checkType == 0 || checkType == TYPE_ALL)
				&& Accounter.getUser().canDoInvoiceTransactions()) {
			return messages.addaNewPayment();
		} else {
			return null;
		}
	}

	@Override
	protected String getListViewHeading() {
		if (checkType == TYPE_PAY_RUNS) {
			return messages.payRuns();
		} else if (checkType == PaymentListView.TYPE_PAY_EMPLOYEES) {
			return messages2.payEmployees();
		} else if (checkType == 0 || checkType == TYPE_ALL) {
			return messages.paymentsList();
		} else if (checkType == TYPE_CUSTOMER_CHECKS) {
			return messages.payeeChecks(Global.get().Customer());
		} else if (checkType == TYPE_WRITE_CHECKS) {
			return messages.otherChecks();
		} else {
			return messages.payeeChecks(Global.get().Vendor());
		}
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

		grid.setRecords(result);
		grid.sort(12, false);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

	@Override
	public void updateInGrid(PaymentsList objectTobeModified) {
		// NOTHING TO DO.
	}

	@Override
	protected void initGrid() {
		grid = new PaymentsListGrid(false, checkType);
		grid.init();
		grid.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		// FinanceApplication.createHomeService().getPaymentsList(this);
	}

	@Override
	protected java.util.List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.all());
		if (checkType != TYPE_PAY_RUNS && checkType != TYPE_PAY_EMPLOYEES) {
			listOfTypes.add(messages.notIssued());
			listOfTypes.add(messages.issued());
			listOfTypes.add(messages.drafts());
		}
		listOfTypes.add(messages.voided());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		onPageChange(start, getPageSize());
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return messages.payments();
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {

		if (getViewType().equalsIgnoreCase(messages.notIssued())) {
			type = STATUS_NOT_ISSUED;
		} else if (getViewType().equalsIgnoreCase(messages.issued())) {
			type = STATUS_ISSUED;
		} else if (getViewType().equalsIgnoreCase(messages.voided())) {
			type = VIEW_VOIDED;
		} else if (getViewType().equalsIgnoreCase(messages.all())) {
			type = TYPE_ALL;
		} else if (getViewType().equalsIgnoreCase(messages.drafts())) {
			type = VIEW_DRAFT;
		}
		if (checkType == 0 || checkType == TYPE_ALL) {
			Accounter.createHomeService().getPaymentsList(
					getStartDate().getDate(), getEndDate().getDate(), start,
					length, type, this);
		} else if (checkType == TYPE_CUSTOMER_CHECKS) {
			Accounter.createHomeService().getPayeeChecks(1,
					getStartDate().getDate(), getEndDate().getDate(), start,
					length, type, this);
		} else if (checkType == TYPE_WRITE_CHECKS) {
			Accounter.createHomeService().getPayeeChecks(0,
					getStartDate().getDate(), getEndDate().getDate(), start,
					length, type, this);

		} else if (checkType == TYPE_PAY_RUNS
				|| checkType == PaymentListView.TYPE_PAY_EMPLOYEES) {
			Accounter.createHomeService().getPayRunsList(
					getStartDate(),
					getEndDate(),
					start,
					length,
					type,
					checkType == TYPE_PAY_RUNS ? ClientTransaction.TYPE_PAY_RUN
							: ClientTransaction.TYPE_PAY_EMPLOYEE, this);
		} else {
			Accounter.createHomeService().getPayeeChecks(2,
					getStartDate().getDate(), getEndDate().getDate(), start,
					length, type, this);
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
		if (checkType == 0 || checkType == TYPE_ALL) {
			Accounter.createExportCSVService().getPaymentsListExportCsv(
					getStartDate().getDate(), getEndDate().getDate(), type,
					getExportCSVCallback(getListViewHeading()));
		} else if (checkType == TYPE_CUSTOMER_CHECKS) {
			Accounter.createExportCSVService().getPayeeChecksExportCsv(1,
					getStartDate().getDate(), getEndDate().getDate(), type,
					getExportCSVCallback(getListViewHeading()));
		} else if (checkType == TYPE_WRITE_CHECKS) {
			Accounter.createExportCSVService().getPayeeChecksExportCsv(0,
					getStartDate().getDate(), getEndDate().getDate(), type,
					getExportCSVCallback(getListViewHeading()));
		} else if (checkType == TYPE_PAY_RUNS
				|| checkType == TYPE_PAY_EMPLOYEES) {
			Accounter.createExportCSVService().getPayRunExportCsv(
					getStartDate().getDate(),
					getEndDate().getDate(),
					type,
					checkType == TYPE_PAY_RUNS ? ClientTransaction.TYPE_PAY_RUN
							: ClientTransaction.TYPE_PAY_EMPLOYEE,
					getExportCSVCallback(getListViewHeading()));
		} else {
			Accounter.createExportCSVService().getPayeeChecksExportCsv(2,
					getStartDate().getDate(), getEndDate().getDate(), type,
					getExportCSVCallback(getListViewHeading()));
		}
	}
}
