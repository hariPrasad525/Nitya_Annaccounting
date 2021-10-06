package com.nitya.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientReminder;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;

public class RemindersListGrid extends BaseListGrid<ClientReminder> {

	public RemindersListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("RemindersListGrid");
	}

	@Override
	public void init() {
		super.init();
		ClientUser user = Accounter.getUser();
		((CheckBox) this.header.getWidget(0, 0))
				.setEnabled(user.isAdmin()
						|| user.getUserRole().equals(
								RolePermissions.FINANCIAL_ADVISER));
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected void executeDelete(ClientReminder object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void headerCellClicked(int colIndex) {
		if (colIndex == 0) {
			CheckBox headerCheckBx = (CheckBox) header.getWidget(0, colIndex);
			List<ClientReminder> records = getRecords();
			for (int row = 0; row < records.size(); row++) {
				CheckBox box = (CheckBox) body.getWidget(row, 0);
				box.setValue(headerCheckBx.getValue());
			}
		}
		super.headerCellClicked(colIndex);
	}

	@Override
	protected Object getColumnValue(ClientReminder obj, int index) {
		ClientTransaction transaction = obj.getRecurringTransaction()
				.getTransaction();
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			return messages.editAndCreate();
		case 2:
			return UIUtils.getDateByCompanyType(new ClientFinanceDate(obj
					.getTransactionDate()));
		case 3:
			return Utility.getTransactionName(transaction.getType());
		case 4:
			return DataUtils.amountAsStringWithCurrency(transaction.getTotal(),
					transaction.getCurrency());
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientReminder obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name(), "", messages.transactionDate(),
				messages.transactionType(), messages.amount() };
	}

	@Override
	protected void onClick(final ClientReminder obj, int row, int col) {
		if (!isCanOpenTransactionView(0, obj.getRecurringTransaction()
				.getTransaction().getType())) {
			return;
		}
		if (col == 1) {
			AccounterAsyncCallback<ClientTransaction> callBack = new AccounterAsyncCallback<ClientTransaction>() {

				@Override
				public void onException(AccounterException exception) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onResultSuccess(ClientTransaction result) {
					if (result != null) {
						result.setTobeDeleteReminder(obj.getID());
						ReportsRPC.openTransactionView(result);
					}
				}
			};
			Accounter.createHomeService().getTransactionToCreate(
					obj.getRecurringTransaction(), obj.getTransactionDate(),
					callBack);
		} else if (col == 0) {
			ReportsRPC.openTransactionView(obj.getRecurringTransaction()
					.getTransaction().getType(), obj.getRecurringTransaction()
					.getTransaction().getID());
		}
	}

	@Override
	protected void addCheckBox(final ClientReminder obj, Boolean value) {
		final CheckBox checkBox = new CheckBox();
		if (isCanOpenTransactionView(0, obj.getRecurringTransaction()
				.getTransaction().getType())) {
			checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					CheckBox box = ((CheckBox) event.getSource());
					Object col = UIUtils.getKey(widgetsMap, box);
					if (col != null)
						editComplete(obj, box.getValue(), (Integer) col);
				}
			});
			if (value != null)
				checkBox.setValue(value);
			if (disable) {
				checkBox.setEnabled(false);
			}
		} else {
			checkBox.setEnabled(false);
		}

		setWidget(currentRow, currentCol, checkBox);
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 225;
		case 1:
			return 115;
		case 2:
			return 200;
		case 3:
			return 200;
		case 4:
			return 150;

		default:
			return -1;
		}
	}

	@Override
	public List<ClientReminder> getSelectedRecords() {
		List<ClientReminder> reminders = new ArrayList<ClientReminder>();
		List<ClientReminder> selectedRecords = super.getSelectedRecords();
		for (ClientReminder clientReminder : selectedRecords) {
			if (isCanOpenTransactionView(0, clientReminder
					.getRecurringTransaction().getTransaction().getType())) {
				reminders.add(clientReminder);
			}
		}
		return reminders;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "col-0", "name", "col-new", "transactiondate",
				"transactiontype", "amount" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "col-0-value", "name-value", "col-new-value",
				"transactiondate-value", "transactiontype-value",
				"amount-value" };
	}
}
