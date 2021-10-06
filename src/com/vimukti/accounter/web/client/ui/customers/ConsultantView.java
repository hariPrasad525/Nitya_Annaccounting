package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.*;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.*;
import com.vimukti.accounter.web.client.ui.combo.*;
import com.vimukti.accounter.web.client.ui.core.*;
import com.vimukti.accounter.web.client.ui.edittable.tables.ContactsTable;
import com.vimukti.accounter.web.client.ui.forms.*;
import com.vimukti.accounter.web.client.ui.widgets.AddButton;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyChangeListener;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyComboWidget;

import java.util.*;

/*
 * @added By Ravinder Rangamgari
 *
 *
 */

public class ConsultantView extends BaseView<ClientItem> {

    /*
     * TextItem fileAsText, webText, linksText, creditLimitText, emailText,
     * phoneText, faxText;
     */
    TextItem custNameText, fileAsText;
    AmountField openingBalText, balanceText, creditLimitText;
    DateField balanceDate, customerSinceDate;
    TextItem linksText, accNameText, sortcode, accNumber, paymentref;
    TextItem vatregno;
    TextAreaItem memoArea, purchaseDescription;
    AddButton addButton;
    TAXCodeCombo custTaxCode;
    SelectCombo incomeAccount,invoiceFrequency, clientName, vendorName, expenseAccount;
    Button addCustomFieldButton;
    CustomFieldDialog customFieldDialog;

    CheckboxItem statusCheck, activeCheckBox, buyCheckBox;

    PriceLevelCombo priceLevelSelect;
    // CreditRatingCombo creditRatingSelect;

    TextItem bankAccountSelect;
    TextItem bankNameSelect;
    TextItem bankBranchSelect;
    TextItem panNumberText;
    TextItem tinNumberText;
    TextItem cstNumberText;
    TextItem serviceTaxRegistrationNo;


    ContactsTable gridView;
    SelectCombo payMethSelect;

    CurrencyComboWidget currencyCombo;
    protected ClientCurrency selectCurrency;

    // private ClientCustomer takenCustomer;

    private DynamicForm customerForm;
    private DynamicForm accInfoForm;
    private DynamicForm balanceForm;
    private DynamicForm purchaseForm;
    private AddressForm addrsForm;
    private PhoneFaxForm fonFaxForm;
    private EmailForm emailForm;

    // private ClientFiscalYear fiscalYear;
    private DynamicForm customerTabForm;
    private String selectPaymentMethodFromDetialsTab;
    protected ClientPriceLevel selectPriceLevelFromDetailsTab;
    protected ClientCreditRating selectCreditRatingFromDetailsTab;
    protected ClientShippingMethod selectShippingMethodFromDetailsTab;
    protected ClientPaymentTerms selectPayTermFromDetailsTab;
    protected ClientCustomerGroup selectCustomerGroupFromDetailsTab;
    private ClientSalesPerson selectSalesPersonFromDetailsTab;
    private ClientTAXCode selectVatCodeFromDetailsTab;

    CustomFieldForm customFieldForm;
    // protected List<ClientTaxAgency> taxAgencies = new
    // ArrayList<ClientTaxAgency>();

    protected boolean isClose;
    private boolean wait;

    private final ClientCompany company = getCompany();
    private ArrayList<DynamicForm> listforms;
    private TextItem custNoText;
    private GwtTabPanel tabSet;

    // private ClientCustomer customer;

    public ConsultantView() {
        super();
        this.getElement().setId("ConsultantView");
    }


    private void createControls() {

        tabSet = (GwtTabPanel) GWT.create(GwtTabPanel.class);
        Label title = new Label("Consultant");
        title.setStyleName("label-title");
        listforms = new ArrayList<DynamicForm>();
        tabSet.add(getGeneralTab(), messages.general());
        tabSet.add(getDetailsTab(), messages.details());
        tabSet.selectTab(0);

        StyledPanel mainVLay = new StyledPanel("mainVLay");
        mainVLay.add(title);
        mainVLay.add(getGeneralTab());

        this.add(mainVLay);

    }

    @Override
    public void saveAndUpdateView() {

        if (!wait) {
            // try {
            updateData();
            saveOrUpdate(getData());
            // } catch (Exception e) {
            // e.printStackTrace();
            // throw e;
            // }
        }

    }

    public static String objectExist(ClientCustomer customer) {

        String error = null;
        ClientCompany company = Accounter.getCompany();
        List<ClientCustomer> list = company.getCustomers();
        ClientCompanyPreferences preferences = company.getPreferences();
        if (list == null || list.isEmpty())
            return "";
        for (ClientCustomer old : list) {
            if (old.getID() == customer.getID()) {
                continue;
            }
            if (customer.getName().equalsIgnoreCase(old.getName())) {
                if (preferences.getUseCustomerId()) {
                    for (ClientCustomer old2 : list) {
                        if (customer.getNumber().equals(old2.getNumber())) {
                            error = messages
                                    .objAlreadyExistsWithNameAndNo(Global.get()
                                            .customer());
                            break;
                        }
                    }
                }
                return messages.objAlreadyExistsWithName(Global.get()
                        .customer());
            } else if (preferences.getUseCustomerId()) {
                if (customer.getNumber().equals(old.getNumber())) {
                    if (customer.getName().equalsIgnoreCase(old.getName())) {
                        error = messages.objAlreadyExistsWithNameAndNo(Global
                                .get().customer());
                        break;
                    }
                    return messages.objAlreadyExistsWithNumber(Global.get()
                            .customer());
                } else if (customer.getNumber() == null
                        || customer.getNumber().trim().length() == 0) {
                    error = messages
                            .pleaseEnterVendorNumberItShouldNotBeEmpty(Global
                                    .get().Customer());
                    break;
                } else if (checkIfNotNumber(customer.getNumber())) {
                    error = messages.payeeNumberShouldBeNumber(Global.get()
                            .customer());
                    break;
                } else if (Integer.parseInt(customer.getNumber().toString()) < 1) {
                    error = messages.payeeNumberShouldBePos(Global.get()
                            .customer());
                    break;
                }
            }
        }
        return error;
    }

    @Override
    public void saveFailed(AccounterException exception) {
        super.saveFailed(exception);
        // BaseView.errordata.setHTML(exception.getMessage());
        // BaseView.commentPanel.setVisible(true);
        // this.errorOccured = true;
        AccounterException accounterException = exception;
        String errorString = AccounterExceptions
                .getErrorString(accounterException);
        Accounter.showError(errorString);
    }

    @Override
    public void saveSuccess(IAccounterCore result) {
        if (result != null) {
            ClientCustomer customer = (ClientCustomer) result;
            if (getMode() == EditMode.CREATE) {
                customer.setBalance(customer.getOpeningBalance());
            }
            super.saveSuccess(result);

        } else {
            saveFailed(new AccounterException());
        }

    }

    protected void save() {

    }

    protected void clearFields() {

        new NewCustomerAction().run(null, false);

    }

    @Override
    public ValidationResult validate() {
        ValidationResult result = new ValidationResult();

        // validate customer form
        // check whether the customer is already available or not
        // grid valid?

        result.add(customerForm.validate());

        String name = custNameText.getValue();

        ClientVendor vendorByName = company.getVendorByName(name);

        ClientCustomer customerByName = company.getCustomerByName(name);

        ClientTAXAgency taxAgencyByName = company.getTaxAgenciesByName(name);

        if (vendorByName != null) {
            result.addError(custNameText, messages.alreadyExist());
            return result;
        }
        if (taxAgencyByName != null) {
            result.addError(custNameText, messages.alreadyExist());
            return result;
        }
        if (customerByName != null
                && !(this.getData().getID() == customerByName.getID())) {
            result.addError(custNameText, messages.alreadyExist());
            return result;
        }

        ClientFinanceDate asOfDate = balanceDate.getEnteredDate();

        gridView.validate(result);

        if (AccounterValidator.isPriorToCompanyPreventPostingDate(asOfDate)) {
            result.addError(balanceDate, messages.priorasOfDate());
        }
        data.setName(custNameText.getValue().toString());

        /*if (getPreferences().getUseVendorId()) {
            data.setNumber(custNoText.getValue().toString());

            String error = objectExist(data);
            if (error != null && !error.isEmpty()) {
                result.addError(custNoText, error);
            }
        }*/
        // gridView.validateGrid();

        return result;

        // }
    }

    // private boolean validateCustomerForm(DynamicForm customerForm)
    // throws InvalidEntryException {
    // if (!customerForm.validate(false)) {
    // if (tabSet.getTabBar().isTabEnabled(1))
    // tabSet.selectTab(0);
    // // throw new
    // // InvalidEntryException(AccounterErrorType.REQUIRED_FIELDS);
    // }
    // return true;
    // }


    @Override
    public ClientItem saveView() {
        ClientItem saveView = super.saveView();
        if (saveView != null) {
            updateData();
        }
        return saveView;
    }

    private void updateData() {

        /*customFieldForm.updateValues(data.getCustomFieldValues(), getCompany(),
                ClientPayee.TYPE_CUSTOMER);*/
        // Setting data from General Tab

        // Setting customer Name
        // customer.setName(UIUtils.toStr(custNameText.getValue()));
        data.setName(custNameText.getValue().toString());
        // setting customer number


        data.setType(ClientPayee.TYPE_CUSTOMER);
        // Setting File As
        // customer.setFileAs(UIUtils.toStr(fileAsText.getValue()));

        // Setting Addresses
        data.setAddress(addrsForm.getAddresss());


        // Setting Phone
        // customer.setPhoneNumbers(fonFaxForm.getAllPhones());
        data.setPhone(fonFaxForm.businessPhoneText.getValue().toString());

        // Setting Email and Internet
        data.setEmail(emailForm.businesEmailText.getValue().toString());

        // Setting Active
        data.setActive(activeCheckBox.getValue());

        // Setting currency
        // data.setCurrency(currencyCombo.getSelectedValue().toString());


        // Setting customer Since


        // Setting Balance
        // Setting Balance
        data.setOpeningBalance(openingBalText.getAmount());

        // data.setBalance(balanceText.getAmount());
        // Setting Balance As of
        //data.setBalanceAsOf(balanceDate.getEnteredDate().getDate());

        // Setting Contacts
        List<ClientContact> allGivenRecords = gridView.getRecords();
        // ListGridRecord selectedRecord = gridView.();
        //
        // if (selectedRecord != null) {
        // System.out.println("Selected Name is "
        // + selectedRecord.getAttribute(ATTR_CONTACT_NAME));
        //
        // }

        for (IsSerializable rec : allGivenRecords) {
            ClientContact tempRecord = (ClientContact) rec;
            ClientContact contact = new ClientContact();

            if (tempRecord == null) {
                contact.setPrimary(false);
                continue;
            }

            contact.setName(tempRecord.getName());

            contact.setTitle(tempRecord.getTitle());
            contact.setBusinessPhone(tempRecord.getBusinessPhone());
            contact.setEmail(tempRecord.getEmail());

            if (tempRecord.isPrimary() == Boolean.TRUE)
                contact.setPrimary(true);
            else
                contact.setPrimary(false);



        }



    }

    private StyledPanel getGeneralTab() {

        custNameText = new TextItem(
                messages.payeeName(Global.get().consultants()), "custNameText");
        TextBox t = new TextBox();
        if (quickAddText != null) {
            custNameText.setValue(quickAddText);
        }

        custNameText.setToolTip(messages.payeeMeaning(Global.get().consultants()));
        custNameText.setRequired(true);
        // custNameText.setWidth(100);
        custNameText.setEnabled(!isInViewMode());

        custNoText = new TextItem(
                messages.payeeNumber(Global.get().consultants()), "custNoText");
        custNoText.setRequired(getPreferences().getUseCustomerId());
        custNoText.setEnabled(!isInViewMode());

        fileAsText = new TextItem(messages.fileAs(), "fileAsText");
        fileAsText.setEnabled(!isInViewMode());
        custNameText.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                if (event.getSource() != null)
                    fileAsText.setValue(custNameText.getValue());
            }

        });

        customerForm = UIUtils.form(Global.get().consultants());

        if (getCompany().getPreferences().getUseCustomerId()) {
            customerForm.add(custNameText, custNoText);
        } else {
            customerForm.add(custNameText);
        }
        // customerForm.setWidth("100%");
        // customerForm.getCellFormatter().setWidth(0, 0, "205");

        // Element ele = DOM.createSpan();
        // ele.addClassName("star");
        // DOM.appendChild(DOM.getChild(customerForm.getElement(), 0), ele);

        statusCheck = new CheckboxItem(messages2.sellService(), "status");
        statusCheck.setValue(true);
        statusCheck.setEnabled(!isInViewMode());

        activeCheckBox = new CheckboxItem(messages.active(), "status");
        activeCheckBox.setValue(true);
        activeCheckBox.setEnabled(!isInViewMode());

        buyCheckBox = new CheckboxItem("I Buy this servcie", "status");
        buyCheckBox.setValue(true);
        buyCheckBox.setEnabled(!isInViewMode());


        customerSinceDate = new DateField(messages.payeeSince(Global.get()
                .Customer()), "customerSinceDate");
        customerSinceDate.setEnabled(!isInViewMode());
        customerSinceDate.setEnteredDate(new ClientFinanceDate());

        openingBalText = new AmountField(messages.openingBalance(), this,
                getBaseCurrency(), true);
        openingBalText.getElement().addClassName("openingBalText");
        openingBalText.setEnabled(!isInViewMode());

        balanceText = new AmountField(messages.balance(), this,
                getBaseCurrency(), "balanceText");
        balanceText.setEnabled(true);
        openingBalText.setVisible(false);
        balanceDate = new DateField(messages.balanceAsOf(), "balanceDate");
        ClientFinanceDate todaydate = new ClientFinanceDate();
        todaydate.setDay(todaydate.getDay());
        balanceDate.setDatethanFireEvent(todaydate);
        balanceDate.setEnabled(!isInViewMode());
        balanceDate.setVisible(false);


        priceLevelSelect = new PriceLevelCombo(messages.priceLevel());
        priceLevelSelect
                .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPriceLevel>() {

                    @Override
                    public void selectedComboBoxItem(ClientPriceLevel selectItem) {
                        selectPriceLevelFromDetailsTab = selectItem;

                    }

                });

        currencyCombo = createCurrencyComboWidget();
        currencyCombo.setEnabled(!isInViewMode());
        accInfoForm = new DynamicForm("accInfoForm");
        accInfoForm.add(statusCheck);

        balanceForm = new DynamicForm("balanceForm");

            balanceForm.add(openingBalText, balanceDate, balanceText);

        Map<String, String> fields = new HashMap<String, String>();
        for (String fieldName : getCompany().getCountryPreferences()
                .getCustomerFields()) {
            fields.put(fieldName, "");
        }
        addFields(fields);

        Label l1 = new Label(messages.contacts());
        l1.addStyleName("editTableTitle");
        addButton = new AddButton(messages.contact());

        addButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ClientContact clientContact = new ClientContact();
                gridView.setEnabled(true);
                if (gridView.getRecords().isEmpty()) {
                    clientContact.setPrimary(true);
                }
                gridView.add(clientContact);
            }
        });
        addButton.setEnabled(!isInViewMode());

        gridView = new ContactsTable() {

            @Override
            protected boolean isInViewMode() {
                return ConsultantView.this.isInViewMode();
            }
        };
        gridView.setEnabled(!isInViewMode());

        StyledPanel panel = new StyledPanel("panel");
        panel.add(l1);
        panel.add(gridView);
        StyledPanel hPanel = new StyledPanel("hPanel");
        hPanel.add(addButton);
        panel.add(hPanel);
        memoArea = new TextAreaItem("Description on Sales Transaction", "memo");
        //memoArea.setToolTip(messages.writeCommentsForThis("Description on Sales Transaction"));
        DynamicForm memoForm = new DynamicForm("memoForm");
        memoForm.add(memoArea);

        // For Editing customer

        addrsForm = new AddressForm(null);
        addrsForm.setEnabled(!isInViewMode());
        //addrsForm.businessSelect.setTitle("Address for Payroll");
        fonFaxForm = new PhoneFaxForm(null, null, this, this.getAction()
                .getViewName());
        fonFaxForm.setEnabled(!isInViewMode());
        fonFaxForm.businessFaxText.setVisible(false);
        emailForm = new EmailForm(null, null, this, this.getAction()
                .getViewName());
        emailForm.setEnabled(!isInViewMode());
        emailForm.webText.setVisible(false);

        incomeAccount = new SelectCombo("Income Account ",true);
        //incomeAccount.
        /* Adding Dynamic Forms in List */
        listforms.add(customerForm);
        listforms.add(accInfoForm);
        listforms.add(balanceForm);
        listforms.add(memoForm);
        StyledPanel leftVLay = new StyledPanel("leftVLay");
        leftVLay.add(customerForm);
        leftVLay.add(emailForm);
        leftVLay.add(fonFaxForm);
        leftVLay.add(addrsForm);

        leftVLay.add(accInfoForm);
        leftVLay.add(memoForm);

        if (isMultiCurrencyEnabled()) {
            leftVLay.add(currencyCombo);
        }
        leftVLay.add(balanceForm);
        leftVLay.add(incomeAccount);
        // leftVLay.add(fonFaxForm);
        // leftVLay.add(emailForm);

        invoiceFrequency = new SelectCombo("Invoicing Frequency",true);
        clientName = new SelectCombo("Client Name",true);
        vendorName = new SelectCombo("Vendor Name",true);
        purchaseDescription = new TextAreaItem("Description on Purchase Transaction", "memo");
        /*creditLimitText = new AmountField("Purchase Price (USD)", this,
                getBaseCurrency(), "creditLimitText");*/
        expenseAccount = new SelectCombo("Expense Account",true);
        List<String> accTypeMap = new ArrayList<String>();
        expenseAccount.initCombo(accTypeMap);
        StyledPanel rightVLay = new StyledPanel("rightVLay");
        // rightVLay.setWidth("100%");
        rightVLay.add(invoiceFrequency);
        rightVLay.add(activeCheckBox);
        rightVLay.add(clientName);
        rightVLay.add(buyCheckBox);
        rightVLay.add(vendorName);
        rightVLay.add(balanceForm);
        rightVLay.add(expenseAccount);
        //rightVLay.add(expenseAccount);

        StyledPanel contHLay = new StyledPanel("contHLay");

        StyledPanel mainVlay = new StyledPanel("mainVlay");
        StyledPanel topHLay = getTopLayOut();
        if (topHLay != null) {
            topHLay.add(leftVLay);
            topHLay.add(rightVLay);
            mainVlay.add(topHLay);
        } else {
            mainVlay.add(leftVLay);
            mainVlay.add(rightVLay);
        }
        mainVlay.add(contHLay);
        //mainVlay.add(panel);
        //mainVlay.add(memoForm);
        // mainVlay.add(memoForm);
        //memoForm.setEnabled(!isInViewMode());
        mainVlay.setStyleName("generalTab");

        // if (UIUtils.isMSIEBrowser())
        // resetFromView();

        return mainVlay;

    }

    protected StyledPanel getTopLayOut() {
        StyledPanel topHLay = new StyledPanel("topHLay");
        topHLay.addStyleName("fields-panel");
        return topHLay;
    }

    private void resetFromView() {
        // addrsForm.getCellFormatter().setWidth(0, 0, "75");
        // addrsForm.getCellFormatter().setWidth(0, 1, "125");
        //
        // fonFaxForm.getCellFormatter().setWidth(0, 0, "75");
        // fonFaxForm.getCellFormatter().setWidth(0, 1, "125");
        //
        // emailForm.getCellFormatter().setWidth(0, 0, "190");
        // emailForm.getCellFormatter().setWidth(0, 1, "150");
        //
        // memoArea.getMainWidget().setWidth("250px");

    }

    protected void adjustFormWidths(int titlewidth, int listBoxWidth) {

        // addrsForm.getCellFormatter().getElement(0, 0).setAttribute("width",
        // titlewidth + "");
        //
        // addrsForm.getCellFormatter().getElement(0, 1).setAttribute(
        // messages.width(), "185px");
        //
        // fonFaxForm.getCellFormatter().getElement(0, 0).setAttribute(
        // messages.width(), "240px");
        // fonFaxForm.getCellFormatter().getElement(0, 1).setAttribute(
        // FinanceApplication.constants().width(), "185px");

        // customerForm.getCellFormatter().getElement(0, 0).getStyle().setWidth(
        // 150, Unit.PX);
        // emailForm.getCellFormatter().getElement(0, 0).setAttribute(
        // messages.width(), "240px");
        // emailForm.getCellFormatter().getElement(0, 1).setAttribute(
        // FinanceApplication.constants().width(), "");
        // accInfoForm.getCellFormatter().getElement(0, 0).setAttribute(
        // messages.width(), "150px");

    }

    private StyledPanel getDetailsTab() {


        // DynamicForm salesForm = UIUtils.form(messages.sales());
        // salesForm.setFields(salesPersonSelect);
        // salesForm.setWidth("100%");

        creditLimitText = new AmountField(messages.creditLimit(), this,
                getBaseCurrency(), "creditLimitText");

        // creditRatingSelect = new CreditRatingCombo(messages.creditRating());
        // creditRatingSelect
        // .addSelectionChangeHandler(new
        // IAccounterComboSelectionChangeHandler<ClientCreditRating>() {
        //
        // @Override
        // public void selectedComboBoxItem(
        // ClientCreditRating selectItem) {
        // selectCreditRatingFromDetailsTab = selectItem;
        // }
        //
        // });

        bankAccountSelect = new TextItem(messages.bankAccountNumber(),
                "bankAccountSelect");
        bankNameSelect = new TextItem(messages.bankName(), "bankNameSelect");
        bankBranchSelect = new TextItem(messages.bankBranch(),
                "bankBranchSelect");
        panNumberText = new TextItem(messages.panNumber(), "panNumberText");
        cstNumberText = new TextItem(messages.cstNumber(), "cstNumberText");
        serviceTaxRegistrationNo = new TextItem(
                messages.serviceTaxRegistrationNumber(),
                "serviceTaxRegistrationNo");
        tinNumberText = new TextItem(messages.tinNumber(), "tinNumberText");

        DynamicForm financeDitailsForm = UIUtils.form(messages
                .financialDetails());


        if (getPreferences().isTrackTax()) {

            if (getCountryPreferences().isSalesTaxAvailable()) {
                financeDitailsForm.add(cstNumberText);
            }
            if (getCountryPreferences().isServiceTaxAvailable()) {
                financeDitailsForm.add(serviceTaxRegistrationNo);
            }
            if (getCountryPreferences().isTDSAvailable()) {
                financeDitailsForm.add(tinNumberText);
            }

        }


        payMethSelect = UIUtils.getPaymentMethodCombo();
        // payMethSelect.setWidth(100);

        payMethSelect
                .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
                    @Override
                    public void selectedComboBoxItem(String selectItem) {
                        selectPaymentMethodFromDetialsTab = payMethSelect
                                .getSelectedValue();
                    }
                });
        selectPaymentMethodFromDetialsTab = payMethSelect.getSelectedValue();


        vatregno = new TextItem(messages.taxRegNo(), "vatregno");
        if (!getCountryPreferences().isServiceTaxAvailable()) {
            vatregno.setTitle(messages.vatRegistrationNumber());
        }
        custTaxCode = new TAXCodeCombo(messages.taxCode(), true);
        custTaxCode
                .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

                    @Override
                    public void selectedComboBoxItem(ClientTAXCode selectItem) {
                        selectVatCodeFromDetailsTab = selectItem;
                    }

                });

        DynamicForm termsForm = UIUtils.form(messages.terms());
        customFieldForm = UIUtils.CustomFieldsform(messages.terms());


        creditLimitText.setEnabled(!isInViewMode());
        priceLevelSelect.setEnabled(!isInViewMode());
        // creditRatingSelect.setEnabled(!isInViewMode());
        bankAccountSelect.setEnabled(!isInViewMode());
        bankNameSelect.setEnabled(!isInViewMode());
        bankBranchSelect.setEnabled(!isInViewMode());
        panNumberText.setEnabled(!isInViewMode());
        cstNumberText.setEnabled(!isInViewMode());
        serviceTaxRegistrationNo.setEnabled(!isInViewMode());
        tinNumberText.setEnabled(!isInViewMode());
        payMethSelect.setEnabled(!isInViewMode());
        vatregno.setEnabled(!isInViewMode());
        custTaxCode.setEnabled(!isInViewMode());

        addCustomFieldButton = new Button();
        addCustomFieldButton.setText(messages.ManageCustomFields());
        addCustomFieldButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                customFieldDialog = new CustomFieldDialog(ConsultantView.this,
                        messages.CustomField(), messages.ManageCustomFields());
                ViewManager.getInstance().showDialog(customFieldDialog);
            }
        });
        addCustomFieldButton.setEnabled(!isInViewMode());

        StyledPanel leftVLay = new StyledPanel("leftVLay");
        StyledPanel rightVLay = new StyledPanel("rightVLay");
        StyledPanel customField = new StyledPanel("customField");
        Label customLable = new Label(messages.CustomFieldstext());
        customField.add(customLable);
        customField.add(addCustomFieldButton);
        rightVLay.add(termsForm);
        rightVLay.add(customField);
        rightVLay.add(customFieldForm);

        leftVLay.add(financeDitailsForm);

        StyledPanel topHLay = new StyledPanel("detailsTab");
        topHLay.add(leftVLay);
        topHLay.add(rightVLay);

        listforms.add(financeDitailsForm);
        listforms.add(termsForm);
        listforms.add(customFieldForm);

        return topHLay;
    }

    @Override
    public void init() {
        super.init();
        createControls();
    }

    @Override
    public void initData() {
        // if (takenCustomer == null)
        // initFiscalYear();
        if (data == null) {
            setData(new ClientItem());
        }

        // initTaxAgenciesList();
        initMainValues();
        // initCreditRatingList();
        // initPriceLevelList();
        if (data != null && data.getPhone() != null)
            data.setPhone(data.getPhone());

        super.initData();

    }

    private void setPayeeFields(HashMap<String, String> payeeFields) {
        for (String key : payeeFields.keySet()) {
            itemsField.get(key).setValue(payeeFields.get(key));
            itemsField.get(key).setEnabled(!isInViewMode());
        }
    }

    private void initMainValues() {
        // Setting Customer Name
        custNameText.setValue(data.getName());
        // Setting customer number
        if (getPreferences().getUseCustomerId()
                && (data.getID() == 0 )) {
            Accounter.createHomeService().getCustomerNumber(
                    new AccounterAsyncCallback<String>() {

                        @Override
                        public void onResultSuccess(String result) {
                            custNoText.setValue(result);
                        }

                        @Override
                        public void onException(AccounterException caught) {
                        }
                    });
        } else {
            //custNoText.setValue(data.getNumber());
            balanceDate.setEnabled(!isInViewMode());
        }
        // Setting File as
        //fileAsText.setValue(data.getFileAs());
        fonFaxForm.businessPhoneText.setValue(data.getPhone());
        emailForm.businesEmailText.setValue(data.getEmail());

        // Setting Status Check
        statusCheck.setValue(data.isActive());

        // Setting Customer Since
        openingBalText.setAmount(data.getOpeningBalance());
        balanceText.setAmount(data.getOpeningBalance());

        // Setting Balance as of




        // Setting AddressForm
        addrsForm.setAddress(data.getAddress());
        // Setting Contacts
        // gridView.setAllRows(new
        // ArrayList<ClientContact>(data.getContacts()));

        int row = 0;


        // Setting salesPerson
    }

    @Override
    protected void onLoad() {
        super.onLoad();
    }

    @Override
    protected void onAttach() {

        super.onAttach();
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
        this.custNameText.setFocus();
        // this.custNoText.setFocus();
    }

    @Override
    public void deleteFailed(AccounterException caught) {

    }

    @Override
    public void deleteSuccess(IAccounterCore result) {

    }

    @Override
    public void fitToSize(int height, int width) {
        super.fitToSize(height, width);
    }

    @Override
    public void onEdit() {
        AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

            @Override
            public void onException(AccounterException caught) {
                int errorCode = caught.getErrorCode();

                Accounter.showError(AccounterExceptions
                        .getErrorString(errorCode));
            }

            @Override
            public void onResultSuccess(Boolean result) {
                if (result)
                    enableFormItems();
            }

        };

        this.rpcDoSerivce.canEdit(AccounterCoreType.CUSTOMER, data.getID(),
                editCallBack);

    }

    private void enableFormItems() {
        setMode(EditMode.EDIT);
        custNameText.setEnabled(!isInViewMode());
        addButton.setEnabled(!isInViewMode());
        custNoText.setEnabled(!isInViewMode());
        customerSinceDate.setEnabled(!isInViewMode());
        openingBalText.setEnabled(!isInViewMode());
        balanceDate.setEnabled(!isInViewMode());
        // balanceText.setDisabled(!data.isOpeningBalanceEditable());
        addrsForm.setEnabled(!isInViewMode());
        statusCheck.setEnabled(!isInViewMode());
        fonFaxForm.setEnabled(!isInViewMode());
        emailForm.setEnabled(!isInViewMode());
        gridView.setEnabled(!isInViewMode());
        creditLimitText.setEnabled(!isInViewMode());
        priceLevelSelect.setEnabled(!isInViewMode());
        // creditRatingSelect.setEnabled(isInViewMode());
        // currencyCombo.setDisabled(!isInViewMode(), isInViewMode());
        // if (!selectCurrency.equals(getCompany().getPreferences()
        // .getPrimaryCurrency())) {
        // currencyCombo.disabledFactorField(false);
        // }
        bankAccountSelect.setEnabled(!isInViewMode());
        bankNameSelect.setEnabled(!isInViewMode());
        bankBranchSelect.setEnabled(!isInViewMode());
        panNumberText.setEnabled(!isInViewMode());
        cstNumberText.setEnabled(!isInViewMode());
        serviceTaxRegistrationNo.setEnabled(!isInViewMode());
        tinNumberText.setEnabled(!isInViewMode());
        payMethSelect.setEnabled(!isInViewMode());
        vatregno.setEnabled(!isInViewMode());
        custTaxCode.setEnabled(!isInViewMode());
        customFieldForm.setEnabled(isInViewMode());
        addCustomFieldButton.setEnabled(!isInViewMode());
        activeCheckBox.setEnabled(!isInViewMode());
        memoArea.setDisabled(isInViewMode());
        customFieldForm.setEnabled(!isInViewMode());
        super.onEdit();

    }

    private void enablePayeeFields(HashMap<String, String> payeeFields) {
        for (String key : payeeFields.keySet()) {
            itemsField.get(key).setEnabled(!isInViewMode());
        }
    }

    @Override
    public void print() {
        // TODO Auto-generated method stub

    }

    @Override
    public void printPreview() {
        // NOTHING TO DO
    }

    @Override
    protected String getViewTitle() {
        return messages.customer();
    }

    protected CurrencyComboWidget createCurrencyComboWidget() {
        ArrayList<ClientCurrency> currenciesList = getCompany().getCurrencies();
        ClientCurrency baseCurrency = getCompany().getPrimaryCurrency();
        CurrencyComboWidget widget = new CurrencyComboWidget(currenciesList,
                baseCurrency);
        widget.setListener(new CurrencyChangeListener() {

            @Override
            public void currencyChanged(ClientCurrency currency, double factor) {
                selectCurrency = currency;
                openingBalText.setCurrency(selectCurrency);
                balanceText.setCurrency(selectCurrency);
                creditLimitText.setCurrency(selectCurrency);
            }

        });
        widget.setEnabled(isInViewMode());
        return widget;
    }

    @Override
    protected boolean canVoid() {
        return false;
    }

    Map<String, TextItem> itemsField = new HashMap<String, TextItem>();

    private void addFields(Map<String, String> payeeFields) {
        itemsField.clear();
        for (String key : payeeFields.keySet()) {
            String value = payeeFields.get(key);
            TextItem item = new TextItem(key, "key");
            item.setValue(value);
            item.setTitle(key);
            balanceForm.add(item);
            itemsField.put(key, item);
            item.setEnabled(!isInViewMode());
        }
    }

}
