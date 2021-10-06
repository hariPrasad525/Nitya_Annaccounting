package com.nitya.accounter.web.client.ui.grids;

import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientRecurringTransaction;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class RecurringsListGrid extends
		BaseListGrid<ClientRecurringTransaction> {

	public RecurringsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("RecurringsListGrid");
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 237;
		case 1:
			return 75;
		case 2:
			return 150;
		case 3:
			return 110;
		case 4:
		case 5:
		case 6:
			return 100;
		case 7:
			return 15;
		default:
			return -1;
		}
	}

	@Override
	protected void executeDelete(ClientRecurringTransaction object) {
		deleteObject(object);
	}

	@Override
	protected Object getColumnValue(ClientRecurringTransaction obj, int index) {
		switch (index) {
		case 0:// Name
			return obj.getName();
		case 1: // Recurring Type
			return getRecurringType(obj.getType());
		case 2: // Transaction Type
			return getTransactionName(obj);
		case 3:
			if (obj.getType() != ClientRecurringTransaction.RECURRING_UNSCHEDULED) {
				return obj.getFrequencyString();
			} else {
				return "";
			}
		case 4: // prevScheduleOn
			return obj.getPrevScheduleOn() == 0 ? null : new ClientFinanceDate(
					obj.getPrevScheduleOn());
		case 5: // nextScheduleOn
			return obj.getNextScheduleOn() == 0 ? null : new ClientFinanceDate(
					obj.getNextScheduleOn());
		case 6: // transaction amount
			return DataUtils.amountAsStringWithCurrency(obj.getTransaction()
					.getTotal(), obj.getTransaction().getCurrency());
		case 7: // delete image
			return Accounter.getFinanceMenuImages().delete();
		default:
			break;
		}
		return null;
	}

	private String getTransactionName(ClientRecurringTransaction obj) {
		ClientTransaction transaction = obj.getTransaction();
		String transactionName = null;
		if (transaction.getType() == ClientTransaction.TYPE_ESTIMATE) {
			transactionName = getEstimateNameByType((ClientEstimate) transaction);
		} else {
			transactionName = Utility.getTransactionName(obj.getTransaction()
					.getType());
		}
		return transactionName;
	}

	private String getEstimateNameByType(ClientEstimate transaction) {
		String estimateName = messages.quote();
		switch (transaction.getEstimateType()) {
		case ClientEstimate.CHARGES:
			estimateName = messages.charge();
			break;
		case ClientEstimate.CREDITS:
			estimateName = messages.credit();
			break;
		case ClientEstimate.SALES_ORDER:
			estimateName = messages.salesOrder();
			break;
		}
		return estimateName;
	}

	private String getRecurringType(int type) {
		switch (type) {
		case 0:
			return messages.scheduled();
		case 1:
			return messages.reminder();
		case 2:
			return messages.unScheduled();
		}
		return "";
	}

	@Override
	public void onDoubleClick(ClientRecurringTransaction obj) {

		if (!(isCanOpenTransactionView(obj.getTransaction().getSaveStatus(),
				obj.getTransaction().getType()))) {
			return;
		}

		ReportsRPC.openTransactionView(obj.getTransaction().getType(), obj
				.getTransaction().getID());

		// TODO need to open dialog also.
		// RecurringTransactionDialog dialog = null;
		// dialog = new RecurringTransactionDialog(obj);
		// dialog.show();
	}

	@Override
	protected void onClick(ClientRecurringTransaction obj, int row, int col) {
		if (!(isCanOpenTransactionView(obj.getTransaction().getSaveStatus(),
				obj.getTransaction().getType()))) {
			return;
		}
		if (col == 7) {
			showWarnDialog(obj);
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name(), messages.type(),
				messages.transactionType(), messages.interval(),
				messages.prevSchedule(), messages.nextSchedule(),
				messages.amount(), "" };
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		view.deleteSuccess(result);
		deleteRecord(this.getSelection());
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "name", "type", "transactiontype", "interval",
				"prevschedule", "nextschedule", "amount", "last-col" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "name-value", "type-value",
				"transactiontype-value", "interval-value",
				"prevschedule-value", "nextschedule-value", "amount-value",
				"last-col-value" };
	}
}
