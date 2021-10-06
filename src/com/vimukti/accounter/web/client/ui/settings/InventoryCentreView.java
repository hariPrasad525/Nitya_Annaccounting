package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.company.InventoryActions;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.core.IEditableView;
import com.vimukti.accounter.web.client.ui.core.ISavableView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class InventoryCentreView<T> extends AbstractBaseView<T> implements
		ISavableView<HashMap<String, Object>>, IEditableView {

	private ClientItem selectedItem;
	private ArrayList<ClientItem> listOfItems;
	protected ArrayList<ClientFinanceDate> startEndDates;
	private ArrayList<TransactionHistory> records;

	private ItemDetailsPanel itemDetailsPanel;
	private InventoryCentreItemsListGrid itemsListGrid;
	private SelectCombo activeInActiveSelect, trasactionViewSelect,
			trasactionViewTypeSelect, dateRangeSelector;
	private StyledPanel transactionGridpanel;
	private ItemTransactionsHistoryGrid transactionHistoryGrid;
	private HashMap<Integer, String> transactiontypebyStatusMap;
	private boolean isActiveItems = true;
	private ClientFinanceDate startDate, endDate;

	private String[] transactionTypes = new String[] { messages.quotes(),
			messages.Charges(), messages.credits(), messages.invoices(),
			messages.cashSales(),
			messages.payeeCreditNotes(Global.get().Customer()),
			messages.cashPurchases(),
			messages.payeeCreditNotes(Global.get().Vendor()), messages.bills(),
			messages.expenses(), messages.salesOrders(),
			messages.purchaseOrders() };
	private Button transactionButton;
	private StyledPanel rightVpPanel, dummyPanel;

	public InventoryCentreView() {
		this.getElement().setId("InventoryCentreView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		StyledPanel mainPanel = new StyledPanel("mainPanel");
		StyledPanel leftVpPanel = new StyledPanel("leftVpPanel");
		viewTypeCombo();
		DynamicForm viewform = new DynamicForm("viewform");
		viewform.add(activeInActiveSelect);
		leftVpPanel.add(viewform);
		itemsListGrid = new InventoryCentreItemsListGrid();
		itemsListGrid.init();
		initItemsListGrid();
		leftVpPanel.add(itemsListGrid);

		itemsListGrid.setStyleName("cusotmerCentrGrid");

		rightVpPanel = new StyledPanel("rightVpPanel");
		dummyPanel = new StyledPanel("dummyPanel");
		itemDetailsPanel = new ItemDetailsPanel(selectedItem);
		rightVpPanel.add(itemDetailsPanel);
		itemsListGrid.setItemSelectionListener(new ItemSelectionListener() {

			@Override
			public void itemSelected(ClientItem selectedItem) {
				InventoryCentreView.this.selectedItem = selectedItem;
				onItemSelected();

			}
		});
		transactionViewSelectCombo();
		transactionViewTypeSelectCombo();
		transactionDateRangeSelector();
		DynamicForm transactionViewform = new DynamicForm("viewform");

		transactionViewform.add(trasactionViewSelect, trasactionViewTypeSelect,
				dateRangeSelector);

		transactionGridpanel = new StyledPanel("transactionGridpanel");
		transactionGridpanel.add(transactionViewform);
		transactionHistoryGrid = new ItemTransactionsHistoryGrid() {
			@Override
			public void initListData() {

				onItemSelected();
			}

			@Override
			protected String[] setHeaderStyle() {
				return new String[] { "date", "type", "no", "memo", "quantity",
						"amount" };
			}

			@Override
			protected String[] setRowElementsStyle() {
				return new String[] { "date-value", "type-value", "no-value",
						"memo-value", "quantity-value", "amount" };
			}
		};
		transactionHistoryGrid.init();
		transactionHistoryGrid.addEmptyMessage(messages.pleaseSelectAnItem());
		int pageSize = getPageSize();
		transactionHistoryGrid.addRangeChangeHandler2(new Handler() {

			@Override
			public void onRangeChange(RangeChangeEvent event) {
				onPageChange(event.getNewRange().getStart(), event
						.getNewRange().getLength());
			}

		});
		SimplePager pager = new SimplePager(TextLocation.CENTER,
				(Resources) GWT.create(Resources.class), false, pageSize * 2,
				true);
		pager.setDisplay(transactionHistoryGrid);
		updateRecordsCount(0, 0, 0);

		if (Accounter.isIpadApp()) {

			transactionButton = new Button(messages.transaction());
			transactionButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ActionFactory.getInventoryTransactionListHistory(
							selectedItem).run();
				}
			});

		} else {
			dummyPanel.add(transactionGridpanel);
			dummyPanel.add(transactionHistoryGrid);
			dummyPanel.add(pager);
		}

		rightVpPanel.add(dummyPanel);

		Label labelTitle = new Label(messages.inventoryCentre());
		labelTitle.setStyleName("label-title");
		mainPanel.add(leftVpPanel);
		mainPanel.add(rightVpPanel);
		add(labelTitle);
		add(mainPanel);

	}

	public void updateRecordsCount(int start, int length, int total) {
		transactionHistoryGrid.updateRange(new Range(start, getPageSize()));
		transactionHistoryGrid.setRowCount(total, (start + length) == total);
	}

	private int getPageSize() {
		return 25;
	}

	private void viewTypeCombo() {
		if (activeInActiveSelect == null) {
			activeInActiveSelect = new SelectCombo(messages.show());

			ArrayList<String> activetypeList = new ArrayList<String>();
			activetypeList.add(messages.active());
			activetypeList.add(messages.inActive());
			activeInActiveSelect.initCombo(activetypeList);
			activeInActiveSelect.setComboItem(messages.active());
			activeInActiveSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (activeInActiveSelect.getSelectedValue() != null) {
								if (activeInActiveSelect.getSelectedValue()
										.toString()
										.equalsIgnoreCase(messages.active())) {
									refreshActiveinactiveList(true);

								} else {
									refreshActiveinactiveList(false);
								}
							}

						}

					});
		}
	}

	private void refreshActiveinactiveList(boolean isActiveList) {
		itemsListGrid.setSelectedItem(null);
		itemDetailsPanel.itemName.setText(messages.noItemSelected());
		this.selectedItem = null;
		onItemSelected();
		isActiveItems = isActiveList;
		initItemsListGrid();

	}

	private void transactionViewTypeSelectCombo() {
		if (trasactionViewTypeSelect == null) {
			trasactionViewTypeSelect = new SelectCombo(messages.type());
			getMessagesList();
			trasactionViewTypeSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (trasactionViewTypeSelect.getSelectedValue() != null) {
								callRPC(0, 25);
							}

						}

					});
		}

	}

	private void transactionViewSelectCombo() {
		if (trasactionViewSelect == null) {
			trasactionViewSelect = new SelectCombo(messages.currentView());

			ArrayList<String> transactionTypeList = new ArrayList<String>();
			transactionTypeList.add(messages.allTransactions());
			for (String type : transactionTypes) {
				if (type.equalsIgnoreCase(messages.Charges())
						|| type.equalsIgnoreCase(messages.credits())) {
					if (getPreferences().isDelayedchargesEnabled()) {
						transactionTypeList.add(type);
					} else {
						continue;
					}
				} else if (type.equalsIgnoreCase(messages.salesOrders())) {
					if (getPreferences().isSalesOrderEnabled()) {
						transactionTypeList.add(type);
					} else {
						continue;
					}
				} else if (type.equalsIgnoreCase(messages.purchaseOrders())) {
					if (getPreferences().isPurchaseOrderEnabled()) {
						transactionTypeList.add(type);
					} else {
						continue;
					}
				} else {
					transactionTypeList.add(type);
				}
			}
			trasactionViewSelect.initCombo(transactionTypeList);
			trasactionViewSelect.setComboItem(messages.allTransactions());
			trasactionViewSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (trasactionViewSelect.getSelectedValue() != null) {
								getMessagesList();
								callRPC(0, 25);
							}

						}

					});
		}

	}

	private void getMessagesList() {
		String selectedValue = trasactionViewSelect.getSelectedValue();
		transactiontypebyStatusMap = new HashMap<Integer, String>();
		if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.allTransactions())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_TRANSACTIONS,
					messages.allTransactions());
		} else if (selectedValue.equalsIgnoreCase(messages.quotes())) {

			transactiontypebyStatusMap.put(TransactionHistory.ALL_QUOTES,
					messages.allQuotes());
			transactiontypebyStatusMap.put(TransactionHistory.DRAFT_QUOTES,
					messages.draftTransaction(messages.quotes()));

		} else if (selectedValue.equalsIgnoreCase(messages.Charges())) {

			transactiontypebyStatusMap.put(TransactionHistory.ALL_CHARGES,
					messages.allCahrges());
			transactiontypebyStatusMap.put(TransactionHistory.DRAFT_CHARGES,
					messages.draftTransaction(messages.Charges()));

		} else if (selectedValue.equalsIgnoreCase(messages.credits())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CREDITS,
					messages.allCredits());
			transactiontypebyStatusMap.put(TransactionHistory.DRAFT_CREDITS,
					messages.draftTransaction(messages.credits()));

		} else if (selectedValue.equalsIgnoreCase(messages.invoices())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_INVOICES,
					messages.getallInvoices());
			transactiontypebyStatusMap.put(TransactionHistory.OPENED_INVOICES,
					messages.getOpendInvoices());
			transactiontypebyStatusMap.put(
					TransactionHistory.OVER_DUE_INVOICES,
					messages.overDueInvoices());
			transactiontypebyStatusMap.put(TransactionHistory.DRAFT_INVOICES,
					messages.draftTransaction(messages.invoices()));
		} else if (selectedValue.equalsIgnoreCase(messages.cashSales())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CASHSALES,
					messages.all() + " " + messages.cashSales());
			transactiontypebyStatusMap.put(TransactionHistory.DRAFT_CASHSALES,
					messages.draftTransaction(messages.cashSales()));

		} else if (selectedValue.equalsIgnoreCase(messages
				.payeeCreditNotes(Global.get().Customer()))) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CREDITMEMOS,
					messages.allCreditMemos());
			transactiontypebyStatusMap.put(
					TransactionHistory.DRAFT_CREDITMEMOS,
					messages.draftTransaction(messages.creditNote()));
			// transactiontypebyStatusMap.put(
			// TransactionHistory.OPEND_CREDITMEMOS,
			// messages.openCreditMemos());

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.cashPurchases())) {
			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_CASH_PURCHASES,
					messages.allCashPurchases());
			transactiontypebyStatusMap.put(
					TransactionHistory.DRAFT_CASH_PURCHASES,
					messages.draftTransaction(messages.cashPurchases()));

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.bills())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_BILLS,
					messages.allBills());
			transactiontypebyStatusMap.put(TransactionHistory.OPEND_BILLS,
					messages.all() + " " + messages.openedBills());
			transactiontypebyStatusMap.put(TransactionHistory.OVERDUE_BILLS,
					messages.all() + " " + messages.overDueBills());
			transactiontypebyStatusMap.put(TransactionHistory.DRAFT_BILLS,
					messages.draftTransaction(messages.bills()));

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.payeeCreditNotes(Global.get().Vendor()))) {
			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_VENDOR_CREDITNOTES,
					messages.all() + " "
							+ messages.payeeCreditNotes(Global.get().Vendor()));
			transactiontypebyStatusMap.put(
					TransactionHistory.DRAFT_VENDOR_CREDITNOTES, messages
							.draftTransaction(messages.payeeCreditNotes(Global
									.get().Vendor())));

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.expenses())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_EXPENSES,
					messages.allExpenses());
			transactiontypebyStatusMap.put(
					TransactionHistory.CREDIT_CARD_EXPENSES,
					messages.creditCardExpenses());
			transactiontypebyStatusMap.put(TransactionHistory.CASH_EXPENSES,
					messages.cashExpenses());
			transactiontypebyStatusMap.put(
					TransactionHistory.DRAFT_CREDIT_CARD_EXPENSES,
					messages.draftTransaction(messages.creditCardExpenses()));
			transactiontypebyStatusMap.put(
					TransactionHistory.DRAFT_CASH_EXPENSES,
					messages.draftTransaction(messages.cashExpenses()));

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.salesOrders())) {

			transactiontypebyStatusMap.put(TransactionHistory.ALL_SALES_ORDERS,
					messages.all());
			transactiontypebyStatusMap.put(
					TransactionHistory.DRAFT_SALES_ORDERS, messages.drafts());

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.purchaseOrders())) {

			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_PURCHASE_ORDERS, messages.all());
			transactiontypebyStatusMap
					.put(TransactionHistory.DRAFT_PURCHASE_ORDERS,
							messages.drafts());

		}
		List<String> typeList = new ArrayList<String>(
				transactiontypebyStatusMap.values());
		Collections.sort(typeList, new Comparator<String>() {

			@Override
			public int compare(String entry1, String entry2) {
				return entry1.compareTo(entry2);
			}

		});
		trasactionViewTypeSelect.initCombo(typeList);
		trasactionViewTypeSelect.setComboItem(typeList.get(0));
	}

	protected void transactionDateRangeSelector() {
		dateRangeSelector = new SelectCombo(messages.date());

		ArrayList<String> dateRangeList = new ArrayList<String>();
		String[] dateRangeArray = { messages.all(), messages.thisWeek(),
				messages.thisMonth(), messages.lastWeek(),
				messages.lastMonth(), messages.thisFinancialYear(),
				messages.lastFinancialYear(), messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(), messages.financialYearToDate() };
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeSelector.initCombo(dateRangeList);

		dateRangeSelector.setComboItem(messages.all());
		dateRangeSelector
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						dateRangeSelector.setComboItem(selectItem);
						if (dateRangeSelector.getValue() != null) {
							dateRangeChanged(selectItem);
							callRPC(0, getPageSize());

						}
					}
				});

	}

	public void dateRangeChanged(String dateRange) {
		ClientFinanceDate date = new ClientFinanceDate();
		startDate = Accounter.getStartDate();
		endDate = getCompany().getCurrentFiscalYearEndDate();
		// getLastandOpenedFiscalYearEndDate();
		if (dateRange.equals(messages.thisWeek())) {
			startDate = getWeekStartDate();
			endDate.setDay(startDate.getDay() + 6);
			endDate.setMonth(startDate.getMonth());
			endDate.setYear(startDate.getYear());
		}
		if (dateRange.equals(messages.thisMonth())) {
			startDate = new ClientFinanceDate(date.getYear(), date.getMonth(),
					1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(new ClientFinanceDate().getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());

		}
		if (dateRange.equals(messages.lastWeek())) {
			endDate = getWeekStartDate();
			endDate.setDay(endDate.getDay() - 1);
			startDate = new ClientFinanceDate(endDate.getDate());
			startDate.setDay(startDate.getDay() - 6);

		}
		if (dateRange.equals(messages.lastMonth())) {
			int day;
			if (date.getMonth() == 0) {
				day = getMonthLastDate(11, date.getYear() - 1);
				startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
			} else {
				day = getMonthLastDate(date.getMonth() - 1, date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, day);
			}
		}
		if (dateRange.equals(messages.thisFinancialYear())) {
			startDate = getCompany().getCurrentFiscalYearStartDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
		}
		if (dateRange.equals(messages.lastFinancialYear())) {

			startDate = Accounter.getCompany().getCurrentFiscalYearStartDate();
			startDate.setYear(startDate.getYear() - 1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(Accounter.getCompany().getCurrentFiscalYearEndDate()
					.getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());
			endDate.setYear(endDate.getYear() - 1);

		}
		if (dateRange.equals(messages.thisFinancialQuarter())) {
			startDate = new ClientFinanceDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
		}
		if (dateRange.equals(messages.lastFinancialQuarter())) {
			startDate = new ClientFinanceDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
			startDate.setYear(startDate.getYear() - 1);
			endDate.setYear(endDate.getYear() - 1);
		}
		if (dateRange.equals(messages.financialYearToDate())) {
			startDate = getCompany().getCurrentFiscalYearStartDate();
			endDate = new ClientFinanceDate();
		}
	}

	private void getCurrentQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		}
	}

	private int getMonthLastDate(int month, int year) {
		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return lastDay;
	}

	public ClientFinanceDate getWeekStartDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		int day = date.getDay() % 6;
		ClientFinanceDate newDate = new ClientFinanceDate();
		if (day != 1) {
			newDate.setDay(date.getDay() - day);
		} else {
			newDate.setDay(date.getDay());
		}
		return newDate;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void initItemsListGrid() {
		filterItems();
		itemsListGrid.removeAllRecords();
		if (listOfItems.isEmpty()) {
			itemsListGrid.addEmptyMessage(messages.youDontHaveAny(messages
					.inventoryItems()));
		} else {
			itemsListGrid.setRecords(listOfItems);
		}
	}

	private void filterItems() {
		ArrayList<ClientItem> items = getCompany().getItems();
		ArrayList<ClientItem> result = new ArrayList<ClientItem>();
		for (ClientItem item : items) {
			if (item.getType() == ClientItem.TYPE_INVENTORY_PART
					|| item.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
				if (isActiveItems == item.isActive()) {
					result.add(item);
				}
			}
		}
		listOfItems = result;
	}

	void onPageChange(int start, int length) {
		callRPC(start, length);
	}

	private void onItemSelected() {

		itemDetailsPanel.showItemDetails(selectedItem);
		if (Accounter.isIpadApp()) {
			rightVpPanel.add(transactionButton);
			transactionButton.setText(messages2.transactionListFor(selectedItem
					.getDisplayName()));
		}
		transactionHistoryGrid.setSelectedItem(selectedItem);
		MainFinanceWindow.getViewManager().updateButtons();
		callRPC(0, 25);
	}

	protected void callRPC(int start, int length) {
		transactionHistoryGrid.removeAllRecords();
		records = new ArrayList<TransactionHistory>();
		if (selectedItem != null) {
			Accounter.createHomeService().getItemTransactionsList(
					selectedItem.getID(), getTransactionType(),
					getTransactionStatusType(), startDate, endDate, start,
					length,
					new AsyncCallback<PaginationList<TransactionHistory>>() {

						@Override
						public void onFailure(Throwable caught) {
							Accounter.showError(messages
									.unableToPerformTryAfterSomeTime());
						}

						@Override
						public void onSuccess(
								PaginationList<TransactionHistory> result) {
							records = result;
							selectedItem = getCompany().getItem(
									selectedItem.getID());
							transactionHistoryGrid
									.setSelectedItem(selectedItem);
							itemDetailsPanel.showItemDetails(selectedItem);
							transactionHistoryGrid.removeAllRecords();
							if (records != null) {
								transactionHistoryGrid.addRecords(records);
							}
							updateRecordsCount(result.getStart(),
									result.size(), result.getTotalCount());
							if (records.size() == 0) {
								transactionHistoryGrid.addEmptyMessage(messages
										.thereAreNo(messages.transactions()));
							}
						}
					});

		} else {
			transactionHistoryGrid.removeAllRecords();
			transactionHistoryGrid.addEmptyMessage(messages.thereAreNo(messages
					.transactions()));
		}
	}

	private int getTransactionType() {
		String selectedValue = trasactionViewSelect.getSelectedValue();
		if (selectedValue.equalsIgnoreCase(messages.invoices())) {
			return ClientTransaction.TYPE_INVOICE;
		} else if (selectedValue.equalsIgnoreCase(messages.cashSales())) {
			return ClientTransaction.TYPE_CASH_SALES;
		} else if (selectedValue.equalsIgnoreCase(messages
				.payeeCreditNotes(Global.get().Customer()))) {
			return ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO;
		} else if (selectedValue.equalsIgnoreCase(messages.quotes())
				|| selectedValue.equalsIgnoreCase(messages.credits())
				|| selectedValue.equalsIgnoreCase(messages.Charges())) {
			return ClientTransaction.TYPE_ESTIMATE;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.cashPurchases())) {
			return ClientTransaction.TYPE_CASH_PURCHASE;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.bills())) {
			return ClientTransaction.TYPE_ENTER_BILL;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.payeeCreditNotes(Global.get().Vendor()))) {
			return ClientTransaction.TYPE_VENDOR_CREDIT_MEMO;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.expenses())) {
			return ClientTransaction.TYPE_EXPENSE;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.salesOrders())) {
			return TransactionHistory.TYPE_SALES_ORDER;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.purchaseOrders())) {
			return TransactionHistory.TYPE_PURCHASE_ORDER;
		}
		return 0;

	}

	private int getTransactionStatusType() {
		if (trasactionViewTypeSelect.getSelectedValue() != null) {
			Set<Integer> keySet = transactiontypebyStatusMap.keySet();
			for (Integer integerKey : keySet) {
				String entrystring = transactiontypebyStatusMap.get(integerKey);
				if (trasactionViewTypeSelect.getSelectedValue().equals(
						entrystring)) {
					return integerKey;
				}
			}
		}
		return TransactionHistory.ALL_INVOICES;

	}

	public String getAddNewLabelString() {
		return messages.addaNew(messages.inventoryItem());
	}

	public NewItemAction getAddNewAction() {
		if (!Accounter.getUser().canDoInvoiceTransactions())
			return null;
		else {
			NewItemAction action = new NewItemAction(true);
			action.setType(ClientItem.TYPE_INVENTORY_PART);
			return action;
		}
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("activeInActive", activeInActiveSelect.getSelectedValue());
		map.put("currentView", trasactionViewSelect.getSelectedValue());
		map.put("transactionType", trasactionViewTypeSelect.getSelectedValue());
		map.put("dateRange", dateRangeSelector.getSelectedValue());
		map.put("selectedItem",
				selectedItem == null ? "" : selectedItem.getName());
		map.put("itemSelection", itemsListGrid.getSelection());
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> map) {

		if (map == null || map.isEmpty()) {
			return;
		}
		String activeInactive = (String) map.get("activeInActive");
		activeInActiveSelect.setComboItem(activeInactive);
		if (activeInactive.equalsIgnoreCase(messages.active())) {
			refreshActiveinactiveList(true);
		} else {
			refreshActiveinactiveList(false);
		}

		String currentView = (String) map.get("currentView");
		trasactionViewSelect.setComboItem(currentView);
		if (currentView != null) {
			getMessagesList();
		}

		String transctionType = (String) map.get("transactionType");
		trasactionViewTypeSelect.setComboItem(transctionType);

		String dateRange1 = (String) map.get("dateRange");
		dateRangeSelector.setComboItem(dateRange1);
		if (dateRange1 != null) {
			dateRangeChanged(dateRange1);
		}
		ClientItem object = (ClientItem) map.get("itemSelection");
		itemsListGrid.setSelection(object);

		String customer = (String) map.get("selectedItem");

		if (customer != null && !(customer.isEmpty())) {
			selectedItem = getCompany().getItemByName(customer);
		}
		if (this.selectedItem != null) {
			itemsListGrid.setSelectedItem(selectedItem);

			selectedItem = Accounter.getCompany().getItem(selectedItem.getID());

			onItemSelected();
		} else {
			callRPC(0, getPageSize());
		}

	}

	@Override
	public boolean canEdit() {
		return selectedItem == null ? false : Utility
				.isUserHavePermissions(selectedItem.getObjectType());
	}

	@Override
	public void onEdit() {
		if (selectedItem.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
			InventoryActions inventoryAssemblyAction = InventoryActions
					.newAssembly();
			inventoryAssemblyAction.setisItemEditable(true);
			inventoryAssemblyAction.run((ClientInventoryAssembly) selectedItem,
					false);

		} else {
			NewItemAction itemAction = new NewItemAction(true);
			itemAction.setType(selectedItem.getType());
			itemAction.setisItemEditable(true);
			itemAction.run(selectedItem, false);
		}
	}

	@Override
	public boolean isDirty() {
		return false;
	}
}
