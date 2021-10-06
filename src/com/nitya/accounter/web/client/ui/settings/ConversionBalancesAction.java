package com.nitya.accounter.web.client.ui.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class ConversionBalancesAction extends Action {

	public ConversionBalancesAction() {
		super();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	public void run(Object data, Boolean isDependent, String endingDate,
			String year) {
		runAsync(data, isDependent, endingDate, year);
	}

	@Override
	public void run() {
		runAsync(data, isDependent, null, null);
	}

	private void runAsync(final Object data, final Boolean isDependent,
			final String endingDate, final String year) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ConversionBalancesView view = new ConversionBalancesView(endingDate, year);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ConversionBalancesAction.this);

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
//				
//			}
//
//		});
	}

	@Override
	public String getHistoryToken() {
		return null;
	}

	@Override
	public String getHelpToken() {
		return "conversion-balance";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return messages.conversionBalance();
	}

}
