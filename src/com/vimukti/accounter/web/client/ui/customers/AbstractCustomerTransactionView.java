/**
 * 
 */
package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.ContactCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.DepositInAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingMethodsCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * Abstract Class for All Customer Transaction Views
 * 
 * @author Fernandez
 * 
 */
public abstract class AbstractCustomerTransactionView<T extends ClientTransaction>
		extends AbstractTransactionBaseView<T> {

	protected CustomerCombo customerCombo;

	protected AddressCombo billToCombo, shipToCombo;
	protected DepositInAccountCombo depositInCombo;
	protected ShippingMethodsCombo shippingMethodsCombo;
	protected DynamicForm custForm;
	protected ClientTAXCode taxCode;

	protected SelectCombo statusSelect;

	protected TextItem phoneSelect;

	protected TextAreaItem billToTextArea;

	protected abstract void salesPersonSelected(ClientSalesPerson person);

	protected abstract void priceLevelSelected(ClientPriceLevel priceLevel);

	protected abstract void taxCodeSelected(ClientTAXCode taxCode);

	protected String[] phoneList;

	protected String phoneNo;

	protected String orderNum;

	protected String status;

	protected ClientShippingTerms shippingTerm;

	protected Set<ClientAddress> addressListOfCustomer;

	protected ClientAccount depositInAccount;

	protected ClientShippingMethod shippingMethod;

	protected ClientAddress billingAddress;

	protected ClientAddress shippingAddress;

	protected boolean saveAndClose;

	protected double amount;

	protected ClientCustomer customer;

	AccounterMessages messages = Global.get().messages();

	private boolean useAccountNumbers;

	// public void initTransactionsItems() {
	// if (transaction.getTransactionItems() != null)
	// customerTransactionGrid.setAllTransactionItems(transaction
	// .getTransactionItems());
	// if (transaction.getID() != 0) {
	// customerTransactionGrid.canDeleteRecord(false);
	// }
	// }

	protected abstract void initTransactionsItems();

	public AbstractCustomerTransactionView(int transactionType) {
		super(transactionType);
	}

	public double getVATRate(long TAXCodeID) {
		double vatRate = 0.0;
		if (TAXCodeID != 0) {
			// Checking the selected object is VATItem or VATGroup.
			// If it is VATItem,the we should get 'VATRate',otherwise 'GroupRate
			if (getCompany().getTAXItemGroup(
					getCompany().getTAXCode(TAXCodeID)
							.getTAXItemGrpForPurchases()) instanceof ClientTAXItem) {
				// The selected one is VATItem,so get 'VATRate' from 'VATItem'
				vatRate = ((ClientTAXItem) getCompany().getTAXItemGroup(
						getCompany().getTAXCode(TAXCodeID)
								.getTAXItemGrpForSales())).getTaxRate();
			} else {

				// The selected one is VATGroup,so get 'GroupRate' from
				// 'VATGroup'
				vatRate = ((ClientTAXGroup) getCompany().getTAXItemGroup(
						getCompany().getTAXCode(TAXCodeID)
								.getTAXItemGrpForSales())).getGroupRate();
			}
		}
		return vatRate;
	}

	protected abstract void initMemoAndReference();

	protected abstract void initTransactionTotalNonEditableItem();

	protected abstract void initSalesTaxNonEditableItem();

	protected void initDepositInAccounts() {
		// undepositedFunds = depositInCombo.getAccounts();
		//
		// depositInCombo.initCombo(undepositedFunds);
		depositInCombo.setAccounts();
		depositInAccount = depositInCombo.getSelectedValue();
		if (depositInAccount != null)
			depositInCombo.setComboItem(depositInAccount);

	}

	protected void initShippingMethod() {
		List<ClientShippingMethod> result = getCompany().getShippingMethods();
		if (shippingMethodsCombo != null) {
			shippingMethodsCombo.initCombo(result);

		}

	}

	/**
	 * 
	 * @param type
	 * @param amount
	 * @param limit
	 * @return
	 */
	public double round(int type, double amount, double limit) {
		if (amount == 0.0) {
			return 0.0;
		}
		double reminder = amount % limit;
		if (reminder == 0.0) {
			return 0.0;
		}
		switch (type) {
		case ClientCompanyPreferences.UP:
			amount += limit - reminder;
			break;
		case ClientCompanyPreferences.DOWN:
			amount -= reminder;
			break;
		default:
			// NORMAL
			double halfMark = limit / 2.0;
			if (reminder > halfMark) {
				amount += limit - reminder;
			} else if (reminder < halfMark) {
				amount -= reminder;
			} else {
				amount -= reminder;
			}
			break;
		}
		return amount;
	}

	protected void initPhones(ClientCustomer customer) {

		if (phoneSelect == null)
			return;

		Set<String> contactsPhoneList = customer.getContactsPhoneList();

		this.phoneList = contactsPhoneList.toArray(new String[contactsPhoneList
				.size()]);

		// phoneSelect.setValueMap(phoneList);
		if (phoneList != null && phoneList.length > 0)
			this.phoneNo = phoneSelect.getValue().toString();
		else
			this.phoneNo = "";

		ClientContact primaContact = customer.getPrimaryContact();

		if (primaContact != null) {
			String primaryPhone = primaContact.getBusinessPhone();
			this.phoneNo = primaryPhone;
			phoneSelect.setValue(primaryPhone);
		}

		phoneSelect.setEnabled(!isInViewMode());

	}

	public abstract List<ClientTransactionItem> getAllTransactionItems();

	protected void initContacts(ClientCustomer customer) {

		if (contactCombo == null)
			return;

		contactCombo.setEnabled(true);

		this.contacts = customer.getContacts();
		List<ClientContact> list = new ArrayList<ClientContact>(this.contacts);
		contactCombo.initCombo(list);
		contactCombo.setEnabled(!isInViewMode());
		if (contacts == null && contacts.size() == 0)
			contactCombo.setValue("");
		// if (transactionObject== null) {
		ClientContact clientContact = customer.getPrimaryContact();
		if (clientContact != null) {
			this.contact = clientContact;
			contactCombo.setComboItem(this.contact);
		} else {
			contactCombo.setValue("");
		}

		// }
		// if (transactionObject != null)
		// contactCombo.setComboItem(this.contact);
	}

	protected void contactSelected(ClientContact contact) {
		this.contact = contact;
		if (contact != null) {
			contactCombo.setComboItem(this.contact);
		}

	}

	protected void customerSelected(ClientCustomer customer) {
		if (customer == null) {
			return;
		}
		this.setCustomer(customer);
		if (customer == null)
			return;
		currency = getCurrency(customer.getCurrency());
		initContacts(customer);
		Iterator<ClientContact> iterator = contacts.iterator();
		while (iterator.hasNext()) {
			ClientContact next = iterator.next();
			if (next.isPrimary()) {
				contactCombo.setComboItem(next);
				contactSelected(next);
				break;
			} else {
				contactSelected(next);
			}
		}

		ClientCompany company = getCompany();

		salesPersonSelected(company.getSalesPerson(customer.getSalesPerson()));
		if (customer.getPaymentTerm() != 0)
			paymentTermsSelected(company.getPaymentTerms(customer
					.getPaymentTerm()));

		shippingMethodSelected(company.getShippingMethod(customer
				.getShippingMethod()));

		priceLevelSelected(company.getPriceLevel(customer.getPriceLevel()));

		paymentMethodSelected(customer.getPaymentMethod());

		paymentTermsSelected(getCompany().getPaymentTerms(
				customer.getPaymentTerm()));

		if (this.shippingMethod != null && shippingMethodsCombo != null)
			shippingMethodsCombo.setComboItem(this.shippingMethod);

		if (this.paymentMethod != null && paymentMethodCombo != null)
			paymentMethodCombo.setComboItem(customer.getPaymentMethod());

		if (this.paymentTerm != null && payTermsSelect != null)
			payTermsSelect.setComboItem(this.paymentTerm);
		// if (transactionObject == null)
		initAddressAndContacts();
		if (isTrackTax()) {
			long taxCodeID = customer.getTAXCode();
			if (taxCodeID == 0)
				taxCodeID = Accounter.getCompany().getDefaultTaxCode();
			ClientTAXCode taxCode = getCompany().getTAXCode(taxCodeID);
			if (taxCode != null) {
				taxCodeSelected(taxCode);
			}
		}
	}

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button, messages.Accounts(),
				messages.productOrServiceItem());
		// FinanceApplication.constants().salesTax());
		// FinanceApplication.constants().comment(),
		// FinanceApplication.constants().VATItem());

	}

	protected void updateTransaction() {
		super.updateTransaction();
		transaction.setTransactionDate(transactionDate.getDate());
	}

	protected void initAddressAndContacts() {
		// initContacts(getCustomer());
		initPhones(getCustomer());
		addressListOfCustomer = getCustomer().getAddress();
		initBillToCombo();
		initShipToCombo();
	}

	public CustomerCombo createCustomerComboItem(String title) {

		CustomerCombo customerCombo = new CustomerCombo(title != null ? title
				: Global.get().customer());
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						customerSelected(selectItem);
						if (contactCombo != null)
							contactCombo.setEnabled(true);
					}
				});

		customerCombo.setRequired(true);
		customerCombo.setEnabled(!isInViewMode());
		// formItems.add(customerCombo);
		setCustomer(customerCombo.getSelectedValue());
		return customerCombo;

	}

	public ContactCombo createContactComboItem() {

		ContactCombo contactCombo = new ContactCombo(messages.contact(), true);
		contactCombo.setEnabled(false);
		contactCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientContact>() {

					public void selectedComboBoxItem(ClientContact selectItem) {

						contactSelected(selectItem);

					}

				});
		contactCombo.addNewContactHandler(new ValueCallBack<ClientContact>() {

			@Override
			public void execute(ClientContact value) {
				addContactToCustomer(value);
			}
		});

		// formItems.add(contactCombo);

		return contactCombo;

	}

	/**
	 * @param value
	 */
	protected void addContactToCustomer(final ClientContact contact) {
		final ClientCustomer selectedCutomer = customerCombo.getSelectedValue();
		if (selectedCutomer == null) {
			return;
		}
		List<ClientContact> clientContacts = new ArrayList<ClientContact>();
		clientContacts.addAll(selectedCutomer.getContacts());
		for (int j = 0; j < clientContacts.size(); j++) {
			if (clientContacts.get(j).getTitle().equals(contact.getTitle())
					&& clientContacts.get(j).getEmail()
							.equals(contact.getEmail())
					&& clientContacts.get(j).getDisplayName()
							.equals(contact.getDisplayName())
					&& clientContacts.get(j).getBusinessPhone()
							.equals(contact.getBusinessPhone())) {
				Accounter.showError(messages.youHaveEnteredduplicateContacts());
				return;
			}
		}
		selectedCutomer.addContact(contact);
		AccounterAsyncCallback<Long> asyncallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				caught.printStackTrace();
			}

			public void onResultSuccess(Long result) {
				selectedCutomer.setVersion(selectedCutomer.getVersion() + 1);
				contactSelected(contact);
			}

		};
		Accounter.createCRUDService().update(selectedCutomer, asyncallBack);
	}

	public AddressCombo createBillToComboItem() {

		AddressCombo addressCombo = new AddressCombo(messages.billTo(), false);
		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						billToaddressSelected(selectItem);

					}

				});

		addressCombo.setEnabled(!isInViewMode());

		// formItems.add(addressCombo);

		return addressCombo;

	}

	protected void initShipToCombo() {

		if (shipToCombo == null || addressListOfCustomer == null)
			return;

		ClientAddress address = getAddress(ClientAddress.TYPE_SHIP_TO);
		Set<IAccounterCore> tempSet = new HashSet<IAccounterCore>();

		if (address != null) {

			tempSet.add(address);
		}
		List<ClientAddress> list = new ArrayList(tempSet);
		shipToCombo.initCombo(list);
		shipToAddressSelected(address);
		shipToCombo.setEnabled(!isInViewMode());

	}

	public ClientAddress getAddress(int type) {
		for (ClientAddress address : addressListOfCustomer) {

			if (address.getType() == type) {
				return address;
			}

		}
		return null;
	}

	protected void initBillToCombo() {

		if (billToCombo == null || addressListOfCustomer == null)
			return;

		ClientAddress address = getAddress(ClientAddress.TYPE_BILL_TO);
		Set<IAccounterCore> tempSet = new HashSet<IAccounterCore>();
		if (address != null) {

			tempSet.add(address);
		}
		List<ClientAddress> list = new ArrayList(tempSet);
		billToCombo.initCombo(list);
		billToaddressSelected(address);
		if (list == null || list.size() == 0)
			billToCombo.setEnabled(false);
		else
			billToCombo.setEnabled(!isInViewMode());

	}

	protected void shipToAddressSelected(ClientAddress selectItem) {
		this.shippingAddress = selectItem;
		if (this.shippingAddress != null && shipToCombo != null)
			shipToCombo.setComboItem(this.shippingAddress);
	}

	protected void billToaddressSelected(ClientAddress selectItem) {

		this.billingAddress = selectItem;
		if (this.billingAddress != null && billToCombo != null)
			billToCombo.setComboItem(this.billingAddress);
		else
			billToCombo.setValue("");

	}

	public AddressCombo createShipToComboItem() {

		AddressCombo shipToCombo = new AddressCombo(messages.shipTo());
		shipToCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						shipToAddressSelected(selectItem);

					}

				});

		shipToCombo.setEnabled(!isInViewMode());
		// shipToCombo.setShowDisabled(false);

		// formItems.add(shipToCombo);

		return shipToCombo;

	}

	public SalesPersonCombo createSalesPersonComboItem() {

		SalesPersonCombo salesPersonCombo = new SalesPersonCombo(
				messages.salesPerson());
		salesPersonCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientSalesPerson>() {

					public void selectedComboBoxItem(
							ClientSalesPerson selectItem) {

						salesPersonSelected(selectItem);

					}

				});

		salesPersonCombo.setEnabled(!isInViewMode());

		// formItems.add(salesPersonCombo);

		return salesPersonCombo;

	}

	public DepositInAccountCombo createDepositInComboItem(
			final AmountField endBalText) {

		DepositInAccountCombo accountCombo = new DepositInAccountCombo(
				messages.depositIn());
		accountCombo.setRequired(true);

		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {

						depositInAccountSelected(selectItem);
						if (endBalText != null) {
							endBalText.setAmount(selectItem
									.getTotalBalanceInAccountCurrency());
						}
					}

				});
		accountCombo.setEnabled(!isInViewMode());

		// formItems.add(accountCombo);

		return accountCombo;

	}

	protected ShippingMethodsCombo createShippingMethodCombo() {

		ShippingMethodsCombo shippingMethodsCombo = new ShippingMethodsCombo(
				messages.shippingMethod());
		shippingMethodsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {

					public void selectedComboBoxItem(
							ClientShippingMethod selectItem) {
						shippingMethodSelected(selectItem);
					}

				});

		shippingMethodsCombo.setEnabled(!isInViewMode());

		// formItems.add(shippingMethodsCombo);

		return shippingMethodsCombo;

	}

	protected DateField createTransactionDeliveryDateItem() {

		final DateField dateItem = new DateField(messages.deliveryDate(),
				"deliveryDate");
		dateItem.setTitle(messages.deliveryDate());

		dateItem.setEnabled(!isInViewMode());

		// formItems.add(dateItem);

		return dateItem;

	}

	protected TAXCodeCombo createTaxCodeSelectItem() {

		TAXCodeCombo taxCodeCombo = new TAXCodeCombo(messages.tax(), true);
		taxCodeCombo.setRequired(true);
		taxCodeCombo.addStyleName("tax_combo");

		taxCodeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					public void selectedComboBoxItem(ClientTAXCode selectItem) {

						taxCodeSelected(selectItem);

					}

				});

		taxCodeCombo.setEnabled(!isInViewMode());

		// formItems.add(taxCodeCombo);

		return taxCodeCombo;

	}

	// protected PriceLevelCombo createPriceLevelSelectItem() {
	//
	// PriceLevelCombo priceLevelCombo = new PriceLevelCombo(Accounter
	// .constants().priceLevel());
	// priceLevelCombo.setHelpInformation(true);
	// priceLevelCombo.setWidth(100);
	//
	// priceLevelCombo
	// .addSelectionChangeHandler(new
	// IAccounterComboSelectionChangeHandler<ClientPriceLevel>() {
	//
	// public void selectedComboBoxItem(ClientPriceLevel selectItem) {
	//
	// priceLevelSelected(selectItem);
	//
	// }
	//
	// });
	//
	// priceLevelCombo.setDisabled(isInViewMode());
	// // priceLevelCombo.setShowDisabled(false);
	//
	// // formItems.add(priceLevelCombo);
	//
	// return priceLevelCombo;
	//
	// }

	public void closeTab() {
		// MainFinanceWindow.removeFromTab(this);

	}

	public PaymentTermsCombo createPaymentTermsSelectItem() {

		PaymentTermsCombo comboItem = new PaymentTermsCombo(
				messages.paymentTerms());
		comboItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {

						paymentTermsSelected(selectItem);

					}

				});
		comboItem.setEnabled(!isInViewMode());
		// comboItem.setShowDisabled(false);
		//
		return comboItem;
	}

	protected AmountField createSalesTaxNonEditableItem(ClientCurrency currency) {

		AmountField amountItem = new AmountField(messages.salesTax(), this,
				currency, "amountItem");

		return amountItem;

	}

	protected AmountLabel createSalesTaxNonEditableLabel() {

		AmountLabel amountLabel = new AmountLabel(messages.tax());

		return amountLabel;

	}

	protected AmountField createTransactionTotalNonEditableItem(
			ClientCurrency currency) {

		AmountField amountItem = new AmountField(messages.total(), this,
				currency, "amountItem");

		return amountItem;

	}

	protected AmountLabel createTransactionTotalNonEditableLabel(
			ClientCurrency currecny) {
		AmountLabel amountLabel = new AmountLabel(
				messages.currencyTotal(currecny.getFormalName()), currecny);
		return amountLabel;

	}

	protected AmountLabel createTransactionRoundingAmountNonEditableLabel(
			ClientCurrency currecny) {
		AmountLabel amountLabel = new AmountLabel(
				messages2.currencyRoundingTotal(currecny.getFormalName()),
				currecny);
		return amountLabel;

	}

	protected AmountField createVATTotalNonEditableItem(ClientCurrency currency) {

		AmountField amountItem = new AmountField(messages.tax(), this,
				currency, "amountItem");

		amountItem.setEnabled(true);

		return amountItem;

	}

	protected AmountLabel createVATTotalNonEditableLabel() {
		AmountLabel amountLabel = new AmountLabel(messages.tax());

		return amountLabel;
	}

	@Override
	public void updateNonEditableItems() {
		// TODO Functinality to be Moved Up here Later...

	}

	@Override
	public void reload() {

		setCustomer(null);
		super.reload();

	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = super.validate();

		// Validations
		// 1. if(!isValidTransactionDate(transactionDate)) ERROR
		// 2. if( isInPreventPostingBeforeDate(transactionDate)) ERROR
		// i.e the transaction date should not be before the company's preferred
		// preventPostingBeforeDate
		// 3. custForm validation
		// 4. if(! transactionType == CUSTOMER_REFUNDS){
		// if(accountingType == US) if(!taxCodeSelect.validate()) ERROR
		// if(isBlankTransaction(customerTransactionGrid)) ERROR
		// else customerTransactionGrid.validateGrid()
		// }

		if (customerCombo.getSelectedValue() == null) {
			customerCombo.setValue("");
		}

		// if (!AccounterValidator.isValidTransactionDate(this.transactionDate))
		// {
		// result.addError(transactionDateItem,
		// messages.invalidateTransactionDate());
		// }
		if (AccounterValidator
				.isInPreventPostingBeforeDate(this.transactionDate)) {
			result.addError(transactionDateItem, messages.invalidateDate());
		}

		if (getPreferences().isTrackTax()) {
			// Exception Report
			// TODO need to get last vat period date
			for (ClientTransactionItem item : transaction.getTransactionItems()) {
				ClientTAXCode taxCode = getCompany().getTAXCode(
						item.getTaxCode());
				if (taxCode == null || !taxCode.isTaxable()) {
					continue;
				}
				ClientTAXItem taxItem = getCompany().getTaxItem(
						taxCode.getTAXItemGrpForSales());
				if (taxItem == null) {
					continue;
				}
				ClientTAXAgency taxAgency = getCompany().getTaxAgency(
						taxItem.getTaxAgency());
				if (taxAgency != null
						&& this.transactionDate.before(taxAgency
								.getLastTAXReturnDate())) {
					result.addWarning(this.transactionDate,
							messages.taxExceptionMesg());
				}
			}
		}

		if (custForm != null) {
			result.add(custForm.validate());
		}

		return result;

	}

	protected abstract boolean isBlankTransactionGrid();

	@Override
	public void init() {

		super.init();
		if (customerCombo != null && customerCombo.getSelectedValue() == null) {
			if (contactCombo != null) {
				contactCombo.setEnabled(false);
			}
		}
	}

	@Override
	// protected void onAddNew(String item) {
	// ClientTransactionItem transactionItem = new ClientTransactionItem();
	// if (item.equals(FinanceApplication.constants().accounts())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
	// } else if (item.equals(FinanceApplication.constants()
	// .items())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
	// } else if (item.equals(FinanceApplication.constants()
	// .comment())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
	// } else if (item.equals(FinanceApplication.constants()
	// .salesTax())
	// || item.equals(FinanceApplication.constants()
	// .VATItem())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
	// }
	// customerTransactionGrid.addData(transactionItem);
	//
	// }
	protected void onAddNew(String item) {
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		if (item.equals(messages.Accounts())) {
			transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
			long ztaxCodeid = getPreferences().getDefaultTaxCode();
			transactionItem
					.setTaxCode(getCustomer() != null ? (getCustomer()
							.getTAXCode() > 0 ? getCustomer().getTAXCode()
							: ztaxCodeid) : ztaxCodeid);
		} else if (item.equals(messages.productOrServiceItem())) {
			transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
			long staxCodeid = getPreferences().getDefaultTaxCode();
			transactionItem.setTaxCode(getCustomer() != null ? (getCustomer()
					.getTAXCode() != 0 ? getCustomer().getTAXCode()
					: staxCodeid) : staxCodeid);
		}
		addNewData(transactionItem);

	}

	protected abstract void addNewData(ClientTransactionItem transactionItem);

	protected void shippingMethodSelected(ClientShippingMethod selectItem) {
		this.shippingMethod = selectItem;
		if (shippingMethod != null && shippingMethodsCombo != null) {
			shippingMethodsCombo.setComboItem(getCompany().getShippingMethod(
					shippingMethod.getID()));
			shippingMethodsCombo.setEnabled(!isInViewMode());
		}

	}

	protected void paymentTermsSelected(ClientPaymentTerms paymentTerm) {

	}

	protected String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
	}

	protected void depositInAccountSelected(ClientAccount depositInAccount2) {
		this.depositInAccount = depositInAccount2;
		if (depositInAccount != null && depositInCombo != null) {

			depositInCombo.setComboItem(getCompany().getAccount(
					depositInAccount.getID()));
			depositInCombo.setEnabled(!isInViewMode());
		}

	}

	@Override
	public void onEdit() {
		if (shippingMethodsCombo != null)
			shippingMethodsCombo.setEnabled(!isInViewMode());
		if (depositInCombo != null)
			depositInCombo.setEnabled(!isInViewMode());
		if (phoneSelect != null)
			phoneSelect.setEnabled(!isInViewMode());
		if (contactCombo != null)
			contactCombo.setEnabled(!isInViewMode());
		if (shipToCombo != null)
			shipToCombo.setEnabled(!isInViewMode());
		// if (billToCombo != null)
		// billToCombo.setDisabled(isInViewMode());
		super.onEdit();
	}

	public ClientCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer;
	}

	public boolean isUseAccountNumbers() {
		return useAccountNumbers;
	}

	public void setUseAccountNumbers(boolean useAccountNumbers) {
		this.useAccountNumbers = useAccountNumbers;
	}

	@Override
	protected void addAccount() {
		ClientTransactionItem transactionItem = new ClientTransactionItem();

		transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
		if (isTrackTax() && getPreferences().isTaxPerDetailLine()) {
			long defaultTax = getPreferences().getDefaultTaxCode();
			transactionItem
					.setTaxCode(getCustomer() != null ? (getCustomer()
							.getTAXCode() > 0 ? getCustomer().getTAXCode()
							: defaultTax) : defaultTax);
		} else {
			if (taxCode != null) {
				transactionItem.setTaxCode(taxCode.getID());
			}
		}
		// if (zvatCodeid != null)
		// transactionItem.setVatCode(zvatCodeid);
		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				transactionItem.setDiscount(discountField.getPercentage());
			}
		}
		addAccountTransactionItem(transactionItem);
	}

	@Override
	protected void addItem() {

		ClientTransactionItem transactionItem = new ClientTransactionItem();
		transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
		long defaultTaxCode = getPreferences().getDefaultTaxCode();
		if (isTrackTax() && getPreferences().isTaxPerDetailLine()) {
			transactionItem.setTaxCode(getCustomer() != null ? (getCustomer()
					.getTAXCode() != 0 ? getCustomer().getTAXCode()
					: defaultTaxCode) : defaultTaxCode);
		} else {
			if (taxCode != null) {
				transactionItem.setTaxCode(taxCode.getID());
			}
		}
		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				transactionItem.setDiscount(discountField.getPercentage());
			}
		}
		if (isTrackClass() && !getPreferences().isClassPerDetailLine()
				&& accounterClass != null) {
			transactionItem.setAccounterClass(accounterClass.getID());
		}
		addItemTransactionItem(transactionItem);

	}

	protected ShippingTermsCombo createShippingTermsCombo() {

		final ShippingTermsCombo shippingTermsCombo = new ShippingTermsCombo(
				messages.shippingTerms());
		shippingTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingTerms>() {

					@Override
					public void selectedComboBoxItem(
							ClientShippingTerms selectItem) {
						shippingTerm = selectItem;
						if (shippingTerm != null && shippingTermsCombo != null) {
							shippingTermsCombo.setComboItem(getCompany()
									.getShippingTerms(shippingTerm.getID()));
							shippingTermsCombo.setEnabled(!isInViewMode());
						}
					}

				});

		shippingTermsCombo.setEnabled(!isInViewMode());
		return shippingTermsCombo;
	}

	protected abstract void addAccountTransactionItem(ClientTransactionItem item);

	protected abstract void addItemTransactionItem(ClientTransactionItem item);

	protected String isExceedCreditLimit(ClientCustomer customer, double total) {
		if (customer == null
				|| DecimalUtil.isEquals(customer.getCreditLimit(), 0)) {
			return null;
		}
		double customerBalance = customer.getBalance();
		double finalBalance = customerBalance + total;
		if (DecimalUtil.isGreaterThan(finalBalance, customer.getCreditLimit())) {
			return messages.creditLimitExceed(customer.getName(),
					DataUtils.getAmountAsStrings(customer.getCreditLimit()),
					DataUtils.getAmountAsStrings(finalBalance));
		}
		return null;
	}

}
