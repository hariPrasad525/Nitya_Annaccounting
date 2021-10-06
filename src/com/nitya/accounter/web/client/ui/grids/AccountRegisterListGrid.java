package com.nitya.accounter.web.client.ui.grids;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.AccountRegister;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;

public class AccountRegisterListGrid extends BaseListGrid<AccountRegister> {

	private final int accountType;
	public double balance = 0.0, payment = 0.0, deposit = 0.0;
	public double totalBalance = 0.0;

	public AccountRegisterListGrid(boolean isMultiSelectionEnable, int type) {
		super(isMultiSelectionEnable
				, true);
		accountType = type;
		this.getElement().setId("AccountRegisterListGrid");
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] getColumns() {
		if (accountType == ClientAccount.TYPE_BANK)
			return new String[] { messages.date(), messages.type(),
					(messages.checkNo() + "."), messages.payTo(),
					messages.memo(), messages.Account(), messages.payment(),
					messages.deposit(), messages.currentBalance(),
					messages.voided() };
		else if (accountType == ClientAccount.TYPE_CREDIT_CARD)
			return new String[] { messages.date(), messages.type(),
					messages.docNo(), messages.payTo(), messages.memo(),
					messages.Account(), messages.charge(), messages.payment(),
					messages.currentBalance(), messages.voided() };
		return new String[0];
	}

	@Override
	protected Object getColumnValue(AccountRegister accRegister, int col) {
		switch (col) {
		case 0:
			return accRegister.getDate();
		case 1:
			return Utility.getTransactionName((accRegister.getType()));
		case 2:
			if (accountType == ClientAccount.TYPE_BANK)
				return (accRegister.getCheckNumber() == null ? ""
						: (accRegister.getCheckNumber()).toString());
			else if (accountType == ClientAccount.TYPE_CREDIT_CARD)
				return accRegister.getNumber();
		case 3:
			return accRegister.getPayTo();
		case 4:
			return accRegister.getMemo();
		case 5:
			return accRegister.getAccount();
		case 6:
			if (accountType == ClientAccount.TYPE_BANK)
				return getBankAccValue(accRegister.getAmount(), 6);
			else if (accountType == ClientAccount.TYPE_CREDIT_CARD)
				return getCreditAccValue(accRegister.getAmount(), 6);
		case 7:
			if (accountType == ClientAccount.TYPE_BANK)
				return getBankAccValue(accRegister.getAmount(), 7);
			else if (accountType == ClientAccount.TYPE_CREDIT_CARD)
				return getCreditAccValue(accRegister.getAmount(), 7);
		case 8:
			return DataUtils.amountAsStringWithCurrency(
					getBalanceValue(accRegister), currency);

		case 9:
			if (!accRegister.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";
		}
		return null;
	}

	/*
	 * For accountType "CreditCard" the "Payment" column contains the amount
	 * value(and the "Deposit" column value is "0") if the amount is -ve. And
	 * "Deposit" column contains amount value( and "Payment" column value is 0)
	 * if the amount is +ve
	 */
	private String getBankAccValue(double value, int col) {
		double selectedvalue = value;
		switch (col) {
		case 6:
			return (DecimalUtil.isLessThan(selectedvalue, 0.0)) ? DataUtils
					.amountAsStringWithCurrency(selectedvalue, currency)
					: DataUtils.amountAsStringWithCurrency(0.00, currency);
		case 7:
			return (DecimalUtil.isGreaterThan(selectedvalue, 0.0)) ? DataUtils
					.amountAsStringWithCurrency(selectedvalue, currency)
					: DataUtils.amountAsStringWithCurrency(0.00, currency);
		}
		return "";
	}

	/*
	 * For accountType "CreditCard" the "Payment" column contains the amount
	 * value(and the "Deposit" column value is "0") if the amount is +ve. And
	 * "Deposit" column contains amount value( and "Payment" column value is 0)
	 * if the amount is -ve
	 */
	private String getCreditAccValue(double value, int col) {
		double selectedvalue = value;
		switch (col) {
		case 6:
			return (DecimalUtil.isGreaterThan(selectedvalue, 0.0)) ? DataUtils
					.amountAsStringWithCurrency(selectedvalue, currency)
					: DataUtils.amountAsStringWithCurrency(0.00, currency);
		case 7:
			return (DecimalUtil.isLessThan(selectedvalue, 0.0)) ? DataUtils
					.amountAsStringWithCurrency(selectedvalue, currency)
					: DataUtils.amountAsStringWithCurrency(0.00, currency);
		}
		return "";
	}

	/*
	 * The balance values for each row is obtained by using the formula...
	 * balance = balance - payment + deposit where balance: previous record's
	 * balance
	 */
	double getBalanceValue(AccountRegister accountRegister) {
		/* Here 'd' value might be "payment" or "Deposit" */
		double d = accountRegister.getAmount();

		if (accountType == ClientAccount.TYPE_BANK) {
			if (DecimalUtil.isLessThan(d, 0.0)) {
				d = -1 * d;
				balance = balance - d;
			} else {
				balance = balance + d;
			}
		} else {
			if (DecimalUtil.isLessThan(d, 0.0)) {
				d = -1 * d;
				balance = balance + d;
			} else {
				balance = balance - d;
			}

		}
		totalBalance += balance;
		return balance;
	}

	@Override
	protected int getCellWidth(int index) {

		if (index == 0)
			return 75;
		return super.getCellWidth(index);
		// return -1;
	}

	@Override
	protected void onClick(AccountRegister obj, int row, int col) {
		// if (col == 9 && !obj.isVoided()) {
		// showWarningDialog(obj);
		// }

	}

	private void showWarningDialog(final AccountRegister obj) {
		Accounter.showWarning(messages.doyouwanttoVoidtheTransaction(),
				AccounterType.WARNING, new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						return false;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {
						voidTransaction(obj);
						return true;
					}

				});
	}

	protected void voidTransaction(final AccountRegister obj) {
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {

			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					obj.setVoided(true);
					updateData(obj);
				}
			}
		};
		AccounterCoreType coretype = UIUtils
				.getAccounterCoreType(obj.getType());

		rpcDoSerivce
				.voidTransaction(coretype, obj.getTransactionId(), callback);
	}

	@Override
	public void onDoubleClick(AccountRegister obj) {
		// NOTHING TO DO.
	}

	@Override
	protected void executeDelete(AccountRegister object) {
		// NOTHING TO DO.
	}

	@Override
	public AccounterCoreType getType() {
		// TODO
		return null;
	}

	@Override
	public boolean isVoided(AccountRegister obj) {
		return obj.isVoided();
	}

	@Override
	public AccounterCoreType getAccounterCoreType(AccountRegister obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	public void setAccount(ClientAccount account) {
		this.currency = getCompany().getCurrency(account.getCurrency());
	}

	@Override
	protected int sort(AccountRegister obj1, AccountRegister obj2, int index) {
		switch (index) {
		case 0:
			ClientFinanceDate date1 = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;
		case 1:
			String type1 = Utility.getTransactionName((obj1.getType()));
			String type2 = Utility.getTransactionName((obj2.getType()));
			return type1.toLowerCase().compareTo(type2.toLowerCase());
		case 2:
			if (accountType == ClientAccount.TYPE_BANK) {
				Integer num1 = Integer.parseInt(obj1.getCheckNumber());
				Integer num2 = Integer.parseInt(obj2.getCheckNumber());
				return num1.compareTo(num2);
			} else {
				Integer num1 = Integer.parseInt(obj1.getNumber());
				Integer num2 = Integer.parseInt(obj2.getNumber());
				return num1.compareTo(num2);
			}
		case 3:
			String name1 = obj1.getPayTo().toLowerCase();
			String name2 = obj2.getPayTo().toLowerCase();
			return name1.compareTo(name2);
		case 4:
			String memo1 = obj1.getMemo().toLowerCase();
			String memo2 = obj2.getMemo().toLowerCase();
			return memo1.compareTo(memo2);
		case 5:
			String account1 = obj1.getAccount().toLowerCase();
			String account2 = obj2.getAccount().toLowerCase();
			return account1.compareTo(account2);
		case 6:
			if (accountType == ClientAccount.TYPE_BANK) {
				String bankAccValue1 = getBankAccValue(obj1.getAmount(), 6)
						.toLowerCase();
				String bankAccValue2 = getBankAccValue(obj2.getAmount(), 6)
						.toLowerCase();
				return bankAccValue1.compareTo(bankAccValue2);
			} else if (accountType == ClientAccount.TYPE_CREDIT_CARD) {
				String creditAccValue1 = getCreditAccValue(obj1.getAmount(), 6);
				String creditAccValue2 = getCreditAccValue(obj2.getAmount(), 6);
				return creditAccValue1.compareTo(creditAccValue2);
			}
		case 7:
			if (accountType == ClientAccount.TYPE_BANK) {
				String bankAccValue1 = getBankAccValue(obj1.getAmount(), 7);
				String bankAccValue2 = getBankAccValue(obj2.getAmount(), 7);
				return bankAccValue1.compareTo(bankAccValue2);
			} else if (accountType == ClientAccount.TYPE_CREDIT_CARD) {
				String creditAccValue1 = getCreditAccValue(obj1.getAmount(), 7);
				String creditAccValue2 = getCreditAccValue(obj2.getAmount(), 7);
				return creditAccValue1.compareTo(creditAccValue2);
			}
		case 8:
			Double balanceValue1 = getBalanceValue(obj1);
			Double balanceValue2 = getBalanceValue(obj2);
			return balanceValue1.compareTo(balanceValue2);
		default:
			break;
		}
		return 0;
	}

	@Override
	protected String[] setHeaderStyle() {
		if (accountType == ClientAccount.TYPE_BANK)
			return new String[] { "date", "type",
					"checkNo", "payTo",
					"memo", "Account", "payment",
					"deposit", "currentBalance",
					"voided" };
		else 
			return new String[] { "date", "type",
					"docNo", "payTo", "memo",
					"Account", "charge", "payment",
					"currentBalance", "voided" };
	}

	@Override
	protected String[] setRowElementsStyle() {

		if (accountType == ClientAccount.TYPE_BANK)
			return new String[] { "dateValue", "typeValue",
					"checkNoValue", "payToValue",
					"memoValue", "AccountValue", "paymentValue",
					"depositValue", "currentBalanceValue",
					"voided" };
		else 
			return new String[] { "dateValue", "typeValue",
					"docNoValue", "payToValue", "memoValue",
					"AccountValue", "chargeValue", "paymentValue",
					"currentBalanceValue", "voidedValue" };
	
	}
}
