package com.vimukti.accounter.web.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.imports.UploadCSVFileDialogAction;
import com.vimukti.accounter.web.client.ui.banking.AccountRegisterAction;
import com.vimukti.accounter.web.client.ui.banking.CreditCardChargeAction;
import com.vimukti.accounter.web.client.ui.banking.MakeDepositAction;
import com.vimukti.accounter.web.client.ui.banking.ReconciliationsListAction;
import com.vimukti.accounter.web.client.ui.banking.WriteChecksAction;
import com.vimukti.accounter.web.client.ui.company.*;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.PayRollActions;
import com.vimukti.accounter.web.client.ui.core.PayRollReportActions;
import com.vimukti.accounter.web.client.ui.core.TransactionsCenterAction;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.*;
import com.vimukti.accounter.web.client.ui.fixedassets.PendingItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.RegisteredItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SoldDisposedFixedAssetsListAction;
import com.vimukti.accounter.web.client.ui.reports.BankingReportsAction;
import com.vimukti.accounter.web.client.ui.reports.ClassAndLocationReportsAction;
import com.vimukti.accounter.web.client.ui.reports.CompanyAndFinancialReportsAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryReportsAction;
import com.vimukti.accounter.web.client.ui.reports.JobReportsAction;
import com.vimukti.accounter.web.client.ui.reports.PayablesAndReceivablesReportsAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseReportsAction;
import com.vimukti.accounter.web.client.ui.reports.ReportsHomeAction;
import com.vimukti.accounter.web.client.ui.reports.SalesReportsAction;
import com.vimukti.accounter.web.client.ui.reports.TAXReportsAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByAccountAndCategoryAction;
import com.vimukti.accounter.web.client.ui.reports.VAT100ReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATDetailsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATUncategorisedAmountsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VatExceptionDetailReportAction;
import com.vimukti.accounter.web.client.ui.search.SearchInputAction;
import com.vimukti.accounter.web.client.ui.settings.GeneralSettingsAction;
import com.vimukti.accounter.web.client.ui.settings.InvoiceBrandingAction;
import com.vimukti.accounter.web.client.ui.settings.JobListAction;
import com.vimukti.accounter.web.client.ui.settings.MeasurementListAction;
import com.vimukti.accounter.web.client.ui.settings.StockSettingsAction;
import com.vimukti.accounter.web.client.ui.settings.UsersAction;
import com.vimukti.accounter.web.client.ui.translation.TranslationAction;
import com.vimukti.accounter.web.client.ui.vat.AdjustTAXAction;
import com.vimukti.accounter.web.client.ui.vat.ETdsFillingAction;
import com.vimukti.accounter.web.client.ui.vat.FileTAXAction;
import com.vimukti.accounter.web.client.ui.vat.ManageTAXCodesListAction;
import com.vimukti.accounter.web.client.ui.vat.NewTAXCodeAction;
import com.vimukti.accounter.web.client.ui.vat.NewVatItemAction;
import com.vimukti.accounter.web.client.ui.vat.PayTAXAction;
import com.vimukti.accounter.web.client.ui.vat.ReceiveVATAction;
import com.vimukti.accounter.web.client.ui.vat.TAXAgencyListAction;
import com.vimukti.accounter.web.client.ui.vat.TDSAcknowledgmentAction;
import com.vimukti.accounter.web.client.ui.vat.TDSChalanDetailsAction;
import com.vimukti.accounter.web.client.ui.vat.TDSFiledDetailsAction;
import com.vimukti.accounter.web.client.ui.vat.TDSForm16AAction;
import com.vimukti.accounter.web.client.ui.vat.TaxHistoryAction;
import com.vimukti.accounter.web.client.ui.vat.VatItemListAction;
import com.vimukti.accounter.web.client.ui.vendors.BillsAction;
import com.vimukti.accounter.web.client.ui.vendors.CashExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.DepositAction;
import com.vimukti.accounter.web.client.ui.vendors.EnterBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpenseClaimsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.IssuePaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCashPurchaseAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCreditMemoAction;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;
import com.vimukti.accounter.web.client.ui.vendors.PayBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.Prepare1099MISCAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsListAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsHomeAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsListAction;

/**
 * 
 * 
 */

public class MainFinanceWindow extends FlowPanel {

	private static ViewManager viewManager;
	private Header header;
	private int height;
	private int width;
	private HelpItem item;
	public static Map<String, Action> actions;
	private static final int CLASS = 1;
	private static final int LOCATION = 2;
	private static final int JOB = 3;

	protected AccounterMessages messages = Global.get().messages();

	public MainFinanceWindow() {
		initializeActionsWithTokens();
		createControls();
		sinkEvents(Event.ONMOUSEOVER);
	}

	private void createControls() {

		viewManager = (ViewManager) GWT.create(ViewManager.class);
		

		WebMenu menubar = GWT.create(WebMenu.class);
		menubar.initialize(isTouch());
		Widget asWidget = menubar.asWidget();
		
		viewManager.createView(this, menubar.getStackPanel());

		header = new Header(Accounter.getCompany());

		add(header);
		
		if (asWidget != null) {
			add(asWidget);
		}
		
		add(viewManager);

		Label help = new Label(messages.helpLinks());
		help.addStyleName("down-panel");
		if (item == null) {
			item = new HelpItem();
		}
		addStyleName("financeWindow");

		if (Accounter.isIpadApp()) {
			ActionFactory.getIpadMenuAction().run(null, false);
		} else {
			ActionFactory.getCompanyHomeAction().run(null, false);
		}

		// // } else {
		// // // if company is not configured then show the setupwizard
		// // SetupWizard setupWizard = new SetupWizard();
		// // add(setupWizard);
		// // }
		// Window.addWindowClosingHandler(new ClosingHandler() {
		//
		// @Override
		// public void onWindowClosing(ClosingEvent event) {
		// Accounter.setShutdown(true);
		// }
		// });
	}

	private native boolean isTouch() /*-{
		return $wnd.isTouch;
	}-*/;

	public HelpItem getHelpItem() {
		return item;

	}

	private Command getDashBoardCommand() {
		Command dashBoardcmd = new Command() {

			@Override
			public void execute() {
				String historyToken = new CompanyHomeAction().getHistoryToken();
				if (!History.getToken().equals(historyToken)) {
					new ExpensesAction(null).run(null, true);
				}
				new CompanyHomeAction().run(null, false);
			}
		};
		return dashBoardcmd;
	}

	public static ViewManager getViewManager() {
		return viewManager;
	}

	public void fitToSize(int height, int width) {
		this.height = height;
		this.width = width - 20;
		viewManager.fitToSize(height, width - 10 - 15);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.getElement().getParentElement()
				.addClassName("main-finance-window");
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOVER:

			break;

		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	/**
	 * Give this MenuBar focus.
	 */
	public void focus() {
		FocusImpl.getFocusImplForPanel().focus(getElement());
	}

	@Override
	protected void onAttach() {
		this.getParent().addStyleName("noScroll");
		this.setStyleName("finance_window");
		super.onAttach();
	}

	@Override
	protected void onDetach() {
		Accounter.getComet().stopComet();
		this.getParent().removeStyleName("noScroll");
		super.onDetach();

	}

	@Override
	protected void onUnload() {
		clearMacMenu();
		super.onUnload();
		// TODO
		// AccounterCometClient.cometStop();
	}

	private native static void clearMacMenu() /*-{
		$wnd.ClearMacMenu();
	}-*/;

	public <T extends IAccounterCore> void historyChanged(String value) {
		if (actions == null || value == null)
			return;

		HistoryToken historyToken;
		try {
			historyToken = new HistoryToken(value);
		} catch (Exception e) {
			return;
		}

		final Action action = actions.get(historyToken.getToken());
		if (action != null) {

			if (historyToken.getType() != null && historyToken.getValue() != 0) {
				AccounterAsyncCallback<T> callback = new AccounterAsyncCallback<T>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages.unableToshowtheview());
					}

					@Override
					public void onResultSuccess(T result) {
						if (result != null) {
							action.setInput(result);
							action.execute();
						}
					}

				};

				Accounter.createGETService().getObjectById(
						historyToken.getType(), historyToken.getValue(),
						callback);

			} else {
				action.run(null, false);
			}
		}
	}

	public static Map<String, Action> getActions() {
		return actions;
	}

	private void initializeActionsWithTokens() {
		actions = new HashMap<String, Action>();
		actions.put(new CompanyHomeAction().getHistoryToken(),
				new CompanyHomeAction());
		actions.put(new NewJournalEntryAction().getHistoryToken(),
				new NewJournalEntryAction());
		actions.put(new NewAccountAction().getHistoryToken(),
				new NewAccountAction());
		PreferencesAction preferencesAction = new PreferencesAction(
				PreferencesAction.COMPANY);
		actions.put(preferencesAction.getHistoryToken(), preferencesAction);
		preferencesAction = new PreferencesAction(PreferencesAction.SETTINGS);
		actions.put(preferencesAction.getHistoryToken(), preferencesAction);
		actions.put(ManageSupportListAction.salesTaxGroups().getHistoryToken(),
				ManageSupportListAction.salesTaxGroups());
		actions.put(ManageSupportListAction.salesTaxItems().getHistoryToken(),
				ManageSupportListAction.salesTaxItems());
		actions.put(new AdjustTAXAction(2).getHistoryToken(),
				new AdjustTAXAction(2));
		actions.put(new NewTAXAgencyAction().getHistoryToken(),
				new NewTAXAgencyAction());
		actions.put(ManageSupportListAction.customerGroups().getHistoryToken(),
				ManageSupportListAction.customerGroups());
		actions.put(ManageSupportListAction.vendorGroups().getHistoryToken(),
				ManageSupportListAction.vendorGroups());
		actions.put(ManageSupportListAction.paymentTerms().getHistoryToken(),
				ManageSupportListAction.paymentTerms());
		actions.put(
				ManageSupportListAction.shippingMethods().getHistoryToken(),
				ManageSupportListAction.shippingMethods());
		actions.put(ManageSupportListAction.shippingTerms().getHistoryToken(),
				ManageSupportListAction.shippingTerms());
		actions.put(ManageSupportListAction.priceLevels().getHistoryToken(),
				ManageSupportListAction.priceLevels());
		actions.put(ManageSupportListAction.itemGroups().getHistoryToken(),
				ManageSupportListAction.itemGroups());
		actions.put(ManageSupportListAction.classes().getHistoryToken(),
				ManageSupportListAction.classes());
		actions.put(new CreditRatingListAction().getHistoryToken(),
				new CreditRatingListAction());
		actions.put(new ManageFiscalYearAction().getHistoryToken(),
				new ManageFiscalYearAction());
		actions.put(new ChartOfAccountsAction().getHistoryToken(),
				new ChartOfAccountsAction());

		// actions.put(ActionFactory.getItemsAction().getHistoryToken(),
		// ActionFactory.getItemsAction());
		actions.put(new CustomersAction().getHistoryToken(),
				new CustomersAction());
		actions.put(new VendorsListAction().getHistoryToken(),
				new VendorsListAction());
		actions.put(new JobListAction().getHistoryToken(), new JobListAction());
		//
		// actions.put(ActionFactory.getImportBankStatementAction()
		// .getHistoryToken(), ActionFactory
		// .getImportBankStatementAction());

		actions.put(new SalesPersonAction().getHistoryToken(),
				new SalesPersonAction());

		// tax related items menus
		actions.put(new NewVatItemAction().getHistoryToken(),
				new NewVatItemAction());
		actions.put(new NewTAXCodeAction().getHistoryToken(),
				new NewTAXCodeAction());
		actions.put(new NewTAXAgencyAction().getHistoryToken(),
				new NewTAXAgencyAction());
		actions.put(new AdjustTAXAction(2).getHistoryToken(),
				new AdjustTAXAction(2));
		actions.put(new FileTAXAction().getHistoryToken(), new FileTAXAction());
		actions.put(new PayTAXAction().getHistoryToken(), new PayTAXAction());
		actions.put(new ReceiveVATAction().getHistoryToken(),
				new ReceiveVATAction());
		actions.put(new VatItemListAction().getHistoryToken(),
				new VatItemListAction());
		actions.put(new ManageTAXCodesListAction().getHistoryToken(),
				new ManageTAXCodesListAction());
		actions.put(new TaxHistoryAction().getHistoryToken(),
				new TaxHistoryAction());

		actions.put(new CustomersHomeAction().getHistoryToken(),
				new CustomersHomeAction());
		actions.put(new NewCustomerAction().getHistoryToken(),
				new NewCustomerAction());

		actions.put(
				new NewQuoteAction(ClientEstimate.QUOTES).getHistoryToken(),
				new NewQuoteAction(ClientEstimate.QUOTES));
		actions.put(
				new NewQuoteAction(ClientEstimate.CHARGES).getHistoryToken(),
				new NewQuoteAction(ClientEstimate.CHARGES));
		actions.put(
				new NewQuoteAction(ClientEstimate.CREDITS).getHistoryToken(),
				new NewQuoteAction(ClientEstimate.CREDITS));

		actions.put(new NewInvoiceAction().getHistoryToken(),
				new NewInvoiceAction());
		actions.put(new InvoiceBrandingAction().getHistoryToken(),
				new InvoiceBrandingAction());
		actions.put(new NewCashSaleAction().getHistoryToken(),
				new NewCashSaleAction());
		actions.put(new NewCreditsAndRefundsAction().getHistoryToken(),
				new NewCreditsAndRefundsAction());
		actions.put(new CustomerPaymentsAction().getHistoryToken(),
				new CustomerPaymentsAction());
		actions.put(new ReceivePaymentAction().getHistoryToken(),
				new ReceivePaymentAction());
		actions.put(new CustomerRefundAction().getHistoryToken(),
				new CustomerRefundAction());
		actions.put(new CustomersAction().getHistoryToken(),
				new CustomersAction());
		// actions.put(ActionFactory.getItemsAction().getHistoryToken(),
		// ActionFactory.getItemsAction());

		actions.put(BankingReportsAction.missingChecks().getHistoryToken(),
				BankingReportsAction.missingChecks());
		actions.put(BankingReportsAction.reconciliationDescrepancy()
				.getHistoryToken(), BankingReportsAction
				.reconciliationDescrepancy());

		actions.put(InventoryActions.newAssembly().getHistoryToken(),
				InventoryActions.newAssembly());

		NewItemAction newItemAction = new NewItemAction(true);
		newItemAction.setType(ClientItem.TYPE_INVENTORY_PART);
		actions.put(newItemAction.getHistoryToken(), newItemAction);

		actions.put(new InvoicesAction(null).getHistoryToken(),
				new InvoicesAction(null));
		actions.put(new TransactionsCenterAction().getHistoryToken(),
				new TransactionsCenterAction());

		actions.put(new TransactionsCenterAction().getHistoryToken(),
				new TransactionsCenterAction());
		actions.put(new VendorsHomeAction().getHistoryToken(),
				new VendorsHomeAction());
		actions.put(new NewVendorAction().getHistoryToken(),
				new NewVendorAction());
		actions.put(new NewCashPurchaseAction().getHistoryToken(),
				new NewCashPurchaseAction());
		actions.put(new NewCreditMemoAction().getHistoryToken(),
				new NewCreditMemoAction());
		actions.put(new EnterBillsAction().getHistoryToken(),
				new EnterBillsAction());
		actions.put(new PayBillsAction().getHistoryToken(),
				new PayBillsAction());
		actions.put(new IssuePaymentsAction().getHistoryToken(),
				new IssuePaymentsAction());
		actions.put(new VendorPaymentsAction().getHistoryToken(),
				new VendorPaymentsAction());
		actions.put(new ExpensesAction("").getHistoryToken(),
				new ExpensesAction(""));
		actions.put(new ExpenseClaimsAction(0).getHistoryToken(),
				new ExpenseClaimsAction(0));

		// actions.put(ActionFactory.getItemsAction().getHistoryToken(),
		// ActionFactory.getItemsAction());
		actions.put(new BillsAction().getHistoryToken(), new BillsAction());
		actions.put(new VendorPaymentsAction().getHistoryToken(),
				new VendorPaymentsAction());

		actions.put(
				new NewAccountAction(ClientAccount.TYPE_BANK).getHistoryToken(),
				new NewAccountAction(ClientAccount.TYPE_BANK));
		actions.put(new WriteChecksAction().getHistoryToken(),
				new WriteChecksAction());
		// actions.put(ActionFactory.getBankStatementAction().getHistoryToken(),
		// ActionFactory.getBankStatementAction());
		actions.put(new MakeDepositAction().getHistoryToken(),
				new MakeDepositAction());
		actions.put(new PayBillsAction().getHistoryToken(),
				new PayBillsAction());
		actions.put(new CreditCardChargeAction().getHistoryToken(),
				new CreditCardChargeAction());

		actions.put(new NewQuoteAction(ClientEstimate.SALES_ORDER)
				.getHistoryToken(), new NewQuoteAction(
				ClientEstimate.SALES_ORDER));

		actions.put(new PurchaseOrderAction().getHistoryToken(),
				new PurchaseOrderAction());

		actions.put(CompanyAndFinancialReportsAction.profitAndLoss()
				.getHistoryToken(), CompanyAndFinancialReportsAction
				.profitAndLoss());
		actions.put(CompanyAndFinancialReportsAction.balanceSheet()
				.getHistoryToken(), CompanyAndFinancialReportsAction
				.balanceSheet());
		actions.put(CompanyAndFinancialReportsAction.cashFlow()
				.getHistoryToken(), CompanyAndFinancialReportsAction.cashFlow());
		actions.put(CompanyAndFinancialReportsAction.trailBalance()
				.getHistoryToken(), CompanyAndFinancialReportsAction
				.trailBalance());

		actions.put(CompanyAndFinancialReportsAction
				.transactionDetailByAccount().getHistoryToken(),
				CompanyAndFinancialReportsAction.transactionDetailByAccount());
		actions.put(new TransactionDetailByAccountAndCategoryAction()
				.getHistoryToken(),
				new TransactionDetailByAccountAndCategoryAction());
		actions.put(CompanyAndFinancialReportsAction.generalLedger()
				.getHistoryToken(), CompanyAndFinancialReportsAction
				.generalLedger());
		actions.put(CompanyAndFinancialReportsAction.expense()
				.getHistoryToken(), CompanyAndFinancialReportsAction.expense());
		actions.put(CompanyAndFinancialReportsAction.incomeByCustomer()
				.getHistoryToken(), CompanyAndFinancialReportsAction
				.incomeByCustomer());

		actions.put(TAXReportsAction.salesTaxLiability().getHistoryToken(),
				TAXReportsAction.salesTaxLiability());
		actions.put(TAXReportsAction.transactionDetailByTaxItem()
				.getHistoryToken(), TAXReportsAction
				.transactionDetailByTaxItem());
		actions.put(PayablesAndReceivablesReportsAction.arAgingSummary()
				.getHistoryToken(), PayablesAndReceivablesReportsAction
				.arAgingSummary());
		actions.put(PayablesAndReceivablesReportsAction.arAgingDetail()
				.getHistoryToken(), PayablesAndReceivablesReportsAction
				.arAgingDetail());
		actions.put(PayablesAndReceivablesReportsAction.customerStatement(0)
				.getHistoryToken(), PayablesAndReceivablesReportsAction
				.customerStatement(0));
		actions.put(PayablesAndReceivablesReportsAction
				.customerTransactionHistory().getHistoryToken(),
				PayablesAndReceivablesReportsAction
						.customerTransactionHistory());
		actions.put(SalesReportsAction.customerSummary().getHistoryToken(),
				SalesReportsAction.customerSummary());
		actions.put(JobReportsAction.estimatesByJob().getHistoryToken(),
				JobReportsAction.estimatesByJob());
		actions.put(JobReportsAction.unbilledCost().getHistoryToken(),
				JobReportsAction.unbilledCost());
		actions.put(SalesReportsAction.customerDetail().getHistoryToken(),
				SalesReportsAction.customerDetail());
		actions.put(SalesReportsAction.itemSummary().getHistoryToken(),
				SalesReportsAction.itemSummary());
		actions.put(SalesReportsAction.itemDetail().getHistoryToken(),
				SalesReportsAction.itemDetail());
		actions.put(SalesReportsAction.salesOrder().getHistoryToken(),
				SalesReportsAction.salesOrder());

		actions.put(PayablesAndReceivablesReportsAction.apAgingSummary()
				.getHistoryToken(), PayablesAndReceivablesReportsAction
				.apAgingSummary());
		actions.put(PayablesAndReceivablesReportsAction.apAgingDetail()
				.getHistoryToken(), PayablesAndReceivablesReportsAction
				.apAgingDetail());
		actions.put(PayablesAndReceivablesReportsAction
				.vendorTransactionHistory().getHistoryToken(),
				PayablesAndReceivablesReportsAction.vendorTransactionHistory());

		actions.put(PurchaseReportsAction.vendorSummary().getHistoryToken(),
				PurchaseReportsAction.vendorSummary());
		actions.put(PurchaseReportsAction.vendorDetail().getHistoryToken(),
				PurchaseReportsAction.vendorDetail());
		actions.put(PurchaseReportsAction.itemSummary().getHistoryToken(),
				PurchaseReportsAction.itemSummary());
		actions.put(PurchaseReportsAction.itemDetail().getHistoryToken(),
				PurchaseReportsAction.itemDetail());
		actions.put(PurchaseReportsAction.purchaseOrder().getHistoryToken(),
				PurchaseReportsAction.purchaseOrder());

		actions.put(PayablesAndReceivablesReportsAction.vendorStatement(0)
				.getHistoryToken(), PayablesAndReceivablesReportsAction
				.vendorStatement(0));

		actions.put(new VATSummaryReportAction().getHistoryToken(),
				new VATSummaryReportAction());
		actions.put(new VATDetailsReportAction().getHistoryToken(),
				new VATDetailsReportAction());
		actions.put(new VAT100ReportAction().getHistoryToken(),
				new VAT100ReportAction());
		actions.put(new InventoryDetailsAction().getHistoryToken(),
				new InventoryDetailsAction());
		actions.put(
				new VATUncategorisedAmountsReportAction().getHistoryToken(),
				new VATUncategorisedAmountsReportAction());

		actions.put(new VatExceptionDetailReportAction().getHistoryToken(),
				new VatExceptionDetailReportAction());

		actions.put(TAXReportsAction.taxItemException().getHistoryToken(),
				TAXReportsAction.taxItemException());

		actions.put(TAXReportsAction.taxItemDetail().getHistoryToken(),
				TAXReportsAction.taxItemDetail());

		actions.put(TAXReportsAction.taxItemSummary().getHistoryToken(),
				TAXReportsAction.taxItemSummary());
		actions.put(new ECSalesListAction().getHistoryToken(),
				new ECSalesListAction());

		actions.put(new GeneralSettingsAction().getHistoryToken(),
				new GeneralSettingsAction());
		actions.put(new NewJobAction(null).getHistoryToken(), new NewJobAction(
				null));
		actions.put("bankAccounts", new ChartOfAccountsAction(
				ClientAccount.TYPE_BANK));
		actions.put(new CashExpenseAction().getHistoryToken(),
				new CashExpenseAction());
		actions.put(new CreditCardExpenseAction().getHistoryToken(),
				new CreditCardExpenseAction());
		actions.put("employeeExpenses", new ExpensesAction(messages.employee()));
		actions.put(new AccountRegisterAction().getHistoryToken(),
				new AccountRegisterAction());
		actions.put("overDueInvoices", new InvoicesAction(messages.overDue()));
		actions.put(new UserDetailsAction().getHistoryToken(),
				new UserDetailsAction());
		actions.put(new Prepare1099MISCAction().getHistoryToken(),
				new Prepare1099MISCAction());

		actions.put(new ReconciliationsListAction().getHistoryToken(),
				new ReconciliationsListAction());

		actions.put(new ReportsHomeAction().getHistoryToken(),
				new ReportsHomeAction());

		actions.put(ClassAndLocationReportsAction.profitAndLossLocation()
				.getHistoryToken(), ClassAndLocationReportsAction
				.profitAndLossLocation());
		actions.put(JobReportsAction.profitAndLoss().getHistoryToken(),
				JobReportsAction.profitAndLoss());

		actions.put(ClassAndLocationReportsAction.profitAndLossClass()
				.getHistoryToken(), ClassAndLocationReportsAction
				.profitAndLossClass());
		CompanyAndFinancialReportsAction reconcilationsAction = CompanyAndFinancialReportsAction
				.reconciliations();
		actions.put(reconcilationsAction.getHistoryToken(),
				reconcilationsAction);

		// Location,class Summary Reports
		actions.put(ClassAndLocationReportsAction.salesLocationSummary()
				.getHistoryToken(), ClassAndLocationReportsAction
				.salesLocationSummary());
		actions.put(ClassAndLocationReportsAction.salesClassSummary()
				.getHistoryToken(), ClassAndLocationReportsAction
				.salesClassSummary());
		actions.put(ClassAndLocationReportsAction.purchaseLocationSummary()
				.getHistoryToken(), ClassAndLocationReportsAction
				.purchaseLocationSummary());
		actions.put(ClassAndLocationReportsAction.purchaseClassSummary()
				.getHistoryToken(), ClassAndLocationReportsAction
				.purchaseClassSummary());

		// Location,class Detail Reports
		actions.put(ClassAndLocationReportsAction.salesLocationDetail()
				.getHistoryToken(), ClassAndLocationReportsAction
				.salesLocationDetail());
		actions.put(ClassAndLocationReportsAction.salesClassDetail()
				.getHistoryToken(), ClassAndLocationReportsAction
				.salesClassDetail());
		actions.put(ClassAndLocationReportsAction.purchaseLocationDetail()
				.getHistoryToken(), ClassAndLocationReportsAction
				.purchaseLocationDetail());
		actions.put(ClassAndLocationReportsAction.purchaseClassDetail()
				.getHistoryToken(), ClassAndLocationReportsAction
				.purchaseClassDetail());

		actions.put(new UsersActivityListAction().getHistoryToken(),
				new UsersActivityListAction());

		actions.put(new RecurringsListAction().getHistoryToken(),
				new RecurringsListAction());

		actions.put(new RemindersListAction().getHistoryToken(),
				new RemindersListAction());

		actions.put(CompanyAndFinancialReportsAction.automaticTransactions()
				.getHistoryToken(), CompanyAndFinancialReportsAction
				.automaticTransactions());

		NewItemAction supplierItem = new NewItemAction(false);
		actions.put(supplierItem.getHistoryToken(), supplierItem);

		NewItemAction customerItem = new NewItemAction(true);
		actions.put(customerItem.getHistoryToken(), customerItem);

		actions.put(ManageSupportListAction.locations().getHistoryToken(),
				ManageSupportListAction.locations());

		ItemsAction customerItemAction = new ItemsAction(Global.get()
				.customer());
		actions.put(customerItemAction.getHistoryToken(), customerItemAction);

		ItemsAction vendorItemAction = new ItemsAction(Global.get().vendor());
		actions.put(vendorItemAction.getHistoryToken(), vendorItemAction);

		ItemsAction allItemAction = new ItemsAction(
				messages.bothCustomerAndVendor(Global.get().Customer(), Global
						.get().Vendor()));
		actions.put(allItemAction.getHistoryToken(), allItemAction);

		// adding actions for inventory

		actions.put(WarehouseActions.newWarehouse().getHistoryToken(),
				WarehouseActions.newWarehouse());
		actions.put(WarehouseActions.warehouseTransfer().getHistoryToken(),
				WarehouseActions.warehouseTransfer());
		actions.put(InventoryActions.measurement().getHistoryToken(),
				InventoryActions.measurement());

		actions.put(InventoryActions.inventoryItems().getHistoryToken(),
				InventoryActions.inventoryItems());

		actions.put(WarehouseActions.warehousesList().getHistoryToken(),
				WarehouseActions.warehousesList());
		actions.put(
				WarehouseActions.warehouseTransfersList().getHistoryToken(),
				WarehouseActions.warehouseTransfersList());
		actions.put(new MeasurementListAction().getHistoryToken(),
				new MeasurementListAction());
		actions.put(InventoryActions.stockAdjustment().getHistoryToken(),
				InventoryActions.stockAdjustment());

		actions.put(new StockSettingsAction().getHistoryToken(),
				new StockSettingsAction());

		actions.put(InventoryActions.stockAdjustments().getHistoryToken(),
				InventoryActions.stockAdjustments());

		actions.put(ManageSupportListAction.currencyGroups().getHistoryToken(),
				ManageSupportListAction.currencyGroups());

		actions.put(new TranslationAction().getHistoryToken(),
				new TranslationAction());

		actions.put(new TAXAgencyListAction().getHistoryToken(),
				new TAXAgencyListAction());

		// for inventory report actions
		actions.put(InventoryReportsAction.itemReport().getHistoryToken(),
				InventoryReportsAction.itemReport());
		actions.put(
				InventoryReportsAction.valuationDetails().getHistoryToken(),
				InventoryReportsAction.valuationDetails());
		actions.put(
				InventoryReportsAction.valuationSummary().getHistoryToken(),
				InventoryReportsAction.valuationSummary());
		actions.put(InventoryReportsAction.stockStatusByItem()
				.getHistoryToken(), InventoryReportsAction.stockStatusByItem());
		actions.put(InventoryReportsAction.stockStatusByVendor()
				.getHistoryToken(), InventoryReportsAction
				.stockStatusByVendor());
		// merge actions
		actions.put(MergeAction.customers().getHistoryToken(),
				MergeAction.customers());

		actions.put(MergeAction.vendors().getHistoryToken(),
				MergeAction.vendors());

		actions.put(MergeAction.accounts().getHistoryToken(),
				MergeAction.accounts());

		actions.put(MergeAction.items().getHistoryToken(), MergeAction.items());

		actions.put(new BudgetAction().getHistoryToken(), new BudgetAction());

		actions.put(MergeAction.classes().getHistoryToken(),
				MergeAction.classes());

		actions.put(MergeAction.locations().getHistoryToken(),
				MergeAction.locations());

		/**
		 * budget report actions
		 */
		actions.put(ActionFactory.getBudgetOverView().getHistoryToken(),
				ActionFactory.getBudgetOverView());
		actions.put(ActionFactory.getBudgetVsActionReport().getHistoryToken(),
				ActionFactory.getBudgetVsActionReport());

		actions.put(ActionFactory.getRecordExpensesAction().getHistoryToken(),
				ActionFactory.getRecordExpensesAction());

		actions.put(ActionFactory.getNewFixedAssetAction().getHistoryToken(),
				ActionFactory.getNewFixedAssetAction());

		actions.put(ActionFactory.getDepreciationSheduleAction()
				.getHistoryToken(), ActionFactory
				.getDepreciationSheduleAction());

		actions.put(ActionFactory.getDepriciationAction().getHistoryToken(),
				ActionFactory.getDepriciationAction());

		actions.put(ActionFactory.getRegisteredItemsListAction()
				.getHistoryToken(), ActionFactory
				.getRegisteredItemsListAction());
		actions.put(
				ActionFactory.getPendingItemsListAction().getHistoryToken(),
				ActionFactory.getPendingItemsListAction());
		actions.put(
				ActionFactory.getSoldDisposedListAction().getHistoryToken(),
				ActionFactory.getSoldDisposedListAction());

		actions.put(new DepreciationAction().getHistoryToken(),
				new DepreciationAction());
		actions.put(new RegisteredItemsListAction().getHistoryToken(),
				new RegisteredItemsListAction());
		actions.put(new PendingItemsListAction().getHistoryToken(),
				new PendingItemsListAction());
		actions.put(new SoldDisposedFixedAssetsListAction().getHistoryToken(),
				new SoldDisposedFixedAssetsListAction());

		actions.put(new SearchInputAction().getHistoryToken(),
				new SearchInputAction());

		actions.put(new CustomerCentreAction().getHistoryToken(),
				new CustomerCentreAction());

		actions.put(new CheckPrintSettingAction().getHistoryToken(),
				new CheckPrintSettingAction());

		actions.put(new DeleteCompanyAction().getHistoryToken(),
				new DeleteCompanyAction());

		// actions.put(ActionFactory.getDepositAction().getHistoryToken(),
		// ActionFactory.getDepositAction());

		actions.put(new TDSChalanDetailsAction().getHistoryToken(),
				new TDSChalanDetailsAction());

		actions.put(new VendorPaymentsListAction().getHistoryToken(),
				new VendorPaymentsListAction());

		actions.put(new ChalanListViewAction().getHistoryToken(),
				new ChalanListViewAction());

		actions.put(new TdsDeductorMasterAction().getHistoryToken(),
				new TdsDeductorMasterAction());

		actions.put(new TDSResponsiblePersonAction().getHistoryToken(),
				new TDSResponsiblePersonAction());

		actions.put(new VendorCenterAction().getHistoryToken(),
				new VendorCenterAction());

		actions.put(new UsersAction().getHistoryToken(), new UsersAction());

		actions.put(new InvoiceBrandingAction().getHistoryToken(),
				new InvoiceBrandingAction());

		actions.put(new ETdsFillingAction().getHistoryToken(),
				new ETdsFillingAction());

		actions.put(new TDSAcknowledgmentAction().getHistoryToken(),
				new TDSAcknowledgmentAction());

		actions.put(new TDSForm16AAction().getHistoryToken(),
				new TDSForm16AAction());

		actions.put(TAXReportsAction.tdsAcknowledgement().getHistoryToken(),
				TAXReportsAction.tdsAcknowledgement());

		actions.put(new TDSFiledDetailsAction().getHistoryToken(),
				new TDSFiledDetailsAction());

		actions.put(ActionFactory.getIARSInformationAction().getHistoryToken(),
				ActionFactory.getIARSInformationAction());

		JobReportsAction action = new JobReportsAction(
				JobReportsAction.TYPE_ESTIMATES_BY_JOB);
		actions.put(action.getHistoryToken(), action);

		JobReportsAction action2 = new JobReportsAction(
				JobReportsAction.TYPE_PROFIT_AND_LOSS);
		actions.put(action2.getHistoryToken(), action2);

		JobReportsAction action3 = new JobReportsAction(
				JobReportsAction.TYPE_PROFITABILITY_DETAILS);
		actions.put(action3.getHistoryToken(), action3);

		JobReportsAction action4 = new JobReportsAction(
				JobReportsAction.TYPE_PROFITABILITY_SUMMARY);
		actions.put(action4.getHistoryToken(), action4);

		JobReportsAction action5 = new JobReportsAction(
				JobReportsAction.TYPE_UNBILLED_COST);
		actions.put(action5.getHistoryToken(), action5);

		actions.put(new CreateIRASInformationFileAction().getHistoryToken(),
				new CreateIRASInformationFileAction());
		actions.put(CompanyAndFinancialReportsAction.realisedLossAndGrains()
				.getHistoryToken(), CompanyAndFinancialReportsAction
				.realisedLossAndGrains());
		actions.put(CompanyAndFinancialReportsAction
				.unRelealisedLossAndGrains().getHistoryToken(),
				CompanyAndFinancialReportsAction.unRelealisedLossAndGrains());
		actions.put(CompanyAndFinancialReportsAction.exchangeRates()
				.getHistoryToken(), CompanyAndFinancialReportsAction
				.exchangeRates());

		actions.put(new DepositAction().getHistoryToken(), new DepositAction());

		actions.put(new UploadCSVFileDialogAction().getHistoryToken(),
				new UploadCSVFileDialogAction());

		actions.put(InventoryActions.buildAssembly().getHistoryToken(),
				InventoryActions.buildAssembly());

		actions.put(InventoryActions.inventoyCentre().getHistoryToken(),
				InventoryActions.inventoyCentre());

		// for banking reports
		actions.put(BankingReportsAction.depositDetail().getHistoryToken(),
				BankingReportsAction.depositDetail());
		actions.put(BankingReportsAction.checkDetail().getHistoryToken(),
				BankingReportsAction.checkDetail());

		actions.put(PayRollActions.newEmployeeAction().getHistoryToken(),
				PayRollActions.newEmployeeAction());

		actions.put(PayRollActions.newPayHeadAction().getHistoryToken(),
				PayRollActions.newPayHeadAction());

		actions.put(PayRollActions.newEmployeeGroupAction().getHistoryToken(),
				PayRollActions.newEmployeeGroupAction());

		actions.put(PayRollActions.newPayRollUnitAction().getHistoryToken(),
				PayRollActions.newPayRollUnitAction());

		actions.put(PayRollActions.employeeListAction().getHistoryToken(),
				PayRollActions.employeeListAction());

		actions.put(PayRollActions.payHeadListAction().getHistoryToken(),
				PayRollActions.payHeadListAction());

		actions.put(PayRollActions.payRollUnitListAction().getHistoryToken(),
				PayRollActions.payRollUnitListAction());

		actions.put(PayRollActions.employeeGroupListAction().getHistoryToken(),
				PayRollActions.employeeGroupListAction());

		actions.put(PayRollActions.newPayStructureAction().getHistoryToken(),
				PayRollActions.newPayStructureAction());

		actions.put(PayRollActions.newPayRunAction().getHistoryToken(),
				PayRollActions.newPayRunAction());

		actions.put(PayRollActions.payStructureListAction().getHistoryToken(),
				PayRollActions.payStructureListAction());

		actions.put(PayRollActions.newAttendanceProductionTypeAction()
				.getHistoryToken(), PayRollActions
				.newAttendanceProductionTypeAction());

		actions.put(PayRollActions.attendanceProductionTypeList()
				.getHistoryToken(), PayRollActions
				.attendanceProductionTypeList());

		actions.put(PayRollReportActions.getPaySlipSummaryReportAction()
				.getHistoryToken(), PayRollReportActions
				.getPaySlipSummaryReportAction());

		actions.put(PayRollReportActions.getPaySlipDetailReportAction()
				.getHistoryToken(), PayRollReportActions
				.getPaySlipDetailReportAction());

		actions.put(PayRollReportActions.getPaySheetReportAction()
				.getHistoryToken(), PayRollReportActions
				.getPaySheetReportAction());
		actions.put(PayRollReportActions
				.getPayTaxSummaryReportAction()
				.getHistoryToken(), PayRollReportActions
				.getPayTaxSummaryReportAction());

		actions.put(PayRollReportActions.getPayHeadSummaryReportAction()
				.getHistoryToken(), PayRollReportActions
				.getPayHeadSummaryReportAction());

		actions.put(PayRollReportActions.getPayHeadDetailReportAction()
				.getHistoryToken(), PayRollReportActions
				.getPayHeadDetailReportAction());

		actions.put(PayRollReportActions.getPayHeadSummaryReportAction()
				.getHistoryToken(), PayRollReportActions
				.getPayHeadSummaryReportAction());
		actions.put(ActionFactory.getPaypalTransaction().getHistoryToken(),
				ActionFactory.getPaypalTransaction());

		actions.put(PayRollActions.newPayEmployeeAction().getHistoryToken(),
				PayRollActions.newPayEmployeeAction());
		
		actions.put(PayRollActions.newPayTaxAction().getHistoryToken(),
				PayRollActions.newPayTaxAction());

		//NewConsultAction consultantAction = new NewConsultAction( "Consultant");
		//actions.put(consultantAction.getHistoryToken(), consultantAction);


		NewConsultAction consultantAction = new NewConsultAction(false);
		consultantAction.setType(ClientItem.TYPE_SERVICE);
		consultantAction.setSubType(ClientItem.TYPE_CONSULTANT_PART);
		//consultantAction.setItemText("Testing");
		actions.put(consultantAction.getHistoryToken(), consultantAction);

		ConsultantCenterAction consultantCenterAction = new ConsultantCenterAction( "consultantCenter");
		actions.put(consultantCenterAction.getHistoryToken(), consultantCenterAction);

		actions.put(CompanyAndFinancialReportsAction.salesTaxLiability()
				.getHistoryToken(), CompanyAndFinancialReportsAction
				.salesTaxLiability());

		actions.put(CompanyAndFinancialReportsAction
				.transactionDetailByTaxItem().getHistoryToken(),
				CompanyAndFinancialReportsAction.transactionDetailByTaxItem());

		actions.put(ActionFactory.getIpadMenuAction().getHistoryToken(),
				ActionFactory.getIpadMenuAction());
		ManageSupportListAction salesTaxGroupAction = ManageSupportListAction
				.salesTaxGroup();
		actions.put(salesTaxGroupAction.getHistoryToken(), salesTaxGroupAction);
		ManageSupportListAction companiesAction = ManageSupportListAction
				.companies();
		actions.put(companiesAction.getHistoryToken(), companiesAction);
	}

	public ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	public void onSessionExpired() {
		// Accounter.getComet().stopComet();
	}
}
