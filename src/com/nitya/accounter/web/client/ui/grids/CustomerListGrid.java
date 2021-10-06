package com.nitya.accounter.web.client.ui.grids;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.Lists.PayeeList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;

public class CustomerListGrid extends BaseListGrid<PayeeList> {
	Map<Integer, Integer> colsMap = new HashMap<Integer, Integer>();

	public CustomerListGrid() {
		super(false, true);
		this.getElement().setId("CustomerListGrid");
	}

	@Override
	protected Object getColumnValue(PayeeList payee, int col) {

		switch (col) {
		case 0:
			return payee.isActive();
		case 1:
			return payee.getPayeeName();
		case 2:
			return DataUtils.amountAsStringWithCurrency(
					payee.getCurrentMonth(), payee.getCurrecny());
		case 3:
			return DataUtils.amountAsStringWithCurrency(
					payee.getPreviousMonth(), payee.getCurrecny());
		case 4:
			return DataUtils.amountAsStringWithCurrency(
					payee.getPreviousSecondMonth(), payee.getCurrecny());

		case 5:
			return DataUtils.amountAsStringWithCurrency(
					payee.getPreviousThirdMonth(), payee.getCurrecny());

		case 6:
			return DataUtils.amountAsStringWithCurrency(
					payee.getPreviousFourthMonth(), payee.getCurrecny());

		case 7:
			return DataUtils.amountAsStringWithCurrency(
					payee.getPreviousFifthMonth(), payee.getCurrecny());

		case 8:
			return DataUtils.amountAsStringWithCurrency(payee.getYearToDate(),
					payee.getCurrecny());
		case 9:
			return DataUtils.amountAsStringWithCurrency(payee.getBalance(),
					payee.getCurrecny());
		case 10:
			updateTotal(payee, true);
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			break;
		}

		return null;
	}

	@Override
	protected String[] getColumns() {
		String[] colArray = new String[11];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = messages.active();
				break;
			case 1:
				colArray[index] = messages.payeeName(Global.get().Customer());
				break;
			case 2:
				colArray[index] = messages.currentMonth();
				colsMap.put(2, getCurrentMonth());
				break;
			case 3:
				colArray[index] = getMonthAsString(getCurrentMonth() - 1);
				colsMap.put(3, getCurrentMonth() - 1);
				break;
			case 4:
				colArray[index] = getMonthAsString(getCurrentMonth() - 2);
				colsMap.put(4, getCurrentMonth() - 2);
				break;
			case 5:
				colArray[index] = getMonthAsString(getCurrentMonth() - 3);
				colsMap.put(5, getCurrentMonth() - 3);
				break;
			case 6:
				colArray[index] = getMonthAsString(getCurrentMonth() - 4);
				colsMap.put(6, getCurrentMonth() - 4);
				break;
			case 7:
				colArray[index] = getMonthAsString(getCurrentMonth() - 5);
				colsMap.put(6, getCurrentMonth() - 5);
				break;
			case 8:
				colArray[index] = messages.yearToDate();
				break;
			case 9:
				colArray[index] = messages.balance();
				break;
			case 10:
				colArray[index] = "";
				break;
			default:
				break;
			}
		}
		return colArray;

	}

	private int getCurrentMonth() {
		return new ClientFinanceDate().getMonth();
	}

	private String getMonthAsString(int month) {
		switch (month) {
		case 1:
			return DayAndMonthUtil.jan();
		case 2:
			return DayAndMonthUtil.feb();
		case 3:
			return DayAndMonthUtil.mar();
		case 4:
			return DayAndMonthUtil.apr();
		case 5:
			return DayAndMonthUtil.mayS();
		case 6:
			return DayAndMonthUtil.jun();
		case 7:
			return DayAndMonthUtil.jul();
		case 8:
		case -4:
			return DayAndMonthUtil.aug();
		case 9:
		case -3:
			return DayAndMonthUtil.sep();
		case 10:
		case -2:
			return DayAndMonthUtil.oct();
		case 11:
		case -1:
			return DayAndMonthUtil.nov();
		case 12:
		case 0:
			return DayAndMonthUtil.dec();

		}
		return "";

	}

	@Override
	protected void onClick(PayeeList obj, int row, int col) {
		if (!Utility.isUserHavePermissions(AccounterCoreType.CUSTOMER)) {
			return;
		}
		List<PayeeList> customers = getRecords();
		PayeeList customer = customers.get(row);

		switch (col) {
		case 10:
			showWarnDialog(obj);
			break;
		default:
			break;
		}
		// super.onClick(obj, row, col);
	}

	@Override
	protected void onValueChange(PayeeList obj, int col, Object value) {

	}

	// private ClientAddress getBillToAddress(ClientCustomer customer) {
	// Set<ClientAddress> address = customer.getAddress();
	// for (ClientAddress a : address) {
	// if (a.getType() == ClientAddress.TYPE_BILL_TO) {
	// return a;
	// }
	//
	// }
	// return null;
	// }

	@Override
	public void onDoubleClick(PayeeList obj) {
		if (Utility.isUserHavePermissions(AccounterCoreType.CUSTOMER)) {
			ReportsRPC.openTransactionView(IAccounterCore.CUSTOMER, obj.id);
		}
	}

	protected void executeDelete(final PayeeList recordToBeDeleted) {
		AccounterAsyncCallback<ClientCustomer> callback = new AccounterAsyncCallback<ClientCustomer>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientCustomer result) {
				if (result != null) {
					deleteObject(result);

				}
			}

		};
		Accounter.createGETService().getObjectById(AccounterCoreType.CUSTOMER,
				recordToBeDeleted.id, callback);

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	protected void updateTotal(PayeeList customer, boolean add) {

		if (add) {
			if (customer.isActive())
				total += customer.getBalance();
			else
				total += customer.getBalance();
		} else
			total -= customer.getBalance();
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal() {
		this.total = 0.0D;
	}

	@Override
	protected int getCellWidth(int index) {

		switch (index) {
		case 0:
			return 40;
		case 1:
			return 211;
		case 2:
			return 100;
		case 3:
			return 70;
		case 4:
			return 70;
		case 5:
			return 70;
		case 6:
			return 70;
		case 7:
			return 70;
		case 8:
			return 75;
		case 9:
			return 70;
		case 10:
			return 25;
		}

		return -1;
	}

	@Override
	protected int sort(PayeeList obj1, PayeeList obj2, int index) {
		switch (index) {
		case 1:
			return obj1.getPayeeName().toLowerCase()
					.compareTo(obj2.getPayeeName().toLowerCase());

			// case 3:
			//
			// String city1 = getBillToAddress(obj1) != null ? getBillToAddress(
			// obj1).getCity() : "";
			// String city2 = getBillToAddress(obj2) != null ? getBillToAddress(
			// obj2).getCity() : "";
			// return city1.compareTo(city2);
			//
			// case 4:
			// String state1 = getBillToAddress(obj1) != null ?
			// getBillToAddress(
			// obj1).getStateOrProvinence() : "";
			// String state2 = getBillToAddress(obj2) != null ?
			// getBillToAddress(
			// obj2).getStateOrProvinence() : "";
			// return state1.compareTo(state2);
			//
			// case 5:
			// String zip1 = getBillToAddress(obj1) != null ? getBillToAddress(
			// obj1).getZipOrPostalCode() : "";
			// String zip2 = getBillToAddress(obj2) != null ? getBillToAddress(
			// obj2).getZipOrPostalCode() : "";
			// return zip1.compareTo(zip2);
		case 2:
			Double currentMonth1 = obj1.getCurrentMonth();
			Double currentMonth2 = obj2.getCurrentMonth();
			return currentMonth1.compareTo(currentMonth2);
		case 3:
			Double previousMonth1 = obj1.getPreviousMonth();
			Double previousMonth2 = obj2.getPreviousMonth();
			return previousMonth1.compareTo(previousMonth2);
		case 4:
			Double previousSecondMonth1 = obj1.getPreviousSecondMonth();
			Double previousSecondMonth2 = obj2.getPreviousSecondMonth();
			return previousSecondMonth1.compareTo(previousSecondMonth2);
		case 5:
			Double previousThirdMonth1 = obj1.getPreviousThirdMonth();
			Double previousThirdMonth2 = obj2.getPreviousThirdMonth();
			return previousThirdMonth1.compareTo(previousThirdMonth2);
		case 6:
			Double previousFourthMonth1 = obj1.getPreviousFourthMonth();
			Double previousFourthMonth2 = obj2.getPreviousFourthMonth();
			return previousFourthMonth1.compareTo(previousFourthMonth2);
		case 7:
			Double previousFifthMonth1 = obj1.getPreviousFifthMonth();
			Double previousFifthMonth2 = obj2.getPreviousFifthMonth();
			return previousFifthMonth1.compareTo(previousFifthMonth2);

		case 8:
			Double yearToDate1 = obj1.getYearToDate();
			Double yearToDate2 = obj2.getYearToDate();
			return yearToDate1.compareTo(yearToDate2);

		case 9:
			Double bal1 = obj1.getBalance();
			Double bal2 = obj2.getBalance();
			return bal1.compareTo(bal2);

		default:
			break;
		}

		return 0;
	}

	public AccounterCoreType getType() {
		return AccounterCoreType.CUSTOMER;
	}

	@Override
	public void addData(PayeeList obj) {
		super.addData(obj);
		((CheckBox) this.getWidget(currentRow, 0)).setEnabled(false);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getTableRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(AccounterExceptions.accounterMessages
					.payeeInUse(Global.get().Customer()));
			return;
		}
		super.deleteFailed(caught);
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "check", "customer-name", "this-month",
				"before-month", "this-month-two", "this-month-three",
				"this-month-four", "this-month-five", "year-to-date",
				"balance", "image-col" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "check-value", "customer-name-value",
				"this-month-value", "before-month-value",
				"this-month-two-value", "this-month-three-value",
				"this-month-four-value", "this-month-five-value",
				"year-to-date-value", "balance-value", "image-col-value" };
	}
}
