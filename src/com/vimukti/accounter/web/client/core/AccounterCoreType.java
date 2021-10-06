/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Fernandez
 * 
 */
public enum AccounterCoreType implements IsSerializable {

	ACCOUNT("ClientAccount", "Account"),

	BANK_ACCOUNT("ClientBankAccount", "BankAccount"),

	VENDOR("ClientVendor", "Vendor"),

	TAXAGENCY("ClientTAXAgency", "TAXAgency"),

	CUSTOMER("ClientCustomer", "Customer"),

	ITEM("ClientItem", "Item"),

	TAX_GROUP("ClientTAXGroup", "TAXGroup"),

	TAX_ITEM_GROUP("ClientTAXItemGroup", "TAXItemGroup"),

	BOX_1099("ClientBox1099", "Box1099"),

	TAX_CODE("ClientTAXCode", "TAXCode"),

	TAXITEM("ClientTAXItem", "TAXItem"),

	ITEM_TAX("ClientItemTax", "ItemTax"),

	CUSTOMER_GROUP("ClientCustomerGroup", "CustomerGroup"),

	VENDOR_GROUP("ClientVendorGroup", "VendorGroup"),

	PAYMENT_TERM("ClientPaymentTerms", "PaymentTerms"),

	SHIPPING_METHOD("ClientShippingMethod", "ShippingMethod"),

	SHIPPING_TERM("ClientShippingTerms", "ShippingTerms"),

	PRICE_LEVEL("ClientPriceLevel", "PriceLevel"),

	ITEM_GROUP("ClientItemGroup", "ItemGroup"),

	SALES_PERSON("ClientSalesPerson", "SalesPerson"),

	PAYEE("ClientPayee", "Payee"),

	CREDIT_RATING("ClientCreditRating", "CreditRating"),

	PAY_TAX("ClientPayTAX", "PayTAX"),

	PAYMENT_METHOD, COMPANY("ClientCompany", "Company"),

	TRANSACTION("ClientTransaction", "Transaction"),

	ADDRESS("ClientAddress", "Address"),

	BANK("ClientBank", "Bank"),

	COMPANY_PREFERENCES("ClientCompanyPreferences", "CompanyPreferences"),

	CONTACT("ClientContact", "Contact"),

	BUDGETITEM("ClientBudgetItem", "BudgetItem"),

	CREDITS_AND_PAYMENTS("ClientCreditsAndPayments", "CreditsAndPayments"),

	CURRENCY("ClientCurrency", "Currency"),

	EMAIL("ClientEmail", "Email"),

	EMAIL_ACCOUNT("ClientEmailAccount", "EmailAccount"),

	ENTRY("ClientEntry", "Entry"),

	FAX("ClientFax", "Fax"),

	FILE_TAX_ENTRY("ClientFileTAXEntry", "FileTAXEntry"),

	PHONE("ClientPhone", "Phone"),

	UNIT_OF_MEASURE("ClientUnitOfMeasure", "UnitOfMeasure"),

	USER_PREFERENCES("ClientUserPreferences", "UserPreferences"),

	JOURNALENTRY("ClientJournalEntry", "JournalEntry"),

	ESTIMATE("ClientEstimate", "Estimate"),

	TRANSACTION_MAKEDEPOSIT("ClientTransactionMakeDeposit",
			"TransactionMakeDeposit"),

	INVOICE("ClientInvoice", "Invoice"),

	CASHPURCHASE("ClientCashPurchase", "CashPurchase"),

	CASHSALES("ClientCashSales", "CashSales"),

	CUSTOMERCREDITMEMO("ClientCustomerCreditMemo", "CustomerCreditMemo"),

	CUSTOMERPREPAYMENT("ClientCustomerPrePayment", "CustomerPrePayment"),

	ENTERBILL("ClientEnterBill", "EnterBill"),

	ISSUEPAYMENT("ClientIssuePayment", "IssuePayment"),

	PAYBILL("ClientPayBill", "PayBill"),

	VENDORPAYMENT("ClientVendorPrePayment", "VendorPrePayment"),

	PAYSALESTAX("ClientPaySalesTax", "PaySalesTax"),

	PURCHASEORDER("ClientPurchaseOrder", "PurchaseOrder"),

	RECEIVEPAYMENT("ClientReceivePayment", "ReceivePayment"),

	SALESORDER("ClientSalesOrder", "SalesOrder"),

	TRANSFERFUND("ClientTransferFund", "TransferFund"),

	VENDORCREDITMEMO("ClientVendorCreditMemo", "VendorCreditMemo"),

	WRITECHECK("ClientWriteCheck", "WriteCheck"),

	MAKEDEPOSIT("ClientMakeDeposit", "MakeDeposit"),

	CREDITCARDCHARGE("ClientCreditCardCharge", "CreditCardCharge"),

	USER("ClientUser", "User"),

	FISCALYEAR("ClientFiscalYear", "FiscalYear"),

	COMMODITYCODE("ClientCommodityCode", "CommodityCode"),

	TRANSACTIONMAKEDEPOSITENTRIES("ClientTransactionMakeDepositEntries",
			"TransactionMakeDepositEntries"),

	ACCOUNTTRANSACTION("ClientAccountTransaction", "AccountTransaction"),

	VATRETURN("ClientVATReturn", "VATReturn"),

	BUDGET("ClientBudget", "Budget"),

	ITEMRECEIPT("ClientItemReceipt", "ItemReceipt"),

	EXPENSE("ClientExpense", "Expense"),

	PAYEXPENSE("ClientPayExpense", "PayExpense"),

	FIXEDASSETNOTE("ClientFixedAssetNote", "FixedAssetNote"),

	FIXEDASSETHISTORY("ClientFixedAssetHistory", "FixedAssetHistory"),

	CUSTOMERREFUND("ClientCustomerRefund", "CustomerRefund"),

	FIXEDASSET("ClientFixedAsset", "FixedAsset"),

	VATBOX("ClientBox", "Box"),

	PAYVAT("ClientPayVAT", "PayVAT"),

	ITEMBACKUP("ClientItemBackUp", "ItemBackUp"),

	TRANSACTION_CREDITS_AND_PAYMENTS("ClientTransactionCreditsAndPayments",
			"TransactionCreditsAndPayments"),

	TRANSACTION_RECEIVEPAYMENT("ClientTransactionReceivePayment",
			"TransactionReceivePayment"),

	TRANSACTION_PAYBILL("ClientTransactionPayBill", "TransactionPayBill"),

	VATRETURNBOX("ClientVATReturnBox", "VATReturnBox"),

	DEPRECIATION("ClientDepreciation", "Depreciation"),

	DELETED, ERROR,

	TAXRATECALCULATION("ClientTAXRateCalculation", "TAXRateCalculation"),

	FINANCELOG("ClientFinanceLogger", "FinanceLogger"),

	RECEIVEVAT("ClientReceiveVAT", "ReceiveVAT"),

	TAXADJUSTMENT("ClientTAXAdjustment", "TAXAdjustment"),

	BRANDINGTHEME("ClientBrandingTheme", "BrandingTheme"),

	EMPLOYEE("ClientEmployee", "Employee"),

	WAREHOUSE("ClientWarehouse", "Warehouse"),

	MEASUREMENT("ClientMeasurement", "Measurement"),

	MESSAGE_OR_TASK("ClientMessageOrTask", "MessageOrTask"),

	STOCK_TRANSFER("ClientStockTransfer", "StockTransfer"),

	RECURRING_TRANSACTION("ClientRecurringTransaction", "RecurringTransaction"),

	REMINDER("ClientReminder", "Reminder"),

	LOCATION("ClientLocation", "Location"),

	STOCK_ADJUSTMENT("ClientStockAdjustment", "StockAdjustment"),

	ACTIVITY("ClientActivity", "Activity"),

	ADMIN_USER("ClientAdminUser", "AdminUser"),

	ACCOUNTER_CLASS("ClientAccounterClass", "AccounterClass"),

	UNIT("ClientUnit", "Unit"),

	RECONCILIATION("ClientReconciliation", "Reconciliation"),

	TRANSACTION_LOG("ClientTransactionLog", "TransactionLog"),

	TAX_RETURN("ClientTAXReturn", "TAXReturn"),

	LANGUAGE("ClientLanguage", "Language"),

	CUSTOMFIELD("ClientCustomField", "CustomField"), CUSTOMFIELDVALUE(
			"ClientCustomFieldValue", "CustomFieldValue"),

	PORTLET_CONFIG("ClientPortletConfiguration", "PortletConfiguration"),

	PORTLET_PAGE_CONFIG("ClientPortletPageConfiguration",
			"PortletPageConfiguration"),

	CHEQUE_LAYOUT("ClientChequeLayout", "ChequeLayout"),

	ATTACHMENT("ClientAttachment", "Attachment"),

	TDSCHALANDETAIL("ClientTDSChalanDetail", "TDSChalanDetail"),

	TDSCHALANTRANSACTIONITEM("ClientTDSTransactionItem", "TDSTransactionItem"),

	TDSRESPONSIBLEPERSON("ClientTDSResponsiblePerson", "TDSResponsiblePerson"),

	TDSDEDUCTORMASTER("ClientTDSDeductorMasters", "TDSDeductorMasters"),

	STATEMENT("ClientStatement", "Statement"),

	STATEMENTRECORD("ClientStatementRecord", "StatementRecord"),

	ITEM_STATUS("ClientItemStatus", "ItemStatus"),

	TRANSACTION_DEPOSIT_ITEM("ClientTransactionDepositItem",
			"TransactionDepositItem"),

	BUILD_ASSEMBLY("ClientBuildAssembly", "BuildAssembly"),

	JOB("ClientJob", "Job"),

	EMAIL_TEMPLATE("ClientEmailTemplate", "EmailTemplate"), EMPLOYEE_CATEGORY(
			"ClientEmployeeCategory", "EmployeeCategory"),

	EMPLOYEE_GROUP("ClientEmployeeGroup", "EmployeeGroup"),

	PAY_HEAD("ClientPayHead", "PayHead"),

	PAYROLL_UNIT("ClientPayrollUnit", "PayrollUnit"),

	ATTENDANCE_PRODUCTION_TYPE("ClientAttendanceOrProductionType",
			"AttendanceOrProductionType"),

	COMPUTATION_FORMULA_FUNCTION("ClientComputaionFormulaFunction",
			"ComputaionFormulaFunction"),

	ATTENDANCE_PAY_HEAD("ClientAttendancePayHead", "AttendancePayHead"),

	COMPUTATION_PAY_HEAD("ClientComputionPayHead", "ComputionPayHead"),

	FLATRATE_PAY_HEAD("ClientFlatRatePayHead", "FlatRatePayHead"),

	PRODUCTION_PAY_HEAD("ClientProductionPayHead", "ProductionPayHead"),

	USERDEFINED_PAY_HEAD("ClientUserDefinedPayHead", "UserDefinedPayHead"),

	COMPUTATION_SLAB("ClientComputationSlab", "ComputationSlab"),

	PAY_STRUCTURE("ClientPayStructure", "PayStructure"),

	PAY_RUN("ClientPayRun", "PayRun"),

	ATTENDANCE_MANAGEMENT("ClientAttendanceManagement", "AttendanceManagement"),
	
	PAYPALTRANSACTION("ClientPaypalTransation", "PaypalTransation"),

	EMPLOYEE_PAYHEAD_COMPONENT("ClientEmployeePayHeadComponent",
			"EmployeePayHeadComponent"),

	EMPLOYEE_PAYMENT_DETAILS("ClientEmployeePaymentDetails",
			"EmployeePaymentDetails"),

	PAY_ROLL_UNIT("ClientPayrollUnit", "PayrollUnit"),

	ATTENDANCE_PRODUCTION_ITEM("ClientAttendanceOrProductionItem",
			"AttendanceOrProductionItem"),

	TRANSACTION_PAYEMPLOYEE("ClientTransactionPayRunComponent",
			"TransactionPayRunComponent"),

	TYPE_PAYEMPLOYEE("ClientPayEmployee", "PayEmployee"),
	
	USER_DEFINED_PAYHEAD_ITEM("ClientUserDefinedPayheadItem",
			"ClientUserDefinedPayheadItem"),
	
	EMPLOYEE_TAX("ClientEmployeeTax", "ClientEmployeeTax"),
	
	EMPLOYEE_COMPENSATION("ClientEmployeeCompensation", "ClientEmployeeCompensation"),
	
	LIVE_TAX_RATE("ClientTaxRate", "ClientTaxRate"),
	
	TAX_COMPUTATION_PAY_HEAD("ClientTaxComputationPayHead", "ClientTaxComputationPayHead"),

	INVOICE_FREQUENCY("ClientInvoiceFrequencyGroup", "InvoiceFrequencyGroup"),

	LIVE_TAX_RATE_RANGE("ClientTaxRateRange", "ClientTaxRateRange"), TYPE_PAYTAX;

	private String clientName;
	private String serverName;

	AccounterCoreType() {

	}

	AccounterCoreType(String clientSimpleName, String serverSimpleName) {

		this.clientName = clientSimpleName;
		this.serverName = serverSimpleName;
	}

	public static AccounterCoreType getObject(String value) {
		String upperCaseString = value.toUpperCase();
		AccounterCoreType type = AccounterCoreType.valueOf(upperCaseString);

		if (type == null) {
			if (upperCaseString.equals("TAXGROUP")) {
				return TAX_GROUP;
			} else if (upperCaseString.equals("TAXITEMGROUP")) {
				return TAX_ITEM_GROUP;
			} else if (upperCaseString.equals("BOX1099")) {
				return BOX_1099;
			} else if (upperCaseString.equals("TAXCODE")) {
				return TAX_CODE;
			} else if (upperCaseString.equals("ITEMTAX")) {
				return ITEM_TAX;
			} else if (upperCaseString.equals("CUSTOMERGROUP")) {
				return CUSTOMER_GROUP;
			} else if (upperCaseString.equals("VENDORGROUP")) {
				return VENDOR_GROUP;
			} else if (upperCaseString.equals("PAYMENTTERM")) {
				return PAYMENT_TERM;
			} else if (upperCaseString.equals("SHIPPINGMETHOD")) {
				return SHIPPING_METHOD;
			} else if (upperCaseString.equals("SHIPPINGTERM")) {
				return SHIPPING_TERM;
			} else if (upperCaseString.equals("PRICELEVEL")) {
				return PRICE_LEVEL;
			} else if (upperCaseString.equals("ITEMGROUP")) {
				return ITEM_GROUP;
			} else if (upperCaseString.equals("SALESPERSON")) {
				return SALES_PERSON;
			} else if (upperCaseString.equals("CREDITRATING")) {
				return CREDIT_RATING;
			} else if (upperCaseString.equals("PAYSALESTAX")) {
				return PAY_TAX;
			} else if (upperCaseString.equals("PAYMENTMETHOD")) {
				return PAYMENT_METHOD;
			} else if (upperCaseString.equals("COMPANYPREFERENCES")) {
				return COMPANY_PREFERENCES;
			} else if (upperCaseString.equals("CREDITSANDPAYMENTS")) {
				return CREDITS_AND_PAYMENTS;
			} else if (upperCaseString.equals("PAYSALESTAXENTRIES")) {
				return FILE_TAX_ENTRY;
			} else if (upperCaseString.equals("UNITOFMEASURE")) {
				return UNIT_OF_MEASURE;
			} else if (upperCaseString.equals("USERPREFERENCES")) {
				return USER_PREFERENCES;
			} else if (upperCaseString.equals("TRANSACTIONMAKEDEPOSIT")) {
				return TRANSACTION_MAKEDEPOSIT;
			} else if (upperCaseString.equals("TRANSACTIONCREDITSANDPAYMENTS")) {
				return TRANSACTION_CREDITS_AND_PAYMENTS;
			} else if (upperCaseString.equals("TRANSACTIONRECEIVEPAYMENT")) {
				return TRANSACTION_RECEIVEPAYMENT;
			} else if (upperCaseString.equals("TRANSACTIONPAYBILL")) {
				return TRANSACTION_PAYBILL;
			} else if (upperCaseString.equals("CHEQUELAYOUT")) {
				return CHEQUE_LAYOUT;
			}else if (upperCaseString.equals("ITEM")) {
				return ITEM;
			}else if (upperCaseString.equals("INVOICEFREQUENCYGROUP")) {
				return INVOICE_FREQUENCY;
			}
		}

		return type;
	}

	public String getClientClassSimpleName() {

		return this.clientName;
	}

	public String getServerClassSimpleName() {

		return this.serverName;
	}

	public String getServerClassFullyQualifiedName() {

		return "com.vimukti.accounter.core." + this.serverName;
	}

}