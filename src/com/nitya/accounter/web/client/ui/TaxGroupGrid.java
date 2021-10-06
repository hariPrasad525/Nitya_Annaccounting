package com.nitya.accounter.web.client.ui;

import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientTAXGroup;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.company.ManageSupportListAction;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;

public class TaxGroupGrid extends BaseListGrid<ClientTAXGroup> {

	public TaxGroupGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("TaxGroupGrid");
	}

	@Override
	protected void executeDelete(ClientTAXGroup object) {

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected Object getColumnValue(ClientTAXGroup obj, int index) {
		return obj.getName() != null ? obj.getName() : "";
	}

	@Override
	public void onDoubleClick(ClientTAXGroup obj) {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			ManageSupportListAction salesTaxGroupaction = ManageSupportListAction
					.salesTaxGroup();
			salesTaxGroupaction.run(obj, false);
		}
	}

	@Override
	protected void onClick(ClientTAXGroup obj, int row, int col) {
		if (!Utility.isUserHavePermissions(AccounterCoreType.TAX_ITEM_GROUP)) {
			return;
		}
		if (recordClickHandler != null)
			recordClickHandler.onRecordClick(obj, col);
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "name" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "name-val" };
	}

}
