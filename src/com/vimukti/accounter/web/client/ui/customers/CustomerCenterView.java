package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ButtonGroup;
import com.vimukti.accounter.web.client.ui.core.IButtonContainer;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.CustomerSelectionListener;
import com.vimukti.accounter.web.client.ui.grids.CustomerTransactionsHistoryGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomersListGrid;

public class CustomerCenterView<T> extends
		AbstractPayeeCenterView<ClientCustomer> implements IPrintableView,
		IButtonContainer {
	private static final int TYPE_ESTIMATE = 7;
	private static final int TYPE_INVOICE = 8;
	private static final int TYPE_CAHSSALE = 1;
	private static final int TYPE_RECEIVE_PAYMENT = 12;
	private static final int TYPE_CREDITNOTE = 4;
	private static final int TYPE_CUSTOMER_REFUND = 5;
	private static final int TYPE_ALL_TRANSACTION = 100;
	private static final int TYPE_WRITE_CHECK = 15;
	private static final int TYPE_SALES_ORDER = 38;
	private ClientCustomer selectedCustomer;
	private List<PayeeList> listOfCustomers;
	private ArrayList<TransactionHistory> records;

	private CustomerDetailsPanel detailsPanel;
	private CustomersListGrid custGrid;
	private SelectCombo activeInActiveSelect, trasactionViewSelect,
			trasactionViewTypeSelect;
	private StyledPanel transactionGridpanel;
	private CustomerTransactionsHistoryGrid custHistoryGrid;
	private Map<Integer, String> transactiontypebyStatusMap;
	private boolean isActiveAccounts = true;
	private StyledPanel deleteButtonPanel;
	private Button transactionButton;
	private StyledPanel rightVpPanel, dummyPanel;

	public CustomerCenterView() {

	}

	@Override
	public void onEdit() {
		NewCustomerAction newCustomerAction = new NewCustomerAction();
		newCustomerAction.setisCustomerViewEditable(true);
		newCustomerAction.run(selectedCustomer, false);
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("CustomerCenterView");
		creatControls();

	}

	private void creatControls() {

		StyledPanel mainPanel = new StyledPanel("customerCenter");

		StyledPanel leftVpPanel = new StyledPanel("leftPanel");

		viewTypeCombo();
		DynamicForm viewform = new DynamicForm("viewform");
		viewform.setStyleName("filterPanel");
		viewform.add(activeInActiveSelect);
		leftVpPanel.add(viewform);
		custGrid = new CustomersListGrid();
		custGrid.init();
		initCustomersListGrid();
		leftVpPanel.add(custGrid);

		custGrid.setStyleName("cusotmerCentrGrid");

		rightVpPanel = new StyledPanel("rightPanel");
		dummyPanel = new StyledPanel("dummyPanel");

		detailsPanel = new CustomerDetailsPanel(selectedCustomer);
		rightVpPanel.add(detailsPanel);
		custGrid.setCustomerSelectionListener(new CustomerSelectionListener() {
			@Override
			public void cusotmerSelected() {
				OncusotmerSelected();
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
		custHistoryGrid = new CustomerTransactionsHistoryGrid() {
			@Override
			public void initListData() {
				OncusotmerSelected();
			}
		};
		custHistoryGrid.init();
		custHistoryGrid.addEmptyMessage(messages.pleaseSelectAnyPayee(Global
				.get().Customer()));
		int pageSize = getPageSize();
		custHistoryGrid.addRangeChangeHandler2(new Handler() {

			@Override
			public void onRangeChange(RangeChangeEvent event) {
				onPageChange(event.getNewRange().getStart(), event
						.getNewRange().getLength());
			}
		});
		SimplePager pager = new SimplePager(TextLocation.CENTER,
				(Resources) GWT.create(Resources.class), false, pageSize * 2,
				true);
		pager.setDisplay(custHistoryGrid);
		updateRecordsCount(0, 0, 0);

		if (Accounter.isIpadApp()) {

			transactionButton = new Button(messages.transaction());
			transactionButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ActionFactory.getCustomerTransactionListHistory(
							selectedCustomer).run();
				}
			});

		} else {
			dummyPanel.add(transactionGridpanel);
			dummyPanel.add(custHistoryGrid);
			dummyPanel.add(pager);
		}
		rightVpPanel.add(dummyPanel);
		Label labelTitle = new Label(messages.customerCentre(Global.get()
				.Customer()));
		labelTitle.setStyleName("label-title");

		mainPanel.add(leftVpPanel);
		mainPanel.add(rightVpPanel);
		deleteButtonPanel = new StyledPanel("deleteButtonPanel");
		add(labelTitle);
		add(deleteButtonPanel);
		add(mainPanel);

	}

	public void updateRecordsCount(int start, int length, int total) {
		custHistoryGrid.updateRange(new Range(start, getPageSize()));
		custHistoryGrid.setRowCount(total, (start + length) == total);
	}

	private void viewTypeCombo() {
		if (activeInActiveSelect == null) {
			activeInActiveSelect = new SelectCombo(messages.show());

			List<String> activetypeList = new ArrayList<String>();
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
									refreshActiveinActiveList(true);
								} else {
									refreshActiveinActiveList(false);

								}

							}
						}

					});
		}
	}

	private void refreshActiveinActiveList(boolean isActivelist) {
		custGrid.setSelectedCustomer(null);
		// detailsPanel.custname.setText(messages.noPayeeSelected(Global.get()
		// .Customer()));
		this.selectedCustomer = null;
		OncusotmerSelected();
		isActiveAccounts = isActivelist;
		initCustomersListGrid();
	}

	private void transactionViewSelectCombo() {
		if (trasactionViewSelect == null) {
			trasactionViewSelect = new SelectCombo(messages.currentView());

			List<String> transactionTypeList = new ArrayList<String>();
			transactionTypeList.add(messages.allTransactions());
			transactionTypeList.add(messages.invoices());
			if (getPreferences().isDoyouwantEstimates()) {
				transactionTypeList.add(messages.quotes());
			}
			if (getCompany().getPreferences().isDelayedchargesEnabled()) {
				transactionTypeList.add(messages.Charges());
				transactionTypeList.add(messages.credits());
			}
			transactionTypeList.add(messages.cashSales());
			transactionTypeList.add(messages.receivedPayments());
			transactionTypeList.add(messages.CustomerCreditNotes());
			transactionTypeList.add(messages.customerRefunds(Global.get()
					.Customer()));
			transactionTypeList.add(messages.cheques());
			if (getPreferences().isSalesOrderEnabled()) {
				transactionTypeList.add(messages.salesOrders());
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
		String selectedValue = trasactionViewSelect.getSelectedValue();
		if (selectedValue.equalsIgnoreCase(messages.allTransactions())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_TRANSACTIONS,
					messages.allTransactions());
		} else if (selectedValue.equalsIgnoreCase(messages.invoices())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_INVOICES,
					messages.getallInvoices());
			transactiontypebyStatusMap.put(TransactionHistory.OPENED_INVOICES,
					messages.getOpendInvoices());
			transactiontypebyStatusMap.put(
					TransactionHistory.OVER_DUE_INVOICES,
					messages.overDueInvoices());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(
						TransactionHistory.DRAFT_INVOICES,
						messages.draftTransaction(messages.invoices()));
			}
		} else if (selectedValue.equalsIgnoreCase(messages.cashSales())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CASHSALES,
					messages.all() + " " + messages.cashSales());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(
						TransactionHistory.DRAFT_CASHSALES,
						messages.draftTransaction(messages.cashSales()));
			}

		} else if (selectedValue.equalsIgnoreCase(messages.quotes())) {

			transactiontypebyStatusMap.put(TransactionHistory.ALL_QUOTES,
					messages.allQuotes());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(TransactionHistory.DRAFT_QUOTES,
						messages.draftTransaction(messages.quotes()));
			}
		} else if (selectedValue.equalsIgnoreCase(messages.credits())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CREDITS,
					messages.allCredits());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(
						TransactionHistory.DRAFT_CREDITS,
						messages.draftTransaction(messages.credits()));
			}

		} else if (selectedValue.equalsIgnoreCase(messages.Charges())) {

			transactiontypebyStatusMap.put(TransactionHistory.ALL_CHARGES,
					messages.allCahrges());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(
						TransactionHistory.DRAFT_CHARGES,
						messages.draftTransaction(messages.Charges()));
			}

		} else if (selectedValue.equalsIgnoreCase(messages.receivedPayments())) {
			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_RECEIVEDPAYMENTS, messages.all()
							+ " " + messages.receivedPayments());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_CASH,
					messages.receivedPaymentsbyCash());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_CHEQUE,
					messages.receivedPaymentsbyCheque());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_CREDITCARD,
					messages.receivedPaymentsbyCreditCard());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_DIRECT_DEBIT,
					messages.receivedPaymentsbyDirectDebit());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_MASTERCARD,
					messages.receivedPaymentsbyMastercard());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_ONLINE,
					messages.receivedPaymentsbyOnlineBanking());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_STANDING_ORDER,
					messages.receivedPaymentsbyStandingOrder());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_MAESTRO,
					messages.receivedPaymentsbySwitchMaestro());

		} else if (selectedValue.equalsIgnoreCase(messages
				.CustomerCreditNotes())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CREDITMEMOS,
					messages.allCreditMemos());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(
						TransactionHistory.DRAFT_CREDITMEMOS,
						messages.draftTransaction(messages.creditNote()));
			}
			// transactiontypebyStatusMap.put(
			// TransactionHistory.OPEND_CREDITMEMOS,
			// messages.openCreditMemos());

		} else if (selectedValue.equalsIgnoreCase(messages
				.customerRefunds(Global.get().Customer()))) {
			transactiontypebyStatusMap.put(
					TransactionHistory.REFUNDS_BY_CREDITCARD,
					messages.refundsByCreditCard());
			transactiontypebyStatusMap.put(TransactionHistory.REFUNDS_BYCASH,
					messages.refundsByCash());
			transactiontypebyStatusMap.put(TransactionHistory.REFUNDS_BYCHEQUE,
					messages.refundsByCheck());

			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_CUSTOMER_REFUNDS,
					messages.allCustomerRefunds());
			if (Accounter.hasPermission(Features.DRAFTS)) {
				transactiontypebyStatusMap.put(
						TransactionHistory.DRAFT_CUSTOMER_REFUNDS, messages
								.draftTransaction(messages
										.customerRefunds(Global.get()
												.Customer())));
			}
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
				messages.salesOrders())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_SALES_ORDERS,
					messages.all());
			transactiontypebyStatusMap.put(
					TransactionHistory.COMPLETED_SALES_ORDERS,
					messages.completed());
			transactiontypebyStatusMap.put(
					TransactionHistory.OPEN_SALES_ORDERS, messages.open());

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

	private void initCustomersListGrid() {
		Accounter.createHomeService().getPayeeList(ClientPayee.TYPE_CUSTOMER,
				isActiveAccounts, 0, -1, true,
				new AsyncCallback<PaginationList<PayeeList>>() {

					@Override
					public void onSuccess(PaginationList<PayeeList> result) {
						custGrid.removeAllRecords();
						if (result.size() == 0) {
							custGrid.addEmptyMessage(messages
									.youDontHaveAny(Global.get().Customers()));
						} else {
							custGrid.setRecords(result);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}
				});
	}

	private void OncusotmerSelected() {

		this.selectedCustomer = custGrid.getSelectedCustomer();
		if (Accounter.isIpadApp()) {
			rightVpPanel.add(transactionButton);
			transactionButton.setText(messages2
					.transactionListFor(selectedCustomer.getDisplayName()));
		}
		detailsPanel.showCustomerDetails(selectedCustomer);
		custHistoryGrid.setSelectedCustomer(selectedCustomer);
		MainFinanceWindow.getViewManager().updateButtons();
		showButtonBar();
		callRPC(0, getPageSize());
	}

	@Override
	public void onClose() {
		Accounter.showError("");
		super.onClose();

	}

	@Override
	protected String getViewTitle() {
		return messages.payees(Global.get().Customer());
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		Iterator<PayeeList> iterator = listOfCustomers.iterator();
		while (iterator.hasNext()) {
			PayeeList next = iterator.next();
			if (next.getID() == result.getID()) {
				iterator.remove();
			}
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	public void setSelectedCustomer(ClientCustomer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public ClientCustomer getSelectedCustomer() {
		return selectedCustomer;
	}

	@Override
	protected void callRPC(int start, int length) {
		custHistoryGrid.removeAllRecords();
		records = new ArrayList<TransactionHistory>();
		if (selectedCustomer != null) {
			Accounter.createReportService().getCustomerTransactionsList(
					selectedCustomer.getID(), getTransactionType(),
					getTransactionStatusType(), getStartDate(), getEndDate(),
					start, length,
					new AsyncCallback<PaginationList<TransactionHistory>>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}

						@Override
						public void onSuccess(
								PaginationList<TransactionHistory> result) {
							records = result;
							custHistoryGrid.removeAllRecords();
							if (records != null) {
								custHistoryGrid.addRecords(records);
							}
							updateRecordsCount(result.getStart(),
									result.size(), result.getTotalCount());
							if (records.size() == 0) {
								custHistoryGrid.addEmptyMessage(messages
										.thereAreNo(messages.transactions()));
							}

						}
					});

		} else {
			custHistoryGrid.removeAllRecords();
			custHistoryGrid.addEmptyMessage(messages.thereAreNo(messages
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
		String selectedValue = trasactionViewSelect.getSelectedValue();
		if (selectedValue.equalsIgnoreCase(messages.invoices())) {

			return TYPE_INVOICE;
		} else if (selectedValue.equalsIgnoreCase(messages.cashSales())) {
			return TYPE_CAHSSALE;
		} else if (selectedValue.equalsIgnoreCase(messages.receivedPayments())) {
			return TYPE_RECEIVE_PAYMENT;
		} else if (selectedValue.equalsIgnoreCase(messages
				.CustomerCreditNotes())) {
			return TYPE_CREDITNOTE;
		} else if (selectedValue.equalsIgnoreCase(messages.quotes())
				|| selectedValue.equalsIgnoreCase(messages.credits())
				|| selectedValue.equalsIgnoreCase(messages.Charges())) {
			return TYPE_ESTIMATE;
		} else if (selectedValue.equalsIgnoreCase(messages
				.customerRefunds(Global.get().Customer()))) {
			return TYPE_CUSTOMER_REFUND;
		} else if (selectedValue.equalsIgnoreCase(messages.cheques())) {
			return TYPE_WRITE_CHECK;
		} else if (selectedValue.equalsIgnoreCase(messages.salesOrders())) {
			return TYPE_SALES_ORDER;
		}
		return TYPE_ALL_TRANSACTION;

	}

	@Override
	public void restoreView(HashMap<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return;
		}
		String activeInactive = (String) map.get("activeInActive");
		activeInActiveSelect.setComboItem(activeInactive);
		if (activeInactive.equalsIgnoreCase(messages.active())) {
			refreshActiveinActiveList(true);
		} else {
			refreshActiveinActiveList(false);

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
		custGrid.setSelection(object);

		String customer = (String) map.get("selectedCustomer");

		if (customer != null && !(customer.isEmpty())) {
			selectedCustomer = getCompany().getCustomerByName(customer);
		}
		if (this.selectedCustomer != null) {
			custGrid.setSelectedCustomer(selectedCustomer);
			OncusotmerSelected();
		} else {
			callRPC(0, getPageSize());
		}
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("activeInActive", activeInActiveSelect.getSelectedValue());
		map.put("currentView", trasactionViewSelect.getSelectedValue());
		map.put("transactionType", trasactionViewTypeSelect.getSelectedValue());
		map.put("dateRange", dateRangeSelector.getSelectedValue());
		map.put("selectedCustomer", selectedCustomer == null ? ""
				: selectedCustomer.getName());
		PayeeList selection = custGrid.getSelection();
		map.put("payeeSelection", selection);
		return map;
	}

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
		if (selectedCustomer != null) {
			Accounter.createExportCSVService()
					.getCustomerTransactionsListExportCsv(selectedCustomer,
							getTransactionType(), getTransactionStatusType(),
							getStartDate(), getEndDate(),
							new AsyncCallback<String>() {

								@Override
								public void onSuccess(String id) {
									UIUtils.downloadFileFromTemp(
											trasactionViewSelect
													.getSelectedValue()
													+ " of "
													+ selectedCustomer
															.getName() + ".csv",
											id);
								}

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}
							});
		} else {
			Accounter.showError(messages.pleaseSelect(Global.get().Customer()));
		}
	}

	@Override
	public boolean canEdit() {
		if (selectedCustomer != null
				&& Accounter.getUser().isCanDoUserManagement()) {
			return true;
		}
		return false;
	}

	public boolean isDirty() {
		return false;
	}

	@Override
	public void addButtons(ButtonGroup group) {

		ImageButton addCustomerButton = null;
		if (Accounter.isIpadApp()) {
			addCustomerButton = new ImageButton(Accounter.getFinanceImages()
					.ipadAdd(), "add");
		} else {
			addCustomerButton = new ImageButton(messages.addNew(Global.get()
					.Customer()), Accounter.getFinanceImages()
					.portletPageSettings(), "add");
		}

		addCustomerButton.addStyleName("settingsButton");
		addCustomerButton.getElement().setId("addCustomerButton");
		addCustomerButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getNewCustomerAction().run();
			}
		});

		addCustomerButton.getElement().setAttribute("data-icon", "add");
		addButton(group, addCustomerButton);

	}

}
