/**
 * 
 */
package com.nitya.accounter.web.client.ui.banking;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.view.client.ListDataProvider;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientReconciliationItem;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.SelectionChangedHandler;
import com.nitya.accounter.web.client.ui.grids.columns.CheckBoxColumn;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationTransactionsGrid extends
		CellTable<ClientReconciliationItem> {

	protected AccounterMessages messages = Global.get().messages();
	private ListDataProvider<ClientReconciliationItem> dataProvider = new ListDataProvider<ClientReconciliationItem>();
	private SelectionChangedHandler<ClientReconciliationItem> clearedTransactionCallback;
	private ReconciliationView view;

	/**
	 * Creates new Instance
	 * 
	 * @param view
	 * 
	 * @param valueCallBack
	 */
	public ReconciliationTransactionsGrid(
			ReconciliationView view,
			SelectionChangedHandler<ClientReconciliationItem> clearedTransactionCallback) {
		this.clearedTransactionCallback = clearedTransactionCallback;
		this.view = view;
		initColumns();
		this.dataProvider.addDataDisplay(this);

	}

	private void initColumns() {

		TextColumn<ClientReconciliationItem> date = new TextColumn<ClientReconciliationItem>() {

			@Override
			public String getValue(ClientReconciliationItem object) {
				return object.getTransactionDate().toString();
			}

		};

		TextColumn<ClientReconciliationItem> transaction = new TextColumn<ClientReconciliationItem>() {

			@Override
			public String getValue(ClientReconciliationItem object) {
				return Utility.getTransactionName(object.getTransationType());
			}
		};

		TextColumn<ClientReconciliationItem> transactionMemo = new TextColumn<ClientReconciliationItem>() {

			@Override
			public String getValue(ClientReconciliationItem object) {
				return object.getTransctionMemo();
			}
		};

		TextColumn<ClientReconciliationItem> transactionID = new TextColumn<ClientReconciliationItem>() {

			@Override
			public String getValue(ClientReconciliationItem object) {
				return String.valueOf(object.getTransactionNo());
			}
		};

		TextColumn<ClientReconciliationItem> debit = new TextColumn<ClientReconciliationItem>() {

			@Override
			public String getValue(ClientReconciliationItem object) {
				setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				if (DecimalUtil.isLessThan(object.getAmount(), 0.00D)) {
					ClientCurrency currency = Accounter.getCompany()
							.getCurrency(
									view.getData().getAccount().getCurrency());
					return DataUtils.getAmountAsStringInCurrency(
							-object.getAmount(), currency.getSymbol());
				}
				return "";
			}
		};

		TextColumn<ClientReconciliationItem> credit = new TextColumn<ClientReconciliationItem>() {

			@Override
			public String getValue(ClientReconciliationItem object) {
				setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				if (DecimalUtil.isGreaterThan(object.getAmount(), 0.00D)) {
					ClientCurrency currency = Accounter.getCompany()
							.getCurrency(
									view.getData().getAccount().getCurrency());
					return DataUtils.getAmountAsStringInCurrency(
							object.getAmount(), currency.getSymbol());
				}
				return "";
			}
		};

		CheckBoxColumn<ClientReconciliationItem> clearCheckbox = new CheckBoxColumn<ClientReconciliationItem>() {

			@Override
			public void update(int index, ClientReconciliationItem object,
					Boolean value) {
				clearedTransactionCallback.selectionChanged(object, value);
			}

			@Override
			public Boolean getValue(ClientReconciliationItem object) {
				return false;
			}
		};

		this.addColumn(date, messages.transactionDate());
		this.addColumn(transactionID, messages.hash());
		this.addColumn(transactionMemo, messages.memo());
		this.addColumn(transaction, messages.transactionType());
		this.addColumn(debit, messages.debit());
		this.addColumn(credit, messages.credit());
		if (!view.isInViewMode()) {
			this.addColumn(clearCheckbox, messages.clear());
			setColumnWidth(clearCheckbox, 30, Unit.PX);
		}
		setColumnWidth(date, 100, Unit.PX);
		setColumnWidth(transactionID, 50, Unit.PX);
		setColumnWidth(transactionMemo, 200, Unit.PX);
		setColumnWidth(transaction, 200, Unit.PX);
		setColumnWidth(debit, 100, Unit.PX);
		setColumnWidth(credit, 100, Unit.PX);

	}

	/**
	 * @return
	 */
	protected ClientAccount getBankAccount() {
		return view.getData().getAccount();
	}

	public void setData(List<ClientReconciliationItem> data) {
		this.dataProvider.setList(data);
		this.setRowCount(data.size());
		this.setPageSize(this.getRowCount());
		this.redraw();
	}

}
