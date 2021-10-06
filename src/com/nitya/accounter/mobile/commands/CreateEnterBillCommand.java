package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.NumberUtils;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.core.PaymentTerms;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.AmountRequirement;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.ChangeListner;
import com.nitya.accounter.mobile.requirements.ContactRequirement;
import com.nitya.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.nitya.accounter.mobile.requirements.CustomerRequirement;
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.requirements.NumberRequirement;
import com.nitya.accounter.mobile.requirements.PaymentTermRequirement;
import com.nitya.accounter.mobile.requirements.PhoneRequirement;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.mobile.requirements.TaxCodeRequirement;
import com.nitya.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.nitya.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.nitya.accounter.mobile.requirements.VendorRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientEnterBill;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CreateEnterBillCommand extends AbstractTransactionCommand {

	private ClientEnterBill enterBill;

	@Override
	protected String getWelcomeMessage() {

		return getMessages().create(getMessages().enterBill());
	}

	@Override
	protected String getDetailsMessage() {
		List<?> list = get(ITEMS).getValue();
		List<?> list2 = get(ACCOUNTS).getValue();
				
		return list.size() != 0||list2.size() != 0?getMessages().readyToCreate(getMessages().enterBill()):null;
	}

	@Override
	protected void setDefaultValues(Context context) {

		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER)
				.setDefaultValue(
						NumberUtils.getNextTransactionNumber(
								ClientTransaction.TYPE_ENTER_BILL,
								context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(CONTACT).setDefaultValue(null);
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		for (PaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}

		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
		get(DELIVERY_DATE).setDefaultValue(new ClientFinanceDate());

		Vendor v = (Vendor) get(VENDOR).getValue();
		if (v != null) {
			Set<Contact> contacts2 = v.getContacts();
			if (contacts2 != null)
				for (Contact c : contacts2) {
					get(CONTACT).setDefaultValue(c);
				}
		}
		get(MEMO).setDefaultValue("");

		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		get(DISCOUNT).setDefaultValue(0.0);

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().enterBill());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getMessages().Vendor()), getMessages().Vendor(), false, true,
				new ChangeListner<Vendor>() {

					@Override
					public void onSelection(Vendor value) {
						get(PHONE).setValue(value.getPhoneNo());
						get(CONTACT).setValue(null);
						for (Contact contact : value.getContacts()) {
							if (contact.isPrimary()) {
								get(CONTACT).setValue(contact);
								break;
							}
						}
						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							CreateEnterBillCommand.this
									.get(CURRENCY_FACTOR)
									.setValue(
											mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}
					}
				}) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return getVendors();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Vendor());
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected Currency getCurrency() {
				Vendor vendor = (Vendor) CreateEnterBillCommand.this
						.get(VENDOR).getValue();
				return vendor.getCurrency();
			}

		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().billNo()), getMessages().billNo(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

		list.add(new DateRequirement(DUE_DATE, getMessages().pleaseEnter(
				getMessages().dueDate()), getMessages().dueDate(), true, true));

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getMessages().deliveryDate()), getMessages().deliveryDate(),
				true, true));

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseSelect(getMessages().paymentTerm()), getMessages()
				.paymentTerms(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
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

		list.add(new TransactionAccountTableRequirement(ACCOUNTS, getMessages()
				.pleaseEnter(getMessages().Account()), getMessages().Account(),
				true, true) {
			@Override
			protected void addRequirement(List<Requirement> list) {
				super.addRequirement(list);
				list.add(new CustomerRequirement(ACCOUNT_CUSTOMER,
						getMessages().pleaseSelect(Global.get().Customer()),
						Global.get().Customer(), true, true, null) {
					@Override
					public Result run(Context context, Result makeResult,
							ResultList list, ResultList actions) {
						if (getPreferences()
								.isBillableExpsesEnbldForProductandServices()
								&& getPreferences()
										.isProductandSerivesTrackingByCustomerEnabled()) {
							return super
									.run(context, makeResult, list, actions);
						}
						return null;
					}

					@Override
					protected List<Customer> getLists(Context context) {
						return CreateEnterBillCommand.this.getCustomers();
					}
				});

				list.add(new BooleanRequirement(IS_BILLABLE, true) {
					@Override
					public Result run(Context context, Result makeResult,
							ResultList list, ResultList actions) {
						if (getPreferences()
								.isBillableExpsesEnbldForProductandServices()
								&& getPreferences()
										.isProductandSerivesTrackingByCustomerEnabled()) {
							return super
									.run(context, makeResult, list, actions);
						}
						return null;
					}

					@Override
					protected String getTrueString() {
						return getMessages().billabe();
					}

					@Override
					protected String getFalseString() {
						return "Not Billable";
					}
				});
			}

			@Override
			protected List<Account> getAccounts(Context context) {
				return com.nitya.accounter.web.client.core.Utility
						.filteredList(new ListFilter<Account>() {

							@Override
							public boolean filter(Account account) {
								if (account.getType() != Account.TYPE_CASH
										&& account.getType() != Account.TYPE_BANK
										&& account.getType() != Account.TYPE_CREDIT_CARD
										&& account.getType() != Account.TYPE_INVENTORY_ASSET
										&& account.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
										&& account.getType() != Account.TYPE_ACCOUNT_PAYABLE
										&& account.getType() != Account.TYPE_INCOME
										&& account.getType() != Account.TYPE_OTHER_INCOME
										&& account.getType() != Account.TYPE_OTHER_CURRENT_ASSET
										&& account.getType() != Account.TYPE_OTHER_CURRENT_LIABILITY
										&& account.getType() != Account.TYPE_OTHER_ASSET
										&& account.getType() != Account.TYPE_EQUITY
										&& account.getType() != Account.TYPE_LONG_TERM_LIABILITY) {
									return true;
								} else {
									return false;
								}
							}
						}, new ArrayList<Account>(context.getCompany()
								.getAccounts()));
			}

			@Override
			public boolean isSales() {
				return false;
			}

			@Override
			protected Currency getCurrency() {
				return ((Vendor) CreateEnterBillCommand.this.get(VENDOR)
						.getValue()).getCurrency();
			}

			@Override
			protected boolean isTrackTaxPaidAccount() {
				return false;
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateEnterBillCommand.this.get(DISCOUNT)
						.getValue();
				return value2;
			}

		});

		list.add(new TransactionItemTableRequirement(ITEMS, getMessages()
				.pleaseEnter(getMessages().itemName()), getMessages().items(),
				true, true) {

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.isIBuyThisItem()) {
						if (item.isActive())
							items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return false;
			}

			@Override
			protected Currency getCurrency() {
				return ((Vendor) CreateEnterBillCommand.this.get(VENDOR)
						.getValue()).getCurrency();
			}

			@Override
			protected double getCurrencyFactor() {
				return CreateEnterBillCommand.this.getCurrencyFactor();
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateEnterBillCommand.this.get(DISCOUNT)
						.getValue();
				return value2;
			}

		});

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getMessages().contactName()), "Contact", true, true, null) {

			@Override
			protected Payee getPayee() {
				return get(VENDOR).getValue();
			}
		});

		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& getPreferences().isTrackPaidTax()
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

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax() && preferences.isTrackPaidTax()) {
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
	public void beforeFinishing(Context context, Result makeResult) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			addFirstMessage(context, getMessages()
					.transactionitemtotalcannotbe0orlessthan0());
		}
		super.beforeFinishing(context, makeResult);
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}

		Vendor vendor = (Vendor) get(VENDOR).getValue();
		enterBill.setVendor(vendor.getID());
		ClientFinanceDate date = get(DATE).getValue();
		if (date != null) {
			enterBill.setDate(date.getDate());
		} else {
			enterBill.setDate(System.currentTimeMillis());
		}
		String number = get(NUMBER).getValue();
		enterBill.setNumber(number);
		enterBill.setType(ClientTransaction.TYPE_ENTER_BILL);

		items.addAll(accounts);

		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && getPreferences().isTrackPaidTax()
				&& !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}

		enterBill.setTransactionItems(items);
		updateTotals(context, enterBill, false);

		ClientFinanceDate dueDate = get(DUE_DATE).getValue();
		enterBill.setDueDate(dueDate.getDate());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		enterBill.setDeliveryDate(deliveryDate);

		Contact contact = get(CONTACT).getValue();
		enterBill.setContact(toClientContact(contact));

		PaymentTerms paymentTerm = get("paymentTerms").getValue();
		enterBill.setPaymentTerm(paymentTerm.getID());

		String phone = get(PHONE).getValue();
		enterBill.setPhone(phone);
		enterBill.setCurrency(vendor.getCurrency().getID());
		enterBill.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		if (enterBill.getID() == 0) {
			enterBill.setDiscountDate(enterBill.getDate().getDate());
		}
		create(enterBill, context);
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().enterBill()));
				return "billsList";
			}
			enterBill = getTransaction(string, AccounterCoreType.ENTERBILL,
					context);

			if (enterBill == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().enterBill()));
				return "billsList " + string;
			}
			setValues(context);
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			enterBill = new ClientEnterBill();
		}
		setTransaction(enterBill);
		return null;
	}

	private void setValues(Context context) {
		get(VENDOR).setValue(
				CommandUtils.getServerObjectById(enterBill.getVendor(),
						AccounterCoreType.VENDOR));
		get(CURRENCY_FACTOR).setValue(enterBill.getCurrencyFactor());
		get(DATE).setValue(enterBill.getDate());
		get(NUMBER).setValue(enterBill.getNumber());
		get(CONTACT).setValue(toServerContact(enterBill.getContact()));
		get(PHONE).setValue(enterBill.getPhone());
		get(PAYMENT_TERMS).setValue(
				CommandUtils.getServerObjectById(enterBill.getPaymentTerm(),
						AccounterCoreType.PAYMENT_TERM));
		get(DUE_DATE).setValue(new ClientFinanceDate(enterBill.getDueDate()));
		get(DELIVERY_DATE).setValue(
				new ClientFinanceDate(enterBill.getDeliveryDate()));
		ArrayList<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		ArrayList<ClientTransactionItem> accountsList = new ArrayList<ClientTransactionItem>();
		if (enterBill.getTransactionItems() != null
				&& !enterBill.getTransactionItems().isEmpty()) {
			for (ClientTransactionItem item : enterBill.getTransactionItems()) {
				if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
					accountsList.add(item);
				} else {
					itemsList.add(item);
				}
			}
		}
		get(ACCOUNTS).setValue(accountsList);
		get(ITEMS).setValue(itemsList);
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && getPreferences().isTrackPaidTax()
				&& !preferences.isTaxPerDetailLine()) {
			get(TAXCODE).setValue(
					getTaxCodeForTransactionItems(
							enterBill.getTransactionItems(), context));
		}
		if (getPreferences().isTrackDiscounts()
				&& !getPreferences().isDiscountPerDetailLine()) {
			get(DISCOUNT).setValue(
					getDiscountFromTransactionItems(enterBill
							.getTransactionItems()));
		}
		get(MEMO).setValue(enterBill.getMemo());
	}

	@Override
	protected Currency getCurrency() {
		return ((Vendor) CreateEnterBillCommand.this.get(VENDOR).getValue())
				.getCurrency();
	}

}
