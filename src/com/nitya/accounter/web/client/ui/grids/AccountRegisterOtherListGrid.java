package com.nitya.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.AccountRegister;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.banking.AccountRegisterOthersView;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class AccountRegisterOtherListGrid extends BaseListGrid<AccountRegister> {

	public List<String> accountIDs = new ArrayList<String>();
	public double balance = 0.0;
	public double totalBalance = 0.0;
	private AccountRegisterOthersView view;

	public AccountRegisterOtherListGrid(boolean isMultiSelectionEnable) {
		super(false, true);
		this.getElement().setId("AccountRegisterOtherListGrid");
	}

	@Override
	protected Object getColumnValue(AccountRegister accRegister, int col) {
		switch (col) {
		case 0:
			return UIUtils.getDateByCompanyType(accRegister.getDate());
		case 1:
			return Utility.getTransactionName((accRegister.getType()));
		case 2:
			return accRegister.getNumber();
		case 3:
			if (DecimalUtil.isGreaterThan(accRegister.getAmount(), 0.0))
				return DataUtils.amountAsStringWithCurrency(accRegister
						.getAmount(), view == null ? accRegister.getCurrency()
						: view.getCurrency());
			else
				return DataUtils.amountAsStringWithCurrency(
						0.00,
						view == null ? accRegister.getCurrency() : view
								.getCurrency());
		case 4:
			if (DecimalUtil.isLessThan(accRegister.getAmount(), 0.0))
				return DataUtils.amountAsStringWithCurrency(
						-1 * accRegister.getAmount(),
						view == null ? accRegister.getCurrency() : view
								.getCurrency());
			else
				return DataUtils.amountAsStringWithCurrency(
						0.00,
						view == null ? accRegister.getCurrency() : view
								.getCurrency());
		case 5:
			return accRegister.getAccount();
		case 6:
			return accRegister.getMemo();
		case 7:
			return DataUtils.amountAsStringWithCurrency(
					getBalanceValue(accRegister),
					view == null ? accRegister.getCurrency() : view
							.getCurrency());

		case 8:
			if (!accRegister.isVoided())
				return Accounter.getFinanceImages().notvoid();
			else
				return Accounter.getFinanceImages().voided();

		}
		return null;
	}

	/*
	 * The balce value calculated using the formaula Balance = Balance - Reduce
	 * + Increase
	 */
	private double getBalanceValue(AccountRegister accountRegister) {
		/* Here 'd' value might be "positive" or "negative" */
		double d = accountRegister.getAmount();

		if (DecimalUtil.isLessThan(d, 0.0)) {
			d = -1 * d;
			balance = balance - d;
		} else {
			balance = balance + d;
		}
		totalBalance += balance;
		return balance;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.date(), messages.type(),
				messages.docNo(), messages.increase(), messages.reduce(),
				messages.Account(), messages.memo(), messages.ClosingBalance(),
				messages.isVoided() };
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	public void onDoubleClick(AccountRegister obj) {
		if (!isCanOpenTransactionView(0, obj.getType())) {
			return;
		}
		ReportsRPC.openTransactionView(obj.getType(), obj.getTransactionId());
	}

	@Override
	protected void executeDelete(AccountRegister object) {

	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 80;
		else if (index == 1)
			return 135;
		else if (index == 2)
			return 60;
		else if (index == 3 || index == 4 || index == 5)
			return 105;
		else if(index==6)
			return 142;
		else if (index == 7)
			return 115;
		else if (index == 8)
			return 40;
		
		return super.getCellWidth(index);
		// return -1;
	}

	@Override
	protected void onClick(AccountRegister obj, int row, int col) {
		if (!isCanOpenTransactionView(0, obj.getType())) {
			return;
		}
		if (col == 8 && !obj.isVoided()) {
			showWarningDialog(obj);
		}

	}

	@Override
	protected int sort(AccountRegister obj1, AccountRegister obj2, int index) {
		if (!(Utility.getTransactionName(obj1.getType()).equalsIgnoreCase(
				messages.openingBalance()) || Utility.getTransactionName(
				obj2.getType()).equalsIgnoreCase(messages.openingBalance()))) {
			switch (index) {
			case 0:
				ClientFinanceDate date1 = obj1.getDate();
				ClientFinanceDate date2 = obj2.getDate();
				if (date1 != null && date2 != null)
					return date1.compareTo(date2);
				break;
			case 1:
				String type1 = Utility.getTransactionName(obj1.getType());
				String type2 = Utility.getTransactionName(obj2.getType());
				return type1.toLowerCase().compareTo(type2.toLowerCase());

			case 2:
				if (obj1.getNumber() != null && obj2.getNumber() != null) {
					return obj1.getNumber().compareTo(obj2.getNumber());
				}

			case 3:
				Double amt1 = obj1.getAmount();
				Double amt2 = obj2.getAmount();
				return amt1.compareTo(amt2);

			case 4:
				Double amt11 = obj1.getAmount();
				Double amt21 = obj2.getAmount();
				return amt11.compareTo(amt21);

			case 5:
				String netPrice1 = obj1.getAccount();
				String netPrice2 = obj2.getAccount();
				return netPrice1.compareTo(netPrice2);

			case 6:
				if (obj1.getMemo() != null && obj2.getMemo() != null)
					return obj1.getMemo().toLowerCase()
							.compareTo(obj2.getMemo().toLowerCase());
				break;
			case 7:
				Double bal1 = getBalanceValue(obj1);
				Double bal2 = getBalanceValue(obj2);
				return bal1.compareTo(bal2);

			default:
				break;
			}
		}
		return 0;
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
		voidTransaction(UIUtils.getAccounterCoreType(obj.getType()),
				obj.getTransactionId());
	}

	@Override
	public AccounterCoreType getType() {
		return null;
	}

	@Override
	public void saveSuccess(IAccounterCore core) {
		view.initData();
	}

	public void setView(AccountRegisterOthersView accountRegisterOthersView) {
		this.view = accountRegisterOthersView;
	}

	public void setAccount(ClientAccount account) {
		this.currency = getCompany().getCurrency(account.getCurrency());
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "date", "type", "doc-no", "increase", "reduce",
				"account", "memo", "current-balance", "is-voided" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "date-value", "type-value", "doc-no-value",
				"increase-value", "reduce-value", "account-value",
				"memo-value", "current-balance-value", "is-voided-value" };
	}

	public void setOpeningBalance(double amount) {
		this.totalBalance = amount;
		this.balance = amount;
	}
}
