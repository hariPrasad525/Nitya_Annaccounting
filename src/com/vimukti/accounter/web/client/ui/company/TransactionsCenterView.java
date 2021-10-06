package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.company.options.TreeListPanel;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.ISavableView;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundListView;
import com.vimukti.accounter.web.client.ui.customers.InvoiceListView;
import com.vimukti.accounter.web.client.ui.customers.QuoteListView;
import com.vimukti.accounter.web.client.ui.customers.ReceivedPaymentListView;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentsListView;
import com.vimukti.accounter.web.client.ui.vat.ChalanDetailsListView;
import com.vimukti.accounter.web.client.ui.vat.TaxAdjustmentsListView;
import com.vimukti.accounter.web.client.ui.vendors.BillListView;
import com.vimukti.accounter.web.client.ui.vendors.ExpensesListView;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderListView;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsListView;
import com.vimukti.accounter.web.client.util.Countries;

public class TransactionsCenterView<T> extends AbstractBaseView<T> implements
		IPrintableView, ISavableView<HashMap<String, Object>> {

	public TransactionsListView<T> baseListView;
	private StyledPanel mainPanel;
	private String selectedItem = null;
	private TreeListPanel listPanel;

	// private String selectedType;
	//
	// private String searchString;

	public TransactionsCenterView() {
		// setSize("100%", "100%");
		// mainPanel.setWidth("100");
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		mainPanel = new StyledPanel("mainPanel");
		mainPanel.setStyleName("Transactions_center");
		createTreeItems();
		this.add(mainPanel);
	}

	@SuppressWarnings("unchecked")
	private void createTreeItems() {
		listPanel = new TreeListPanel() {

			@Override
			protected void onMenuClick(String menuTitle) {
			}

			@Override
			protected void onMenuItemClick(String menuItemName) {
				if (Accounter.isIpadApp()) {
					ActionFactory.getTransactionListView(menuItemName).run();
				} else {
					initGridData(menuItemName);
					baseListView.initListCallback();
				}
			}
		};
		listPanel.getElement().setId("listPanel");
		mainPanel.add(listPanel);

		listPanel.addMenuPanel(Global.get().Customers(),
				getCustomerCenterItems());
		listPanel.addMenuPanel(Global.get().Vendors(), getVendorCenterItems());
		listPanel.addMenuPanel(messages.banking(), getBankCenterItems());
		listPanel.addMenuPanel(messages.others(), getOtherCenterItems());
		if (Accounter.getUser().canSeeInvoiceTransactions()
				&& getPreferences().isTrackTax()) {

			listPanel.addMenuPanel(messages.tax(), getTaxCenterItems());
		}
		if (Accounter.getUser().getPermissions().getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES
				&& getPreferences().isInventoryEnabled()) {
			listPanel.addMenuPanel(messages.inventory(),
					getInventoryCenterList());
		}

		baseListView = (TransactionsListView<T>) new InvoiceListView();

		initGridData(getMessages().invoices());
	}

	private List<String> getInventoryCenterList() {
		ArrayList<String> inventoryItems = new ArrayList<String>();
		inventoryItems.add(getMessages().buildAssembly());
		inventoryItems.add(getMessages().inventoryAdjustments());
		return inventoryItems;
	}

	private List<String> getTaxCenterItems() {
		ArrayList<String> taxItems = new ArrayList<String>();
		taxItems.add(getMessages().taxAdjustment());
		taxItems.add(getMessages().fileTAX());
		taxItems.add(getMessages().payTax());
		taxItems.add(getMessages().tAXRefund());
		if (getPreferences().getTradingAddress().getCountryOrRegion()
				.equals(Countries.INDIA)) {
			if (getPreferences().isTDSEnabled()) {
				taxItems.add(getMessages().challanDetails());
			}
		}
		return taxItems;
	}

	private ArrayList<String> getBankCenterItems() {
		ArrayList<String> bankItems = new ArrayList<String>();
		bankItems.add(getMessages().payments());
		bankItems.add(getMessages().deposits());
		bankItems.add(getMessages().transferFunds());
		return bankItems;
	}

	@SuppressWarnings("unchecked")
	protected void initGridData(String itemName) {
		selectedItem = itemName;
		baseListView.clear();
		mainPanel.remove(baseListView);
		// Customer Menu
		if (itemName.equalsIgnoreCase(messages.customerchecks())) {
			baseListView = (TransactionsListView<T>) new PaymentListView(
					PaymentListView.TYPE_CUSTOMER_CHECKS);

		} else if (itemName.equalsIgnoreCase(getMessages().cashSales())) {
			baseListView = (TransactionsListView<T>) new InvoiceListView(
					ClientTransaction.TYPE_CASH_SALES);

		} else if (itemName.equalsIgnoreCase(getMessages()
				.customerCreditNotes())) {
			baseListView = (TransactionsListView<T>) new InvoiceListView(
					ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);

		} else if (itemName.equalsIgnoreCase(getMessages().quotes())) {
			baseListView = (TransactionsListView<T>) new QuoteListView(
					ClientEstimate.QUOTES);

		} else if (itemName.equalsIgnoreCase(getMessages().Charges())) {
			baseListView = (TransactionsListView<T>) new QuoteListView(
					ClientEstimate.CHARGES);

		} else if (itemName.equalsIgnoreCase(getMessages().salesOrders())) {

			baseListView = (TransactionsListView<T>) new QuoteListView(
					ClientEstimate.SALES_ORDER);

		} else if (itemName.equalsIgnoreCase(getMessages().credits())) {
			baseListView = (TransactionsListView<T>) new QuoteListView(
					ClientEstimate.CREDITS);

		} else if (itemName.equalsIgnoreCase(getMessages().invoices())) {
			baseListView = (TransactionsListView<T>) new InvoiceListView(
					ClientTransaction.TYPE_INVOICE);
		} else if (itemName.equalsIgnoreCase(getMessages().receivedPayments())) {
			baseListView = (TransactionsListView<T>) new ReceivedPaymentListView(
					ClientTransaction.TYPE_RECEIVE_PAYMENT);

		} else if (itemName.equals(getMessages().payeePayments(
				Global.get().Customers()))) {
			baseListView = (TransactionsListView<T>) new ReceivedPaymentListView(
					ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);

		} else if (itemName.equalsIgnoreCase(getMessages().customerRefunds(
				Global.get().Customer()))) {
			baseListView = (TransactionsListView<T>) new CustomerRefundListView();

		} else if (itemName.equalsIgnoreCase(getMessages().billCredits())) {
			baseListView = (TransactionsListView<T>) new BillListView(
					getMessages().all(),
					ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);

		} else if (itemName.equalsIgnoreCase(getMessages().billPayments())) {
			baseListView = (TransactionsListView<T>) new VendorPaymentsListView(
					ClientTransaction.TYPE_VENDOR_PAYMENT);

		} else if (itemName.equalsIgnoreCase(getMessages().bills())) {
			baseListView = (TransactionsListView<T>) new BillListView(
					getMessages().all(), ClientTransaction.TYPE_ENTER_BILL);

		} else if (itemName.equalsIgnoreCase(messages.vendorchecks())) {
			baseListView = (TransactionsListView<T>) new PaymentListView(
					PaymentListView.TYPE_VENDOR_CHECKS);

		} else if (itemName
				.equalsIgnoreCase(getMessages().creditCardExpenses())) {
			baseListView = (TransactionsListView<T>) new ExpensesListView(
					getMessages().creditCard(),
					ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);

		} else if (itemName.equalsIgnoreCase(getMessages().cashExpenses())) {
			baseListView = (TransactionsListView<T>) new ExpensesListView(
					getMessages().cash(), ClientTransaction.TYPE_CASH_EXPENSE);

		} else if (itemName.equalsIgnoreCase(getMessages().cashPurchases())) {
			baseListView = (TransactionsListView<T>) new BillListView(
					getMessages().all(), ClientTransaction.TYPE_CASH_PURCHASE);

		} else if (itemName.equalsIgnoreCase(getMessages().deposits())) {
			baseListView = (TransactionsListView<T>) new DepositsTransfersListView(
					0);

		} else if (itemName.equalsIgnoreCase(getMessages()
				.inventoryAdjustments())) {
			baseListView = (TransactionsListView<T>) new StockAdjustmentsListView();

		} else if (itemName.equalsIgnoreCase(getMessages().journalEntries())) {
			baseListView = (TransactionsListView<T>) new JournalEntryListView();

		} else if (itemName.equalsIgnoreCase(getMessages().transferFunds())) {
			baseListView = (TransactionsListView<T>) new DepositsTransfersListView(
					1);

		} else if (itemName.equalsIgnoreCase(getMessages().payments())) {
			baseListView = (TransactionsListView<T>) new PaymentListView(
					PaymentListView.TYPE_ALL);
		} else if (itemName.equalsIgnoreCase(messages.otherChecks())) {
			baseListView = (TransactionsListView<T>) new PaymentListView(
					PaymentListView.TYPE_WRITE_CHECKS);
		} else if (itemName.equalsIgnoreCase(getMessages().purchaseOrders())) {
			baseListView = (TransactionsListView<T>) new PurchaseOrderListView();
		} else if (itemName.equalsIgnoreCase(getMessages().taxAdjustment())) {
			baseListView = (TransactionsListView<T>) new TaxAdjustmentsListView();
		} else if (itemName.equalsIgnoreCase(getMessages().payRuns())) {
			baseListView = (TransactionsListView<T>) new PaymentListView(
					PaymentListView.TYPE_PAY_RUNS);
		} else if (itemName.equalsIgnoreCase(getMessages().fileTAX())) {
			baseListView = (TransactionsListView<T>) new FileTAXListView();
		} else if (itemName.equalsIgnoreCase(getMessages().payTax())) {
			baseListView = (TransactionsListView<T>) new PayTaxListView();
		} else if (itemName.equalsIgnoreCase(getMessages().tAXRefund())) {
			baseListView = (TransactionsListView<T>) new TaxRefundListView();
		} else if (itemName.equalsIgnoreCase(getMessages().challanDetails())) {
			baseListView = (TransactionsListView<T>) new ChalanDetailsListView(
					true);
		} else if (itemName.equalsIgnoreCase(getMessages().buildAssembly())) {
			baseListView = (TransactionsListView<T>) new BuildAssembliesView();
		} else if (itemName.equalsIgnoreCase(getMessages2().payEmployees())) {
			baseListView = (TransactionsListView<T>) new PaymentListView(
					PaymentListView.TYPE_PAY_EMPLOYEES);
		}

		if (!Accounter.isIpadApp()) {
			mainPanel.add(baseListView);
		}
		MainFinanceWindow.getViewManager().updateButtons();
		baseListView.showButtonBar();
		baseListView.init();
		baseListView.removeStyleName("abstract_base_view");
		baseListView.setStatus(status);
	}

	@Override
	public void initData() {
		super.initData();
		baseListView.initData();
	}

	private ArrayList<String> getVendorCenterItems() {
		ArrayList<String> vendorItems = new ArrayList<String>();
		vendorItems.add(getMessages().billCredits());
		vendorItems.add(getMessages().billPayments());
		if (getPreferences().isKeepTrackofBills()) {
			vendorItems.add(getMessages().bills());
		}
		vendorItems.add(getMessages().vendorchecks());
		vendorItems.add(getMessages().creditCardExpenses());
		vendorItems.add(getMessages().cashExpenses());
		vendorItems.add(getMessages().cashPurchases());
		if (getPreferences().isPurchaseOrderEnabled()) {
			vendorItems.add(getMessages().purchaseOrders());
		}
		return vendorItems;
	}

	private AccounterMessages getMessages() {
		return messages;
	}

	private AccounterMessages2 getMessages2() {
		return messages2;
	}

	private ArrayList<String> getCustomerCenterItems() {
		ArrayList<String> customerItems = new ArrayList<String>();
		if (Accounter.getUser().canSeeInvoiceTransactions()) {
			customerItems.add(getMessages().invoices());
			customerItems.add(getMessages().customerchecks());
			customerItems.add(getMessages().customerCreditNotes());
			if (getPreferences().isDoyouwantEstimates()) {
				customerItems.add(getMessages().quotes());
			}

			if (getPreferences().isDelayedchargesEnabled()) {
				customerItems.add(getMessages().Charges());
				customerItems.add(getMessages().credits());
			}
			if (getPreferences().isSalesOrderEnabled()) {
				customerItems.add(getMessages().salesOrders());
			}
			customerItems.add(getMessages().cashSales());
		}

		if (Accounter.getUser().canSeeBanking()) {
			customerItems.add(getMessages().receivedPayments());
			customerItems.add(getMessages().payeePayments(
					Global.get().Customers()));
			customerItems.add(getMessages().customerRefunds(
					Global.get().Customer()));
		}
		return customerItems;
	}

	private ArrayList<String> getOtherCenterItems() {
		ArrayList<String> otherItems = new ArrayList<String>();
		// otherItems.add(getMessages().deposits());
		otherItems.add(getMessages().journalEntries());
		otherItems.add(getMessages().otherChecks());
		// if (Accounter.getUser().canSeeInvoiceTransactions()
		// && getPreferences().isTrackTax()) {
		// otherItems.add(getMessages().taxAdjustment());
		// }

		if (Accounter.hasPermission(Features.PAY_ROLL)) {
			otherItems.add(getMessages().payRuns());
			otherItems.add(getMessages2().payEmployees());
		}

		// otherItems.add(getMessages().transferFunds());
		return otherItems;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
	}

	@Override
	protected String getViewTitle() {
		return messages.transactionscenter();
	}

	@Override
	public void setFocus() {
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
		// int viewId = 0;
		// if (!selectedItem
		// .equalsIgnoreCase(getMessages().inventoryAdjustments())) {
		// viewId = checkViewType(baseListView.getViewType());
		// }
		baseListView.exportToCsv();
		// Accounter.createExportCSVService().getExportListCsv(
		// getStartDate().getDate(), getEndDate().getDate(),
		// transactionType, viewId, selectedItem,
		// baseListView.getExportCSVCallback(selectedItem));
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("currentView", baseListView.getViewType());
		// map.put("dateRange", baseListView.getDateRange());
		// map.put("startDate", baseListView.getStartDate());
		// map.put("endDate", baseListView.getEndDate());
		map.put("selectedItem", selectedItem);
		map.put("listView", baseListView.saveView());
		return map;

	}

	@Override
	public void restoreView(HashMap<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return;
		}
		String selectedItem = (String) map.get("selectedItem");
		if (selectedItem != null) {
			initGridData(selectedItem);
			listPanel.setMenuSelected(selectedItem);
		}
		// String currentView = (String) map.get("currentView");
		// baseListView.setViewType(currentView);
		// String dateRange1 = (String) map.get("dateRange");
		// baseListView.setDateRange(dateRange1);
		// ClientFinanceDate startDate1 = (ClientFinanceDate)
		// map.get("startDate");
		// baseListView.setStartDate(startDate1);
		// ClientFinanceDate endDate1 = (ClientFinanceDate) map.get("endDate");
		// baseListView.setEndDate(endDate1);
		HashMap<String, Object> listViewMap = (HashMap<String, Object>) map
				.get("listView");
		baseListView.restoreView(listViewMap);
	}

	// @Override
	// public void onSearch(String searchString) {
	// this.searchString = searchString;
	// }
	//
	// @Override
	// public void setSelectionType(String selectedType) {
	// this.selectedType = selectedType;
	// }
	//
	// private String getSelectedSearchType() {
	// return selectedType;
	// }
	//
	// private String getSearchString() {
	// return searchString;
	// }

}
