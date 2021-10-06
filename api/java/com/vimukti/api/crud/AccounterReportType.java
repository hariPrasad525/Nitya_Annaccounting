package com.vimukti.api.crud;

public enum AccounterReportType {
	SALES_BY_CUSTOMER_SUMMARY("SalesByCustomerSummary"), AGEDCREDITORS_BY_DEBITOR_NAME(
			"AgedCreditorsbyDebitorname"), AGED_CREDITORS("AgedCreditors"), AGEDDEBTORS_BY_DEBITOR(
			"AgeddebtorsBydebitor"), AGEDDEBTORS("Ageddebtors"), AMOUNTS_DUETO_VENDOR(
			"AmountsDuetoVendor"), BALANCE_SHEET_REPORT("BalanceSheetReport"), CASH_FLOW_REPORT(
			"CashFlowReport"), CHECK_DETAILS("CheckDetails"), CREDITORS(
			"Creditors"), CUSTOMER_TRANSACTION_HISTORY(
			"CustomerTransactionHistory"), DEBITORS_LIST("Debitorslist"), DEPOSIT_DETAIL(
			"DepositDetail"), EC_SALES_LIST_DETAILS("EcsaleslistDetails"), EC_SALES_LIST(
			"Ecsaleslist"), EXPENSE_REPORTS("ExpenseReports"), MINIMUM_AND_MAXIMUM_TRANSACTION_DATE(
			"MinimumandMaximumTransactiondate"), MOST_PROFITABLE_CUSTOMERS(
			"MostProfitableCustomers"), PAYEE_STATEMENTS("PayeeStatements"), PRIOR_RETURN_VAT_SUMMARY(
			"PriorreturnVatSummary"), PRIOR_VAT_RETURN_REPORT(
			"PriorvatReturnReport"), PRIOR_VAT_RETURN_VAT_DETAIL(
			"PriorvatReturnVatdetail"), PROFIT_AND_LOSS_REPORT(
			"ProfitandLossreport"), PURCHASE_ORDER("PurchaseOrder"), PURCHASE_REPORT_ITEMS(
			"PurchaseReportItems"), PURCHASES_BY_ITEM_DETAIL_NAME(
			"PurchasesbyItemDetailName"), PURCHASES_BY_ITEM_DETAIL(
			"PurchasesbyItemDetail"), PURCHASES_BY_ITEM_SUMMARY(
			"PurchasesbyItemSummary"), PURCHASES_BY_VENDOR_DETAIL_BY_NAME(
			"PurchasesbyVendordetailbyName"), PURCHASES_BY_VENDOR_DETAIL(
			"PurchasesbyVendordetail"), PURCHASES_BY_VENDOR_SUMMARY(
			"PurchasesbyVendorSummary"), REVERSE_CHARGE_LIST_DETAILS(
			"ReverseChargelistDetails"), REVERSE_CHARGE_LIST(
			"Reversechargelist"), SALES_BY_CUSTOMER_DETAIL_BY_NAME(
			"SalesbycustomerDetailbyname"), SALES_BY_CUSTOMER_DETAIL(
			"SalesbyCustomerdetail"), SALES_BY_ITEM_DETAIL_BY_NAME(
			"SalesbyItemdetailbyName"), SALES_BY_ITEM_DETAIL(
			"SalesbyItemdetail"), SALES_BY_ITEM("Salesbyitem"), SALES_ORDER_LIST(
			"SalesorderList"), SALES_REPORT_ITEMS("SalesReportItems"), SALES_TAX_LIABILITY(
			"SalesTaxLiability"), TRAIL_BALANCE("TrailBalance"), TRANSACTION_DETAIL_BY_ACCOUNT_NAME(
			"TransactiondetailbyAccountname"), TRANSACTION_DETAIL_BY_ACCOUNT(
			"TransactionDetailbyAccount"), TRANSACTION_DETAIL_BY_TAX_ITEM_NAME(
			"TransactionDetailbyTaxitemname"), TRANSACTION_DETAIL_BY_TAX_ITEM(
			"TransactiondetailbyTaxitem"), TRANSACTION_HISTORY_CUSTOMERS(
			"TransactionHistoryCustomers"), TRANSACTION_HISTORY_VENDORS(
			"TransactionHistoryVendors"), UNCATEGORISED_AMOUNTS(
			"UncategorisedAmounts"), VAT100_REPORT("Vat100Report"), VAT_ITEM_DETAILS(
			"VatItemDetails"), VAT_ITEM_SUMMARY("VatitemSummary"), VENDOR_TRANSACTION_HISTORY(
			"VendorTransactionHistory"), BUDGET_OVERVIEW("Budget Overview"), BUDGET_VS_ACTUALS(
			"Budget Vs Actuals"), DEPRECIATION("Depreciation"), INVENTORY_VALUATION_SUMMARY(
			"inventoryValutionSummary"), INVENTORY_VALUATION_DETAILS(
			"inventoryValuationDetails"), INVENTORY_STOCK_STATUS_BY_ITEM(
			"inventoryStockStatusByItem"), INVENTORY_STOCK_STATUS_BY_VENDOR(
			"inventoryStockStatusByVendor"), MISSING_CHECKS("missingchecks"), RECONCILATION_DISCREPANY(
			"reconcilationDiscrepany"), TAX_ITEM_EXCEPTION_DETAIL_REPORT(
			"taxItemExceptionDetailReport"), REPORTS_BY_CLASS("ReportsByClass"), REPORTS_BY_LOCATION(
			"ReportsByLocation"), AUTOMATIC_TRANSACTIONS(
			"AutomaticTransactions");

	private String value;

	AccounterReportType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
