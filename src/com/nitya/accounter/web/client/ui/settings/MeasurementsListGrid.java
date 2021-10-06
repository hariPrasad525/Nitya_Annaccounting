package com.nitya.accounter.web.client.ui.settings;

import java.util.List;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientMeasurement;
import com.nitya.accounter.web.client.core.ClientUnit;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.company.InventoryActions;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;

public class MeasurementsListGrid extends BaseListGrid<ClientMeasurement> {

	private List<ClientUnit> units;

	public MeasurementsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("MeasurementsListGrid");
	}

	@Override
	protected int getColumnType(int col) {
		switch (col) {
		case 0:
			return ListGrid.COLUMN_TYPE_LINK;
		case 1:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 3:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		case 4:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return 0;
		}
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.measurementName(),
				messages.measurementDescription(), messages.unitName(),
				messages.factor(), messages.delete() };
	}

	@Override
	protected void executeDelete(ClientMeasurement object) {

		AccounterAsyncCallback<ClientMeasurement> callback = new AccounterAsyncCallback<ClientMeasurement>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientMeasurement result) {
				if (result != null) {
					deleteObject(result);
				}
			}
		};
		Accounter.createGETService().getObjectById(
				AccounterCoreType.MEASUREMENT, object.getID(), callback);

	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 215;
		case 1:
			return 215;
		case 2:
			return 215;
		case 3:
			return 215;
		case 4:
			return 45;
		default:
			break;
		}
		return -1;
	}

	@Override
	protected void onClick(ClientMeasurement obj, int row, int col) {
		if (!Utility.isUserHavePermissions(IAccounterCore.MEASUREMENT)) {
			return;
		}
		switch (col) {
		case 4:
			showWarnDialog(obj);
			break;

		default:
			break;
		}
	}

	@Override
	protected Object getColumnValue(ClientMeasurement obj, int index) {
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			return obj.getDesctiption();
		case 2:
			this.units = obj.getUnits();
			return getDefaultUnitType();
		case 3:
			this.units = obj.getUnits();
			return String.valueOf(getDefaultUnitFactor());
		case 4:
			return Accounter.getFinanceMenuImages().delete();
		default:
			break;
		}
		return null;
	}

	private double getDefaultUnitFactor() {
		for (ClientUnit unit : this.units) {
			if (unit.isDefault()) {
				return unit.getFactor();
			}
		}
		return 1;
	}

	private String getDefaultUnitType() {
		for (ClientUnit unit : this.units) {
			if (unit.isDefault()) {
				return unit.getType();
			}
		}
		return "";
	}

	@Override
	public void onDoubleClick(ClientMeasurement obj) {
		if (Utility.isUserHavePermissions(IAccounterCore.MEASUREMENT)) {
			InventoryActions.measurement().run(obj, false);
		}
	}

	private boolean isUserHavePermissions(ClientMeasurement obj) {
		ClientUser user = Accounter.getUser();
		if (user.canDoInvoiceTransactions()) {
			return true;
		}

		if (user.getPermissions().getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES) {
			return true;
		}
		return false;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "measurementname", "measurementdescription",
				"unitname", "factor", "delete" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "measurementname-value",
				"measurementdescription-value", "unitname-value",
				"factor-value", "delete-value" };
	}
}
