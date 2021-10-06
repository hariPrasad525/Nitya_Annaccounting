/**
 * 
 */
package com.nitya.accounter.web.client.ui.grids;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCashPurchase;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.Lists.BillsList;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.grids.columns.AccounterTextColumn;
import com.nitya.accounter.web.client.ui.grids.columns.CustomColumn;
import com.nitya.accounter.web.client.ui.grids.columns.DecimalTextColumn;
import com.nitya.accounter.web.client.ui.grids.columns.ImageActionColumn;
import com.nitya.accounter.web.client.ui.grids.columns.Width;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;

/**
 * @author Prasanna Kumar G
 * 
 */
public class BillsTable extends BaseTable<BillsList> {

	protected static AccounterMessages messages = Global.get().messages();
	@Override
	public void init() {

		// Add a selection model so we can select cells.
		final SelectionModel<BillsList> selectionModel = new MultiSelectionModel<BillsList>();
		// this.setSelectionModel(
		// selectionModel,
		// DefaultSelectionEventManager
		// .createCustomManager(new
		// DefaultSelectionEventManager.CheckboxEventTranslator<BillsList>() {
		// @Override
		// public SelectAction translateSelectionEvent(
		// CellPreviewEvent<BillsList> event) {
		// SelectAction action = super
		// .translateSelectionEvent(event);
		// if (action.equals(SelectAction.IGNORE)) {
		// GWT.log("DO WHAT YOU WANT!!!");
		// return SelectAction.IGNORE;
		// }
		// return action;
		// }
		// }));

		final NoSelectionModel<BillsList> noSelectionModel = new NoSelectionModel<BillsList>();

		noSelectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						Object source = event.getSource();
						BillsList clickedObject = noSelectionModel
								.getLastSelectedObject();
					}
				});
		this.setSelectionModel(noSelectionModel);

		this.addCellPreviewHandler(new Handler<BillsList>() {

			@Override
			public void onCellPreview(CellPreviewEvent<BillsList> event) {
				// System.out.println(event.getNativeEvent().getType());
				if (!"click".equals(event.getNativeEvent().getType())) {
					return;
				}
				if (event.getColumn() == 0) {
					return;
				}
				BillsList value = event.getValue();
				ReportsRPC.openTransactionView(value.getType(), value
						.getTransactionId());

			}
		});
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		super.init();
	}

	@Override
	protected void initColumns() {

		CheckboxCell checkboxCell = new CheckboxCell(true, true);
		// Initiating Columns
		Column<BillsList, Boolean> primaryCheckBox = new CustomColumn<BillsList, Boolean>(
				checkboxCell) {

			@Override
			public Boolean getValue(BillsList object) {
				return object.isVoided();
			}

			@Override
			public void update(int index, BillsList object, Boolean value) {
				object.isVoided();
			}

			@Override
			protected boolean enableSorting() {
				return false;
			}
		};
		this.addColumn(primaryCheckBox);

		AccounterTextColumn<BillsList> type = new AccounterTextColumn<BillsList>() {

			@Override
			public String getValue(BillsList object) {
				return Utility.getTransactionName((object.getType()));
			}

			@Override
			protected boolean enableSorting() {
				return true;
			}

			@Override
			public int compare(BillsList o1, BillsList o2) {
				String type1 = Utility.getTransactionName((o1.getType()))
						.toLowerCase();
				String type2 = Utility.getTransactionName((o2.getType()))
						.toLowerCase();
				return type1.compareTo(type2);
			}
		};
		this.addColumn(type, messages.type());

		AccounterTextColumn<BillsList> date = new AccounterTextColumn<BillsList>() {

			@Override
			public String getValue(BillsList object) {
				return object.getDate().toString();
			}

			@Override
			protected boolean enableSorting() {
				return true;
			}

			@Override
			public int compare(BillsList o1, BillsList o2) {
				ClientFinanceDate date1 = o1.getDate();
				ClientFinanceDate date2 = o2.getDate();
				if (date1 != null && date2 != null) {
					return date1.compareTo(date2);
				}
				return 0;
			}
		};
		this.addColumn(date, messages.date());

		AccounterTextColumn<BillsList> no = new AccounterTextColumn<BillsList>() {

			@Override
			public String getValue(BillsList object) {
				return object.getNumber();
			}

			@Override
			protected boolean enableSorting() {
				return true;
			}

			@Override
			public int compare(BillsList o1, BillsList o2) {
				int num1 = UIUtils.isInteger(o1.getNumber()) ? Integer
						.parseInt(o1.getNumber()) : 0;
				int num2 = UIUtils.isInteger(o2.getNumber()) ? Integer
						.parseInt(o2.getNumber()) : 0;
				if (num1 != 0 && num2 != 0) {
					return UIUtils.compareInt(num1, num2);
				} else {
					return o1.getNumber().compareTo(o2.getNumber());
				}
			}
		};
		this.addColumn(no, messages.no());

		AccounterTextColumn<BillsList> vendor = new AccounterTextColumn<BillsList>() {

			@Override
			public String getValue(BillsList object) {
				return object.getVendorName();
			}

			@Override
			protected boolean enableSorting() {
				return true;
			}

			@Override
			public int compare(BillsList o1, BillsList o2) {
				return o1.getVendorName().toLowerCase().compareTo(
						o2.getVendorName().toLowerCase());
			}
		};
		this.addColumn(vendor, Global.get().messages().payeeName(
				Global.get().Vendor()));

		DecimalTextColumn<BillsList> originalAmount = new DecimalTextColumn<BillsList>() {

			@Override
			public String getValue(BillsList object) {
				return String.valueOf(object.getOriginalAmount());
			}

			@Override
			protected boolean enableSorting() {
				return true;
			}

			@Override
			public int compare(BillsList o1, BillsList o2) {
				return o1.getOriginalAmount().compareTo(o2.getOriginalAmount());
			}
		};
		this.addColumn(originalAmount, messages.originalAmount());

		DecimalTextColumn<BillsList> balance = new DecimalTextColumn<BillsList>() {

			@Override
			public String getValue(BillsList object) {
				return String.valueOf(object.getBalance());
			}

			@Override
			protected boolean enableSorting() {
				return true;
			}

			@Override
			public int compare(BillsList o1, BillsList o2) {
				Double balance1 = o1.getBalance();
				Double balance2 = o2.getBalance();
				return UIUtils.compareTo(balance1, balance2);
			}

			@Override
			public Width getWidth() {
				return new Width(65.0);
			}
		};
		this.addColumn(balance, messages.balance());

		ImageActionColumn<BillsList> isVoid = new ImageActionColumn<BillsList>() {

			@Override
			protected void onSelect(int index, BillsList object) {
				if (!Accounter.getUser().canDoInvoiceTransactions()) {
					return;
				}
				if (!object.isVoided()) {

					if (object.getType() != ClientTransaction.TYPE_EMPLOYEE_EXPENSE
							|| (object.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE && object
									.getExpenseStatus() == ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)) {
						showWarningDialog(object, UIUtils
								.getAccounterCoreType(object.getType()), object
								.getTransactionId());
					} else {
						Accounter.showError(messages
								.expensecantbevoiditisApproved());
					}
				}

			}

			@Override
			public ImageResource getValue(BillsList object) {
				if (!object.isVoided())
					return Accounter.getFinanceImages().notvoid();
				else
					return Accounter.getFinanceImages().voided();
			}

			@Override
			public Width getWidth() {
				return new Width(30.0);
			}

		};
		this.addColumn(isVoid, messages.isVoid());
	}

}
