package com.nitya.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientMeasurement;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class MeasurementListAction extends Action<ClientMeasurement> {

	

	public MeasurementListAction() {
		super();
		this.catagory = messages.inventory();

	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(ClientMeasurement data, boolean isDependent) {
		try {
			MeasurementListView view = new MeasurementListView();
			MainFinanceWindow.getViewManager().showView(view, data, false,
					MeasurementListAction.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "MeasurementList";
	}

	@Override
	public String getHelpToken() {
		return "MeasurementList";
	}

	@Override
	public String getText() {
		return messages.measurement();
	}

}
