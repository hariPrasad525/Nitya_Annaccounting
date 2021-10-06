package com.nitya.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.TransactionsListView;
import com.nitya.accounter.web.client.ui.vat.FileTAXAction;

public class FileTAXListView extends TransactionsListView<ClientTAXReturn>
		implements IPrintableView {

	private List<ClientTAXReturn> listOftaxTaxReturns;
	private int viewId;

	public FileTAXListView() {
		super(messages.open());
		isDeleteDisable = true;
	}

	@Override
	public void updateInGrid(ClientTAXReturn objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new FileTaxListGrid(this);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.fileTAX();
	}

	@Override
	protected Action getAddNewAction() {
		return new FileTAXAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.fileTAX();
	}

	@Override
	protected String getViewTitle() {
		return messages.fileTAX();
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
	public void onSuccess(PaginationList<ClientTAXReturn> result) {
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
	protected void onPageChange(int start, int length) {
		setViewId(checkViewType(getViewType()));
		Accounter.createHomeService().getTaxReturnList(
				getStartDate().getDate(), getEndDate().getDate(), start,
				length, getViewId(), this);
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.open());
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.paid());
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
		if (getViewType().equalsIgnoreCase(messages.open())) {
			setViewId(VIEW_OPEN);
		} else if (getViewType().equalsIgnoreCase(messages.voided())) {
			setViewId(VIEW_VOIDED);
		} else if (getViewType().equalsIgnoreCase(messages.all())) {
			setViewId(VIEW_ALL);
		} else if (getViewType().equalsIgnoreCase(messages.paid())) {
			setViewId(VIEW_PAID);
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
		Accounter.createExportCSVService().getFileTaxesExportCsv(
				getStartDate().getDate(), getEndDate().getDate(), viewId,
				getExportCSVCallback(messages.fileTAXList()));
	}
}
