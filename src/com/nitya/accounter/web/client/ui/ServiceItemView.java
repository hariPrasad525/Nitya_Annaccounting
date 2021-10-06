package com.nitya.accounter.web.client.ui;
//package com.nitya.accounter.web.client.ui;
//
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import com.google.gwt.user.client.rpc.AccounterAsyncCallback;
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.smartgwt.client.types.Alignment;
//import com.smartgwt.client.util.SC;
//import com.smartgwt.client.widgets.Button;
//import com.smartgwt.client.widgets.events.ClickEvent;
//import com.smartgwt.client.widgets.events.ClickHandler;
//import com.smartgwt.client.widgets.form.DynamicForm;
//import com.smartgwt.client.widgets.form.fields.CheckboxItem;
//import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
//import com.smartgwt.client.widgets.form.fields.FormItem;
//import com.smartgwt.client.widgets.form.fields.TextAreaItem;
//import com.smartgwt.client.widgets.form.fields.TextItem;
//import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
//import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
//import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
//import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
//import com.smartgwt.client.widgets.form.validator.FloatRangeValidator;
//import com.smartgwt.client.widgets.layout.StyledPanel;
//import com.smartgwt.client.widgets.layout.VLayout;
//import com.nitya.accounter.web.client.core.ClientAccount;
//import com.nitya.accounter.web.client.core.ClientItem;
//import com.nitya.accounter.web.client.core.ClientItemGroup;
//import com.nitya.accounter.web.client.core.ClientVendor;
//import com.nitya.accounter.web.client.ui.combo.CustomCombo;
//import com.nitya.accounter.web.client.ui.combo.SelectItemType;
//
//public class ServiceItemView extends AbstractBaseView {
//
//	TextItem nameText, skuText, salesPriceText, stdCostText, purchasePriceTxt;
//	CheckboxItem isellCheck, comCheck, activeCheck, ibuyCheck;
//	TextAreaItem salesDescArea, purchaseDescArea;
//	// ITemGroupCombo itemGroupCombo;
//	// ITemTaxCombo itemTaxCombo;
//	CustomCombo itemGroupCombo;
//	ComboBoxItem itemTaxCombo;
//	List<ClientAccount> incomeAccounts;
//	// List<ClientItemTax> itemTaxes;
//
//	ClientAccount selectedAccount;
//	// ClientItemTax selectedTax;
//
//	// private VendorCombo prefVendorCombo;
//	HashMap<String, ClientVendor> allvendors;
//	HashMap<String, ClientAccount> allaccounts;
//	HashMap<String, ClientItemGroup> allitemgroups;
//	// HashMap<String, ClientItemTax> allitemtaxes;
//	private TextItem vendItemNumText;
//	private Button saveCloseButt;
//	private Button saveNewButt;
//	// private AccountCombo accountCombo, expAccCombo;
//	private CustomCombo accountCombo, expAccCombo, prefVendorCombo;
//	private DynamicForm itemForm;
//	private DynamicForm stdCostForm;
//	private DynamicForm itemInfoForm;
//	private DynamicForm purchaseInfoForm;
//	private DynamicForm salesInfoForm;
//	private FloatRangeValidator floatRangeValidator;
//	private TextItem weightText;
//
//	public ServiceItemView() {
//		// super.company = FinanceApplication.getCompany();
//		createControls();
//		getDataFromRPC();
//
//	}
//
//	private void getDataFromRPC() {
//		List<ClientAccount> result = FinanceApplication.getCompany()
//				.getAccounts();
//		accountCombo.initCombo(result);
//		expAccCombo.initCombo(result);
//
//		// AccounterAsyncCallback<ArrayList<ClientAccount>> accountsCallback = new
//		// AccounterAsyncCallback<ArrayList<ClientAccount>>() {
//		//
//		// public void onException(AccounterException caught) {
//		// Accounter.showError("Result: null");
//		// }
//		//
//		// public void onSuccess(ArrayList<ClientAccount> result) {
//		// accountCombo.initCombo(result);
//		// expAccCombo.initCombo(result);
//		// }
//		//
//		// };
//		//itemTaxCombo.initCombo(FinanceApplication.getCompany().getItemTaxs());
//		// AccounterAsyncCallback<ArrayList<ClientItemTax>> taxsCallback = new
//		// AccounterAsyncCallback<ArrayList<ClientItemTax>>() {
//		//
//		// public void onException(AccounterException caught) {
//		// Accounter.showError("Result: null");
//		// }
//		//
//		// public void onSuccess(ArrayList<ClientItemTax> result) {
//		// itemTaxCombo.initCombo(result);
//		// }
//		//
//		// };
//		prefVendorCombo.initCombo(FinanceApplication.getCompany().getVendors());
//		// AccounterAsyncCallback<ArrayList<ClientVendor>> vendorsCallBack = new
//		// AccounterAsyncCallback<ArrayList<ClientVendor>>() {
//		//
//		// public void onException(AccounterException caught) {
//		// Accounter.showError("Result: null");
//		// }
//		//
//		// public void onSuccess(ArrayList<ClientVendor> result) {
//		// prefVendorCombo.initCombo(result);
//		// }
//		//
//		// };
//		AccounterAsyncCallback<ArrayList<ClientItemGroup>> itemGroupsCallBack = new AccounterAsyncCallback<ArrayList<ClientItemGroup>>() {
//
//			public void onException(AccounterException caught) {
//				Accounter.showError("Result: null");
//			}
//
//			public void onSuccess(ArrayList<ClientItemGroup> result) {
//				itemGroupCombo.initCombo(result);
//				disablePurchaseFormItems(true);
//			}
//
//		};
//		// FinanceApplication.createGETService().getAccounts(
//		// accountsCallback);
//		// FinanceApplication.createGETService().getItemTaxes(
//		// taxsCallback);
//		// FinanceApplication.createGETService().getVendors(
//		// vendorsCallBack);
//		FinanceApplication.createGETService().getItemGroups(itemGroupsCallBack);
//	}
//
//	private void createControls() {
//
//		setTitle(UIUtils.title("New Service Item"));
//		nameText = new TextItem("Item name");
//		nameText.setWidth("*");
//		nameText.setRequired(true);
//
//		floatRangeValidator = new FloatRangeValidator();
//		floatRangeValidator.setMin(1);
//
//		skuText = new TextItem("UPC/SKU");
//		skuText.setWidth("*");
//		weightText = new TextItem("Weight");
//
//		itemForm = new DynamicForm();
//		itemForm.setWidth("100%");
//		itemForm.setIsGroup(true);
//		itemForm.setGroupTitle("Item");
//		itemForm.setWrapItemTitles(false);
//		itemForm.setFields(nameText, skuText);
//
//		isellCheck = new CheckboxItem("I sell this item");
//		isellCheck.setValue(true);
//		isellCheck.setWidth("*");
//		isellCheck.addChangeHandler(new ChangeHandler() {
//
//			public void onChange(ChangeEvent event) {
//				disableSalesFormItems(!(Boolean) event.getValue());
//			}
//		});
//
//		salesDescArea = new TextAreaItem("Sales description");
//		salesDescArea.setWidth("*");
//
//		salesPriceText = new TextItem("Sales price");
//		salesPriceText.setWidth("*");
//		salesPriceText.setDefaultValue("$0.0");
//		salesPriceText.setValidators(floatRangeValidator);
//		salesPriceText.setValidateOnChange(true);
//		salesPriceText.addBlurHandler(new BlurHandler() {
//
//			public void onBlur(BlurEvent event) {
//				String value = salesPriceText.getValue().toString().replace(
//						"$", "");
//				salesPriceText.setValue("");
//				salesPriceText.setValue("$" + value
//						+ (value.contains(".") ? "" : "0.00"));
//			}
//		});
//
//		accountCombo = new CustomCombo("Income account",
//				SelectItemType.ACCOUNT, true);
//		accountCombo.setRequired(true);
//		accountCombo.setWidth("*");
//
//		itemTaxCombo = new ComboBoxItem();
//		itemTaxCombo.setRequired(true);
//		LinkedHashMap<String, Boolean> map = new LinkedHashMap<String, Boolean>();
//		map.put("Taxable", true);
//		map.put("Non-Taxable", false);
//		itemTaxCombo.setValueMap(map);
//		itemTaxCombo.setWidth("*");
//
//		comCheck = new CheckboxItem("Commission item");
//		comCheck.setWidth("*");
//
//		salesInfoForm = UIUtils.form("Sales information");
//		salesInfoForm.setFields(isellCheck, salesDescArea, salesPriceText,
//				accountCombo, itemTaxCombo, comCheck);
//
//		stdCostText = new TextItem("Standard cost");
//		stdCostText.setWidth("*");
//		stdCostText.setDefaultValue("$0.0");
//		stdCostText.setValidators(floatRangeValidator);
//		stdCostText.setValidateOnChange(true);
//		stdCostText.addBlurHandler(new BlurHandler() {
//
//			public void onBlur(BlurEvent event) {
//				String value = stdCostText.getValue().toString().replace("$",
//						"");
//				stdCostText.setValue("");
//				stdCostText.setValue("$" + value
//						+ (value.contains(".") ? "" : "0.00"));
//			}
//		});
//
//		stdCostForm = UIUtils.form("Standard cost");
//		stdCostForm.setFields(stdCostText);
//
//		VLayout leftVLay = new VLayout();
//		leftVLay.setWidth("100%");
//		leftVLay.setMembers(itemForm, salesInfoForm, stdCostForm);
//
//		itemGroupCombo = new CustomCombo("Item group",
//				SelectItemType.ITEM_GROUP, true);
//		itemGroupCombo.setWidth("*");
//		activeCheck = new CheckboxItem("Active");
//		activeCheck.setValue(true);
//		activeCheck.setWidth("*");
//
//		itemInfoForm = UIUtils.form("Item information");
//		itemInfoForm.setFields(itemGroupCombo, activeCheck);
//
//		ibuyCheck = new CheckboxItem("I buy this item");
//		ibuyCheck.setWidth("*");
//		ibuyCheck.addChangeHandler(new ChangeHandler() {
//
//			public void onChange(ChangeEvent event) {
//				disablePurchaseFormItems(!(Boolean) event.getValue());
//			}
//		});
//
//		purchaseDescArea = new TextAreaItem("Purchase description");
//		purchaseDescArea.setWidth("*");
//
//		purchasePriceTxt = new TextItem("Purchase price");
//		purchasePriceTxt.setWidth("*");
//		purchasePriceTxt.setDefaultValue("$0.0");
//		purchasePriceTxt.setValidators(floatRangeValidator);
//		purchasePriceTxt.setValidateOnChange(true);
//		purchasePriceTxt.addBlurHandler(new BlurHandler() {
//
//			public void onBlur(BlurEvent event) {
//				String value = purchasePriceTxt.getValue().toString().replace(
//						"$", "");
//				purchasePriceTxt.setValue("");
//				purchasePriceTxt.setValue("$" + value
//						+ (value.contains(".") ? "" : "0.00"));
//			}
//		});
//
//		expAccCombo = new CustomCombo("Expense account",
//				SelectItemType.ACCOUNT, true);
//		expAccCombo.setRequired(true);
//		expAccCombo.setDisabled(true);
//		expAccCombo.setWidth("*");
//
//		prefVendorCombo = new CustomCombo("Preferred vendor",
//				SelectItemType.VENDOR, true);
//		prefVendorCombo.setDisabled(true);
//		vendItemNumText = new TextItem("Vendor item no");
//		purchaseInfoForm = UIUtils.form("Purchase information");
//		// purchaseInfoForm.set
//		purchaseInfoForm
//				.setFields(ibuyCheck, purchaseDescArea, purchasePriceTxt,
//						expAccCombo, prefVendorCombo, vendItemNumText);
//
//		VLayout rightVLay = new VLayout();
//		rightVLay.setWidth("100%");
//		rightVLay.setMembers(itemInfoForm, purchaseInfoForm);
//
//		StyledPanel topHLay = new StyledPanel();
//		topHLay.setMembersMargin(10);
//		topHLay.setMargin(10);
//		topHLay.setAlign(Alignment.CENTER);
//		topHLay.setMembers(leftVLay, rightVLay);
//
//		saveCloseButt = new Button("Save and Close");
//		// saveCloseButt.setLayoutAlign(Alignment.LEFT);
//		saveCloseButt.setAutoFit(true);
//
//		saveCloseButt.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				save(true);
//			}
//		});
//		saveNewButt = new Button("Save and New");
//		// saveNewButt.setLayoutAlign(Alignment.RIGHT);
//		saveNewButt.setAutoFit(true);
//
//		saveNewButt.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				save(false);
//			}
//		});
//		StyledPanel buttHLay = new StyledPanel();//buttHLay.setBackgroundColor("#ff00bb"
//		// );
//		buttHLay.setAlign(Alignment.RIGHT);
//		buttHLay.setLayoutBottomMargin(20);
//		buttHLay.setMargin(20);
//		buttHLay.setWidth("100%");
//		buttHLay.setAutoHeight();
//		buttHLay.setMembersMargin(150);
//		buttHLay.setMembers(saveCloseButt, saveNewButt);
//
//		VLayout mainVLay = new VLayout();
//		mainVLay.setSize("100%", "100%");
//		mainVLay.setMembers(topHLay, buttHLay);
//		mainVLay.setTop(20);
//		mainVLay.setAlign(Alignment.CENTER);
//		addChild(mainVLay);
//
//		setSize("100%", "100%");
//	}
//
//	protected void disableSalesFormItems(Boolean isEdit) {
//		salesDescArea.setDisabled(isEdit);
//		salesPriceText.setDisabled(isEdit);
//		accountCombo.setDisabled(isEdit);
//		itemTaxCombo.setDisabled(isEdit);
//		comCheck.setDisabled(isEdit);
//	}
//
//	/**
//	 * call to save Entered data on this View
//	 * 
//	 * @param isSaveClose
//	 *            if it is true, than it means it will close View, if false,than
//	 *            reset values to new View
//	 */
//	protected void save(boolean isSaveClose) {
//		final boolean isSaveoRreset = isSaveClose;
//		boolean isSalesFormValidated = true;
//		boolean isPurchaseformValidated = true;
//		ClientItem item = new ClientItem();
//		if (validateItemform(item)) {
//			if (getBooleanValue(isellCheck)) {
//				isSalesFormValidated = false;
//				isSalesFormValidated = validateSalesform(item);
//			}
//			if (getBooleanValue(ibuyCheck)) {
//				isPurchaseformValidated = false;
//				isPurchaseformValidated = validatePurchaseFomr(item);
//			}
//			if (isPurchaseformValidated && isSalesFormValidated) {
//				FinanceApplication.createCRUDService().createItem(item,
//						new AccounterAsyncCallback<IsSerializable>() {
//
//							public void onException(AccounterException caught) {
//								Accounter.showError("Could not able to create Item..");
//							}
//
//							public void onSuccess(IsSerializable result) {
//								Accounter.showError("Item created Successfully");
//								if (isSaveoRreset) {
//									// MainFinanceWindow.removeSelectedTab();
//								} else {
//									salesInfoForm.resetValues();
//									itemForm.resetValues();
//									purchaseInfoForm.resetValues();
//									itemInfoForm.resetValues();
//									stdCostForm.resetValues();
//								}
//							}
//						});
//
//			}
//		}
//	}
//
//	private boolean validateItemform(ClientItem item) {
//		if (itemForm.validate()) {
//			item.setActive(getBooleanValue(activeCheck));
//			item.setName(getStringValue(nameText));
//			item.setItemGroup(((ClientItemGroup) itemGroupCombo.getSelection())
//					.getID());
//			item.setStandardCost(getDoubleValue(stdCostText));
//			item.setUPCorSKU(getStringValue(skuText));
//			item.setISellThisItem(getBooleanValue(isellCheck));
//			item.setIBuyThisItem(getBooleanValue(ibuyCheck));
//			return true;
//		}
//		return false;
//	}
//
//	private boolean validateSalesform(ClientItem item) {
//		// validate sales form
//
//		if (salesInfoForm.validate()) {
//			item.setCommissionItem(getBooleanValue(comCheck));
//			item.setSalesDescription(getStringValue(salesDescArea));
//			item.setSalesPrice(getDoubleValue(salesPriceText));
//			item.setIncomeAccount(((ClientAccount) accountCombo.getSelection())
//					.getID());
//			return true;
//		}
//		return false;
//	}
//
//	private boolean validatePurchaseFomr(ClientItem item) {
//		if (purchaseInfoForm.validate()) {
//			item.setPurchaseDescription(getStringValue(purchaseDescArea));
//			item.setPurchasePrice(getDoubleValue(purchasePriceTxt));
//			item.setPreferredVendor(((ClientVendor) prefVendorCombo
//					.getSelection()).getID());
//			item
//					.setExpenseAccount((((ClientAccount) expAccCombo
//							.getSelection()).getID()));
//			item.setVendorItemNumber(getStringValue(vendItemNumText));
//			return true;
//		}
//		return false;
//	}
//
//	private boolean getBooleanValue(FormItem item) {
//		return item.getValue() != null ? (Boolean) item.getValue() : false;
//	}
//
//	private String getStringValue(FormItem item) {
//		return item.getValue() != null ? item.getValue().toString() : "";
//	}
//
//	private Double getDoubleValue(FormItem item) {
//		return Double.parseDouble(item.getValue() != null
//				&& !item.getValue().equals("") ? item.getValue().toString()
//				.replace("$", "") : "0.0");
//	}
//
//	protected void disablePurchaseFormItems(Boolean isEdit) {
//		purchasePriceTxt.setDisabled(isEdit);
//		purchaseDescArea.setDisabled(isEdit);
//		expAccCombo.setDisabled(isEdit);
//		prefVendorCombo.setDisabled(isEdit);
//		vendItemNumText.setDisabled(isEdit);
//	}
//
//	@Override
//	public void init() {
//
//	}
//
//	@Override
//	public void initData() {
//
//	}
//
//}
