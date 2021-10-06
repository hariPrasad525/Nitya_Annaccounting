package com.nitya.accounter.web.client.ui;
//package com.nitya.accounter.web.client.ui;
//
//import java.util.Date;
//
//import com.google.gwt.layout.client.Layout.Alignment;
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.nitya.accounter.web.client.core.ClientTaxAgency;
//import com.nitya.accounter.web.client.core.ClientTaxCode;
//import com.nitya.accounter.web.client.ui.combo.CustomCombo;
//import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
//import com.nitya.accounter.web.client.ui.combo.SelectItemType;
//import com.nitya.accounter.web.client.ui.core.Accounter;
//import com.nitya.accounter.web.client.ui.core.BaseDialog;
//import com.nitya.accounter.web.client.ui.core.RecordAddhandler;
//import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
//import com.nitya.accounter.web.client.ui.forms.DateItem;
//import com.nitya.accounter.web.client.ui.forms.DynamicForm;
//import com.nitya.accounter.web.client.ui.forms.TextItem;
//
///**
// * 
// * @author G.Ravi Kiran
// * 
// * 
// */
//public class AddEditSalesTaxCodeDialog extends BaseDialog {
//
//	static final String ATTR_RATE = "rate";
//	static final String ATTR_AS_OF = "asof";
//
//	TextItem taxCodeText;
//	TextItem descriptionText;
////	TaxAgencyCombo taxAgencyCombo;
//	CustomCombo taxAgencyCombo;
//	// TaxCode takenTaxCode;
//
//	CheckboxItem statusCheck;
//	DynamicForm taxCodeForm;
//	ListGridView gridView;
//	private ClientTaxAgency selectedTaxAgency;
//	private ClientTaxCode selectedTaxCode;
//
//	boolean isRateInValid;
//
//	public AddEditSalesTaxCodeDialog(String title, String description,
//			ClientTaxCode selectedTaxCode) {
//
//		super(title, description);
//		this.selectedTaxCode = selectedTaxCode;
//
//		initTaxAgencyCombo();
//		createControls();
//
//	}
//
//	private void initTaxAgencyCombo() {
//		taxAgencyCombo.initCombo(FinanceApplication.getCompany().getTaxAgencies());
//		selectedTaxAgency = (ClientTaxAgency) taxAgencyCombo.getSelectedValue();
//		if (selectedTaxCode != null) {
//			selectedTaxAgency = FinanceApplication.getCompany().getTaxAgency(selectedTaxCode.getTaxAgency());
//			taxAgencyCombo.setComboItem(selectedTaxCode.getTaxAgency());
//
//		}
//		
//
//	}
//
//	private void createControls() {
//		setWidth("400");
////		setPageTop(10);
//		taxCodeText = new TextItem();
//		taxCodeText.setTitle("Tax code");
//		taxCodeText.setRequired(true);
//
//		descriptionText = new TextItem();
//		descriptionText.setTitle("Description");
//		taxAgencyCombo = new CustomCombo("Tax Agency", SelectItemType.TAX_AGENCY, true);
//		taxAgencyCombo.setRequired(true);
//
//		taxAgencyCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler() {
//
//					public void selectedComboBoxItem(IsSerializable selectItem) {
//
//						selectedTaxAgency = (ClientTaxAgency) selectItem;
//
//					}
//				});
//
//		statusCheck = new CheckboxItem("Active");
//		statusCheck.setValue(true);
//
//		taxCodeForm = new DynamicForm();
//		taxCodeForm = UIUtils.form("Tax Code");
//		taxCodeForm.setFields(taxCodeText, descriptionText, taxAgencyCombo,
//				statusCheck);
//
////		taxCodeForm.setHeight100();
//
//		Label taxRates = new Label(
//				"<div style='font-weight:bold;margin-top:10px;'>Tax Rates</div>");
////		taxRates.setAutoFit(true);
////		taxRates.setWrap(false);
////		taxRates.setMargin(10);
//
//		initListGrid();
//		StyledPanel bodyLayout = new StyledPanel();
//		bodyLayout.add(taxCodeForm);
//		bodyLayout.add(taxRates);
//		bodyLayout.add(gridView);
//
//		setBodyLayout(bodyLayout);
//
//	}
//
//	private void initListGrid() {
//		gridView = new ListGridView(ListGridView.NON_TRANSACTIONAL);
//		gridView.addRecordAddHandler(new RecordAddhandler() {
//
//			public boolean onRecordAdd(ListGridRecord record) {
//				if (record != null
//						&& record.getAttributeAsDate(ATTR_AS_OF) == null) {
//					return false;
//				}
//				return true;
//			}
//		});
//		gridView.setEnableMenu(false);
//		gridView.setHeight("20%");
//		gridView.setGroupTitle("Tax Rate");
//		ListGridField rateField = new ListGridField(ATTR_RATE, "Rate", 100);
//		rateField.setEditorType(new TextItem());
////		rateField.setAlign(Alignment.CENTER);
//
//		ListGridField asOfField = new ListGridField(ATTR_AS_OF, "As of", 100);
//		asOfField.setType(ListGridFieldType.DATE);
//		asOfField.setEditorType(new DateItem());
//		asOfField.setDateFormatter(DateDisplayFormat.TOUSSHORTDATE);
////		asOfField.setAlign(Alignment.CENTER);
//
//		gridView.setDefaultValue(ATTR_RATE, "0%");
//
//		gridView.addEditCompleteHandler(new EditCompleteHandler() {
//			public void onEditComplete(EditCompleteEvent event) {
//				ListGridRecord selectedRecord = gridView.getSelectedRecord();
//				isRateInValid = false;
//				validateRateField(selectedRecord);
//				validateDateField(selectedRecord);
//			}
//		});
//
//		gridView.addFields(rateField, asOfField);
//		if (selectedTaxCode == null) {
//			ListGridRecord record = new ListGridRecord();
//			record.setAttribute(ATTR_RATE, "0.0%");
//			record.setAttribute(ATTR_AS_OF, company.getPreferences()
//					.getStartOfFiscalYear());
//			gridView.addRecords(record);
//		}
//
//	}
//
//	// validating the "as of" field for each record
//	protected void validateDateField(ListGridRecord selectedRecord) {
//		String invalidRatealert = " ";
//		if (isRateInValid) {
//			invalidRatealert = "Tax Rate should be in the range 0% to 100%!!";
//			SC.say(invalidRatealert);
//			selectedRecord.setAttribute(ATTR_RATE, "0%");
//		}
//
//		try {
//			Date selectedRecordDate = selectedRecord
//					.getAttributeAsDate(ATTR_AS_OF);
//
//			if (selectedRecordDate != null) {
//				ListGridRecord records[] = gridView.getRecords();
//				if (records.length > 1) {
//
//					boolean isDeleted = false;
//					for (int i = 0; i < records.length; i++) {
//						if (!selectedRecord.equals(records[i])) {
//							Date date = records[i]
//									.getAttributeAsDate(ATTR_AS_OF);
//							if (date.equals(selectedRecordDate)) {
//								Accounter.showError(invalidRatealert
//										+ " Date should be unique");
//								gridView.deleteRecord(selectedRecord);
//								isDeleted = true;
//							}
//						}
//						if (isDeleted)
//							break;
//					}
//				}
//			} else {
//				Accounter.showError(invalidRatealert + " date should not be null");
//				gridView.deleteRecord(selectedRecord);
//
//			}
//		} catch (Exception e) {
//			Accounter.showError(invalidRatealert + " Invalid Date");
//			gridView.deleteRecord(selectedRecord);
//		}
//	}
//
//	// validating the "rate" field for each record
//	protected void validateRateField(ListGridRecord selectedRecord) {
//		String selectedRecordRate = selectedRecord.getAttribute(ATTR_RATE);
//		if (selectedRecordRate.endsWith("%"))
//			selectedRecordRate = selectedRecordRate.substring(0,
//					selectedRecordRate.length() - 1);
//		try {
//			Double rate = UIUtils.toDbl(selectedRecordRate);
//			if (rate < 0.00 || rate > 100.00) {
//				isRateInValid = true;
//			} else
//				selectedRecord.setAttribute(ATTR_RATE, rate + "%");
//		} catch (Exception e) {
//			isRateInValid = true;
//		}
//	}
//
//	public boolean validForm() {
//
//		boolean isGridEmpty = false;
//		if (gridView.getRecords().length == 0) {
//			Accounter.showError("Provide atleast one TaxRate!!");
//			isGridEmpty = true;
//		}
//		return taxCodeForm.validate() && !isGridEmpty;
//
//	}
//
//	public ClientTaxAgency getSelectedTaxAgency() {
//		return selectedTaxAgency;
//	}
//
//	public boolean checkLastRecord() {
//		ListGridRecord records[] = gridView.getRecords();
//		if (records[records.length - 1].getAttributeAsDate(ATTR_AS_OF) == null) {
//			Accounter.showError("Date should not be null");
//			return false;
//
//		}
//		return true;
//	}
//
//}
