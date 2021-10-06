package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ItemGroupCombo;
import com.vimukti.accounter.web.client.ui.combo.MeasurementCombo;
import com.vimukti.accounter.web.client.ui.combo.PurchaseItemCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesItemCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.FloatRangeValidator;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.IntegerRangeValidator;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class InventoryAssemblyView extends BaseView<ClientInventoryAssembly> {
	private TextItem nameText, skuText;
	private AmountField salesPriceText, purchasePriceTxt, itemTotalValue;
	private IntegerField vendItemNumText, reorderPoint, onHandQuantity;
	private TextAreaItem salesDescArea, purchaseDescArea;
	CheckboxItem comCheck, activeCheck, itemTaxCheck;

	private ItemGroupCombo itemGroupCombo, commodityCode;
	private VendorCombo prefVendorCombo;
	private SalesItemCombo accountCombo;
	private PurchaseItemCombo expAccCombo;
	private TAXCodeCombo taxCode;
	List<ClientAccount> incomeAccounts;
	HashMap<String, ClientVendor> allvendors;
	HashMap<String, ClientAccount> allaccounts;
	HashMap<String, ClientItemGroup> allitemgroups;
	private DynamicForm itemForm;
	private DynamicForm itemInfoForm;
	private DynamicForm purchaseInfoForm;
	private DynamicForm salesInfoForm;
	private FloatRangeValidator floatRangeValidator;
	private IntegerRangeValidator integerRangeValidator;

	protected ClientItemGroup selectItemGroup;
	protected ClientVendor selectVendor;
	protected ClientTAXCode selectTaxCode;
	private ArrayList<DynamicForm> listforms;
	private String name;
	private String itemName;
	private InventoryAssemblyItemTable table;

	private MeasurementCombo measurement;
	private WarehouseCombo wareHouse;
	private AccountCombo assetsAccount;
	private DateField asOfDate;
	private AddNewButton itemTableButton;
	private AmountLabel totalLabel;
	private Label quantityUnitsLabel;
	private CheckboxItem isellCheck;
	private CheckboxItem ibuyCheck;
	private StyledPanel mainVLay;

	public InventoryAssemblyView() {
		super();
		this.getElement().setId("InventoryAssemblyView");
	}

	private void initTaxCodes() {
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			List<ClientTAXCode> result = getCompany().getActiveTaxCodes();
			if (result != null) {
				taxCode.initCombo(result);
				ClientTAXCode code = null;
				if (isInViewMode()) {
					code = getCompany().getTAXCode(data.getTaxCode());
				}
				if (code == null) {
					code = getCompany().getTAXCode(
							getPreferences().getDefaultTaxCode());
				}
				taxCode.setComboItem(code);
			}
		}

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		Label lab1 = new Label(messages.newAssembly());
		lab1.setStyleName("label-title");

		StyledPanel hPanel = new StyledPanel("hPanel");
		hPanel.add(lab1);

		nameText = new TextItem(messages.inventoryAssembly(), "nameText");
		nameText.setValue(itemName);
		nameText.setRequired(true);
		nameText.setEnabled(!isInViewMode());

		floatRangeValidator = new FloatRangeValidator();
		floatRangeValidator.setMin(0);

		integerRangeValidator = new IntegerRangeValidator();
		integerRangeValidator.setMin(0);

		skuText = new TextItem(messages.upcsku(), "skuText");
		skuText.setEnabled(!isInViewMode());

		commodityCode = new ItemGroupCombo(messages.commodityCode());
		itemForm = new DynamicForm("itemForm");
		itemForm.add(nameText);
		salesDescArea = new TextAreaItem(messages.salesDescription(),
				"salesDescArea");

		salesDescArea.setDisabled(isInViewMode());

		salesDescArea.setToolTip(messages.writeCommentsForThis(
				this.getAction().getViewName()).replace(messages.comments(),
				messages.salesDescription()));
		salesPriceText = new AmountField(messages.salesPrice(), this,
				getBaseCurrency(), "salesPriceText");
		salesPriceText.setEnabled(!isInViewMode());

		accountCombo = new SalesItemCombo(messages.incomeAccount());
		accountCombo.setEnabled(!isInViewMode());
		accountCombo.setRequired(true);

		/**
		 * adding the inventory information controls
		 */

		assetsAccount = new AccountCombo(messages.assetsAccount()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return getCompany().getAccounts(
						ClientAccount.TYPE_INVENTORY_ASSET);
			}

			@Override
			public void onAddNew() {
				NewAccountAction action = new NewAccountAction();
				List<Integer> list = new ArrayList<Integer>();
				list.add(ClientAccount.TYPE_INVENTORY_ASSET);
				action.setAccountTypes(list);
				action.setCallback(new ActionCallback<ClientAccount>() {

					@Override
					public void actionResult(ClientAccount result) {
						if (result.getIsActive())
							addItemThenfireEvent(result);
					}
				});

				action.run(null, true);
			}
		};

		ArrayList<ClientAccount> accounts = getCompany().getAccounts(
				ClientAccount.TYPE_INVENTORY_ASSET);
		for (ClientAccount clientAccount : accounts) {
			assetsAccount.setComboItem(clientAccount);
			break;
		}
		assetsAccount.setEnabled(!isInViewMode());
		assetsAccount.setRequired(true);

		reorderPoint = new IntegerField(this, "Reorder Point");
		reorderPoint.setEnabled(!isInViewMode());
		reorderPoint.setValidators(integerRangeValidator);

		itemTotalValue = new AmountField(messages.total(), this,
				getBaseCurrency(), "itemTotalValue");
		itemTotalValue.setAmount(0.00D);
		itemTotalValue.setEnabled(false);

		onHandQuantity = new IntegerField(this, "On Hand Quantity");
		onHandQuantity.setNumber(0l);
		// onHandQuantity.setWidth(100);
		onHandQuantity.setEnabled(!isInViewMode());
		onHandQuantity.setValidators(integerRangeValidator);
		quantityUnitsLabel = new Label();
		quantityUnitsLabel.setText(getCompany()
				.getMeasurement(getCompany().getDefaultMeasurement())
				.getDefaultUnit().getType());
		onHandQuantity.add(quantityUnitsLabel);
		onHandQuantity.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				Double amount = purchasePriceTxt.getAmount();
				if (onHandQuantity.getValue().length() > 0) {
					Double amount2 = Double.valueOf(onHandQuantity.getValue());
					itemTotalValue.setAmount(amount * amount2);
				}

			}
		});

		asOfDate = new DateField(messages.asOf(), "asOfDate");
		asOfDate.setEnabled(!isInViewMode());
		asOfDate.setEnteredDate(new ClientFinanceDate());

		DynamicForm inventoryInfoForm = new DynamicForm("inventoryInfoForm");
		inventoryInfoForm.add(assetsAccount, reorderPoint, onHandQuantity,
				itemTotalValue, asOfDate);

		/**
		 * over
		 */

		itemTaxCheck = new CheckboxItem(messages.taxable(), "itemTaxCheck");
		itemTaxCheck.setValue(true);
		itemTaxCheck.setEnabled(false);

		comCheck = new CheckboxItem(messages.commissionItem(), "comCheck");
		comCheck.setEnabled((!isInViewMode()));

		salesInfoForm = UIUtils.form(messages.salesInformation());

		itemGroupCombo = new ItemGroupCombo(messages.itemGroup());
		itemGroupCombo.setEnabled(!isInViewMode());
		itemGroupCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItemGroup>() {
					@Override
					public void selectedComboBoxItem(ClientItemGroup selectItem) {
						selectItemGroup = selectItem;
					}
				});

		taxCode = new TAXCodeCombo(messages.taxCode(), false);
		taxCode.setRequired(false);
		taxCode.setEnabled(!isInViewMode());

		taxCode.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {
			@Override
			public void selectedComboBoxItem(ClientTAXCode selectItem) {
				selectTaxCode = selectItem;
			}
		});
		taxCode.setDefaultValue(messages.ztozeroperc());
		activeCheck = new CheckboxItem(messages.active(), "activeCheck");
		activeCheck.setValue(true);
		activeCheck.setEnabled(!isInViewMode());
		purchaseDescArea = new TextAreaItem(messages.purchaseDescription(),
				"purchaseDescArea");
		purchaseDescArea.setDisabled(isInViewMode());

		purchasePriceTxt = new AmountField(messages.standardCost(), this,
				getBaseCurrency(), "purchasePriceTxt");
		purchasePriceTxt.setEnabled(!isInViewMode());
		purchasePriceTxt.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				itemTotalValue.setAmount(purchasePriceTxt.getAmount()
						* onHandQuantity.getNumber());
			}
		});

		expAccCombo = new PurchaseItemCombo(messages.costOfGoodSold());
		expAccCombo.setRequired(true);
		expAccCombo.setEnabled(!isInViewMode());
		expAccCombo.setPopupWidth("500px");
		ClientAccount costOfGoodsAccount = getCompany().getAccount(
				getCompany().getCostOfGoodsSold());
		if (costOfGoodsAccount != null) {
			expAccCombo.setComboItem(costOfGoodsAccount);
		}
		prefVendorCombo = new VendorCombo(messages.preferredVendor(Global.get()
				.Vendor()));
		prefVendorCombo.setEnabled(!isInViewMode());
		prefVendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {
					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectVendor = selectItem;
					}
				});

		vendItemNumText = new IntegerField(this,
				messages.vendorProductNo(Global.get().Vendor()));
		vendItemNumText.setEnabled(!isInViewMode());

		isellCheck = new CheckboxItem(messages.isellthisproduct(), "isellCheck");
		isellCheck.setEnabled(!isInViewMode());

		isellCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				disableSalesFormItems(!event.getValue());
			}

		});

		ibuyCheck = new CheckboxItem(messages.ibuythisproduct(), "ibuyCheck");
		ibuyCheck.setEnabled(!isInViewMode());
		ibuyCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				disablePurchaseFormItems(!event.getValue());
			}

		});

		isellCheck.setValue(true);
		ibuyCheck.setValue(true);

		disableSalesFormItems(isInViewMode());
		disablePurchaseFormItems(isInViewMode());

		salesInfoForm.add(isellCheck, salesDescArea, salesPriceText,
				accountCombo, itemTaxCheck, comCheck);

		if (!getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine())
			salesInfoForm.add(itemTaxCheck);

		salesInfoForm.setStyleName("align-form");
		salesInfoForm.setStyleName("new_service_table");
		itemInfoForm = UIUtils.form(messages.itemInformation());

		itemInfoForm.add(itemGroupCombo, activeCheck);
		if (!getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine())
			itemInfoForm.add(activeCheck);

		purchaseInfoForm = UIUtils.form(messages.purchaseInformation());
		purchaseInfoForm.setStyleName("purchase_info_form");
		purchaseInfoForm.add(ibuyCheck, purchaseDescArea, purchasePriceTxt,
				expAccCombo/* , prefVendorCombo, vendItemNumText */);

		StyledPanel salesVPanel = new StyledPanel("salesVPanel");
		StyledPanel itemHPanel = new StyledPanel("itemHPanel");

		salesVPanel.add(salesInfoForm);
		salesVPanel.add(inventoryInfoForm);
		StyledPanel purchzVPanel = new StyledPanel("purchzVPanel");

		StyledPanel itemInfoPanel = new StyledPanel("itemInfoPanel");

		purchzVPanel.add(purchaseInfoForm);

		this.mainVLay = new StyledPanel("mainVLay");

		// mainVLay.getElement().getStyle().setMarginBottom(15, Unit.PX);
		mainVLay.add(hPanel);

		StyledPanel topPanel1 = new StyledPanel("topPanel1");
		topPanel1.add(itemForm);
		topPanel1.setStyleName("service-item-group");
		topPanel1.add(itemInfoForm);
		StyledPanel topHLay = getTopLayout();

		if (topHLay != null) {
			StyledPanel topPanel2 = new StyledPanel("topPanel2");
			topPanel2.add(salesVPanel);
			topPanel2.add(purchzVPanel);
			topHLay.add(topPanel1);
			topHLay.add(topPanel2);
			mainVLay.add(topHLay);
		} else {
			mainVLay.add(topPanel1);
			mainVLay.add(salesVPanel);
			mainVLay.add(purchzVPanel);
		}

		table = new InventoryAssemblyItemTable(
				InventoryAssemblyItemTable.INVENTORY_ASSEMBLY_ITEM,
				new ICurrencyProvider() {

					// private ClientCurrency currency;
					// private double currencyFactor;

					@Override
					public Double getAmountInBaseCurrency(Double amount) {
						// if (currency != null && amount != null) {
						// if (currencyFactor < 0.0) {
						// currencyFactor = 1.0;
						// }
						// return amount * currencyFactor;
						// } else {
						return amount;
						// }
					}

					@Override
					public ClientCurrency getTransactionCurrency() {
						return getCompany().getPrimaryCurrency();
					}

					@Override
					public Double getCurrencyFactor() {
						// return currencyFactor;
						return 1.0;
					}

				}) {

			@Override
			public ClientCurrency getTransactionCurrency() {
				return getCompany().getPrimaryCurrency();
			}

			@Override
			public Double getCurrencyFactor() {
				return 1.0;
			}

			@Override
			public Double getAmountInBaseCurrency(Double amount) {
				return amount;
			}

			@Override
			protected void updateNonEditableItems() {
				InventoryAssemblyView.this.updateNonEditableItems();
			}

			@Override
			protected boolean isInViewMode() {
				return InventoryAssemblyView.this.isInViewMode();
			}

			@Override
			protected ClientInventoryAssemblyItem getEmptyRow() {
				return new ClientInventoryAssemblyItem();
			}

			@Override
			protected long getAssembly() {
				if (getData() == null) {
					return 0;
				}
				return getData().getID();
			}
		};
		Label assemblyTableTitle = new Label(messages2.table(messages
				.inventoryAssemblyItem()));
		assemblyTableTitle.addStyleName("editTableTitle");
		StyledPanel itemPanel = new StyledPanel("assemblyTableContainer");
		itemPanel.add(assemblyTableTitle);
		itemPanel.add(table);
		mainVLay.add(itemPanel);
		table.setEnabled(!isInViewMode());

		totalLabel = new AmountLabel(messages.totalValueOfMaterialsCost());
		DynamicForm amountLabelsForm = new DynamicForm("amountLabelsForm");
		amountLabelsForm.add(totalLabel);
		mainVLay.add(amountLabelsForm);

		this.add(mainVLay);

		StyledPanel stockPanel_1 = getStockPanel_1();
		purchzVPanel.add(stockPanel_1);
		StyledPanel stockPanel_2 = getStockPanel_2();
		measurement
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientMeasurement>() {

					@Override
					public void selectedComboBoxItem(
							ClientMeasurement selectItem) {
						quantityUnitsLabel.setText(selectItem.getDefaultUnit()
								.getType());

					}
				});
		if (getPreferences().isUnitsEnabled()) {
			purchzVPanel.add(stockPanel_2);
		}

		/* Adding dynamic forms in list */
		listforms.add(itemForm);
		listforms.add(salesInfoForm);

		listforms.add(itemInfoForm);
		listforms.add(purchaseInfoForm);
		// settabIndexes();

	}

	protected StyledPanel getTopLayout() {
		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.addStyleName("fields-panel");
		return topHLay;
	}

	private StyledPanel getStockPanel_2() {
		StyledPanel measurementPanel = new StyledPanel("measurementPanel");
		measurement = new MeasurementCombo(messages.measurement());
		measurement.setEnabled(!isInViewMode());

		DynamicForm dynamicForm = new DynamicForm("stock_dynamicForm");
		dynamicForm.add(measurement);

		measurementPanel.add(dynamicForm);

		return measurementPanel;
	}

	private StyledPanel getStockPanel_1() {

		StyledPanel stockPanel = new StyledPanel("stockPanel");
		DynamicForm stockForm = new DynamicForm("stockForm");

		wareHouse = new WarehouseCombo(messages.wareHouse());

		wareHouse.setEnabled(!isInViewMode());
		stockForm.add(wareHouse);
		if (getPreferences().iswareHouseEnabled()) {
			stockPanel.add(stockForm);
		}
		listforms.add(stockForm);

		// stockPanel.setWidth("100%");
		return stockPanel;

	}

	public void updateNonEditableItems() {
		if (table == null)
			return;
		totalLabel.setAmount(table.getLineTotal());
	}

	private void addItem() {
		ClientInventoryAssemblyItem transactionItem = new ClientInventoryAssemblyItem();
		table.add(transactionItem);
	}

	@Override
	public ClientInventoryAssembly saveView() {
		ClientInventoryAssembly saveView = super.saveView();
		if (saveView != null) {
			updateItem();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateItem();
		saveOrUpdate(data);

	}

	protected void disableSalesFormItems(Boolean isEdit) {
		salesDescArea.setDisabled(isEdit);
		salesPriceText.setEnabled(!isEdit);
		accountCombo.setEnabled(!isEdit);
		itemTaxCheck.setEnabled(!isEdit);
		comCheck.setEnabled(!isEdit);
	}

	/**
	 * call to save Entered data on this View
	 * 
	 * @param isSaveClose
	 *            if it is true, than it means it will close View, if false,than
	 *            reset values to new View
	 */
	protected void updateItem() {

		data.setActive(getBooleanValue(activeCheck));
		data.setType(ClientItem.TYPE_INVENTORY_ASSEMBLY);
		if (nameText.getValue() != null)
			data.setName(nameText.getValue().toString());
		if (selectItemGroup != null)
			data.setItemGroup(selectItemGroup.getID());

		data.setUPCorSKU(skuText.getValue());

		if (salesDescArea.getValue() != null)
			data.setSalesDescription(salesDescArea.getValue().toString());
		data.setSalesPrice(salesPriceText.getAmount());
		if (accountCombo != null && accountCombo.getSelectedValue() != null) {
			data.setIncomeAccount(accountCombo.getSelectedValue().getID());
		}
		data.setISellThisItem(getBooleanValue(isellCheck));
		data.setIBuyThisItem(getBooleanValue(ibuyCheck));

		data.setCommissionItem(getBooleanValue(comCheck));

		data.setPurchaseDescription(getStringValue(purchaseDescArea));
		data.setPurchasePrice(purchasePriceTxt.getAmount());
		if (selectVendor != null)
			data.setPreferredVendor(selectVendor.getID());
		if (expAccCombo != null && expAccCombo.getSelectedValue() != null) {
			data.setExpenseAccount(expAccCombo.getSelectedValue().getID());
		}
		data.setVendorItemNumber(vendItemNumText.getValue().toString());

		if (table.getAllRows() != null) {

			data.setComponents(getComponents());
		}
		if (assetsAccount.getSelectedValue() != null) {
			data.setAssestsAccount(assetsAccount.getSelectedValue().getID());
		}
		Long reorderPointValue = null;
		if (reorderPoint.getValue().length() > 0)
			reorderPointValue = reorderPoint.getNumber();
		else
			reorderPointValue = 0l;

		data.setItemTotalValue(itemTotalValue.getAmount());

		if (wareHouse.getSelectedValue() != null)
			data.setWarehouse(wareHouse.getSelectedValue().getID());

		Long qtyValue = null;
		if (onHandQuantity.getValue().length() > 0) {
			qtyValue = onHandQuantity.getNumber();
		} else {
			qtyValue = 0l;
		}
		ClientMeasurement measurement = null;

		if (this.measurement.getSelectedValue() != null) {
			measurement = getCompany().getMeasurement(
					this.measurement.getSelectedValue().getID());
		} else {
			measurement = getCompany().getMeasurement(
					getCompany().getDefaultMeasurement());
		}
		data.setMeasurement(measurement.getId());
		ClientQuantity quantity = new ClientQuantity();
		quantity.setValue(qtyValue);
		quantity.setUnit(measurement.getDefaultUnit().getId());
		data.setOnhandQty(quantity);

		ClientQuantity reorderedQuantity = new ClientQuantity();
		reorderedQuantity.setValue(reorderPointValue);
		reorderedQuantity.setUnit(measurement.getDefaultUnit().getId());
		data.setReorderPoint(reorderedQuantity);

		data.setAsOfDate(asOfDate.getValue());
		data.setTaxable(getBooleanValue(itemTaxCheck));
		data.setStandardCost(purchasePriceTxt.getAmount());

	}

	private Set<ClientInventoryAssemblyItem> getComponents() {
		List<ClientInventoryAssemblyItem> list = table.getAllRows();
		Set<ClientInventoryAssemblyItem> set = new HashSet<ClientInventoryAssemblyItem>();
		for (ClientInventoryAssemblyItem item : list) {
			if (!item.isEmpty()) {
				set.add(item);
			}
		}
		return set;
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);

		String exceptionMessage = exception.getMessage();

		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

		updateItem();
		if (exceptionMessage != null
				&& exceptionMessage.contains(messages.failed())) {
			data.setName(name);
			System.out.println(name + messages.aftersaving());
		}

	}

	private void setStockPanelData() {
		ClientMeasurement measurement;
		if (data.getMeasurement() != 0) {
			measurement = getCompany().getMeasurement(data.getMeasurement());
		} else {
			measurement = getCompany().getMeasurement(
					getCompany().getDefaultMeasurement());
		}
		this.measurement.setComboItem(measurement);
		ClientWarehouse wareHouse;
		if (data.getWarehouse() != 0) {
			wareHouse = getCompany().getWarehouse(data.getWarehouse());
		} else {
			wareHouse = getCompany().getWarehouse(
					getCompany().getDefaultWarehouse());
		}
		this.wareHouse.setComboItem(wareHouse);

	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result == null) {
			saveFailed(new AccounterException());
			return;
		} else {
			super.saveSuccess(result);

		}

	}

	private boolean getBooleanValue(FormItem item) {
		return item.getValue() != null ? (Boolean) item.getValue() : false;
	}

	private String getStringValue(FormItem item) {
		return item.getValue() != null ? item.getValue().toString() : "";
	}

	protected void disablePurchaseFormItems(Boolean isDisable) {

		purchasePriceTxt.setEnabled(!isDisable);
		purchaseDescArea.setEnabled(!isDisable);
		vendItemNumText.setEnabled(!isDisable);
		prefVendorCombo.setEnabled(!isDisable);
	}

	@Override
	public void init() {
		super.init();
		createControls();
		// setSize("100%", "100%");
	}

	@Override
	public void initData() {
		if (data != null) {

			nameText.setValue(data.getName());
			name = data.getName();

			isellCheck.setValue(data.isISellThisItem());

			if (data.getSalesDescription() != null)
				salesDescArea.setValue(data.getSalesDescription());
			salesPriceText.setAmount(data.getSalesPrice());

			ClientCompany company = getCompany();
			comCheck.setValue(data.isCommissionItem());

			selectItemGroup = company.getItemGroup(data.getItemGroup());
			itemGroupCombo.setComboItem(selectItemGroup);

			activeCheck.setValue((data.isActive()));

			ibuyCheck.setValue(data.isIBuyThisItem());

			if (data.getPurchaseDescription() != null)
				purchaseDescArea.setValue(data.getPurchaseDescription());
			purchasePriceTxt.setAmount(data.getPurchasePrice());

			selectVendor = company.getVendor(data.getPreferredVendor());
			if (data.getVendorItemNumber() != null) {
				vendItemNumText.setValue(data.getVendorItemNumber());
			}
			if (getPreferences().isTrackTax()
					&& getPreferences().isTaxPerDetailLine()) {
				selectTaxCode = company.getTAXCode(data.getTaxCode());
			}
			itemTaxCheck.setValue(data.isTaxable());
			if (data.getAssestsAccount() != 0) {
				assetsAccount.setSelected(getCompany().getAccount(
						data.getAssestsAccount()).getName());
			}
			if (data.getReorderPoint() != null) {
				reorderPoint.setValue(String.valueOf(data.getReorderPoint()
						.getValue()));
			}
			onHandQuantity.setValue(String.valueOf(data.getOnhandQty()
					.getValue())); // onHandQuantity.setValue("0");
			itemTotalValue.setValue(Double.toString(data.getItemTotalValue()));
			asOfDate.setValue(data.getAsOfDate());

			if (data.getComponents() != null) {
				List<ClientInventoryAssemblyItem> list = new ArrayList<ClientInventoryAssemblyItem>(
						data.getComponents());
				table.setRecords(list);
			}

		} else {
			setData(new ClientInventoryAssembly());
			table.setRecords(new ArrayList<ClientInventoryAssemblyItem>());
		}
		initAccountList();
		initVendorsList();
		initItemGroups();
		setStockPanelData();
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			if (data != null) {
				if (getPreferences().isTaxPerDetailLine())
					initTaxCodes();
			}
		}
		super.initData();
	}

	private void initItemGroups() {
		List<ClientItemGroup> clientItemgroup = getCompany().getItemGroups();

		if (clientItemgroup != null) {
			itemGroupCombo.initCombo(clientItemgroup);
			if (isInViewMode()) {
				itemGroupCombo.setComboItem(getCompany().getItemGroup(
						data.getItemGroup()));
			}
		}

		if (isInViewMode()) {
			if (data.isISellThisItem()) {
				ibuyCheck.setEnabled(false);
				disablePurchaseFormItems(true);
			}

			if (data.isIBuyThisItem()) {
				isellCheck.setEnabled(false);
				disableSalesFormItems(true);
			}

		} else {
			disableSalesFormItems(false);
			disablePurchaseFormItems(false);
		}
	}

	private void initVendorsList() {
		List<ClientVendor> clientVendor = getCompany().getActiveVendors();
		if (clientVendor != null) {
			prefVendorCombo.initCombo(clientVendor);
			if (data != null) {
				prefVendorCombo.setComboItem(getCompany().getVendor(
						data.getPreferredVendor()));
			}
			prefVendorCombo.setEnabled(!isInViewMode());
		}

	}

	private void initAccountList() {
		List<ClientAccount> listAccount = accountCombo.getFilterdAccounts();
		List<ClientAccount> listExpAccount = expAccCombo.getFilterdAccounts();
		if (listAccount != null) {
			accountCombo.initCombo(listAccount);
			expAccCombo.initCombo(listExpAccount);
		}
		if (!isInViewMode()) {
			if (data != null && data.getIncomeAccount() != 0) {
				accountCombo.setComboItem(getCompany().getAccount(
						data.getIncomeAccount()));
			}
			ClientAccount selectExpAccount = getCompany().getAccount(
					data.getExpenseAccount());
			if (selectExpAccount != null) {
				expAccCombo.setComboItem(selectExpAccount);
			}
		} else {
			expAccCombo.setEnabled(false);
			ClientAccount incomeAccount = getCompany().getAccount(
					data.getIncomeAccount());
			accountCombo.setComboItem(incomeAccount);

			ClientAccount expenseAccount = getCompany().getAccount(
					data.getExpenseAccount());
			expAccCombo.setComboItem(expenseAccount);

		}
	}

	@SuppressWarnings("unused")
	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		String name = nameText.getValue().toString();

		ClientInventoryAssembly clientItem = null;
		if (clientItem != null
				&& !(this.getData().getID() == clientItem.getID())) {
			result.addError(nameText,
					messages.anItemAlreadyExistswiththisname());
			return result;
		}

		result.add(itemForm.validate());

		if (!AccounterValidator.isSellorBuyCheck(isellCheck, ibuyCheck)) {
			result.addError(isellCheck, messages.checkAnyone());
		}
		if (isellCheck.isChecked()) {
			result.add(salesInfoForm.validate());
		}

		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			result.add(itemInfoForm.validate());
		}

		if (ibuyCheck.isChecked()) {
			result.add(purchaseInfoForm.validate());
		} else {
			if (!expAccCombo.validate()) {
				result.addError(expAccCombo,
						messages.pleaseEnter(expAccCombo.getTitle()));
			}
		}

		if (isellCheck.isChecked()) {
			if (AccounterValidator.isNegativeAmount(salesPriceText.getAmount())) {
				result.addError(salesPriceText, messages.enterValidAmount());
			}
		}

		if (ibuyCheck.isChecked()) {
			if (AccounterValidator.isNegativeAmount(purchasePriceTxt
					.getAmount())) {
				result.addError(purchasePriceTxt, messages.enterValidAmount());
			}
		}

		Long number = onHandQuantity.getNumber();
		if (number != null && number > 0) {
			if (getComponents().isEmpty()) {
				result.addError(table,
						messages.youCannotBuildWithoutComponents());
			}
		}

		if (assetsAccount != null && assetsAccount.getSelectedValue() == null) {
			result.addError(assetsAccount,
					messages.pleaseEnter(messages.assetsAccount()));
		}
		result.add(table.validateGrid());
		return result;
	}

	@Override
	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.nameText.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void onEdit() {

		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.ITEM, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		nameText.setEnabled(!isInViewMode());
		skuText.setEnabled(!isInViewMode());
		salesDescArea.setEnabled(!isInViewMode());
		salesPriceText.setEnabled(!isInViewMode());
		accountCombo.setEnabled(!isInViewMode());
		itemGroupCombo.setEnabled(!isInViewMode());
		taxCode.setEnabled(!isInViewMode());
		isellCheck.setEnabled(!isInViewMode());
		ibuyCheck.setEnabled(!isInViewMode());

		if (ibuyCheck.getValue()) {
			purchaseDescArea.setEnabled(!isInViewMode());
			expAccCombo.setEnabled(!isInViewMode());
			prefVendorCombo.setEnabled(!isInViewMode());
			purchasePriceTxt.setEnabled(!isInViewMode());
			vendItemNumText.setEnabled(!isInViewMode());
		} else {
			purchaseDescArea.setEnabled(false);
			expAccCombo.setEnabled(false);
			prefVendorCombo.setEnabled(false);
			purchasePriceTxt.setEnabled(false);
			vendItemNumText.setEnabled(false);
		}
		if (isellCheck.getValue()) {
			salesDescArea.setEnabled(!isInViewMode());
			accountCombo.setEnabled(!isInViewMode());
			salesPriceText.setEnabled(!isInViewMode());
		} else {
			salesDescArea.setEnabled(false);
			accountCombo.setEnabled(false);
			salesPriceText.setEnabled(false);
		}
		assetsAccount.setEnabled(isInViewMode());
		reorderPoint.setEnabled(isInViewMode());
		onHandQuantity.setEnabled(isInViewMode());
		totalLabel.setEnabled(isInViewMode());
		asOfDate.setEnabled(isInViewMode());

		table.setEnabled(!isInViewMode());
		// measurement.setEnabled(!isInViewMode());
		wareHouse.setEnabled(!isInViewMode());
		activeCheck.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
	}

	@Override
	protected String getViewTitle() {
		return messages.item();
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	private void settabIndexes() {
		nameText.setTabIndex(1);
		// isservice.setTabIndex(2);
		salesDescArea.setTabIndex(4);
		salesPriceText.setTabIndex(5);
		accountCombo.setTabIndex(6);
		comCheck.setTabIndex(7);
		itemGroupCombo.setTabIndex(9);
		taxCode.setTabIndex(10);
		activeCheck.setTabIndex(11);
		purchaseDescArea.setTabIndex(13);
		purchasePriceTxt.setTabIndex(14);
		expAccCombo.setTabIndex(15);
		prefVendorCombo.setTabIndex(16);
		vendItemNumText.setTabIndex(17);
		saveAndCloseButton.setTabIndex(18);
		saveAndNewButton.setTabIndex(19);
		cancelButton.setTabIndex(20);
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected void createButtons() {
		super.createButtons();
		itemTableButton = new AddNewButton(messages.addNew(messages.item()));
		itemTableButton.getElement().setAttribute("data-icon", "add");
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});
		addButton(mainVLay, itemTableButton);
	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		removeButton(mainVLay, itemTableButton);
	}
}
