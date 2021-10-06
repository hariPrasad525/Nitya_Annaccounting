package com.nitya.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.AbstractBaseView;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.ISavableView;
import com.nitya.accounter.web.client.ui.core.TransactionsListView;
import com.nitya.accounter.web.client.ui.customers.CustomerRefundListView;
import com.nitya.accounter.web.client.ui.customers.InvoiceListView;
import com.nitya.accounter.web.client.ui.customers.QuoteListView;
import com.nitya.accounter.web.client.ui.customers.ReceivedPaymentListView;
import com.nitya.accounter.web.client.ui.settings.StockAdjustmentsListView;
import com.nitya.accounter.web.client.ui.vat.TaxAdjustmentsListView;
import com.nitya.accounter.web.client.ui.vendors.BillListView;
import com.nitya.accounter.web.client.ui.vendors.ExpensesListView;
import com.nitya.accounter.web.client.ui.vendors.PurchaseOrderListView;
import com.nitya.accounter.web.client.ui.vendors.VendorPaymentsListView;

public class TransactionListGrids<T>  extends AbstractBaseView<T> implements
IPrintableView, ISavableView<Map<String, Object>>{

	public TransactionsListView<T> baseListView;
	private String selectedItem;


	public TransactionListGrids() {
		
	}

	@Override
	public void init() {
		createTreeItems();
	}
	
	@SuppressWarnings("unchecked")
	private void createTreeItems() {
		baseListView = (TransactionsListView<T>) new InvoiceListView();
		initGridData(selectedItem, false);
	}


	@SuppressWarnings("unchecked")
	protected void initGridData(String itemName, boolean isRestoreView) {
		baseListView.clear();
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
					getMessages().all());

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
		}

		this.add(baseListView);
		MainFinanceWindow.getViewManager().updateButtons();
		baseListView.init();
		if (!isRestoreView) {
			baseListView.initData();
		}
		baseListView.removeStyleName("abstract_base_view");
	}

	private List<String> getVendorCenterItems() {
		List<String> vendorItems = new ArrayList<String>();
		vendorItems.add(getMessages().billCredits());
		vendorItems.add(getMessages().billPayments());
		if (getPreferences().isKeepTrackofBills()) {
			vendorItems.add(getMessages().bills());
		}
		vendorItems.add(getMessages().vendorchecks());
		vendorItems.add(getMessages().creditCardExpenses());
		vendorItems.add(getMessages().cashExpenses());
		if (getPreferences().isPurchaseOrderEnabled()) {
			vendorItems.add(getMessages().purchaseOrders());
		}
		return vendorItems;
	}

	private AccounterMessages getMessages() {
		return messages;
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
		baseListView.exportToCsv();
	}

	@Override
	public Map<String, Object> saveView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {
	}

	public void setSelectedView(String type) {
		selectedItem = type;
	}


}
