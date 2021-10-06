package com.vimukti.accounter.web.client.imports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.ImporterDialog;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class ImportView extends AbstractBaseView {

	/**
	 * Contains Map<Column,AllValues>
	 */
	private HashMap<String, ArrayList<String>> importData = new HashMap<String, ArrayList<String>>();
	private int importType;
	/**
	 * All Fields of the CurrentImporter
	 */
	private HashMap<String, ImportField> fields = new HashMap<String, ImportField>();

	private FlexTable mappingTable, previewTable, mainTable;
	// contains accounter names and fields..
	private HashMap<String, ImportField> listBoxMap;

	private int currentLine = 0;
	private String fileID;
	private double noOfRecords;
	Button backButton;
	Button nextButton;
	private Label dateFormat_Label;
	private ListBox dateFormatList;
	private String selectedDateFormate;
	private int size;

	public ImportView(int importType, String fileID,
			ArrayList<ImportField> importerFields,
			HashMap<String, ArrayList<String>> data, double recordCount) {
		this.importType = importType;
		this.importData = data;
		this.fileID = fileID;
		this.noOfRecords = recordCount;
		for (ImportField field : importerFields) {
			fields.put(field.getDesplayName(), field);
		}

		this.getElement().setId("import-preview-panel");
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		mappingTable = new FlexTable();
		mainTable = new FlexTable();
		mainTable.getElement().setId("mainTable");

		previewTable = new FlexTable();
		previewTable.getElement().setId("previewTable");

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setStyleName("scrollPanel");
		scrollPanel.add(previewTable);

		mappingTable.addStyleName("import-match-content");
		// previewTable.addStyleName("import-match-content");
		// importerMatchingPanel.addStyleName("import-mapping-panel");
		StyledPanel buttonPanel = new StyledPanel("buttonPanel");

		Label header = new Label();

		backButton = new Button(messages.back());
		nextButton = new Button(messages.next());
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentLine--;
				updateColumnValues();
			}
		});

		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentLine++;
				updateColumnValues();
			}
		});
		backButton.addStyleName("prev_button");
		nextButton.addStyleName("next_button");
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.addStyleName("import-button-panel");

		StyledPanel datePanel = new StyledPanel("datePanel");
		dateFormat_Label = new Label(messages.DateFormat());
		dateFormatList = new ListBox(false);
		String[] dateFormates = new String[] { "ddMMyy", "MM/dd/yy",
				"dd/MM/yy", "ddMMyyyy", "MMddyyyy", "MMM-dd-yy", "MMMddyyyy",
				"dd/MM/yyyy", "MM/dd/yyyy", "dd/MMMM/yyyy", "MMMMddyyyy",
				"dd-MM-yyyy", "MM-dd-yyyy", "dd/MMM/yyyy", "MMM/dd/yyyy", };

		for (int i = 0; i < dateFormates.length; i++) {
			dateFormatList.addItem(dateFormates[i]);
		}

		dateFormatList.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				selectedDateFormate = getSelectedDateFormat(dateFormatList
						.getSelectedIndex());
			}
		});
		dateFormatList.setSelectedIndex(2);
		selectedDateFormate = getSelectedDateFormat(2);
		datePanel.add(dateFormat_Label);
		datePanel.add(dateFormatList);
		StyledPanel mappingTablePanel = new StyledPanel("mappingTablePanel");
		mappingTablePanel.add(mappingTable);
		mappingTablePanel.add(datePanel);

		StyledPanel previewPanel = new StyledPanel("previewPanel");
		Label previewHeader = new Label(messages.mappingPreview());
		previewHeader.addStyleName("preview-header");
		previewPanel.add(previewHeader);
		previewPanel.add(scrollPanel);
		previewPanel.addStyleName("import-match-content");
		// Save & Import buttons
		StyledPanel buttonPanel2 = new StyledPanel("buttonPanel2");

		ImageButton saveButton = new ImageButton(messages.save(), Accounter
				.getFinanceImages().saveAndClose(), "save");
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listBoxMap.size() > 0) {
					saveAndUpdateView();
				} else {
					Accounter.showError(messages.pleaseMapAtLeastOneField());
				}
			}
		});
		CancelButton cancelButton = new CancelButton(this);
		buttonPanel2.add(saveButton);
		buttonPanel2.add(cancelButton);
		buttonPanel2.addStyleName("import-button-panel");

		StyledPanel importHeaderPanel = new StyledPanel("importHeaderPanel");
		String importerName = ImporterType.getAllSupportedImporters().get(
				new Integer(importType));
		Label importerTitle = new Label(messages.importFile() + " "
				+ importerName);
		importerTitle.addStyleName("preview-header");
		importHeaderPanel.add(importerTitle);
		mainTable.setWidget(1, 0, header);
		mainTable.setWidget(1, 0, importHeaderPanel);
		mainTable.setWidget(2, 0, buttonPanel);
		mainTable.setWidget(3, 0, mappingTablePanel);
		mainTable.setWidget(3, 1, previewPanel);
		mainTable.setWidget(4, 1, buttonPanel2);
		add(mainTable);
		importHeaderPanel.addStyleName("import-preview-panel");
		previewPanel.addStyleName("import-preview-panel");
		initMappingGUI();
		refreshPreviewGUI();
		showOrHideButtons();
	}

	protected void close() {
		this.removeFromParent();
	}

	private void updateColumnValues() {
		HashMap<String, String> nextLine = (HashMap<String, String>) getCurrentLine();
		int row = 1;
		if (!nextLine.isEmpty()) {
			for (Entry<String, String> entry : nextLine.entrySet()) {
				Label label = (Label) mappingTable.getWidget(row, 1);
				label.setText(entry.getValue());
				row++;
			}
		} else {
			currentLine = 0;
		}
		refreshPreviewGUI();
		showOrHideButtons();
	}

	private void initMappingGUI() {

		listBoxMap = new HashMap<String, ImportField>();
		Label csvHeader = new Label(messages.nameInCSV());
		Label valueHeader = new Label(messages.fieldValue());
		Label accounterNameHeader = new Label(messages.accounterField());

		csvHeader.addStyleName("table-header");
		valueHeader.addStyleName("table-header");
		accounterNameHeader.addStyleName("table-header");

		mappingTable.setWidget(0, 0, csvHeader);
		mappingTable.setWidget(0, 1, valueHeader);
		mappingTable.setWidget(0, 2, accounterNameHeader);
		int objectIndex = 0;
		HashMap<String, String> map = (HashMap<String, String>) getCurrentLine();
		for (final Entry<String, String> entry : map.entrySet()) {
			Label columnName = new Label(entry.getKey());
			Label columnValue = new Label(entry.getValue());
			final ListBox importerFields = new ListBox();
			importerFields.addItem(messages.unassigned());
			for (String f : fields.keySet()) {
				importerFields.addItem(f);
			}
			importerFields.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					int selectedIndex = importerFields.getSelectedIndex();
					String selectedVal = importerFields
							.getItemText(selectedIndex);
					ImportField tempField = fields.get(selectedVal);
					String columnName = entry.getKey();
					for (ImportField field : fields.values()) {
						if (field.getColumnName() != null
								&& !(field.getColumnName().isEmpty())) {

							/*
							 * In one list box, we are not allow multiple
							 * columns having same value.
							 */
							if (field.getColumnName().equals(columnName)) {
								field.setColumnName(null);
								if (listBoxMap.containsKey(columnName)) {
									listBoxMap.remove(columnName);
								}
							}

						}
					}
					// tempField.setColumnName(entry.getKey());
					validateImportSelection(selectedVal, selectedIndex,
							tempField, importerFields, entry.getValue(),
							columnName);
					refreshPreviewGUI();
				}
			});
			mappingTable.setWidget(objectIndex + 1, 0, columnName);
			mappingTable.setWidget(objectIndex + 1, 1, columnValue);
			mappingTable.setWidget(objectIndex + 1, 2, importerFields);
			objectIndex++;
		}
	}

	/**
	 * get selected date format
	 * 
	 * @param index
	 *            of combo Box
	 */
	private String getSelectedDateFormat(int i) {
		switch (i) {
		case 0:
			return "ddMMyy";

		case 1:
			return "MM/dd/yy";

		case 2:
			return "dd/MM/yy";

		case 3:
			return "ddMMyyyy";

		case 4:
			return "MMddyyyy";

		case 5:
			return "MMM-dd-yy";

		case 6:
			return "MMMddyyyy";

		case 7:
			return "dd/MM/yyyy";

		case 8:
			return "MM/dd/yyyy";

		case 9:
			return "dd/MMMM/yyyy";

		case 10:
			return "MMMMddyyyy";

		case 11:
			return "dd-MM-yyyy";

		case 12:
			return "MM-dd-yyyy";

		case 13:
			return "dd/MMM/yyyy";

		case 14:
			return "MMM/dd/yyyy";

		}
		return null;
	}

	protected void refreshListBoxes(String selectedVal, ImportField tempField,
			int selectedIndex, ListBox selectedBox) {
		for (int i = 1; i <= importData.values().size(); i++) {
			ListBox listBox = (ListBox) mappingTable.getWidget(i, 2);
			if (listBox != selectedBox
					&& listBox.getSelectedIndex() == selectedIndex) {
				listBox.setSelectedIndex(0);
			}
		}
		refreshPreviewGUI();
		selectedBox.setSelectedIndex(selectedIndex);
	}

	protected void validateImportSelection(final String fieldName,
			final int selectedIndex, final ImportField field,
			final ListBox importerFields, final String columnValue,
			final String columnName) {
		if (listBoxMap.containsKey(fieldName)) {
			Accounter.showWarning(messages.aleadyMapped(fieldName),
					AccounterType.WARNING, new ErrorDialogHandler() {

						@SuppressWarnings("unchecked")
						@Override
						public boolean onYesClick() {
							if (validateFieldValue(fieldName, field,
									columnValue, columnName)) {
								refreshListBoxes(fieldName,
										(ImportField) field, selectedIndex,
										importerFields);
							} else {
								refreshPreviewGUI();
								importerFields.setSelectedIndex(0);
							}
							return true;
						}

						@Override
						public boolean onNoClick() {
							importerFields.setSelectedIndex(0);
							return true;
						}

						@Override
						public boolean onCancelClick() {
							return false;
						}
					});
		} else {
			if (!validateFieldValue(fieldName, field, columnValue, columnName)) {
				importerFields.setSelectedIndex(0);
			}
		}
	}

	protected boolean validateFieldValue(String fieldName, ImportField field,
			String columnValue, String columnName) {
		if (field.isFinanceDate()) {
			((FinanceDateField) field).setDateFormate(this.selectedDateFormate);
		}
		if (field.validate(columnValue)) {
			refreshMapByValue(fieldName, field);
			listBoxMap.put(fieldName, field);
			ImportField localField = fields.get(fieldName);
			localField.setColumnName(columnName);
			return true;
		} else {

			Accounter.showError(messages.matchedWithWrongProprty(fieldName,
					field.getValueAsString()));
			return false;
		}
	}

	private void refreshMapByValue(String fieldName, ImportField field) {
		for (Entry<String, ImportField> entry : listBoxMap.entrySet()) {
			if (field != null && field.getColumnName() != null && entry != null
					&& entry.getValue() != null) {
				if (field.getColumnName().equals(
						entry.getValue().getColumnName())) {
					listBoxMap.remove(entry.getKey());
					return;
				}
			}

		}

	}

	private void refreshPreviewGUI() {
		int row = 0;
		HashMap<String, String> currentLine = (HashMap<String, String>) getCurrentLine();
		for (ImportField field : fields.values()) {
			Label fieldNameLabel = new Label(field.getDesplayName());
			String string = currentLine.get(field.getColumnName());
			Label valLabel = new Label(string != null ? string
					: messages.unassigned());
			previewTable.setWidget(row, 0, fieldNameLabel);
			previewTable.setWidget(row, 1, valLabel);
			row++;
		}

	}

	private HashMap<String, String> getCurrentLine() {
		HashMap<String, String> map = new HashMap<String, String>();
		for (Entry<String, ArrayList<String>> entrySet : importData.entrySet()) {
			String key = entrySet.getKey();
			List<String> value = entrySet.getValue();
			map.put(key, value.size() > currentLine ? value.get(currentLine)
					: "");
		}
		return map;
	}

	@Override
	public void saveAndUpdateView() {
		if (!listBoxMap.isEmpty()) {
			HashMap<String, String> importMap = (HashMap<String, String>) updateImport();
			Accounter.createHomeService().importData(this.fileID, importType,
					importMap, this.selectedDateFormate,
					new AccounterAsyncCallback<HashMap<Integer, Object>>() {

						@Override
						public void onException(AccounterException exception) {
							if (exception.getErrorCode() == AccounterException.ERROR_NAME_CONFLICT) {
								Accounter.showError(messages
										.objAlreadyExistsWithName(ImporterType
												.getAllSupportedImporters()
												.get(importType)));
								return;
							}
							String errorString = AccounterExceptions
									.getErrorString(exception);
							Accounter.showError(errorString);
						}

						@Override
						public void onResultSuccess(
								final HashMap<Integer, Object> result) {
							size = result.size();
							ImporterDialog dialog = new ImporterDialog(messages
									.importerInformation(), result) {
								@Override
								protected void closeView() {
									if (size <= 1) {
										ImportView.this.getManager()
												.closeCurrentView();
									}
								}
							};
							ViewManager.getInstance().showDialog(dialog);
						}
					});
			super.saveAndUpdateView();
		} else {
			Accounter.showError(messages.pleaseMapAtLeastOneField());
		}
	}

	private HashMap<String, String> updateImport() {
		HashMap<String, String> map = new HashMap<String, String>();
		for (ImportField field : fields.values()) {
			map.put(field.getName(), field.getColumnName());
		}
		return map;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void showOrHideButtons() {
		if (noOfRecords == 1) {
			this.nextButton.setVisible(false);
			this.backButton.setVisible(false);
		} else if (noOfRecords > 1 && currentLine == 0) {
			this.nextButton.setVisible(true);
			this.backButton.setVisible(false);
		} else if (currentLine > 0 && noOfRecords > 1
				&& currentLine + 1 == noOfRecords) {
			this.nextButton.setVisible(false);
			this.backButton.setVisible(true);
		} else {
			this.nextButton.setVisible(true);
			this.backButton.setVisible(true);
		}
	}
}
