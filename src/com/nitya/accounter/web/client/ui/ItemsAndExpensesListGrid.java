package com.nitya.accounter.web.client.ui;
//package com.nitya.accounter.web.client.ui;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.nitya.accounter.web.client.core.ClientAccount;
//import com.nitya.accounter.web.client.core.ClientCompany;
//import com.nitya.accounter.web.client.core.ClientItem;
//import com.nitya.accounter.web.client.core.ClientTaxCode;
//import com.nitya.accounter.web.client.core.ClientTransaction;
//import com.nitya.accounter.web.client.core.ClientTransactionItem;
//import com.nitya.accounter.web.client.core.RecordDoubleClickHandler;
//import com.nitya.accounter.web.client.ui.combo.CustomCombo;
//import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
//import com.nitya.accounter.web.client.ui.combo.SelectItemType;
//import com.nitya.accounter.web.client.ui.core.ListGridView;
//import com.nitya.accounter.web.client.ui.core.TransactionItemRecord;
//import com.nitya.accounter.web.client.ui.forms.TextItem;
//
///**
// * 
// * @author venki.p
// * 
// */
//
//public class ItemsAndExpensesListGrid extends ListGridView {
//
//	private ListGridField nameField;
//	// private ItemCombo itemCombo;
//	// private AccountCombo accountsCombo;
//	// private TaxCodeCombo taxCodeCombo;
//	// private TextItem descrptextItem;
//	// private ListGridField qtyField;
//	private CustomCombo accountsCombo, itemCombo, taxCodeCombo;
//	private ClientCompany company;
//	private ListGridField descpField;
//	private TextItem unitpriceTextItem;
//	private ListGridField unitPriceField;
//	private ListGridField linetotalField;
//	private TextItem linetotalTextItem;
//	protected ListGridRecord currentRecord;
//	protected ClientAccount selectedAccountFromTX;
//	protected ClientItem selectedItemFromTX;
//	double totallinetotal = 0;
//	ClientAccount accountsPayable;
//	protected List<ClientAccount> allAccounts;
//	protected List<ClientItem> allItems;
//	boolean displaySalesTaxMenuItem;
//	protected List<ClientTaxCode> allTaxeCodes;
//	protected ClientTaxCode selectedTaxCodeFromTX;
//	private TextItem amtTextItem;
//
//	public ItemsAndExpensesListGrid(TextItem amTextItem, int transactionDomain) {
//		super(transactionDomain);
//		this.amtTextItem = amTextItem;
//		company = FinanceApplication.getCompany();
//		displaySalesTaxMenuItem = false;
//		getData();
//		createControls();
//	}
//
//	public ItemsAndExpensesListGrid(TextItem amtTextItem,
//			boolean displaySalesTaxMenuItem, int transactionDomain) {
//		super(transactionDomain);
//		this.amtTextItem = amtTextItem;
//		company = FinanceApplication.getCompany();
//
//		getData();
//		createControls();
//	}
//
//	private void getData() {
//		List<ClientAccount> result = FinanceApplication.getCompany()
//				.getAccounts();
//		allAccounts = result;
//		accountsCombo.initCombo(result);
//		nameField.setEditorType(accountsCombo);
//		for (ClientAccount tempAccount : result) {
//			if (tempAccount.getName().equalsIgnoreCase("Accounts Payable")) {
//				accountsPayable = tempAccount;
//				break;
//			}
//		}
//
//		// AccounterAsyncCallback<ArrayList<ClientAccount>> accountsCallback = new
//		// AccounterAsyncCallback<ArrayList<ClientAccount>>() {
//		//
//		// public void onException(AccounterException caught) {
//		// Accounter.showError("Result: null");
//		// }
//		//
//		// public void onSuccess(ArrayList<ClientAccount> result) {
//		// allAccounts = result;
//		// accountsCombo.initCombo(result);
//		// nameField.setEditorType(accountsCombo);
//		// for (ClientAccount tempAccount : result) {
//		// if (tempAccount.getName().equalsIgnoreCase(
//		// "Accounts Payable")) {
//		// accountsPayable = tempAccount;
//		// break;
//		// }
//		// }
//		// }
//		//
//		// };
//		// FinanceApplication.createGETService().getAccounts(accountsCallback);
//		List<ClientItem> clientItem = FinanceApplication.getCompany()
//				.getItems();
//		allItems = clientItem;
//		itemCombo.initCombo(clientItem);
//		nameField.setEditorType(itemCombo);
//
//		// AccounterAsyncCallback<ArrayList<ClientItem>> itemsCallback = new
//		// AccounterAsyncCallback<ArrayList<ClientItem>>() {
//		//
//		// public void onException(AccounterException caught) {
//		// Accounter.showError("Result: null");
//		// }
//		//
//		// public void onSuccess(ArrayList<ClientItem> result) {
//		// allItems = result;
//		// itemCombo.initCombo(result);
//		// nameField.setEditorType(itemCombo);
//		// }
//		//
//		// };
//		// FinanceApplication.createGETService().getItems(itemsCallback);
//
//		if (displaySalesTaxMenuItem) {
//			allTaxeCodes = FinanceApplication.getCompany().getTaxcodes();
//			taxCodeCombo.initCombo(allTaxeCodes);
//			nameField.setEditorType(taxCodeCombo);
//
//		}
//
//	}
//
//	private void createControls() {
//		createMenu();
//		nameField = new ListGridField("name", "Name", 100);
//
//		itemCombo = new CustomCombo("Item", SelectItemType.ITEM, true);
//		itemCombo.setName("name");
//		itemCombo.setPickListWidth(200);
//		itemCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler() {
//
//					public void selectedComboBoxItem(IsSerializable selectItem) {
//						selectedItemFromTX = (ClientItem) selectItem;
//					}
//
//				});
//
//		accountsCombo = new CustomCombo("Accounts", SelectItemType.ACCOUNT,
//				true);
//		accountsCombo.setName("name");
//		accountsCombo.setPickListWidth(200);
//		accountsCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler() {
//
//					public void selectedComboBoxItem(IsSerializable selectItem) {
//						selectedAccountFromTX = (ClientAccount) selectItem;
//					}
//
//				});
//
//		taxCodeCombo = new CustomCombo("TaxCodes", SelectItemType.TAX_CODE,
//				true);
//		taxCodeCombo.setName("name");
//		taxCodeCombo.setPickListWidth(200);
//		taxCodeCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler() {
//
//					public void selectedComboBoxItem(IsSerializable selectItem) {
//						selectedTaxCodeFromTX = (ClientTaxCode) selectItem;
//					}
//
//				});
//
//		nameField.setEditorType(itemCombo);
//		nameField.setRequired(true);
//
//		TextItem descrptextItem = new TextItem();
//		descrptextItem.setWidth("20%");
//
//		descpField = new ListGridField("description", "Description", 175);
//
//		descpField.setEditorType(descrptextItem);
//
//		ListGridField qtyField = new ListGridField("qty", "Qty", 100);
//		qtyField.setEditorType(new SpinnerItem());
//		// qtyField.setType(ListGridFieldType.INTEGER);
//		qtyField.setRequired(true);
//
//		qtyField.setValidateOnChange(true);
//
//		unitpriceTextItem = new TextItem();
//		unitpriceTextItem.setName("unitPrice");
//
//		unitPriceField = new ListGridField("unitPrice", "Unit Price", 225);
//		unitPriceField.setEditorType(new TextItem());
//		// unitPriceField.setAttribute("unitPrice", "0");
//		// unitPriceField.setValidateOnChange(true);
//		unitPriceField.setRequired(true);
//		// unitPriceField.setType(ListGridFieldType.INTEGER);
//
//		linetotalTextItem = new TextItem();
//		linetotalTextItem.setName("lineTotal");
//
//		linetotalField = new ListGridField("lineTotal", "Line Total", 225);
//		linetotalField.setEditorType(linetotalTextItem);
//		linetotalField.setCanEdit(false);
//		linetotalField.setValidateOnChange(true);
//
//		addEditCompleteHandler(new EditCompleteHandler() {
//
//			public void onEditComplete(EditCompleteEvent event) {
//				adjustCurrentRecord(event.getRowNum());
//				calaculateTotal();
//			}
//		});
//		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
//
//			@Override
//			public void OnCellDoubleClick(ListGridRecord record) {
//				changeRecordType(record.getAttribute("type"));
//
//			}
//		});
//		addRecordDeleteHandler(new RecordDeleteHandler() {
//			public boolean onRecordDelete(ListGridRecord record) {
//
//				calaculateTotal();
//				return true;
//			}
//		});
//
//		// addToIntegerValidator(qtyField, unitPriceField);
//
//		addFields(nameField, descpField, qtyField, unitPriceField,
//				linetotalField);
//		setDefaultValue("qty", "1");
//		setDefaultValue("unitPrice", "$0.00");
//		setDefaultValue("lineTotal", "$0.00");
//
//		setBottomLabelTitle("Total:", 6);
//	}
//
//	protected void adjustCurrentRecord(int rowNum) {
//		currentRecord = getGrid().getRecord(rowNum);
//		int type = Integer.parseInt(currentRecord.getAttribute("type"));
//		if (type == ClientTransactionItem.TYPE_ACCOUNT
//				|| type == ClientTransactionItem.TYPE_ITEM) {
//			int qty = Integer.parseInt(currentRecord.getAttribute("qty"));
//
//			Double unitprice = Double.parseDouble(currentRecord.getAttribute(
//					"unitPrice").replace("$", ""));
//
//			currentRecord.setAttribute("lineTotal", "$" + qty * unitprice);
//			currentRecord.setAttribute("qty", "" + qty);
//			currentRecord.setAttribute("unitPrice", "$" + unitprice);
//		}
//		if (type == ClientTransactionItem.TYPE_ACCOUNT) {
//			currentRecord.setAttribute("name", selectedAccountFromTX.getName());
//
//		} else if (type == ClientTransactionItem.TYPE_ITEM) {
//			currentRecord.setAttribute("name", selectedItemFromTX.getName());
//
//		} else if (type == ClientTransactionItem.TYPE_SALESTAX)
//			currentRecord.setAttribute("name", selectedTaxCodeFromTX.getName());
//	}
//
//	protected void calaculateTotal() {
//
//		ListGridRecord allrecords[] = getRecords();
//		totallinetotal = 0;
//		for (int i = 0; i < allrecords.length; i++) {
//			ListGridRecord rec = allrecords[i];
//			try {
//				totallinetotal += Double.parseDouble(rec.getAttribute(
//						"lineTotal").replace("$", ""));
//			} catch (Exception e) {
//			}
//		}
//		setBottomLabelTitle("Total:" + "$" + totallinetotal, 6);
//		if (amtTextItem != null)
//			amtTextItem.setValue("$" + totallinetotal);
//	}
//
//	/**
//	 * This method is for change type of current record
//	 * 
//	 * @param type
//	 */
//	protected void changeRecordType(String type) {
//		editLastRecord(true);
//		linetotalField.setCanEdit(false);
//		if (type.equals("4")) {
//			setSelectedRecordImagePath("account");
//			nameField.setEditorType(accountsCombo);
//		} else if (type.equals("1")) {
//			setSelectedRecordImagePath("customers/new_item");
//			nameField.setEditorType(itemCombo);
//		} else if (type.equals("2")) {
//			setSelectedRecordImagePath("comment");
//			setEditDiableCellsExecpt(descpField);
//		} else if (type.equals("3")) {
//			setSelectedRecordImagePath("salestax");
//			nameField.setEditorType(taxCodeCombo);
//			setEditDiableCellsExecpt(nameField, descpField, linetotalField);
//		} else {
//			editLastRecord(false);
//		}
//
//	}
//
//	public void createMenu() {
////		ClickHandler AccountsclickHandler = new ClickHandler() {
////
////			public void onClick(MenuItemClickEvent event) {
////				onMenuClick(ClientTransactionItem.TYPE_ACCOUNT + "");
////			}
////		};
////		addMenuItem("Expenses", "/images/icons/account.png",
////				AccountsclickHandler);
////
////		ClickHandler itemsclickHandler = new ClickHandler() {
////
////			public void onClick(MenuItemClickEvent event) {
////				onMenuClick(ClientTransactionItem.TYPE_ITEM + "");
////			}
////
////		};
////		addMenuItem("Items", "/images/icons/customers/new_item.png",
////				itemsclickHandler);
////
////		ClickHandler commentclickHandler1 = new ClickHandler() {
////
////			public void onClick(MenuItemClickEvent event) {
////				onMenuClick(ClientTransactionItem.TYPE_COMMENT + "");
////			}
////		};
////		addMenuItem("Comment", "/images/icons/comment.png",
////				commentclickHandler1);
////
////		if (displaySalesTaxMenuItem) {
////			ClickHandler taxCodeClickHandler = new ClickHandler() {
////
////				public void onClick(MenuItemClickEvent event) {
////					onMenuClick(ClientTransactionItem.TYPE_SALESTAX + "");
////				}
////			};
////			addMenuItem("SalesTax", "/images/icons/salestax.png",
////					taxCodeClickHandler);
////
////		}
//	}
//
//	public void onMenuClick(String itemType) {
//		currentRecord = getSelectedRecord();
//		if (currentRecord != null) {
//			String type = currentRecord.getAttribute("type");
//			if (type.equals("none")) {
//				addNewRecord(itemType);
//			} else {
//				changeRecordType(itemType);
//				makeCurrentRecordEditable();
//			}
//			currentRecord.setAttribute("type", itemType);
//		}
//	}
//
//	public void addNewRecord(String type) {
//		if (type.equals("1")) {
//			setImagePath("customers/new_item");
//		} else if (type.equals("2")) {
//			setImagePath("comment");
//			isShowDefaultValues(false);
//		} else if (type.equals("4")) {
//			setImagePath("account");
//		} else {
//			isShowDefaultValues(false);
//			setImagePath("salestax");
//		}
//		changeRecordType(type);
//		addEmptyRecord(type);
//	}
//
//	public List<ClientTransactionItem> getallTransactions(
//			ClientTransaction object) {
//
//		ListGridRecord[] records = getRecords();
//		final List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
//		if (records != null) {
//			for (int i = 0; i < records.length; i++) {
//				ListGridRecord rec = records[i];
//				final ClientTransactionItem item = new ClientTransactionItem();
//				int type = Integer.parseInt(rec.getAttribute("type"));
//
//				// Setting Object to Transaction item in bidirectional way
//				// item.setTransaction(object);
//				// Setting type of trasaction
//				try {
//					System.out.println("Type is "
//							+ Integer.parseInt(rec.getAttribute("type")));
//					item.setType(Integer.parseInt(rec.getAttribute("type")));
//				} catch (Exception e) {
//				}
//				// Setting Description
//				try {
//					System.out.println("Description is "
//							+ rec.getAttribute("description"));
//					item.setDescription(rec.getAttribute("description"));
//
//				} catch (Exception e) {
//				}
//
//				if (type == ClientTransactionItem.TYPE_ACCOUNT
//						|| type == ClientTransactionItem.TYPE_ITEM) {
//					// Setting Quantity
//					try {
//						item.setQuantity(Double.parseDouble(rec
//								.getAttribute("qty")));
//						System.out.println("QTY is "
//								+ Double.parseDouble(rec.getAttribute("qty")));
//
//					} catch (Exception e) {
//						
//					}
//
//					// Setting Unit Price
//					try {
//						item.setUnitPrice(Double.parseDouble(rec.getAttribute(
//								"unitPrice").replace("$", "")));
//						System.out.println("Unit Price is "
//								+ Double.parseDouble(rec.getAttribute(
//										"unitPrice").replace("$", "")));
//
//					} catch (Exception e) {
//					}
//					// Setting Line total
//					try {
//						item.setLineTotal(Double.parseDouble(rec
//								.getAttribute("qty"))
//								* Double.parseDouble(rec.getAttribute(
//										"unitPrice").replace("$", "")));
//						System.out.println("Line Total is "
//								+ Double.parseDouble(rec.getAttribute("qty"))
//								* Double.parseDouble(rec.getAttribute(
//										"unitPrice").replace("$", "")));
//
//					} catch (Exception e) {
//					}
//
//					// Setting Name
//					if (type == ClientTransactionItem.TYPE_ACCOUNT) {
//						System.out.println("Account name is   "
//								+ rec.getAttribute("name"));
//						for (ClientAccount temp : allAccounts) {
//							if (temp.getName().equalsIgnoreCase(
//									rec.getAttribute("name"))) {
//								item.setAccount(temp.getID());
//								break;
//							}
//						}
//
//					} else {
//						System.out.println("Item is  "
//								+ rec.getAttribute("name"));
//						for (ClientItem temp : allItems) {
//							if (temp.getName().equalsIgnoreCase(
//									rec.getAttribute("name"))) {
//								item.setItem(temp.getID());
//								break;
//							}
//						}
//					}
//				}
//				if (type == ClientTransactionItem.TYPE_SALESTAX) {
//					System.out.println("TaxCode name is   "
//							+ rec.getAttribute("name"));
//					for (ClientTaxCode temp : allTaxeCodes) {
//						if (temp.getName().equalsIgnoreCase(
//								rec.getAttribute("name"))) {
//							item.setTaxCode(temp.getID());
//							break;
//						}
//					}
//					item.setLineTotal(DataUtils.getBalance(rec
//							.getAttribute("lineTotal")));
//
//				}
//				transactionItems.add(item);
//			}
//		}
//		return transactionItems;
//	}
//
//	public Double getTotal() {
//		System.out.println("Total is  " + new Double(totallinetotal));
//		return new Double(totallinetotal);
//	}
//
//	public ClientAccount getAccountPayable() {
//		return accountsPayable;
//	}
//
//	public void setAllTransactions(List<ClientTransactionItem> transactionItems) {
//
//		TransactionItemRecord allRecords[] = new TransactionItemRecord[transactionItems
//				.size()];
//		int i = 0;
//		for (ClientTransactionItem item : transactionItems) {
//			allRecords[i] = new TransactionItemRecord(1);
//			// Setting type
//			allRecords[i].setAttribute("type", item.getType() + "");
//
//			// Setting Name
//			if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT)
//				allRecords[i].setAttribute("name", FinanceApplication
//						.getCompany().getAccount(item.getAccount()).getName());
//			else if (item.getType() == ClientTransactionItem.TYPE_ITEM)
//				allRecords[i].setAttribute("name", FinanceApplication
//						.getCompany().getItem(item.getItem()).getName());
//			// Setting Description
//			allRecords[i].setAttribute("description", item.getDescription());
//			// Setting Qty
//			allRecords[i].setAttribute("qty", item.getQuantity());
//			// Setting Unitprice
//			allRecords[i].setAttribute("unitPrice", item.getUnitPrice());
//			// Setting LineTotal
//			allRecords[i].setAttribute("lineTotal", item.getLineTotal());
//			i++;
//		}
//		addRecords(allRecords);
//		calaculateTotal();
//	}
// }
