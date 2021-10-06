package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.AccountRegisterAction;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;

public class ChartOfAccountsListGrid extends BaseListGrid<ClientAccount> {

	ClientCurrency currency = getCompany().getPrimaryCurrency();
	private boolean useAccountNumbers = getPreferences().getUseAccountNumbers();;

	public ChartOfAccountsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("ChartOfAccountsListGrid");
	}

	@Override
	protected int getColumnType(int col) {
		return super.getColumnType(col);
	}

	@Override
	protected Object getColumnValue(ClientAccount obj, int col) {

		if (useAccountNumbers) {
			switch (col) {
			case 0:
				return obj.getIsActive();
			case 1:
				return obj.getNumber() != null ? obj.getNumber() : "";
			case 2:
				return obj.getName() != null ? obj.getName() : "";
			case 3:
				return Utility.getAccountTypeString(obj.getType());
			case 4:
				return DataUtils.getAmountAsStringInCurrency(obj
						.getTotalBalanceInAccountCurrency(), getCompany()
						.getCurrency(obj.getCurrency()).getSymbol());
			case 5:
				return Accounter.getFinanceMenuImages().accounterRegisterIcon();
			case 6:
				return Accounter.getFinanceMenuImages().delete();
			default:
				break;
			}
			return null;
		} else {
			switch (col) {
			case 0:
				return obj.getIsActive();
			case 1:
				return obj.getName() != null ? obj.getName() : "";
			case 2:
				return Utility.getAccountTypeString(obj.getType());
			case 3:
				return DataUtils.getAmountAsStringInCurrency(obj
						.getTotalBalanceInAccountCurrency(), getCompany()
						.getCurrency(obj.getCurrency()).getSymbol());
			case 4:
				return Accounter.getFinanceMenuImages().accounterRegisterIcon();
			case 5:
				return Accounter.getFinanceMenuImages().delete();
			default:
				break;
			}
			return null;
		}

	}

	@Override
	protected String[] getColumns() {

		if (useAccountNumbers == true) {
			return new String[] { messages.active(), messages.no(),
					messages.name(), messages.type(), messages.balance(),
					messages.register(), "" };
		} else {
			return new String[] { messages.active(), messages.name(),
					messages.type(), messages.balance(), messages.register(),
					"" };
		}

	}

	@Override
	protected void onClick(ClientAccount obj, int row, int col) {
		if (!isCanOpenTransactionView(
				0,
				obj.getType() == ClientAccount.TYPE_BANK ? IAccounterCore.BANK_ACCOUNT
						: IAccounterCore.ACCOUNT)) {
			return;
		}
		if (col == getColumns().length - 1)
			showWarnDialog(obj);
		if (col == 5) {
			new AccountRegisterAction().run(obj, false);
		}
	}

	@Override
	public void onDoubleClick(ClientAccount account) {
		if (!isCanOpenTransactionView(
				0,
				account.getType() == ClientAccount.TYPE_BANK ? IAccounterCore.BANK_ACCOUNT
						: IAccounterCore.ACCOUNT)) {
			return;
		}
		if (account.getType() == ClientAccount.TYPE_BANK) {
			new NewAccountAction(ClientAccount.TYPE_BANK).run(
					(ClientBankAccount) account, false);
		} else {
			new NewAccountAction().run(account, false);
		}

	}

	@Override
	protected int[] setColTypes() {
		if (useAccountNumbers) {
			return new int[] { ListGrid.COLUMN_TYPE_CHECK,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE, ListGrid.COLUMN_TYPE_IMAGE };
		} else {
			return new int[] { ListGrid.COLUMN_TYPE_CHECK,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE, ListGrid.COLUMN_TYPE_IMAGE };
		}

	}

	@Override
	protected int getCellWidth(int index) {
		if (useAccountNumbers) {
			if (index == 6) {
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			}
			if (index == 0 || index == 1) {
				return 50;
			} else if (index == 2) {
				return 348;
			} else if (index == 4)
				return 150;
			else if (index == 5)
				return 80;
			if (index == 3)
				return 200;
			// return super.getCellWidth(index);
			return -1;
		} else {
			if (index == 5) {
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			}
			if (index == 0) {
				return 50;
			} else if (index == 2) {
				return 348;
			} else if (index == 3)
				return 200;
			else if (index == 4)
				return 80;
			else if (index == 1) {
				if (UIUtils.isMSIEBrowser()) {
					return 960 - 700;
				} else {
					return 960 - 690;
				}
			}
			if (index == 2)
				return 200;
			if (index == 6) {
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			}
			// return super.getCellWidth(index);
			return -1;
		}

	}

	@Override
	protected void executeDelete(ClientAccount obj) {
		deleteObject(obj);
	}

	@Override
	protected int sort(ClientAccount obj1, ClientAccount obj2, int index) {

		switch (index) {
		case 1:
			int num1 = Integer.parseInt(obj1.getNumber());
			int num2 = Integer.parseInt(obj2.getNumber());
			return UIUtils.compareInt(num1, num2);

		case 2:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());
		case 3:
			String type1 = Utility.getAccountTypeString(obj1.getType())
					.toLowerCase();
			String type2 = Utility.getAccountTypeString(obj2.getType())
					.toLowerCase();
			return type1.compareTo(type2);

		case 4:
			Double bal1 = obj1.getTotalBalance();
			Double bal2 = obj2.getTotalBalance();
			return bal1.compareTo(bal2);

		default:
			break;
		}

		return 0;
	}

	@Override
	public AccounterCoreType getType() {
		return AccounterCoreType.ACCOUNT;
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getTableRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}

	@Override
	protected String[] setHeaderStyle() {

		if (getPreferences().getUseAccountNumbers() == true) {
			return new String[] { "active", "no", "name", "type", "balance",
					"register", "unknown" };
		} else {
			return new String[] { "active", "name", "type", "balance",
					"register", "unknown" };
		}
	}

	@Override
	protected String[] setRowElementsStyle() {

		if (getPreferences().getUseAccountNumbers() == true) {
			return new String[] { "activeValue", "noValue", "nameValue",
					"typeValue", "balanceValue", "registerValue",
					"unknownValue", };
		} else {
			return new String[] { "activeValue", "nameValue", "typeValue",
					"balanceValue", "registerValue", "unknownValue", };
		}
	}
}
