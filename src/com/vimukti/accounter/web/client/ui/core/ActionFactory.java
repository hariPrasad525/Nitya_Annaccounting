package com.vimukti.accounter.web.client.ui.core;

import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.*;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.imports.UploadCSVFileDialogAction;
import com.vimukti.accounter.web.client.ui.RemindersListAction;
import com.vimukti.accounter.web.client.ui.banking.AccountRegisterAction;
import com.vimukti.accounter.web.client.ui.banking.BankStatementAction;
import com.vimukti.accounter.web.client.ui.banking.BankingHomeAction;
import com.vimukti.accounter.web.client.ui.banking.BuyChecksAndFormsAction;
import com.vimukti.accounter.web.client.ui.banking.CreditCardChargeAction;
import com.vimukti.accounter.web.client.ui.banking.EnterPaymentsAction;
import com.vimukti.accounter.web.client.ui.banking.ImportBankFilesAction;
import com.vimukti.accounter.web.client.ui.banking.MakeDepositAction;
import com.vimukti.accounter.web.client.ui.banking.MatchTrasactionsAction;
import com.vimukti.accounter.web.client.ui.banking.NewReconcileAccountAction;
import com.vimukti.accounter.web.client.ui.banking.PrintChecksAction;
import com.vimukti.accounter.web.client.ui.banking.ReconciliationsListAction;
import com.vimukti.accounter.web.client.ui.banking.ServicesOverviewAction;
import com.vimukti.accounter.web.client.ui.banking.StatementImportViewAction;
import com.vimukti.accounter.web.client.ui.banking.StatementReconcilationAction;
import com.vimukti.accounter.web.client.ui.banking.SyncOnlinePayeesAction;
import com.vimukti.accounter.web.client.ui.banking.WriteChecksAction;
import com.vimukti.accounter.web.client.ui.combo.DropDownAction;
import com.vimukti.accounter.web.client.ui.company.*;
import com.vimukti.accounter.web.client.ui.customers.*;
import com.vimukti.accounter.web.client.ui.fixedassets.DisposingRegisteredItemAction;
import com.vimukti.accounter.web.client.ui.fixedassets.HistoryListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.NewFixedAssetAction;
import com.vimukti.accounter.web.client.ui.fixedassets.PendingItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.RegisteredItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SellingRegisteredItemAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SoldDisposedFixedAssetsListAction;
import com.vimukti.accounter.web.client.ui.reports.BudgetOverviewReportAction;
import com.vimukti.accounter.web.client.ui.reports.BudgetvsActualsAction;
import com.vimukti.accounter.web.client.ui.reports.DepreciationSheduleAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.ItemActualCostDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.JobActualDetailAction;
import com.vimukti.accounter.web.client.ui.reports.MISC1099TransactionDetailAction;
import com.vimukti.accounter.web.client.ui.reports.MostProfitableCustomersAction;
import com.vimukti.accounter.web.client.ui.reports.ReportsHomeAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByAccountAndCategoryAction;
import com.vimukti.accounter.web.client.ui.reports.VAT100ReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATDetailsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATUncategorisedAmountsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VaTItemDetailAction;
import com.vimukti.accounter.web.client.ui.reports.VatExceptionDetailReportAction;
import com.vimukti.accounter.web.client.ui.search.SearchInputAction;
import com.vimukti.accounter.web.client.ui.settings.AutomaticSequenceAction;
import com.vimukti.accounter.web.client.ui.settings.ConversionBalancesAction;
import com.vimukti.accounter.web.client.ui.settings.ConversionDateAction;
import com.vimukti.accounter.web.client.ui.settings.CopyThemeAction;
import com.vimukti.accounter.web.client.ui.settings.CustomThemeAction;
import com.vimukti.accounter.web.client.ui.settings.DeleteThemeAction;
import com.vimukti.accounter.web.client.ui.settings.GeneralSettingsAction;
import com.vimukti.accounter.web.client.ui.settings.InviteUserAction;
import com.vimukti.accounter.web.client.ui.settings.InvoiceBrandingAction;
import com.vimukti.accounter.web.client.ui.settings.JobListAction;
import com.vimukti.accounter.web.client.ui.settings.MeasurementListAction;
import com.vimukti.accounter.web.client.ui.settings.NewBrandThemeAction;
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
import com.vimukti.accounter.web.client.ui.vendors.EmployeeExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.EnterBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpenseClaimsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.IssuePaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCashPurchaseAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCreditMemoAction;
import com.vimukti.accounter.web.client.ui.vendors.NewItemReceiptAction;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;
import com.vimukti.accounter.web.client.ui.vendors.PayBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.Prepare1099MISCAction;
import com.vimukti.accounter.web.client.ui.vendors.PreviousClaimAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderAction;
import com.vimukti.accounter.web.client.ui.vendors.RecordExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.TDSPayAction;
import com.vimukti.accounter.web.client.ui.vendors.TDSVendorsListAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsListAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsHomeAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsListAction;
import com.vimukti.accounter.web.client.ui.win8.IPadMenuAction;
import com.vimukti.accounter.web.client.ui.win8.Windows8MenuAction;

public class ActionFactory {

	public static AccounterMessages messages = Global.get().messages();

	public static GeneralSettingsAction getGeneralSettingsAction() {
		return new GeneralSettingsAction();
	}

	public static NewItemAction getNewInventoryItemAction() {
		NewItemAction action = new NewItemAction(true);
		action.setType(ClientItem.TYPE_INVENTORY_PART);
		return action;
	}

	public static ConversionBalancesAction getConversionBalancesAction() {
		return new ConversionBalancesAction();
	}

	public static InvoiceBrandingAction getInvoiceBrandingAction() {
		return new InvoiceBrandingAction();
	}

	public static NewBrandThemeAction getNewBrandThemeAction() {
		return new NewBrandThemeAction();

	}

	public static NewBrandCustomThemeAction getNewBrandCustomThemeAction() {
		return new NewBrandCustomThemeAction();

	}

	public static NewBrandCustomThemeAction getEditBrandCustomThemeAction() {
		return new NewBrandCustomThemeAction();

	}

	public static ConversionDateAction getConversionDateAction() {
		return new ConversionDateAction();
	}

	public static AutomaticSequenceAction getAutomaticSequenceAction() {
		return new AutomaticSequenceAction();
	}

	public static CustomThemeAction getCustomThemeAction() {
		return new CustomThemeAction();
	}

	public static UsersAction getUsersAction() {
		return new UsersAction();
	}

	public static InviteUserAction getInviteUserAction() {
		return new InviteUserAction();
	}

	public static DeleteThemeAction getDeleteThemeAction() {
		return new DeleteThemeAction();
	}

	public static CopyThemeAction getCopyThemeAction() {
		return new CopyThemeAction();
	}

	// Banking action factory

	public static BankingHomeAction getBankingHomeAction() {
		return new BankingHomeAction();
	}

	public static NewAccountAction getNewBankAccountAction() {
		return new NewAccountAction(ClientAccount.TYPE_BANK);
	}

	public static AccountRegisterAction getAccountRegisterAction() {
		return new AccountRegisterAction();
	}

	public static WriteChecksAction getWriteChecksAction() {
		return new WriteChecksAction();
	}

	public static BankStatementAction getBankStatementAction(
			ClientAccount account) {
		return new BankStatementAction(account);
	}

	public static MakeDepositAction getMakeDepositAction() {
		return new MakeDepositAction();
	}

	public static MakeDepositAction getMakeDepositAction(
			ClientTransferFund makeDeposit,
			AccounterAsyncCallback<Object> callBackObject) {
		return new MakeDepositAction(makeDeposit, callBackObject);
	}

	// for reconcilation.
	public static MakeDepositAction getMakeDepositAction(
			ClientAccount reconcilationAccount, double amount,
			ClientStatementRecord statementRecord) {
		return new MakeDepositAction(reconcilationAccount, amount,
				statementRecord);
	}

	public static EnterPaymentsAction getEnterPaymentsAction() {
		return new EnterPaymentsAction();
	}

	public static SyncOnlinePayeesAction getSyncOnlinePayeesAction() {
		return new SyncOnlinePayeesAction();
	}

	public static ImportBankFilesAction getImportBankFilesAction() {
		return new ImportBankFilesAction();
	}

	public static CreditCardChargeAction getCreditCardChargeAction() {
		return new CreditCardChargeAction();
	}

	public static PrintChecksAction getPrintChecksAction() {
		return new PrintChecksAction();
	}

	public static MatchTrasactionsAction getMatchTrasactionsAction() {
		return new MatchTrasactionsAction();
	}

	// ActionFactory

	public static CompanyHomeAction getCompanyHomeAction() {
		return new CompanyHomeAction();
	}

	public static MakeActiveAction getMakeActiveAction() {
		return new MakeActiveAction();

	}

	public static MakeInActiveAction getMakeInActiveAction() {
		return new MakeInActiveAction();

	}

	public static PreferencesAction getPreferencesAction(int catagory) {
		return new PreferencesAction(catagory);
	}

	public static IntegrateWithBusinessContactManagerAction getIntegrateWithBusinessContactManagerAction() {
		return new IntegrateWithBusinessContactManagerAction();
	}

	public static NewJournalEntryAction getNewJournalEntryAction() {
		return new NewJournalEntryAction();
	}

	public static NewAccountAction getNewAccountAction() {
		return new NewAccountAction();
	}

	// public static MergeAccountsAction getMergeVendorsAction() {
	// return new MergeAccountsAction(messages.mergeVendors(Global.get()
	// .vendors()));
	// }

	// public static MergeItemsAction getMergeItemsAction() {
	// return new MergeItemsAction(messages.mergeItems());
	// }

	// public static ManageSalesTaxCodesAction getManageSalesTaxCodesAction() {
	// String constant = messages.manageSalesTaxCodes();
	// return new ManageSalesTaxCodesAction(constant);
	// }

	public static CreditRatingListAction getCreditRatingListAction() {
		return new CreditRatingListAction();
	}

	public static CountryRegionListAction getCountryRegionListAction() {
		return new CountryRegionListAction();
	}

	public static FormLayoutsListAction getFormLayoutsListAction() {
		return new FormLayoutsListAction();
	}

	public static PayTypeListAction getPayTypeListAction() {
		return new PayTypeListAction();
	}

	public static ManageFiscalYearAction getManageFiscalYearAction() {
		return new ManageFiscalYearAction();
	}

	public static ChartOfAccountsAction getChartOfAccountsAction() {
		return new ChartOfAccountsAction();
	}

	public static ChartOfAccountsAction getChartOfAccountsAction(int accountType) {
		return new ChartOfAccountsAction(accountType);
	}

	public static SalesPersonListsAction getSalesPersonListAction() {
		return new SalesPersonListsAction();
	}

	public static NewbankAction getNewbankAction() {
		return new NewbankAction();
	}

	public static NewItemAction getNewItemAction(boolean forCustomer) {
		return new NewItemAction(forCustomer);
	}

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AccounterAsyncCallback<Object> callback) {
	// return new NewItemAction(messages.newItem(),
	// }

	public static DepreciationAction getDepriciationAction() {
		return new DepreciationAction();

	}

	public static NewTAXAgencyAction getNewTAXAgencyAction() {
		return new NewTAXAgencyAction();
	}

	// public static NewTaxItemAction getTaxItemAction() {
	// return new NewTaxItemAction(FinanceApplication.constants()
	// .newTaxItem());
	// }

	public static AdjustTAXAction getAdjustTaxAction() {
		return new AdjustTAXAction(2);
	}

	public static ChangePasswordAction getChangePasswordAction() {
		return new ChangePasswordAction();

	}

	public static UserDetailsAction getUserDetailsAction() {
		return new UserDetailsAction();
	}

	// public static ForgetPasswordAction getForgetPasswordAction(){
	// return new ForgetPasswordAction(messages.forgetPassword());
	//
	// }

	// Customers actions Factory

	public static NewCustomerAction getNewCustomerAction() {
		return new NewCustomerAction();
	}

	public static NewCustomerAction getNewCustomerAction(String quickAddText) {
		return new NewCustomerAction(quickAddText);
	}

	public static NewVendorAction getNewVendorAction(String quickAddText) {
		return new NewVendorAction(quickAddText);
	}

	public static NewCustomerAction getNewCustomerAction(
			ClientCustomer customer,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewCustomerAction(customer, callBackObject);
	}

	public static CustomersHomeAction getCustomersHomeAction() {
		return new CustomersHomeAction();
	}

	public static NewQuoteAction getNewQuoteAction(int type) {
		return new NewQuoteAction(type);
	}

	// public static NewQuoteAction getNewQuoteAction(ClientEstimate quote,
	// AccounterAsyncCallback<Object> callBackObject, int type) {
	// return new NewQuoteAction(quote, callBackObject,
	// type);
	// }

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AccounterAsyncCallback<Object> callBackObject) {
	// return new NewItemAction(messages.newItem(),
	// }

	public static TaxDialogAction getTaxAction() {
		return new TaxDialogAction();
	}

	public static NewInvoiceAction getNewInvoiceAction() {
		return new NewInvoiceAction();
	}

	public static NewInvoiceAction getNewInvoiceAction(ClientInvoice invoice,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewInvoiceAction(invoice, callBackObject);
	}

	public static NewCashSaleAction getNewCashSaleAction() {
		return new NewCashSaleAction();
	}

	public static NewCashSaleAction getNewCashSaleAction(
			ClientCashSales cashSales,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewCashSaleAction(cashSales, callBackObject);
	}

	public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction() {
		return new NewCreditsAndRefundsAction();
	}

	// public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction(
	// ClientCustomerCreditMemo creditMemo,
	// AccounterAsyncCallback<Object> callBackObject) {
	// return new NewCreditsAndRefundsAction(messages.newCreditsAndRefunds(),
	// creditMemo, callBackObject);
	// }

	public static ReceivePaymentAction getReceivePaymentAction() {
		return new ReceivePaymentAction();
	}

	public static ReceivePaymentAction getReceivePaymentAction(
			ClientReceivePayment receivePayment,
			AccounterAsyncCallback<Object> callBackObject) {
		return new ReceivePaymentAction(messages.receivePayment(),
				receivePayment, callBackObject);
	}

	public static CustomerRefundAction getCustomerRefundAction() {
		return new CustomerRefundAction();
	}

	// public static CustomerRefundAction getCustomerRefundAction(
	// ClientCustomerRefund customerRefund,
	// AccounterAsyncCallback<Object> callBackObject) {
	// return new CustomerRefundAction(messages.customerRefund(),
	// callBackObject);
	// }

	public static CreateStatementAction getCreateStatementAction() {
		return new CreateStatementAction();
	}

	public static CustomersAction getCustomersAction() {
		return new CustomersAction();
	}

	public static ItemsAction getItemsAction(boolean customer, boolean vendor) {
		if (customer) {
			return new ItemsAction(Global.get().customer());
		} else if (vendor) {
			return new ItemsAction(Global.get().vendor());
		} else
			return new ItemsAction(messages.bothCustomerAndVendor(Global.get()
					.Customer(), Global.get().Vendor()));
	}

	public static NewSalesperSonAction getNewSalesperSonAction() {
		return new NewSalesperSonAction();
	}

	public static SalesPersonAction getSalesPersonAction() {
		return new SalesPersonAction();
	}

	public static CustomerPaymentsAction getNewCustomerPaymentAction() {
		return new CustomerPaymentsAction();
	}

	public static BrandingThemeComboAction getBrandingThemeComboAction() {
		return new BrandingThemeComboAction();
	}

	public static EmailThemeComboAction getEmailThemeComboAction() {
		return new EmailThemeComboAction();
	}

	public static EmailViewAction getEmailViewAction() {
		return new EmailViewAction();
	}

	public static InvoiceListViewAction getInvoiceListViewAction() {
		return new InvoiceListViewAction();
	}

	public static PaymentDialogAction getPaymentDialogAction() {
		return new PaymentDialogAction();
	}

	// Fixed Assests action factory

	public static NewFixedAssetAction getNewFixedAssetAction() {
		return new NewFixedAssetAction();
	}

	public static SellingRegisteredItemAction getSellingRegisteredItemAction() {
		return new SellingRegisteredItemAction();
	}

	public static DisposingRegisteredItemAction getDiposingRegisteredItemAction() {
		return new DisposingRegisteredItemAction();
	}

	public static PendingItemsListAction getPendingItemsListAction() {
		return new PendingItemsListAction();

	}

	public static RegisteredItemsListAction getRegisteredItemsListAction() {
		return new RegisteredItemsListAction();
	}

	public static SoldDisposedFixedAssetsListAction getSoldDisposedListAction() {
		return new SoldDisposedFixedAssetsListAction();
	}

	public static HistoryListAction getHistoryListAction() {
		return new HistoryListAction();
	}

	public static ReportsHomeAction getReportsHomeAction() {
		return new ReportsHomeAction();
	}

	public static TAXAgencyListAction getTAXAgencyListAction() {
		return new TAXAgencyListAction();
	}

	public static TransactionDetailByAccountAndCategoryAction getTransactionDetailByAccountAndCategoryAction() {
		return new TransactionDetailByAccountAndCategoryAction();
	}

	public static MostProfitableCustomersAction getMostProfitableCustomersAction() {
		return new MostProfitableCustomersAction();
	}

	public static VATDetailsReportAction getVATDetailsReportAction() {
		return new VATDetailsReportAction();
	}

	public static VATSummaryReportAction getVATSummaryReportAction() {
		return new VATSummaryReportAction();
	}

	public static VAT100ReportAction getVAT100ReportAction() {
		return new VAT100ReportAction();
	}

	public static VATUncategorisedAmountsReportAction getVATUncategorisedAmountsReportAction() {
		return new VATUncategorisedAmountsReportAction();
	}

	public static ECSalesListAction getECSalesListAction() {
		return new ECSalesListAction();
	}

	public static ECSalesListDetailAction getECSalesListDetailAction() {
		return new ECSalesListDetailAction();
	}

	public static ReverseChargeListAction getReverseChargeListAction() {
		return new ReverseChargeListAction();
	}

	public static ReverseChargeListDetailAction getReverseChargeListDetailAction() {
		return new ReverseChargeListDetailAction();
	}

	public static VaTItemDetailAction getVaTItemDetailAction() {
		return new VaTItemDetailAction();
	}

	public static NewVatItemAction getNewVatItemAction() {
		return new NewVatItemAction();
	}

	public static FileTAXAction getFileTAXAction() {
		return new FileTAXAction();
	}

	public static VatItemListAction getVatItemListAction() {
		return new VatItemListAction();
	}

	public static ManageTAXCodesListAction getTAXCodeListAction() {
		return new ManageTAXCodesListAction();
	}

	public static NewTAXCodeAction getNewTAXCodeAction() {
		return new NewTAXCodeAction();
	}

	public static AdjustTAXAction getVatAdjustmentAction() {
		return new AdjustTAXAction(1);

	}

	public static PayTAXAction getpayTAXAction() {
		return new PayTAXAction();

	}

	public static ReceiveVATAction getreceiveVATAction() {
		return new ReceiveVATAction();
	}

	public static VendorsHomeAction getVendorsHomeAction() {
		return new VendorsHomeAction();
	}

	public static NewVendorAction getNewVendorAction() {
		return new NewVendorAction();
	}

	public static NewVendorAction getNewVendorAction(ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		return new NewVendorAction();
	}

	public static NewCashPurchaseAction getNewCashPurchaseAction() {
		return new NewCashPurchaseAction();
	}

	public static NewCashPurchaseAction getNewCashPurchaseAction(
			ClientCashPurchase cashPurchase,
			AccounterAsyncCallback<Object> callback) {
		return new NewCashPurchaseAction(cashPurchase, callback);
	}

	public static NewCreditMemoAction getNewCreditMemoAction() {
		return new NewCreditMemoAction();
	}

	public static EnterBillsAction getEnterBillsAction() {
		return new EnterBillsAction();
	}

	public static PayBillsAction getPayBillsAction() {
		return new PayBillsAction();
	}

	public static IssuePaymentsAction getIssuePaymentsAction() {
		return new IssuePaymentsAction();
	}

	public static VendorPaymentsAction getNewVendorPaymentAction() {
		return new VendorPaymentsAction();
	}

	public static RecordExpensesAction getRecordExpensesAction() {
		return new RecordExpensesAction();
	}

	public static ServicesOverviewAction getServicesOverviewAction() {
		return new ServicesOverviewAction();
	}

	public static BuyChecksAndFormsAction getBuyChecksAndFormsAction() {
		return new BuyChecksAndFormsAction("");
	}

	public static VendorsListAction getVendorsAction() {
		return new VendorsListAction();
	}

	public static BillsAction getBillsAction() {
		return new BillsAction();
	}

	public static ExpensesAction getExpensesAction(String viewType) {
		return new ExpensesAction(viewType);
	}

	public static VendorPaymentsListAction getVendorPaymentsAction() {
		return new VendorPaymentsListAction();
	}

	public static PurchaseOrderAction getPurchaseOrderAction() {
		return new PurchaseOrderAction();
	}

	public static NewItemReceiptAction getItemReceiptAction() {
		return new NewItemReceiptAction();
	}

	public static CashExpenseAction CashExpenseAction() {
		return new CashExpenseAction();
	}

	public static EmployeeExpenseAction EmployeeExpenseAction() {
		return new EmployeeExpenseAction();
	}

	public static CreditCardExpenseAction CreditCardExpenseAction() {
		return new CreditCardExpenseAction();
	}

	public static PreviousClaimAction getPreviousClaimAction() {
		return new PreviousClaimAction();

	}

	public static ExpenseClaimsAction getExpenseClaimsAction(int selectedTab) {
		return new ExpenseClaimsAction(selectedTab);
	}

	public static MeasurementListAction getMeasurementsAction() {
		return new MeasurementListAction();
	}

	public static JobListAction getJobListAction() {
		return new JobListAction();
	}

	public static EditProfileAction getEditProfileAction() {
		return new EditProfileAction();
	}

	public static RecurringsListAction getRecurringsListAction() {
		return new RecurringsListAction();
	}

	public static RemindersListAction getRemindersListAction() {
		return new RemindersListAction();
	}

	public static Prepare1099MISCAction getPrepare1099MISCAction() {
		return new Prepare1099MISCAction();
	}

	public static BudgetAction getBudgetActions() {
		return new BudgetAction();
	}

	public static NewBudgetAction getNewBudgetAction() {
		return new NewBudgetAction();
	}

	public static UsersActivityListAction getUsersActivityListAction() {
		return new UsersActivityListAction();
	}

	public static TDSVendorsListAction getTDSVendorsAction(boolean isTDSView) {
		return new TDSVendorsListAction(isTDSView);
	}

	public static NewReconcileAccountAction getNewReconciliationAction() {
		return new NewReconcileAccountAction();
	}

	public static ReconciliationsListAction getReconciliationsListAction() {
		return new ReconciliationsListAction();
	}

	public static TDSPayAction getpayTDSAction() {
		return new TDSPayAction();
	}

	public static MISC1099TransactionDetailAction getMisc1099TransactionDetailAction() {
		return new MISC1099TransactionDetailAction();
	}

	public static StockSettingsAction getStockSettingsAction() {
		return new StockSettingsAction();
	}

	/*
	 * public static BudgetReportAction getBudgetReportsAction(int i) { switch
	 * (i) { case 1: return new BudgetReportAction(messages.accountVScustom(),
	 * i); case 2: return new BudgetReportAction(messages.accountVSmonths(), i);
	 * case 3: return new BudgetReportAction(messages.accountVSquaters(), i);
	 * case 4: return new BudgetReportAction(messages.accountVSyears(), i);
	 * default: return new BudgetReportAction(messages.accountVScustom(), i); }
	 * 
	 * }
	 */

	public static TaxHistoryAction getTaxHistoryAction() {
		return new TaxHistoryAction();
	}

	public static VatExceptionDetailReportAction getVATExceptionDetailsReportAction() {
		return new VatExceptionDetailReportAction();
	}

	public static TranslationAction getTranslationAction() {
		return new TranslationAction();
	}

	public static SearchInputAction getSearchInputAction() {
		return new SearchInputAction();
	}

	public static BudgetOverviewReportAction getBudgetOverView() {
		return new BudgetOverviewReportAction();
	}

	public static BudgetvsActualsAction getBudgetVsActionReport() {
		return new BudgetvsActualsAction();
	}

	public static AuditHistoryAction getAuditHistory(ClientActivity object) {
		return new AuditHistoryAction(object);

	}

	public static TransactionsCenterAction getTransactionCenterAction() {
		return new TransactionsCenterAction();
	}

	public static DepreciationSheduleAction getDepreciationSheduleAction() {
		return new DepreciationSheduleAction();
	}

	public static CheckPrintSettingAction getCheckPrintSettingAction() {
		return new CheckPrintSettingAction();
	}

	public static DeleteCompanyAction getDeleteCompanyAction() {
		return new DeleteCompanyAction();
	}

	public static CustomerCentreAction getCustomerCentre() {
		return new CustomerCentreAction();
	}

	public static TDSChalanDetailsAction getTDSChalanDetailsView() {
		return new TDSChalanDetailsAction();
	}

	public static TdsDeductorMasterAction getTdsDeductorMasters() {
		return new TdsDeductorMasterAction();
	}

	public static TDSResponsiblePersonAction getTDSResponsiblePerson() {
		return new TDSResponsiblePersonAction();
	}

	public static ChalanListViewAction getTDSChalanListView() {
		return new ChalanListViewAction();
	}

	public static ETdsFillingAction getETdsFillingView() {
		return new ETdsFillingAction();
	}

	public static TDSAcknowledgmentAction getTDSAcknowledgmentAction() {
		return new TDSAcknowledgmentAction();
	}

	public static TDSFiledDetailsAction getTDSFiledDetailsAction() {
		return new TDSFiledDetailsAction();
	}

	public static VendorCenterAction getVendorCentreAction() {
		return new VendorCenterAction();
	}

	public static TDSForm16AAction getTDSForm16AAction() {
		return new TDSForm16AAction();
	}

	public static CreateIRASInformationFileAction getIARSInformationAction() {
		return new CreateIRASInformationFileAction();
	}

	public static StatementReconcilationAction getStatementReconcilationAction(
			long accountid, ClientStatement statementId) {
		return new StatementReconcilationAction(accountid, statementId);
	}

	public static StatementImportViewAction getStatementImportViewAction(
			List<String[]> data, long accountId) {
		return new StatementImportViewAction(data, accountId);
	}

	public static Action<?> getBankFeesAction() {
		return null;
	}

	public static Action<?> getMinorAdjustmentsAction() {
		return null;
	}

	public static DepositAction getDepositAction() {
		return new DepositAction();
	}

	public static RecurringTransactionDialogAction getRecurringTransactionDialogAction() {
		return new RecurringTransactionDialogAction();
	}

	public static UploadCSVFileDialogAction getUploadCSVFileDialog() {
		return new UploadCSVFileDialogAction();
	}

	public static JobActualDetailAction getJobActualCostDetailReportAction() {
		return new JobActualDetailAction();
	}

	public static ItemActualCostDetailReportAction getItemActualCostDetailReportAction() {
		return new ItemActualCostDetailReportAction();
	}

	public static NewJobAction getNewJobAction(ClientCustomer customer) {
		return new NewJobAction(customer);
	}

	public static InventoryDetailsAction getInventoryDetailsAction() {
		return new InventoryDetailsAction();
	}

	public static Windows8MenuAction getWindows8MenuAction() {
		return new Windows8MenuAction();
	}

	public static IPadMenuAction getIpadMenuAction() {
		return new IPadMenuAction();
	}

	public static <T> DropDownAction<T> getDropDOwn() {
		return new DropDownAction<T>();
	}

	public static TransactionsListViewGridAction getTransactionListView(
			String menuItemName) {
		return new TransactionsListViewGridAction(menuItemName);
	}

	public static VendorTransactionHistoryListAction getVendorTransactionListHistory(
			ClientVendor selectedVendor) {
		return new VendorTransactionHistoryListAction(selectedVendor);
	}

	public static CustomerTransactionHistoryListAction getCustomerTransactionListHistory(
			ClientCustomer selectedCustomer) {
		return new CustomerTransactionHistoryListAction(selectedCustomer);
	}

	public static PaypalTransactionListAction getPaypalTransaction() {
		return new PaypalTransactionListAction();
	}

	public static InventoryTransactionHistoryListAction getInventoryTransactionListHistory(
			ClientItem selectedItem) {
		return new InventoryTransactionHistoryListAction(selectedItem);
	}

	public static NewConsultAction getNewConsultantAction() {
		return new NewConsultAction();
	}

	public static NewConsultAction getNewConsultantAction(String quickAddText) {
		return new NewConsultAction(quickAddText);
	}

	public static NewConsultAction getNewConsultantAction(
			ClientItem customer,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewConsultAction(customer, callBackObject);
	}
}
