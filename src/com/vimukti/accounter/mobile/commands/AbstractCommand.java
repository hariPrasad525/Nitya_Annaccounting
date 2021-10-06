package com.vimukti.accounter.mobile.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

public abstract class AbstractCommand extends AbstractBaseCommand {
	public static final String FIRST_MESSAGE = "firstMessage";
	private AccounterMessages messages;
	private AccounterMessages2 messages2;
	protected static final String AMOUNTS_INCLUDE_TAX = "Amounts Include tax";
	// protected static final String COUNTRY = "country";
	protected static final String PHONE = "phone";
	protected static final String EMAIL = "email";
	protected static final String ACTIONS = "actions";
	protected static final int VALUES_TO_SHOW = 20;
	protected static final int COUNTRIES_TO_SHOW = 10;
	protected static final String VIEW_BY = "viewBy";
	protected static final String FROM_DATE = "fromDate";
	protected static final String TO_DATE = "to_date";
	protected static final String DATE_RANGE = "dateRange";
	protected static final String CURRENCY = "Currency";
	protected static final String CURRENCY_FACTOR = "Currency Factor";

	public AbstractCommand() {

	}

	@Override
	public void init() {
		messages = Global.get().messages();
		messages2 = Global.get().messages2();
		super.init();
	}

	@Override
	public AccounterMessages getMessages() {
		return messages;
	}

	public AccounterMessages2 getMessages2() {
		return messages2;
	}

	protected void create(IAccounterCore coreObject, Context context) {
		try {
			if (coreObject.getID() == 0) {
				String clientClassSimpleName = coreObject.getObjectType()
						.getClientClassSimpleName();
				OperationContext opContext = new OperationContext(context
						.getCompany().getID(), coreObject, context
						.getIOSession().getUserEmail());

				opContext.setArg2(clientClassSimpleName);

				new FinanceTool().create(opContext);
			} else {
				String serverClassFullyQualifiedName = coreObject
						.getObjectType().getServerClassFullyQualifiedName();

				OperationContext opContext = new OperationContext(context
						.getCompany().getID(), coreObject, context
						.getIOSession().getUserEmail(),
						String.valueOf(coreObject.getID()),
						serverClassFullyQualifiedName);

				new FinanceTool().update(opContext);
			}
		} catch (AccounterException e) {
			int errorCode = e.getErrorCode();
			addFirstMessage(context, showErrorCode(errorCode));
		}
	}

	protected long getNumberFromString(String string) {
		if (string.isEmpty()) {
			return 0;
		}
		if (string.charAt(0) != '#') {
			return 0;
		}
		string = string.substring(1);
		if (string.isEmpty()) {
			return 0;
		}
		return Long.parseLong(string);
	}

	@SuppressWarnings("unchecked")
	public void addFirstMessage(Context context, String string) {
		((List<String>) context.getAttribute(FIRST_MESSAGE)).add(string);
	}

	protected List<String> getPaymentMethods() {
		List<String> listOfPaymentMethods = new ArrayList<String>();
		listOfPaymentMethods.add(getMessages().cash());
		listOfPaymentMethods.add(getMessages().cheque());
		listOfPaymentMethods.add(getMessages().creditCard());
		listOfPaymentMethods.add(getMessages().directDebit());
		listOfPaymentMethods.add(getMessages().masterCard());
		listOfPaymentMethods.add(getMessages().onlineBanking());
		listOfPaymentMethods.add(getMessages().standingOrder());
		listOfPaymentMethods.add(getMessages().switchMaestro());
		return listOfPaymentMethods;
	}

	/**
	 * set address Type
	 * 
	 * @param type
	 * @return
	 */
	public int getAddressType(String type) {
		if (type.equalsIgnoreCase("1"))
			return ClientAddress.TYPE_BUSINESS;
		else if (type.equalsIgnoreCase(getMessages().billTo()))
			return ClientAddress.TYPE_BILL_TO;
		else if (type.equalsIgnoreCase(getMessages().shipTo()))
			return ClientAddress.TYPE_SHIP_TO;
		else if (type.equalsIgnoreCase("2"))
			return ClientAddress.TYPE_WAREHOUSE;
		else if (type.equalsIgnoreCase("3"))
			return ClientAddress.TYPE_LEGAL;
		else if (type.equalsIgnoreCase("4"))
			return ClientAddress.TYPE_POSTAL;
		else if (type.equalsIgnoreCase("5"))
			return ClientAddress.TYPE_HOME;
		else if (type.equalsIgnoreCase(messages.company()))
			return ClientAddress.TYPE_COMPANY;
		else if (type.equalsIgnoreCase(messages.companyregistration()))
			return ClientAddress.TYPE_COMPANY_REGISTRATION;

		return ClientAddress.TYPE_OTHER;
	}

	protected ClientContact toClientContact(Contact contact) {
		if (contact == null) {
			return null;
		}
		ClientContact clientContact = new ClientContact();
		clientContact.setBusinessPhone(contact.getBusinessPhone());
		clientContact.setEmail(contact.getEmail());
		clientContact.setID(contact.getID());
		clientContact.setName(contact.getName());
		clientContact.setPrimary(contact.isPrimary());
		clientContact.setTitle(contact.getTitle());
		clientContact.setVersion(contact.getVersion());
		return clientContact;
	}

	protected Contact toServerContact(ClientContact contact) {
		if (contact == null) {
			return null;
		}
		Contact clientContact = new Contact();
		clientContact.setBusinessPhone(contact.getBusinessPhone());
		clientContact.setEmail(contact.getEmail());
		clientContact.setName(contact.getName());
		clientContact.setPrimary(contact.isPrimary());
		clientContact.setTitle(contact.getTitle());
		clientContact.setVersion(contact.getVersion());
		return clientContact;
	}

	protected ClientAddress toClientAddress(Address address) {
		if (address == null) {
			return null;
		}
		ClientAddress clientAddress = new ClientAddress();
		clientAddress.setAddress1(address.getAddress1());
		clientAddress.setCity(address.getCity());
		clientAddress.setCountryOrRegion(address.getCountryOrRegion());
		clientAddress.setID(address.getID());
		clientAddress.setIsSelected(address.getIsSelected());
		clientAddress.setStateOrProvinence(address.getStateOrProvinence());
		clientAddress.setStreet(address.getStreet());
		clientAddress.setType(address.getType());
		clientAddress.setVersion(address.getVersion());
		clientAddress.setZipOrPostalCode(address.getZipOrPostalCode());
		return clientAddress;
	}

	protected Address toServerAddress(ClientAddress clientAddress) {
		if (clientAddress == null) {
			return null;
		}
		Address address = new Address();
		address.setAddress1(address.getAddress1());
		address.setCity(address.getCity());
		address.setCountryOrRegion(address.getCountryOrRegion());
		address.setId(address.getID());
		address.setSelected(address.getIsSelected());
		address.setStateOrProvinence(address.getStateOrProvinence());
		address.setStreet(address.getStreet());
		address.setType(address.getType());
		address.setVersion(address.getVersion());
		address.setZipOrPostalCode(address.getZipOrPostalCode());
		return address;
	}

	protected List<Customer> getCustomers() {
		Session currentSession = HibernateUtil.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Customer> customers = currentSession
				.getNamedQuery("getCustomersOrderByName")
				.setParameter("company", getCompany()).list();
		return new ArrayList<Customer>(customers);
	}

	protected List<Vendor> getVendors() {
		Session currentSession = HibernateUtil.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Vendor> customers = currentSession
				.getNamedQuery("getVendorsOrderByName")
				.setParameter("company", getCompany()).list();
		return new ArrayList<Vendor>(customers);
	}

	// /**
	// * get client currency object by id
	// *
	// * @param currency
	// * @return {@link ClientCurrency}
	// */
	// protected ClientCurrency getCurrency(long currency) {
	// return (ClientCurrency) CommandUtils.getClientObjectById(currency,
	// AccounterCoreType.CURRENCY, getCompanyId());
	// }

	public long autoGenerateAccountnumber(int range1, int range2,
			Company company) {
		// TODO::: add a filter to filter the accounts based on the account type
		Set<Account> accounts = company.getAccounts();
		Long number = (long) range1;
		for (Account account : accounts) {
			while (number.toString().equals(account.getNumber())) {
				number++;
				if (number >= range2) {
					number = (long) range1;
				}
			}
		}
		return number;
	}

	protected String showErrorCode(int errorCode) {
		switch (errorCode) {
		case AccounterException.ERROR_NUMBER_CONFLICT:
			return getMessages().numberConflict();

		case AccounterException.ERROR_NAME_CONFLICT:
			return getMessages().nameConflict();

		case AccounterException.ERROR_TRAN_CONFLICT:
			return getMessages().transactionConflict();

		case AccounterException.ERROR_PERMISSION_DENIED:
			return getMessages().permissionDenied();

		case AccounterException.ERROR_INTERNAL:
			return getMessages().internal();

		case AccounterException.ERROR_ILLEGAL_ARGUMENT:
			return getMessages().illegalArgument();

		case AccounterException.ERROR_NO_SUCH_OBJECT:
			return getMessages().cantVoid();

		case AccounterException.ERROR_DEPOSITED_FROM_UNDEPOSITED_FUNDS:
			return getMessages().depositedFromUndepositedFunds();

		case AccounterException.ERROR_CANT_EDIT:
			return getMessages().cantEdit();

		case AccounterException.ERROR_CANT_VOID:
			return getMessages().cantVoid();

		case AccounterException.ERROR_RECEIVE_PAYMENT_DISCOUNT_USED:
			return getMessages().receivePaymentDiscountUsed();

		case AccounterException.ERROR_OBJECT_IN_USE:
			return getMessages().objectInUse();

		case AccounterException.ERROR_VERSION_MISMATCH:
			return getMessages().objectModified();
		case AccounterException.ERROR_EDITING_TRANSACTION_RECONCILIED:
			return getMessages().errorEditingTransactionReconcilied();
		case AccounterException.ERROR_VOIDING_TRANSACTION_RECONCILIED:
			return getMessages().errorVoidingTransactionReconcilied();
		case AccounterException.ERROR_DELETING_TRANSACTION_RECONCILIED:
			return getMessages().errorDeletingTransactionReconcilied();
		case AccounterException.USED_IN_INVOICE:
			return getMessages().usedinInvoiceSoYoucantEdit();
		case AccounterException.INVOICE_PAID_VOID_IT:
			return getMessages().usedinReceivepayYoucantEdit();

		case AccounterException.ERROR_CANT_EDIT_DELETE:
			return getMessages().cantEditOrDelete();

		case AccounterException.ERROR_CUSTOMER_NULL:
			return getMessages().pleaseSelect(Global.get().Customer());

		case AccounterException.ERROR_VENDOR_NULL:
			return getMessages().pleaseSelect(Global.get().Vendor());

		case AccounterException.ERROR_TAX_CODE_NULL:
			return getMessages().pleaseSelect(getMessages().taxCode());

		case AccounterException.ERROR_ACCOUNT_NULL:
			return getMessages().pleaseSelect(getMessages().Account());

		case AccounterException.ERROR_TRANSACTION_ITEM_NULL:
			return getMessages().pleaseSelect(getMessages().transactionItem());

		case AccounterException.ERROR_TRANSACTION_TOTAL_ZERO:
			return getMessages().transactionitemtotalcannotbe0orlessthan0();

		case AccounterException.ERROR_AMOUNT_ZERO:
			return getMessages().shouldNotbeZero(getMessages().amount());

		case AccounterException.ERROR_PAY_FROM_NULL:
			return getMessages().pleaseSelect(getMessages().payFrom());

		case AccounterException.ERROR_PAY_TO_NULL:
			return getMessages().pleaseSelect(getMessages().payTo());

		case AccounterException.ERROR_DEPOSIT_FROM_NULL:
			return getMessages().pleaseSelect(getMessages().transferFrom());

		case AccounterException.ERROR_DEPOSIT_TO_NULL:
			return getMessages().pleaseSelect(getMessages().transferTo());

		case AccounterException.ERROR_PAYMENT_METHOD_NULL:
			return getMessages().pleaseSelect(getMessages().paymentMethod());

		case AccounterException.ERROR_BANK_ACCOUNT_NULL:
			return getMessages().pleaseSelect(getMessages().bankAccount());

		case AccounterException.ERROR_CREDIT_DEBIT_TOTALS_NOT_EQUAL:
			return getMessages().totalMustBeSame();

		case AccounterException.ERROR_INCOME_ACCOUNT_NULL:
			return getMessages().pleaseSelect(getMessages().incomeAccount());

		case AccounterException.ERROR_EXPENSE_ACCOUNT_NULL:
			return getMessages().pleaseSelect(getMessages().expenseAccount());

		case AccounterException.ERROR_CUSTOMER_NAME_EMPTY:
			return getMessages().pleaseEnterName(Global.get().Customer());

		case AccounterException.ERROR_CUSTOMER_NUMBER_EMPTY:
			return getMessages().pleaseEnter(
					getMessages().payeeNumber(Global.get().Customer()));

		case AccounterException.ERROR_VENDOR_NAME_EMPTY:
			return getMessages().pleaseEnterName(Global.get().Vendor());

		case AccounterException.ERROR_VENDOR_NUMBER_EMPTY:
			return getMessages().pleaseEnter(
					getMessages().payeeNumber(Global.get().Vendor()));
		case AccounterException.ERROR_THERE_IS_NO_TRANSACTION_ITEMS:
			return getMessages().thereAreNoTransactionItemsToSave();

		case AccounterException.ERROR_ITEM_NAME_NULL:
			return getMessages().pleaseEnterName(getMessages().item());

		case AccounterException.ERROR_TRANSACTION_ITEM_TOTAL_0:
			return getMessages().transactionitemtotalcannotbe0orlessthan0();

		case AccounterException.WRITECHECK_PAID_VOID_IT:
			return getMessages().writeCheckPaid();

		default:
			return null;
		}
	}

	public <T> T getServerObject(Class<T> class1, long id) {
		return (T) HibernateUtil.getCurrentSession().get(class1, id);
	}

	protected String getAmountWithCurrency(double amount) {
		String symbol = getPreferences().getPrimaryCurrency().getSymbol();
		return Global.get().toCurrencyFormat(amount, symbol);
	}

	protected String getAmountWithCurrency(double amount, String symbol) {
		return Global.get().toCurrencyFormat(amount, symbol);
	}

	public String getDateByCompanyType(ClientFinanceDate value) {
		SimpleDateFormat format = new SimpleDateFormat(getPreferences()
				.getDateFormat());
		return format.format(value.getDateAsObject());

	}
}
