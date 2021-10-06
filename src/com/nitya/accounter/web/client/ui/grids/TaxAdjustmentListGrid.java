package com.nitya.accounter.web.client.ui.grids;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientTAXAdjustment;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class TaxAdjustmentListGrid extends BaseListGrid<ClientTAXAdjustment> {

	public TaxAdjustmentListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("TaxAdjustmentListGrid");

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { messages.date(), messages.taxAgency(),
				messages.taxItem(), messages.adjustmentAccount(),
				messages.total(), "image-col" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { messages.date(), messages.taxAgency(),
				messages.taxItem(), messages.adjustmentAccount(),
				messages.total(), "image-col" };
	}

	@Override
	protected void onClick(ClientTAXAdjustment obj, int row, int col) {
		if (!Utility
				.isUserHavePermissions(ClientTransaction.TYPE_ADJUST_VAT_RETURN)) {
			return;
		}
		switch (col) {
		case 5:
			showWarnDialog(obj);
			break;
		default:
			break;
		}
	}

	@Override
	protected void executeDelete(ClientTAXAdjustment recordToBeDeleted) {
		AccounterAsyncCallback<ClientTAXAdjustment> callback = new AccounterAsyncCallback<ClientTAXAdjustment>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientTAXAdjustment result) {
				if (result != null) {
					deleteObject(result);

				}
			}

		};
		Accounter.createGETService()
				.getObjectById(AccounterCoreType.TAXADJUSTMENT,
						recordToBeDeleted.id, callback);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(AccounterExceptions.accounterMessages
					.payeeInUse(messages.taxAdjustment()));
			return;
		}
		super.deleteFailed(caught);
	}

	@Override
	protected Object getColumnValue(ClientTAXAdjustment obj, int index) {
		switch (index) {

		case 0:
			return obj.getDate();
		case 1:
			ClientTAXAgency taxAgency = getCompany().getTaxAgency(
					obj.getTaxAgency());
			return taxAgency != null ? taxAgency.getDisplayName() : "";
		case 2:
			ClientTAXItem taxItem = getCompany().getTaxItem(obj.getTaxItem());
			return taxItem != null ? taxItem.getDisplayName() : "";
		case 3:
			ClientAccount account = getCompany().getAccount(
					obj.getAdjustmentAccount());
			return account != null ? account.getDisplayName() : "";
		case 4:
			return DataUtils.amountAsStringWithCurrency(obj.getTotal(),
					obj.getCurrency());
		case 5:
			return Accounter.getFinanceMenuImages().delete();
		}
		return "";
	}

	@Override
	public void onDoubleClick(ClientTAXAdjustment obj) {
		if (!Utility
				.isUserHavePermissions(ClientTransaction.TYPE_ADJUST_VAT_RETURN)) {
			return;
		}
		ReportsRPC.openTransactionView(
				ClientTransaction.TYPE_ADJUST_VAT_RETURN, obj.getID());
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.date(), messages.taxAgency(),
				messages.taxItem(), messages.adjustmentAccount(),
				messages.total(), "" };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 90;
		case 1:
			return 100;
		case 2:
			return 100;
		case 3:
			return 100;
		case 4:
			return 100;
		case 5:
			return 30;
		default:
			return -1;
		}
	}
}
