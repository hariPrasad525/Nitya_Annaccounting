package com.nitya.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Lists.BillsList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.TransactionsListView;
import com.nitya.accounter.web.client.ui.grids.BillsListGrid;

public class ExpensesListView extends TransactionsListView<BillsList> {

	public ExpensesListView() {
		super(messages.all());
	}

	public ExpensesListView(String viewType, int transactionType) {
		super(viewType);
		this.transactionType = transactionType;
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (getViewType().equals(messages.cash())) {
				return new CashExpenseAction();
			} else if (getViewType().equals(messages.creditCard())) {
				return new CreditCardExpenseAction();
			}
			return new RecordExpensesAction();
		} else {
			return null;
		}
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (getViewType() == null || getViewType().equals(messages.all())
					|| getViewType().equals(messages.voided())) {
				return messages.addNewExpense();
			}
			if (getViewType().equals(messages.cash())) {
				return messages.newcashExpenses();
			} else if (getViewType().equals(messages.creditCard())) {
				return messages.newCreditCardExpenses();
			}
		}
		return "";
	}

	@Override
	protected String getListViewHeading() {
		if (getViewType() == null || getViewType().equals(messages.all())) {
			return messages.expensesList();
		}
		if (getViewType().equals(messages.cashExpenses())) {
			return messages.cashExpensesList();
		} else if (getViewType().equals(messages.creditCardExpenses())) {
			return messages.creditCardExpensesList();
		}
		return messages.expensesList();
	}

	@Override
	protected void initGrid() {
		grid = new BillsListGrid(false);
		grid.init();
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();

		if (this.transactionType == ClientTransaction.TYPE_CREDIT_CARD_EXPENSE) {
			listOfTypes.add(messages.creditCard());
		} else if (this.transactionType == ClientTransaction.TYPE_CASH_EXPENSE) {
			listOfTypes.add(messages.cash());
		} else {
			listOfTypes.add(messages.cash());
			listOfTypes.add(messages.creditCard());
		}

		// This should be added when user select to track employee expenses.
		if (Global.get().preferences().isHaveEpmloyees()
				&& Global.get().preferences().isTrackEmployeeExpenses()) {
			listOfTypes.add(messages.employee());
		}
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		listOfTypes.add(messages.drafts());
		return listOfTypes;
	}

	int transactionType = 0;

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createHomeService().getBillsAndItemReceiptList(true,
				transactionType, getStartDate().getDate(),
				getEndDate().getDate(), start, length, checkViewType(), this);
	}

	private int checkViewType() {
		int viewId = 0;
		if (getViewType().equalsIgnoreCase(messages.cash())) {
			viewId = VIEW_OPEN;
		} else if (getViewType().equalsIgnoreCase(messages.creditCard())) {
			viewId = VIEW_OVERDUE;
		} else if (getViewType().equalsIgnoreCase(messages.voided())) {
			viewId = VIEW_VOIDED;
		} else if (getViewType().equalsIgnoreCase(messages.all())) {
			viewId = VIEW_ALL;
		} else if (getViewType().equalsIgnoreCase(messages.drafts())) {
			viewId = VIEW_DRAFT;
		}

		return viewId;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		onPageChange(start, getPageSize());
	}

	@Override
	public void onSuccess(PaginationList<BillsList> result) {
		grid.removeLoadingImage();
		viewSelect.setComboItem(getViewType());
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), result.size(),
				result.getTotalCount());
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
		// NOTHING TO DO
	}

	@Override
	public void updateInGrid(BillsList objectTobeModified) {
		// NOTHING TO DO
	}

	@Override
	protected String getViewTitle() {
		return messages.expensesList();
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void exportToCsv() {
		Accounter.createExportCSVService().getBillsAndItemReceiptListExportCSV(
				true,
				transactionType,
				getStartDate().getDate(),
				getEndDate().getDate(),
				start,
				getPageSize(),
				checkViewType(),
				getExportCSVCallback(UIUtils
						.getTransactionTypeName(transactionType)));
	}
}
