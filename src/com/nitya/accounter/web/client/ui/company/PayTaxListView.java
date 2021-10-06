package com.nitya.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.nitya.accounter.web.client.core.ClientPayTAX;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.TransactionsListView;
import com.nitya.accounter.web.client.ui.vat.PayTAXAction;

public class PayTaxListView extends TransactionsListView<ClientPayTAX>
		implements IPrintableView {

	private List<ClientPayTAX> listOftax;
	private int viewId;

	public PayTaxListView() {
		super(messages.all());
		isDeleteDisable = true;
	}

	@Override
	public void updateInGrid(ClientPayTAX objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new PayTaxListGrid(this);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.payTax();
	}

	@Override
	protected Action getAddNewAction() {
		return new PayTAXAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.payTax();
	}

	@Override
	protected String getViewTitle() {
		return messages.payTax();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		onPageChange(0, getPageSize());
	}

	@Override
	protected void filterList(String selectedValue) {
		onPageChange(start, getPageSize());
	}

	@Override
	public void onSuccess(PaginationList<ClientPayTAX> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), result.size(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.removeLoadingImage();
		listOftax = result;
		grid.setRecords(listOftax);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), result.size(),
				result.getTotalCount());
	}

	@Override
	protected void onPageChange(int start, int length) {
		setViewId(checkViewType(getViewType()));
		Accounter.createHomeService().getPayTaxList(getStartDate().getDate(),
				getEndDate().getDate(), getViewId(), start, length, this);
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		if (getViewType() != null && !getViewType().equals("")) {
			viewSelect.setComboItem(getViewType());
		}
		return listOfTypes;
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return false;
	}

	private int checkViewType(String view) {
		if (getViewType().equalsIgnoreCase(messages.voided())) {
			setViewId(VIEW_VOIDED);
		} else if (getViewType().equalsIgnoreCase(messages.all())) {
			setViewId(VIEW_ALL);
		}

		return getViewId();
	}

	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}

	@Override
	public void exportToCsv() {
		setViewId(checkViewType(getViewType()));
		Accounter.createExportCSVService().getPayTaxesExportCsv(
				getStartDate().getDate(), getEndDate().getDate(), viewId,
				getExportCSVCallback(messages.payTAXList()));
	}
}
