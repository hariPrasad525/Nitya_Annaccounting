package com.vimukti.accounter.web.client.ui.company;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientBuildAssembly;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class BuildAssembliesGrid extends BaseListGrid<ClientBuildAssembly> {

	public BuildAssembliesGrid() {
		super(false);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "assemblyItem", "description", "buildQuantity",
				"quantityOnHand", "reorderPoint", "delete" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "assemblyItem-value", "description-value",
				"buildQuantity-value", "quantityOnHand-value",
				"reorderPoint-value", "delete-value" };
	}

	@Override
	protected void executeDelete(ClientBuildAssembly object) {

		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly())) {
			AccounterAsyncCallback<ClientBuildAssembly> callback = new AccounterAsyncCallback<ClientBuildAssembly>() {

				@Override
				public void onException(AccounterException caught) {
				}

				@Override
				public void onResultSuccess(ClientBuildAssembly result) {
					if (result != null) {
						deleteObject(result);
					}
				}

			};
			Accounter.createGETService().getObjectById(
					AccounterCoreType.BUILD_ASSEMBLY, object.getID(), callback);
		}
	}

	@Override
	protected Object getColumnValue(ClientBuildAssembly obj, int index) {
		ClientItem item = getCompany().getItem(obj.getInventoryAssembly());
		switch (index) {
		case 0:
			return item.getName();
		case 1:
			return obj.getMemo();
		case 2:
			return obj.getQuantityToBuild();
		case 3:
			return item.getOnhandQty();
		case 4:
			return item.getReorderPoint();
		case 5:
			return Accounter.getFinanceImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientBuildAssembly obj) {
		if (isCanOpenTransactionView(0, IAccounterCore.STOCK_ADJUSTMENT)) {
			ReportsRPC.openTransactionView(
					ClientTransaction.TYPE_BUILD_ASSEMBLY, obj.getID());
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.assemblyItem(), messages.description(),
				messages.buildQuantity(), messages.quantityOnHand(),
				messages.reorderPoint(), messages.delete() };
	}

	@Override
	protected void onClick(ClientBuildAssembly obj, int row, int col) {
		if (!isCanOpenTransactionView(0, IAccounterCore.STOCK_ADJUSTMENT)) {
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
	protected void showWarnDialog(final ClientBuildAssembly object) {
		ClientItem item = getCompany().getItem(object.getInventoryAssembly());
		Accounter.showWarning(
				messages.doyouwanttoDeleteObj(messages.This() + " "
						+ item.getName() + " " + messages.buildAssembly()),
				AccounterType.WARNING, new ErrorDialogHandler() {

					@Override
					public boolean onYesClick() {
						executeDelete(object);
						return true;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onCancelClick() {
						return false;
					}
				});
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 150;
		case 1:
			return 150;
		case 2:
			return 100;
		case 3:
			return 120;
		case 4:
			return 120;
		case 5:
			return 40;

		default:
			return -1;
		}
	}
}
