package com.nitya.accounter.web.client.ui;

import java.util.HashMap;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.grids.ManageSalesTaxItemListGrid;
import com.nitya.accounter.web.client.ui.vat.NewVatItemAction;

public class SalesTaxItemsView extends BaseListView<ClientTAXItem> {
	private List<ClientTAXItem> listOfTaxItems;

	public SalesTaxItemsView() {
		super();
		this.getElement().setId("SalesTaxItemsView");
	}

	@Override
	protected Action getAddNewAction() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return new NewVatItemAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return messages.addaNewTaxItem();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {

		return messages.taxItemsList();
	}

	@Override
	protected void initGrid() {
		grid = new ManageSalesTaxItemListGrid(false);
		grid.addStyleName("listgrid-tl");
		grid.init();
		listOfTaxItems = getCompany().getActiveTaxItems();
		filterList(true);

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
	public void initListCallback() {
		filterList(isActive);
	}

	@Override
	public void updateInGrid(ClientTAXItem objectTobeModified) {
		// NOTHING TO DO.
	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		for (ClientTAXItem item : listOfTaxItems) {
			if (isActive) {
				if (item.isActive() == true)
					grid.addData(item);
			} else if (item.isActive() == false) {
				grid.addData(item);
			}

		}

	}

	@Override
	public void onSuccess(PaginationList<ClientTAXItem> result) {
		super.onSuccess(result);
		grid.sort(10, false);
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
	protected String getViewTitle() {
		String constant;
		if (Accounter.getUser().canDoInvoiceTransactions())
			constant = messages.manageSalesItems();
		else
			constant = messages.salesTaxItems();
		return constant;
	}

}
