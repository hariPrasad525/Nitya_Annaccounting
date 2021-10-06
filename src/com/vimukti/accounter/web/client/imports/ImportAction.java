package com.vimukti.accounter.web.client.imports;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ImportAction extends Action {

	private HashMap<String, ArrayList<String>> columnData;
	private int importType;
	private ArrayList<ImportField> importerFields;
	private String fileID;
	private double noOfRows;

	public ImportAction(ArrayList<ImportField> importerFields,
			HashMap<String, ArrayList<String>> data, int type, String fileID,
			double noOfRows) {
		this.columnData = data;
		this.importType = type;
		this.importerFields = importerFields;
		this.fileID = fileID;
		this.catagory = messages.importFile();
		this.noOfRows = noOfRows;
	}

	@Override
	public String getText() {
		return messages.importFile();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ImportView view = new ImportView(importType, fileID,
						importerFields, columnData, noOfRows);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ImportAction.this);

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
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
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
