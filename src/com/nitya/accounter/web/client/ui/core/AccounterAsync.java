package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;

public class AccounterAsync {

	private static PopupPanel loadingMessageDialog;

	public static void createAsync(final CreateViewAsyncCallback callback) {

		loadingMessageDialog = UIUtils.getLoadingMessageDialog(Global.get()
				.messages().processingRequest());
		loadingMessageDialog.center();

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				loadingMessageDialog.removeFromParent();
				callback.onCreated();
			}

			public void onFailure(Throwable e) {
				loadingMessageDialog.removeFromParent();
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
	}

	/*
	 * public static void runAsync(final RunAsyncCallback callback) {
	 * 
	 * GWT.runAsync(new RunAsyncCallback() {
	 * 
	 * public void onSuccess() { callback.onSuccess(); }
	 * 
	 * public void onFailure(Throwable e) { callback.onFailure(e); } }); }
	 */
}
