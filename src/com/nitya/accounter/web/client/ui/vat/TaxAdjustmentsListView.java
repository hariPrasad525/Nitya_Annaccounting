package com.nitya.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTAXAdjustment;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.TransactionsListView;
import com.nitya.accounter.web.client.ui.grids.TaxAdjustmentListGrid;

public class TaxAdjustmentsListView extends
		TransactionsListView<ClientTAXAdjustment> implements IPrintableView {

	public int viewId;
	private int length;

	public TaxAdjustmentsListView() {
		super(messages.all());
		setViewType(messages.all());
	}

	@Override
	public void updateInGrid(ClientTAXAdjustment objectTobeModified) {

	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void initGrid() {
		grid = new TaxAdjustmentListGrid(false);
		grid.init();
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.open());
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		return listOfTypes;
	}

	@Override
	protected String getListViewHeading() {
		return messages.payeeList(messages.taxAdjustment());
	}

	@Override
	protected Action getAddNewAction() {

		if (Utility
				.isUserHavePermissions(ClientTransaction.TYPE_ADJUST_VAT_RETURN)) {
			return new AdjustTAXAction(2);
		} else {
			return null;
		}
	}

	@Override
	protected void onPageChange(int start, int length) {
		this.length = length;
		setViewId(checkViewType(getViewType()));
		Accounter.createHomeService().getTaxAdjustmentsList(getViewId(),
				getStartDate().getDate(), getEndDate().getDate(), start,
				length, this);
	}

	private int checkViewType(String view) {
		if (getViewType().equalsIgnoreCase(messages.open())) {
			setViewId(VIEW_OPEN);
		} else if (getViewType().equalsIgnoreCase(messages.voided())) {
			setViewId(VIEW_VOIDED);
		} else if (getViewType().equalsIgnoreCase(messages.all())) {
			setViewId(VIEW_ALL);
		}

		return getViewId();
	}

	@Override
	protected String getAddNewLabelString() {
		if (Utility
				.isUserHavePermissions(ClientTransaction.TYPE_ADJUST_VAT_RETURN)) {
			return messages.addaNew(messages.taxAdjustment());
		}
		return null;
	}

	@Override
	protected String getViewTitle() {
		return messages.payees(messages.taxAdjustment());
	}

	@Override
	public void onSuccess(PaginationList<ClientTAXAdjustment> result) {

		if (result.isEmpty()) {
			grid.removeAllRecords();
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
	protected void filterList(String text) {
		grid.removeAllRecords();
		if (text.equalsIgnoreCase(messages.open())) {
			setViewId(VIEW_OPEN);
		} else if (text.equalsIgnoreCase(messages.voided())) {
			setViewId(VIEW_VOIDED);
		} else if (text.equalsIgnoreCase(messages.all())) {
			setViewId(VIEW_ALL);
		}
		onPageChange(start, getPageSize());
	}

	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}

	@Override
	public boolean canPrint() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		Accounter.createExportCSVService().getTaxAdjustmentsList(getViewId(),
				getStartDate().getDate(), getEndDate().getDate(), start,
				length,
				getExportCSVCallback(Global.get().messages().taxAdjustment()));
	}
}
