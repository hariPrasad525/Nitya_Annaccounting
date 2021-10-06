package com.vimukti.accounter.server.imports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.imports.BooleanField;
import com.vimukti.accounter.web.client.imports.DoubleField;
import com.vimukti.accounter.web.client.imports.FinanceDateField;
import com.vimukti.accounter.web.client.imports.Integer2Field;
import com.vimukti.accounter.web.client.imports.LongField;
import com.vimukti.accounter.web.client.imports.StringField;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class AccountImporter extends AbstractImporter<ClientAccount> {

	private double currencyFactor = 1.0;

	@Override
	public ClientAccount getData() {
		ClientAccount account = null;
		int type = getType();
		if (type == ClientAccount.TYPE_BANK) {
			account = new ClientBankAccount();
		} else {
			account = new ClientAccount();
		}
		// TODO need to check whether user entered or mapped the type or not for
		// account.
		account.setType(type);
		account.setOpeningBalanceEditable(true);
		account.setIsActive(true);
		// TODO need to check whether user entered or mapped number or not for
		// account.
		account.setNumber(getString("accountNumber"));
		// TODO need to check whether user entered or mapped name or not for
		// account.
		account.setName(getString("accountName"));
		account.setOpeningBalance(getDouble("openingBalances"));
		account.setAsOf(getFinanceDate("asOf").getDate());
		account.setCurrencyFactor(getDouble("currencyFactor") != null
				&& (getDouble("currencyFactor") > 0) ? getDouble("currencyFactor")
				: currencyFactor);

		String parentAccName = getString("parentAccountName");
		Long parentAccNumber = getLong("parentAccountNumber");
		long parentAccId = 0l;
		if (parentAccNumber > 0
				|| (parentAccName != null && !(parentAccName.isEmpty()))) {
			if (parentAccNumber > 0) {
				parentAccId = getAccountByNumberOrName(
						parentAccNumber.toString(), false);
				if (parentAccId > 0) {
					account.setParent(parentAccId);
				}
			} else {
				parentAccId = getAccountByNumberOrName(parentAccName, true);
				if (parentAccId > 0) {
					account.setParent(parentAccId);
				}
			}
		}
		// TODO
		switch (type) {
		case ClientAccount.TYPE_BANK:
			((ClientBankAccount) account)
					.setBank((getString("bankName") != null && !(getString("bankName")
							.isEmpty())) ? getBankName() : null);
			String bankAccTypStr = getString("bankAccountType");
			if (bankAccTypStr != null && !bankAccTypStr.isEmpty()) {
				int bankAccTyp = 0;
				if (bankAccTypStr.equalsIgnoreCase(messages.cuurentAccount()))
					bankAccTyp = ClientAccount.BANK_ACCCOUNT_TYPE_CURRENT_ACCOUNT;
				else if (bankAccTypStr.equalsIgnoreCase(messages.checking()))
					bankAccTyp = ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING;
				else if (bankAccTypStr.equalsIgnoreCase(messages.moneyMarket()))
					bankAccTyp = ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET;
				else if (bankAccTypStr.equalsIgnoreCase(messages.saving()))
					bankAccTyp = ClientAccount.BANK_ACCCOUNT_TYPE_SAVING;
				((ClientBankAccount) account).setBankAccountType(bankAccTyp);
			}
			((ClientBankAccount) account)
					.setBankAccountNumber(getString("bankAccountNumber"));
			account.setIncrease(Boolean.FALSE);
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			if (getDouble("creditLimit") != null) {
				account.setCreditLimit(getDouble("creditLimit"));
			}

			if (getDouble("cardOrLoanNumber") != null)
				account.setCardOrLoanNumber(getDouble("cardOrLoanNumber")
						.toString());
			break;
		case ClientAccount.TYPE_PAYPAL:
			if (getString("paypalEmail") != null) {
				account.setPaypalEmail(getString("paypalEmail"));
			}
			break;
		}
		account.setComment(getString("comments") != null ? getString("comments")
				: "");
		if (account.getType() == ClientAccount.TYPE_INCOME
				|| account.getType() == ClientAccount.TYPE_OTHER_INCOME
				|| account.getType() == ClientAccount.TYPE_CREDIT_CARD
				|| account.getType() == ClientAccount.TYPE_PAYROLL_LIABILITY
				|| account.getType() == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
				|| account.getType() == ClientAccount.TYPE_LONG_TERM_LIABILITY
				|| account.getType() == ClientAccount.TYPE_EQUITY
				|| account.getType() == ClientAccount.TYPE_ACCOUNT_PAYABLE) {
			account.setIncrease(Boolean.TRUE);
		} else {
			account.setIncrease(Boolean.FALSE);
		}
		account.updateBaseTypes();
		if (account.isAllowCurrencyChange()
				&& getCurrencyAsLong("currency") > 0) {
			account.setCurrency(getCurrencyAsLong("currency") > 0 ? getCurrencyAsLong("currency")
					: createCurrency());
		}
		return account;
	}

	private long createCurrency() {
		String currencyFormalName = getString("currency");
		ClientCurrency currency = CoreUtils.getCurrency(currencyFormalName);
		if (currency != null) {
			return currency.getID();
		}
		return 0;
	}

	private Long getBankName() {
		return getAccountByNumberOrName("bankName", true);
	}

	private int getType() {
		String accTypeName = getString("accountTypeName");
		int accTypeNumber = getInteger("accountTypeNumber");
		if (accTypeName != null && !(accTypeName.isEmpty())) {
			return Utility.getAccountType(accTypeName);
		} else if (accTypeNumber > 0) {
			return accTypeNumber;
		}
		return 0;
	}

	@Override
	public List<ImportField> getAllFields() {

		List<ImportField> fields = new ArrayList<ImportField>();
		fields.add(new StringField("accountTypeName", messages.accountType()
				+ messages.name(), true));
		fields.add(new Integer2Field("accountTypeNumber", messages
				.accountType() + messages.number(), true));
		fields.add(new StringField("accountNumber", messages.accountNumber(),
				true));
		fields.add(new StringField("accountName", messages.accountName(), true));
		// TODO
		// fields.add(new BooleanField("isActive", messages.isActive()));
		fields.add(new BooleanField("isSubAccount", messages.isSubAccount()));
		fields.add(new StringField("parentAccountName", messages
				.parentAccount() + messages.name(), true));
		fields.add(new LongField("parentAccountNumber", messages
				.parentAccount() + messages.number(), true));
		fields.add(new DoubleField("openingBalances", messages.openBalance()));
		fields.add(new FinanceDateField("asOf", messages.asOf()));
		fields.add(new StringField("comments", messages.comments()));
		fields.add(new StringField("currency", messages.currency()));
		fields.add(new DoubleField("currencyFactor", messages.currencyFactor()));
		fields.add(new StringField("bankName", messages.bankName()));
		fields.add(new StringField("bankAccountType", messages
				.bankAccountType(), true));
		fields.add(new StringField("bankAccountNumber", messages
				.bankAccountNumber()));
		fields.add(new DoubleField("creditLimit", messages.creditLimit()));
		fields.add(new DoubleField("cardOrLoanNumber", messages
				.cardOrLoanNumber()));
		fields.add(new DoubleField("paypalEmail", messages.paypalEmail(), true));
		return fields;
	}

	@Override
	public void validate(List<AccounterException> exceptions) {
		// ValidationResult result = new ValidationResult();
		// // accinfoform valid?
		// // check whether the account is already available or not
		// // valid account no?
		// // is prior to company prevent posting date?
		// // bankform valid?
		//
		Company company = getCompanyById(getCompanyId());

		int type = getType();
		if (!isValidAccountType()) {
			AccounterException exception = new AccounterException(
					AccounterException.ERROR_PLEASE_ENTER_OR_MAP,
					messages.accountType());
			exceptions.add(exception);
		}
		FinanceDateField financeDateField = (FinanceDateField) getFieldByName("asOf");
		ClientFinanceDate financeDate = null;
		if (financeDateField != null) {
			financeDate = financeDateField.getValue();
		}

		if (financeDate == null || financeDate.getDate() == 0) {
			AccounterException exception = new AccounterException(
					AccounterException.ERROR_PLEASE_ENTER_OR_MAP,
					messages.asOf() + messages.date());
			exceptions.add(exception);
		}

		//
		// String name = accNameText.getValue().toString() != null ? accNameText
		// .getValue().toString() : "";

		StringField accountNumberField = (StringField) getFieldByName("accountNumber");
		Long accountNumber = 0l;
		if (accountNumberField != null && accountNumberField.getValue() != null) {
			accountNumber = Long.parseLong(accountNumberField.getValue());
		}
		if (accountNumber == 0) {
			AccounterException exception = new AccounterException(
					AccounterException.ERROR_PLEASE_ENTER_OR_MAP,
					messages.accountNumber());
			exceptions.add(exception);
		}

		long accountByNumberOrName2 = getAccountByNumberOrName("accountNumber",
				false);
		if (accountByNumberOrName2 > 0) {
			AccounterException exception = new AccounterException(
					AccounterException.ERROR_NUMBER_ALREADY_EXIST,
					messages.account());
			exceptions.add(exception);
		}

		StringField accNameField = (StringField) getFieldByName("accountName");
		String accName = getString("accountName");
		if (accName == null || accName.isEmpty()) {
			AccounterException exception = new AccounterException(
					AccounterException.ERROR_PLEASE_ENTER_OR_MAP,
					messages.account());
			exceptions.add(exception);
		}

		long accountByNumberOrName = getAccountByNumberOrName("accountName",
				true);
		if (accountByNumberOrName > 0) {
			AccounterException exception = new AccounterException(
					AccounterException.ERROR_NAME_ALREADY_EXIST,
					messages.account());
			exceptions.add(exception);
		}

		String parentAccName = getString("parentAccountName");
		Long parentAccNumber = getLong("parentAccountNumber");
		long parentAccId = 0l;
		if (parentAccNumber > 0
				|| (parentAccName != null && !(parentAccName.isEmpty()))) {
			if (parentAccNumber > 0) {
				parentAccId = getAccountByNumberOrName(
						parentAccNumber.toString(), false);
				if (parentAccId == 0) {
					AccounterException exception = new AccounterException(
							AccounterException.ERROR_DOES_NOT_EXIST_WITH_THIS_NUMBER,
							messages.parentAccount());
					exceptions.add(exception);
				}
			} else {
				parentAccId = getAccountByNumberOrName(parentAccName, true);
				if (parentAccId == 0) {
					AccounterException exception = new AccounterException(
							AccounterException.ERROR_DOES_NOT_EXIST_WITH_THIS_NAME,
							messages.parentAccount());
					exceptions.add(exception);
				}
			}
			long currency = getCurrencyAsLong("currency");
			for (Account acc : company.getAccounts()) {
				if (acc.getID() == parentAccId) {
					if (acc.getType() != type) {
						AccounterException exception = new AccounterException(
								AccounterException.ERROR_PARENT_ACCOUNT_SHOULD_BE_SAME);
						exceptions.add(exception);
					}
					if (currency > 0)
						if (acc.getCurrency().getID() != currency) {
							AccounterException exception = new AccounterException(
									AccounterException.ERROR_PARENT_ACCOUNT_CURRENCY_SHOULD_BE_SAME);
							exceptions.add(exception);
						}
				}
			}
		}

		if (type == ClientAccount.TYPE_BANK) {

			StringField bankAccTypefield = (StringField) getFieldByName("bankAccountType");
			if (bankAccTypefield == null || bankAccTypefield.getValue() == null
					|| bankAccTypefield.getValue().isEmpty()) {
				AccounterException exception = new AccounterException(
						AccounterException.ERROR_PLEASE_ENTER_OR_MAP,
						messages.bankAccountType());
				exceptions.add(exception);
			}

		}
		if (type == ClientAccount.TYPE_PAYPAL) {

			StringField paypalEmailField = (StringField) getFieldByName("paypalEmail");
			if (paypalEmailField == null || paypalEmailField.getValue() == null
					|| paypalEmailField.getValue().isEmpty()) {
				AccounterException exception = new AccounterException(
						AccounterException.ERROR_PLEASE_ENTER_OR_MAP,
						messages.paypalEmail());
				exceptions.add(exception);
			}

			if (!ImporterUtils.isValidEmail(paypalEmailField.getValue())) {
				AccounterException exception = new AccounterException(
						AccounterException.ERROR_INVALID_EMAIL_ID);
				exceptions.add(exception);
			}
		}
	}

	private boolean isValidAccountType() {
		int type = getType();
		if (type == ClientAccount.TYPE_INCOME
				|| type == ClientAccount.TYPE_OTHER_INCOME
				|| type == ClientAccount.TYPE_CREDIT_CARD
				|| type == ClientAccount.TYPE_PAYROLL_LIABILITY
				|| type == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
				|| type == ClientAccount.TYPE_LONG_TERM_LIABILITY
				|| type == ClientAccount.TYPE_EQUITY
				|| type == ClientAccount.TYPE_ACCOUNT_PAYABLE
				|| type == ClientAccount.TYPE_EXPENSE
				|| type == ClientAccount.TYPE_OTHER_EXPENSE
				|| type == ClientAccount.TYPE_COST_OF_GOODS_SOLD
				|| type == ClientAccount.TYPE_CASH
				|| type == ClientAccount.TYPE_BANK
				|| type == ClientAccount.TYPE_OTHER_CURRENT_ASSET
				|| type == ClientAccount.TYPE_INVENTORY_ASSET
				|| type == ClientAccount.TYPE_OTHER_ASSET
				|| type == ClientAccount.TYPE_FIXED_ASSET
				|| type == ClientAccount.TYPE_PAYPAL) {
			return true;
		}
		return false;
	}
}
