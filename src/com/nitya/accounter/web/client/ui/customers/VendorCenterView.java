package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Lists.PayeeList;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.ImageButton;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.ActionFactory;
import com.nitya.accounter.web.client.ui.core.ButtonGroup;
import com.nitya.accounter.web.client.ui.core.IButtonContainer;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.grids.VendorSelectionListener;
import com.nitya.accounter.web.client.ui.grids.VendorTransactionsHistoryGrid;
import com.nitya.accounter.web.client.ui.grids.VendorsListGrid;
import com.nitya.accounter.web.client.ui.vendors.NewVendorAction;

public class VendorCenterView<T> extends AbstractPayeeCenterView<ClientVendor>
		implements IButtonContainer {

	private static final int TYPE_CASH_PURCHASE = 2;
	private static final int TYPE_ENTER_BILL = 6;
	private static final int TYPE_PAY_BILL = 11;
	private static final int TYPE_WRITE_CHECK = 15;
	private static final int TYPE_PURCHASE_ORDER = 22;
	private static final int TYPE_ALL_TRANSACTION = 100;
	private static final int TYPE_VENDOR_CREDIT_MEMO = 14;
	private static final int TYPE_EXPENSE = 18;

	private ClientVendor selectedVendor;
	private ArrayList<PayeeList> listOfVendors;
	protected ArrayList<ClientFinanceDate> startEndDates;
	private ArrayList<TransactionHistory> records;

	private VendorDetailsPanel detailsPanel;
	private VendorsListGrid vendorlistGrid;
	private SelectCombo activeInActiveSelect, trasactionViewSelect,
			trasactionViewTypeSelect;
	private StyledPanel transactionGridpanel;
	private VendorTransactionsHistoryGrid vendHistoryGrid;
	private HashMap<Integer, String> transactiontypebyStatusMap;
	private boolean isActiveAccounts = true;
	private StyledPanel deleteButtonPanel;
	private StyledPanel rightVpPanel, dummyPanel;
	private Button transactionButton;

	public VendorCenterView() {
		this.getElement().setId("vendorCenterView");
	}

	@Override
	public void onEdit() {
		NewVendorAction newVendorAction = new NewVendorAction();
		newVendorAction.setisVendorViewEditable(true);
		newVendorAction.run(selectedVendor, false);
	}

	@Override
	public void init() {
		super.init();
		creatControls();
	}

	private void creatControls() {
		StyledPanel mainPanel = new StyledPanel("mainPanel");
		StyledPanel leftVpPanel = new StyledPanel("leftVpPanel");
		viewTypeCombo();
		DynamicForm viewform = new DynamicForm("viewform");
		viewform.add(activeInActiveSelect);
		leftVpPanel.add(viewform);
		vendorlistGrid = new VendorsListGrid();
		vendorlistGrid.init();
		initVendorListGrid();
		leftVpPanel.add(vendorlistGrid);
		vendorlistGrid.setStyleName("cusotmerCentrGrid");

		rightVpPanel = new StyledPanel("rightVpPanel");
		dummyPanel = new StyledPanel("dummyPanel");
		detailsPanel = new VendorDetailsPanel(selectedVendor);
		rightVpPanel.add(detailsPanel);
		vendorlistGrid
				.setVendorSelectionListener(new VendorSelectionListener() {

					@Override
					public void vendorSelected() {
						onVendorSelected();

					}
				});
		transactionViewSelectCombo();
		transactionViewTypeSelectCombo();
		transactionDateRangeSelector();
		DynamicForm transactionViewform = new DynamicForm("transactionViewform");

		transactionViewform.add(trasactionViewSelect, trasactionViewTypeSelect,
				dateRangeSelector);

		transactionGridpanel = new StyledPanel("transactionGridpanel");
		transactionGridpanel.add(transactionViewform);
		vendHistoryGrid = new VendorTransactionsHistoryGrid() {
			@Override
			public void initListData() {

				onVendorSelected();
			}
		};
		vendHistoryGrid.init();
		vendHistoryGrid.addEmptyMessage(messages.pleaseSelectAnyPayee(Global
				.get().Vendor()));
		int pageSize = getPageSize();
		vendHistoryGrid.addRangeChangeHandler2(new Handler() {

			@Override
			public void onRangeChange(RangeChangeEvent event) {
				onPageChange(event.getNewRange().getStart(), event
						.getNewRange().getLength());
			}

		});
		SimplePager pager = new SimplePager(TextLocation.CENTER,
				(Resources) GWT.create(Resources.class), false, pageSize * 2,
				true);
		pager.setDisplay(vendHistoryGrid);
		updateRecordsCount(0, 0, 0);
		if (Accounter.isIpadApp()) {

			transactionButton = new Button(messages.transaction());
			transactionButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ActionFactory.getVendorTransactionListHistory(
							selectedVendor).run();
				}
			});

		} else {
			dummyPanel.add(transactionGridpanel);
			dummyPanel.add(vendHistoryGrid);
			dummyPanel.add(pager);
		}

		rightVpPanel.add(dummyPanel);
		Label labelTitle = new Label(messages.customerCentre(Global.get()
				.Vendor()));
		labelTitle.setStyleName("label-title");
		mainPanel.add(leftVpPanel);
		mainPanel.add(rightVpPanel);
		deleteButtonPanel = new StyledPanel("deleteButtonPanel");
		add(labelTitle);
		add(deleteButtonPanel);
		add(mainPanel);

	}

	public void updateRecordsCount(int start, int length, int total) {
		vendHistoryGrid.updateRange(new Range(start, getPageSize()));
		vendHistoryGrid.setRowCount(total, (start + length) == total);
	}

	private void initVendorListGrid() {
		Accounter.createHomeService().getPayeeList(ClientPayee.TYPE_VENDOR,
				isActiveAccounts, 0, -1, true,
				new AsyncCallback<PaginationList<PayeeList>>() {

					@Override
					public void onSuccess(PaginationList<PayeeList> result) {
						vendorlistGrid.removeAllRecords();
						if (result.size() == 0) {
							vendorlistGrid.addEmptyMessage(messages
									.youDontHaveAny(Global.get().Vendors()));
						} else {
							vendorlistGrid.setRecords(result);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
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
		vendorlistGrid.setSelectedVendor(null);
		detailsPanel.vendName.setText(messages.noPayeeSelected(Global.get()
				.Vendor()));
		this.selectedVendor = null;
		onVendorSelected();
		isActiveAccounts = isActiveList;
		initVendorListGrid();

	}

	private void transactionViewSelectCombo() {
		if (trasactionViewSelect == null) {
			trasactionViewSelect = new SelectCombo(messages.currentView());

			ArrayList<String> transactionTypeList = new ArrayList<String>();
			transactionTypeList.add(messages.allTransactions());
			transactionTypeList.add(messages.cashPurchases());
			if (getPreferences().isKeepTrackofBills()) {
				transactionTypeList.add(messages.bills());
			}
			transactionTypeList.add(messages.payBills());
			transactionTypeList.add(messages.cheques());
			transactionTypeList.add(messages.payeeCreditNotes(Global.get()
					.Vendor()));
			transactionTypeList.add(messages.expenses());
			if (getPreferences().isPurchaseOrderEnabled()) {
				transactionTypeList.add(messages.purchaseOrders());
			}
			trasactionViewSelect.initCombo(transactionTypeList);
			trasactionViewSelect.setComboItem(messages.allTransactions());
			trasactionViewSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (trasactionViewSelect.getSelectedValue() != null) {
								getMessagesList();
								callRPC(0, getPageSize());
							}

						}

					});
		}

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
								callRPC(0, getPageSize());
							}

						}

					});
		}

	}

	private void getMessagesList() {
		transactiontypebyStatusMap = new HashMap<Integer, String>();
		if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.allTransactions())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_TRANSACTIONS,
					messages.allTransactions());
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.cashPurchases())) {
			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_CASH_PURCHASES,
					messages.allCashPurchases());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(
						TransactionHistory.DRAFT_CASH_PURCHASES,
						messages.draftTransaction(messages.cashPurchases()));
			}
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.bills())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_BILLS,
					messages.allBills());
			transactiontypebyStatusMap.put(TransactionHistory.OPEND_BILLS,
					messages.all() + " " + messages.openedBills());
			transactiontypebyStatusMap.put(TransactionHistory.OVERDUE_BILLS,
					messages.all() + " " + messages.overDueBills());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(TransactionHistory.DRAFT_BILLS,
						messages.draftTransaction(messages.bills()));
			}

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.payBills())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_PAYBILLS,
					messages.all() + " " + messages.payBills());
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.cheques())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CHEQUES,
					messages.allcheques());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(
						TransactionHistory.DRAFT_CHEQUES,
						messages.draftTransaction(messages.cheques()));
			}
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.payeeCreditNotes(Global.get().Vendor()))) {
			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_VENDOR_CREDITNOTES,
					messages.all() + " "
							+ messages.payeeCreditNotes(Global.get().Vendor()));
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(
						TransactionHistory.DRAFT_VENDOR_CREDITNOTES,
						messages.draftTransaction(messages
								.payeeCreditNotes(Global.get().Vendor())));
			}

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.expenses())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_EXPENSES,
					messages.allExpenses());
			transactiontypebyStatusMap.put(
					TransactionHistory.CREDIT_CARD_EXPENSES,
					messages.creditCardExpenses());
			transactiontypebyStatusMap.put(TransactionHistory.CASH_EXPENSES,
					messages.cashExpenses());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap
						.put(TransactionHistory.DRAFT_CREDIT_CARD_EXPENSES,
								messages.draftTransaction(messages
										.creditCardExpenses()));
				transactiontypebyStatusMap.put(
						TransactionHistory.DRAFT_CASH_EXPENSES,
						messages.draftTransaction(messages.cashExpenses()));
			}

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.purchaseOrders())) {
			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_PURCHASE_ORDERS,
					messages.allPurchaseOrders());
			transactiontypebyStatusMap.put(
					TransactionHistory.OPEN_PURCHASE_ORDERS,
					messages.openPurchaseOrders());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(
						TransactionHistory.DRAFT_PURCHASE_ORDERS,
						messages.draftTransaction(messages.purchaseOrders()));

			}
		}
		ArrayList<String> typeList = new ArrayList<String>(
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

	private void onVendorSelected() {
		this.selectedVendor = vendorlistGrid.getSelectedVendor();
		if (Accounter.isIpadApp()) {
			rightVpPanel.add(transactionButton);
			transactionButton.setText(messages2
					.transactionListFor(selectedVendor.getDisplayName()));
		}
		detailsPanel.showVendorDetails(selectedVendor);
		vendHistoryGrid.setSelectedVendor(selectedVendor);
		MainFinanceWindow.getViewManager().updateButtons();
		showButtonBar();
		callRPC(0, getPageSize());
	}

	@Override
	protected String getViewTitle() {
		return messages.payees(Global.get().Vendor());
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		Iterator<PayeeList> iterator = listOfVendors.iterator();
		while (iterator.hasNext()) {
			PayeeList next = iterator.next();
			if (next.getID() == result.getID()) {
				iterator.remove();
			}
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	public void setSelectedVendor(ClientVendor selectedVendor) {
		this.selectedVendor = selectedVendor;
	}

	public ClientVendor getSelectedVendor() {
		return selectedVendor;
	}

	@Override
	protected void callRPC(int start, int length) {
		vendHistoryGrid.removeAllRecords();
		records = new ArrayList<TransactionHistory>();
		if (selectedVendor != null) {
			Accounter.createReportService().getVendorTransactionsList(
					selectedVendor.getID(), getTransactionType(),
					getTransactionStatusType(), getStartDate(), getEndDate(),
					start, length,
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
							vendHistoryGrid.removeAllRecords();
							if (records != null) {
								vendHistoryGrid.addRecords(records);
							}
							updateRecordsCount(result.getStart(),
									result.size(), result.getTotalCount());
							if (records.size() == 0) {
								vendHistoryGrid.addEmptyMessage(messages
										.thereAreNo(messages.transactions()));
							}
						}
					});

		} else {
			vendHistoryGrid.removeAllRecords();
			vendHistoryGrid.addEmptyMessage(messages.thereAreNo(messages
					.transactions()));
		}
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

	private int getTransactionType() {

		if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.cashPurchases())) {

			return TYPE_CASH_PURCHASE;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.bills())) {
			return TYPE_ENTER_BILL;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.payBills())) {
			return TYPE_PAY_BILL;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.cheques())) {
			return TYPE_WRITE_CHECK;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.payeeCreditNotes(Global.get().Vendor()))) {
			return TYPE_VENDOR_CREDIT_MEMO;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.expenses())) {
			return TYPE_EXPENSE;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.purchaseOrders())) {
			return TYPE_PURCHASE_ORDER;
		}
		return TYPE_ALL_TRANSACTION;

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

	// @Override
	// public void restoreView(ClientPayee vendor) {
	// this.selectedVendor = (ClientVendor) vendor;
	// if (this.selectedVendor != null) {
	// vendorlistGrid.setSelectedVendor(selectedVendor);
	// onVendorSelected();
	// }
	// }
	//
	// @Override
	// public ClientVendor saveView() {
	// return selectedVendor;
	// }

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		if (selectedVendor != null) {
			Accounter.createExportCSVService()
					.getVendorTransactionsListExportCsv(selectedVendor,
							getTransactionType(), getTransactionStatusType(),
							getStartDate(), getEndDate(),
							new AsyncCallback<String>() {

								@Override
								public void onSuccess(String id) {
									UIUtils.downloadFileFromTemp(
											trasactionViewSelect
													.getSelectedValue()
													+ " of "
													+ selectedVendor.getName()
													+ ".csv", id);
								}

								@Override
								public void onFailure(Throwable caught) {
									if (Accounter.isShutdown()) {
										Accounter.showError(messages
												.unableToPerformTryAfterSomeTime());
									}
								}
							});
		} else {
			Accounter.showError(messages.pleaseSelect(Global.get().vendor()));
		}
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("activeInActive", activeInActiveSelect.getSelectedValue());
		map.put("currentView", trasactionViewSelect.getSelectedValue());
		map.put("transactionType", trasactionViewTypeSelect.getSelectedValue());
		map.put("dateRange", dateRangeSelector.getSelectedValue());
		map.put("selectedCustomer", selectedVendor == null ? ""
				: selectedVendor.getName());
		PayeeList selection = vendorlistGrid.getSelection();
		map.put("payeeSelection", selection);
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
		PayeeList object = (PayeeList) map.get("payeeSelection");
		vendorlistGrid.setSelection(object);

		String customer = (String) map.get("selectedCustomer");

		if (customer != null && !(customer.isEmpty())) {
			selectedVendor = getCompany().getVendorByName(customer);
		}
		if (this.selectedVendor != null) {
			vendorlistGrid.setSelectedVendor(selectedVendor);
			onVendorSelected();
		} else {
			callRPC(0, getPageSize());
		}
	}

	@Override
	public boolean canEdit() {
		if (selectedVendor != null
				&& Accounter.getUser().isCanDoUserManagement()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void addButtons(ButtonGroup group) {
		ImageButton addVendorButton = null;

		if (Accounter.isIpadApp()) {
			addVendorButton = new ImageButton(Accounter.getFinanceImages()
					.ipadAdd(), "add");
		} else {
			addVendorButton = new ImageButton(messages.addNew(Global.get()
					.Vendor()), Accounter.getFinanceImages()
					.portletPageSettings(), "add");
		}

		addVendorButton.addStyleName("settingsButton");
		addVendorButton.getElement().setId("addVendorButton");
		addVendorButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getNewVendorAction().run();
			}
		});
		addVendorButton.getElement().setAttribute("data-icon", "add");
		addButton(group, addVendorButton);
	}

}
