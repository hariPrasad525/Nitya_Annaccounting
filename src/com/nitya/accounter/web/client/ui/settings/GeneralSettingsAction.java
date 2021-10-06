package com.nitya.accounter.web.client.ui.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class GeneralSettingsAction extends Action {

	public GeneralSettingsAction() {
		super();
		this.catagory = messages.settings();
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public ImageResource getBigImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				GeneralSettingsView view = new GeneralSettingsView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, GeneralSettingsAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			public void onCreated() {
//				
//
//			}
//
//		});
	}

	@Override
	public String getHistoryToken() {
		return "generalSettings";
	}

	@Override
	public String getHelpToken() {
		return "settings";
	}

	@Override
	public String getText() {
		return messages.generalSettings();
	}

}
