package com.vimukti.accounter.core;

import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * @author vimukti16 Not used Yet
 */
public class Currency extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the Currency
	 */
	String name;
	/**
	 * Symbol of the Currency.
	 */
	String symbol;
	/**
	 * Formal Name of the Currency.
	 */
	String formalName;

	/**
	 * Accounts Receivable Account for this Currency
	 */
	private Account accountsReceivable;

	/**
	 * Accounts Payable Account for this Currency
	 */
	private Account accountsPayable;

	/*
	 * String countryName;
	 * 
	 * public Currency() { }
	 * 
	 * 
	 * public String getCountryName() { return countryName; }
	 * 
	 * public void setCountryName(String countryName) { this.countryName =
	 * countryName; }
	 */

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @return the formalName
	 */
	public String getFormalName() {
		return formalName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setFormalName(String formalName) {
		this.formalName = formalName;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.CURRENCY;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.currency()).gap();
		w.gap().put(messages.name(), this.name);
	}

	/**
	 * @return the accountsReceivable
	 */
	public Account getAccountsReceivable() {
		return accountsReceivable;
	}

	/**
	 * @param accountsReceivable
	 *            the accountsReceivable to set
	 */
	public void setAccountsReceivable(Account accountsReceivable) {
		this.accountsReceivable = accountsReceivable;
	}

	/**
	 * @return the accountsPayable
	 */
	public Account getAccountsPayable() {
		return accountsPayable;
	}

	/**
	 * @param accountsPayable
	 *            the accountsPayable to set
	 */
	public void setAccountsPayable(Account accountsPayable) {
		this.accountsPayable = accountsPayable;
	}

	/**
	 * Creates Accounts Receivable and Payable Account for this currency if not
	 * Exists
	 * 
	 * @param session
	 */
	public void createAccountsReveivablesAndPayables(Session session) {
		if (isPrimaryCurrency()) {
			return;
		}

		if (getAccountsReceivable() == null) {
			String nextReceivaleAccNumber = NumberUtils.getNextAccountNumber(
					getCompany().getId(), Account.TYPE_ACCOUNT_RECEIVABLE);
			String receivableName = !isPrimaryCurrency() ? AccounterServerConstants.ACCOUNTS_RECEIVABLE
					+ " - " + getFormalName()
					: AccounterServerConstants.ACCOUNTS_RECEIVABLE;
			Account accountsReceivable = new Account(
					Account.TYPE_ACCOUNT_RECEIVABLE, nextReceivaleAccNumber,
					receivableName, Account.CASH_FLOW_CATEGORY_OPERATING);
			accountsReceivable.setCurrency(this);
			accountsReceivable.setCompany(getCompany());
			session.saveOrUpdate(accountsReceivable);
			ChangeTracker.put(accountsReceivable);
			this.setAccountsReceivable(accountsReceivable);
		}

		if (getAccountsPayable() == null) {
			String nextPayableAccNumber = NumberUtils.getNextAccountNumber(
					getCompany().getId(), Account.TYPE_ACCOUNT_PAYABLE);

			String payableName = !isPrimaryCurrency() ? AccounterServerConstants.ACCOUNTS_PAYABLE
					+ " - " + getFormalName()
					: AccounterServerConstants.ACCOUNTS_PAYABLE;

			Account accountsPayable = new Account(Account.TYPE_ACCOUNT_PAYABLE,
					nextPayableAccNumber, payableName,
					Account.CASH_FLOW_CATEGORY_OPERATING);
			accountsPayable.setCurrency(this);
			accountsPayable.setCompany(getCompany());
			session.saveOrUpdate(accountsPayable);
			ChangeTracker.put(accountsPayable);
			this.setAccountsPayable(accountsPayable);
		}

	}

	private boolean isPrimaryCurrency() {
		return getCompany().getPrimaryCurrency().getID() == getID();
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		arg0.delete(accountsPayable);
		arg0.delete(accountsReceivable);
		return super.onDelete(arg0);
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (name == null || name.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().currency());
		}
		if (formalName == null || formalName.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().currencyFormalName());
		}
		if (symbol == null || symbol.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().currencySymbol());
		}

		Set<Currency> currencies = getCompany().getCurrencies();
		for (Currency currency : currencies) {
			if (currency.getName().equalsIgnoreCase(getName())
					&& currency.getID() != getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_ALREADY_EXIST, Global
								.get().messages().currency());
			}
			if (currency.getFormalName().equalsIgnoreCase(getFormalName())
					&& currency.getID() != getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_ALREADY_EXIST, Global
								.get().messages().currencyFormalName());
			}
		}

	}

	public ClientCurrency toClientCurrency() {
		ClientCurrency clientCurrency = new ClientCurrency();
		clientCurrency.setName(name);
		clientCurrency.setSymbol(symbol);
		clientCurrency.setFormalName(formalName);
		clientCurrency.setAccountsReceivable(accountsReceivable.getID());
		clientCurrency.setAccountsPayable(accountsPayable.getID());
		return clientCurrency;
	}
}
