package com.vimukti.accounter.web.client.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.ui.banking.ReconciliationsListAction;
import com.vimukti.accounter.web.client.ui.banking.WriteChecksAction;
import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.company.CustomersAction;
import com.vimukti.accounter.web.client.ui.company.NewJournalEntryAction;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.customers.NewInvoiceAction;
import com.vimukti.accounter.web.client.ui.customers.NewQuoteAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivePaymentAction;
import com.vimukti.accounter.web.client.ui.reports.ReportsHomeAction;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.client.ui.vendors.BillsAction;
import com.vimukti.accounter.web.client.ui.vendors.CashExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.EnterBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsListAction;

public class QuickLinksPortlet extends Portlet {

	private Map<String, String> quickLinksMap;

	public QuickLinksPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.quickLinks(), "", "100%");
		this.getElement().setId("QuickLinksPortlet");
	}

	@Override
	public void createBody() {
		quickLinksMap = new HashMap<String, String>();

		FlexTable linksTable = new FlexTable();
		linksTable.addStyleName("quick-links-table");
		// initializing links...
		quickLinksMap.put(messages.newInvoice(),
				new NewInvoiceAction().getHistoryToken());
		quickLinksMap.put(messages.expenses(),
				new ExpensesAction("").getHistoryToken());
		quickLinksMap.put(messages.payees(Global.get().Customers()),
				new CustomersAction().getHistoryToken());
		quickLinksMap.put(messages.banking(), new ChartOfAccountsAction(
				ClientAccount.TYPE_BANK).getHistoryToken());
		quickLinksMap.put(messages.reports(),
				new ReportsHomeAction().getHistoryToken());
		quickLinksMap
				.put(messages.bills(), new BillsAction().getHistoryToken());
		quickLinksMap.put(messages.cashExpense(),
				new CashExpenseAction().getHistoryToken());
		quickLinksMap.put(messages.chartOfAccounts(),
				new ChartOfAccountsAction().getHistoryToken());
		quickLinksMap.put(messages.creditCardExpense(),
				new CreditCardExpenseAction().getHistoryToken());
		quickLinksMap.put(messages.newQuote(), new NewQuoteAction(
				ClientEstimate.QUOTES).getHistoryToken());
		if (Accounter.getCompany().getPreferences().isDelayedchargesEnabled()) {
			quickLinksMap.put(messages.newCharge(), new NewQuoteAction(
					ClientEstimate.CHARGES).getHistoryToken());
			quickLinksMap.put(messages.newCredit(), new NewQuoteAction(
					ClientEstimate.CREDITS).getHistoryToken());
		}
		if (Accounter.getCompany().getPreferences().isSalesOrderEnabled()) {
			quickLinksMap.put(messages.newSalesOrder(), new NewQuoteAction(
					ClientEstimate.SALES_ORDER).getHistoryToken());
		}
		if (Accounter.getUser().getPermissions().getTypeOfManageAccounts() == RolePermissions.TYPE_YES) {
			quickLinksMap.put(messages.newJournalEntry(),
					new NewJournalEntryAction().getHistoryToken());
		}

		quickLinksMap.put(messages.receivePayment(),
				new ReceivePaymentAction().getHistoryToken());
		quickLinksMap.put(messages.Reconcile(),
				new ReconciliationsListAction().getHistoryToken());
		quickLinksMap.put(messages.payees(Global.get().Vendors()),
				new VendorsListAction().getHistoryToken());
		quickLinksMap.put(messages.writeCheck(),
				new WriteChecksAction().getHistoryToken());
		if (Accounter.getCompany().getPreferences().isKeepTrackofBills()) {
			quickLinksMap.put(messages.enterBill(),
					new EnterBillsAction().getHistoryToken());
		}

		// adding to flex table...
		Iterator<String> iterator = quickLinksMap.keySet().iterator();
		int j = 0, i = 0;
		while (iterator.hasNext()) {
			if (i == 3) {
				i = 0;
				j++;
			} else {
				final String linkName = iterator.next();
				Anchor link = new Anchor(linkName);
				link.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						historyChanged(quickLinksMap.get(linkName));
					}
				});
				linksTable.setWidget(j, i, link);
				i++;
			}
		}
		body.add(linksTable);
		completeInitialization();
	}

	protected void historyChanged(String value) {
		Action<?> action = MainFinanceWindow.actions.get(value);
		action.run();
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
