package com.nitya.accounter.web.client.ui;
////package com.nitya.accounter.web.client.ui;
////
////import java.util.List;
////
////import com.google.gwt.user.client.rpc.AccounterAsyncCallback;
////import com.google.gwt.user.client.rpc.IsSerializable;
////import com.smartgwt.client.util.SC;
////import com.smartgwt.client.widgets.grid.ListGridRecord;
////import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
////import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
////import com.nitya.accounter.web.client.core.ClientItemTax;
////import com.nitya.accounter.web.client.core.IAccounterCore;
////import com.nitya.accounter.web.client.ui.core.GroupDialog;
////import com.nitya.accounter.web.client.ui.core.GroupDialogButtonsHandler;
////import com.nitya.accounter.web.client.ui.core.InputDialogHandler;
////
////public class ManageItemTaxDialog extends GroupDialog {
////	private GroupDialogButtonsHandler dialogButtonsHandler;
////	List<ClientItemTax> itemTaxes;
////	ClientItemTax itemTax, selectedItemTax;
////	private AddItemTaxDialog dialog;
////
////	public ManageItemTaxDialog(String title, String descript) {
////		super(title, descript);
////		createControls();
////		getItemTaxMethods();
////
////	}
////
////	private void getItemTaxMethods() {
//////		List<ClientItemTax> result = FinanceApplication.getCompany().getItemTaxs();
//////		if (result != null) {
//////			itemTaxes = result;
//////			ListGridRecord[] records = new ListGridRecord[result.size()];
//////			for (int i = 0; i < result.size(); i++) {
//////				records[i] = new ListGridRecord();
//////				records[i]
//////						.setAttribute("name", result.get(i).getName());
//////				records[i].setAttribute("id", result.get(i).getID());
//////			}
//////			addRecords(records);
//////		}
//////		AccounterAsyncCallback<ArrayList<ClientItemTax>> callback = new AccounterAsyncCallback<ArrayList<ClientItemTax>>() {
//////
//////			public void onException(AccounterException caught) {
//////				//UIUtils.log(caught.toString());
//////			}
//////
//////			public void onSuccess(ArrayList<ClientItemTax> result) {
//////				if (result != null) {
//////					itemTaxes = result;
//////					ListGridRecord[] records = new ListGridRecord[result.size()];
//////					for (int i = 0; i < result.size(); i++) {
//////						records[i] = new ListGridRecord();
//////						records[i]
//////								.setAttribute("name", result.get(i).getName());
//////						records[i].setAttribute("id", result.get(i).getID());
//////					}
//////					addRecords(records);
//////				}
//////
//////			}
//////		};
//////
//////		FinanceApplication.createGETService().getItemTaxes(callback);
////
////	}
////
////	private void createControls() {
////
////		getGrid().addRecordClickHandler(new RecordClickHandler() {
////
////			public void onRecordClick(RecordClickEvent event) {
////
////				disableEditRemoveButtons(false);
////
////			}
////
////		});
////
////		dialogButtonsHandler = new GroupDialogButtonsHandler() {
////
////			public void onCloseButtonClick() {
////
////			}
////
////			public void onFirstButtonClick() {
////				showAddEditDialog(null);
////
////			}
////
////			public void onSecondButtonClick() {
////				showAddEditDialog(getSelectedItemTaxes());
////
////			}
////
////			public void onThirdButtonClick() {
////				FinanceApplication.createCRUDService().deleteItemTax(
////						getSelectedItemTaxes().getID(),
////						new AccounterAsyncCallback<Boolean>() {
////
////							public void onException(AccounterException caught) {
////								//UIUtils.log(caught.toString());
////							}
////
////							public void onSuccess(Boolean result) {
////								if (result == true) {
////									deleteSelectedRecord();
////									Accounter.showError("Item Tax Deleted Successfully ");
////								} else
////									Accounter.showError("Cannot Delete Item Tax");
////							}
////						});
////
////			}
////		};
////		addGroupButtonsHandler(dialogButtonsHandler);
////
////	}
////
////	protected ClientItemTax getSelectedItemTaxes() {
////		for (ClientItemTax method : itemTaxes) {
////			Long id = Long.parseLong(getSelectedRecord().getAttribute("id"));
////			if (method.getID() == id) {
////
////				return method;
///*package com.nitya.accounter.web.client.ui;
//
//import java.util.List;
//
//import com.google.gwt.user.client.rpc.AccounterAsyncCallback;
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.smartgwt.client.util.SC;
//import com.smartgwt.client.widgets.grid.ListGridRecord;
//import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
//import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
//import com.nitya.accounter.web.client.core.ClientItemTax;
//import com.nitya.accounter.web.client.core.IAccounterCore;
//import com.nitya.accounter.web.client.ui.core.GroupDialog;
//import com.nitya.accounter.web.client.ui.core.GroupDialogButtonsHandler;
//import com.nitya.accounter.web.client.ui.core.InputDialogHandler;
//
//public class ManageItemTaxDialog extends GroupDialog {
//	private GroupDialogButtonsHandler dialogButtonsHandler;
//	List<ClientItemTax> itemTaxes;
//	ClientItemTax itemTax, selectedItemTax;
//	private AddItemTaxDialog dialog;
//
//	public ManageItemTaxDialog(String title, String descript) {
//		super(title, descript);
//		createControls();
//		getItemTaxMethods();
//
//	}
//
//	private void getItemTaxMethods() {
////		List<ClientItemTax> result = FinanceApplication.getCompany().getItemTaxs();
////		if (result != null) {
////			itemTaxes = result;
////			ListGridRecord[] records = new ListGridRecord[result.size()];
////			for (int i = 0; i < result.size(); i++) {
////				records[i] = new ListGridRecord();
////				records[i]
////						.setAttribute("name", result.get(i).getName());
////				records[i].setAttribute("id", result.get(i).getID());
//>>>>>>> 1.3.4.12
////			}
////		}
////
////		return null;
////	}
////
////	public void createItemTaxes() {
////		ClientItemTax itemTax = new ClientItemTax();
////		if (dialog.taxText.getValue() != null)
////			itemTax.setName(dialog.taxText.getValue().toString());
////		if (dialog.taxableRadio.getValue() != null) {
////			String val = dialog.taxableRadio.getValue().toString();
////			itemTax.setTaxable(val.equals("Taxable"));
////		}
////		AccounterAsyncCallback<IsSerializable> createItemCallback = new AccounterAsyncCallback<IsSerializable>() {
////
////			public void onException(AccounterException caught) {
////				//UIUtils.log(caught.toString());
////			}
////
////			public void onSuccess(IsSerializable result) {
////				if (result != null) {
////					ClientItemTax itemTx = (ClientItemTax) result;
////					itemTaxes.add(itemTx);
////					ListGridRecord record = new ListGridRecord();
////					record.setAttribute("name", dialog.taxText.getValue()
////							.toString());
////					record.setAttribute("id", itemTx.getID());
////					addRecord(record);
////					if (callBack != null) {
////						callBack.onSuccess((IAccounterCore) result);
////					}
////					dialog.destroy();
////				}
////			}
////
////		};
////		FinanceApplication.createCRUDService().createItemTax(itemTax,
////				createItemCallback);
////	}
////
////	public void showAddEditDialog(ClientItemTax rec) {
////		dialog = new AddItemTaxDialog("Item Group", "");
////
////		selectedItemTax = rec;
////		if (selectedItemTax != null) {
////			dialog.taxableRadio.setDisabled(true);
////			dialog.taxText.setValue(selectedItemTax.getName());
////			dialog.taxableRadio
////					.setValue(selectedItemTax.isTaxable() ? "Taxable"
////							: "Non-taxable");
////
////		}
////		dialog.addInputDialogHandler(new InputDialogHandler() {
////
////			public void onCancelClick() {
////
////			}
////
//<<<<<<< ManageItemTaxDialog.java
////			public boolean onOkClick() {
////				if (dialog.form.validate()) {
////					if (selectedItemTax != null) {
////
////						editItemTax();
////					} else
////						createItemTaxes();
////				} else {
////					
////					return false;
////				}
////				return true;
////			}
////
////		});
////		dialog.show();
////
////	}
////
////	protected void editItemTax() {
////
////		selectedItemTax.setName(dialog.taxText.getValue().toString());
////
////		FinanceApplication.createCRUDService().alterItemTax(selectedItemTax,
////				new AccounterAsyncCallback<IsSerializable>() {
////
////					public void onException(AccounterException caught) {
////						Accounter.showError("Failed to edit Item Tax ");
////					}
////
////					public void onSuccess(IsSerializable result) {
////						if (result != null) {
////
////							ClientItemTax itemTax = (ClientItemTax) result;
////
////							getSelectedRecord().setAttribute("name",
////									itemTax.getName());
////							refreshGrid();
////
////							Accounter.showError("Group has been Modified ");
////						}
////					}
////				});
////
////	}
////
////}
//=======
////		FinanceApplication.createGETService().getItemTaxes(callback);
//
//	}
//
//	private void createControls() {
//
//		getGrid().addRecordClickHandler(new RecordClickHandler() {
//
//			public void onRecordClick(RecordClickEvent event) {
//
//				disableEditRemoveButtons(false);
//
//			}
//
//		});
//
//		dialogButtonsHandler = new GroupDialogButtonsHandler() {
//
//			public void onCloseButtonClick() {
//
//			}
//
//			public void onFirstButtonClick() {
//				showAddEditDialog(null);
//
//			}
//
//			public void onSecondButtonClick() {
//				showAddEditDialog(getSelectedItemTaxes());
//
//			}
//
//			public void onThirdButtonClick() {
//				FinanceApplication.createCRUDService().deleteItemTax(
//						getSelectedItemTaxes().getID(),
//						new AccounterAsyncCallback<Boolean>() {
//
//							public void onException(AccounterException caught) {
//								//UIUtils.log(caught.toString());
//							}
//
//							public void onSuccess(Boolean result) {
//								if (result == true) {
//									deleteSelectedRecord();
//									Accounter.showError("Item Tax Deleted Successfully ");
//								} else
//									Accounter.showError("Cannot Delete Item Tax");
//							}
//						});
//
//			}
//		};
//		addGroupButtonsHandler(dialogButtonsHandler);
//
//	}
//
//	protected ClientItemTax getSelectedItemTaxes() {
//		for (ClientItemTax method : itemTaxes) {
//			Long id = Long.parseLong(getSelectedRecord().getAttribute("id"));
//			if (method.getID() == id) {
//
//				return method;
//			}
//		}
//
//		return null;
//	}
//
//	public void createItemTaxes() {
//		ClientItemTax itemTax = new ClientItemTax();
//		if (dialog.taxText.getValue() != null)
//			itemTax.setName(dialog.taxText.getValue().toString());
//		if (dialog.taxableRadio.getValue() != null) {
//			String val = dialog.taxableRadio.getValue().toString();
//			itemTax.setTaxable(val.equals("Taxable"));
//		}
//		AccounterAsyncCallback<IsSerializable> createItemCallback = new AccounterAsyncCallback<IsSerializable>() {
//
//			public void onException(AccounterException caught) {
//				//UIUtils.log(caught.toString());
//			}
//
//			public void onSuccess(IsSerializable result) {
//				if (result != null) {
//					ClientItemTax itemTx = (ClientItemTax) result;
//					itemTaxes.add(itemTx);
//					ListGridRecord record = new ListGridRecord();
//					record.setAttribute("name", dialog.taxText.getValue()
//							.toString());
//					record.setAttribute("id", itemTx.getID());
//					addRecord(record);
//					if (callBack != null) {
//						callBack.onSuccess((IAccounterCore) result);
//					}
//					dialog.destroy();
//				}
//			}
//
//		};
//		FinanceApplication.createCRUDService().createItemTax(itemTax,
//				createItemCallback);
//	}
//
//	public void showAddEditDialog(ClientItemTax rec) {
//		dialog = new AddItemTaxDialog("Item Group", "");
//
//		selectedItemTax = rec;
//		if (selectedItemTax != null) {
//			dialog.taxableRadio.setDisabled(true);
//			dialog.taxText.setValue(selectedItemTax.getName());
//			dialog.taxableRadio
//					.setValue(selectedItemTax.isTaxable() ? "Taxable"
//							: "Non-taxable");
//
//		}
//		dialog.addInputDialogHandler(new InputDialogHandler() {
//
//			public void onCancelClick() {
//
//			}
//
//			public boolean onOkClick() {
//				if (dialog.form.validate()) {
//					if (selectedItemTax != null) {
//
//						editItemTax();
//					} else
//						createItemTaxes();
//				} else {
//					
//					return false;
//				}
//				return true;
//			}
//
//		});
//		dialog.show();
//
//	}
//
//	protected void editItemTax() {
//
//		selectedItemTax.setName(dialog.taxText.getValue().toString());
//
//		FinanceApplication.createCRUDService().alterItemTax(selectedItemTax,
//				new AccounterAsyncCallback<IsSerializable>() {
//
//					public void onException(AccounterException caught) {
//						Accounter.showError("Failed to edit Item Tax ");
//					}
//
//					public void onSuccess(IsSerializable result) {
//						if (result != null) {
//
//							ClientItemTax itemTax = (ClientItemTax) result;
//
//							getSelectedRecord().setAttribute("name",
//									itemTax.getName());
//							refreshGrid();
//
//							Accounter.showError("Group has been Modified ");
//						}
//					}
//				});
//
//	}
//
//}
