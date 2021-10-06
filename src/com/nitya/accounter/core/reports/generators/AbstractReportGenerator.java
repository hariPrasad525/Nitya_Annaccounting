package com.nitya.accounter.core.reports.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nitya.accounter.core.CSVReportTemplate;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.ITemplate;
import com.nitya.accounter.core.ReportTemplate;
import com.nitya.accounter.core.Utility_R;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.utils.Converter;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.BooleanReportInput;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.MapNumberReportInput;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.ReportInput;
import com.nitya.accounter.web.client.core.StringReportInput;
import com.nitya.accounter.web.client.ui.serverreports.AbstractFinaneReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.server.AccounterReportServiceImpl;
import com.nitya.accounter.web.server.FinanceTool;

public abstract class AbstractReportGenerator implements IReportGenerator {

	private String footerImg = ("war" + File.separator + "images"
			+ File.separator + "footer-print-img.jpg");

	private String style = ("war" + File.separator + "css" + File.separator + "FinancePrint.css");

	protected Company company;
	protected FinanceTool financeTool;
	protected int generationType;

	protected AccounterReportServiceImpl reportsSerivce;
	protected int reportType;
	protected FinanceDate startDate;
	protected FinanceDate endDate;

	private List<ReportInput> input = new ArrayList<ReportInput>();

	protected abstract ReportGridTemplate<?> generate();

	@Override
	public List<String> generate(int generationType, Company company,
			FinanceTool finTool, int reportType, long startDate, long endDate,
			List<ReportInput> input) throws Exception {
		ITemplate template = generateTemplate(generationType, company, finTool,
				reportType, startDate, endDate, input);

		String fileName = template.getFileName();
		fileName = fileName.replaceAll(" ", "");
		File file = null;
		FileOutputStream fos = null;
		try {
			if (generationType == GENERATION_TYPE_CSV) {
				file = File.createTempFile(fileName, ".csv");
				fileName = fileName + ".csv";
				fos = new FileOutputStream(file);
				String templateBody = template.getBody();
				if (templateBody == null) {
					templateBody = "No records to show";
				}
				String header = template.getHeader() + "\r\n" + templateBody;
				fos.write(header.getBytes("UTF-8"));
			} else if (generationType == GENERATION_TYPE_PDF) {
				file = File.createTempFile(fileName, ".pdf");
				fileName = fileName + ".pdf";
				fos = new FileOutputStream(file);
				Converter conventer = new Converter();
				conventer.generatePdfReports(template, fos);
			}
		} finally {
			if (fos != null) {
				fos.close();
			}
		}

		List<String> fileNames = new ArrayList<String>();
		fileNames.add(fileName);
		fileNames.add(file.getName());
		return fileNames;
	}

	@Override
	public ITemplate generateTemplate(int generationType, Company company,
			FinanceTool finTool, int reportType, long startDate, long endDate,
			List<ReportInput> input) {
		this.company = company;
		this.financeTool = finTool;
		this.generationType = generationType;
		this.reportType = reportType;
		this.startDate = new FinanceDate(startDate);
		this.endDate = new FinanceDate(endDate);
		this.input = input;

		reportsSerivce = new AccounterReportServiceImpl() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected FinanceTool getFinanceTool() {
				return financeTool;
			}

		};

		ReportGridTemplate<?> gridTemplate = generate();

		String gridBody = gridTemplate.getBody(Global.get().messages());

		String reportName = getReportNameByType(reportType);

		ITemplate template = null;

		if (generationType == GENERATION_TYPE_CSV) {
			template = new CSVReportTemplate(company, reportType, new String[] {
					gridBody, null, style, "" }, reportName);
		} else if (generationType == GENERATION_TYPE_PDF) {
			template = new ReportTemplate(company, reportType, new String[] {
					gridBody, footerImg, style }, reportName);
		}

		return template;
	}

	public Company getCompany() {
		return company;
	}

	protected String getInputAsString(int index) {
		ReportInput reportInput = getInput(index);
		String value = null;
		if (reportInput != null && reportInput instanceof StringReportInput) {
			value = ((StringReportInput) reportInput).getValue();
			if (value != null && value.isEmpty()) {
				value = null;
			}
		}
		return value;
	}

	protected Long getInputAsLong(int index) {
		ReportInput reportInput = getInput(index);
		if (reportInput != null && reportInput instanceof NumberReportInput) {
			return (Long) ((NumberReportInput) reportInput).getValue();
		}
		return null;
	}

	protected Integer getInputAsInteger(int index) {
		ReportInput reportInput = getInput(index);
		if (reportInput != null && reportInput instanceof NumberReportInput) {
			return (Integer) ((NumberReportInput) reportInput).getValue();
		}
		return null;
	}

	protected Boolean getInputAsBoolean(int index) {
		ReportInput reportInput = getInput(index);
		if (reportInput != null && reportInput instanceof BooleanReportInput) {
			return ((BooleanReportInput) reportInput).getValue();
		}
		return null;
	}

	protected Map<? extends Number, ? extends Number> getInputAsNumberMap(
			int index) {
		ReportInput reportInput = getInput(index);
		if (reportInput != null && reportInput instanceof MapNumberReportInput) {
			return ((MapNumberReportInput) reportInput).getValue();
		}
		return null;
	}

	private ReportInput getInput(int index) {
		if (index < 0 || index >= input.size()) {
			return null;
		}
		return input.get(index);
	}

	protected void updateReport(AbstractFinaneReport<?> abstractFinaneReport) {
		updateReport(abstractFinaneReport, financeTool);
	}

	protected void updateReport(AbstractFinaneReport<?> abstractFinaneReport,
			FinanceTool financeTool) {
		abstractFinaneReport.setStartAndEndDates(
				startDate.toClientFinanceDate(), endDate.toClientFinanceDate());
		abstractFinaneReport.makeReportRequest(startDate.getDate(),
				endDate.getDate());
		abstractFinaneReport.setCurrentFiscalYearStartDate(Utility_R
				.getCurrentFiscalYearStartDate(company));
		abstractFinaneReport.setCurrentFiscalYearEndDate(Utility_R
				.getCurrentFiscalYearEndDate(company));
	}

	protected String getDateInDefaultType(ClientFinanceDate date) {
		if (date == null) {
			return "";
		}
		if (company == null) {
			date.toString();
		}

		SimpleDateFormat dateFormatter = new SimpleDateFormat(company
				.getPreferences().getDateFormat());
		String format = dateFormatter.format(date.getDateAsObject());
		return format;
	}

	public String getReportNameByType(int reportType) {
		switch (reportType) {
		case REPORT_TYPE_PROFITANDLOSS:
			return "Profit And Loss Report";
		case REPORT_TYPE_DEPRECIATIONSHEDULE:
			return "Depreciation Shedule Report";
		case REPORT_TYPE_RECONCILATIONS:
			return "ReconcilationDetails By Account Report";
		case REPORT_TYPE_BALANCESHEET:
			return "Balance Sheet Report";
		case REPORT_TYPE_RECONCILATION_ACCOUNTSLIST:
			return "Reconcilation Reports";
		case REPORT_TYPE_TRIALBALANCE:
			return "Trial Balance Report";
		case REPORT_TYPE_TRANSACTIONDETAILBYTAXITEM:
			return "Transaction Detail By Tax Item Report";
		case REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT:
			return "Transaction Detail By Account Report";
		case REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_LOCATION:
			return "Transaction Detail By Account Report";
		case REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_JOB:
			return "Transaction Detail By Account Report";
		case REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_CLASS:
			return "Transaction Detail By Account Report";
		case REPORT_TYPE_EXPENSE:
			return "Expense Report";
		case REPORT_TYPE_AR_AGEINGSUMMARY:
			return "AR Ageing Summary Report";
		case REPORT_TYPE_AR_AGEINGDETAIL:
			return "AR Ageing Detail Report";
		case REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY:
			return "Customer Transaction History Report";
		case REPORT_TYPE_SALESBYCUSTOMERSUMMARY:
			return "Sales By Customer Summary Report";
		case REPORT_TYPE_SALESBYCUSTOMERDETAIL:
			return "Sales By Customer Detail Report";
		case REPORT_TYPE_SALESBYITEMSUMMARY:
			return "Sales By Item Summary Report";
		case REPORT_TYPE_SALESBYITEMDETAIL:
			return "Sales By Item Detail Report";
		case REPORT_TYPE_SALESORDER_OPEN:
			Integer salesStatus = getInputAsInteger(0);
			if (salesStatus == ClientTransaction.STATUS_OPEN) {
				return "Sales Open Order Report";
			} else if (salesStatus == ClientTransaction.STATUS_COMPLETED) {
				return "Sales Completed Order Report";
			} else if (salesStatus == ClientTransaction.STATUS_CANCELLED) {
				return "Sales Cancelled Order Report";
			} else {
				return "Sales Order Report";
			}
			/*
			 * case REPORT_TYPE_SALESORDER_CLOSE: return
			 * "Sales Closed Order Report";
			 */
		case REPORT_TYPE_AP_AGEINGSUMMARY:
			return "AP Ageing Summary Report";
		case REPORT_TYPE_PURCHASEBYLOCATIONDETAIL:
			return "Purchase By Location Details Report";
		case REPORT_TYPE_PURCHASEBYCLASSDETAIL:
			return "Purchase By Class Details Report";
		case REPORT_TYPE_PURCHASEBYCLASSDETAILFORCLASS:
			return "Purchase By Class Summary Report";
		case REPORT_TYPE_PURCHASEBYLOCATIONDETAILFORLOCATION:
			return "Purchase By Location Summary Report";
		case REPORT_TYPE_AP_AGEINGDETAIL:
			return "AP Ageing Detail Report";
		case REPORT_TYPE_VENDORTRANSACTIONHISTORY:
			// return "Supplier Transaction History Report";
			return Global.get().messages()
					.payeeTransactionHistory(Global.get().Vendor());
		case REPORT_TYPE_PURCHASEBYVENDORSUMMARY:
			return "Purchase By Supplier Summary Report";
		case REPORT_TYPE_PURCHASEBYVENDORDETAIL:
			return "Purchase By Supplier Detail Report";
		case REPORT_TYPE_PURCHASEBYITEMSUMMARY:
			return "Purchase By Item Summary Report";
		case REPORT_TYPE_PURCHASEBYITEMDETAIL:
			return "Purchase By Item Detail Report";
		case REPORT_TYPE_PURCHASEORDER_OPEN:
			int purchaseStatus = this.getInputAsInteger(0);
			if (purchaseStatus == ClientTransaction.STATUS_OPEN) {
				return "Purchase Open Order Report";
			} else if (purchaseStatus == ClientTransaction.STATUS_COMPLETED) {
				return "Purchase Completed Order Report";
			} else if (purchaseStatus == ClientTransaction.STATUS_CANCELLED) {
				return "Purchase Cancelled Order Report";
			} else {
				return "Purchase Order Report";
			}

			/*
			 * case REPORT_TYPE_PURCHASEORDER_CLOSE: return
			 * "Purchase Closed Order Report";
			 */
		case REPORT_TYPE_PRIORVATRETURNS:
			return "Prior VAT Return Report";
		case REPORT_TYPE_VAT100:
			return "VAT 100 Report";
		case REPORT_TYPE_VATDETAIL:
			return "VAT Detail Report";
		case REPORT_TYPE_VATITEMDETAIL:
			return "VAT Item Detail Report";
		case REPORT_TYPE_UNCATEGORISEDVATAMOUNT:
			return "Uncategorised VAT Amount Report";
		case REPORT_TYPE_VATITEMSUMMARY:
			return "VAT Item Summary Report";
		case REPORT_TYPE_EC_SALES_LIST:
			return "EC Sales List Report";
		case REPORT_TYPE_ECSCALESLISTDETAIL:
			return "EC Sales List Detail Report";
		case REPORT_TYPE_SALESTAXLIABILITY:
			return "Sales Tax Liability Report";
		case REPORT_TYPE_REVERSECHARGELIST:
			return "Reverse Charge List Report";
		case REPORT_TYPE_REVERSECHARGELISTDETAIL:
			return "Reverse Charge List Detail Report";
		case REPORT_TYPE_MOSTPROFITABLECUSTOMER:
			return "Most Profitable Customer Report";
		case REPORT_TYPE_CASHFLOWSTATEMENT:
			return "Cash Flow Statement Report";
		case REPORT_TYPE_AMOUNTSDUETOVENDOR:
			return "Amount Due To Vendor Report";
		case REPORT_TYPE_CUSTOMERSTATEMENT:
			return "Customer Statement";
		case REPORT_TYPE_SALESBYLOCATIONDETAIL:
			return "Sales By Location Details Report";
		case REPORT_TYPE_SALESBYLOCATIONDETAILFORLOCATION:
			return "Sales By Location Summary Report";
		case REPORT_TYPE_PROFITANDLOSSBYLOCATION:
			return "Profit and Loss by Location";
		case REPORT_TYPE_BUDGET:
			return "Budget Report";
		case REPORT_TYPE_BUDGET_VS_ACTUALS:
			return "Budget vs Actuals";
		case REPORT_TYPE_1099TRANSACTIONDETAIL:
			return "1099 Transaction Detail By Vendor";
		case REPORT_TYPE_SALESBYCLASSDETAIL:
			return "Sales By Class Details Report";
		case REPORT_TYPE_SALESBYCLASSDETAILFORCLASS:
			return "Sales By Class Summary Report";
		case REPORT_TYPE_PROFITANDLOSSBYCLASS:
			return "Profit and Loss by Class";
		case REPORT_TYPE_GENERAL_LEDGER_REPORT:
			return "General Ledger Report";
		case REPORT_TYPE_TAX_ITEM_DETAIL:
			return "TAXItemDetail Report";
		case REPORT_TYPE_VAT_EXCEPTION_DETAIL:
			return "VAT Exception Detail Report";
		case REPORT_TYPE_VENDORSTATEMENT:
			return "Vendor Statement";
		case REPORT_TYPE_TAX_EXCEPTION_DETAIL:
			return "TAX Item Exception Report";
		case REALISED_EXCHANGE_LOSSES_AND_GAINS:
			return "Realised Exchange Losses & Gains Report";
		case UNREALISED_EXCHANGE_LOSSES_AND_GAINS:
			return "Un-Realised Exchange Losses & Gains Report";
		case REPORT_TYPE_INVENTORY_VALUTION_SUMMARY:
			return "Inventory Valution Summary";
		case REPORT_TYPE_INVENTORY_VALUTION_DETAIL:
			return "Inventory Valution Detail";
		case REPORT_TYPE_INVENTORY_STOCK_STATUS_BYITEM:
			return "Inventory Stock Status By Item";
		case REPORT_TYPE_INVENTORY_STOCK_STATUS_BYVENDOR:
			return "Inventory Stock Status By Vendor";
		case REPORT_TYPE_BANK_DEPOSIT_REPORT:
			return "Deposit Detail";
		case REPORT_TYPE_BANK_CHECK_DETAIL_REPORT:
			return "Check Detail";
		case REPORT_TYPE_MISSION_CHECKS:
			return "Missing Checks";
		case REPORT_TYPE_RECONCILIATION_DISCREPANCY:
			return "Reconciliation Discrepancy";
		case REPORT_TYPE_ESTIMATE_BY_JOB:
			return "Estimates By Job";
		case REPORT_TYPE_JOB_ACTUAL_COST_DETAIL:
			return "Job Actual Cost Detail Report";
		case REPORT_TYPE_JOB_PROFITABILTY_SUMMARY:
			return "Job Profitability Summary Report";
		case REPORT_TYPE_UNBILLED_COSTS_BY_JOB:
			return "Unbilled Costs By Job";
		case REPORT_TYPE_ITEM_ACTUAL_COST_DETAIL:
			return "Item Actual Cost Detail";
		case REPORT_TYPE_JOB_PROFITABILITY_BY_JOBID:
			return "Job Profitability Detail";
		case REPORT_TYPE_PROFITANDLOSSBYJOB:
			return "Profit and Loss by Job";
		case REPORT_TYPE_TDS_ACKNOWLEDGEMENT_REPORT:
			return "TDS Acknowledgement Report";
		case REPORT_TYPE_AUTOMATIC_TRANSACTIONS:
			return "Automatic Transactions";
		case REPORT_TYPE_INVENTORY_DETAILS:
			return "Inventory Details Report";
		case REPORT_TYPE_PAY_HEAD_SUMMARY_REPORT:
			return "Pay Head Summart Report";
		case REPORT_TYPE_PAY_HEAD_DETAIL_REPORT:
			return "Pay Head Detail Report";
		case REPORT_TYPE_PAYSLIP_SUMMARY:
			return "Payslip Summary";
		case REPORT_TYPE_PAYSLIP_DETAIL:
			return "Payslip Detail";
		case REPORT_TYPE_PAYSHEET:
			return "Paysheet";
		case REPORT_TYPE_INCOMEBY_CUSTOMERDETAIL:
			return Global.get().messages2()
					.incomeByCustomerDetail(Global.get().Customer());
		default:
			break;
		}
		return "";
	}

	private String getVendorName() {
		Set<Vendor> vendors = company.getVendors();
		long vendorID = getInputAsLong(0);
		for (Vendor vendor : vendors) {
			if (vendor.getID() == vendorID) {
				String name = vendor.getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				return name;
			}
		}
		return Global.get().Vendor() + " Statement";
	}

}
