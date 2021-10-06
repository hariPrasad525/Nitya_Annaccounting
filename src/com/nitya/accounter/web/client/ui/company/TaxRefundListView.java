package com.nitya.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.nitya.accounter.web.client.core.ClientReceiveVAT;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.TransactionsListView;
import com.nitya.accounter.web.client.ui.vat.ReceiveVATAction;

public class TaxRefundListView extends TransactionsListView<ClientReceiveVAT>
		implements IPrintableView {

	private List<ClientReceiveVAT> listOftaxTaxReturns;
	private int viewId;

	public TaxRefundListView() {
		super(messages.open());
		isDeleteDisable = true;
	}

	@Override
	public void updateInGrid(ClientReceiveVAT objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new TaxRefundListGrid(this);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.receiveTAX();
	}

	@Override
	protected Action getAddNewAction() {
		return new ReceiveVATAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.receiveTAX();
	}

	@Override
	protected String getViewTitle() {
		return messages.receiveTAX();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		onPageChange(0, getPageSize());
	}

	@Override
	protected void filterList(String selectedValue) {
		grid.removeAllRecords();
		if (selectedValue.equalsIgnoreCase(messages.open())) {
			setViewId(VIEW_OPEN);
		} else if (selectedValue.equalsIgnoreCase(messages.voided())) {
			setViewId(VIEW_VOIDED);
		} else if (selectedValue.equalsIgnoreCase(messages.all())) {
			setViewId(VIEW_ALL);
		}

		onPageChange(start, getPageSize());
	}

	@Override
	public void onSuccess(PaginationList<ClientReceiveVAT> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), result.size(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.removeLoadingImage();
		listOftaxTaxReturns = result;
		viewSelect.setComboItem(getViewType());
		grid.setRecords(listOftaxTaxReturns);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), result.size(),
				result.getTotalCount());
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.open());
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		if (getViewType() != null && !getViewType().equals("")) {
			viewSelect.setComboItem(getViewType());
		}
		return listOfTypes;
	}

	@Override
	protected void onPageChange(int start, int length) {
		setViewId(checkViewType(getViewType()));
		Accounter.createHomeService().getTaxRefundsList(
				getStartDate().getDate(), getEndDate().getDate(), start,
				length, getViewId(), this);
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
		if (getViewType().equalsIgnoreCase(messages.open())) {
			setViewId(VIEW_OPEN);
		} else if (getViewType().equalsIgnoreCase(messages.voided())) {
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
		Accounter.createExportCSVService().getTaxRefundsExportCsv(
				getStartDate().getDate(), getEndDate().getDate(), viewId,
				getExportCSVCallback(messages.TaxRefundList()));
	}
}
