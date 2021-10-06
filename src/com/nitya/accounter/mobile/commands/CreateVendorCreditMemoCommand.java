package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.NumberUtils;
import com.nitya.accounter.core.Payee;
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
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.requirements.NumberRequirement;
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
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ClientVendorCreditMemo;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.exception.AccounterException;

public class CreateVendorCreditMemoCommand extends AbstractTransactionCommand {
	ClientVendorCreditMemo vendorCreditMemo;

	@Override
	protected String getWelcomeMessage() {
		return getMessages().create(getMessages().vendorCreditMemo());
	}

	@Override
	protected String getDetailsMessage() {
		List<?> list = get(ITEMS).getValue();
		List<?> list2 = get(ACCOUNTS).getValue();
				
		return list.size() != 0||list2.size() != 0? getMessages().readyToCreate(getMessages().vendorCreditMemo()):null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_VENDOR_CREDIT_MEMO,
						context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		get(DISCOUNT).setDefaultValue(0.0);

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(
				getMessages().vendorCreditMemo());
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
							CreateVendorCreditMemoCommand.this.get(
									CURRENCY_FACTOR).setValue(
									mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}
					}
				})

		{

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
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
			}
		});

		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected Currency getCurrency() {
				Vendor vendor = (Vendor) CreateVendorCreditMemoCommand.this
						.get(VENDOR).getValue();
				return vendor.getCurrency();
			}

		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().creditNoteNo()), getMessages().creditNoteNo(),
				true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

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
				.pleaseSelect(getMessages().Accounts()), getMessages()
				.Account(), true, true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getIsActive()
									&& (e.getType() == Account.TYPE_COST_OF_GOODS_SOLD
											|| e.getType() == Account.TYPE_FIXED_ASSET
											|| e.getType() == Account.TYPE_EXPENSE || e
											.getType() == Account.TYPE_OTHER_EXPENSE)) {
								return true;
							}
							return false;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			public boolean isSales() {
				return false;
			}

			@Override
			protected Currency getCurrency() {
				return ((Vendor) CreateVendorCreditMemoCommand.this.get(VENDOR)
						.getValue()).getCurrency();
			}

			@Override
			protected boolean isTrackTaxPaidAccount() {
				return false;
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateVendorCreditMemoCommand.this
						.get(DISCOUNT).getValue();
				return value2;
			}

		});
		list.add(new TransactionItemTableRequirement(ITEMS,
				"Please Enter Item Name or number", getMessages().items(),
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
				return ((Vendor) CreateVendorCreditMemoCommand.this.get(VENDOR)
						.getValue()).getCurrency();
			}

			@Override
			protected double getCurrencyFactor() {
				return CreateVendorCreditMemoCommand.this.getCurrencyFactor();
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateVendorCreditMemoCommand.this
						.get(DISCOUNT).getValue();
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
		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()
						&& getPreferences().isTrackPaidTax()
						&& !context.getPreferences().isTaxPerDetailLine()) {
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
				if (preferences.isTrackTax()
						&& getPreferences().isTrackPaidTax()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().includeVATwithAmountenabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().includeVATwithAmountDisabled();
			}
		});

		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}
		vendorCreditMemo = new ClientVendorCreditMemo();

		ClientFinanceDate date = get(DATE).getValue();
		vendorCreditMemo.setDate(date.getDate());
		String number = get(NUMBER).getValue();
		vendorCreditMemo.setNumber(number);

		items.addAll(accounts);

		vendorCreditMemo.setTransactionItems(items);

		Contact contact = get(CONTACT).getValue();
		if (contact != null && contact.getName() != null) {
			vendorCreditMemo.setContact(toClientContact(contact));
		}
		String phone = get(PHONE).getValue();
		vendorCreditMemo.setPhone(phone);
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && getPreferences().isTrackPaidTax()
				&& !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}

		Vendor supplier = get(VENDOR).getValue();
		vendorCreditMemo.setVendor(supplier.getID());
		String memo = get(MEMO).getValue();
		vendorCreditMemo.setMemo(memo);
		vendorCreditMemo.setCurrency(supplier.getCurrency().getID());
		vendorCreditMemo.setCurrencyFactor((Double) get(CURRENCY_FACTOR)
				.getValue());
		updateTotals(context, vendorCreditMemo, false);
		create(vendorCreditMemo, context);
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (!context.getPreferences().isKeepTrackofBills()) {
			addFirstMessage(context, getMessages()
					.youDntHavePermissionToDoThis());
			return "cancel";
		}
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().vendorCreditMemo()));
				return "billsList";
			}
			vendorCreditMemo = getTransaction(string,
					AccounterCoreType.VENDORCREDITMEMO, context);

			if (vendorCreditMemo == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().vendorCreditMemo()));
				return "billsList " + string;
			}
			setValues(context);
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			vendorCreditMemo = new ClientVendorCreditMemo();
		}
		setTransaction(vendorCreditMemo);
		return null;

	}

	private void setValues(Context context) {
		get(VENDOR).setValue(
				CommandUtils.getServerObjectById(vendorCreditMemo.getVendor(),
						AccounterCoreType.VENDOR));
		get(CURRENCY_FACTOR).setValue(vendorCreditMemo.getCurrencyFactor());
		get(DATE).setValue(vendorCreditMemo.getDate());
		get(NUMBER).setValue(vendorCreditMemo.getNumber());
		get(CONTACT).setValue(toServerContact(vendorCreditMemo.getContact()));
		get(PHONE).setValue(vendorCreditMemo.getPhone());

		ArrayList<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		ArrayList<ClientTransactionItem> accountsList = new ArrayList<ClientTransactionItem>();
		if (vendorCreditMemo.getTransactionItems() != null
				&& !vendorCreditMemo.getTransactionItems().isEmpty()) {
			for (ClientTransactionItem item : vendorCreditMemo
					.getTransactionItems()) {
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
							vendorCreditMemo.getTransactionItems(), context));
		}
		if (getPreferences().isTrackDiscounts()
				&& !getPreferences().isDiscountPerDetailLine()) {
			get(DISCOUNT).setValue(
					getDiscountFromTransactionItems(vendorCreditMemo
							.getTransactionItems()));
		}
		get(MEMO).setValue(vendorCreditMemo.getMemo());
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
	protected Currency getCurrency() {
		return ((Vendor) CreateVendorCreditMemoCommand.this.get(VENDOR)
				.getValue()).getCurrency();
	}

}