package com.vimukti.accounter.core;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * @author Suresh
 * @since 1.0.0
 * 
 *        Account is one of the Basic object in Finance with which we can keep
 *        track the money transfer with different type of Transactions with our
 *        Customers or Suppliers. It represents how the money is coming in or
 *        going out in our Business. We have several type of Accounts to manage
 *        our accounting software and we have some System Accounts (Opening
 *        Balance, Accounts Receivable, Accounts Payable, Un Deposited Funds).
 *        The existence of these System Accounts can not be avoidable. Each
 *        Account Object has its own values for opening balance, current balance
 *        and total balance properties. opening balance property hold the
 *        initial amount given for the account while creating. Current Balance
 *        can hold the amount which is used by any transaction. Total balance is
 *        the sum of it's current balance and it's all childs Total Balances.
 * 
 * 
 */
@SuppressWarnings({ "serial", "unchecked" })
public class Account extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	Logger log = Logger.getLogger(Account.class);

	// public static final int BASETYPE_INCOME = 1;
	// public static final int BASETYPE_EXPENSE = 2;
	// public static final int BASETYPE_ASSET = 3;
	// public static final int BASETYPE_LIABILITY = 4;
	// public static final int BASETYPE_EQUITY = 5;
	/**
	 * Any account must belong from any of these type
	 */
	public static final int TYPE_CASH = 1;
	public static final int TYPE_BANK = 2;
	public static final int TYPE_ACCOUNT_RECEIVABLE = 3;// For UK Sales Ledger
	public static final int TYPE_OTHER_CURRENT_ASSET = 4;
	public static final int TYPE_INVENTORY_ASSET = 5;// For UK Stock Asset
	public static final int TYPE_FIXED_ASSET = 6;
	public static final int TYPE_OTHER_ASSET = 7;
	public static final int TYPE_ACCOUNT_PAYABLE = 8;// For UK Purchase Ledger
	public static final int TYPE_OTHER_CURRENT_LIABILITY = 9;
	public static final int TYPE_CREDIT_CARD = 10;
	public static final int TYPE_PAYROLL_LIABILITY = 11;
	public static final int TYPE_LONG_TERM_LIABILITY = 12;
	public static final int TYPE_EQUITY = 13;
	public static final int TYPE_INCOME = 14;
	public static final int TYPE_COST_OF_GOODS_SOLD = 15;
	public static final int TYPE_EXPENSE = 16;
	public static final int TYPE_OTHER_INCOME = 17;
	public static final int TYPE_OTHER_EXPENSE = 18;
	public static final int TYPE_LIABILITY = 19;
	public static final int TYPE_ASSET = 20;
	public static final int TYPE_PAYPAL = 21;

	/**
	 * Types of cash flow
	 */
	public static final int CASH_FLOW_CATEGORY_FINANCING = 1;
	public static final int CASH_FLOW_CATEGORY_INVESTING = 2;
	public static final int CASH_FLOW_CATEGORY_OPERATING = 3;

	/**
	 * Types of bank
	 */
	public static final int BANK_ACCCOUNT_TYPE_NONE = 0;
	public static final int BANK_ACCCOUNT_TYPE_CHECKING = 1;
	public static final int BANK_ACCCOUNT_TYPE_SAVING = 2;
	public static final int BANK_ACCCOUNT_TYPE_MONEY_MARKET = 3;

	public static final int BASETYPE_ASSET = 1;
	public static final int BASETYPE_LIABILITY = 2;
	public static final int BASETYPE_EQUITY = 3;

	public static final int BASETYPE_ORDINARY_INCOME_OR_EXPENSE = 4;
	public static final int BASETYPE_OTHER_INCOME_OR_EXPENSE = 5;

	public static final int SUBBASETYPE_CURRENT_ASSET = 1;
	public static final int SUBBASETYPE_FIXED_ASSET = 2;
	public static final int SUBBASETYPE_OTHER_ASSET = 3;
	public static final int SUBBASETYPE_CURRENT_LIABILITY = 4;
	public static final int SUBBASETYPE_LONG_TERM_LIABILITY = 5;
	public static final int SUBBASETYPE_EQUITY = 6;
	public static final int SUBBASETYPE_INCOME = 7;
	public static final int SUBBASETYPE_COST_OF_GOODS_SOLD = 8;
	public static final int SUBBASETYPE_EXPENSE = 9;

	public static final int SUBBASETYPE_OTHER_EXPENSE = 10;

	public static final int GROUPTYPE_CASH = 1;

	/**
	 * To decide about the type of the {@link Account}
	 */
	int type;

	/**
	 * This is to categorise again the existing basic Accounting types for the
	 * purpose of Reports.
	 */
	int baseType;

	/**
	 * This is to categorise again the existing Accounting Base Types for the
	 * purpose of Reports.
	 */
	int subBaseType;

	/**
	 * This is to categorise some of the Accounts to particular group for the
	 * purpose of Reports.
	 */
	int groupType;

	/**
	 * This is to store Account nominal code.
	 */
	String number;

	/**
	 * This is to store Account name.
	 */
	String name;

	/**
	 * This is to make an Account active or in active so that we can avoid all
	 * non interested Accounts in Finance when ever we want.
	 */
	boolean isActive;

	/**
	 * This is to hold the parent of the currently created Account. The value
	 * for this property is null if the currently created Account has no parent.
	 */
	Account parent;

	/**
	 * This property is to hold the cashFlowCategory of an Account. It will be
	 * useful for Cash Flow Category Report.
	 */
	int cashFlowCategory;

	/**
	 * This property is to hold the balance of the Account at the moment of
	 * creation.
	 */
	double openingBalance = 0.0D;

	/**
	 * This will hold the FinanceDate up to which the opening balance of this
	 * Account is.
	 */
	FinanceDate asOf;

	// OneZeroNineNineCategory oneZeroNineNineCategory;

	boolean isConsiderAsCashAccount;

	String comment;

	// Bank bank;// for Bank name
	// int bankAccountType;
	// String bankAccountNumber;

	/**
	 * saves the paypal token and secret key for the paypal account if the user
	 * has done synchronization. or else it will be null
	 */
	private String paypalToken;

	private String paypalSecretkey;

	private FinanceDate endDate;

	/**
	 * For Credit Card Account
	 */
	double creditLimit;
	String cardOrLoanNumber;

	/**
	 * this will decide the behaviour of the account whether the amount of this
	 * account should decrease or increase depends on the Transaction.
	 */
	boolean isIncrease;

	/**
	 * To keep track of the currently updated balance of an Account in any
	 * Transaction.
	 */
	double currentBalance = 0.0D;

	/**
	 * This will hold it's total balance and sum of all it's child Accounts.
	 */
	double totalBalance = 0.0D;

	/**
	 * This will decide whether an Account's opening balance can be editable or
	 * not
	 */
	boolean isOpeningBalanceEditable = Boolean.TRUE;

	/**
	 * This will represent the whole path of the Account (recursively it's name
	 * preceded with it's parent name)
	 */
	String hierarchy;

	/**
	 * This will hold a set of {@link Budget}
	 */
	Set<Budget> budget = new HashSet<Budget>();

	/**
	 * This is not Persistent variable, logically we have used in our code.
	 */
	// transient AccountTransaction accountTransaction;

	/**
	 * This is not Persistent variable, logically we have used in our code.
	 */
	transient Account oldParent;

	/**
	 * This will index all Accounts.
	 */
	String flow;

	Account linkedAccumulatedDepreciationAccount;

	boolean isDefault;

	Map<Integer, Double> monthViceAmounts = new HashMap();

	int boxNumber;

	private String lastCheckNum;

	private Currency currency;

	/**
	 * Used in OpeningBalance Only
	 */
	private double currencyFactor = 1;

	private double totalBalanceInAccountCurrency;

	private String paypalEmail;

	transient private FinanceDate previousAsOfDate;

	transient private double previousCurrencyFactor = 1;

	transient private double previousOpeningBalance;

	private double statementBalance;

	private FinanceDate statementLastDate;

	/**
	 * Constructor of Account class
	 */
	public Account() {

	}

	/**
	 * Creates new Instance Used to Create Default Accounts
	 * 
	 * @param type
	 * @param number
	 * @param name
	 * @param cashFlowCategory
	 * @param isConsiderAsCashAccount
	 * @param isOpeningBalanceEditable
	 * 
	 */
	public Account(int type, String number, String name, int cashFlowCategory,
			boolean isDefault) {
		this.type = type;
		this.number = number;
		this.name = name;
		this.isActive = true;
		this.cashFlowCategory = cashFlowCategory;
		this.isOpeningBalanceEditable = true;
		this.flow = String.valueOf(number);
		this.isDefault = isDefault;
	}

	/**
	 * Creates new Instance Used to Create Default Accounts
	 * 
	 * @param type
	 * @param number
	 * @param name
	 * @param cashFlowCategory
	 * @param isConsiderAsCashAccount
	 * @param isOpeningBalanceEditable
	 * 
	 */
	public Account(int type, String number, String name, int cashFlowCategory) {
		this(type, number, name, cashFlowCategory, false);
	}

	/**
	 * Constructor of Account class
	 * 
	 * @param session
	 * @param clientaccount
	 */
	public Account(Session session, ClientAccount clientaccount) {
		int type = clientaccount.getType();
		if (type == Account.TYPE_INCOME || type == Account.TYPE_OTHER_INCOME
				|| type == Account.TYPE_CREDIT_CARD
				|| type == Account.TYPE_PAYROLL_LIABILITY
				|| type == Account.TYPE_OTHER_CURRENT_LIABILITY
				|| type == Account.TYPE_LONG_TERM_LIABILITY
				|| type == Account.TYPE_EQUITY
				|| type == Account.TYPE_ACCOUNT_PAYABLE) {
			clientaccount.setIncrease(Boolean.TRUE);
		} else {
			clientaccount.setIncrease(Boolean.FALSE);
		}

	}

	/**
	 * 
	 * Constructor of Account class
	 * 
	 * @param type
	 * 
	 * @param number
	 * 
	 * @param name
	 * 
	 * @param parent
	 * 
	 * @param openingBalance
	 * 
	 * @param comment
	 * 
	 * @param asOf
	 * 
	 * @return
	 */

	public Account(int type, String number, String name, Account parent,
			String comment, FinanceDate asOf) {
		this.type = type;
		this.number = number;
		this.name = name;
		this.parent = parent;
		this.comment = comment;
		this.asOf = asOf;
	}

	public Map<Integer, Double> getMonthViceAmounts() {
		return monthViceAmounts;
	}

	public void setMonthViceAmounts(Map<Integer, Double> monthViceAmounts) {
		this.monthViceAmounts = monthViceAmounts;
	}

	/**
	 * @return the budget
	 */
	public Set<Budget> getBudget() {
		return budget;
	}

	/**
	 * @param budget
	 *            the budget to set
	 */
	public void setBudget(Set<Budget> budget) {
		this.budget = budget;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	// public void setID(long id){
	// this.id = id;
	// }
	//
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	public Account getLinkedAccumulatedDepreciationAccount() {
		return linkedAccumulatedDepreciationAccount;
	}

	public void setLinkedAccumulatedDepreciationAccount(
			Account linkedAccumulatedDepreciationAccount) {
		this.linkedAccumulatedDepreciationAccount = linkedAccumulatedDepreciationAccount;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public int getBaseType() {
		return baseType;
	}

	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}

	public int getSubBaseType() {
		return subBaseType;
	}

	public void setSubBaseType(int subBaseType) {
		this.subBaseType = subBaseType;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	// public void setType(int type) {
	// this.type = type;
	// }
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	// public void setNumber(String number) {
	// this.number = number;
	// }
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	// public void setName(String name) {
	// this.name = name;
	// }
	/**
	 * @return the isActive
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the parent
	 */
	public Account getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Account parent) {
		this.parent = parent;
	}

	/**
	 * @return the cashFlowCategory
	 */
	public int getCashFlowCategory() {
		return cashFlowCategory;
	}

	/**
	 * @param cashFlowCategory
	 *            the cashFlowCategory to set
	 */
	// public void setCashFlowCategory(int cashFlowCategory) {
	// this.cashFlowCategory = cashFlowCategory;
	// }
	/**
	 * @return the openingBalance
	 */
	public double getOpeningBalance() {
		return openingBalance;
	}

	/**
	 * @param openingBalance
	 *            the openingBalance to set
	 */
	// public void setOpeningBalance(double openingBalance) {
	// this.openingBalance = openingBalance;
	//
	// }
	/**
	 * @return the asOf
	 */
	public FinanceDate getAsOf() {
		return asOf;
	}

	/**
	 * @param asOf
	 *            the asOf to set
	 */
	// public void setAsOf(Date asOf) {
	// this.asOf = asOf;
	// }
	/**
	 * @return the isConsiderAsCashAccount
	 */
	public boolean isConsiderAsCashAccount() {
		return isConsiderAsCashAccount;
	}

	/**
	 * @param isConsiderAsCashAccount
	 *            the isConsiderAsCashAccount to set
	 */
	public void setConsiderAsCashAccount(boolean isConsiderAsCashAccount) {
		this.isConsiderAsCashAccount = isConsiderAsCashAccount;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	// public void setComment(String comment) {
	// this.comment = comment;
	// }
	/**
	 * @return the bankName
	 */
	// public Bank getBank() {
	// return bank;
	// }

	/**
	 * @param bankName
	 *            the bankName to set
	 */
	// public void setBank(Bank bank) {
	// this.bank = bank;
	// }
	/**
	 * @return the bankAccountType
	 */
	// public int getBankAccountType() {
	// return bankAccountType;
	// }

	/**
	 * @param bankAccountType
	 *            the bankAccountType to set
	 */
	// public void setBankAccountType(int bankAccountType) {
	// this.bankAccountType = bankAccountType;
	// }
	/**
	 * @return the bankAccountNumber
	 */
	// public String getBankAccountNumber() {
	// return bankAccountNumber;
	// }

	/**
	 * @param bankAccountNumber
	 *            the bankAccountNumber to set
	 */
	// public void setBankAccountNumber(String bankAccountNumber) {
	// this.bankAccountNumber = bankAccountNumber;
	// }
	/**
	 * @return the creditLimit
	 */
	public double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @param creditLimit
	 *            the creditLimit to set
	 */
	// public void setCreditLimit(double creditLimit) {
	// this.creditLimit = creditLimit;
	// }
	/**
	 * @return the cardOrLoanNumber
	 */
	public String getCardOrLoanNumber() {
		return cardOrLoanNumber;
	}

	/**
	 * @param cardOrLoanNumber
	 *            the cardOrLoanNumber to set
	 */
	// public void setCardOrLoanNumber(String cardOrLoanNumber) {
	// this.cardOrLoanNumber = cardOrLoanNumber;
	// }
	/**
	 * @return the isIncrease
	 */
	public boolean isIncrease() {
		return isIncrease;
	}

	/**
	 * @param isIncrease
	 *            the isIncrease to set
	 */
	public void setIncrease(boolean isIncrease) {
		this.isIncrease = isIncrease;
	}

	/**
	 * @return the currentBalance
	 */
	public double getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * @return the totalBalance
	 */
	public double getTotalBalance() {
		return totalBalance;
	}

	// /**
	// * @param currentBalance
	// * the currentBalance to set
	// */
	// public void setCurrentBalance(double currentBalance) {
	// this.currentBalance = currentBalance;
	// }

	// /**
	// * @param totalBalance
	// * the totalBalance to set
	// */
	// public void setTotalBalance(double totalBalance) {
	// this.totalBalance = totalBalance;
	// }

	/**
	 * @return the isOpeningBalanceEditable
	 */
	public boolean isOpeningBalanceEditable() {
		return isOpeningBalanceEditable;
	}

	/**
	 * @param isOpeningBalanceEditable
	 *            the isOpeningBalanceEditable to set
	 */
	public void setOpeningBalanceEditable(boolean isOpeningBalanceEditable) {
		this.isOpeningBalanceEditable = isOpeningBalanceEditable;
	}

	/**
	 * @return the hierarchy
	 */
	public String getHierarchy() {
		return hierarchy;
	}

	public int getBoxNumber() {
		return boxNumber;
	}

	public void setBoxNumber(int boxNumber) {
		this.boxNumber = boxNumber;
	}

	/**
	 * @param hierarchy
	 *            the hierarchy to set
	 */
	// public void setHierarchy(String hierarchy) {
	// this.hierarchy = hierarchy;
	// }
	@Override
	public boolean onDelete(Session session) throws CallbackException {

		JournalEntry existEntry = (JournalEntry) session
				.getNamedQuery("getJournalEntryForAccount")
				.setLong("id", this.getID()).uniqueResult();
		if (existEntry != null) {
			session.delete(existEntry);
		}

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(getID());
		accounterCore.setObjectType(AccounterCoreType.ACCOUNT);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		super.onLoad(arg0, arg1);
		this.oldParent = parent;
		this.previousOpeningBalance = openingBalance;
		this.previousCurrencyFactor = currencyFactor;
		this.previousAsOfDate = asOf;

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;
		if (this.currency == null) {
			this.currency = getCompany().getPrimaryCurrency();
		}

		try {
			if (this.type == Account.TYPE_INCOME
					|| this.type == Account.TYPE_OTHER_INCOME
					|| this.type == Account.TYPE_CREDIT_CARD
					|| this.type == Account.TYPE_PAYROLL_LIABILITY
					|| this.type == Account.TYPE_OTHER_CURRENT_LIABILITY
					|| this.type == Account.TYPE_LONG_TERM_LIABILITY
					|| this.type == Account.TYPE_EQUITY
					|| this.type == Account.TYPE_ACCOUNT_PAYABLE) {
				this.isIncrease = true;
			} else {
				this.isIncrease = false;
			}
			// if (this.type == Account.TYPE_INVENTORY_ASSET) {
			// this.isOpeningBalanceEditable = false;
			// }

			switch (type) {

			case Account.TYPE_CASH:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
				this.setGroupType(Account.GROUPTYPE_CASH);

				break;
			case Account.TYPE_BANK:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
				this.setGroupType(Account.GROUPTYPE_CASH);

				break;
			case Account.TYPE_ACCOUNT_RECEIVABLE:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
				break;
			case Account.TYPE_OTHER_CURRENT_ASSET:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
				break;
			case Account.TYPE_INVENTORY_ASSET:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
				break;
			case Account.TYPE_FIXED_ASSET:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_FIXED_ASSET);
				break;
			case Account.TYPE_OTHER_ASSET:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_OTHER_ASSET);
				break;
			case Account.TYPE_ACCOUNT_PAYABLE:
				this.setBaseType(Account.BASETYPE_LIABILITY);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
				break;
			case Account.TYPE_CREDIT_CARD:
				this.setBaseType(Account.BASETYPE_LIABILITY);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
				break;
			case Account.TYPE_OTHER_CURRENT_LIABILITY:
				this.setBaseType(Account.BASETYPE_LIABILITY);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
				break;
			case Account.TYPE_PAYROLL_LIABILITY:
				this.setBaseType(Account.BASETYPE_LIABILITY);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
				break;
			case Account.TYPE_LONG_TERM_LIABILITY:
				this.setBaseType(Account.BASETYPE_LIABILITY);
				this.setSubBaseType(Account.SUBBASETYPE_LONG_TERM_LIABILITY);
				break;
			case Account.TYPE_EQUITY:
				this.setBaseType(Account.BASETYPE_EQUITY);
				this.setSubBaseType(Account.SUBBASETYPE_EQUITY);
				break;

			case Account.TYPE_INCOME:
				this.setBaseType(Account.BASETYPE_ORDINARY_INCOME_OR_EXPENSE);
				this.setSubBaseType(Account.SUBBASETYPE_INCOME);
				break;
			case Account.TYPE_COST_OF_GOODS_SOLD:
				this.setBaseType(Account.BASETYPE_ORDINARY_INCOME_OR_EXPENSE);
				this.setSubBaseType(Account.SUBBASETYPE_COST_OF_GOODS_SOLD);
				break;
			case Account.TYPE_EXPENSE:
				this.setBaseType(Account.BASETYPE_ORDINARY_INCOME_OR_EXPENSE);
				this.setSubBaseType(Account.SUBBASETYPE_EXPENSE);
				break;

			case Account.TYPE_OTHER_INCOME:
				this.setBaseType(Account.BASETYPE_OTHER_INCOME_OR_EXPENSE);
				this.setSubBaseType(Account.SUBBASETYPE_INCOME);
				break;

			case Account.TYPE_OTHER_EXPENSE:
				this.setBaseType(Account.BASETYPE_OTHER_INCOME_OR_EXPENSE);
				this.setSubBaseType(Account.SUBBASETYPE_OTHER_EXPENSE);
				break;
			case Account.TYPE_PAYPAL:
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
				break;
			}

			String className = this.getClass().getName();
			if (this.flow == null) {
				if (this.parent == null) {
					List l = session
							.getNamedQuery("getFlow.by.Id.from.Account")
							.setEntity("company", getCompany()).list();
					if (l != null && l.size() > 0) {
						int count = Integer.parseInt((String) l.get(0));
						count++;
						this.flow = count + "";

					} else {
						this.flow = "1";
					}
				} else {
					List l = session
							.getNamedQuery("getCount.from.Account.and.parent")
							.setLong("parentId", this.parent.getID())
							.setEntity("company", getCompany()).list();
					if (l != null) {
						long count = (Long) l.get(0);
						count++;
						this.flow = this.parent.flow + "." + count;
					}
				}
			}

			if (currency == null) {
				currency = getCompany().getPrimaryCurrency();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return onUpdate(session);

	}

	protected void checkNullValues() throws AccounterException {
		if (this.name == null || this.name.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().Account());
		}
		if (this.number == null || this.number.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NUMBER_NULL,
					Global.get().messages().Account());
		}
		if (this.getType() == Account.TYPE_PAYPAL) {
			if (getPaypalEmail() == null || getPaypalEmail().trim().isEmpty()) {
				throw new AccounterException(
						AccounterException.ERROR_OBJECT_NULL, Global.get()
								.messages().paypalEmail());
			} else {
				if (!UIUtils.isValidEmail(getPaypalEmail())) {
					throw new AccounterException(
							AccounterException.ERROR_INVALID_EMAIL_ID);
				}
			}
		}

		/*
		 * Set<Account> accounts = getCompany().getAccounts(); for (Account
		 * account : accounts) { if
		 * (this.name.equalsIgnoreCase(account.getName())) { throw new
		 * AccounterException( AccounterException.ERROR_NAME_CONFLICT); } if
		 * (this.number.equalsIgnoreCase(account.getNumber())) { throw new
		 * AccounterException( AccounterException.ERROR_NUMBER_CONFLICT); } }
		 */
	}

	public void updateTotalBalance(double amount, double currencyFactor) {
		System.out.println(this.getName());

		String tempStr = " Total Balance of " + this.getName()
				+ " has been updated from " + this.totalBalance;

		this.totalBalance += amount;

		if (DecimalUtil.isEquals(currencyFactor, 0.00D)) {
			currencyFactor = 1.0;
		}

		double amountInAccountCurrency = amount / currencyFactor;

		this.totalBalanceInAccountCurrency += amountInAccountCurrency;

		if (this.parent != null) {
			this.parent.updateTotalBalance(amount, currencyFactor);
		}
		ChangeTracker.put(this);
	}

	public void updateCurrentBalance(Transaction transaction, double amount,
			double currencyFactor) {

		// if (!this.getName().equals(AccounterConstants.SALES_TAX_VAT_UNFILED))
		if (amount == 0) {
			return;
		}
		amount = (isIncrease ? 1 : -1) * amount * currencyFactor;

		if (this.getCurrency().getID() == getCompany().getPrimaryCurrency()
				.getID()) {
			currencyFactor = 1;
		}

		log.info("Updating Current Balance of  " + this.getName() + "("
				+ this.getCurrentBalance() + ") " + "with " + amount);

		this.currentBalance += amount;

		if (!DecimalUtil.isEquals(this.currentBalance, 0.0)
				&& isOpeningBalanceEditable) {
			isOpeningBalanceEditable = Boolean.FALSE;
		}

		this.updateTotalBalance(amount, currencyFactor);
		// log.info(accountTransaction);

		AccountTransaction accountTransaction = new AccountTransaction(this,
				transaction, amount, false);
		transaction.addAccountTransaction(accountTransaction);

		if (this.name.equalsIgnoreCase("Un Deposited Funds")) {
			TransactionMakeDepositEntries transactionMakeDepositEntries = accountTransaction
					.getTransaction().getTransactionMakeDepositEntries();

			if (transactionMakeDepositEntries != null) {
				transactionMakeDepositEntries.updateAmount(amount);
			} else {
				transactionMakeDepositEntries = new TransactionMakeDepositEntries(
						this, transaction, amount);
				accountTransaction.getTransaction()
						.setTransactionMakeDepositEntries(
								transactionMakeDepositEntries);
			}

		}
		ChangeTracker.put(this);
	}

	public void effectCurrentBalance(double amount, double currencyFactor) {

		if (amount == 0) {
			return;
		}

		if (this.getCurrency().getID() == getCompany().getPrimaryCurrency()
				.getID()) {
			currencyFactor = 1;
		}

		log.info("Effecting Current Balance of  " + this.getName() + "("
				+ this.getCurrentBalance() + ") " + "with " + amount);

		this.currentBalance += amount;

		if (!DecimalUtil.isEquals(this.currentBalance, 0.0)
				&& isOpeningBalanceEditable) {
			isOpeningBalanceEditable = Boolean.FALSE;
		}

		this.updateTotalBalance(amount, currencyFactor);

		ChangeTracker.put(this);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(session);
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);

		log.info("Update Account: " + this.getName() + "Balace:"
				+ this.totalBalance);

		// updating the flow
		String oldFlow = this.flow;

		if (this.getID() != 0) {
			if ((oldParent == null && parent == null)
					|| (oldParent != null && parent != null && (oldParent
							.getID() == parent.getID()))) {
				// NOTHING TO DO HERE, BECAUSE BOTH ARE SAME
			} else {
				if (oldParent != null) {
					this.oldParent
							.updateTotalBalance(-1 * this.totalBalance, 1);
					session.update(this.oldParent);
				}
				if (parent != null) {
					// While changing parents we give factor 1.0 as we will not
					// allow to
					// change parents for other currency accounts
					parent.updateTotalBalance(this.totalBalance, 1);

					Query query = session
							.getNamedQuery("getCountOfParentAccount")
							.setParameter("companyId",
									this.getCompany().getID())
							.setParameter("parentId", this.parent.getID());
					List l = query.list();
					// List l = session.createQuery(
					// "select count(*) from Account a where a.parent=:parent")
					// .setParameter("parent", this.parent).list();
					if (l != null) {
						long count = ((BigInteger) l.get(0)).longValue();
						count++;
						this.flow = this.parent.flow + "." + count;
					}
					if (oldParent != null) {
						// While changing parents we give factor 1.0 as we will
						// not
						// allow to
						// change parents for other currency accounts

						int i = Integer.parseInt(oldFlow.substring(oldFlow
								.length() - 1));

						// Query query1 =
						// session.getNamedQuery("getAccountDetails").setParameter("parentId",
						// this.oldParent.getID()).setParameter("flow",
						// oldFlow);

						Query query1 = session
								.getNamedQuery("getFlowList.form.Account.byId")
								.setParameter("parentId",
										this.oldParent.getID())
								.setParameter("flow", oldFlow,
										EncryptedStringType.INSTANCE)
								.setEntity("company", getCompany());
						List<Account> l2 = query1.list();

						// List<Account> l2 = session
						// .createQuery(
						// "from com.vimukti.accounter.core.Account a where a.parent=:parent and a.flow =:flow order by a.id")
						// .setParameter("parent", this.oldParent).setParameter(
						// "flow", oldFlow).list();

						if (l2 != null) {
							for (Account account : l2) {

								if (Integer.parseInt(account.flow
										.substring(oldFlow.length() - 1)) > i) {

									account.flow = account.flow.substring(0,
											account.flow.length() - 1).concat(
											"" + i);
									session.update(account);
									i++;
								}

							}
						}
					}
				}
			}

		}

		if (isOpenBalanceFieldsChanged()) {
			boolean temp = isOpeningBalanceEditable;
			isOpeningBalanceEditable = false;
			JournalEntry existEntry = (JournalEntry) session
					.getNamedQuery("getJournalEntryForAccount")
					.setLong("id", this.getID()).uniqueResult();
			String number = null;
			double openingBalance = this.openingBalance;
			if (existEntry != null) {
				number = existEntry.getNumber();
				session.delete(existEntry);
			}
			this.openingBalance = openingBalance;
			if (!DecimalUtil.isEquals(this.openingBalance, 0)) {
				JournalEntry journalEntry = createJournalEntry(this, number);
				session.save(journalEntry);
			}

			isOpeningBalanceEditable = temp;
		}

		// if (this.accountTransaction != null) {
		// session.saveOrUpdate(this.accountTransaction);
		// }

		// Object object =
		// session.createQuery(
		// "from com.vimukti.accounter.core.TransactionMakeDepositEntries tme where tme.transaction.id=?"
		// ).setParameter(0,
		// accountTransaction.getTransaction().getID()).uniqueResult();
		// transactionMakeDepositEntries = object !=
		// null?(TransactionMakeDepositEntries) object:null;
		// if(transactionMakeDepositEntries != null){
		// transactionMakeDepositEntries.updateAmount(accountTransaction.getAmount
		// ());
		// }else{
		// transactionMakeDepositEntries=new
		// TransactionMakeDepositEntries(this.accountTransaction.transaction,this
		// .accountTransaction.getAmount());
		// }
		// this.updateEntryMemo(session);
		ChangeTracker.put(this);

		session.setFlushMode(flushMode);
		return false;
	}

	private JournalEntry createJournalEntry(Account account, String number) {
		if (number == null) {
			number = NumberUtils.getNextTransactionNumber(
					Transaction.TYPE_JOURNAL_ENTRY, getCompany());
		}
		JournalEntry journalEntry = new JournalEntry();
		journalEntry.setCompany(account.getCompany());
		journalEntry.setInvolvedAccount(this);
		journalEntry.number = number;
		journalEntry.transactionDate = account.asOf;
		journalEntry.memo = "Opening Balance";
		journalEntry.setCurrency(this.currency);
		journalEntry.currencyFactor = this.currencyFactor;

		List<TransactionItem> items = new ArrayList<TransactionItem>();
		TransactionItem item1 = new TransactionItem();
		item1.setAccount(getCompany().getOpeningBalancesAccount());
		item1.setType(TransactionItem.TYPE_ACCOUNT);
		item1.setDescription(account.getName());
		items.add(item1);

		TransactionItem item2 = new TransactionItem();
		item2.setAccount(account);
		item2.setType(TransactionItem.TYPE_ACCOUNT);
		item2.setDescription(AccounterServerConstants.MEMO_OPENING_BALANCE);
		items.add(item2);

		if (account.isIncrease()) {
			item2.setLineTotal(-1 * account.getOpeningBalance());
			item1.setLineTotal(account.getOpeningBalance());
			journalEntry.setDebitTotal(item1.getLineTotal());
			journalEntry.setCreditTotal(item2.getLineTotal());
		} else {
			item2.setLineTotal(account.getOpeningBalance());
			item1.setLineTotal(-1 * account.getOpeningBalance());
			journalEntry.setDebitTotal(item2.getLineTotal());
			journalEntry.setCreditTotal(item1.getLineTotal());
		}

		journalEntry.setTransactionItems(items);

		journalEntry.setSaveStatus(Transaction.STATUS_APPROVE);

		return journalEntry;
	}

	@Override
	public String toString() {
		return "Account ID:" + getID() + " Name: " + this.name
				+ "    Balance: " + this.totalBalance;
	}

	public void setNumber(String number) {
		this.number = number;

	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;

	}

	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
		this.previousOpeningBalance = openingBalance;
	}

	public void setAsOf(FinanceDate asOf) {
		this.asOf = asOf;

	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		Account account = (Account) clientObject;
		// Query query = session.createQuery(
		// "from com.vimukti.accounter.core.Account A where A.name=?")
		// .setParameter(0, account.name);
		// List list = query.list();
		//
		// if (list != null && list.size() > 0) {
		// Account newAccount = (Account) list.get(0);
		// if (account.id != newAccount.id) {
		// throw new InvalidOperationException(
		// .Account Name already existed Enter Unique name for Account");

		if (!UserUtils.canDoThis(Account.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		Query query = session
				.getNamedQuery("getAccounts")
				.setParameter("name", this.name, EncryptedStringType.INSTANCE)
				.setParameter("number", this.number,
						EncryptedStringType.INSTANCE)
				.setLong("id", this.getID())
				.setParameter("companyId", account.getCompany().getID());

		List list = query.list();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();

			if (this.name.equals(object[0])) {
				Iterator it2 = list.iterator();
				while (it2.hasNext()) {
					Object[] object2 = (Object[]) it2.next();
					if (this.number.equals(object2[1])) {
						throw new AccounterException(
								AccounterException.ERROR_NAME_CONFLICT);
						// "An Account already exists with this name and number");
					}
				}
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "An Account already exists with this name");
			} else if (this.number.equals(object[1])) {
				Iterator it2 = list.iterator();
				while (it2.hasNext()) {
					Object[] object2 = (Object[]) it2.next();
					if (this.name.equals(object2[0])) {
						throw new AccounterException(
								AccounterException.ERROR_NUMBER_CONFLICT);
						// "An Account already exists with this name and number");
					}
				}
				throw new AccounterException(
						AccounterException.ERROR_NUMBER_CONFLICT);
				// "An Account already exists with this number");
			}
		}
		return true;

	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	/**
	 * @return the currenctBalanceInAccountCurrency
	 */
	public double getTotalBalanceInAccountCurrency() {
		return totalBalanceInAccountCurrency;
	}

	/**
	 * @return the currencyFactor
	 */
	public double getCurrencyFactor() {
		return currencyFactor;
	}

	/**
	 * @param currencyFactor
	 *            the currencyFactor to set
	 */
	public void setCurrencyFactor(double currencyFactor) {
		this.currencyFactor = currencyFactor;
	}

	public void setCurrenctBalance(double amount) {
		this.currentBalance += amount;
	}

	public void setPaypalEmail(String paypalEmail) {
		this.paypalEmail = paypalEmail;
	}

	public String getPaypalEmail() {
		return paypalEmail;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.ACCOUNT;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		w.put(messages.accountType(), Utility.getAccountTypeString(this.type))
				.gap();
		w.put(messages.accountNumber(), this.number);

		w.put(messages.accountName(), this.name).gap();

		if (this.currency != null)
			w.put(messages.currency(), this.currency.getFormalName());

		w.put(messages.openingBalance(), this.openingBalance).gap();
		if (this.asOf != null) {
			w.put(messages.date(), this.asOf.toString());
		}
		w.put(messages.comment(), this.comment).gap();

	}

	public boolean isOpenBalanceFieldsChanged() {
		if (!isOpeningBalanceEditable) {
			return false;
		}
		if (!DecimalUtil.isEquals(openingBalance, previousOpeningBalance)
				|| !DecimalUtil
						.isEquals(currencyFactor, previousCurrencyFactor)
				|| (previousAsOfDate != null && !asOf.equals(previousAsOfDate))) {
			return true;
		}
		return false;
	}

	public String getLastCheckNum() {
		return lastCheckNum;
	}

	public void setLastCheckNum(String lastCheckNum) {
		this.lastCheckNum = lastCheckNum;
	}

	public double getStatementBalance() {
		return statementBalance;
	}

	public void setStatementBalance(double statementBalance) {
		this.statementBalance = statementBalance;
	}

	public FinanceDate getStatementLastDate() {
		return statementLastDate;
	}

	public void setStatementLastDate(FinanceDate statementLastDate) {
		this.statementLastDate = statementLastDate;
	}

	public void clearOpeningBalance() {
		totalBalance -= openingBalance;
		openingBalance = 0.0;
	}

	@Override
	public void selfValidate() throws AccounterException {

		if (this.name == null || this.name.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().Account());
		}
		if (this.number == null || this.number.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NUMBER_NULL,
					Global.get().messages().Account());
		}
		if (this.getType() == Account.TYPE_PAYPAL) {
			if (getPaypalEmail() == null || getPaypalEmail().trim().isEmpty()) {
				throw new AccounterException(
						AccounterException.ERROR_OBJECT_NULL, Global.get()
								.messages().paypalEmail());
			} else {
				if (!UIUtils.isValidEmail(getPaypalEmail())) {
					throw new AccounterException(
							AccounterException.ERROR_INVALID_EMAIL_ID);
				}
			}
		}

		/*
		 * Set<Account> accounts = getCompany().getAccounts(); for (Account
		 * account : accounts) { if
		 * (this.name.equalsIgnoreCase(account.getName())) { throw new
		 * AccounterException( AccounterException.ERROR_NAME_CONFLICT); } if
		 * (this.number.equalsIgnoreCase(account.getNumber())) { throw new
		 * AccounterException( AccounterException.ERROR_NUMBER_CONFLICT); } }
		 */

	}

	public String getPaypalToken() {
		return paypalToken;
	}

	public void setPaypalToken(String paypalToken) {
		this.paypalToken = paypalToken;
	}

	public String getPaypalSecretkey() {
		return paypalSecretkey;
	}

	public void setPaypalSecretkey(String paypalSecretkey) {
		this.paypalSecretkey = paypalSecretkey;
	}

	public FinanceDate getEndDate() {
		return endDate;
	}

	public void setEndDate(FinanceDate endDate) {
		this.endDate = endDate;
	}

}
