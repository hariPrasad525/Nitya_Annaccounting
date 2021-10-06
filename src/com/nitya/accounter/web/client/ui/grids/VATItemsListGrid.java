/**
 * 
 */
package com.nitya.accounter.web.client.ui.grids;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.vat.NewVatItemAction;

/**
 * @author Murali.A
 * 
 */
public class VATItemsListGrid extends BaseListGrid<ClientTAXItem> {

	/**
	 * @param isMultiSelectionEnable
	 */
	public VATItemsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("VATItemsListGrid");
	}

	/*
	 * @see com.nitya.accounter.web.client.ui.grids.BaseListGrid#setColTypes()
	 */
	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 50;
		if (index == 5) {
			return 15;
		}
		if (index == 1)
			return 180;
		if (index == 4)
			return 70;
		if (index == 2)
			return 180;
		if (index == 3)
			return 275;

		return -1;
	}

	/*
	 * @see com.nitya.accounter.web.client.ui.grids.CustomTable#getColumns()
	 */
	@Override
	protected String[] getColumns() {
		return new String[] { messages.active(), messages.product(),
				messages.vatAgency(), messages.description(), messages.rate(),
				"" };
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.grids.ListGrid#getColumnValue(java
	 * .lang.Object, int)
	 */
	@Override
	protected Object getColumnValue(ClientTAXItem item, int index) {
		switch (index) {
		case 0:
			return item.isActive();
		case 1:
			return item.getName() != null ? item.getName() : "";
		case 2:
			ClientTAXAgency agency = null;
			if (item.getTaxAgency() != 0) {
				agency = getCompany().getTaxAgency(item.getTaxAgency());
			}
			return agency != null ? agency.getName() : "";
		case 3:
			return item.getDescription() != null ? item.getDescription() : "";
		case 4:
			if (item.isPercentage())
				return item.getTaxRate() + "%";
			else
				return DataUtils.amountAsStringWithCurrency(item.getTaxRate(),
						getCompany().getPrimaryCurrency());
		case 5:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		}
		return "";
	}

	@Override
	protected void onClick(ClientTAXItem obj, int row, int col) {
		if (!Utility.isUserHavePermissions(AccounterCoreType.TAXITEM)) {
			return;
		}
		List<ClientTAXItem> records = getRecords();
		if (col == 5)
			showWarnDialog(records.get(row));
	}

	protected void executeDelete(ClientTAXItem item) {
		deleteObject(item);
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.grids.ListGrid#onDoubleClick(java
	 * .lang.Object)
	 */
	@Override
	public void onDoubleClick(ClientTAXItem obj) {
		if (!Utility.isUserHavePermissions(AccounterCoreType.TAXITEM)) {
			return;
		}
		new NewVatItemAction().run(obj, false);
	}

	@Override
	protected int sort(ClientTAXItem obj1, ClientTAXItem obj2, int index) {
		switch (index) {
		case 1:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());

		case 2:
			String agency1 = null;
			String agency2 = null;
			// if (FinanceApplication.getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_US) {
			// agency1 = getTaxAgency(obj1);
			// agency2 = getTaxAgency(obj2);
			// }
			agency1 = getVATAgencyID(obj1);
			agency2 = getVATAgencyID(obj2);
			return agency1.toLowerCase().compareTo(agency2.toLowerCase());
		case 3:
			String desc1 = obj1.getDescription() != null ? obj1
					.getDescription() : "";
			String desc2 = obj2.getDescription() != null ? obj2
					.getDescription() : "";
			;
			return desc1.toLowerCase().compareTo(desc2.toLowerCase());

		case 4:
			Double rate1 = obj1.getTaxRate();
			Double rate2 = obj2.getTaxRate();
			return rate1.compareTo(rate2);

		}
		return 0;
	}

	// private String getTaxAgency(ClientVATItem obj) {
	// ClientTaxAgency agency = null;
	//
	// if (obj.getVatAgency() != null) {
	// if (FinanceApplication.getCompany().getAccountingType() ==
	// ClientCompany.ACCOUNTING_TYPE_US) {
	// agency = FinanceApplication.getCompany().getTaxAgency(
	// obj.getVatAgency());
	// }
	//
	// }
	// return agency != null ? agency.getName() : "";
	//
	// }

	private String getVATAgencyID(ClientTAXItem obj) {

		ClientTAXAgency agency = null;
		if (obj.getTaxAgency() != 0) {
			if (getPreferences().isTrackTax()) {
				agency = getCompany().getTaxAgency(obj.getTaxAgency());
			}
		}
		return agency != null ? agency.getName() : "";

	}

	public AccounterCoreType getType() {
		return AccounterCoreType.TAXITEM;
	}

	@Override
	public void addData(ClientTAXItem obj) {
		super.addData(obj);
		((CheckBox) this.getWidget(currentRow, 0)).setEnabled(false);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getTableRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(AccounterExceptions.accounterMessages
					.vatItemInUse());
			return;
		}
		super.deleteFailed(caught);
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "active", "product", "vatagency", "description",
				"rate", "col-last" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "active-value", "product-value",
				"vatagency-value", "description-value", "rate-value",
				"col-last-value" };
	}

}
