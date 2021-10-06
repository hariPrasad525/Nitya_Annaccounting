package com.nitya.accounter.web.client.ui;
//package com.nitya.accounter.web.client.ui;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import com.google.gwt.user.client.rpc.AccounterAsyncCallback;
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.nitya.accounter.web.client.core.ClientTaxCode;
//import com.nitya.accounter.web.client.core.ClientTaxRates;
//import com.nitya.accounter.web.client.ui.core.Accounter;
//import com.nitya.accounter.web.client.ui.core.GroupDialog;
//import com.nitya.accounter.web.client.ui.core.GroupDialogButtonsHandler;
//import com.nitya.accounter.web.client.ui.core.InputDialogHandler;
//
///**
// * 
// * @author G.Ravi Kiran
// * 
// */
//
//public class SalesTaxCodesDialog extends GroupDialog {
//
//	private static final String ATTR_DESCRIPTION = "description";
//	private final String title = "Add or Edit Tax Code";
//	private final String description = "<b>Add or Edit Tax Code</b><br>To Enter a new tax rate, type a rate and select a date in the tax settings list";
//
//	private AddEditSalesTaxCodeDialog addEditTaxCodeDialog;
//	private GroupDialogButtonsHandler dialogButtonHandler;
//
//	private ClientTaxCode takenTaxCode;
//	private ClientTaxCode selectedTaxCode;
//
//	protected List<ClientTaxCode> taxCodesList;
//
//	private int selectedTaxCodeIndex;
//
//	public SalesTaxCodesDialog(String title, String description) {
//		super(title, description);
//		createControls();
//		addTaxCodesList();
//
//	}
//
//	private void createControls() {
//
//		ListGridField descriptionField = new ListGridField(ATTR_DESCRIPTION,
//				"Description", 80);
//		addField(descriptionField);
//
//		dialogButtonHandler = new GroupDialogButtonsHandler() {
//
//			public void onCloseButtonClick() {
//
//			}
//
//			// invokes when Add button clicked.
//			public void onFirstButtonClick() {
//				showAddEditTaxCodeDialog(null);
//			}
//
//			// invokes when Edit button clicked.
//			public void onSecondButtonClick() {
//				if ((selectedTaxCode = getSelectedTaxCode()) != null)
//					showAddEditTaxCodeDialog(selectedTaxCode);
//				else
//					Accounter.showError("Choose any Tax Code to Edit!!");
//
//			}
//
//			// invokes when Remove button clicked.
//			public void onThirdButtonClick() {
//				FinanceApplication.createCRUDService().deleteTaxCode(
//						getSelectedTaxCode().getID(),
//						new AccounterAsyncCallback<Boolean>() {
//
//							public void onException(AccounterException caught) {
//
//							}
//
//							public void onSuccess(Boolean result) {
//								if (result != null)
//									deleteSelectedRecord();
//								else
//									Accounter.showError("TaxCode cannot be deleted");
//
//							}
//						});
//
//			}
//
//		};
//		addGroupButtonsHandler(dialogButtonHandler);
//
//	}
//
//	protected ClientTaxCode getSelectedTaxCodes() {
//		for (ClientTaxCode taxCode : taxCodesList) {
//			Long id = Long.parseLong(getSelectedRecord().getAttribute("id"));
//			if (taxCode.getID() == id) {
//				return taxCode;
//			}
//		}
//		return null;
//	}
//
//	// deletes the selected record from the grid.
//	// public void deleteSelectedRecord() {
//	//
//	// if ((selectedTaxCode = getSelectedTaxCode()) != null) {
//	// super.deleteSelectedRecord();
//	// taxCodesList.remove(selectedTaxCodeIndex);
//	// deleteSeletedTaxCode(selectedTaxCode);
//	// } else
//	// Accounter.showError("Choose a record to delete");
//	// }
//
//	// deletes the selected Tax Code from the database.
//	private void deleteSeletedTaxCode(ClientTaxCode selectedTaxCode) {
//
//		AccounterAsyncCallback<Boolean> deleteTaxCodeCallback = new AccounterAsyncCallback<Boolean>() {
//
//			public void onException(AccounterException caught) {
//				// //UIUtils.log(caught.toString());
//				// Accounter.showError("Failed to remove");
//				Accounter.showError("Tax Code Participated in some Transactions");
//			}
//
//			public void onSuccess(Boolean result) {
//				ClientTaxCode deletedTaxCode;
//				if (result) {
//					Accounter.showError("Tax code removed successfully!!");
//				} else
//					Accounter.showError("Tax Code Participated in some Transactions");
//
//			}
//
//		};
//
//		FinanceApplication.createCRUDService().deleteTaxCode(selectedTaxCode.getID(), deleteTaxCodeCallback);
//
//	}
//
//	// gives the selected TaxCode from the grid.
//	protected ClientTaxCode getSelectedTaxCode() {
//
//		if (getSelectedRecord() != null) {
//			for (int i = 0; i < taxCodesList.size(); i++) {
//				Long id = UIUtils
//						.toLong(getSelectedRecord().getAttribute("id"));
//				if (taxCodesList.get(i).getID()==(id)) {
//					selectedTaxCodeIndex = i;
//					return taxCodesList.get(i);
//				}
//
//			}
//		}
//		return null;
//
//	}
//
//	// shows the child dialog(Add or Edit sales tax code dialog) either to Add
//	// new Tax Code or to Edit the existing Tax Code.
//	private void showAddEditTaxCodeDialog(ClientTaxCode taxCode) {
//
//		addEditTaxCodeDialog = new AddEditSalesTaxCodeDialog(title,
//				description, taxCode);
//
//		takenTaxCode = taxCode;
//
//		// Editing the Tax Code.
//		if (takenTaxCode != null) {
//
//			// setting taxCode text.
//			addEditTaxCodeDialog.taxCodeText.setValue(takenTaxCode.getName());
//
//			// setting description.
//			if (takenTaxCode.getDescription() != null)
//				addEditTaxCodeDialog.descriptionText.setValue(takenTaxCode
//						.getDescription());
//			addEditTaxCodeDialog.statusCheck.setValue(takenTaxCode
//					.getIsActive());
//
//			// filling the grid.
//			setGridView();
//		}
//
//		// Adding Ok-Cancel button handler
//		addEditTaxCodeDialog.addInputDialogHandler(new InputDialogHandler() {
//
//			public void onCancelClick() {
//
//			}
//
//			public boolean onOkClick() {
//
//				if (addEditTaxCodeDialog.validForm()
//						&& addEditTaxCodeDialog.checkLastRecord()) {
//					createOrEditTaxCode();
//					return true;
//				}
//				return false;
//			}
//
//		});
//
//		// shows the Add or Edit sales tax code dialog box.
//		addEditTaxCodeDialog.show();
//
//	}
//
//	// adding newly created tax Code to the list grid in this dialog.
//	private void addToGrid(ClientTaxCode taxCode) {
//
//		// adding newly created taxCode to list of Tax Codes.
//		taxCodesList.add(taxCode);
//		String description = new String();
//		ListGridRecord record = new ListGridRecord();
//		record.setAttribute("name", taxCode.getName());
//		if ((description = taxCode.getDescription()) != null)
//			record.setAttribute(ATTR_DESCRIPTION, description);
//
//		record.setAttribute("id", taxCode.getID());
//		addRecord(record);
//
//	}
//
//	// updating the list grid after editing the tax code.
//	private void updateGrid(ClientTaxCode taxCode) {
//
//		getSelectedRecord().setAttribute("name", taxCode.getName());
//		if ((taxCode.getDescription()) != null)
//			getSelectedRecord().setAttribute(ATTR_DESCRIPTION,
//					taxCode.getDescription());
//
//		getSelectedRecord().setAttribute("id", taxCode.getID());
//
//		refreshGrid();
//
//	}
//
//	// filling of the grid with the list of tax rates present the tax code
//	// object.
//	private void setGridView() {
//
//		Set<ClientTaxRates> taxRatesOfEditableTaxCode = takenTaxCode
//				.getTaxRates();
//		ListGridRecord records[] = new ListGridRecord[taxRatesOfEditableTaxCode
//				.size()];
//		int i = 0;
//
//		for (ClientTaxRates taxRate : takenTaxCode.getTaxRates()) {
//			records[i] = new ListGridRecord();
//			records[i].setAttribute(AddEditSalesTaxCodeDialog.ATTR_RATE,
//					UIUtils.toStr(taxRate.getRate()) + "%");
//			records[i++].setAttribute(AddEditSalesTaxCodeDialog.ATTR_AS_OF,
//					(taxRate.getAsOf()));
//
//		}
//		addEditTaxCodeDialog.gridView.addRecords(records);
//
//	}
//
//	// creates a new tax code OR edits an existing tax code.
//	protected void createOrEditTaxCode() {
//
//		ClientTaxCode taxCode = getTaxCode();
//		AccounterAsyncCallback<IsSerializable> taxCodeCallback = new AccounterAsyncCallback<IsSerializable>() {
//
//			public void onException(AccounterException caught) {
//				Accounter.showError("Duplication of TaxCode are not allowed...");
//			}
//
//			public void onSuccess(IsSerializable result) {
//				ClientTaxCode taxCode = (ClientTaxCode) result;
//
//				if (taxCode != null && taxCode.getID() != 0) {
//
//					if (takenTaxCode == null) {
//						addToGrid(taxCode);
////						SC
////								.say(taxCode.getName()
////										+ " is created Successfully!!");
//
//					} else {
//						Accounter.showInformation(taxCode.getName()
//								+ " is upadated Successfully!!");
//						updateGrid(taxCode);
//						updateTaxCodesList(taxCode);
//						// if (callBack != null)
//						// callBack.onSuccess(result);
//
//					}
//					updateCompany();
//
//				} else
//					onFailure(new Exception());
//
//			}
//
//		};
//
//		if (takenTaxCode == null)
//			FinanceApplication.createCRUDService().createTaxCode(taxCode,
//					taxCodeCallback);
//		else
//			FinanceApplication.createCRUDService().alterTaxCode(taxCode,
//					taxCodeCallback);
//
//	}
//
//	protected void updateTaxCodesList(ClientTaxCode updatedTaxCode) {
//		taxCodesList.add(selectedTaxCodeIndex, updatedTaxCode);
//	}
//
//	private ClientTaxCode getTaxCode() {
//
//		ClientTaxCode taxCode;
//		if (takenTaxCode != null)
//			taxCode = takenTaxCode;
//		else
//			taxCode = new ClientTaxCode();
//
//		// Setting Company
//
//		// Setting Tax Code
//		taxCode.setName(UIUtils.toStr(addEditTaxCodeDialog.taxCodeText
//				.getValue()));
//
//		// Setting description
//		Object obj = addEditTaxCodeDialog.descriptionText.getValue();
//		if (obj != null)
//			taxCode.setDescription(UIUtils.toStr(obj));
//
//		// Setting Tax Agency
//		taxCode.setTaxAgency(addEditTaxCodeDialog.getSelectedTaxAgency()
//				.getID());
//
//		// Setting status check
//		boolean isActive = (Boolean) addEditTaxCodeDialog.statusCheck
//				.getValue();
//		taxCode.setIsActive(isActive);
//
//		// Setting Tax Rates
//		Set<ClientTaxRates> allTaxRates = new HashSet<ClientTaxRates>();
//		ListGridRecord records[] = addEditTaxCodeDialog.gridView.getRecords();
//		for (ListGridRecord record : records) {
//
//			ClientTaxRates taxRate = new ClientTaxRates();
//			String rate = record
//					.getAttributeAsString(AddEditSalesTaxCodeDialog.ATTR_RATE);
//			taxRate
//					.setRate(UIUtils
//							.toDbl(rate.substring(0, rate.length() - 1)));
//			taxRate.setAsOf(record
//					.getAttributeAsDate(AddEditSalesTaxCodeDialog.ATTR_AS_OF));
//			allTaxRates.add(taxRate);
//
//		}
//
//		taxCode.setTaxRates(allTaxRates);
//
//		return taxCode;
//	}
//
//	private void addTaxCodesList() {
//		taxCodesList = FinanceApplication.getCompany().getTaxcodes();
//		ListGridRecord[] records = new ListGridRecord[taxCodesList.size()];
//		for (int i = 0; i < taxCodesList.size(); i++) {
//			records[i] = new ListGridRecord();
//			records[i]
//					.setAttribute("name", taxCodesList.get(i).getName());
//			records[i].setAttribute(ATTR_DESCRIPTION, taxCodesList.get(i)
//					.getDescription());
//			records[i].setAttribute("id", taxCodesList.get(i).getID());
//		}
//		addRecords(records);
//
//	}
//
//}
