package com.nitya.accounter.web.client.ui.settings;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientMeasurement;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.company.InventoryActions;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.core.IPrintableView;

public class MeasurementListView extends BaseListView<ClientMeasurement>
		implements IPrintableView {

	public MeasurementListView() {
		this.getElement().setId("JobListView");
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected String getViewTitle() {
		return messages.measurementList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getAllUnits(this);
	}

	@Override
	public void onSuccess(PaginationList<ClientMeasurement> result) {
		super.onSuccess(result);
		grid.sort(10, false);
	}

	@Override
	protected void initGrid() {
		viewSelect.setVisible(false);
		grid = new MeasurementsListGrid(false);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.measurementList();
	}

	@Override
	protected Action getAddNewAction() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly()))
			return InventoryActions.measurement();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly()))
			return messages.addNewMeasurement();
		else
			return "";
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		start = (Integer) viewDate.get("start");
	}

	@Override
	public void updateInGrid(ClientMeasurement objectTobeModified) {
		// TODO Auto-generated method stub
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
		Accounter.createExportCSVService().getAllUnitsExportCsv(
				getExportCSVCallback(messages.measurementList()));
	}
}
