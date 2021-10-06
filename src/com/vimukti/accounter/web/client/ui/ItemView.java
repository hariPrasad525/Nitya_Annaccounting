package com.vimukti.accounter.web.client.ui;

/*	 Modified by Rajesh.A, Murali.A
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoiceFrequencyGroup;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientItemStatus;
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
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.InvoiceFrequencyCombo;
import com.vimukti.accounter.web.client.ui.combo.ItemCombo;
import com.vimukti.accounter.web.client.ui.combo.ItemGroupCombo;
import com.vimukti.accounter.web.client.ui.combo.MeasurementCombo;
import com.vimukti.accounter.web.client.ui.combo.PurchaseItemCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesItemCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.company.NewConsultAction;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DoubleField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.FloatRangeValidator;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.IntegerRangeValidator;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class ItemView extends BaseView<ClientItem> {

    public static final int TYPE_SERVICE = 1;
    public static final int NON_INVENTORY_PART = 3;
    public static final int TYPE_CONSULTANT_PART = 12;
    private int type;
    private int subType;
    private TextItem nameText, skuText;
    private AmountField salesPriceText, stdCostText, purchasePriceTxt,
            openingBalTxt, itemTotalValue;
    private TextItem vendItemNumText;
    private IntegerField weightText;
    private DoubleField onHandQuantity, reorderPoint;
    private AmountField avarageCost;
    private TextAreaItem salesDescArea, purchaseDescArea;
    CheckboxItem isellCheck, comCheck, activeCheck, ibuyCheck, itemTaxCheck,
            parentItemCheck;

    private ItemGroupCombo itemGroupCombo, commodityCode, clientCombo;
    protected CustomerCombo customerCombo;

    private InvoiceFrequencyCombo invoiceFrequencyGroupCombo;
    private VendorCombo prefVendorCombo;
    private SalesItemCombo accountCombo;
    private PurchaseItemCombo expAccCombo;
    private TAXCodeCombo taxCode;
    List<ClientAccount> incomeAccounts;
    HashMap<String, ClientVendor> allvendors;
    HashMap<String, ClientAccount> allaccounts;
    HashMap<String, ClientItemGroup> allitemgroups;
    private DynamicForm itemForm;
    private DynamicForm stdCostForm;
    private DynamicForm itemInfoForm;
    private DynamicForm purchaseInfoForm;
    private DynamicForm salesInfoForm;
    private FloatRangeValidator floatRangeValidator;
    private IntegerRangeValidator integerRangeValidator;
    protected ClientAccount defaultIncomeAccount, defaultExpAccount;

    protected ClientItemGroup selectItemGroup, clientItemGroup;
    protected ClientCustomer clientCustomer;
    protected ClientInvoiceFrequencyGroup selectInvoiceFrequencyGroup;
    protected ClientVendor selectVendor;
    protected ClientTAXCode selectTaxCode;
    private final ClientCompany company;
    private boolean isGeneratedFromCustomer;
    private ArrayList<DynamicForm> listforms;
    private String name;
    private String itemName;

    private MeasurementCombo measurement;
    private WarehouseCombo wareHouse;
    private AccountCombo assetsAccount;
    private DateField asOfDate;
    private DynamicForm inventoryInfoForm;
    private Label quantityUnitsLabel;
    private ItemCombo itemCombo;

    private AddressForm addrsForm;
    private PhoneFaxForm fonFaxForm;
    private EmailForm emailForm;

    public ItemView() {
        super();
        this.getElement().setId("itemView");
        this.company = getCompany();
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

    private ClientAccount getDefaultAccount(String defaultAccount) {
        List<ClientAccount> accountList = getCompany().getActiveAccounts();
        for (ClientAccount account : accountList) {
            if (account.getName().equalsIgnoreCase(defaultAccount)) {
                return account;
            }
        }
        return null;
    }

    private void createControls() {

        listforms = new ArrayList<DynamicForm>();

        Label lab1 = new Label(messages.newProduct());
        lab1.setStyleName("label-title");

        parentItemCheck = new CheckboxItem(messages.isSubItemOf(),
                "parentItemCheck");
        parentItemCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                itemCombo.setEnabled(event.getValue());
            }

        });

        itemCombo = new ItemCombo(" ", getType(), true, true,
                isGeneratedFromCustomer());
        itemCombo.setEnabled(false);

        StyledPanel hPanel = new StyledPanel("hPanel");
        hPanel.add(lab1);
        if (this.getType() == TYPE_SERVICE) {
            nameText = new TextItem(messages.serviceName(), "nameText");
            if (this.getSubType() == TYPE_CONSULTANT_PART) {
                nameText = new TextItem(messages2.consultantName(), "nameText");
                //itemCombo.setTitle("Invoice Frequency");
                addrsForm = new AddressForm(null);
                addrsForm.setEnabled(!isInViewMode());
                addrsForm.businessSelect.setTitle("Address for Payroll");
                fonFaxForm = new PhoneFaxForm(null, null, this, this.getAction()
                        .getViewName());
                fonFaxForm.setEnabled(!isInViewMode());
                fonFaxForm.businessFaxText.setVisible(false);
                emailForm = new EmailForm(null, null, this, this.getAction()
                        .getViewName());
                emailForm.setEnabled(!isInViewMode());
                emailForm.webText.setVisible(false);
                customerCombo = new CustomerCombo(messages.payeeName(Global.get()
                        .Customer()));
                customerCombo
                        .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

                            @Override
                            public void selectedComboBoxItem(ClientCustomer selectItem) {
                                clientCustomer =selectItem;
                            }
                        });

                customerCombo.setRequired(true);
                customerCombo.setEnabled(!isInViewMode());

                clientCombo = new ItemGroupCombo("Client Name");

                clientCombo
                        .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItemGroup>() {
                            @Override
                            public void selectedComboBoxItem(ClientItemGroup selectItem) {
                                clientItemGroup = selectItem;
                            }
                        });

                itemCombo.setVisible(false);

            }
        } else if (this.getType() == NON_INVENTORY_PART) {
            nameText = new TextItem(messages.productName(), "nameText");
        } else {
            nameText = new TextItem(messages.inventoryName(), "nameText");
        }

        nameText.setValue(itemName);
        // nameText.setWidth(100);
        nameText.setRequired(true);
        nameText.setEnabled(!isInViewMode());

        floatRangeValidator = new FloatRangeValidator();
        floatRangeValidator.setMin(0);

        integerRangeValidator = new IntegerRangeValidator();
        integerRangeValidator.setMin(0);

        skuText = new TextItem(messages.upcsku(), "skuText");
        // skuText.setWidth(100);
        skuText.setEnabled(!isInViewMode());

        weightText = new IntegerField(this, messages.weight());
        // weightText.setWidth(100);
        weightText.setEnabled(!isInViewMode());
        weightText.setValidators(integerRangeValidator);
        commodityCode = new ItemGroupCombo(messages.commodityCode());
        itemForm = new DynamicForm("itemForm");
        itemForm.setStyleName("item-form-view");
        if (isInViewMode()) {
            this.setType(data.getType());
        }
        itemForm.add(nameText);

        if (getType() == ClientItem.TYPE_SERVICE) {
            if (this.getSubType() == TYPE_CONSULTANT_PART) {
                lab1.setText(messages2.consultantItem());
                weightText.setVisible(false);
                parentItemCheck.setVisible(false);
                itemForm.add(itemCombo);
                itemForm.add(emailForm);
                itemForm.add(fonFaxForm);
                itemForm.add(addrsForm);
            } else {
                lab1.setText(messages.serviceItem());
                itemForm.add(parentItemCheck, itemCombo);
            }
        } else if (getType() == NON_INVENTORY_PART) {
            lab1.setText(messages.productItem());
            itemForm.add(weightText);
            itemForm.add(parentItemCheck, itemCombo);
        } else {
            lab1.setText(messages.inventoryItem());
            itemForm.add(parentItemCheck, itemCombo);
        }

        salesDescArea = new TextAreaItem(messages.salesDescription(),
                "salesDescArea");
        // salesDescArea.setWidth(100);

        salesDescArea.setEnabled(!isInViewMode());

        salesDescArea.setToolTip(messages.writeCommentsForThis(
                this.getAction().getViewName()).replace(messages.comments(),
                messages.salesDescription()));
        salesPriceText = new AmountField(messages.salesPrice(), this,
                getBaseCurrency(), "salesPriceText");
        // salesPriceText.setWidth(100);
        salesPriceText.setEnabled(!isInViewMode());

        accountCombo = new SalesItemCombo(messages.incomeAccount());
        accountCombo.setEnabled(!isInViewMode());
        accountCombo.setPopupWidth("500px");
        accountCombo.setRequired(true);
        accountCombo
                .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
                    @Override
                    public void selectedComboBoxItem(ClientAccount selectAccount) {
                        if (selectAccount != null
                                && selectAccount != defaultIncomeAccount
                                && defaultIncomeAccount != null) {
                            if (getType() == ClientItem.TYPE_SERVICE)
                                AccounterValidator
                                        .defaultIncomeAccountServiceItem(
                                                selectAccount,
                                                defaultIncomeAccount);
                            if (getType() == ClientItem.TYPE_NON_INVENTORY_PART)
                                AccounterValidator
                                        .defaultIncomeAccountNonInventory(
                                                selectAccount,
                                                defaultIncomeAccount);
                        }
                    }
                });

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
        assetsAccount.setPopupWidth("500px");
        assetsAccount.setRequired(true);

        // ClientUnit unit =
        // Accounter.getCompany().getUnitById(row.getAdjustmentQty().getUnit());
        // unitCombo = new UnitCombo("Select Unit");
        //
        // unitCombo.setHelpInformation(true);
        // unitCombo.setEnabled(!isInViewMode());
        // unitCombo.setPopupWidth("500px");

        reorderPoint = new DoubleField(this, messages.reorderPoint());
        reorderPoint.setNumber(0.0);
        // reorderPoint.setWidth(100);
        reorderPoint.setEnabled(!isInViewMode());
        reorderPoint.setValidators(floatRangeValidator);

        itemTotalValue = new AmountField(messages.total(), this,
                getBaseCurrency(), "itemTotalValue");
        // itemTotalValue.setWidth(100);
        itemTotalValue.setAmount(0.00D);
        itemTotalValue.setEnabled(!true);

        onHandQuantity = new DoubleField(this, messages.onHandQty());
        onHandQuantity.setNumber(0.0);
        // onHandQuantity.setWidth(100);
        onHandQuantity.setEnabled(!isInViewMode());
        onHandQuantity.setValidators(floatRangeValidator);
        quantityUnitsLabel = new Label();
        quantityUnitsLabel.setText(getCompany()
                .getMeasurement(getCompany().getDefaultMeasurement())
                .getDefaultUnit().getType());
        onHandQuantity.add(quantityUnitsLabel);
        onHandQuantity.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                Double amount = stdCostText.getAmount();
                if (onHandQuantity.getValue().length() > 0) {
                    Double amount2 = Double.valueOf(onHandQuantity.getValue());
                    itemTotalValue.setAmount(amount * amount2);
                }

            }
        });

        avarageCost = new AmountField(messages.avarageCost(), this);
        avarageCost.setEnabled(false);

        asOfDate = new DateField(messages.asOf(), "asOfDate");
        asOfDate.setEnabled(!isInViewMode());
        asOfDate.setEnteredDate(new ClientFinanceDate());

        itemTotalValue.setVisible(!isInViewMode());
        avarageCost.setVisible(isInViewMode());

        /**
         * over
         */

        itemTaxCheck = new CheckboxItem(messages.taxable(), "itemTaxCheck");
        itemTaxCheck.setValue(true);
        itemTaxCheck.setEnabled(!isInViewMode());
        itemTaxCheck.setVisible(getPreferences().isTrackTax());
        comCheck = new CheckboxItem(messages.commissionItem(), "comCheck");

        salesInfoForm = UIUtils.form(messages.salesInformation());

        stdCostText = new AmountField(messages.standardCost(), this,
                getBaseCurrency(), "stdCostText");
        // stdCostText.setWidth(100);
        stdCostText.setEnabled(!isInViewMode());

        stdCostText.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                itemTotalValue.setAmount(stdCostText.getAmount()
                        * onHandQuantity.getNumber());
            }
        });

        itemGroupCombo = new ItemGroupCombo(messages.itemGroup());
        invoiceFrequencyGroupCombo = new InvoiceFrequencyCombo(messages2.invoiceGroup());
        invoiceFrequencyGroupCombo.setEnabled(!isInViewMode());
        invoiceFrequencyGroupCombo
                .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientInvoiceFrequencyGroup>() {
                    @Override
                    public void selectedComboBoxItem(ClientInvoiceFrequencyGroup selectItem) {
                        selectInvoiceFrequencyGroup = selectItem;
                    }
                });

        itemGroupCombo.setEnabled(!isInViewMode());
        itemGroupCombo
                .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItemGroup>() {
                    @Override
                    public void selectedComboBoxItem(ClientItemGroup selectItem) {
                        selectItemGroup = selectItem;
                    }
                });

        taxCode = new TAXCodeCombo(messages.taxCode(),
                isGeneratedFromCustomer());
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
        // purchaseDescArea.setWidth(100);
        purchaseDescArea.setEnabled(!isInViewMode());
        purchasePriceTxt = new AmountField(messages.purchasePrice(), this,
                getBaseCurrency(), "purchasePriceTxt");
        // purchasePriceTxt.setWidth(100);
        purchasePriceTxt.setEnabled(!isInViewMode());

        expAccCombo = new PurchaseItemCombo(
                getType() == ClientItem.TYPE_INVENTORY_PART ? messages
                        .costOfGoodSold() : messages.expenseAccount());
        expAccCombo.setRequired(true);
        expAccCombo.setEnabled(!isInViewMode());
        expAccCombo
                .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
                    @Override
                    public void selectedComboBoxItem(
                            ClientAccount selectExpAccount) {
                        if (selectExpAccount != null
                                && selectExpAccount != defaultExpAccount) {
                            if (getType() == ClientItem.TYPE_SERVICE)
                                AccounterValidator
                                        .defaultExpenseAccountServiceItem(
                                                selectExpAccount,
                                                defaultExpAccount);
                            if (getType() == ClientItem.TYPE_NON_INVENTORY_PART)
                                AccounterValidator
                                        .defaultExpenseAccountNonInventory(
                                                selectExpAccount,
                                                defaultExpAccount);
                        }
                    }
                });
        expAccCombo.setPopupWidth("500px");
        if (getType() == ClientItem.TYPE_INVENTORY_PART) {
            ClientAccount selectExpAccount = getCompany().getAccount(
                    getCompany().getCostOfGoodsSold());
            if (selectExpAccount != null) {
                expAccCombo.setComboItem(selectExpAccount);
            }
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

        vendItemNumText = new TextItem(
                this.getType() != ClientItem.TYPE_SERVICE ? messages.vendorProductNo(Global
                        .get().Vendor())
                        : messages.vendorServiceNo(Global.get().Vendor()),
                "vendorServiceNo");
        // vendItemNumText.setWidth(100);
        vendItemNumText.setEnabled(!isInViewMode());

        isellCheck = new CheckboxItem(
                this.getType() == ClientItem.TYPE_SERVICE ? messages.isellthisservice()
                        : messages.isellthisproduct(), "isellCheck");
        isellCheck.setEnabled(!isInViewMode());

        isellCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                disableSalesFormItems(!event.getValue());
                changeSubItemsCombo(event.getValue(), ibuyCheck.getValue());
            }

        });

        ibuyCheck = new CheckboxItem(
                this.getType() == ClientItem.TYPE_SERVICE ? messages.ibuythisservice()
                        : messages.ibuythisproduct(), "ibuyCheck");
        ibuyCheck.setEnabled(!isInViewMode());
        ibuyCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                disablePurchaseFormItems(!event.getValue());
                changeSubItemsCombo(isellCheck.getValue(), event.getValue());
                addPreferredCombo();
            }

        });

        if (getType() == ClientItem.TYPE_INVENTORY_PART) {
            isellCheck.setValue(true);
            ibuyCheck.setValue(true);
            ibuyCheck.setVisible(false);
        } else if (!isInViewMode()) {
            if (isGeneratedFromCustomer()) {
                isellCheck.setValue(isGeneratedFromCustomer());
                disablePurchaseFormItems(isGeneratedFromCustomer());
            } else {
                ibuyCheck.setValue(!isGeneratedFromCustomer());
                disableSalesFormItems(isGeneratedFromCustomer());
            }
        }

        // if (getPreferences().isTrackTax()
        // && getPreferences().isTaxPerDetailLine())
        // salesInfoForm.add(isellCheck, salesDescArea, salesPriceText,
        // accountCombo, comCheck, stdCostText);
        // else
        // salesInfoForm.add(isellCheck, salesDescArea,
        // salesPriceText, accountCombo, itemTaxCheck, comCheck,
        // stdCostText);
        salesInfoForm.add(isellCheck, salesDescArea, salesPriceText,
                accountCombo, itemTaxCheck, comCheck);

        this.inventoryInfoForm = new DynamicForm("inventoryInfoForm");

        if (getType() != ClientItem.TYPE_INVENTORY_PART) {
            salesInfoForm.add(stdCostText);
            inventoryInfoForm.add(assetsAccount, onHandQuantity,
                    itemTotalValue, asOfDate);
        } else {
            inventoryInfoForm.add(assetsAccount, stdCostText, onHandQuantity,
                    itemTotalValue, asOfDate, reorderPoint);
        }

        if (!getPreferences().isTrackTax()
                && getPreferences().isTaxPerDetailLine())
            salesInfoForm.add(itemTaxCheck);

        salesInfoForm.setStyleName("align-form");
        salesInfoForm.setStyleName("new_service_table");
        // salesInfoForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
        itemInfoForm = UIUtils.form(messages.itemInformation());


        if (getSubType() == ClientItem.TYPE_CONSULTANT_PART) {
            itemInfoForm.add(invoiceFrequencyGroupCombo, activeCheck);
            itemInfoForm.add(customerCombo);
        } else {
            itemInfoForm.add(itemGroupCombo, activeCheck);
        }
        if (!getPreferences().isTrackTax()
                && getPreferences().isTaxPerDetailLine())
            itemInfoForm.add(activeCheck);

        purchaseInfoForm = UIUtils.form(messages.purchaseInformation());
        // purchaseInfoForm.setNumCols(2);
        purchaseInfoForm.setStyleName("purchase_info_form");
        // if (getType() == ClientItem.TYPE_INVENTORY_PART) {
        // purchaseInfoForm.add(purchaseDescArea, purchasePriceTxt,
        // expAccCombo, prefVendorCombo, vendItemNumText);
        // } else {
        purchaseInfoForm.add(ibuyCheck, purchaseDescArea, purchasePriceTxt,
                expAccCombo);
        if (ibuyCheck.getValue() == true) {
            purchaseInfoForm.add(prefVendorCombo, vendItemNumText);
        }
        // purchaseInfoForm.getCellFormatter().addStyleName(1, 0,
        // "memoFormAlign");

        StyledPanel salesVPanel = new StyledPanel("salesVPanel");
        // salesVPanel.setWidth("100%");
        StyledPanel itemHPanel = new StyledPanel("itemHPanel");

        // itemHPanel.setCellHorizontalAlignment(itemForm, ALIGN_LEFT);

        // salesVPanel.setCellHorizontalAlignment(itemHPanel, ALIGN_LEFT);
        salesVPanel.add(salesInfoForm);
        if (getType() == ClientItem.TYPE_INVENTORY_PART) {
            salesVPanel.add(inventoryInfoForm);
        }
        StyledPanel purchzVPanel = new StyledPanel("purchzVPanel");

        // purchzVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        // purchzVPanel.setWidth("100%");
        StyledPanel itemInfoPanel = new StyledPanel("itemInfoPanel");

        // itemInfoPanel.setCellHorizontalAlignment(itemInfoForm, ALIGN_LEFT);

        purchzVPanel.add(purchaseInfoForm);

        StyledPanel topPanel1 = new StyledPanel("topPanel1");
        // topPanel1.setHorizontalAlignment(ALIGN_RIGHT);
        // topPanel1.setWidth("100%");
        topPanel1.add(itemForm);
        // topPanel1.setCellHorizontalAlignment(itemForm, ALIGN_LEFT);
        topPanel1.setStyleName("service-item-group");
        // topPanel1.setCellHorizontalAlignment(itemInfoPanel, ALIGN_LEFT);
        topPanel1.add(itemInfoForm);
        // topPanel1.setCellHorizontalAlignment(itemInfoForm, ALIGN_LEFT);
        // topPanel1.setCellWidth(itemForm, "50%");
        // topPanel1.setCellWidth(itemInfoForm, "50%");

        // topHLay.setWidth("100%");

        StyledPanel emptyPanel = new StyledPanel("emptyPanel");
        // emptyPanel.setWidth("100%");

        // topPanel2.setHorizontalAlignment(ALIGN_RIGHT);
        // topPanel2.setWidth("100%");

        // topPanel2.setCellWidth(salesVPanel, "50%");
        // topPanel2.setCellWidth(purchzVPanel, "50%");

        StyledPanel mainVLay = new StyledPanel("mainVLay");

        // mainVLay.setSize("100%", "100%");
        // mainVLay.getElement().getStyle().setMarginBottom(15, Unit.PX);
        mainVLay.add(hPanel);
        StyledPanel topHLay = getTopLayout();
        if (topHLay != null) {
            topHLay.add(topPanel1);
            StyledPanel topPanel2 = new StyledPanel("topPanel2");
            topPanel2.add(salesVPanel);
            // topPanel2.setCellHorizontalAlignment(purchzVPanel, ALIGN_LEFT);
            topPanel2.add(purchzVPanel);
            topHLay.add(topPanel2);
            mainVLay.add(topHLay);
        } else {
            mainVLay.add(topPanel1);
            mainVLay.add(salesVPanel);
            // topPanel2.setCellHorizontalAlignment(purchzVPanel, ALIGN_LEFT);
            mainVLay.add(purchzVPanel);
        }

        if (getType() == ClientItem.TYPE_INVENTORY_PART) {
            StyledPanel stockPanel_1 = getStockPanel_1();

            purchzVPanel.add(stockPanel_1);
            // purchzVPanel.setCellHorizontalAlignment(stockPanel_1,
            // ALIGN_LEFT);
            StyledPanel stockPanel_2 = getStockPanel_2();

            measurement
                    .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientMeasurement>() {

                        @Override
                        public void selectedComboBoxItem(
                                ClientMeasurement selectItem) {
                            quantityUnitsLabel.setText(selectItem
                                    .getDefaultUnit().getType());

                        }
                    });
            if (getPreferences().isUnitsEnabled()) {
                purchzVPanel.add(stockPanel_2);
                // purchzVPanel.setCellHorizontalAlignment(stockPanel_2,
                // ALIGN_LEFT);
            }
        }
        this.add(mainVLay);

        /* Adding dynamic forms in list */
        listforms.add(itemForm);
        listforms.add(salesInfoForm);

        listforms.add(stdCostForm);
        listforms.add(itemInfoForm);
        listforms.add(purchaseInfoForm);
        // settabIndexes();

    }

    protected void addPreferredCombo() {
        if (ibuyCheck.getValue() == true) {
            purchaseInfoForm.add(prefVendorCombo, vendItemNumText);
        } else {
            prefVendorCombo.removeFromParent();
            vendItemNumText.removeFromParent();
        }
    }

    protected StyledPanel getTopLayout() {
        StyledPanel topHLay = new StyledPanel("topHLay");
        topHLay.addStyleName("fields-panel");
        return topHLay;
    }

    protected void changeSubItemsCombo(Boolean iSellThis, Boolean iBuyThis) {
        itemCombo.changeComboItems(iSellThis.booleanValue(),
                iBuyThis.booleanValue());
    }

    private void getItemStatus() {

        Accounter.createHomeService().getItemStatuses(data.getWarehouse(),
                new AccounterAsyncCallback<ArrayList<ClientItemStatus>>() {

                    @Override
                    public void onException(AccounterException exception) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onResultSuccess(
                            ArrayList<ClientItemStatus> result) {
                        if (result != null && !result.isEmpty()) {
                            for (ClientItemStatus clientItemStatus : result) {
                                if (clientItemStatus.getItem() == data.getID())
                                    onHandQuantity.setValue(Double
                                            .toString(clientItemStatus
                                                    .getQuantity().getValue()));
                            }
                        }
                    }
                });

    }

    private StyledPanel getStockPanel_2() {
        StyledPanel measurementPanel = new StyledPanel("measurementPanel");
        measurement = new MeasurementCombo(messages.measurement());
        measurement.setEnabled(!isInViewMode());

        DynamicForm dynamicForm = new DynamicForm("dynamicForm");
        dynamicForm.add(measurement);

        measurementPanel.add(dynamicForm);

        return measurementPanel;
    }

    private StyledPanel getStockPanel_1() {

        StyledPanel stockPanel = new StyledPanel("stockPanel");
        DynamicForm stockForm = new DynamicForm("stockForm");

        wareHouse = new WarehouseCombo(messages.wareHouse());

        openingBalTxt = new AmountField(messages.openingBalance(), this,
                getBaseCurrency(), "openingBalTxt");
        openingBalTxt.setEnabled(!isInViewMode());
        wareHouse.setEnabled(!isInViewMode());
        stockForm.add(wareHouse);
        if (getPreferences().iswareHouseEnabled()) {
            stockPanel.add(stockForm);
        }
        listforms.add(stockForm);

        // stockPanel.setWidth("100%");
        return stockPanel;

    }

    @Override
    public ClientItem saveView() {
        //GWT.debugger();
        ClientItem saveView = super.saveView();
        if (saveView != null) {
            updateItem();
        }
        return saveView;
    }

    @Override
    public void saveAndUpdateView() {
        //GWT.debugger();
        updateItem();
        ClientItem clientItemData= (ClientItem)data;
        saveOrUpdate(clientItemData);

    }

    protected void disableSalesFormItems(Boolean isEdit) {
        salesDescArea.setEnabled(!isEdit);
        salesPriceText.setEnabled(!isEdit);
        accountCombo.setEnabled(!isEdit);
        comCheck.setEnabled(!isEdit);
    }

    /**
     * call to save Entered data on this View
     *
     * @param isSaveClose if it is true, than it means it will close View, if false,than
     *                    reset values to new View
     */
    protected void updateItem() {

        data.setActive(getBooleanValue(activeCheck));
        data.setType(getType());
        if (nameText.getValue() != null) {
            data.setName(nameText.getValue().toString());
        }
        if (parentItemCheck != null) {
            data.setSubItemOf(parentItemCheck.getValue());
            if (parentItemCheck.getValue() && itemCombo != null
                    && itemCombo.getSelectedValue() != null) {
                data.setParentItem((itemCombo.getSelectedValue()).getID());
            } else {
                data.setParentItem(0);
            }
        }
        if (selectItemGroup != null) {
            data.setItemGroup(selectItemGroup.getID());
        }
        if (clientItemGroup != null) {
            data.setClientItemGroup(clientItemGroup.getID());
        }
        if (selectInvoiceFrequencyGroup != null) {
            data.setInvoiceFrequencyGroup(selectInvoiceFrequencyGroup.getID());
        }
        data.setISellThisItem(getBooleanValue(isellCheck));
        data.setIBuyThisItem(getBooleanValue(ibuyCheck));

        data.setStandardCost(stdCostText.getAmount());

        data.setUPCorSKU(skuText.getValue());

        if ((getType() == ClientItem.TYPE_NON_INVENTORY_PART || getType() == ClientItem.TYPE_INVENTORY_PART)
                && weightText.getNumber() != null) {
            data.setWeight(UIUtils.toInt(weightText.getNumber()));
            if (assetsAccount.getSelectedValue() != null) {
                data.setAssestsAccount(assetsAccount.getSelectedValue().getID());
            }

            data.setItemTotalValue(itemTotalValue.getAmount());
            if (asOfDate != null) {
                data.setAsOfDate(asOfDate.getDate());
            }
            Double qtyValue = null;
            if (onHandQuantity.getValue().length() > 0) {
                qtyValue = Double.valueOf(onHandQuantity.getValue());
            } else {
                qtyValue = 0D;
            }
            data.setAsOfDate(asOfDate.getValue());
            Double reorderpointQtyValue = null;
            if (reorderPoint.getValue().length() > 0) {
                reorderpointQtyValue = Double.valueOf(reorderPoint.getValue());
            } else {
                reorderpointQtyValue = 0D;
            }
            if (getType() == ClientItem.TYPE_INVENTORY_PART) {
                data.setMinStockAlertLevel(null);
                data.setMaxStockAlertLevel(null);
                data.setOpeningBalance(openingBalTxt.getAmount());
                if (wareHouse.getSelectedValue() != null) {
                    data.setWarehouse(wareHouse.getSelectedValue().getID());
                }
                ClientMeasurement measurement = null;
                if (this.measurement.getSelectedValue() != null) {
                    measurement = getCompany().getMeasurement(
                            this.measurement.getSelectedValue().getId());
                } else {
                    measurement = getCompany().getMeasurement(
                            getCompany().getDefaultMeasurement());
                }
                data.setMeasurement(measurement.getId());
                if (qtyValue != null) {
                    ClientQuantity qty = new ClientQuantity();
                    qty.setValue(qtyValue.doubleValue());
                    qty.setUnit(measurement.getDefaultUnit().getId());
                    data.setOnhandQty(qty);
                }
                // Setting Reorder Point Value
                if (reorderPoint.getValue().length() > 0) {
                    ClientQuantity reorderpointQuantity = new ClientQuantity();
                    reorderpointQuantity.setValue(reorderpointQtyValue
                            .doubleValue());
                    reorderpointQuantity.setUnit(measurement.getDefaultUnit()
                            .getId());
                    data.setReorderPoint(reorderpointQuantity);
                }
            }

        }

        if (getBooleanValue(isellCheck)) {
            if (salesDescArea.getValue() != null)
                data.setSalesDescription(salesDescArea.getValue().toString());
            data.setSalesPrice(salesPriceText.getAmount());
            if (accountCombo.getSelectedValue() != null)
                data.setIncomeAccount(accountCombo.getSelectedValue().getID());
            data.setCommissionItem(getBooleanValue(comCheck));
        } else {
            if (salesDescArea.getValue() != null)
                data.setSalesDescription(salesDescArea.getValue().toString());
            data.setSalesPrice(salesPriceText.getAmount());
            if (accountCombo.getSelectedValue() != null)
                data.setIncomeAccount(accountCombo.getSelectedValue().getID());
            data.setCommissionItem(getBooleanValue(comCheck));
        }

        if (data.isIBuyThisItem()) {

            data.setPurchaseDescription(getStringValue(purchaseDescArea));
            data.setPurchasePrice(purchasePriceTxt.getAmount());
            if (selectVendor != null)
                data.setPreferredVendor(selectVendor.getID());
            if (expAccCombo.getSelectedValue() != null)
                data.setExpenseAccount(expAccCombo.getSelectedValue().getID());

            data.setVendorItemNumber(vendItemNumText.getValue().toString());
        } else {
            data.setPurchaseDescription(getStringValue(purchaseDescArea));
            data.setPurchasePrice(purchasePriceTxt.getAmount());
            if (selectVendor != null)
                data.setPreferredVendor(selectVendor.getID());
            if (expAccCombo.getSelectedValue() != null)
                data.setExpenseAccount(0);

            data.setVendorItemNumber(vendItemNumText.getValue().toString());
        }
        if (getPreferences().isTrackTax()) {
            data.setTaxable(getBooleanValue(itemTaxCheck));
        }
        if (getType() == ClientItem.TYPE_NON_INVENTORY_PART
                || getType() == ClientItem.TYPE_SERVICE)
            data.setTaxCode(selectTaxCode != null ? selectTaxCode.getID() : 0);

        if (getSubType() == ClientItem.TYPE_CONSULTANT_PART) {
            //GWT.debugger();
            data.setConsultantAddress(addrsForm.getAddrArea().getValue());
            data.setPhone(fonFaxForm.businessPhoneText.getValue());
            data.setEmail(emailForm.businesEmailText.getValue());
        }
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

    @Override
    public void saveSuccess(IAccounterCore result) {
        if (result == null) {
            saveFailed(new AccounterException());
            return;
        } else if (this.getSubType() == TYPE_CONSULTANT_PART) {

            NewConsultAction action = (NewConsultAction) this.getAction();
            action.setType(getType());
            action.setSubType(TYPE_CONSULTANT_PART);
            super.saveSuccess(result);

        }else {

            NewItemAction action = (NewItemAction) this.getAction();
            action.setType(getType());
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
        expAccCombo.setEnabled(!isDisable);
        prefVendorCombo.setEnabled(!isDisable);

        if (!isInViewMode()) {
            if (type != ClientItem.TYPE_INVENTORY_PART) {
                stdCostText.setEnabled(isDisable);
            }
        }

    }

    @Override
    public void init() {
        super.init();
        createControls();
        // setSize("100%", "100%");
    }

    @Override
    public void initData() {
        if (getData() != null) {

            nameText.setValue(data.getName());
            name = data.getName();
            parentItemCheck.setValue(data.isSubItemOf());
            ClientItem item = getCompany().getItem(data.getParentItem());
            if (item != null) {
                itemCombo.setComboItem(item);
            }
            stdCostText.setAmount(data.getStandardCost());

            weightText.setValue(String.valueOf(data.getWeight()));

            isellCheck.setValue(data.isISellThisItem());
            if (data.getSalesDescription() != null)
                salesDescArea.setValue(data.getSalesDescription());
            salesPriceText.setAmount(data.getSalesPrice());

            ClientCompany company = getCompany();
            comCheck.setValue(data.isCommissionItem());

            selectItemGroup = company.getItemGroup(data.getItemGroup());
            clientItemGroup = company.getItemGroup(data.getClientItemGroup());
            itemGroupCombo.setComboItem(selectItemGroup);

            selectInvoiceFrequencyGroup=company.getInvoiceFrequencyGroup(data.getInvoiceFrequencyGroup());
            invoiceFrequencyGroupCombo.setComboItem(selectInvoiceFrequencyGroup);

            activeCheck.setValue((data.isActive()));

            ibuyCheck.setValue(data.isIBuyThisItem());
            if (data.getPurchaseDescription() != null)
                purchaseDescArea.setValue(data.getPurchaseDescription());
            purchasePriceTxt.setAmount(data.getPurchasePrice());

            selectVendor = company.getVendor(data.getPreferredVendor());
            if (selectVendor != null) {
                prefVendorCombo.setComboItem(selectVendor);
            }
            if (data.getVendorItemNumber() != null) {
                vendItemNumText.setValue(data.getVendorItemNumber());
            }
            if (getPreferences().isTrackTax()
                    && getPreferences().isTaxPerDetailLine()) {
                selectTaxCode = company.getTAXCode(data.getTaxCode());
            }
            itemTaxCheck.setValue(data.isTaxable());
            if (getType() == ClientItem.TYPE_INVENTORY_PART
                    || getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
                if (data.getAssestsAccount() != 0) {
                    assetsAccount.setSelected(getCompany().getAccount(
                            data.getAssestsAccount()).getName());
                }
                if (data.getReorderPoint() != null) {
                    reorderPoint.setValue(String.valueOf(data.getReorderPoint()
                            .getValue()));
                }
                // onHandQuantity.setValue(Long.toString(data.getOnhandQuantity()));
                onHandQuantity.setValue(String.valueOf(data.getOnhandQty()
                        .getValue()));
                // getItemStatus();
                itemTotalValue.setValue(Double.toString(data
                        .getItemTotalValue()));
                avarageCost.setAmount(data.getAverageCost());
                asOfDate.setValue(data.getAsOfDate());
            }

        } else {
            setData(new ClientItem());
        }
        initAccountList();
        initVendorsList();
        initItemGroups();
        initInvoiceFrequencyGroups();
        if (getType() == ClientItem.TYPE_INVENTORY_PART) {
            setStockPanelData();
        }
        if (getPreferences().isTrackTax()
                && getPreferences().isTaxPerDetailLine()) {
            if (data != null) {
                if (getPreferences().isTaxPerDetailLine())
                    initTaxCodes();
            }
        }
        if (getType() == ClientItem.TYPE_INVENTORY_PART
                || getType() == ClientItem.TYPE_NON_INVENTORY_PART) {
            weightText.setValue(String.valueOf(data.getWeight()));
        }
        super.initData();

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
        this.openingBalTxt.setAmount(data.getOpeningBalance());

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
            if (getType() != ClientItem.TYPE_INVENTORY_PART) {
                disablePurchaseFormItems(isGeneratedFromCustomer());
                disableSalesFormItems(!isGeneratedFromCustomer());
            } else {
                disableSalesFormItems(false);
                disablePurchaseFormItems(false);
            }
        }
    }
    private void initInvoiceFrequencyGroups() {
        //GWT.debugger();
        List<ClientInvoiceFrequencyGroup> clientInvoiceFrequencyGroups = getCompany().getInvoiceFrequencyGroups();

        if (clientInvoiceFrequencyGroups != null) {
            invoiceFrequencyGroupCombo.initCombo(clientInvoiceFrequencyGroups);
            if (isInViewMode()) {
                invoiceFrequencyGroupCombo.setComboItem(getCompany().getInvoiceFrequencyGroup(
                        data.getInvoiceFrequencyGroup()));
            }
        }

    }
    private void initVendorsList() {
        List<ClientVendor> clientVendor = getCompany().getActiveVendors();
        if (clientVendor != null) {
            prefVendorCombo.initCombo(clientVendor);
            if (data != null && !isInViewMode()) {
                prefVendorCombo.setComboItem(getCompany().getVendor(
                        data.getPreferredVendor()));
                if (data.isIBuyThisItem() == false)
                    prefVendorCombo.setEnabled(false);
                else
                    prefVendorCombo.setEnabled(true);

            } else
                prefVendorCombo.setEnabled(false);
        }

    }

    private void initAccountList() {
        List<ClientAccount> listAccount = accountCombo.getFilterdAccounts();
        List<ClientAccount> listExpAccount = expAccCombo.getFilterdAccounts();
        if (listAccount != null) {
            accountCombo.initCombo(listAccount);
            expAccCombo.initCombo(listExpAccount);
            if (getType() == ClientItem.TYPE_SERVICE) {
                defaultIncomeAccount = getDefaultAccount(messages
                        .incomeandDistribution());
                defaultExpAccount = getDefaultAccount(messages
                        .cashDiscountTaken());
                accountCombo.setComboItem(defaultIncomeAccount);
                expAccCombo.setComboItem(defaultExpAccount);
            }
            if (getType() == ClientItem.TYPE_NON_INVENTORY_PART) {
                defaultIncomeAccount = getDefaultAccount(messages
                        .incomeandDistribution());
                defaultExpAccount = getDefaultAccount(messages
                        .cashDiscountTaken());
                accountCombo.setComboItem(defaultIncomeAccount);
                expAccCombo.setComboItem(defaultExpAccount);
            }
        }
        if (!isInViewMode()) {
            if (data != null && data.getIncomeAccount() != 0) {
                accountCombo.setComboItem(getCompany().getAccount(
                        data.getIncomeAccount()));
            }
            if (data != null && data.isISellThisItem() == false) {
                accountCombo.setEnabled(false);
                ClientAccount selectExpAccount = getCompany().getAccount(
                        data.getExpenseAccount());
                if (selectExpAccount != null) {
                    expAccCombo.setComboItem(selectExpAccount);
                }
            }
            if (data != null && data.isIBuyThisItem() == false)
                expAccCombo.setEnabled(false);
            else
                expAccCombo.setEnabled(true);

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

    @Override
    public ValidationResult validate() {
        ValidationResult result = new ValidationResult();

        String name = nameText.getValue().toString();

        ClientItem clientItem = company.getItemByName(name);
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

        if (getType() == ClientItem.TYPE_INVENTORY_PART) {
            result.add(inventoryInfoForm.validate());
        }

        // an item should not be a parent to it self.

        if (parentItemCheck.isChecked() && itemCombo.getSelectedValue() != null) {
            ClientItem selectedValue = itemCombo.getSelectedValue();
            if (nameText != null) {
                if (nameText.getValue().equalsIgnoreCase(
                        selectedValue.getName())) {
                    result.addError(itemCombo,
                            messages.youCannotMakeAnItemAItemOfIteSelf());
                }
            }

            if (data != null && data.getID() != 0) {
                // Checking for sub items.
                ClientItem parentItem = null;
                long parentItemId = selectedValue.getParentItem();
                while (parentItemId > 0) {
                    parentItem = getCompany().getItem(parentItemId);
                    if (data.getID() == parentItem.getParentItem()) {
                        result.addError(itemCombo,
                                messages.youCannotMakeAnItemAItemOfIteSelf());
                        break;
                    } else {
                        parentItemId = parentItem.getParentItem();
                    }

                }

            }
        }

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
        if (caught != null) {
            saveFailed(caught);
        } else {
            saveFailed(new AccounterException());
        }
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
        fonFaxForm.businessPhoneText.textBox.setValue(data.getPhone());
        emailForm.businesEmailText.textBox.setValue(data.getEmail());
        addrsForm.getAddrArea().setValue(data.getConsultantAddress());
        invoiceFrequencyGroupCombo.setValue(selectInvoiceFrequencyGroup);
//        customerCombo.setValue(data.get);
        
        nameText.setEnabled(!isInViewMode());
        reorderPoint.setEnabled(!isInViewMode());
        skuText.setEnabled(!isInViewMode());
        weightText.setEnabled(!isInViewMode());
        salesDescArea.setEnabled(!isInViewMode());
        salesPriceText.setEnabled(!isInViewMode());
        accountCombo.setEnabled(!isInViewMode());
        if (type != ClientItem.TYPE_INVENTORY_PART) {
            stdCostText.setEnabled(!ibuyCheck.getValue() && !isInViewMode());
        }

        itemGroupCombo.setEnabled(!isInViewMode());
        taxCode.setEnabled(!isInViewMode());
        isellCheck.setEnabled(!isInViewMode());
        ibuyCheck.setEnabled(!isInViewMode());
        itemTaxCheck.setEnabled(!isInViewMode());
        parentItemCheck.setEnabled(!isInViewMode());
        if (parentItemCheck.getValue()) {
            itemCombo.setEnabled(!isInViewMode());
        }
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

        if (getType() == ClientItem.TYPE_INVENTORY_PART) {
            // measurement.setEnabled(!isInViewMode());
            wareHouse.setEnabled(!isInViewMode());
            openingBalTxt.setEnabled(!isInViewMode());
            onHandQuantity.setRequired(false);
        }
        activeCheck.setEnabled(!isInViewMode());

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
        weightText.setTabIndex(2);
        isellCheck.setTabIndex(3);
        salesDescArea.setTabIndex(4);
        salesPriceText.setTabIndex(5);
        accountCombo.setTabIndex(6);
        comCheck.setTabIndex(7);
        stdCostText.setTabIndex(8);
        itemGroupCombo.setTabIndex(9);
        taxCode.setTabIndex(10);
        activeCheck.setTabIndex(11);
        ibuyCheck.setTabIndex(12);
        purchaseDescArea.setTabIndex(13);
        purchasePriceTxt.setTabIndex(14);
        expAccCombo.setTabIndex(15);
        prefVendorCombo.setTabIndex(16);
        vendItemNumText.setTabIndex(17);
        if (saveAndCloseButton != null)
            saveAndCloseButton.setTabIndex(18);
        if (saveAndNewButton != null)
            saveAndNewButton.setTabIndex(19);
        cancelButton.setTabIndex(20);
    }

    @Override
    protected boolean canVoid() {
        return false;
    }

    public boolean isGeneratedFromCustomer() {
        return isGeneratedFromCustomer;
    }

    public void setGeneratedFromCustomer(boolean isGeneratedFromCustomer) {
        this.isGeneratedFromCustomer = isGeneratedFromCustomer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }
}