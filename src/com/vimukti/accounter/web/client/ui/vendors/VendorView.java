package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomFieldValue;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEmail;
import com.vimukti.accounter.web.client.core.ClientFax;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPhone;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressForm;
import com.vimukti.accounter.web.client.ui.CustomFieldDialog;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.GwtTabPanel;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingMethodsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.TaxItemCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorGroupCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.edittable.tables.ContactsTable;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.CustomFieldForm;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.AddButton;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyChangeListener;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyComboWidget;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;
import com.vimukti.accounter.web.client.util.Countries;

/**
 * 
 * @author venki.p modified by Rajesh.A and Ravi kiran.G, Murali
 * 
 */

public class VendorView extends BaseView<ClientVendor> {

	DynamicForm vendorForm, accInfoForm;
	Button addCustomFieldButton;
	TextItem vendorNoText;
	TextItem vendorNameText;
	TextItem fileAsText, accountText, bankNameText, bankBranchText, webText,
			linksText, expenseAccountsText, taxIDText, panNumberText,
			serviceTaxRegisterationNumber, taxID;
	TextAreaItem memoArea;
	DateField balanceDate, vendorSinceDate;
	EmailField emailText;
	AmountField creditLimitText, openingBalText, balanceText;
	TextItem vatRegistrationNumber;

	PaymentTermsCombo payTermsSelect;
	ShippingMethodsCombo preferredShippingSelect;
	OtherAccountsCombo expenseAccountsSelect;
	TAXCodeCombo vendorTaxCode;
	TaxItemCombo vendorTDSTaxCode;
	VendorGroupCombo vendorGroupSelect;
	SelectCombo preferredPaymentSelect;
	CurrencyComboWidget currencyWidget;
	CheckboxItem euVATexempVendor;
	CheckboxItem track1099MISC;
	CheckboxItem isTDS;
	// TabPanel tabSet;
	AddButton addButton;
	CustomFieldForm customFieldForm;

	LinkedHashMap<String, ClientAddress> allAddresses;
	LinkedHashMap<String, ClientPhone> allPhones;
	LinkedHashMap<String, ClientFax> allFaxes;
	LinkedHashMap<String, ClientEmail> allEmails;

	private DynamicForm balanceForm;
	CheckboxItem statusCheck;

	ContactsTable gridView;

	List<ClientFax> faxList;
	List<ClientEmail> emailList;
	List<ClientPhone> phoneList;
	List<ClientAddress> addressList;

	List<ClientShippingMethod> shipMethodList;
	List<String> payMethodList;
	List<ClientAccount> expenseAccountList;
	List<ClientPaymentTerms> payTermList;
	List<ClientVendorGroup> vendorGroupList;

	LinkedHashMap<String, String> payTermMap;
	LinkedHashMap<String, String> payMethodMap;
	LinkedHashMap<String, String> shipMethodMap;
	LinkedHashMap<String, String> vendorGroupMap;

	protected ClientCurrency selectCurrency;

	protected ClientAccount selectAccountFromDetailsTab;
	protected ClientShippingMethod selectShippingMethodFromDetailsTab;
	protected String selectPaymentMethodFromDetialsTab;
	protected ClientPaymentTerms selectPaymentTermFromDetailsTab;
	protected ClientVendorGroup selectVendorGroupFromDetailsTab;
	// protected ClientCurrency clientCurrency;

	private AddressForm addrsForm;
	private PhoneFaxForm fonFaxForm;
	private EmailForm emailForm;

	// private ClientVendor takenVendor;
	protected ClientAccount accountPayableAccount;
	private ClientCompany company;
	private boolean wait;

	private ArrayList<DynamicForm> listforms;

	private StyledPanel hPanel;
	protected ClientTAXCode selectTaxCodeFromDetailsTab;
	protected ClientTAXItem selectTaxItemFromDetailsTab;
	public CustomFieldDialog customFieldDialog;
	private GwtTabPanel tabSet;

	public VendorView() {
		super();
		this.getElement().setId("newVendorView");
	}

	// private void getFiscalYear() {
	// List<ClientFiscalYear> result = getCompany().getFiscalYears();
	// if (result != null && !isInViewMode()) {
	// for (ClientFiscalYear fiscalYear : result) {
	// if (fiscalYear != null && fiscalYear.getIsCurrentFiscalYear()) {
	// if (fiscalYear != null
	// && fiscalYear.getIsCurrentFiscalYear()) {
	// VendorView.this.fiscalYear = fiscalYear;
	// // balanceDate.setEnteredDate(VendorView.this.fiscalYear
	// // .getStartDate());
	// break;
	// }
	// }
	// }
	// }
	// }

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		tabSet = (GwtTabPanel) GWT.create(GwtTabPanel.class);

		Label title = new Label(Global.get().Vendor());
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
	public ClientVendor saveView() {
		ClientVendor saveView = super.saveView();
		if (saveView != null) {
			updateVendorObject();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		if (!wait) {
			updateVendorObject();
			saveOrUpdate(getData());
		}
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		result.add(vendorForm.validate());

		String name = vendorNameText.getValue();

		String taxNumber = taxID.getValue();

		ClientVendor vendorByName = company.getVendorByName(name);

		ClientCustomer customerByName = company.getCustomerByName(name);

		ClientTAXAgency taxAgencyByName = company.getTaxAgenciesByName(name);

		if (customerByName != null) {
			result.addError(vendorNameText, messages.alreadyExist());
			return result;
		}
		if (taxAgencyByName != null) {
			result.addError(vendorNameText, messages.alreadyExist());
			return result;
		}
		if (vendorByName != null
				&& !(this.getData().getID() == vendorByName.getID())) {
			result.addError(vendorNameText, messages.alreadyExist());
			return result;
		}

		if (currencyWidget != null && !currencyWidget.isShowFactorField()) {
			if (currencyWidget.getCurrencyFactor() <= 0) {
				result.addError(currencyWidget,
						messages.pleaseEntervalidCurrencyFactor());
			}
		}
		data.setName(name);
		if (getPreferences().getUseVendorId()) {
			data.setVendorNumber(vendorNoText.getValue().toString());
			String error = objectExist(data);
			if (error != null && !error.isEmpty()) {
				result.addError(vendorNoText, error);
				return result;
			}
		}

		ClientFinanceDate asOfDate = balanceDate.getEnteredDate();
		if (AccounterValidator.isPriorToCompanyPreventPostingDate(asOfDate)) {
			result.addError(balanceDate, messages.priorasOfDate());
		}

		if (getPreferences().isTDSEnabled()) {
			if (isTDS.getValue()) {
				ClientTAXItem selectedValue = vendorTDSTaxCode
						.getSelectedValue();
				if (selectedValue == null) {
					result.addError(vendorTDSTaxCode,
							messages.pleaseSelectTDS());
				}
			}
		}

		return result;
	}

	public String objectExist(ClientVendor vendor) {

		String error = null;

		List<ClientVendor> list = Accounter.getCompany().getVendors();
		if (list == null || list.isEmpty())
			return "";
		for (ClientVendor old : list) {
			if (old.getID() == vendor.getID()) {
				continue;
			}
			if (vendor.getName().equalsIgnoreCase(old.getName())) {
				if (getCompany().getPreferences().getUseVendorId()) {
					for (ClientVendor client : list) {
						if (vendor.getVendorNumber().equals(
								client.getVendorNumber())) {
							error = messages
									.objAlreadyExistsWithNameAndNo(Global.get()
											.Vendor());
							break;
						}
					}
				}
				error = messages
						.objAlreadyExistsWithName(Global.get().vendor());
				break;
			} else if (getCompany().getPreferences().getUseVendorId()) {
				if (vendor.getVendorNumber().isEmpty()) {
					error = messages
							.pleaseEnterVendorNumberItShouldNotBeEmpty(Global
									.get().vendor());
					break;
				} else if (vendor.getVendorNumber().equals(
						old.getVendorNumber())) {
					error = messages.objAlreadyExistsWithNumber(Global.get()
							.Vendor());
					break;
				} else if (checkIfNotNumber(vendor.getVendorNumber())) {
					error = messages.payeeNumberShouldBeNumber(Global.get()
							.Vendor());
					break;
				} else if (Integer
						.parseInt(vendor.getVendorNumber().toString()) < 1) {
					error = messages.payeeNumberShouldBePos(Global.get()
							.Vendor());
					break;
				}
			}

		}
		return error;
	}

	private boolean isObjectExist(long id, String name) {
		List<ClientVendor> vendors = getCompany().getVendors();
		for (ClientVendor old : vendors) {
			if (old.getID() == id) {
				continue;
			}
			if (old.getName().toLowerCase().equals(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private StyledPanel getGeneralTab() {
		vendorNameText = new TextItem(
				messages.payeeName(Global.get().Vendor()), "vendorNameText");
		if (quickAddText != null) {
			vendorNameText.setValue(quickAddText);
		}
		// vendorNameText.setHelpInformation(true);
		vendorNameText.setRequired(true);
		// vendorNameText.setWidth(100);
		vendorNameText.setEnabled(!isInViewMode());

		vendorNoText = new TextItem(
				messages.payeeNumber(Global.get().Vendor()), "vendorNoText");
		// vendorNoText.setHelpInformation(true);
		vendorNoText.setRequired(getPreferences().getUseVendorId());
		// vendorNoText.setWidth(100);

		fileAsText = new TextItem(messages.fileAs(), "fileAsText");
		// fileAsText.setHelpInformation(true);
		// fileAsText.setWidth(100);

		taxID = new TextItem("Tax ID", "taxID");
		// taxID.setHelpInformation(true);
		// taxID.setWidth(100);
		taxID.setEnabled(!isInViewMode());
		vendorNameText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event.getSource() != null)
					fileAsText.setValue(vendorNameText.getValue());
			}

		});

		vendorForm = UIUtils.form(Global.get().Vendor());
		if (getCompany().getPreferences().getUseVendorId()) {
			vendorForm.add(vendorNameText, vendorNoText);
			vendorNoText.setEnabled(!isInViewMode());
		} else {
			vendorForm.add(vendorNameText);

		}
		vendorForm.setStyleName("vender-form");

		accInfoForm = new DynamicForm("accInfoForm");
		// accInfoForm.setIsGroup(true);
		// accInfoForm
		// .setGroupTitle(messages.payeeInformation(messages.Account()));

		statusCheck = new CheckboxItem(messages.active(), "statusCheck");
		statusCheck.setValue(true);
		statusCheck.setEnabled(!isInViewMode());

		vendorSinceDate = new DateField(messages.payeeSince(Global.get()
				.Vendor()), "vendorSinceDate");
		vendorSinceDate.setEnabled(!isInViewMode());
		track1099MISC = new CheckboxItem(messages.track1099Form(),
				"track1099MISC");
		track1099MISC.setValue(false);

		// vendorSinceDate.setHelpInformation(true);
		vendorSinceDate.setEnteredDate(new ClientFinanceDate());

		openingBalText = new AmountField(messages.openingBalance(), this,
				getBaseCurrency(), true);
		openingBalText.getElement().addClassName("openingBalText");
		// openingBalText.setHelpInformation(true);
		openingBalText.setEnabled(!isInViewMode());

		balanceText = new AmountField(messages.balance(), this,
				getBaseCurrency(), "balanceText");
		// balanceText.setHelpInformation(true);
		balanceText.setEnabled(false);

		balanceDate = new DateField(messages.balanceAsOf(), "balanceDate");
		// balanceDate.setHelpInformation(true);
		ClientFinanceDate todaydate = new ClientFinanceDate();
		todaydate.setDay(todaydate.getDay());
		balanceDate.setDatethanFireEvent(todaydate);
		balanceDate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (!isInViewMode()) {
					ClientFinanceDate vendSinceDate = vendorSinceDate.getDate();
					if (date.before(vendSinceDate)) {
						String msg = messages.msg(Global.get().Vendor());
					}
				}
			}
		});

		balanceForm = new DynamicForm("balanceForm");

		currencyWidget = createCurrencyComboWidget();
		currencyWidget.setEnabled(!isInViewMode());

		accInfoForm.add(statusCheck, vendorSinceDate);
		balanceForm.add(openingBalText, balanceDate, balanceText);

		if (getPreferences().isTrackTax()) {
			if (getCountryPreferences().isSalesTaxAvailable()) {
				accInfoForm.add(taxID);
			}
			if (getCompany().getCountry().equals(Countries.UNITED_STATES)) {
				accInfoForm.add(track1099MISC);
			}
		}
		Map<String, String> fields = new HashMap<String, String>();
		for (String fieldName : getCompany().getCountryPreferences()
				.getVendorFields()) {
			fields.put(fieldName, "");
		}
		addFields(fields);

		Label l1 = new Label(messages.contacts());

		gridView = new ContactsTable() {

			@Override
			protected boolean isInViewMode() {
				return VendorView.this.isInViewMode();
			}
		};
		gridView.setEnabled(!isInViewMode());
		// gridView.setCanEdit(!isInViewMode());
		// gridView.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		// gridView.isEnable = false;
		// gridView.init();

		StyledPanel panel = new StyledPanel("") {
			@Override
			protected void onAttach() {

				// gridView.setHeight("88px");

				super.onAttach();
			}
		};
		panel.add(l1);
		hPanel = new StyledPanel("addnew_edit_panel");
		Label contactTableTitle = new Label(messages.contacts());
		contactTableTitle.addStyleName("editTableTitle");
		hPanel.add(contactTableTitle);
		hPanel.add(gridView);
		panel.add(hPanel);

		memoArea = new TextAreaItem(messages.memo(), "memoArea");
		memoArea.setToolTip(messages.writeCommentsForThis(this.getAction()
				.getViewName()));
		// memoArea.setHelpInformation(true);
		memoArea.setTitle(messages.notes());

		addrsForm = new AddressForm(null);
		addrsForm.setEnabled(!isInViewMode());
		fonFaxForm = new PhoneFaxForm(null, null, this, this.getAction()
				.getViewName());
		fonFaxForm.setEnabled(!isInViewMode());
		emailForm = new EmailForm(null, null, this, this.getAction()
				.getViewName());
		emailForm.setEnabled(!isInViewMode());
		DynamicForm memoForm = new DynamicForm("align-form");
		memoForm.add(memoArea);
		// memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		memoForm.setEnabled(!isInViewMode());

		StyledPanel bottomPanel = new StyledPanel("bottomPanel");
		bottomPanel.add(memoForm);

		// StyledPanel vendorHPanel = new StyledPanel();
		// vendorHPanel.setWidth("100%");
		// vendorHPanel.setCellHorizontalAlignment(vendorForm, ALIGN_RIGHT);
		// vendorHPanel.add(vendorForm);

		// StyledPanel addressHPanel = new StyledPanel();
		// addressHPanel.setWidth("100%");
		// addressHPanel.add(addrsForm);

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		// leftVLay.setHorizontalAlignment(ALIGN_RIGHT);
		// leftVLay.setWidth("100%");
		// leftVLay.setCellHorizontalAlignment(vendorHPanel, ALIGN_RIGHT);
		leftVLay.add(vendorForm);
		leftVLay.add(accInfoForm);
		if (isMultiCurrencyEnabled()) {
			leftVLay.add(currencyWidget);
		}
		leftVLay.add(balanceForm);

		StyledPanel rightVLay = new StyledPanel("rightVLay");
		rightVLay.add(addrsForm);
		rightVLay.add(fonFaxForm);
		rightVLay.add(emailForm);
		// addrsForm.getCellFormatter().addStyleName(0, 0,
		// "addrsFormCellAlign");
		// addrsForm.getCellFormatter().addStyleName(0, 1,
		// "addrsFormCellAlign");

		// StyledPanel emailHPanel = new StyledPanel();
		// emailHPanel.setCellHorizontalAlignment(emailForm, ALIGN_LEFT);
		// emailHPanel.setWidth("90%");
		// emailHPanel.add(emailForm);

		StyledPanel accInfoHPanel = new StyledPanel("accInfoHPanel");

		StyledPanel mainVlay = new StyledPanel("generalTab");
		StyledPanel topHLay = getTopLayout();
		if (topHLay != null) {
			topHLay.add(leftVLay);
			topHLay.add(rightVLay);
			mainVlay.add(topHLay);
		} else {
			mainVlay.add(leftVLay);
			mainVlay.add(rightVLay);
		}
		mainVlay.add(panel);
		mainVlay.add(bottomPanel);

		/* Adding dynamic forms in list */
		listforms.add(vendorForm);
		listforms.add(accInfoForm);
		listforms.add(balanceForm);
		listforms.add(memoForm);

		return mainVlay;
	}

	protected StyledPanel getTopLayout() {
		return new StyledPanel("fields-panel");
	}

	private void resetFromView() {
		// addrsForm.getCellFormatter().setWidth(0, 0, "75");
		// addrsForm.getCellFormatter().setWidth(0, 1, "125");
		//
		// fonFaxForm.getCellFormatter().setWidth(0, 0, "75");
		// fonFaxForm.getCellFormatter().setWidth(0, 1, "125");
		//
		// emailForm.getCellFormatter().setWidth(0, 0, "150");
		// emailForm.getCellFormatter().setWidth(0, 1, "150");

	}

	private StyledPanel getDetailsTab() {

		Label lab = new Label(Global.get().Vendor());

		expenseAccountsSelect = new OtherAccountsCombo(messages.Account()) {

		};
		expenseAccountsSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectAccountFromDetailsTab = selectItem;
					}
				});
		expenseAccountsSelect.setEnabled(!isInViewMode());

		List<ClientAccount> list = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getAccounts()) {
			if (account.getType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD
					|| account.getType() == ClientAccount.TYPE_EXPENSE) {
				list.add(account);
			}
		}
		expenseAccountsSelect.initCombo(list);
		creditLimitText = new AmountField(messages.creditLimit(), this,
				getBaseCurrency(), "creditLimitText");
		// creditLimitText.setHelpInformation(true);
		// creditLimitText.setWidth(100);
		creditLimitText.setEnabled(!isInViewMode());

		preferredShippingSelect = new ShippingMethodsCombo(
				messages.preferredShippingMethod());
		// preferredShippingSelect.setHelpInformation(true);
		preferredShippingSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {
					@Override
					public void selectedComboBoxItem(
							ClientShippingMethod selectItem) {
						selectShippingMethodFromDetailsTab = selectItem;
					}

				});
		preferredShippingSelect.setEnabled(!isInViewMode());

		preferredPaymentSelect = UIUtils.getPaymentMethodCombo();
		preferredPaymentSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						selectPaymentMethodFromDetialsTab = preferredPaymentSelect
								.getSelectedValue();
					}
				});
		preferredPaymentSelect.setEnabled(!isInViewMode());

		payTermsSelect = new PaymentTermsCombo(messages.paymentTerms());
		// payTermsSelect.setHelpInformation(true);
		payTermsSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {
					@Override
					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {
						selectPaymentTermFromDetailsTab = selectItem;
					}
				});
		payTermsSelect.setEnabled(!isInViewMode());
		accountText = new TextItem(messages.accountNumber(), "accountText");
		accountText.setEnabled(!isInViewMode());
		bankNameText = new TextItem(messages.bankName(), "bankNameText");
		bankNameText.setEnabled(!isInViewMode());
		bankBranchText = new TextItem(messages.bankBranch(), "bankBranchText");
		bankBranchText.setEnabled(!isInViewMode());

		customFieldForm = new CustomFieldForm();
		DynamicForm financeDetailsForm = new DynamicForm("financeDetailsForm");

		financeDetailsForm.add(expenseAccountsSelect, creditLimitText,
				preferredShippingSelect, preferredPaymentSelect,
				payTermsSelect, accountText, bankNameText, bankBranchText);

		vendorGroupSelect = new VendorGroupCombo(messages.payeeGroup(Global
				.get().Vendor()));
		// vendorGroupSelect.setHelpInformation(true);
		vendorGroupSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendorGroup>() {
					@Override
					public void selectedComboBoxItem(
							ClientVendorGroup selectItem) {
						selectVendorGroupFromDetailsTab = selectItem;
					}

				});
		vendorGroupSelect.setEnabled(!isInViewMode());
		NewVendorAction newVendorAction = (NewVendorAction) this.getAction();
		if (newVendorAction.getOpenedFrom() == NewVendorAction.FROM_CREDIT_CARD_EXPENSE) {
			vendorGroupList = getCompany().getVendorGroups();

			for (ClientVendorGroup vendorGroup : vendorGroupList) {
				// id 1 for creditCardCompanies
				if (vendorGroup.getID() == 1) {
					// vendorGroupSelect.setComboItem(vendorGroup);
					vendorGroupSelect.addItemThenfireEvent(vendorGroup);
					selectVendorGroupFromDetailsTab = vendorGroup;
					break;
				}
			}
		}
		taxIDText = new TextItem(messages.taxId(), "taxIDText");
		// taxIDText.setHelpInformation(true);
		taxIDText.setEnabled(!isInViewMode());
		vendorTDSTaxCode = new TaxItemCombo(messages.vendorTDSCode(Global.get()
				.Vendor()), ClientTAXAgency.TAX_TYPE_TDS);
		// vendorTDSTaxCode.setHelpInformation(true);
		vendorTDSTaxCode.setEnabled(false);
		// panNumber=new TextItem(messages.panNumber());
		// panNumber.setHelpInformation(true);
		// panNumber.setWidth("100%");

		vendorTDSTaxCode
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						selectTaxItemFromDetailsTab = selectItem;
					}

				});
		isTDS = new CheckboxItem(messages.tdsApplicable(), "isTDS");
		isTDS.setValue(true);
		isTDS.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if (isTDS.isChecked()) {
					vendorTDSTaxCode.setEnabled(true);
				} else {
					vendorTDSTaxCode.setEnabled(false);
				}
			}
		});
		isTDS.setEnabled(!isInViewMode());
		panNumberText = new TextItem(messages.panNumber(), "panNumberText");
		// panNumberText.setHelpInformation(true);
		panNumberText.setEnabled(!isInViewMode());
		serviceTaxRegisterationNumber = new TextItem(
				messages.serviceTaxRegistrationNumber(),
				"serviceTaxRegisterationNumber");
		// serviceTaxRegisterationNumber.setHelpInformation(true);
		serviceTaxRegisterationNumber.setEnabled(!isInViewMode());
		DynamicForm vendorGrpForm = new DynamicForm("vendorGrpForm");
		// vendorGrpForm.setIsGroup(false);

		/*
		 * In UK n US versions we need different widths as for the view
		 * requirement
		 */
		vendorGrpForm.add(vendorGroupSelect);

		// vendorGrpForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "44%");

		vatRegistrationNumber = new TextItem(messages.taxRegNo(),
				"vatRegistrationNumber");
		if (!getCountryPreferences().isServiceTaxAvailable()) {
			vatRegistrationNumber.setTitle(messages.vatRegistrationNumber());
		}
		// vatRegistrationNumber.setHelpInformation(true);
		vatRegistrationNumber.setEnabled(!isInViewMode());
		vendorTaxCode = new TAXCodeCombo(messages.taxCode(), false);
		// vendorTaxCode.setHelpInformation(true);
		vendorTaxCode
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					@Override
					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						selectTaxCodeFromDetailsTab = selectItem;
					}

				});
		vendorTaxCode.setEnabled(!isInViewMode());
		DynamicForm vatform = new DynamicForm("vatform");
		// vatform.setIsGroup(true);
		// vatform.setGroupTitle(messages.vatDetails());
		if (getPreferences().isTrackTax()) {
			if (getCountryPreferences().isVatAvailable()) {
				vatform.add(vatRegistrationNumber);
			}
			vatform.add(vendorTaxCode);
			if (getCountryPreferences().isServiceTaxAvailable()) {
				vatform.add(serviceTaxRegisterationNumber);
			}
		}

		addCustomFieldButton = new Button();
		addCustomFieldButton.setText(messages.ManageCustomFields());
		addCustomFieldButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				customFieldDialog = new CustomFieldDialog(VendorView.this,
						messages.CustomField(), messages.ManageCustomFields());
				ViewManager.getInstance().showDialog(customFieldDialog);
			}
		});
		addCustomFieldButton.setEnabled(!isInViewMode());
		DynamicForm tdsFrom = new DynamicForm("tdsFrom");
		tdsFrom.add(isTDS, vendorTDSTaxCode);

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		// leftVLay.getElement().getStyle()
		// .setBorderColor("none repeat scroll 0 0 #eee !important");
		leftVLay.add(financeDetailsForm);

		StyledPanel rVLayout = new StyledPanel("rVLayout");

		// rVLayout.setSize("100%", "100%");
		rVLayout.add(vendorGrpForm);
		if (getPreferences().isTrackTax()) {
			rVLayout.add(vatform);
		}
		if (getPreferences().isTDSEnabled()) {
			rVLayout.add(tdsFrom);
		}
		StyledPanel customField = new StyledPanel("customField");
		Label customLable = new Label(messages.CustomFieldstext());
		customField.add(customLable);
		customField.add(addCustomFieldButton);
		rVLayout.add(customField);
		rVLayout.add(customFieldForm);
		StyledPanel mainHLay = new StyledPanel("mainHLay");
		mainHLay.addStyleName("fields-panel");
		mainHLay.add(leftVLay);
		mainHLay.add(rVLayout);

		StyledPanel mainVLayout = new StyledPanel("detailsTab");
		mainVLayout.add(lab);
		mainVLayout.add(mainHLay);

		/* Adding dynamic forms in list */
		listforms.add(financeDetailsForm);
		listforms.add(vendorGrpForm);
		listforms.add(vatform);

		// if (UIUtils.isMSIEBrowser()) {
		// financeDetailsForm.getCellFormatter().setWidth(0, 1, "200px");
		// vendorGrpForm.getCellFormatter().setWidth(0, 1, "200px");
		// vatform.getCellFormatter().setWidth(0, 1, "200px");
		// financeDetailsForm.setWidth("75%");
		// vendorGrpForm.setWidth("75%");
		// vatform.setWidth("75%");
		// }

		return mainVLayout;
	}

	public void createCustomFieldControls() {
		if (data != null && data.getCustomFieldValues() != null) {
			customFieldForm.updateValues(data.getCustomFieldValues(),
					getCompany(), ClientPayee.TYPE_VENDOR);
		}
		customFieldForm.createControls(getCompany(),
				data == null ? null : data.getCustomFieldValues(),
				ClientPayee.TYPE_VENDOR);
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

	protected void adjustFormWidths(int titlewidth, int listBoxWidth) {
		//
		// addrsForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "25px");
		// addrsForm.getCellFormatter().getElement(0, 1).setAttribute(
		// messages.width(), "186px");
		//
		// fonFaxForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "240px");
		// fonFaxForm.getCellFormatter().getElement(0, 1).setAttribute(
		// FinanceApplication.constants().width(), "185px");

		// vendorForm.getCellFormatter().getElement(0,
		// 0).getStyle().setWidth(150,
		// Unit.PX);
		// emailForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "240px");
		// emailForm.getCellFormatter().getElement(0, 1).setAttribute(
		// FinanceApplication.constants().width(), "");
		// accInfoForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "150px");

	}

	@Override
	protected void onLoad() {
		// int titlewidth = fonFaxForm.getCellFormatter().getElement(0, 0)
		// .getOffsetWidth();
		// int listBoxWidth = fonFaxForm.getCellFormatter().getElement(0, 1)
		// .getOffsetWidth();

		// adjustFormWidths(titlewidth, listBoxWidth);
		super.onLoad();
	}

	@Override
	protected void onAttach() {

		// int titlewidth = fonFaxForm.getCellFormatter().getElement(0, 0)
		// .getOffsetWidth();
		// int listBoxWidth = fonFaxForm.getCellFormatter().getElement(0, 1)
		// .getOffsetWidth();
		//
		// adjustFormWidths(titlewidth, listBoxWidth);

		super.onAttach();
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = (AccounterException) exception;
		String errorString = AccounterExceptions
				.getErrorString(accounterException);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			ClientVendor vendor = (ClientVendor) result;
			if (getMode() == EditMode.CREATE) {
				vendor.setBalance(vendor.getOpeningBalance());
			}
			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}

	}

	protected void clearFields() {
		new NewVendorAction().run(null, true);
	}

	private void updateVendorObject() {

		customFieldForm.updateValues(data.getCustomFieldValues(), getCompany(),
				ClientPayee.TYPE_CUSTOMER);
		// Setting data from General Tab

		// Setting Vendor Name
		data.setName(vendorNameText.getValue().toString() != null ? vendorNameText
				.getValue().toString() : "");
		if (vendorNoText.getValue() != null) {
			data.setVendorNumber(vendorNoText.getValue().toString());
		}

		// Setting File As
		data.setFileAs(fileAsText.getValue().toString());

		data.setType(ClientPayee.TYPE_VENDOR);

		// Setting Addresses
		data.setAddress(addrsForm.getAddresss());

		data.setTdsApplicable(isTDS.getValue());

		if (selectTaxItemFromDetailsTab != null) {
			data.setTaxItemCode(Utility.getID(selectTaxItemFromDetailsTab));
		}

		// Setting Phone
		// data.setPhoneNumbers(fonFaxForm.getAllPhones());
		data.setPhoneNo(fonFaxForm.businessPhoneText.getValue().toString());

		// Setting Fax
		// data.setFaxNumbers(fonFaxForm.getAllFaxes());
		data.setFaxNo(fonFaxForm.businessFaxText.getValue().toString());

		// Setting Email and Internet
		data.setEmail(emailForm.businesEmailText.getValue().toString());

		// Setting web page Address
		data.setWebPageAddress(emailForm.getWebTextValue());

		// Setting MISC 1099 form value
		// data.setActive((Boolean) track1099MISC.getValue());

		// Setting Active
		data.setActive((Boolean) statusCheck.getValue());

		// Setting Vendor Since
		data.setPayeeSince(vendorSinceDate.getEnteredDate().getDate());

		// Setting Currency
		if (isMultiCurrencyEnabled()) {
			data.setCurrency(currencyWidget.getSelectedCurrency().getID());
		}
		data.setCurrencyFactor(currencyWidget.getCurrencyFactor());

		// Setting Balance
		data.setOpeningBalance(openingBalText.getAmount());
		data.setBalance(balanceText.getAmount());

		// Setting Balance As of
		if (balanceDate.getEnteredDate() != null)
			data.setBalanceAsOf(balanceDate.getEnteredDate().getDate());
		// Setting Contacts
		List<ClientContact> allGivenRecords = gridView.getRecords();

		Set<ClientContact> allContacts = new HashSet<ClientContact>();

		for (Object tmpRec : allGivenRecords) {
			ClientContact tempRecord = (ClientContact) tmpRec;
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
		}
		data.setContacts(allContacts);

		// Setting Memo
		if (memoArea.getValue() != null)
			data.setMemo((String) memoArea.getValue());

		// Setting Data from Details Tab

		// Setting Expense Account
		data.setExpenseAccount(Utility.getID(selectAccountFromDetailsTab));

		// Setting Credit Limit
		data.setCreditLimit(creditLimitText.getAmount());

		// Setting Preferred Shipping Method
		data.setShippingMethod(Utility
				.getID(selectShippingMethodFromDetailsTab));

		// Setting Preferred Payment Method
		data.setPaymentMethod(selectPaymentMethodFromDetialsTab != null ? selectPaymentMethodFromDetialsTab
				: preferredPaymentSelect.getSelectedValue());
		// Setting Preferred Payment Terms
		data.setPaymentTerms(Utility.getID(selectPaymentTermFromDetailsTab));

		// Setting Vendor Group
		data.setVendorGroup(Utility.getID(selectVendorGroupFromDetailsTab));
		if (getPreferences().isTrackTax()) {
			if (taxIDText.getValue() != null) {
				data.setFederalTaxId(taxIDText.getValue().toString());
			}
			if (vendorTaxCode != null)
				data.setTAXCode(Utility.getID(selectTaxCodeFromDetailsTab));
			// if (panNumberText.getValue() != null) {
			// data.setPanNumber(panNumberText.getValue().toString());
			// }
			if (getCountryPreferences().isServiceTaxAvailable()) {
				if (serviceTaxRegisterationNumber.getValue() != null) {
					data.setServiceTaxRegistrationNumber(serviceTaxRegisterationNumber
							.getValue().toString());
				}
			}
			if (getCountryPreferences().isVatAvailable()) {
				if (vatRegistrationNumber != null) {
					String vatReg = vatRegistrationNumber.getValue() != null ? vatRegistrationNumber
							.getValue().toString() : "";
					data.setVATRegistrationNumber(vatReg.length() != 0 ? vatReg
							: null);
				}
			}
		}
		// Setting Account Number
		data.setBankAccountNo(accountText.getValue().toString());
		data.setBankName(bankNameText.getValue().toString());
		data.setBankBranch(bankBranchText.getValue().toString());

		// Here No Need to Set AccountPayableAccount. All Vendors will have the
		// Only one Payable Account

		// data.setAccountsPayable(getCompany().getAccountsPayableAccount());
		// Seting opening balance accounts
		data.setOpeningBalanceAccount(getCompany().getOpeningBalancesAccount());
		// data.setAccountsPayable(getCompany().getAccountsPayableAccount());

		// Setting opening balance accounts

		data.setTaxId(taxID.getValue());
		data.setTrackPaymentsFor1099(track1099MISC.isChecked());

		HashMap<String, String> payeeFields = new HashMap<String, String>();
		for (String fieldName : itemsField.keySet()) {
			payeeFields.put(fieldName, itemsField.get(fieldName).getValue());
		}
		data.setPayeeFields(payeeFields);

	}

	public void addAccountsToList() {
		List<ClientAccount> allAccounts = expenseAccountsSelect.getAccounts();
		expenseAccountsSelect.initCombo(allAccounts);

		if (isInViewMode()) {
			ClientAccount temp = getCompany().getAccount(
					data.getExpenseAccount());
			// Setting Expense Account
			if (temp != null)
				expenseAccountsSelect.setComboItem(temp);
		}

	}

	public void addVendorGroupList() {

		vendorGroupSelect.initCombo(getCompany().getVendorGroups());
		// Setting Vendor Group
		if (isInViewMode()) {
			if (data.getVendorGroup() != 0)
				vendorGroupSelect.setComboItem(company.getVendorGroup(data
						.getVendorGroup()));
		}

	}

	public void addShippingMethodList() {
		preferredShippingSelect.initCombo(getCompany().getShippingMethods());
		// Setting Preferred Shipping Method
		if (isInViewMode()) {
			if (data.getShippingMethod() != 0)
				preferredShippingSelect.setComboItem(company
						.getShippingMethod(data.getShippingMethod()));

		}
	}

	public void addPaymentMethodList() {
		// Setting Preferred Payment Method
		if (isInViewMode()) {
			if (getData().getPaymentMethod() != null)
				preferredPaymentSelect
						.setComboItem(selectPaymentMethodFromDetialsTab);
		}

	}

	public void addPaymentTermsList() {

		payTermsSelect.initCombo(getCompany().getPaymentsTerms());
		// Setting Payment Term
		if (isInViewMode()) {
			if (getData().getPaymentTermsId() != 0)
				payTermsSelect.setComboItem(company.getPaymentTerms(getData()
						.getPaymentTermsId()));
		}

	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("VendorView");
		createControls();

	}

	@Override
	public void initData() {
		super.initData();
		if (data == null) {
			setData(new ClientVendor(getCompany().getPrimaryCurrency().getID()));

		}
		company = getCompany();
		// getFiscalYear();
		addAccountsToList();
		addVendorGroupList();
		if (getPreferences().isTrackTax()) {
			addSuplierTaxCode();
		}
		addShippingMethodList();
		addPaymentTermsList();
		if (data != null && data.getPhoneNo() != null)
			data.setPhoneNo(data.getPhoneNo());
		if (data != null && data.getFaxNo() != null)
			data.setFaxNo(data.getFaxNo());
		initMainValues();
		addPaymentMethodList();
		setPayeeFields(data.getPayeeFields());
	}

	private void setPayeeFields(HashMap<String, String> payeeFields) {
		for (String key : payeeFields.keySet()) {
			itemsField.get(key).setValue(payeeFields.get(key));
		}
	}

	Map<String, TextItem> itemsField = new HashMap<String, TextItem>();

	private void addFields(Map<String, String> payeeFields) {
		itemsField.clear();
		for (String key : payeeFields.keySet()) {
			String value = payeeFields.get(key);
			TextItem item = new TextItem(key, "key");
			item.setValue(value);
			balanceForm.add(item);
			itemsField.put(key, item);
			item.setEnabled(!isInViewMode());
		}
	}

	private void initMainValues() {
		// Setting Vendor Name
		vendorNameText.setValue(data.getName());

		if (data.getID() == 0) {
			if (getPreferences().getUseVendorId()) {
				Accounter.createHomeService().getVendorNumber(
						new AccounterAsyncCallback<String>() {

							@Override
							public void onResultSuccess(String result) {
								vendorNoText.setValue(result);
							}

							@Override
							public void onException(AccounterException caught) {
							}
						});
			}
		} else {
			vendorNoText.setValue(data.getVendorNumber());
			balanceDate.setEnabled(false);
		}

		// Setting File as
		fileAsText.setValue(data.getFileAs());
		data.getPrimaryContact();

		// Setting AddressForm
		addrsForm.setAddress(data.getAddress());

		// Setting Phone Fax Form
		fonFaxForm.businessPhoneText.setValue(data.getPhoneNo());
		fonFaxForm.businessFaxText.setValue(data.getFaxNo());

		// Setting Email Form
		emailForm.businesEmailText.setValue(data.getEmail());
		emailForm.webText.setValue(data.getWebPageAddress());

		// Setting Status Check
		statusCheck.setValue(data.isActive());

		if (data.getCurrency() != 0) {
			selectCurrency = company.getCurrency(data.getCurrency());
			currencyWidget.setSelectedCurrency(selectCurrency);
			openingBalText.setCurrency(selectCurrency);
			balanceText.setCurrency(selectCurrency);
			creditLimitText.setCurrency(selectCurrency);
			if (!selectCurrency.equals(getCompany().getPreferences()
					.getPrimaryCurrency())) {
				currencyWidget.disabledFactorField(false);
			}
		}
		currencyWidget.setCurrencyFactor(data.getCurrencyFactor());
		track1099MISC.setValue(data.isActive());

		vendorSinceDate.setEnteredDate(new ClientFinanceDate(data
				.getPayeeSince()));

		// Setting Account No
		// accountText.setValue(takenVendor.getBankAccountNo());

		// Setting Balance

		// openingBalText.setAmount(getAmountInPayeeCurrency(
		// data.getOpeningBalance(), data.getCurrencyFactor()));

		openingBalText.setAmount(data.getOpeningBalance());

		balanceText.setAmount(data.getBalance());

		// Setting Balance as of
		balanceDate
				.setEnteredDate(new ClientFinanceDate(data.getBalanceAsOf()));

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

		// Setting Memo
		memoArea.setValue(data.getMemo());

		// Setting Account
		selectAccountFromDetailsTab = getCompany().getAccount(
				data.getExpenseAccount());

		accountText.setValue(data.getBankAccountNo() != null ? data
				.getBankAccountNo().toString() : "");
		bankNameText.setValue(data.getBankName() != null ? data.getBankName()
				.toString() : "");
		bankBranchText.setValue(data.getBankBranch() != null ? data
				.getBankBranch().toString() : "");

		// Setting Credit Limit Text
		if (!DecimalUtil.isEquals(data.getCreditLimit(), 0))
			creditLimitText.setAmount(data.getCreditLimit());

		// Setting Preferred shipping method
		selectShippingMethodFromDetailsTab = Accounter.getCompany()
				.getShippingMethod(data.getShippingMethod());

		// Setting Payment Method
		selectPaymentMethodFromDetialsTab = data.getPaymentMethod();

		// Setting payment Terms
		selectPaymentTermFromDetailsTab = getCompany().getPaymentTerms(
				(data.getPaymentTermsId()));

		isTDS.setValue(data.isTdsApplicable());
		vendorTDSTaxCode
				.setSelected(vendorTDSTaxCode.getDisplayName(getCompany()
						.getTAXItem(data.getTaxItemCode())));

		taxID.setValue(data.getTaxId());

		track1099MISC.setValue(data.isTrackPaymentsFor1099());

		// Setting Vendor Group
		ClientVendorGroup vendorGroup = getCompany().getVendorGroup(
				data.getVendorGroup());
		if (vendorGroup != null) {
			selectVendorGroupFromDetailsTab = vendorGroup;
		}
		if (getPreferences().isTrackTax()) {
			if (getCountryPreferences().isVatAvailable()) {
				vatRegistrationNumber.setValue(data.getVATRegistrationNumber());
				vendorTaxCode.setSelected(vendorTaxCode.getDisplayName(data
						.getTAXCode() != 0 ? Accounter.getCompany().getTAXCode(
						data.getTAXCode()) : null));
			} else {
				// Setting Federal Id
				taxIDText.setValue(data.getFederalTaxId());
			}
			if (getCountryPreferences().isServiceTaxAvailable()) {
				serviceTaxRegisterationNumber.setValue(data
						.getServiceTaxRegistrationNumber());
			}
		}
	}

	private void addSuplierTaxCode() {
		vendorTaxCode.initCombo(getCompany().getActiveTaxCodes());
		if (isInViewMode()) {
			vendorTaxCode.setSelected(vendorTaxCode.getDisplayName(data
					.getTAXCode() != 0 ? getCompany().getTAXCode(
					data.getTAXCode()) : null));
			vendorTaxCode.setComboItem(getCompany().getTAXCode(
					data.getTAXCode()));
		}
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vendorNameText.setFocus();
	}

	@Override
	public void fitToSize(int height, int width) {
		// super.fitToSize(height, width);

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	// CustomCombo<?> getComboType(AccounterCoreType coreType) {
	// if (coreType == AccounterCoreType.PAYMENT_TERM)
	// return payTermsSelect;
	// if (coreType == AccounterCoreType.SHIPPING_METHOD)
	// return preferredShippingSelect;
	// if (coreType == AccounterCoreType.ACCOUNT)
	// return expenseAccountsSelect;
	// if (coreType == AccounterCoreType.VATCODE
	// && FinanceApplication.getCompany().getAccountingType() ==
	// ClientCompany.ACCOUNTING_TYPE_UK)
	// return supplierVatCode;
	// if (coreType == AccounterCoreType.VENDOR_GROUP)
	// return vendorGroupSelect;
	// return null;
	// }

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

		this.rpcDoSerivce.canEdit(AccounterCoreType.VENDOR, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		vendorNameText.setEnabled(!isInViewMode());
		if (getCompany().getPreferences().getUseVendorId()) {
			vendorNoText.setEnabled(!isInViewMode());
		}
		statusCheck.setEnabled(!isInViewMode());
		addButton.setEnabled(!isInViewMode());
		vendorSinceDate.setEnabled(!isInViewMode());
		addrsForm.setEnabled(!isInViewMode());
		fonFaxForm.setEnabled(!isInViewMode());
		emailForm.setEnabled(!isInViewMode());
		gridView.setEnabled(!isInViewMode());
		openingBalText.setEnabled(!isInViewMode());
		balanceDate.setEnabled(!isInViewMode());
		expenseAccountsSelect.setEnabled(!isInViewMode());
		currencyWidget.setEnabled(isInViewMode(), !isInViewMode());
		// if (!selectCurrency.equals(getCompany().getPreferences()
		// .getPrimaryCurrency())) {
		// currencyWidget.disabledFactorField(false);
		// }
		creditLimitText.setEnabled(!isInViewMode());
		preferredShippingSelect.setEnabled(!isInViewMode());
		preferredPaymentSelect.setEnabled(!isInViewMode());
		payTermsSelect.setEnabled(!isInViewMode());
		accountText.setEnabled(!isInViewMode());
		bankNameText.setEnabled(!isInViewMode());
		bankBranchText.setEnabled(!isInViewMode());
		vendorGroupSelect.setEnabled(!isInViewMode());
		panNumberText.setEnabled(!isInViewMode());
		serviceTaxRegisterationNumber.setEnabled(!isInViewMode());
		vatRegistrationNumber.setEnabled(!isInViewMode());
		vendorTaxCode.setEnabled(!isInViewMode());
		isTDS.setEnabled(!isInViewMode());
		if (getData().isTdsApplicable()) {
			vendorTDSTaxCode.setEnabled(!isInViewMode());
		}
		taxIDText.setEnabled(!isInViewMode());
		taxID.setEnabled(!isInViewMode());
		customFieldForm.setEnabled(!isInViewMode());
		addCustomFieldButton.setEnabled(!isInViewMode());
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

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		// return messages.newSupplier(Global.get().Vendor());
		return " ";
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
		widget.setEnabled(!isInViewMode());
		return widget;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected void createButtons() {
		super.createButtons();

		addButton = getAddButton();
		addButton.setEnabled(!isInViewMode());
		addButton(hPanel, addButton);
	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		removeButton(hPanel, addButton);
	}

	private AddButton getAddButton() {
		AddButton addButton = new AddButton(messages.addNew(messages.contact()));
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
		return addButton;
	}
}