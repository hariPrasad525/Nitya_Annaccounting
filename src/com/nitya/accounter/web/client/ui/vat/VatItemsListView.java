package com.nitya.accounter.web.client.ui.vat;

import java.util.HashMap;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.grids.VATItemsListGrid;

public class VatItemsListView extends BaseListView<ClientTAXItem> implements
		IPrintableView {

	private List<ClientTAXItem> listOfVatItems;
	private int start;

	public VatItemsListView() {
		this.getElement().setId("VatItemsListView");
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			return new NewVatItemAction();
		} else {
			return null;
		}
	}

	@Override
	public void onSuccess(PaginationList<ClientTAXItem> result) {
		super.onSuccess(result);
		grid.sort(10, false);
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			return messages.addaNewTaxItem();
		} else {
			return "";
		}
	}

	@Override
	protected String getListViewHeading() {
		return messages.taxItemsList();
	}

	@Override
	protected void initGrid() {
		grid = new VATItemsListGrid(false);
		grid.addStyleName("listgrid-tl");
		grid.init();
		listOfVatItems = getCompany().getTaxItems();
		filterList(true);
	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		for (ClientTAXItem item : listOfVatItems) {
			if (isActive) {
				if (item.isActive() == true)
					grid.addData(item);
			} else if (item.isActive() == false) {
				grid.addData(item);
			}

		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedValue = viewSelect.getSelectedValue();
		if (selectedValue.equalsIgnoreCase(messages.active())) {
			isActive = true;
		} else {
			isActive = false;
		}
		map.put("isActive", isActive);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		isActive = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		if (isActive) {
			viewSelect.setComboItem(messages.active());
		} else {
			viewSelect.setComboItem(messages.inActive());
		}

	}

	@Override
	public void initListCallback() {
		filterList(isActive);
	}

	@Override
	public void updateInGrid(ClientTAXItem objectTobeModified) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		// currently not using

	}

	@Override
	public void print() {
		// currently not using

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void printPreview() {
	}

	@Override
	protected String getViewTitle() {
		return messages.vatItemList();
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
		Accounter.createExportCSVService().getTaxItemsListExportCsv(
				viewSelect.getSelectedValue(),
				getExportCSVCallback(messages.taxItemsList()));
	}
}
