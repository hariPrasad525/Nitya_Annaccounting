package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionLog;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.core.AbstractTransactionBaseView;

public class TransactionHistoryTable extends CellTable<ClientTransactionLog> {
	private AsyncDataProvider<ClientTransactionLog> listDataProvider;
	protected AccounterMessages messages = Global.get().messages();
	private long transctionId;
	private AbstractTransactionBaseView<ClientTransaction> baseView;

	public TransactionHistoryTable(long id) {
		this.transctionId = id;
		this.getElement().setId("TransactionHistoryTable");
		createControls();
	}

	public TransactionHistoryTable(long id,
			AbstractTransactionBaseView<ClientTransaction> baseView) {
		this.baseView = baseView;
		this.transctionId = id;
		this.getElement().setId("TransactionHistoryTable");
		createControls();
	}

	private void createControls() {
		updateGridData();

		listDataProvider.addDataDisplay(this);

//		this.setWidth("100%", true);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		initTableColumns();
		updateColumnsData();
	}

	private void updateGridData() {
		listDataProvider = new AsyncDataProvider<ClientTransactionLog>() {
			@Override
			protected void onRangeChanged(HasData<ClientTransactionLog> display) {
				final int start = display.getVisibleRange().getStart();
				Accounter.createGETService().getTransactionHistory(
						transctionId,
						new AsyncCallback<ArrayList<ClientTransactionLog>>() {

							@Override
							public void onSuccess(
									ArrayList<ClientTransactionLog> result) {
								if (result == null || result.isEmpty()) {
									return;
								}
								baseView.updateLastActivityPanel(result
										.get(result.size() - 1));
								updateRowData(start, result);
								setRowCount(result.size());
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});
			}
		};
	}

	public void updateColumnsData() {
		listDataProvider.removeDataDisplay(this);
		updateGridData();
		listDataProvider.addDataDisplay(this);
	}

	private void initTableColumns() {
		TextColumn<ClientTransactionLog> changesColumn = new TextColumn<ClientTransactionLog>() {

			@Override
			public String getValue(ClientTransactionLog object) {
				return getActivityType(object.getType());
			}
		};

		TextColumn<ClientTransactionLog> dateColumn = new TextColumn<ClientTransactionLog>() {

			@Override
			public String getValue(ClientTransactionLog object) {
				return new Date(object.getTime()).toString();
			}
		};

		TextColumn<ClientTransactionLog> userNameColumn = new TextColumn<ClientTransactionLog>() {

			@Override
			public String getValue(ClientTransactionLog object) {
				return object.getUserName();
			}
		};

		TextColumn<ClientTransactionLog> detailsColumn = new TextColumn<ClientTransactionLog>() {

			@Override
			public String getValue(ClientTransactionLog object) {
				return object.getDescription();
			}
		};

		this.addColumn(changesColumn, messages.changes());
		this.addColumn(dateColumn, messages.date());
		this.addColumn(userNameColumn, messages.userName());
		this.addColumn(detailsColumn, messages.details());

		this.setColumnWidth(changesColumn, "80px");
		this.setColumnWidth(dateColumn, "275px");
		this.setColumnWidth(userNameColumn, "120px");

	}

	public String getActivityType(int activityType) {
		switch (activityType) {
		case ClientTransactionLog.TYPE_CREATE:
			return messages.created();
		case ClientTransactionLog.TYPE_EDIT:
			return messages.edited();
		case ClientTransactionLog.TYPE_VOID:
			return messages.voided();
		case ClientTransactionLog.TYPE_NOTE:
			return messages.note();
		default:
			return "";
		}
	}

}
