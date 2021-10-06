package com.nitya.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public abstract class TransactionAccountTableRequirement extends
		AbstractTableRequirement<ClientTransactionItem> {

	private static final String ACCOUNT = "accountitemaccount";
	private static final String AMOUNT = "accountitemamount";
	private static final String TAXCODE = "accountitemtaxCode";
	private static final String DISCOUNT = "accountitemdiscount";
	private static final String DESCRIPTION = "accountitemdescription";
	private static final String TAX = "accountitemtax";
	protected static final String IS_BILLABLE = "isBillable";
	protected static final String ACCOUNT_CUSTOMER = "accountcustomer";

	public TransactionAccountTableRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, true, isOptional,
				isAllowFromContext);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new AccountRequirement(ACCOUNT, getMessages().pleaseSelect(
				getMessages().account()), getMessages().account(), false, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().Account());
			}

			@Override
			protected List<Account> getLists(Context context) {
				return getAccounts(context);
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Accounts());
			}
		});

		list.add(new CurrencyAmountRequirement(AMOUNT, getMessages()
				.pleaseEnter(getMessages().amount()), getMessages().amount(),
				false, true) {

			@Override
			protected Currency getCurrency() {
				return TransactionAccountTableRequirement.this.getCurrency();
			}
		});

		list.add(new AmountRequirement(DISCOUNT, getMessages().pleaseEnter(
				getMessages().discount()), getMessages().discount(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackDiscounts()
						&& getPreferences().isDiscountPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& (isSales() ? true : getPreferences()
								.isTrackPaidTax())
						&& getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				Set<TAXCode> taxCodes = context.getCompany().getTaxCodes();
				List<TAXCode> clientcodes = new ArrayList<TAXCode>();
				for (TAXCode taxCode : taxCodes) {
					if (taxCode.isActive()) {
						clientcodes.add(taxCode);
					}
				}
				return clientcodes;
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});
		list.add(new BooleanRequirement(TAX, true) {

			@Override
			protected String getTrueString() {
				return getMessages().taxable();
			}

			@Override
			protected String getFalseString() {
				return getMessages().taxExempt();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& (isSales() ? true : getPreferences()
								.isTrackPaidTax())
						&& !getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new StringRequirement(DESCRIPTION, getMessages().pleaseEnter(
				getMessages().description()), getMessages().description(),
				true, true));
	}

	public abstract boolean isSales();

	protected List<Account> getAccounts(Context context) {
		List<Account> listAccounts = new ArrayList<Account>();
		Set<Account> accounts = context.getCompany().getAccounts();
		for (Account account : accounts) {
			if (account.getIsActive()) {
				listAccounts.add(account);

			}
		}
		return listAccounts;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getMessages().accountsSelected());
	}

	@Override
	protected void getRequirementsValues(ClientTransactionItem obj) {
		Account account = get(ACCOUNT).getValue();
		obj.setAccount(account.getID());
		String description = get(DESCRIPTION).getValue();
		obj.setDescription(description);
		double amount = get(AMOUNT).getValue();
		obj.setUnitPrice(amount);
		Boolean isTaxable = get(TAX).getValue();
		if (getPreferences().isTrackTax() && isTrackTaxPaidAccount() ? false
				: getPreferences().isTrackPaidTax())
			obj.setTaxable(isTaxable);
		TAXCode taxCode = get(TAXCODE).getValue();
		if (taxCode != null) {
			obj.setTaxCode(taxCode.getID());
		}
		if (get(IS_BILLABLE) != null) {
			Boolean isBillable = get(IS_BILLABLE).getValue();
			obj.setIsBillable(isBillable);
			Customer customer = get(ACCOUNT_CUSTOMER).getValue();
			if (customer != null) {
				obj.setCustomer(customer.getID());
			}
		}
		double discount = get(DISCOUNT).getValue();
		obj.setDiscount(discount);
		double lt = obj.getUnitPrice();
		if (getPreferences().isTrackDiscounts()
				&& !getPreferences().isDiscountPerDetailLine()) {
			obj.setDiscount(getDiscount());
		}
		double disc = obj.getDiscount();
		obj.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt * disc / 100))
				: lt);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionItem obj) {
		get(ACCOUNT).setValue(
				CommandUtils.getServerObjectById(obj.getAccount(),
						AccounterCoreType.ACCOUNT));
		get(DESCRIPTION).setDefaultValue(obj.getDescription());
		get(AMOUNT).setValue(obj.getUnitPrice());
		get(TAXCODE).setValue(
				CommandUtils.getServerObjectById(obj.getTaxCode(),
						AccounterCoreType.TAX_CODE));
		get(TAX).setDefaultValue(obj.isTaxable());
		get(DISCOUNT).setDefaultValue(obj.getDiscount());
		if (get(IS_BILLABLE) != null) {
			get(IS_BILLABLE).setDefaultValue(obj.isBillable());
			get(ACCOUNT_CUSTOMER).setValue(
					CommandUtils.getServerObjectById(obj.getCustomer(),
							AccounterCoreType.CUSTOMER));
		}
	}

	@Override
	protected ClientTransactionItem getNewObject() {
		ClientTransactionItem clientTransactionItem = new ClientTransactionItem();
		clientTransactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
		clientTransactionItem.setTaxable(true);
		clientTransactionItem.setIsBillable(false);
		ClientQuantity clientQuantity = new ClientQuantity();
		clientQuantity.setValue(1.0);
		clientTransactionItem.setQuantity(clientQuantity);
		clientTransactionItem.setDiscount(0.0);
		clientTransactionItem.setLineTotal(0d);
		clientTransactionItem.setUnitPrice(0.0);
		return clientTransactionItem;
	}

	@Override
	protected Record createFullRecord(ClientTransactionItem t) {
		Record record = new Record(t);
		ClientAccount clientObjectById = (ClientAccount) CommandUtils
				.getClientObjectById(t.getAccount(), AccounterCoreType.ACCOUNT,
						getCompanyId());
		record.add(getMessages().name(), clientObjectById == null ? ""
				: clientObjectById.getDisplayName());
		String formalName;
		if (getPreferences().isEnableMultiCurrency()) {
			formalName = getCurrency().getFormalName();
		} else {
			formalName = getPreferences().getPrimaryCurrency().getFormalName();
		}
		record.add(getMessages().unitPrice() + "(" + formalName + ")",
				t.getUnitPrice());
		if (getPreferences().isTrackTax() && isTrackTaxPaidAccount() ? false
				: getPreferences().isTrackPaidTax()) {
			if (getPreferences().isTaxPerDetailLine()) {
				ClientTAXCode taxCode = (ClientTAXCode) CommandUtils
						.getClientObjectById(t.getTaxCode(),
								AccounterCoreType.TAX_CODE, getCompanyId());
				record.add(getMessages().taxCode(), taxCode == null ? ""
						: taxCode.getDisplayName());
			} else {
				if (t.isTaxable()) {
					record.add(getMessages().taxable());
				} else {
					record.add(getMessages().taxExempt());
				}
			}
		}
		if (getPreferences().isTrackDiscounts()
				&& getPreferences().isDiscountPerDetailLine()) {
			record.add(getMessages().discount(), t.getDiscount());
		}
		record.add(getMessages().description(), t.getDescription());
		return record;
	}

	@Override
	protected List<ClientTransactionItem> getList() {
		return null;
	}

	@Override
	protected Record createRecord(ClientTransactionItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<ClientTransactionItem> items = getValue();
		return items.isEmpty() ? getMessages().addOf(getMessages().Accounts())
				: getMessages().addMore(getMessages().Accounts());
	}

	@Override
	protected boolean contains(List<ClientTransactionItem> oldValues,
			ClientTransactionItem t) {
		for (ClientTransactionItem clientTransactionItem : oldValues) {
			if (clientTransactionItem.getAccount() != 0
					&& clientTransactionItem.getAccount() == t.getAccount()) {
				return true;
			}
		}
		return false;
	}

	protected abstract boolean isTrackTaxPaidAccount();

	// protected abstract Payee getPayee();

	protected abstract Currency getCurrency();

	protected abstract double getDiscount();

	@Override
	public <T> T getValue() {
		List<ClientTransactionItem> oldValues = super.getValue();
		for (ClientTransactionItem obj : oldValues) {
			double lt = obj.getUnitPrice();
			if (getPreferences().isTrackDiscounts()
					&& !getPreferences().isDiscountPerDetailLine()) {
				obj.setDiscount(getDiscount());
			}
			double disc = obj.getDiscount();
			obj.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
					* disc / 100)) : lt);
		}
		return (T) oldValues;
	}
}
