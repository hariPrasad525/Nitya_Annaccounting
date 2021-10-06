package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.PurchaseOrderListGrid;

public class PurchaseOrderListView extends
		TransactionsListView<PurchaseOrdersList> implements IPrintableView {

	// private PurchaseDetailesView purchaseDetailView;
	private List<PurchaseOrdersList> listOfPurchaseOrders;

	// private static String CANCELLED = "Cancelled";

	public PurchaseOrderListView() {
		super(messages.open());
		isDeleteDisable = true;
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return new PurchaseOrderAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return messages.addNewPurchaseOrder();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return messages.purchaseOrderList();
	}

	@Override
	protected void initGrid() {
		grid = new PurchaseOrderListGrid(this);
		grid.init();
	}

	@Override
	public void updateInGrid(PurchaseOrdersList objectTobeModified) {
		// TODO currently not using anywhere

	}

	@Override
	public void fitToSize(int height, int width) {
		if (height > 0 && width - 350 > 0) {
			// grid.setHeight("100%");
			// grid.setWidth("100%");
		}

	}

	@Override
	public void onSuccess(PaginationList<PurchaseOrdersList> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), result.size(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.removeLoadingImage();
		viewSelect.setComboItem(getViewType());
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), result.size(),
				result.getTotalCount());
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.open());
		listOfTypes.add(messages.completed());
		listOfTypes.add(messages.cancelled());
		listOfTypes.add(messages.drafts());
		listOfTypes.add(messages.expired());
		listOfTypes.add(messages.all());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		onPageChange(start, getPageSize());
	}

	@Override
	protected void onPageChange(int start, int length) {
		int type = -1;
		if (getViewType().equals(messages.open())) {
			type = ClientTransaction.STATUS_OPEN;
		} else if (getViewType().equals(messages.completed())) {
			type = ClientTransaction.STATUS_COMPLETED;
		} else if (getViewType().equals(messages.cancelled())) {
			type = ClientTransaction.STATUS_CANCELLED;
		} else if (getViewType().equals(messages.drafts())) {
			type = ClientTransaction.STATUS_DRAFT;
		} else if (getViewType().equalsIgnoreCase(messages.expired())) {
			type = 6;
		}
		Accounter.createHomeService().getPurchaseOrders(
				getStartDate().getDate(), getEndDate().getDate(), type, start,
				length, this);
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// its not using any where return null;
	}

	@Override
	public boolean shouldSaveInHistory() {
		return true;
	}

	@Override
	protected String getViewTitle() {
		return messages.purchaseOrders();
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void exportToCsv() {
		int type = -1;
		if (getViewType().equals(messages.open())) {
			type = ClientTransaction.STATUS_OPEN;
		} else if (getViewType().equals(messages.completed())) {
			type = ClientTransaction.STATUS_COMPLETED;
		} else if (getViewType().equals(messages.cancelled())) {
			type = ClientTransaction.STATUS_CANCELLED;
		} else if (getViewType().equals(messages.drafts())) {
			type = ClientTransaction.STATUS_DRAFT;
		}
		Accounter.createExportCSVService().getPurchaseOrderExportCsv(type,
				getStartDate().getDate(), getEndDate().getDate(),
				getExportCSVCallback(getViewTitle()));
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}
}
