package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.imports.ImportAction;
import com.vimukti.accounter.web.client.imports.ImporterType;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class UploadCSVFileDialog extends BaseDialog {

	private FileUpload selectFileToUpload;
	private FormPanel uploadForm;
	HashMap<String, Integer> csvFileTypeListMap = new HashMap<String, Integer>();
	private ArrayList<String> list = new ArrayList<String>();
	private SelectCombo typeCombo;
	private HashMap<Integer, String> allSupportedImporters;

	@Override
	protected boolean onOK() {
		return true;
	}

	@Override
	public void setFocus() {

	}

	public UploadCSVFileDialog() {
		doCreateContents();
		this.getElement().setId("UploadCSVFileDialog");
	}

	@SuppressWarnings("deprecation")
	protected void doCreateContents() {
		setText(messages.importFromCSV());
		uploadForm = new FormPanel();
		uploadForm.setStyleName("fileuploaddialog-uploadform");
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		StyledPanel vpaPanel = new StyledPanel("vpaPanel");

		StyledPanel panel = new StyledPanel("panel");
		/* make space small */
		// Create a FileUpload widget.

		// Create list combo for type selection.
		typeCombo = new SelectCombo(messages.whatYouWantToImport());
		typeCombo.initCombo(getCSVFileList());
		typeCombo.setComboItem(Global.get().Customers());
		// typeCombo.getLabelWidget().addStyleName("bold_HTML");

		HTML detailsHtml3 = new HTML(messages.chooseACSVFileToUpload());
		detailsHtml3.addStyleName("bold_HTML");
		selectFileToUpload = new FileUpload();
		selectFileToUpload.setName("Import");

		DynamicForm form = new DynamicForm("form");
		form.add(typeCombo);

		panel.add(form);

		panel.add(detailsHtml3);
		// panel.add(selectFileToUpload);
		vpaPanel.add(panel);
		vpaPanel.add(selectFileToUpload);

		// Add a 'submit' button.
		Button uploadSubmitButton = new Button(messages.upload());
		uploadSubmitButton.getElement().setAttribute("data-icon", "upload");
		// uploadSubmitButton.setWidth("80px");
		// vpaPanel.add(uploadSubmitButton);

		Button closeButton = new Button(messages.close());
		closeButton.getElement().setAttribute("data-icon", "cancel");
		StyledPanel buttonHlay = new StyledPanel("buttonHlay");
		addButton(buttonHlay, uploadSubmitButton);
		addButton(buttonHlay, closeButton);
		buttonHlay.setStyleName("panel-right-align");
		vpaPanel.add(buttonHlay);

		/* Make align three Element on there position */
		// buttonHlay.setCellWidth(uploadSubmitButton);

		uploadSubmitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				processOnUpload();
			}

		});
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				close();
			}

		});
		uploadForm.setWidget(vpaPanel);
		uploadForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String result = /* Base64.decode( */event.getResults();
				System.out.println(event.getResults());
				// for checking the data length
				if (result.trim().length() > 2) {
					JSONValue jSONValue = JSONParser.parseLenient(result);
					JSONObject object = jSONValue.isObject();
					final String fileID = object.get("fileID").isString()
							.stringValue();
					final JSONNumber noOfRows = object.get("noOfRows")
							.isNumber();
					final double recordCount = noOfRows.doubleValue();

					final HashMap<String, ArrayList<String>> data = parseJsonArray(object);

					Accounter
							.createHomeService()
							.getFieldsOf(
									getType(),
									new AccounterAsyncCallback<ArrayList<ImportField>>() {

										@Override
										public void onException(
												AccounterException exception) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onResultSuccess(
												final ArrayList<ImportField> result) {
											close();
											new Timer() {

												@Override
												public void run() {
													ImportAction action = new ImportAction(
															result, data,
															getType(), fileID,
															recordCount);
													action.run();
												}
											}.schedule(1500);
										}
									});

				} else {
					Accounter.showInformation(messages
							.unableToUploadStatementFile());
					removeFromParent();
					return;
				}

			}

		});
		mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(uploadForm);
		add(mainPanel);
		ViewManager.getInstance().showDialog(this);
	}

	protected int getType() {
		return ImporterType.getTypeByName(typeCombo.getSelectedValue());
	}

	private ArrayList<String> getCSVFileList() {
		allSupportedImporters = (HashMap<Integer, String>) ImporterType
				.getAllSupportedImporters();
		Collection<String> keySet = allSupportedImporters.values();
		list.addAll(keySet);
		return list;
	}

	/**
	 * Returns Map<ColumnName,AllValues>
	 * 
	 * @param object
	 * @return
	 */
	protected HashMap<String, ArrayList<String>> parseJsonArray(
			JSONObject object) {
		JSONValue first20Records = object.get("first20Records");
		JSONObject jObject = first20Records.isObject();
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		for (String columnName : jObject.keySet()) {
			JSONArray array = jObject.get(columnName).isArray();
			ArrayList<String> list = new ArrayList<String>();
			for (int x = 0; x < array.size(); x++) {
				list.add(toString(array.get(x)));
			}
			map.put(columnName, list);
		}
		return map;
	}

	private String toString(JSONValue value) {

		if (value.isString() != null) {
			return value.isString().stringValue();
		} else if (value.isNumber() != null) {
			return value.isNumber().toString();
		} else if (value.isObject() != null) {
			return value.isObject().toString();
		} else if (value.isBoolean() != null) {
			return value.isBoolean().toString();
		} else {
			return value.isString().stringValue();
		}
	}

	private void processOnUpload() {
		if (!validateFile()) {
			Accounter.showInformation(messages.noFileSelected());
			return;
		}
		uploadForm.submit();
	}

	private boolean validateFile() {
		boolean fileSelected = false;
		try {
			String filename = selectFileToUpload.getFilename();

			if (filename != null && filename.length() > 0) {
				// for checking the file extension
				if (filename.endsWith(".csv")) {
					fileSelected = true;
				}
			}
			if (fileSelected) {
				uploadForm.setAction("/do/uploadImportDataFile");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return fileSelected;
	}

	protected void close() {
		onCancel();
		hide();
	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}
}
