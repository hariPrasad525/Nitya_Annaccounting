package com.nitya.accounter.web.client.imports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.customers.UploadCSVFileDialog;

public class UploadCSVFileDialogAction extends Action {

	public UploadCSVFileDialogAction() {
		this.catagory = messages.importFile();
	}

	@Override
	public String getText() {
		return messages.uploadCsvFileDialog();
	}

	@Override
	public void run() {
		runAsync(isDependent, data);
	}

	private void runAsync(final boolean isDependent, final Object data) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				UploadCSVFileDialog dialog = new UploadCSVFileDialog();
				ViewManager.getInstance().showDialog(dialog);

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// @Override
		// public void onCreated() {
		//
		// }
		// });

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
		return "import";
	}

	@Override
	public String getHelpToken() {
		return "import";
	}

}
