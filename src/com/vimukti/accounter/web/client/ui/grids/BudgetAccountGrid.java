package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.company.AddBudgetAmountDialogue;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class BudgetAccountGrid extends BaseListGrid<ClientBudgetItem> {

	ClientCurrency currency = getCompany().getPrimaryCurrency();
	List<ClientBudgetItem> budgetItem = new ArrayList<ClientBudgetItem>();

	public BudgetAccountGrid() {
		super(false);
		this.getElement().setId("BudgetAccountGrid");
	}

	@Override
	protected int getColumnType(int index) {

		switch (index) {
		case 14:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		}
	}

	@Override
	protected Object getColumnValue(ClientBudgetItem budget, int index) {

		switch (index) {
		case 0:
			return budget.getAccountsName();
		case 1:
			return DataUtils.amountAsStringWithCurrency(
					budget.getJanuaryAmount(), currency);
		case 2:
			return DataUtils.amountAsStringWithCurrency(
					budget.getFebruaryAmount(), currency);
		case 3:
			return DataUtils.amountAsStringWithCurrency(
					budget.getMarchAmount(), currency);
		case 4:
			return DataUtils.amountAsStringWithCurrency(
					budget.getAprilAmount(), currency);
		case 5:
			return DataUtils.amountAsStringWithCurrency(budget.getMayAmount(),
					currency);
		case 6:
			return DataUtils.amountAsStringWithCurrency(budget.getJuneAmount(),
					currency);
		case 7:
			return DataUtils.amountAsStringWithCurrency(budget.getJulyAmount(),
					currency);
		case 8:
			return DataUtils.amountAsStringWithCurrency(
					budget.getAugustAmount(), currency);
		case 9:
			return DataUtils.amountAsStringWithCurrency(
					budget.getSpetemberAmount(), currency);
		case 10:
			return DataUtils.amountAsStringWithCurrency(
					budget.getOctoberAmount(), currency);
		case 11:
			return DataUtils.amountAsStringWithCurrency(
					budget.getNovemberAmount(), currency);
		case 12:
			return DataUtils.amountAsStringWithCurrency(
					budget.getDecemberAmount(), currency);
		case 13:
			return DataUtils.amountAsStringWithCurrency(
					budget.getTotalAmount(), currency);
		case 14:
			return Accounter.getFinanceMenuImages().accounterRegisterIcon();
		default:
			break;
		}

		return null;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {

		case 0:
			return 120;
		case 1:
			return 60;
		case 2:
			return 60;
		case 3:
			return 60;
		case 4:
			return 60;
		case 5:
			return 60;
		case 6:
			return 60;
		case 7:
			return 60;
		case 8:
			return 60;
		case 9:
			return 60;
		case 10:
			return 60;
		case 11:
			return 60;
		case 12:
			return 60;
		case 13:
			return 60;
		case 14:
			return 25;

		}
		return -1;
	}

	@Override
	protected String[] getColumns() {
		String[] colArray = new String[15];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = messages.Account();
				break;
			case 1:
				colArray[index] = DayAndMonthUtil.jan();
				break;
			case 2:
				colArray[index] = DayAndMonthUtil.feb();
				break;
			case 3:
				colArray[index] = DayAndMonthUtil.mar();
				break;
			case 4:
				colArray[index] = DayAndMonthUtil.apr();
				break;
			case 5:
				colArray[index] = DayAndMonthUtil.mayS();
				break;
			case 6:
				colArray[index] = DayAndMonthUtil.jun();
				break;
			case 7:
				colArray[index] = DayAndMonthUtil.jul();
				break;
			case 8:
				colArray[index] = DayAndMonthUtil.aug();
				break;
			case 9:
				colArray[index] = DayAndMonthUtil.sep();
				break;
			case 10:
				colArray[index] = DayAndMonthUtil.oct();
				break;
			case 11:
				colArray[index] = DayAndMonthUtil.nov();
				break;
			case 12:
				colArray[index] = DayAndMonthUtil.dec();
				break;
			case 13:
				colArray[index] = messages.total();
				break;
			case 14:
				colArray[index] = messages.add();
				break;
			default:
				break;
			}
		}
		return colArray;
	}

	public void addData(ClientBudgetItem obj, ClientAccount account) {
		budgetItem.add(obj);
		obj.setAccountsName(account.getName());
		obj.setAccount(account);
		super.addData(obj);

	}

	@Override
	protected int[] setColTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void executeDelete(ClientBudgetItem object) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDoubleClick(final ClientBudgetItem obj) {
		HashMap<String, String> map = new HashMap<String, String>();
		String budgetTitle = messages.AddBudgetfor(obj.getAccountsName());
		AddBudgetAmountDialogue assignAccountsTo1099Dialog = new AddBudgetAmountDialogue(
				budgetTitle, "", map, obj);
		assignAccountsTo1099Dialog
				.setCallback(new ActionCallback<HashMap<String, String>>() {

					@Override
					public void actionResult(HashMap<String, String> result) {
						refreshView(result, obj);

					}
				});
		ViewManager.getInstance().showDialog(assignAccountsTo1099Dialog);

	}

	private void refreshView(HashMap<String, String> result,
			ClientBudgetItem obj) {

		obj.setJanuaryAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.jan())));
		obj.setFebruaryAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.feb())));
		obj.setMarchAmount(Double.parseDouble(result.get(DayAndMonthUtil.mar())));
		obj.setAprilAmount(Double.parseDouble(result.get(DayAndMonthUtil.apr())));
		obj.setMayAmount(Double.parseDouble(result.get(DayAndMonthUtil.mayS())));
		obj.setJuneAmount(Double.parseDouble(result.get(DayAndMonthUtil.jun())));
		obj.setJulyAmount(Double.parseDouble(result.get(DayAndMonthUtil.jul())));
		obj.setAugustAmount(Double.parseDouble(result.get(DayAndMonthUtil.aug())));
		obj.setOctoberAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.oct())));
		obj.setNovemberAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.nov())));
		obj.setSeptemberAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.sep())));
		obj.setDecemberAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.dec())));

		Double total;
		total = Double.parseDouble(result.get(DayAndMonthUtil.jan()))
				+ Double.parseDouble(result.get(DayAndMonthUtil.feb()))
				+ Double.parseDouble(result.get(DayAndMonthUtil.mar()))
				+ Double.parseDouble(result.get(DayAndMonthUtil.apr()))
				+ Double.parseDouble(result.get(DayAndMonthUtil.mayS()))
				+ Double.parseDouble(result.get(DayAndMonthUtil.jun()))
				+ Double.parseDouble(result.get(DayAndMonthUtil.jul()))
				+ Double.parseDouble(result.get(DayAndMonthUtil.aug()))
				+ Double.parseDouble(result.get(DayAndMonthUtil.oct()))
				+ Double.parseDouble(result.get(DayAndMonthUtil.nov()))
				+ Double.parseDouble(result.get(DayAndMonthUtil.sep()))
				+ Double.parseDouble(result.get(DayAndMonthUtil.dec()));

		obj.setTotalAmount(total);

		refreshAllRecords();

	}

	@Override
	public void addRecords(List<ClientBudgetItem> list) {
		// TODO Auto-generated method stub
		super.addRecords(list);
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "account", "jan", "feb", "mar", "apr", "may",
				"jun", "jul", "aug", "sep", "oct", "nov", "dec", "total", "add" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "account-value", "jan-value", "feb-value",
				"mar-value", "apr-value", "may-value", "jun-value",
				"jul-value", "aug-value", "sep-value", "oct-value",
				"nov-value", "dec-value", "total-value", "add-value" };
	}

}
