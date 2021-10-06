package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class StockSettingsAction extends Action {


	public StockSettingsAction() {
		super();
		this.catagory = messages.stockSettings();
	}

	@Override
	public void run() {
		runAysnc(data, isDependent);
	}

	private void runAysnc(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				StockSettingsView view = new StockSettingsView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, StockSettingsAction.this);

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
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "stockSettings";
	}

	@Override
	public String getHelpToken() {
		return "stockSettings";
	}

	@Override
	public String getText() {
		return messages.stockSettings();
	}

}
