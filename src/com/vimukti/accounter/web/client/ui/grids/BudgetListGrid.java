package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;

public class BudgetListGrid extends BaseListGrid<ClientBudgetItem> {

	public BudgetListGrid() {
		super(false, true);
		this.getElement().setId("BudgetListGrid");
	}

	public void initBudgetItems(List<ClientBudgetItem> allBudgetItems) {
		this.setRecords(allBudgetItems);

	}

	@Override
	protected Object getColumnValue(ClientBudgetItem budget, int col) {
		switch (col) {
		case 0:
			return budget.getAccountsName();

		case 1:
			return DataUtils.amountAsStringWithCurrency(
					budget.getTotalAmount(),

					getCompany().getPrimaryCurrency());
		default:
			break;
		}

		return null;
	}

	@Override
	protected String[] getColumns() {
		String[] colArray = new String[2];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = messages.Account();
				break;

			case 1:
				colArray[index] = messages.total();
				break;
			default:
				break;
			}
		}
		return colArray;

	}

	@Override
	protected void onClick(ClientBudgetItem obj, int row, int col) {
	}

	@Override
	protected void onValueChange(ClientBudgetItem obj, int col, Object value) {

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
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,

		ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	protected void updateTotal(ClientBudgetItem customer, boolean add) {

	}

	public Double getTotal() {
		return total;
	}

	public void setTotal() {
		this.total = 0.0D;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0) {
			return 300;

		} else {
			return 65;
		}
	}

	public AccounterCoreType getType() {
		return AccounterCoreType.BUDGET;
	}

	@Override
	public void addData(ClientBudgetItem obj) {
		super.addData(obj);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub
		super.deleteFailed(caught);
	}

	@Override
	protected void executeDelete(ClientBudgetItem object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(ClientBudgetItem obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "account", "total" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "account-value", "total-value" };
	}

}
