package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientStatement;
import com.nitya.accounter.web.client.core.ClientStatementRecord;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.banking.StatementImportViewAction;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class UploadStatementDialog extends BaseDialog implements
		AsyncCallback<PaginationList<ClientStatement>> {

	private static final String STATEMENT = "statement";
	private FileUpload fileUpload;
	private FormPanel uploadForm;
	private StyledPanel panel, mainLayout;
	private HTML detailsHtml1;
	private StyledPanel buttonHlay;
	private ArrayList<FileUpload> uploadItems = new ArrayList<FileUpload>();
	private ClientAccount account;
	private List<ClientStatement> statementsList;

	public UploadStatementDialog(String title, ClientAccount account) {
		setText(title);
		this.getElement().setId("UploadStatementDialog");
		this.account = account;
		getStatementRecordsByAccountId();
		doCreateContents();
	}

	public UploadStatementDialog() {
		this.getElement().setId("UploadStatementDialog");
	}

	/**
	 * to get all the statement records with the corresponding accountId
	 */
	private void getStatementRecordsByAccountId() {
		Accounter.createHomeService().getBankStatements(account.getID(), this);
	}

	@SuppressWarnings("deprecation")
	private void doCreateContents() {

		uploadForm = new FormPanel();
		uploadForm.setStyleName("fileuploaddialog-uploadform");
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		StyledPanel vpaPanel = new StyledPanel("vpaPanel");

		panel = new StyledPanel("panel");
		// Create a FileUpload widget.

		// for uploading the custom template files
		detailsHtml1 = new HTML("upload statement file");

		fileUpload = new FileUpload();
		fileUpload.setName(STATEMENT);

		panel.add(detailsHtml1);
		panel.add(fileUpload);
		uploadItems.add(fileUpload);

		vpaPanel.add(panel);

		// Add a 'submit' button.
		Button uploadSubmitButton = new Button(messages.save());
		uploadSubmitButton.getElement().setAttribute("data-icon", "save");
		// uploadSubmitButton.setWidth("80px");
		// vpaPanel.add(uploadSubmitButton);

		Button closeButton = new Button(messages.close());
		closeButton.getElement().setAttribute("data-icon", "cancel");
		// closeButton.setWidth("80px");
		buttonHlay = new StyledPanel("buttonHlay");
		addButton(buttonHlay, uploadSubmitButton);
		addButton(buttonHlay, closeButton);
		buttonHlay.setStyleName("panel-right-align");
		vpaPanel.add(buttonHlay);
		/* Make align three Element on there position */
		// buttonHlay.setCellWidth(uploadSubmitButton);

		uploadSubmitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				processOnUpload();
				// removeFromParent();
			}

		});
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				close();
			}

		});

		uploadForm.setWidget(vpaPanel);
		uploadForm.addFormHandler(new FormHandler() {
			@Override
			public void onSubmit(FormSubmitEvent event) {
			}

			@Override
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				StringBuilder result = new StringBuilder(event.getResults());

				String aa = result.toString().replaceAll("<pre>", " ");
				aa = aa.replaceAll("</pre>", " ");
				// for checking the data length
				if (aa.trim().length() > 2) {
					JSONValue jSONValue = JSONParser.parseLenient(aa.toString());
					JSONArray array = jSONValue.isArray();
					List<String[]> data = parseJsonArray(array);

					HashMap<String, Integer> map = compareUploadedFileRecords(data);
					int matched = map.get("matched");
					int notMatched = map.get("notMatched");
					if (notMatched != 0) {
						new StatementImportViewAction(data, account.getID())
								.run();
						removeFromParent();
					} else {
						Accounter.showInformation(messages
								.statementAlreadyImported());
						removeFromParent();
						return;
					}
				} else {
					Accounter.showInformation(messages
							.unableToUploadStatementFile());
					removeFromParent();
					return;
				}

			}

		});

		mainLayout = new StyledPanel("mainLayout");
		mainLayout.add(uploadForm);
		add(mainLayout);
		ViewManager.getInstance().showDialog(this);
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
			String filename = fileUpload.getFilename();

			if (filename != null && filename.length() > 0) {
				// for checking the file extension
				if (filename.endsWith(".csv")) {
					fileSelected = true;
				}
			}
			if (fileSelected) {
				uploadForm.setAction("/do/uploadstatementfile?id="
						+ account.getID());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return fileSelected;
	}

	public void close() {
		this.removeFromParent();
	}

	@Override
	protected boolean onOK() {
		return true;
	}

	@Override
	public void setFocus() {
	}

	/**
	 * used to convert the JsonArray to a List
	 * 
	 * @param array
	 */
	private List<String[]> parseJsonArray(JSONArray array) {
		boolean forHeadings = true;
		List<String[]> data = new ArrayList<String[]>();
		JSONArray array1 = array.isArray();
		for (int i = 0; i < array1.size(); i++) {
			JSONValue jsonValue = array1.get(i);
			JSONObject object = jsonValue.isObject();
			Set<String> keySet = object.keySet();
			String[] headings = new String[object.size()];
			if (forHeadings) {
				int jj = 0;
				for (String key : keySet) {
					headings[jj] = key.trim();
					jj++;
				}
				data.add(headings);
			}
			forHeadings = false;
			String[] values = new String[object.size()];
			for (int j = 0; j < object.size(); j++) {
				int tmp = 0;
				for (String key : keySet) {
					String value = toString(object.get(key));
					values[tmp] = value.trim();
					tmp++;
				}
			}
			data.add(values);
		}
		return data;
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

	/**
	 * for checking the imported statement file records with the previous
	 * statementRecordList
	 * 
	 * @param importedList
	 */
	private HashMap<String, Integer> compareUploadedFileRecords(
			List<String[]> importedList) {
		int matched = 0, notMatched = importedList.size() - 1;
		if (statementsList != null) {
			for (int i = 0; i < statementsList.size(); i++) {
				ClientStatement stObj = statementsList.get(i);
				List<ClientStatementRecord> stList = stObj.getStatementList();

				for (int j = 0; j < importedList.size() - 1; j++) {

					String[] fileValues = importedList.get(j + 1);
					for (ClientStatementRecord stRecord : stList) {
						String[] recValues = getStatementRecordValues(stRecord);
						boolean isMatched = compare(recValues, fileValues);
						if (isMatched) {
							importedList.remove(j + 1);
							matched++;
							j = j - 1;
							break;
						}

					}

				}

			}
		}
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("matched", matched);
		map.put("notMatched", notMatched - matched);
		return map;
	}

	private boolean compare(String[] recValues, String[] fileValues) {
		boolean contains;
		int count = 0;
		List<String> recordList = Arrays.asList(recValues);
		List<String> fileList = Arrays.asList(fileValues);
		for (int i = 0; i < fileList.size(); i++) {
			contains = recordList.contains(fileList.get(i));
			if (contains) {
				count++;
			}
		}

		if (count > 2) {
			return true;
		}
		return false;
	}

	/**
	 * returns statementRecord obj values in String[] form
	 * 
	 * @param stRecord
	 * @return
	 */
	private String[] getStatementRecordValues(ClientStatementRecord stRecord) {
		String[] values = new String[] {
				stRecord.getStatementDate().toString(),
				stRecord.getDescription(), stRecord.getReferenceNumber(),
				String.valueOf(stRecord.getSpentAmount()),
				String.valueOf(stRecord.getReceivedAmount()),
				String.valueOf(stRecord.getClosingBalance()) };
		return values;

	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(PaginationList<ClientStatement> result) {
		if (result != null && !result.isEmpty()) {
			statementsList = result;
		}
	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}
}
