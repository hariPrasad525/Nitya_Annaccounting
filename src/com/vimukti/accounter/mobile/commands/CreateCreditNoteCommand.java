package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CreateCreditNoteCommand extends AbstractTransactionCommand {
	ClientCustomerCreditMemo creditMemo;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER, getMessages().pleaseEnter(
				Global.get().customer()), Global.get().customer(), false, true,
				new ChangeListner<Customer>() {

					@Override
					public void onSelection(Customer value) {
						get(BILL_TO).setValue(null);
						Set<Address> addresses = value.getAddress();
						for (Address address : addresses) {
							if (address.getType() == Address.TYPE_BILL_TO) {
								try {
									ClientAddress addr = new ClientConvertUtil()
											.toClientObject(address,
													ClientAddress.class);
									get(BILL_TO).setValue(addr);
								} catch (AccounterException e) {
									e.printStackTrace();
								}
								break;
							}
						}

						CreateCreditNoteCommand.this.get(CONTACT)
								.setValue(null);
						for (Contact contact : value.getContacts()) {
							if (contact.isPrimary()) {
								CreateCreditNoteCommand.this.get(CONTACT)
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
							CreateCreditNoteCommand.this
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

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().creditNo()), getMessages().creditNo(), true, true));
		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contact(), true,
				true, null) {
			@Override
			protected Payee getPayee() {
				return get(CUSTOMER).getValue();
			}
		});

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));

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
									&& account.getType() == ClientAccount.TYPE_INCOME) {
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
				return ((Customer) CreateCreditNoteCommand.this.get(CUSTOMER)
						.getValue()).getCurrency();
			}

			@Override
			protected boolean isTrackTaxPaidAccount() {
				return true;
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateCreditNoteCommand.this.get(DISCOUNT)
						.getValue();
				return value2;
			}
		});

		list.add(new TransactionItemTableRequirement(ITEMS, getMessages()
				.pleaseEnter(getMessages().itemName()), getMessages().items(),
				true, true) {
			@Override
			protected double getCurrencyFactor() {
				return CreateCreditNoteCommand.this.getCurrencyFactor();
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
				return ((Customer) CreateCreditNoteCommand.this.get(CUSTOMER)
						.getValue()).getCurrency();
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateCreditNoteCommand.this.get(DISCOUNT)
						.getValue();
				return value2;
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {

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
				return e.getName().contains(name);
			}
		});
		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected Currency getCurrency() {
				Customer customer = (Customer) CreateCreditNoteCommand.this
						.get(CUSTOMER).getValue();
				return customer.getCurrency();
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
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().customerCreditNote(
										Global.get().Customer())));
				return "invoices";
			}

			creditMemo = getTransaction(string,
					AccounterCoreType.CUSTOMERCREDITMEMO, context);

			if (creditMemo == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().customerCreditNote(
										Global.get().Customer())));
				return "invoices " + string;
			}
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			creditMemo = new ClientCustomerCreditMemo();
		}
		setTransaction(creditMemo);
		return null;
	}

	private void setValues() {
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(creditMemo.getCustomer(),
						AccounterCoreType.CUSTOMER));
		get(DATE).setValue(creditMemo.getDate());
		get(NUMBER).setValue(creditMemo.getNumber());
		get(CONTACT).setValue(toServerContact(creditMemo.getContact()));
		get(BILL_TO).setValue(creditMemo.getBillingAddress());
		/* get(CURRENCY_FACTOR).setValue(creditMemo.getCurrencyFactor()); */
		List<ClientTransactionItem> items = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> accounts = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> transactionItems = creditMemo
				.getTransactionItems();
		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				accounts.add(clientTransactionItem);
			} else {
				items.add(clientTransactionItem);
			}
		}
		if (getPreferences().isTrackDiscounts()
				&& !getPreferences().isDiscountPerDetailLine()) {
			get(DISCOUNT).setValue(
					getDiscountFromTransactionItems(creditMemo
							.getTransactionItems()));
		}
		get(ITEMS).setValue(items);
		get(ACCOUNTS).setValue(accounts);
		get(IS_VAT_INCLUSIVE).setValue(isAmountIncludeTAX(creditMemo));
	}

	@Override
	protected String getWelcomeMessage() {
		return creditMemo.getID() == 0 ? "Create  New Customer CreditNote......."
				: "Updating Customer credit note";
	}

	@Override
	protected String getDetailsMessage() {
		
		List<?> list = get(ITEMS).getValue();
		List<?> list2 = get(ACCOUNTS).getValue();
				
		return list.size() != 0||list2.size() != 0?creditMemo.getID() == 0 ? getMessages().readyToCreate(
				getMessages().customerCreditNote(Global.get().customer()))
				: getMessages().readyToUpdate(
						getMessages().customerCreditNote(
								Global.get().customer())): null;
						
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
						context.getCompany()));
		get(CONTACT).setDefaultValue(null);
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */
		get(BILL_TO).setDefaultValue(new ClientAddress());
		get(DISCOUNT).setDefaultValue(0.0);

	}

	@Override
	public String getSuccessMessage() {
		return creditMemo.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().customerCreditNote(Global.get().customer()))
				: getMessages().updateSuccessfully(
						getMessages().customerCreditNote(
								Global.get().customer()));

	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();
		creditMemo.setType(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);
		Customer customer = get(CUSTOMER).getValue();
		creditMemo.setCustomer(customer.getID());

		ClientFinanceDate date = get(DATE).getValue();
		creditMemo.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		creditMemo.setNumber(number);

		Contact contact = get(CONTACT).getValue();
		creditMemo.setContact(toClientContact(contact));

		ClientAddress billTo = get(BILL_TO).getValue();
		creditMemo.setBillingAddress(billTo);

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}
		items.addAll(accounts);
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		creditMemo.setTransactionItems(items);
		creditMemo.setCurrency(customer.getCurrency().getID());
		double taxTotal = updateTotals(context, creditMemo, true);
		creditMemo.setTaxTotal(taxTotal);
		create(creditMemo, context);
		return null;
	}

	@Override
	protected Currency getCurrency() {
		return ((Customer) CreateCreditNoteCommand.this.get(CUSTOMER)
				.getValue()).getCurrency();
	}

}
