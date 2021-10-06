package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class AddMeasurementAction extends Action<ClientMeasurement> {

	public AddMeasurementAction() {
		super();
		this.catagory = messages.inventory();
	}

	@Override
	public void run() {

		runAysnc(data, isDependent);

	}

	private void runAysnc(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AddMeasurementView view = new AddMeasurementView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, AddMeasurementAction.this);

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				AddMeasurementView view = new AddMeasurementView();
//				MainFinanceWindow.getViewManager().showView(view, data,
//						isDependent, AddMeasurementAction.this);
//
//			}
//		});

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

	// @Override
	// public ParentCanvas getView() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public String getHistoryToken() {
		return "addMeasurement";
	}

	@Override
	public String getHelpToken() {
		return "add-measurement";
	}

	@Override
	public String getText() {
		return messages.addMeasurementName();
	}

}
