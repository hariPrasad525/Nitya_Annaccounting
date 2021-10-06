package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.core.ShippingMethod;
import com.nitya.accounter.core.ShippingTerms;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.UserCommand;
import com.nitya.accounter.mobile.requirements.AccountRequirement;
import com.nitya.accounter.mobile.requirements.AmountRequirement;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.ChangeListner;
import com.nitya.accounter.mobile.requirements.ContactRequirement;
import com.nitya.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.nitya.accounter.mobile.requirements.CustomerRequirement;
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.requirements.NumberRequirement;
import com.nitya.accounter.mobile.requirements.ShippingMethodRequirement;
import com.nitya.accounter.mobile.requirements.ShippingTermRequirement;
import com.nitya.accounter.mobile.requirements.StringListRequirement;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.mobile.requirements.TaxCodeRequirement;
import com.nitya.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.nitya.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientCashSales;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;

public class CreateCashSaleCommand extends AbstractTransactionCommand {

	private static final String DELIVERY_DATE = "deliveryDate";
	private static final String DEPOSIT_OR_TRANSFER_TO = "depositOrTransferTo";
	private static final String SHIPPING_TERMS = "shippingTerms";
	private static final String SHIPPING_METHODS = "shippingMethods";
	ClientCashSales cashSale;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER, getMessages()
				.pleaseEnterNameOrNumber(Global.get().Customer()),
				getMessages().payeeNumber(Global.get().Customer()), false,
				true, new ChangeListner<Customer>() {

					@Override
					public void onSelection(Customer value) {
						CreateCashSaleCommand.this.get(PHONE).setValue(
								value.getPhoneNo());

						CreateCashSaleCommand.this.get(CONTACT).setValue(null);
						for (Contact contact : value.getContacts()) {
							if (contact.isPrimary()) {
								CreateCashSaleCommand.this.get(CONTACT)
										.setValue(contact);
								break;
							}
						}
						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							CreateCashSaleCommand.this
									.get(CURRENCY_FACTOR)
									.setValue(
											mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}
					}
				}) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}
		});
		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter("Currency Factor"), getMessages().currencyFactor()) {

			@Override
			protected Currency getCurrency() {
				Customer customer = (Customer) CreateCashSaleCommand.this.get(
						CUSTOMER).getValue();
				return customer.getCurrency();
			}

		});

		list.add(new AmountRequirement(DISCOUNT, getMessages().pleaseEnter(
				getMessages().discount()), getMessages().discount(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackDiscounts()
						&& !getPreferences().isDiscountPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

		});

		list.add(new TransactionItemTableRequirement(ITEMS,
				"Please Enter Item Name or number", getMessages().items(),
				true, true) {
			@Override
			protected double getCurrencyFactor() {
				return CreateCashSaleCommand.this.getCurrencyFactor();
			}

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.isISellThisItem()) {
						if (item.isActive())
							items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return true;
			}

			@Override
			protected Currency getCurrency() {
				return ((Customer) CreateCashSaleCommand.this.get(CUSTOMER)
						.getValue()).getCurrency();
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateCashSaleCommand.this.get(DISCOUNT)
						.getValue();
				return value2;
			}

		});

		list.add(new TransactionAccountTableRequirement(ACCOUNTS, getMessages()
				.pleaseEnterNameOrNumber(getMessages().Account()),
				getMessages().Account(), true, true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account account) {
							if (account.getIsActive()
									&& account.getType() != ClientAccount.TYPE_CASH
									&& account.getType() != ClientAccount.TYPE_BANK
									&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
									&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
									&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
									&& account.getType() != ClientAccount.TYPE_EXPENSE
									&& account.getType() != ClientAccount.TYPE_OTHER_EXPENSE
									&& account.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD
									&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
									&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
									&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY
									&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
									&& account.getType() != ClientAccount.TYPE_EQUITY) {
								return true;
							} else {
								return false;
							}
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			public boolean isSales() {
				return true;
			}

			@Override
			protected Currency getCurrency() {
				return ((Customer) CreateCashSaleCommand.this.get(CUSTOMER)
						.getValue()).getCurrency();
			}

			@Override
			protected boolean isTrackTaxPaidAccount() {
				return true;
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateCashSaleCommand.this.get(DISCOUNT)
						.getValue();
				return value2;
			}

		});
		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getMessages().paymentMethod()), getMessages()
				.paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return new ArrayList<String>(CommandUtils.getPaymentMethods());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().paymentMethod());
			}
		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, false));

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contacts(), true,
				true, null) {

			@Override
			protected Payee getPayee() {
				return get(CUSTOMER).getValue();
			}
		});

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phone(), true, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

		list.add(new AccountRequirement(DEPOSIT_OR_TRANSFER_TO, getMessages()
				.pleaseEnterNameOrNumber(getMessages().depositAccount()),
				getMessages().depositAccount(), false, true, null) {

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("createBankAccount", getMessages()
						.bank()));
				list.add(new UserCommand("createBankAccount",
						"Create Other CurrentAsset Account", getMessages()
								.otherCurrentAsset()));
				list.add(new UserCommand("createBankAccount",
						"Create CreditAccount", getMessages().creditCard()));
				list.add(new UserCommand("createBankAccount",
						"Create FixedAsset Account", getMessages().fixedAsset()));
				list.add(new UserCommand("createBankAccount",
						"Create Paypal Account", getMessages().paypal()));
			}

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().depositAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account acc) {
							return acc.getIsActive()
									&& Arrays
											.asList(ClientAccount.TYPE_BANK,
													ClientAccount.TYPE_CASH,
													ClientAccount.TYPE_PAYPAL,
													ClientAccount.TYPE_CREDIT_CARD,
													ClientAccount.TYPE_OTHER_CURRENT_ASSET,
													ClientAccount.TYPE_INVENTORY_ASSET,
													ClientAccount.TYPE_FIXED_ASSET)
											.contains(acc.getType())
									&& acc.getType() != Account.TYPE_ACCOUNT_RECEIVABLE;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Account());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().equals(name);
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				new ChangeListner<TAXCode>() {

					@Override
					public void onSelection(TAXCode value) {
						setTaxCodeToItems(value);
					}
				}) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& !getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return "Include VAT with Amount enabled";
			}

			@Override
			protected String getFalseString() {
				return "Include VAT with Amount disabled";
			}
		});

		list.add(new ShippingTermRequirement(SHIPPING_TERMS, getMessages()
				.pleaseEnterName(getMessages().shippingTerm()), getMessages()
				.shippingTerm(), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isDoProductShipMents()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().shippingTerm());
			}

			@Override
			protected List<ShippingTerms> getLists(Context context) {
				return new ArrayList<ShippingTerms>(context.getCompany()
						.getShippingTerms());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().shippingTerms());
			}

			@Override
			protected boolean filter(ShippingTerms e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new ShippingMethodRequirement(SHIPPING_METHODS, getMessages()
				.pleaseEnterName(getMessages().shippingMethod()), getMessages()
				.shippingMethod(), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isDoProductShipMents()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().shippingMethod());
			}

			@Override
			protected List<ShippingMethod> getLists(Context context) {
				return new ArrayList<ShippingMethod>(context.getCompany()
						.getShippingMethods());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().shippingMethod());
			}

		});

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getMessages().deliveryDate()), getMessages().deliveryDate(),
				true, true));
	}

	protected void setTaxCodeToItems(TAXCode value) {
		List<ClientTransactionItem> items = this.get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		List<ClientTransactionItem> allrecords = new ArrayList<ClientTransactionItem>();
		allrecords.addAll(items);
		allrecords.addAll(accounts);
		for (ClientTransactionItem clientTransactionItem : allrecords) {
			clientTransactionItem.setTaxCode(value.getID());
		}
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}
		ClientFinanceDate date = get(DATE).getValue();
		cashSale.setDate(date.getDate());

		cashSale.setType(Transaction.TYPE_CASH_SALES);
		String number = get(NUMBER).getValue();
		cashSale.setNumber(number);

		accounts.addAll(items);
		cashSale.setTransactionItems(accounts);

		ShippingTerms shippingTerms = get(SHIPPING_TERMS).getValue();
		cashSale.setShippingTerm(shippingTerms != null ? shippingTerms.getID()
				: 0);

		ShippingMethod shippingMethod = get(SHIPPING_METHODS).getValue();
		cashSale.setShippingMethod(shippingMethod != null ? shippingMethod
				.getID() : 0);

		Customer customer = get(CUSTOMER).getValue();
		cashSale.setCustomer(customer.getID());

		Set<Address> addresses = customer.getAddress();
		for (Address address : addresses) {
			if (address.getType() == Address.TYPE_BILL_TO) {
				try {
					ClientAddress addr = new ClientConvertUtil()
							.toClientObject(address, ClientAddress.class);
					cashSale.setBillingAddress(addr);
				} catch (AccounterException e) {
					e.printStackTrace();
				}
				break;
			}
		}

		Contact contact = get(CONTACT).getValue();
		cashSale.setContact(toClientContact(contact));

		cashSale.setShippingAdress(getAddress(ClientAddress.TYPE_SHIP_TO,
				customer));
		cashSale.setShippingAdress(getAddress(ClientAddress.TYPE_BILL_TO,
				customer));

		String phone = get(PHONE).getValue();
		cashSale.setPhone(phone);

		String memo = get(MEMO).getValue();
		cashSale.setMemo(memo);

		String paymentMethod = get(PAYMENT_METHOD).getValue();
		cashSale.setPaymentMethod(paymentMethod);

		Account account = get(DEPOSIT_OR_TRANSFER_TO).getValue();
		cashSale.setDepositIn(account.getID());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		cashSale.setDeliverydate(deliveryDate.getDate());

		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		cashSale.setCurrency(customer.getCurrency().getID());
		cashSale.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		double taxTotal = updateTotals(context, cashSale, true);
		cashSale.setTaxTotal(taxTotal);
		create(cashSale, context);

		return null;
	}

	public ClientAddress getAddress(int type, Customer customer) {
		for (Address address : customer.getAddress()) {

			if (address.getType() == type) {
				return toClientAddress(address);
			}

		}
		return null;
	}

	private String getNextTransactionNumber(Context context) {
		String nextTransactionNumber = new FinanceTool()
				.getNextTransactionNumber(ClientTransaction.TYPE_CASH_SALES,
						context.getCompany().getID());
		return nextTransactionNumber;
	}

	@Override
	protected String getWelcomeMessage() {
		return cashSale.getID() == 0 ? getMessages().creating(
				getMessages().cashSale()) : "Cash sale updating...";
	}

	@Override
	protected String getDetailsMessage() {
		List<?> list = get(ITEMS).getValue();
		List<?> list2 = get(ACCOUNTS).getValue();
		return list.size() != 0 || list2.size() != 0 ? cashSale.getID() == 0 ? getMessages()
				.readyToCreate(getMessages().cashSale()) : getMessages()
				.readyToUpdate(getMessages().cashSale())
				: null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(getNextTransactionNumber(context));
		Contact contact = new Contact();
		contact.setName(null);
		get(CONTACT).setDefaultValue(contact);
		get(PAYMENT_METHOD).setDefaultValue(getMessages().cash());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		get(DISCOUNT).setDefaultValue(0.0);

	}

	@Override
	public String getSuccessMessage() {
		return cashSale.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().cashSale()) : getMessages().updateSuccessfully(
				getMessages().cashSale());
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			addFirstMessage(context, getMessages()
					.transactiontotalcannotbe0orlessthan0());
		}
		super.beforeFinishing(context, makeResult);
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().cashSale()));
				return "Invoices List";
			}
			cashSale = getTransaction(string, AccounterCoreType.CASHSALES,
					context);
			if (cashSale == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().cashSale()));
				return "Invoices List " + string;
			}
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			cashSale = new ClientCashSales();
		}
		setTransaction(cashSale);
		return null;
	}

	private void setValues() {
		get(CURRENCY_FACTOR).setValue(cashSale.getCurrencyFactor());
		List<ClientTransactionItem> transactionItems = cashSale
				.getTransactionItems();
		List<ClientTransactionItem> items = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> accounts = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				accounts.add(clientTransactionItem);
			} else if (clientTransactionItem.getType() == ClientTransactionItem.TYPE_ITEM) {
				items.add(clientTransactionItem);
			}
		}
		get(ITEMS).setValue(items);
		get(ACCOUNTS).setValue(accounts);
		get(DATE).setValue(cashSale.getDate());
		get(NUMBER).setValue(cashSale.getNumber());
		get(SHIPPING_TERMS).setValue(
				CommandUtils.getServerObjectById(cashSale.getShippingTerm(),
						AccounterCoreType.SHIPPING_TERM));
		get(SHIPPING_METHODS).setValue(
				CommandUtils.getServerObjectById(cashSale.getShippingMethod(),
						AccounterCoreType.SHIPPING_METHOD));
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(cashSale.getCustomer(),
						AccounterCoreType.CUSTOMER));
		get(CONTACT).setValue(toServerContact(cashSale.getContact()));
		get(PHONE).setValue(cashSale.getPhone());
		get(MEMO).setValue(cashSale.getMemo());
		get(PAYMENT_METHOD).setValue(
				CommandUtils.getPaymentMethod(
						cashSale.getPaymentMethodForCommands(), getMessages()));
		get(DEPOSIT_OR_TRANSFER_TO).setValue(
				CommandUtils.getServerObjectById(cashSale.getDepositIn(),
						AccounterCoreType.ACCOUNT));
		get(DELIVERY_DATE).setValue(
				new ClientFinanceDate(cashSale.getDeliverydate()));
		if (getPreferences().isTrackDiscounts()
				&& !getPreferences().isDiscountPerDetailLine()) {
			get(DISCOUNT).setValue(
					getDiscountFromTransactionItems(cashSale
							.getTransactionItems()));
		}

	}

	@Override
	protected Currency getCurrency() {
		return ((Customer) CreateCashSaleCommand.this.get(CUSTOMER).getValue())
				.getCurrency();
	}

}
