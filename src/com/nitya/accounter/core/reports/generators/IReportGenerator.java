package com.nitya.accounter.core.reports.generators;

import java.io.IOException;
import java.util.List;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.ITemplate;
import com.nitya.accounter.web.client.core.ReportInput;
import com.nitya.accounter.web.server.FinanceTool;

/**
 * Used to Generate a Report
 * 
 * @author Prasanna Kumar G
 * 
 */
public interface IReportGenerator {

	/** Report Generation Types */
	public static final int GENERATION_TYPE_PDF = 1001;
	public static final int GENERATION_TYPE_CSV = 1002;

	/** Report Types */
	public final static int REPORT_TYPE_PROFITANDLOSS = 111;
	public final static int REPORT_TYPE_BALANCESHEET = 112;
	public final static int REPORT_TYPE_TRIALBALANCE = 113;
	public final static int REPORT_TYPE_TRANSACTIONDETAILBYTAXITEM = 114;
	// public final static int REPORT_TYPE_TRANSACTIONDETAILBYTAXCODE = 114;
	public final static int REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT = 115;
	public final static int REPORT_TYPE_EXPENSE = 116;
	public final static int REPORT_TYPE_AR_AGEINGSUMMARY = 117;
	public final static int REPORT_TYPE_AR_AGEINGDETAIL = 118;
	public final static int REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY = 119;
	public final static int REPORT_TYPE_SALESBYCUSTOMERSUMMARY = 121;
	public final static int REPORT_TYPE_SALESBYCUSTOMERDETAIL = 122;
	public final static int REPORT_TYPE_SALESBYITEMSUMMARY = 123;
	public final static int REPORT_TYPE_SALESBYITEMDETAIL = 124;
	public final static int REPORT_TYPE_SALESORDER_OPEN = 125;
	// public final static int REPORT_TYPE_SALESORDER_CLOSE = 126;
	public final static int REPORT_TYPE_AP_AGEINGSUMMARY = 127;
	public final static int REPORT_TYPE_AP_AGEINGDETAIL = 128;
	public final static int REPORT_TYPE_VENDORTRANSACTIONHISTORY = 129;
	public final static int REPORT_TYPE_PURCHASEBYVENDORSUMMARY = 130;
	public final static int REPORT_TYPE_PURCHASEBYVENDORDETAIL = 131;
	public final static int REPORT_TYPE_PURCHASEBYITEMSUMMARY = 132;
	public final static int REPORT_TYPE_PURCHASEBYITEMDETAIL = 133;
	public final static int REPORT_TYPE_PURCHASEORDER_OPEN = 134;
	// public final static int REPORT_TYPE_PURCHASEORDER_CLOSE = 135;
	public final static int REPORT_TYPE_PRIORVATRETURNS = 136;
	public final static int REPORT_TYPE_VAT100 = 137;
	public final static int REPORT_TYPE_VATDETAIL = 138;
	public final static int REPORT_TYPE_VATITEMDETAIL = 139;
	public final static int REPORT_TYPE_UNCATEGORISEDVATAMOUNT = 140;
	public final static int REPORT_TYPE_VATITEMSUMMARY = 141;
	public final static int REPORT_TYPE_EC_SALES_LIST = 142;
	public final static int REPORT_TYPE_ECSCALESLISTDETAIL = 143;
	public final static int REPORT_TYPE_SALESTAXLIABILITY = 144;
	public final static int REPORT_TYPE_REVERSECHARGELIST = 145;
	public final static int REPORT_TYPE_REVERSECHARGELISTDETAIL = 146;
	public final static int REPORT_TYPE_MOSTPROFITABLECUSTOMER = 147;
	public final static int REPORT_TYPE_CASHFLOWSTATEMENT = 148;
	public final static int REPORT_TYPE_AMOUNTSDUETOVENDOR = 149;
	public final static int REPORT_TYPE_CUSTOMERSTATEMENT = 150;
	public final static int REPORT_TYPE_SALESBYLOCATIONDETAIL = 151;
	public final static int REPORT_TYPE_SALESBYLOCATIONDETAILFORLOCATION = 152;
	public final static int REPORT_TYPE_PROFITANDLOSSBYLOCATION = 153;
	public final static int REPORT_TYPE_BUDGET = 154;
	public final static int REPORT_TYPE_1099TRANSACTIONDETAIL = 158;
	public final static int REPORT_TYPE_SALESBYCLASSDETAIL = 159;
	public final static int REPORT_TYPE_SALESBYCLASSDETAILFORCLASS = 160;
	public final static int REPORT_TYPE_PROFITANDLOSSBYCLASS = 161;
	public final static int REPORT_TYPE_GENERAL_LEDGER_REPORT = 162;
	public final static int REPORT_TYPE_TAX_ITEM_DETAIL = 165;
	public final static int REPORT_TYPE_VAT_EXCEPTION_DETAIL = 166;
	public final static int REPORT_TYPE_VENDORSTATEMENT = 167;
	public final static int REPORT_TYPE_DEPRECIATIONSHEDULE = 168;
	public final static int REPORT_TYPE_RECONCILATIONS = 169;
	public final static int REPORT_TYPE_RECONCILATION_ACCOUNTSLIST = 170;
	public final static int REPORT_TYPE_TAX_EXCEPTION_DETAIL = 171;
	public final static int REALISED_EXCHANGE_LOSSES_AND_GAINS = 172;
	public final static int UNREALISED_EXCHANGE_LOSSES_AND_GAINS = 173;
	public final static int PRINT_FILE_TAX = 174;
	public final static int REPORT_TYPE_INVENTORY_VALUTION_SUMMARY = 175;
	public final static int REPORT_TYPE_INVENTORY_VALUTION_DETAIL = 176;
	public final static int REPORT_TYPE_INVENTORY_STOCK_STATUS_BYITEM = 177;
	public final static int REPORT_TYPE_INVENTORY_STOCK_STATUS_BYVENDOR = 178;
	public final static int REPORT_TYPE_BANK_DEPOSIT_REPORT = 179;
	public final static int REPORT_TYPE_BANK_CHECK_DETAIL_REPORT = 180;
	public final static int REPORT_TYPE_MISSION_CHECKS = 181;
	public final static int REPORT_TYPE_RECONCILIATION_DISCREPANCY = 182;
	public final static int REPORT_TYPE_BUDGET_VS_ACTUALS = 183;

	public final static int REPORT_TYPE_JOB_ACTUAL_COST_DETAIL = 184;
	public final static int REPORT_TYPE_JOB_PROFITABILTY_SUMMARY = 185;
	public final static int REPORT_TYPE_UNBILLED_COSTS_BY_JOB = 186;
	public final static int REPORT_TYPE_ITEM_ACTUAL_COST_DETAIL = 187;
	public final static int REPORT_TYPE_PROFITANDLOSSBYJOB = 189;
	public final static int REPORT_TYPE_JOB_PROFITABILITY_BY_JOBID = 190;
	public final static int REPORT_TYPE_ESTIMATE_BY_JOB = 191;
	public final static int REPORT_TYPE_TDS_ACKNOWLEDGEMENT_REPORT = 192;
	public final static int REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_JOB = 193;
	public final static int REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_CLASS = 194;
	public final static int REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_LOCATION = 195;
	public final static int REPORT_TYPE_AUTOMATIC_TRANSACTIONS = 196;
	public final static int REPORT_TYPE_PURCHASEBYLOCATIONDETAILFORLOCATION = 197;
	public final static int REPORT_TYPE_PURCHASEBYCLASSDETAILFORCLASS = 198;
	public final static int REPORT_TYPE_PURCHASEBYLOCATIONDETAIL = 199;
	public final static int REPORT_TYPE_PURCHASEBYCLASSDETAIL = 200;
	public final static int REPORT_TYPE_INVENTORY_DETAILS = 202;
	public final static int REPORT_TYPE_PAY_HEAD_SUMMARY_REPORT = 203;
	public final static int REPORT_TYPE_PAY_HEAD_DETAIL_REPORT = 204;

	public final static int REPORT_TYPE_PAYSLIP_SUMMARY = 205;
	public final static int REPORT_TYPE_PAYSLIP_DETAIL = 206;
	public final static int REPORT_TYPE_PAYSHEET = 207;
	public final static int REPORT_TYPE_INCOMEBY_CUSTOMERDETAIL = 201;

	/**
	 * Generates the Template for Report
	 * 
	 * @param generationType
	 * @param company
	 * @param financeTool
	 * @param input
	 * @return
	 */
	public ITemplate generateTemplate(int generationType, Company company,
			FinanceTool finTool, int reportType, long startDate, long endDate,
			List<ReportInput> input);

	/**
	 * Generates the Template for Report and File
	 * 
	 * Return FileName and AttachementId
	 * 
	 * @param generationType
	 * @param company
	 * @param financeTool
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public List<String> generate(int generationType, Company company,
			FinanceTool finTool, int reportType, long startDate, long endDate,
			List<ReportInput> input) throws Exception;

	/**
	 * Returns the ReportType
	 * 
	 * @return
	 */
	public int getReportType();

}
