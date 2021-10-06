/**
 * 
 */
package com.nitya.accounter.web.client.ui.vat;

import java.util.HashMap;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.grids.ManageTAXCodeListGrid;

/**
 * @author gwt
 * 
 */
public class ManageTAXCodesListView extends BaseListView<ClientTAXCode>
		implements IPrintableView {

	private List<ClientTAXCode> listOfTaxCodes;

	public ManageTAXCodesListView() {
		this.getElement().setId("ManageTAXCodesListView");
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			return new NewTAXCodeAction();
		} else {
			return null;
		}
	}

	@Override
	public void onSuccess(PaginationList<ClientTAXCode> result) {
		super.onSuccess(result);
		grid.sort(10, false);
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			return messages.addaNewTaxCode();
		} else {
			return "";
		}
	}

	@Override
	protected String getListViewHeading() {
		return messages.taxCodesList();
	}

	@Override
	protected void initGrid() {
		grid = new ManageTAXCodeListGrid(false);
		grid.addStyleName("listgrid-tl");
		grid.init();
		listOfTaxCodes = getCompany().getTaxCodes();
		filterList(true);

	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		for (ClientTAXCode taxCode : listOfTaxCodes) {
			if (isActive) {
				if (taxCode.isActive() == true)
					grid.addData(taxCode);
			} else if (taxCode.isActive() == false) {
				grid.addData(taxCode);
			}
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	@Override
	public void initListCallback() {
		filterList(isActive);
	}

	@Override
	public void updateInGrid(ClientTAXCode objectTobeModified) {

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
		return messages.vatCodeList();
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

		Accounter.createExportCSVService().getTaxCodesListExportCsv(
				viewSelect.getSelectedValue(),
				getExportCSVCallback(messages.taxCodesList()));
	}
}
