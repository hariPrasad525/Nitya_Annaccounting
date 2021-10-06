package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientContact;
import com.nitya.accounter.web.client.core.ClientCreditRating;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientCustomFieldValue;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientCustomerGroup;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.ClientPaymentTerms;
import com.nitya.accounter.web.client.core.ClientPriceLevel;
import com.nitya.accounter.web.client.core.ClientSalesPerson;
import com.nitya.accounter.web.client.core.ClientShippingMethod;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.AddressForm;
import com.nitya.accounter.web.client.ui.CustomFieldDialog;
import com.nitya.accounter.web.client.ui.EmailForm;
import com.nitya.accounter.web.client.ui.GwtTabPanel;
import com.nitya.accounter.web.client.ui.PhoneFaxForm;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.CustomerGroupCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.nitya.accounter.web.client.ui.combo.PriceLevelCombo;
import com.nitya.accounter.web.client.ui.combo.SalesPersonCombo;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.combo.ShippingMethodsCombo;
import com.nitya.accounter.web.client.ui.combo.TAXCodeCombo;
import com.nitya.accounter.web.client.ui.core.AccounterValidator;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.edittable.tables.ContactsTable;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.CustomFieldForm;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.widgets.AddButton;
import com.nitya.accounter.web.client.ui.widgets.CurrencyChangeListener;
import com.nitya.accounter.web.client.ui.widgets.CurrencyComboWidget;

/*
 * @modified by Rajesh.A,Ravi Kiran.G, Murali Annamneni,B.srinivasa rao
 * 
 * 
 */

public class CustomerView extends BaseView<ClientCustomer> {

	/*
	 * TextItem fileAsText, webText, linksText, creditLimitText, emailText,
	 * phoneText, faxText;
	 */
	TextItem custNameText, fileAsText;
	AmountField openingBalText, balanceText, creditLimitText;
	DateField balanceDate, customerSinceDate;
	TextItem linksText, accNameText, sortcode, accNumber, paymentref;
	TextItem vatregno;
	TextAreaItem memoArea;
	AddButton addButton;
	TAXCodeCombo custTaxCode;
	Button addCustomFieldButton;
	CustomFieldDialog customFieldDialog;

	CheckboxItem statusCheck, selectCheckBox, tdsCheckBox;

	PriceLevelCombo priceLevelSelect;
	// CreditRatingCombo creditRatingSelect;

	TextItem bankAccountSelect;
	TextItem bankNameSelect;
	TextItem bankBranchSelect;
	TextItem panNumberText;
	TextItem tinNumberText;
	TextItem cstNumberText;
	TextItem serviceTaxRegistrationNo;

	ShippingMethodsCombo shipMethSelect;
	PaymentTermsCombo payTermsSelect;
	CustomerGroupCombo custGroupSelect;
	SalesPersonCombo salesPersonSelect;

	ContactsTable gridView;
	SelectCombo payMethSelect;

	CurrencyComboWidget currencyCombo;
	protected ClientCurrency selectCurrency;

	// private ClientCustomer takenCustomer;

	private DynamicForm customerForm;
	private DynamicForm accInfoForm;
	private DynamicForm balanceForm;
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

	public CustomerView() {
		super();
		this.getElement().setId("CustomerView");
	}

	// private void initFiscalYear() {
	// for (ClientFiscalYear fiscalYear : company.getFiscalYears()) {
	// if (fiscalYear.getIsCurrentFiscalYear()) {
	// CustomerView.this.fiscalYear = fiscalYear;
	// // balanceDate.setEnteredDate(CustomerView.this.fiscalYear
	// // .getStartDate());
	// break;
	// }
	// }
	// }

	private void initSalesPersonList() {
		salesPersonSelect.initCombo(company.getActiveSalesPersons());
		if (data != null && data.getSalesPerson() != 0)
			salesPersonSelect.setComboItem(company.getSalesPerson(data
					.getSalesPerson()));

	}

	// private void initTaxItemGroupList() {
	// List<ClientTAXItemGroup> result = getCompany().getTaxItemGroups();
	//
	// for (ClientTAXItemGroup taxItemGroup : result) {
	// if (taxItemGroup.getName().equalsIgnoreCase("None")) {
	// selectTaxGroupFromDetailsTab = taxItemGroup;
	// }
	// }
	// if (data != null && data.getTaxGroup() != null)
	// taxGroupSelect.setComboItem(company.getTAXItemGroup(data
	// .getTaxItemGroups()));
	// else
	// taxGroupSelect.setComboItem(selectTaxGroupFromDetailsTab);
	// }

	private void initCustomerGroupList() {

		custGroupSelect.initCombo(getCompany().getCustomerGroups());

		// Setting Customer Group
		if (data != null && data.getCustomerGroup() != 0)
			custGroupSelect.setComboItem(company.getCustomerGroup(data
					.getCustomerGroup()));

	}

	public void initPaymentTermsList() {
		payTermsSelect.initCombo(getCompany().getPaymentsTerms());
		// Setting Payment Term
		if (data != null && data.getPaymentTerm() != 0)
			payTermsSelect.setComboItem(company.getPaymentTerms(data
					.getPaymentTerm()));

	}

	public void initShippingMethodList() {

		shipMethSelect.initCombo(getCompany().getShippingMethods());
		// Setting Preferred Shipping Method
		if (data != null && data.getShippingMethod() != 0)
			shipMethSelect.setComboItem(company.getShippingMethod(data
					.getShippingMethod()));
	}

	// private void initCreditRatingList() {
	// creditRatingSelect.initCombo(company.getCreditRatings());
	//
	// if (data != null && data.getCreditRating() != 0)
	// creditRatingSelect.setComboItem(company.getCreditRating(data
	// .getCreditRating()));
	// }

	private void initPriceLevelList() {

		priceLevelSelect.initCombo(getCompany().getPriceLevels());
		// Setting Preferred Shipping Method
		if (data != null && data.getPriceLevel() != 0)
			priceLevelSelect.setComboItem(company.getPriceLevel(data
					.getPriceLevel()));
	}

	private void initVatCodeList() {
		List<ClientTAXCode> taxcodes = company.getActiveTaxCodes();
		if (taxcodes != null)
			custTaxCode.initCombo(taxcodes);
		if (data != null && data.getTAXCode() > 0)
			custTaxCode.setComboItem(company.getTAXCode(data.getTAXCode()));
	}

	private void createControls() {

		tabSet = (GwtTabPanel) GWT.create(GwtTabPanel.class);
		Label title = new Label(Global.get().Customer());
		title.setStyleName("label-title");
		listforms = new ArrayList<DynamicForm>();
		tabSet.add(getGeneralTab(), messages.general());
		tabSet.add(getDetailsTab(), messages.details());
		tabSet.selectTab(0);
		createCustomFieldControls();

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(title);
		mainVLay.add(tabSet.getPanel());

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

		if (getPreferences().getUseVendorId()) {
			data.setNumber(custNoText.getValue().toString());

			String error = objectExist(data);
			if (error != null && !error.isEmpty()) {
				result.addError(custNoText, error);
			}
		}
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
	public void createCustomFieldControls() {
		if (data != null && data.getCustomFieldValues() != null) {
			customFieldForm.updateValues(data.getCustomFieldValues(),
					getCompany(), ClientPayee.TYPE_CUSTOMER);
		}
		customFieldForm.createControls(getCompany(),
				data == null ? null : data.getCustomFieldValues(),
				ClientPayee.TYPE_CUSTOMER);
		Set<ClientCustomFieldValue> customFieldValues = data == null ? new HashSet<ClientCustomFieldValue>()
				: data.getCustomFieldValues();
		Set<ClientCustomFieldValue> deleteCustomFieldValues = new HashSet<ClientCustomFieldValue>();
		for (ClientCustomFieldValue value : customFieldValues) {
			if (getCompany().getClientCustomField(value.getID()) == null) {
				deleteCustomFieldValues.add(value);
			}
		}

		for (ClientCustomFieldValue clientCustomFieldValue : deleteCustomFieldValues) {
			customFieldValues.remove(clientCustomFieldValue);
		}
		customFieldForm.setEnabled(!isInViewMode());
	}

	@Override
	public ClientCustomer saveView() {
		ClientCustomer saveView = super.saveView();
		if (saveView != null) {
			updateData();
		}
		return saveView;
	}

	private void updateData() {

		customFieldForm.updateValues(data.getCustomFieldValues(), getCompany(),
				ClientPayee.TYPE_CUSTOMER);
		// Setting data from General Tab

		// Setting customer Name
		// customer.setName(UIUtils.toStr(custNameText.getValue()));
		data.setName(custNameText.getValue().toString());
		// setting customer number
		if (custNoText.getValue() != null) {
			data.setNumber(custNoText.getValue().toString());
		}

		data.setType(ClientPayee.TYPE_CUSTOMER);
		// Setting File As
		// customer.setFileAs(UIUtils.toStr(fileAsText.getValue()));

		data.setFileAs(fileAsText.getValue().toString());
		// Setting Addresses
		data.setAddress(addrsForm.getAddresss());

		if (isMultiCurrencyEnabled()) {
			data.setCurrency(currencyCombo.getSelectedCurrency().getID());
		}
		data.setCurrencyFactor(currencyCombo.getCurrencyFactor());

		// Setting Phone
		// customer.setPhoneNumbers(fonFaxForm.getAllPhones());
		data.setPhoneNo(fonFaxForm.businessPhoneText.getValue().toString());

		// Setting Fax
		// customer.setFaxNumbers(fonFaxForm.getAllFaxes());
		data.setFaxNo(fonFaxForm.businessFaxText.getValue().toString());

		// Setting Email and Internet
		data.setEmail(emailForm.businesEmailText.getValue().toString());

		// Setting web page Address
		data.setWebPageAddress(emailForm.getWebTextValue());

		// Setting Active
		data.setActive(statusCheck.getValue());

		// Setting accout number
		data.setBankAccountNo(bankAccountSelect.getValue().toString());

		// Setting currency
		// data.setCurrency(currencyCombo.getSelectedValue().toString());

		// Setting Bank name
		data.setBankName(bankNameSelect.getValue().toString());
		// Setting Branch name
		data.setBankBranch(bankBranchSelect.getValue().toString());
		if (getPreferences().isTrackTax()) {
			// setting Pan Number
			// data.setPanNumber(panNumberText.getValue().toString());
			if (getCountryPreferences().isSalesTaxAvailable()) {
				// setting for CST Number
				data.setCstNumber(cstNumberText.getValue().toString());
			}
			if (getCountryPreferences().isTDSAvailable()) {
				// setting for TIN Number
				data.setTinNumber(tinNumberText.getValue().toString());
			}
			if (getCountryPreferences().isServiceTaxAvailable()) {
				// setting for Service tax Num
				data.setServiceTaxRegistrationNumber(serviceTaxRegistrationNo
						.getValue().toString());
			}

			data.setDeductTDS(tdsCheckBox.getValue());
		}
		// Setting customer Since
		if (customerSinceDate != null
				&& customerSinceDate.getEnteredDate() != null)
			data.setPayeeSince(customerSinceDate.getEnteredDate().getDate());

		// Setting Balance
		// Setting Balance
		data.setOpeningBalance(openingBalText.getAmount());

		// data.setBalance(balanceText.getAmount());
		// Setting Balance As of
		data.setBalanceAsOf(balanceDate.getEnteredDate().getDate());

		// Setting Contacts
		List<ClientContact> allGivenRecords = gridView.getRecords();
		// ListGridRecord selectedRecord = gridView.();
		//
		// if (selectedRecord != null) {
		// System.out.println("Selected Name is "
		// + selectedRecord.getAttribute(ATTR_CONTACT_NAME));
		//
		// }
		Set<ClientContact> allContacts = new HashSet<ClientContact>();

		if (allGivenRecords.isEmpty()) {
			data.setContacts(allContacts);
		}
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

			if (!contact.getName().equals("") || !contact.getTitle().equals("")
					|| !contact.getBusinessPhone().equals("")
					|| !contact.getEmail().equals("")) {
				allContacts.add(contact);

			}
			data.setContacts(allContacts);

		}

		// Setting Memo
		if (memoArea.getValue() != null)
			data.setMemo(memoArea.getValue().toString());

		// Setting Data from Details Tab

		// Setting SalesPerson
		data.setSalesPerson(Utility.getID(selectSalesPersonFromDetailsTab));

		// Setting Credit Limit

		if (creditLimitText.getAmount() != null) {
			data.setCreditLimit(creditLimitText.getAmount());
		}

		// Setting Price Level
		data.setPriceLevel(Utility.getID(selectPriceLevelFromDetailsTab));

		// Setting Credit Rating
		data.setCreditRating(Utility.getID(selectCreditRatingFromDetailsTab));

		// Setting Preferred Shipping Method
		data.setShippingMethod(Utility
				.getID(selectShippingMethodFromDetailsTab));

		// Setting Preferred Payment Method
		data.setPaymentMethod(selectPaymentMethodFromDetialsTab);

		// Setting Preferred Payment Terms
		data.setPaymentTerm(Utility.getID(selectPayTermFromDetailsTab));

		// Setting customer Group
		data.setCustomerGroup(Utility.getID(selectCustomerGroupFromDetailsTab));
		if (getPreferences().isTrackTax()) {
			// Setting Tax Group
			data.setTAXCode(Utility.getID(selectVatCodeFromDetailsTab));
			if (vatregno.getValue() != null)
				data.setVATRegistrationNumber(vatregno.getValue().toString());
		}

		HashMap<String, String> payeeFields = new HashMap<String, String>();
		for (String fieldName : itemsField.keySet()) {
			payeeFields.put(fieldName, itemsField.get(fieldName).getValue());
		}
		data.setPayeeFields(payeeFields);

	}

	private StyledPanel getGeneralTab() {

		custNameText = new TextItem(
				messages.payeeName(Global.get().Customer()), "custNameText");
		TextBox t = new TextBox();
		if (quickAddText != null) {
			custNameText.setValue(quickAddText);
		}

		custNameText.setToolTip(messages.payeeMeaning(Global.get().customer()));
		custNameText.setRequired(true);
		// custNameText.setWidth(100);
		custNameText.setEnabled(!isInViewMode());

		custNoText = new TextItem(
				messages.payeeNumber(Global.get().Customer()), "custNoText");
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

		customerForm = UIUtils.form(Global.get().customer());

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

		statusCheck = new CheckboxItem(messages.active(), "status");
		statusCheck.setValue(true);
		statusCheck.setEnabled(!isInViewMode());

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
		balanceText.setEnabled(false);
		balanceDate = new DateField(messages.balanceAsOf(), "balanceDate");
		ClientFinanceDate todaydate = new ClientFinanceDate();
		todaydate.setDay(todaydate.getDay());
		balanceDate.setDatethanFireEvent(todaydate);
		balanceDate.setEnabled(!isInViewMode());
		// balanceDate.addDateValueChangeHandler(new DateValueChangeHandler() {
		//
		// @Override
		// public void onDateValueChange(ClientFinanceDate date) {
		// if (data == null) {
		// ClientFinanceDate custSinceDate = customerSinceDate
		// .getDate();
		// if (date.before(custSinceDate)) {
		// String msg = messages.msg();
		// // Accounter.showError(msg);
		// }
		// }
		//
		// }
		//
		// });
		// balanceDate.setUseTextField(true);
		// balanceDate.setTitle(messages.balanceAsOf());
		// balanceDate.setEnteredDate(new Date(company.getPreferences()
		// .getPreventPostingBeforeDate()));
		// balanceDate.addDateValueChangeHandler(new DateValueChangeHandler() {
		//
		// @Override
		// public void onDateValueChange(Date date) {
		//
		// }
		// });
		// if (fiscalYear != null) {
		// fiscalYear.setStartDate(company.getPreferences()
		// .getStartOfFiscalYear());
		// balanceDate.setEnteredDate(fiscalYear.getStartDate());
		// }

		// accInfoForm.setWidth("100%");

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
		accInfoForm.add(statusCheck, customerSinceDate);

		balanceForm = new DynamicForm("balanceForm");
		if (getPreferences().isPricingLevelsEnabled()) {
			balanceForm.add(openingBalText, balanceDate, balanceText,
					priceLevelSelect);
		} else {
			balanceForm.add(openingBalText, balanceDate, balanceText);
		}

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
				return CustomerView.this.isInViewMode();
			}
		};
		gridView.setEnabled(!isInViewMode());

		StyledPanel panel = new StyledPanel("panel");
		panel.add(l1);
		panel.add(gridView);
		StyledPanel hPanel = new StyledPanel("hPanel");
		hPanel.add(addButton);
		panel.add(hPanel);
		memoArea = new TextAreaItem(messages.notes(), "memo");
		memoArea.setToolTip(messages.writeCommentsForThis(this.getAction()
				.getViewName()));
		DynamicForm memoForm = new DynamicForm("memoForm");
		memoForm.add(memoArea);

		// For Editing customer

		addrsForm = new AddressForm(null);
		addrsForm.setEnabled(!isInViewMode());
		fonFaxForm = new PhoneFaxForm(null, null, this, this.getAction()
				.getViewName());
		fonFaxForm.setEnabled(!isInViewMode());
		emailForm = new EmailForm(null, null, this, this.getAction()
				.getViewName());
		emailForm.setEnabled(!isInViewMode());

		/* Adding Dynamic Forms in List */
		listforms.add(customerForm);
		listforms.add(accInfoForm);
		listforms.add(balanceForm);
		listforms.add(memoForm);
		StyledPanel leftVLay = new StyledPanel("leftVLay");
		leftVLay.add(customerForm);
		leftVLay.add(accInfoForm);
		if (isMultiCurrencyEnabled()) {
			leftVLay.add(currencyCombo);
		}
		leftVLay.add(balanceForm);
		// leftVLay.add(fonFaxForm);
		// leftVLay.add(emailForm);

		StyledPanel rightVLay = new StyledPanel("rightVLay");
		// rightVLay.setWidth("100%");
		rightVLay.add(addrsForm);
		rightVLay.add(fonFaxForm);
		rightVLay.add(emailForm);

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
		mainVlay.add(panel);
		mainVlay.add(memoForm);
		// mainVlay.add(memoForm);
		memoForm.setEnabled(!isInViewMode());
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

		salesPersonSelect = new SalesPersonCombo(messages.salesPerson());
		salesPersonSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientSalesPerson>() {

					@Override
					public void selectedComboBoxItem(
							ClientSalesPerson selectItem) {
						selectSalesPersonFromDetailsTab = selectItem;

					}

				});

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

		financeDitailsForm.add(salesPersonSelect, creditLimitText,
				bankNameSelect, bankAccountSelect, bankBranchSelect);

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

		shipMethSelect = new ShippingMethodsCombo(
				messages.preferredShippingMethod());
		shipMethSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {

					@Override
					public void selectedComboBoxItem(
							ClientShippingMethod selectItem) {
						selectShippingMethodFromDetailsTab = selectItem;

					}

				});

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
		payTermsSelect = new PaymentTermsCombo(messages.paymentTerms());
		payTermsSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					@Override
					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {
						selectPayTermFromDetailsTab = selectItem;

					}

				});

		custGroupSelect = new CustomerGroupCombo(messages.payeeGroup(Global
				.get().Customer()));
		custGroupSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomerGroup>() {

					@Override
					public void selectedComboBoxItem(
							ClientCustomerGroup selectItem) {
						selectCustomerGroupFromDetailsTab = selectItem;

					}

				});

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
		termsForm.add(payMethSelect, payTermsSelect, custGroupSelect);

		tdsCheckBox = new CheckboxItem(messages.willDeductTDSforUs(),
				"tdsCheckBox");
		tdsCheckBox.setEnabled(!isInViewMode());

		if (getPreferences().isTrackTax()) {
			if (getCountryPreferences().isVatAvailable()
					|| getCountryPreferences().isServiceTaxAvailable()) {
				termsForm.add(vatregno);
			}
			termsForm.add(custTaxCode);
			// custTaxCode.setRequired(true);
			if (getPreferences().isTDSEnabled()) {
				termsForm.add(tdsCheckBox);
			}
		}

		if (getPreferences().isDoProductShipMents()) {
			termsForm.add(shipMethSelect);
		}
		salesPersonSelect.setEnabled(!isInViewMode());
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
		shipMethSelect.setEnabled(!isInViewMode());
		payMethSelect.setEnabled(!isInViewMode());
		payTermsSelect.setEnabled(!isInViewMode());
		custGroupSelect.setEnabled(!isInViewMode());
		vatregno.setEnabled(!isInViewMode());
		custTaxCode.setEnabled(!isInViewMode());

		addCustomFieldButton = new Button();
		addCustomFieldButton.setText(messages.ManageCustomFields());
		addCustomFieldButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				customFieldDialog = new CustomFieldDialog(CustomerView.this,
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
			setData(new ClientCustomer(getCompany().getPrimaryCurrency()
					.getID()));
		}

		// initTaxAgenciesList();
		initMainValues();
		initSalesPersonList();
		initCustomerGroupList();
		initPaymentTermsList();
		initShippingMethodList();
		// initCreditRatingList();
		// initPriceLevelList();
		if (data != null && data.getPhoneNo() != null)
			data.setPhoneNo(data.getPhoneNo());
		if (data != null && data.getFaxNo() != null)
			data.setFaxNo(data.getFaxNo());
		if (getPreferences().isTrackTax()) {
			initVatCodeList();
		}
		if (data != null && data.getPriceLevel() != 0) {
			priceLevelSelect.setValue(company.getPriceLevel(
					data.getPriceLevel()).getDisplayName());
		}

		setPayeeFields(data.getPayeeFields());
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
				&& (data.getID() == 0 || data.getNumber() == null || data
						.getNumber().isEmpty())) {
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
			custNoText.setValue(data.getNumber());
			balanceDate.setEnabled(!isInViewMode());
		}
		// Setting File as
		fileAsText.setValue(data.getFileAs());
		fonFaxForm.businessPhoneText.setValue(data.getPhoneNo());
		fonFaxForm.businessFaxText.setValue(data.getFaxNo());
		emailForm.businesEmailText.setValue(data.getEmail());
		emailForm.webText.setValue(data.getWebPageAddress());

		// Setting Status Check
		statusCheck.setValue(data.isActive());

		// Setting Customer Since
		customerSinceDate.setEnteredDate(new ClientFinanceDate(data
				.getPayeeSince()));
		openingBalText.setAmount(data.getOpeningBalance());
		balanceText.setAmount(data.getBalance());

		// Setting Balance as of
		balanceDate
				.setEnteredDate(new ClientFinanceDate(data.getBalanceAsOf()));

		// Setting Memo
		memoArea.setValue(data.getMemo());

		bankAccountSelect.setValue(data.getBankAccountNo());
		bankNameSelect.setValue(data.getBankName());
		bankBranchSelect.setValue(data.getBankBranch());
		panNumberText.setValue(data.getPanNumber());
		cstNumberText.setValue(data.getCstNumber());
		serviceTaxRegistrationNo.setValue(data
				.getServiceTaxRegistrationNumber());
		tinNumberText.setValue(data.getTinNumber());

		if (getPreferences().isTrackTax()
				&& (getCountryPreferences().isServiceTaxAvailable() || getCountryPreferences()
						.isVatAvailable())) {
			// setting vatRegistrationNumber
			vatregno.setValue(data.getVATRegistrationNumber());
		}

		// Setting AddressForm
		addrsForm.setAddress(data.getAddress());
		// Setting Contacts
		// gridView.setAllRows(new
		// ArrayList<ClientContact>(data.getContacts()));

		int row = 0;
		for (ClientContact clientContact : data.getContacts()) {
			if (clientContact.isPrimary()) {
				gridView.add(clientContact);
				gridView.checkColumn(row, 0, true);
			} else {
				gridView.add(clientContact);
			}
			row++;
		}

		// Setting salesPerson
		selectSalesPersonFromDetailsTab = getCompany().getSalesPerson(
				data.getSalesPerson());

		// Setting Credit Limit Text
		if (!DecimalUtil.isEquals(data.getCreditLimit(), 0))
			creditLimitText.setAmount(data.getCreditLimit());

		// Setting price level
		selectPriceLevelFromDetailsTab = getCompany().getPriceLevel(
				data.getPriceLevel());
		// Setting Credit Rating
		selectCreditRatingFromDetailsTab = getCompany().getCreditRating(
				data.getCreditRating());
		// Setting Shipping Method
		selectShippingMethodFromDetailsTab = Accounter.getCompany()
				.getShippingMethod(data.getShippingMethod());
		// Setting Payment Method
		// selectPaymentMethodFromDetialsTab = takenCustomer
		// .getPaymentMethod();
		payMethSelect.setComboItem(data.getPaymentMethod());

		if (data.getCurrency() != 0) {
			selectCurrency = company.getCurrency(data.getCurrency());
			currencyCombo.setSelectedCurrency(selectCurrency);
			openingBalText.setCurrency(selectCurrency);
			balanceText.setCurrency(selectCurrency);
			creditLimitText.setCurrency(selectCurrency);
			if (!selectCurrency.equals(getCompany().getPreferences()
					.getPrimaryCurrency())) {
				currencyCombo.disabledFactorField(false);
			}
		}
		currencyCombo.setCurrencyFactor(data.getCurrencyFactor());
		// Setting payemnt term
		selectPayTermFromDetailsTab = getCompany().getPaymentTerms(
				data.getPaymentTerm());
		// Setting Customer Group
		selectCustomerGroupFromDetailsTab = getCompany().getCustomerGroup(
				data.getCustomerGroup());
		if (getPreferences().isTrackTax()) {
			selectVatCodeFromDetailsTab = getCompany().getTAXCode(
					data.getTAXCode());
		}
		tdsCheckBox.setValue(data.willDeductTDS());
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
		salesPersonSelect.setEnabled(!isInViewMode());
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
		shipMethSelect.setEnabled(!isInViewMode());
		payMethSelect.setEnabled(!isInViewMode());
		payTermsSelect.setEnabled(!isInViewMode());
		custGroupSelect.setEnabled(!isInViewMode());
		vatregno.setEnabled(!isInViewMode());
		custTaxCode.setEnabled(!isInViewMode());
		customFieldForm.setEnabled(isInViewMode());
		addCustomFieldButton.setEnabled(!isInViewMode());
		tdsCheckBox.setEnabled(!isInViewMode());
		memoArea.setDisabled(isInViewMode());
		enablePayeeFields(data.getPayeeFields());
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
