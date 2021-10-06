package com.nitya.accounter.web.client.ui.win8portlets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.portlet.PortletPage;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.BaseHomeView;
import com.nitya.accounter.web.client.ui.ImageButton;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.banking.ReconciliationsListAction;
import com.nitya.accounter.web.client.ui.banking.WriteChecksAction;
import com.nitya.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.nitya.accounter.web.client.ui.company.CustomersAction;
import com.nitya.accounter.web.client.ui.company.NewJournalEntryAction;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.ActionFactory;
import com.nitya.accounter.web.client.ui.core.ButtonGroup;
import com.nitya.accounter.web.client.ui.core.IButtonContainer;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.customers.NewInvoiceAction;
import com.nitya.accounter.web.client.ui.customers.NewQuoteAction;
import com.nitya.accounter.web.client.ui.customers.ReceivePaymentAction;
import com.nitya.accounter.web.client.ui.reports.ReportsHomeAction;
import com.nitya.accounter.web.client.ui.vendors.BillsAction;
import com.nitya.accounter.web.client.ui.vendors.CashExpenseAction;
import com.nitya.accounter.web.client.ui.vendors.CreditCardExpenseAction;
import com.nitya.accounter.web.client.ui.vendors.EnterBillsAction;
import com.nitya.accounter.web.client.ui.vendors.ExpensesAction;
import com.nitya.accounter.web.client.ui.vendors.VendorsListAction;

public class Windows8DashBoard extends BaseHomeView implements IButtonContainer {

	private PortletPage page;
	private StyledPanel mainPanel;
	private StyledPanel quicklinksPanel;
	private Map<String, String> quickLinksMap;
	private Button moreButton;

	public Windows8DashBoard() {

	}

	@Override
	public void init() {
		// super.init();
		this.addStyleName("portlet_quicklinks");
		add(createControl());

	}

	private Widget createControl() {
		Label lab1 = new Label(Global.get().messages().dashBoard());
		lab1.setStyleName("label-title");

		mainPanel = new StyledPanel("dashBoard");
		mainPanel.add(lab1);
		page = new PortletPage(PortletPage.DASHBOARD);
		quicklinksPanel = new StyledPanel("quickLinks-panel");

		createQuickLinksPanel();

		moreButton = new Button();
		moreButton.setText("More...");
		moreButton.getElement().setId("moreButton");
		moreButton.setTitle("click here to go to menu");
		moreButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				ActionFactory.getWindows8MenuAction().run(null, false);
			}
		});
		quicklinksPanel.add(moreButton);
		// TODO add portlets to windows8
		mainPanel.add(page);
		// mainPanel.add(portletsPanel);

		mainPanel.add(quicklinksPanel);
		return mainPanel;
	}

	private void createQuickLinksPanel() {
		quickLinksMap = new HashMap<String, String>();
		// initializing links...
		quickLinksMap.put(messages.newInvoice(),
				new NewInvoiceAction().getHistoryToken());

		quickLinksMap.put(messages.expenses(),
				new ExpensesAction("").getHistoryToken());

		quickLinksMap.put(messages.payees(Global.get().Customers()),
				new CustomersAction().getHistoryToken());

		quickLinksMap.put(messages.writeCheck(),
				new WriteChecksAction().getHistoryToken());

		// quickLinksMap.put(messages.depositTransferFunds(), ActionFactory
		// .getTransferFundsAction().getHistoryToken());

		quickLinksMap
				.put(messages.bills(), new BillsAction().getHistoryToken());

		if (Accounter.getCompany().getPreferences().isKeepTrackofBills()) {
			quickLinksMap.put(messages.enterBill(),
					new EnterBillsAction().getHistoryToken());
		}

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
		}
		quickLinksMap.put(messages.newJournalEntry(),
				new NewJournalEntryAction().getHistoryToken());

		quickLinksMap.put(messages.receivePayment(),
				new ReceivePaymentAction().getHistoryToken());

		quickLinksMap.put(messages.Reconcile(),
				new ReconciliationsListAction().getHistoryToken());

		quickLinksMap.put(messages.payees(Global.get().Vendor()),
				new VendorsListAction().getHistoryToken());

		quickLinksMap.put(messages.reports(),
				new ReportsHomeAction().getHistoryToken());

		// adding to flex table...
		Iterator<String> iterator = quickLinksMap.keySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			if (i == 3) {
				i = 0;
			} else {
				final String linkName = iterator.next();
				Anchor link = new Anchor(linkName);
				link.setStyleName("quickLinkBox");
				link.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						historyChanged(quickLinksMap.get(linkName));
					}
				});
				quicklinksPanel.add(link);
				i++;
			}
		}
	}

	protected void historyChanged(String value) {
		Action<?> action = MainFinanceWindow.actions.get(value);
		action.run();
	}

	@Override
	protected void onAttach() {
		getPage().refreshWidgets();
		super.onAttach();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
	}

	public void refreshWidgetData() {
	}

	public void showGettingStarted() {
	}

	public void hideGettingStarted() {
	}

	public PortletPage getPage() {
		return page;
	}

	public void setPage(PortletPage page) {
		this.page = page;
	}

	@Override
	public void addButtons(ButtonGroup group) {
		final ImageButton configButton = new ImageButton(
				messages.configurePortlets(), Accounter.getFinanceImages()
						.portletPageSettings(), "settings");
		configButton.addStyleName("settingsButton");
		configButton.getElement().setId("configButton");
		configButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ViewManager.getInstance().showDialogRelativeTo(
						getPage().createSettingsDialog(), configButton);
			}
		});
		configButton.getElement().setAttribute("data-icon", "settings");
		addButton(group, configButton);
	}
}