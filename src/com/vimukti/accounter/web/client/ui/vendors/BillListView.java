package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.BillsListGrid;

/**
 * 
 * @author venki.p
 * @modified by Ravi kiran.G
 * 
 */
public class BillListView extends TransactionsListView<BillsList> implements
		IPrintableView {
	protected List<BillsList> allEnterBills;
	private int transactionType;
	int viewTypeId = 0;

	private BillListView() {
		super(messages.all());
	}

	public BillListView(String viewType) {
		super(viewType);
	}

	public BillListView(String viewType, int listType) {
		super(viewType);
		this.transactionType = listType;
	}

	public static BillListView getInstance() {
		return new BillListView();
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (transactionType == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO) {
				return new NewCreditMemoAction();
			} else if (transactionType == ClientTransaction.TYPE_CASH_PURCHASE) {
				return new NewCashPurchaseAction();
			}
			return new EnterBillsAction();
		}
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()
				&& getCompany().getPreferences().isKeepTrackofBills()) {
			if (transactionType == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO) {
				return messages.customerCreditNote(Global.get().Vendor());
			} else if (transactionType == ClientTransaction.TYPE_CASH_PURCHASE) {
				return messages.addNew(messages.cashPurchase());
			}
			return messages.addaNewBill();
		}
		return "";
	}

	@Override
	protected String getListViewHeading() {
		if (transactionType == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO) {
			return messages.payeeCreditNotes(Global.get().Vendor());
		} else if (transactionType == ClientTransaction.TYPE_CASH_PURCHASE) {
			return messages.cashPurchases();
		}
		return messages.billsAndExpenses();
	}

	@Override
	public void updateInGrid(BillsList objectTobeModified) {
	}

	@Override
	protected void initGrid() {
		grid = new BillsListGrid(false, transactionType);
		grid.init();
	}

	@Override
	public void onSuccess(PaginationList<BillsList> result) {
		allEnterBills = result;
		grid.refreshAllRecords();
		grid.removeLoadingImage();
		grid.setRecords(result);
		grid.sort(12, false);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), result.size(),
				result.getTotalCount());
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.open());
		listOfTypes.add(messages.voided());
		if (transactionType == 0) {
			listOfTypes.add(messages.overDue());
		}
		listOfTypes.add(messages.all());
		listOfTypes.add(messages.drafts());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		this.setViewType(text);
		grid.removeAllRecords();
		grid.addLoadingImagePanel();
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
		return messages.billsAndExpenses();
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {

		String text = this.getViewType();
		if (text.equalsIgnoreCase(messages.open())) {
			viewTypeId = VIEW_OPEN;
		} else if (text.equalsIgnoreCase(messages.voided())) {
			viewTypeId = VIEW_VOIDED;
		} else if (text.equalsIgnoreCase(messages.overDue())) {
			viewTypeId = VIEW_OVERDUE;
		} else if (text.equalsIgnoreCase(messages.all())) {
			viewTypeId = VIEW_ALL;
		} else if (text.equalsIgnoreCase(messages.drafts())) {
			viewTypeId = VIEW_DRAFT;
		}
		Accounter.createHomeService().getBillsAndItemReceiptList(false,
				transactionType, getStartDate().getDate(),
				getEndDate().getDate(), start, length, viewTypeId, this);
		viewSelect.setComboItem(text);
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
		Accounter.createExportCSVService().getBillsAndItemReceiptListExportCsv(
				false, transactionType, getStartDate().getDate(),
				getEndDate().getDate(), viewTypeId,
				getExportCSVCallback(getListViewHeading()));
	}
}
