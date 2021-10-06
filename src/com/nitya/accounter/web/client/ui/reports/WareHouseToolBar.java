package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientWarehouse;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.WarehouseCombo;

public class WareHouseToolBar extends DateRangeReportToolbar {

	WarehouseCombo warehouseCombo;
	ClientWarehouse selectWareHouse = null;

	public WareHouseToolBar(AbstractReportView reportView) {
		super(reportView);
	}

	@Override
	protected void createControls() {
		warehouseCombo = new WarehouseCombo(messages.wareHouse(), false, true);
		warehouseCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientWarehouse>() {

					@Override
					public void selectedComboBoxItem(ClientWarehouse selectItem) {
						setWareHouseId(selectItem.getID());
					}
				});
		warehouseCombo.setSelectedItem(0);
		super.createControls();
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		if (selectWareHouse == null) {
			ClientWarehouse warehouse = new ClientWarehouse();
			warehouse.setName(messages.all());
			this.selectWareHouse = warehouse;
		}
		reportview
				.makeReportRequest(selectWareHouse.getID() == 0 ? 0
						: selectWareHouse.getID(), fromItem.getDate(), toItem
						.getDate());
	}

	@Override
	protected void wareHouseData() {
		if (getWareHouseId() != 0) {
			wareHouseData(Accounter.getCompany().getWarehouse(getWareHouseId()));
		} else {
			ClientWarehouse warehouse = new ClientWarehouse();
			warehouse.setName(messages.all());
			wareHouseData(warehouse);
		}
	}

	protected com.nitya.accounter.web.client.ui.forms.FormItem<?> getItem() {
		return warehouseCombo;
	}

	private void wareHouseData(ClientWarehouse selectWareHouse) {
		if (selectWareHouse != null) {
			this.selectWareHouse = selectWareHouse;
			ClientFinanceDate startDate = fromItem.getDate();
			ClientFinanceDate endDate = toItem.getDate();
			reportview.makeReportRequest(selectWareHouse.getID(), startDate,
					endDate);
			reportview.removeEmptyStyle();
			warehouseCombo.setSelected(selectWareHouse.getName());
		}
	}
}
