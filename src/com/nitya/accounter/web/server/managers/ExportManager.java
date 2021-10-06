package com.nitya.accounter.web.server.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nitya.accounter.core.reports.generators.APAgingDetailRG;
import com.nitya.accounter.core.reports.generators.APAgingSummaryRG;
import com.nitya.accounter.core.reports.generators.ARAgingDetailRG;
import com.nitya.accounter.core.reports.generators.ARAgingSummaryRG;
import com.nitya.accounter.core.reports.generators.AmountsDueToVendorRG;
import com.nitya.accounter.core.reports.generators.AutomaticTransactionRG;
import com.nitya.accounter.core.reports.generators.BalanceSheetRG;
import com.nitya.accounter.core.reports.generators.BankCheckDetailRG;
import com.nitya.accounter.core.reports.generators.BankDepositsRG;
import com.nitya.accounter.core.reports.generators.BudgetReportGenerator;
import com.nitya.accounter.core.reports.generators.BudgetVsActualsRG;
import com.nitya.accounter.core.reports.generators.CashFlowStatementRG;
import com.nitya.accounter.core.reports.generators.CustomerStatementRG;
import com.nitya.accounter.core.reports.generators.CustomerTransactionHistoryRG;
import com.nitya.accounter.core.reports.generators.DepreciationScheduleRG;
import com.nitya.accounter.core.reports.generators.ECSalesListDetailRG;
import com.nitya.accounter.core.reports.generators.ECSalesListReportGenerator;
import com.nitya.accounter.core.reports.generators.EstimatesByJobRG;
import com.nitya.accounter.core.reports.generators.ExpenseRG;
import com.nitya.accounter.core.reports.generators.IReportGenerator;
import com.nitya.accounter.core.reports.generators.IncomeByCustomerDetailRG;
import com.nitya.accounter.core.reports.generators.InventoryDetailsRG;
import com.nitya.accounter.core.reports.generators.InventoryStockStatusByItemRG;
import com.nitya.accounter.core.reports.generators.InventoryStockStatusByVendorRG;
import com.nitya.accounter.core.reports.generators.InventoryValutionDetailRG;
import com.nitya.accounter.core.reports.generators.InventoryValutionSummaryRG;
import com.nitya.accounter.core.reports.generators.ItemActualCostDetailRG;
import com.nitya.accounter.core.reports.generators.JobActualCostDetailRG;
import com.nitya.accounter.core.reports.generators.JobProfitabilityRG;
import com.nitya.accounter.core.reports.generators.JobProfitabilitySummaryRG;
import com.nitya.accounter.core.reports.generators.MISC1099TransactionDetailRG;
import com.nitya.accounter.core.reports.generators.MissingChecksRG;
import com.nitya.accounter.core.reports.generators.MostProfitableCustomerRG;
import com.nitya.accounter.core.reports.generators.PayHeadDetailRG;
import com.nitya.accounter.core.reports.generators.PayHeadSummaryRG;
import com.nitya.accounter.core.reports.generators.PaySheetRG;
import com.nitya.accounter.core.reports.generators.PaySlipDetailRG;
import com.nitya.accounter.core.reports.generators.PaySlipSummaryRG;
import com.nitya.accounter.core.reports.generators.PriorVATReturnsRG;
import com.nitya.accounter.core.reports.generators.ProfitAndLossByClassOrLocationorJobRG;
import com.nitya.accounter.core.reports.generators.ProfitAndLossRG;
import com.nitya.accounter.core.reports.generators.PurchasesByItemDetailRG;
import com.nitya.accounter.core.reports.generators.PurchasesByVendorDetailRG;
import com.nitya.accounter.core.reports.generators.PurchasesByVendorSummaryRG;
import com.nitya.accounter.core.reports.generators.PurchasesByitemSummaryRG;
import com.nitya.accounter.core.reports.generators.PurchasesOpenRG;
import com.nitya.accounter.core.reports.generators.RealisedExchangeLossesAndGainsRG;
import com.nitya.accounter.core.reports.generators.ReconciliationAccountsListRG;
import com.nitya.accounter.core.reports.generators.ReconciliationDescipancyRG;
import com.nitya.accounter.core.reports.generators.ReconciliationRG;
import com.nitya.accounter.core.reports.generators.ReverseChargeListDetailRG;
import com.nitya.accounter.core.reports.generators.ReverseChargeListRG;
import com.nitya.accounter.core.reports.generators.SalesByCustomerDetailRG;
import com.nitya.accounter.core.reports.generators.SalesByCustomerSummaryRG;
import com.nitya.accounter.core.reports.generators.SalesByItemDetailRG;
import com.nitya.accounter.core.reports.generators.SalesByItemSummaryRG;
import com.nitya.accounter.core.reports.generators.SalesOrPurchasesByClassOrLocationDetailRG;
import com.nitya.accounter.core.reports.generators.SalesOrPurchasesByClassOrLocationSummaryRG;
import com.nitya.accounter.core.reports.generators.SalesOrderOpenRG;
import com.nitya.accounter.core.reports.generators.SalesTAXLiabilityRG;
import com.nitya.accounter.core.reports.generators.TAXExceptionDetailRG;
import com.nitya.accounter.core.reports.generators.TAXItemDetailRG;
import com.nitya.accounter.core.reports.generators.TDSAcknowledgementRG;
import com.nitya.accounter.core.reports.generators.TrailBalanceRG;
import com.nitya.accounter.core.reports.generators.TransactionDetailByAccountAndClassRG;
import com.nitya.accounter.core.reports.generators.TransactionDetailByAccountAndJobRG;
import com.nitya.accounter.core.reports.generators.TransactionDetailByAccountAndLocationRG;
import com.nitya.accounter.core.reports.generators.TransactionDetailByTaxItemRG;
import com.nitya.accounter.core.reports.generators.TranxDetailByAccountOrGeneralLedgerRG;
import com.nitya.accounter.core.reports.generators.UnBilledCostByJobRG;
import com.nitya.accounter.core.reports.generators.UnCategorisedVATAmountRG;
import com.nitya.accounter.core.reports.generators.UnRealisedExchangeLossesAndGainsRG;
import com.nitya.accounter.core.reports.generators.VAT100ReportGenerator;
import com.nitya.accounter.core.reports.generators.VATDetailRG;
import com.nitya.accounter.core.reports.generators.VATExceptionDetailRG;
import com.nitya.accounter.core.reports.generators.VATItemDetailRG;
import com.nitya.accounter.core.reports.generators.VATItemSummaryRG;
import com.nitya.accounter.core.reports.generators.VendorStatementRG;
import com.nitya.accounter.core.reports.generators.VendorTransactionHistoryRG;
import com.nitya.accounter.web.client.core.ReportInput;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;

public class ExportManager extends Manager {

	private FinanceTool financeTool;

	public static Map<Integer, Class<? extends IReportGenerator>> reportTypeGenerator = new HashMap<Integer, Class<? extends IReportGenerator>>();

	static {
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_PROFITANDLOSS,
				ProfitAndLossRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_BALANCESHEET,
				BalanceSheetRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_TRIALBALANCE,
				TrailBalanceRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_TRANSACTIONDETAILBYTAXITEM,
				TransactionDetailByTaxItemRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT,
				TranxDetailByAccountOrGeneralLedgerRG.class);

		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PAY_HEAD_SUMMARY_REPORT,
				PayHeadSummaryRG.class);

		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PAY_HEAD_DETAIL_REPORT,
				PayHeadDetailRG.class);

		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_EXPENSE,
				ExpenseRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_AR_AGEINGSUMMARY,
				ARAgingSummaryRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_AR_AGEINGDETAIL,
				ARAgingDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY,
				CustomerTransactionHistoryRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_SALESBYCUSTOMERSUMMARY,
				SalesByCustomerSummaryRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_SALESBYCUSTOMERDETAIL,
				SalesByCustomerDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_SALESBYITEMSUMMARY,
				SalesByItemSummaryRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_SALESBYITEMDETAIL,
				SalesByItemDetailRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_SALESORDER_OPEN,
				SalesOrderOpenRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_AP_AGEINGSUMMARY,
				APAgingSummaryRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_AP_AGEINGDETAIL,
				APAgingDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_VENDORTRANSACTIONHISTORY,
				VendorTransactionHistoryRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PURCHASEBYVENDORSUMMARY,
				PurchasesByVendorSummaryRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PURCHASEBYVENDORDETAIL,
				PurchasesByVendorDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PURCHASEBYITEMSUMMARY,
				PurchasesByitemSummaryRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PURCHASEBYITEMDETAIL,
				PurchasesByItemDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PURCHASEORDER_OPEN,
				PurchasesOpenRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_PRIORVATRETURNS,
				PriorVATReturnsRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_VAT100,
				VAT100ReportGenerator.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_VATDETAIL,
				VATDetailRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_VATITEMDETAIL,
				VATItemDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_UNCATEGORISEDVATAMOUNT,
				UnCategorisedVATAmountRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_VATITEMSUMMARY,
				VATItemSummaryRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_EC_SALES_LIST,
				ECSalesListReportGenerator.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_ECSCALESLISTDETAIL,
				ECSalesListDetailRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_SALESTAXLIABILITY,
				SalesTAXLiabilityRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_REVERSECHARGELIST,
				ReverseChargeListRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_REVERSECHARGELISTDETAIL,
				ReverseChargeListDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_MOSTPROFITABLECUSTOMER,
				MostProfitableCustomerRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_CASHFLOWSTATEMENT,
				CashFlowStatementRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_AMOUNTSDUETOVENDOR,
				AmountsDueToVendorRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_CUSTOMERSTATEMENT,
				CustomerStatementRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_SALESBYLOCATIONDETAIL,
				SalesOrPurchasesByClassOrLocationDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_SALESBYLOCATIONDETAILFORLOCATION,
				SalesOrPurchasesByClassOrLocationSummaryRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PROFITANDLOSSBYLOCATION,
				ProfitAndLossByClassOrLocationorJobRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_BUDGET,
				BudgetReportGenerator.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_1099TRANSACTIONDETAIL,
				MISC1099TransactionDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_SALESBYCLASSDETAIL,
				SalesOrPurchasesByClassOrLocationDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_SALESBYCLASSDETAILFORCLASS,
				SalesOrPurchasesByClassOrLocationSummaryRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PROFITANDLOSSBYCLASS,
				ProfitAndLossByClassOrLocationorJobRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_GENERAL_LEDGER_REPORT,
				TranxDetailByAccountOrGeneralLedgerRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_TAX_ITEM_DETAIL,
				TAXItemDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_VAT_EXCEPTION_DETAIL,
				VATExceptionDetailRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_VENDORSTATEMENT,
				VendorStatementRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_DEPRECIATIONSHEDULE,
				DepreciationScheduleRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_RECONCILATIONS,
				ReconciliationRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_RECONCILATION_ACCOUNTSLIST,
				ReconciliationAccountsListRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_TAX_EXCEPTION_DETAIL,
				TAXExceptionDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REALISED_EXCHANGE_LOSSES_AND_GAINS,
				RealisedExchangeLossesAndGainsRG.class);
		reportTypeGenerator.put(
				IReportGenerator.UNREALISED_EXCHANGE_LOSSES_AND_GAINS,
				UnRealisedExchangeLossesAndGainsRG.class);
		reportTypeGenerator.put(IReportGenerator.PRINT_FILE_TAX, null);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_INVENTORY_VALUTION_SUMMARY,
				InventoryValutionSummaryRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_INVENTORY_VALUTION_DETAIL,
				InventoryValutionDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_INVENTORY_STOCK_STATUS_BYITEM,
				InventoryStockStatusByItemRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_INVENTORY_STOCK_STATUS_BYVENDOR,
				InventoryStockStatusByVendorRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_BANK_DEPOSIT_REPORT,
				BankDepositsRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_BANK_CHECK_DETAIL_REPORT,
				BankCheckDetailRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_MISSION_CHECKS,
				MissingChecksRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_RECONCILIATION_DISCREPANCY,
				ReconciliationDescipancyRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_BUDGET_VS_ACTUALS,
				BudgetVsActualsRG.class);

		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_JOB_ACTUAL_COST_DETAIL,
				JobActualCostDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_JOB_PROFITABILTY_SUMMARY,
				JobProfitabilitySummaryRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_UNBILLED_COSTS_BY_JOB,
				UnBilledCostByJobRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_ITEM_ACTUAL_COST_DETAIL,
				ItemActualCostDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PROFITANDLOSSBYJOB,
				ProfitAndLossByClassOrLocationorJobRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_JOB_PROFITABILITY_BY_JOBID,
				JobProfitabilityRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_ESTIMATE_BY_JOB,
				EstimatesByJobRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_TDS_ACKNOWLEDGEMENT_REPORT,
				TDSAcknowledgementRG.class);
		reportTypeGenerator
				.put(IReportGenerator.REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_JOB,
						TransactionDetailByAccountAndJobRG.class);
		reportTypeGenerator
				.put(IReportGenerator.REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_CLASS,
						TransactionDetailByAccountAndClassRG.class);
		reportTypeGenerator
				.put(IReportGenerator.REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_LOCATION,
						TransactionDetailByAccountAndLocationRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_AUTOMATIC_TRANSACTIONS,
				AutomaticTransactionRG.class);
		reportTypeGenerator
				.put(IReportGenerator.REPORT_TYPE_PURCHASEBYLOCATIONDETAILFORLOCATION,
						SalesOrPurchasesByClassOrLocationSummaryRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PURCHASEBYCLASSDETAILFORCLASS,
				SalesOrPurchasesByClassOrLocationSummaryRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PURCHASEBYLOCATIONDETAIL,
				SalesOrPurchasesByClassOrLocationDetailRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_PURCHASEBYCLASSDETAIL,
				SalesOrPurchasesByClassOrLocationDetailRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_INVENTORY_DETAILS,
				InventoryDetailsRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_PAYSLIP_SUMMARY,
				PaySlipSummaryRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_PAYSLIP_DETAIL,
				PaySlipDetailRG.class);
		reportTypeGenerator.put(IReportGenerator.REPORT_TYPE_PAYSHEET,
				PaySheetRG.class);
		reportTypeGenerator.put(
				IReportGenerator.REPORT_TYPE_INCOMEBY_CUSTOMERDETAIL,
				IncomeByCustomerDetailRG.class);
	}

	public ExportManager(FinanceTool financeTool) {
		this.financeTool = financeTool;
	}

	public List<String> exportReportToFile(Long companyId, int exportType,
			int reportType, long startDate, long endDate,
			List<ReportInput> input) throws AccounterException {
		try {
			IReportGenerator generator = getReportGeneratoryByType(reportType);
			List<String> file = generator.generate(exportType,
					getCompany(companyId), financeTool, reportType, startDate,
					endDate, input);

			return file;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterException(e);
		}
	}

	private IReportGenerator getReportGeneratoryByType(int reportType)
			throws InstantiationException, IllegalAccessException {
		Class<? extends IReportGenerator> clazz = reportTypeGenerator
				.get(reportType);
		if (clazz == null) {
			return null;
		}
		IReportGenerator generator = clazz.newInstance();
		return generator;
	}

}
