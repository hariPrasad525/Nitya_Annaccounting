package com.vimukti.accounter.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AccounterServerConstants implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SYMBOL_PLUS = "+";
	public static final String SYMBOL_MINUS = "-";

	public static final String MEMO_OPENING_BALANCE = "Opening Balance";
	// Constants for Account Names

	public static final String ACCOUNTS_RECEIVABLE = "Debtors";
	public static final String ACCOUNTS_PAYABLE = "Creditors";
	public static final String OPENING_BALANCE = "Opening Balances";
	public static final String UN_DEPOSITED_FUNDS = "Un Deposited Funds";
	public static final String PENDING_ITEM_RECEIPTS = "Pending Item Receipts";
	public static final String SALES_TAX_PAYABLE = "Sales Tax Payable";
	public static final String EMPLOYEE_PAYROLL_LIABILITIES = "Employee Payroll Liabilities";
	public static final String RETAINED_EARNINGS = "Retained Earnings";
	public static final String CASH_DISCOUNT_GIVEN = "Discounts Given";
	public static final String WRITE_OFF = "Write off";
	public static final String CASH_DISCOUNT_TAKEN = "Cash Discount taken";
	public static final String COST_OF_GOODS_SOLD = "Cost of Goods Sold";
	public static final String BANK_CHARGE = "Bank Charge";
	public static final String OTHER_CASH_INCOME = "Other Cash Income";
	public static final String OTHER_CASH_EXPENSE = "Other Cash Expense";
	public static final String DEPOSITS = "Deposits";
	// in UK
	public static final String SALES_LEDGER = "Sales Ledger";
	public static final String PURCHASE_LEDGER = "Purchase Ledger";
	public static final String PENDING_GOODS_RECEIVED_NOTES = "Pending Goods Received Notes";
	public static final String VAT_LIABILITY_ACCOUNT = "VAT Liability Account";
	public static final String VAT_LIABILITY_ACCOUNT_IR = "VAT Liability Account (IR)";
	public static final String EARLY_PAYMENT_DISCOUNT_GIVEN = "Early Payment Discount Given";
	public static final String EARLY_PAYMENT_DISCOUNT_TAKEN = "Early Payment Discount Taken";

	public static final String PAYROLL_LIABILITY_TAX = "Payroll Liability - Tax";
	public static final String PAYROLL_LIABILITY_OTHER = "Payroll Liability - Other";
	public static final String PAYROLL_LIABILITIES = "Payroll Liabilities";
	public static final String PAYROLL_LIABILITY_NET_PAY = "Payroll Liability - Net Pay";
	public static final String PAYROLL_EXPENSE_EMPLOYEES = "Payroll Expense - Employees";
	public static final String PAYROLL_EXPENSE_EMPLOYEE_SALARY = "Payroll Expense - Employee Salary";
	public static final String PAYROLL_EXPENSE_BONUSES = "Payroll Expense - Bonuses";
	public static final String PAYROLL_EXPENSE_SSP_SMP_SPP_SAP = "Payroll Expense - SSP,SMP,SPP,SAP";
	public static final String PAYROLL_CHARGE_EXPENSE = "Payroll Charge Expense";
	public static final String PAYROLL_CHARGE_EXP_NI_EMPLOYER = "Payroll Charge Exp - NI Employer";
	public static final String PAYROLL_CHARGE_EXP_PENSION_EMPLOYER = "Payroll Charge Exp - Pension Employer";
	public static final String EC_SALE = "EC Sale";

	// Constants for Account types
	// in US
	public static final String TYPE_INCOME = "Income";
	public static final String TYPE_OTHER_INCOME = "Other Income";
	public static final String TYPE_EXPENSE = "Expense";
	public static final String TYPE_OTHER_EXPENSE = "Other Expense";
	public static final String TYPE_COST_OF_GOODS_SOLD = "Cost of Goods Sold";
	public static final String TYPE_CASH = "Cash";
	public static final String TYPE_BANK = "Bank";
	public static final String TYPE_OTHER_CURRENT_ASSET = "Other Current Asset";
	public static final String TYPE_INVENTORY_ASSET = "Inventory Asset";
	public static final String TYPE_OTHER_ASSET = "Other Asset";
	public static final String TYPE_FIXED_ASSET = "Fixed Asset";
	public static final String TYPE_CREDIT_CARD = "Credit Card";
	public static final String TYPE_PAYROLL_LIABILITY = "Payroll Liability";
	public static final String TYPE_OTHER_CURRENT_LIABILITY = "Other Current Liability";
	public static final String TYPE_LONG_TERM_LIABILITY = "Long term Liability";
	public static final String TYPE_EQUITY = "Equity";

	public static final String TYPE_ACCOUNT_RECEIVABLE = "Accounts Receivable";
	public static final String TYPE_ACCOUNT_PAYABLE = "Accounts Payable";

	// in UK

	public static final String TYPE_REVENUE = "Revenue";
	public static final String TYPE_INDIRECT_COSTS = "Indirect Costs";
	public static final String TYPE_OTHER_DIRECT_COSTS = "Other Direct Costs";
	public static final String TYPE_DIRECT_PRODUCTS_AND_MATERIAL_COSTS = "Direct Products and Material Goods";
	public static final String TYPE_CURRENT_ASSET = "Current Asset";
	public static final String TYPE_CURRENT_LIABILITY = "Current Liability";
	public static final String TYPE_CAPITAL_AND_RESERVES = "Capital & Reserves";

	public static final String TYPE_SALES_LEDGER = "Sales Ledger";
	public static final String TYPE_PURCHASE_LEDGER = "Purchase Ledger";
	public static final String TYPE_STOCK_ASSET = "Stock Asset";

	// Constants for Bank Account Types
	public static final String BANK_ACCCOUNT_TYPE_NONE = "None";
	public static final String BANK_ACCCOUNT_TYPE_CHECKING = "Checking";
	public static final String BANK_ACCCOUNT_TYPE_SAVING = "Saving";
	public static final String BANK_ACCCOUNT_TYPE_MONEY_MARKET = "Money Market";

	// Cash Flow Categories.
	public static final String CASH_FLOW_CATEGORY_FINANCING = "Financing";
	public static final String CASH_FLOW_CATEGORY_INVESTING = "Investing";
	public static final String CASH_FLOW_CATEGORY_OPERATING = "Operating";

	// Payment Method names.
	public static final String PAYMENT_METHOD_CASH = "Cash";
	public static final String PAYMENT_METHOD_CHECK_FOR_UK = "Cheque";
	public static final String PAYMENT_METHOD_CHECK = "Check";
	public static final String PAYMENT_METHOD_CREDITCARD = "Credit Card";
	public static final String PAYMENT_METHOD_AMERICAN_EXPRESS = "American Express";
	public static final String PAYMENT_METHOD_DIRECT_DEBIT = "Direct Debit";
	public static final String PAYMENT_METHOD_MASTER_CARD = "MasterCard";
	public static final String PAYMENT_METHOD_ONLINE_BANKING = "Online Banking";
	public static final String PAYMENT_METHOD_STANDING_ORDER = "Standing Order";
	public static final String PAYMENT_METHOD_SWITCH_MAESTRO = "Switch/Maestro";
	public static final String PAYMENT_METHOD_VISA = "Visa";

	// Default Payment Terms
	// US
	public static final String PM_ONE_PERCENT_TEN_NET_THIRTY = "1 % 10 Net 30";
	public static final String PM_TWO_PERCENT_TEN_NET_THIRTY = "2 % 10 Net 30";
	public static final String PM_DUE_ON_RECEIPT = "Due on Receipt";
	public static final String PM_NET_FIFTEEN = "Net 15";
	public static final String PM_NET_THIRTY = "Net 30";
	public static final String PM_NET_SIXTY = "Net 60";
	public static final String PM_MONTHLY = "Net Monthly";
	public static final String PM_QUARTERLY = "Quarterly";
	public static final String PM_ANNUALLY = "Annually";

	// UK
	public static final String PM_MONTHLY_PAYROLL_LIABILITY = "Monthly-Payroll Liability";
	public static final String PM_QUARTERLY_PAYROLL_LIABILITY = "Quarterly-Payroll Liability";

	// US
	public static final String DISCOUNT_ONEPERCENT_IF_PAID_WITHIN_TENDAYS = "Discount 1 % if paid with in 10 days";
	public static final String DISCOUNT_TWOPERCENT_IF_PAID_WITHIN_TENDAYS = "Discount 2 % if paid with in 10 days";
	public static final String DUE_ON_RECEIPT = "Due on Receipt";
	public static final String PAY_WITH_IN_FIFTEEN_DAYS = "Pay with in 15 days";
	public static final String PAY_WITH_IN_THIRTY_DAYS = "Pay with in 30 days";
	public static final String PAY_WITH_IN_SIXTY_DAYS = "Pay with in 60 days";
	public static final String SALES_TAX_PAID_MONTHLY = "Sales Tax paid Monthly";
	public static final String SALES_TAX_PAID_QUARTERLY = "Sales Tax paid Quarterly";
	public static final String SALES_TAX_PAID_ANNUALLY = "Sales Tax paid Annually";

	// UK
	public static final String PAYROLL_LIABILITY_PAID_MONTHLY = "Payroll Liabilities Paid monthly(+18 days)";
	public static final String PAYROLL_LIABILITY_PAID_QUARTERLY = "Payroll Liabilities Paid quarterly(+18 days)";

	// UK default VAT Agency
	public static final String DEFAULT_VAT_AGENCY_NAME = "HM Revenue & Customs VAT";

	// Constants for Credits and Payments

	public static final String CUSTOMER_CREDIT_MEMO = "Customer Credit";
	public static final String CUSTOMER_PAYMENT = "Customer Payment";

	// constants for Transaction Names

	public static final String TYPE_CASH_SALES = "Cash Sale";
	public static final String TYPE_CASH_PURCHASE = "Cash Purchase";
	public static final String TYPE_CREDIT_CARD_CHARGE = "Credit Card Charge";
	public static final String TYPE_CUSTOMER_CREDIT_MEMO = "Customer Credit";
	public static final String TYPE_CUSTOMER_REFUNDS = "Customer Refund";
	public static final String TYPE_CUSTOMER_PRE_PAYMENT = "Customer prepayment";
	public static final String TYPE_ENTER_BILL = "Vendor Bill";
	public static final String TYPE_ESTIMATE = "Quote";
	public static final String TYPE_INVOICE = "Invoice";
	public static final String TYPE_ISSUE_PAYMENT = "Issue Payment";
	public static final String TYPE_MAKE_DEPOSIT = "Make Deposit";
	public static final String TYPE_PAY_BILL = "Vendor Payment";
	public static final String TYPE_RECEIVE_PAYMENT = "Customer Payment";
	public static final String TYPE_TRANSFER_FUND = "Transfer Fund";
	public static final String TYPE_VENDOR_CREDIT_MEMO = "Vendor Credit";
	public static final String TYPE_WRITE_CHECK = "Check";
	public static final String TYPE_JOURNAL_ENTRY = "Journal Entry";
	public static final String TYPE_PAY_TAX = "Pay Tax";
	public static final String TYPE_PAY_VAT = "Pay VAT";
	public static final String TYPE_RECEIVE_VAT = "Receive VAT";
	public static final String TYPE_SALES_ORDER = "Sales Order";
	public static final String TYPE_PURCHASE_ORDER = "Purchase Order";
	public static final String TYPE_ITEM_RECEIPT = "Item Receipt";
	public static final String TYPE_VAT_RETURN = "VAT Return";
	public static final String TYPE_VAT_ADJUSTMENT = "VAT Adjustment";
	public static final String TYPE_CASH_EXPENSE = "Cash Expense";
	public static final String TYPE_CREDIT_CARD_EXPENSE = "Credit Card Expense";
	public static final String TYPE_EMPLOYEE_EXPENSE = "Employee Expense";
	public static final String TYPE_STOCK_ADJUSTMENT = "Stock Adjustment";
	public static final String TYPE_TDS_CHALLAN = "TDS Challan";
	public static final String TYPE_BUILD_ASSEMBLY = "Build Assembly";
	public static final String TYPE_CHARGE = "Charge";
	public static final String TYPE_CREDIT = "Credit";

	// constants for Table Names

	public static final String TABLE_INVOICE = "INVOICE";
	public static final String TABLE_ENTER_BILL = "ENTER_BILL";
	public static final String TABLE_CASH_SALES = "CASH_SALES";
	public static final String TABLE_CASH_PURCHASE = "CASH_PURCHASE";
	public static final String TABLE_CREDIT_CARD_CHARGES = "CREDIT_CARD_CHARGES";
	public static final String TABLE_CUSTOMER_CREDIT_MEMO = "CUSTOMER_CREDIT_MEMO";
	public static final String TABLE_VENDOR_CREDIT_MEMO = "VENDOR_CREDIT";
	public static final String TABLE_WRITE_CHECKS = "WRITE_CHECKS";
	public static final String TABLE_TRANSACTION_MAKE_DEPOSIT = "TRANSACTION_MAKE_DEPOSIT";
	public static final String TABLE_CUSTOMER_REFUND = "CUSTOMER_REFUND";

	// CONSTANTS FOR TRANSACTION STATUS
	public static final String STATUS_NOT_PAID = "Not Paid";
	public static final String STATUS_PARTIALLY_PAID = "Partially Paid";
	public static final String STATUS_PAID = "Paid";
	public static final String STATUS_OPEN = "Open";
	public static final String STATUS_ACCEPTED = "Accepted";
	public static final String STATUS_REJECTED = "Rejected";
	public static final String STATUS_UN_APPLIED = "Un Applied";
	public static final String STATUS_PARTIALLY_APPLIED = "Partially Applied";
	public static final String STATUS_APPLIED = "Applied";
	public static final String STATUS_NOT_ISSUED = "Not Issued";
	public static final String STATUS_ISSUED = "Issued";
	public static final String STATUS_NEW = "New";
	public static final String STATUS_POSTED = "Posted";

	public static final String STATUS_NOT_INVOICED = "Not Invoiced";
	public static final String STATUS_PARTIALLY_INVOICED = "Partially Invoiced";
	public static final String STATUS_INVOICED = "Invoiced";
	public static final String STATUS_NOT_RECEIVED = "Not Received";
	public static final String STATUS_PARTIALLY_RECEIVED = "Partially Received";
	public static final String STATUS_RECEIVED = "Received";

	// UK VAT Returns Boxes names

	// public static final String BOX_1 =
	// "VAT due in this period on sales and other outputs";
	// public static final String BOX_2 =
	// "VAT due in this period on acquisitions from other EC member states";
	// public static final String BOX_3 =
	// "Total VAT due(the sum of boxes 1 and 2)";
	// public static final String BOX_4 =
	// "VAT reclaimed in this period on purchases and other inputs (including acquisitions from the EC)";
	// public static final String BOX_5 =
	// "Net VAT to be paid to Customs or reclaimed (Difference between Box 3 and Box4)";
	// public static final String BOX_6 =
	// "Total value of sales and other outputs excluding any VAT. Include Box 8 figure";
	// public static final String BOX_7 =
	// "Total value of puchases and all other inputs excluding any VAT. Include Box 9 figure";
	// public static final String BOX_8 =
	// "Total value of supplies of goods and related services, exculding any BAT to other EC Member states";
	// public static final String BOX_9 =
	// "Total value of all acquisitions of goods and related services, excluding any VAT from other EC Member states";

	// UK Boxe's names

	public static final String UK_BOX1_VAT_DUE_ON_SALES = "Box 1 VAT due in this period on sales and other outputs";

	public static final String UK_BOX2_VAT_DUE_ON_ACQUISITIONS = "Box 2 VAT due in this period on acquisitions from other EC member states";

	public static final String UK_BOX3_TOTAL_OUTPUT = "Box 3 Total VAT due (the sum of Box 1 and Box 2)";

	public static final String UK_BOX4_VAT_RECLAMED_ON_PURCHASES = "Box 4 VAT reclaimed in this period on purchases and other inputs (including acquisitions from the EC)";

	public static final String UK_BOX5_NET_VAT = "Box 5 Net VAT to be paid (positive number) or reclaimed (negative number) by you";

	public static final String UK_BOX6_TOTAL_NET_SALES = "Box 6 Total value of sales and all other outputs excluding any VAT includes Box 8 figure";

	public static final String UK_BOX7_TOTAL_NET_PURCHASES = "Box 7 Total value of purchases and all other inputs excluding any VAT includes Box 9 figure";

	public static final String UK_BOX8_TOTAL_NET_SUPPLIES = "Box 8 Total value of all supplies of goods and related costs excluding any VAT, to other EC member states";

	public static final String UK_BOX9_TOTAL_NET_ACQUISITIONS = "Box 9 Total value of all acquisitions of goods and related costs excluding any VAT, from other EC member states";

	public static final String UK_BOX10_UNCATEGORISED = "Uncategorised Vat Amounts";

	public static final String IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES = "Box1 VAT charged on supplies of Goods and Services";

	public static final String IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS = "Box2 VAT due on Intra-EC acquisitions";

	public static final String IRELAND_BOX3_VAT_ON_SALES = "T1 VAT on Sales";

	public static final String IRELAND_BOX4_VAT_ON_PURCHASES = "T2 VAT on Purchases";

	public static final String IRELAND_BOX5_T3_T4_PAYMENT_DUE = "T3/T4 (Payment Due if positive, Refund Owed if negative";

	public static final String IRELAND_BOX6_E1_GOODS_TO_EU = "E1 Total Goods to other EU countries";

	public static final String IRELAND_BOX7_E2_GOODS_FROM_EU = "E2 Total Goods from other EU countries";

	public static final String IRELAND_BOX8_TOTAL_NET_SALES = "Box 9 Total Net Value of Sales";

	public static final String IRELAND_BOX9_TOTAL_NET_PURCHASES = "Box 10 Total Net Value of Purchases";

	public static final String IRELAND_BOX10_UNCATEGORISED = "Uncategorised Vat Amounts";

	// VATReturnBox

	public static final String UK_DOMESTIC_SALES = "Domestic Sales <Box 1, Box 6> ";

	public static final String UK_DOMESTIC_PURCHASES = "Domestic Purchases <Box 4, Box 7";

	public static final String UK_EC_SALES_GOODS = "EC Sales of Goods <None, Box 8>";

	public static final String UK_EC_SALES_SERVICES = "EC Sales of Services <None, Box 6>";

	public static final String UK_EC_PURCHASES_GOODS = "EC PUrchases of Goods <Box 2, Box 9>";

	public static final String UK_REVERSE_CHARGE = "Reverse Charge (Carousel/MTIC) <Box 1, None>";

	public static final String UK_NOT_REGISTERED_SALES = "Domestic Not Registered Sales <Box 1, Box 6>";

	public static final String UK_NOT_REGISTERED_PURCHASES = "Domestic Not Registered Purchases <Box 4, Box 7>";

	public static final String IRELAND_DOMESTIC_SALES = "Domestic Sales <Box 1, Box 9> ";

	public static final String IRELAND_DOMESTIC_PURCHASES = "Domestic Purchases <T2, Box 10";

	public static final String IRELAND_EC_SALES_GOODS = "EC Sales of Goods <None, E1>";

	public static final String IRELAND_EC_PURCHASES_GOODS = "EC PUrchases of Goods <Box 2, E2>";

	public static final String IRELAND_EXEMPT_SALES = "Domestic Exempt Sales <Box 1, Box 9>";

	public static final String IRELAND_EXEMPT_PURCHASES = "Domestic Exempt Purchases <T2, Box 10>";

	public static final String IRELAND_NOT_REGISTERED_PURCHASES = "Domestic Not Registered Purchases <T2, Box 10>";

	public static final String IRELAND_NOT_REGISTERED_SALES = "Domestic Not Registered Sales <Box 1, Box 9>";

	public static final String BOX_NONE = "NONE";

	public static final String SALES_INCOME_TYPE_A = "Sales/Income Type A";
	public static final String SALES_INCOME_TYPE_B = "Sales/Income Type B";
	public static final String SALES_INCOME_TYPE_C = "Sales/Income Type C";
	public static final String SALES_INCOME_TYPE_D = "Sales/Income Type D";
	public static final String SALES_INCOME_TYPE_E = "Sales/Income Type E";
	public static final String MISCELLANEOUS_INCOME = "Miscellaneous Income";
	public static final String DISTRIBUTION_AND_CARRIAGE = "Distribution & Carriage";
	public static final String DISCOUNTS = "Discounts";
	public static final String COMMISSION_RECIEVED = "Commissions Received";
	public static final String CREDIT_CHARGES_LATEPAYMENT = "Credit Charges (Late Payment)";
	public static final String INSURANCE_CLAIMS = "Insurance Claims";
	public static final String INTEREST_INCOME = "Interest Income";
	public static final String RENT_INCOME = "Rent Income";
	public static final String ROYALTIES_RECIEVED = "Royalties Received";
	public static final String PROFIT_OR_LOSS_ON_SALES_ASSETS = "Profit/Loss on Sales of Assets";
	public static final String PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A = "Products/Materials Purchased Type A";
	public static final String PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_B = "Products/Materials Purchased Type B";
	public static final String PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_C = "Products/Materials Purchased Type C";
	public static final String PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_D = "Products/Materials Purchased Type D";
	public static final String PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_E = "Products/Materials Purchased Type E";
	public static final String PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_F = "Products/Materials Purchased Type F";
	public static final String OPENING_STOCK = "Stock Value Change";
	public static final String CLOSING_STOCK = "Closing Stock";
	public static final String OPEN_FINISHED_GOODS = "Finished Goods Value Change";
	public static final String CLOSE_FINISHED_GOODS = "Closing Finished Goods";
	public static final String OPEN_WORK_IN_PROGRESS = "Work in Progress Value Change";
	public static final String CLOSE_WORK_IN_PROGRESS = "Closing Work in Progress";
	public static final String CARRIAGE = "Carriage";
	public static final String DISCOUNTS_TAKEN = "Discounts Taken";
	public static final String IMPORT_DUTY = "Import Duty";

	public static final String DIRECT_LABOUR = "Direct Labour";
	public static final String DIRECT_EMPLOYERS_NI = "Direct Employers NI";
	public static final String OTHER_DIRECT_EMPLOYEE_RELATED_COSTS = "Other Direct Employee Related Costs";
	public static final String DIRECT_EXPENSES = "Direct Expenses";
	public static final String DIRECT_TRAVEL = "Direct Travel";
	public static final String DIRECT_CONSUMABLES = "Direct Consumables";
	public static final String MERCHANT_ACCOUNT_FEES = "Merchant Account Fees";
	public static final String COMMISSIONS_PAID = "Commissions Paid";

	public static final String INDIRECT_LABOUR = "Indirect Labour";
	public static final String INDIRECT_EMPLOYERS_NI = "Indirect Employers NI";
	public static final String DIRECTORS_REMUNERATION = "Directors Remuneration";
	public static final String CASUAL_LABOUR = "Casual Labour";
	public static final String EMPLOYERS_PANSION_CONTRIBUTIONS = "Employers Pension Contributions";
	public static final String SSP_RECLAIMED = "SSP Reclaimed";
	public static final String SMP_RECLAIMED = "SMP Reclaimed";
	public static final String EMPLOYEE_BENIFITS = "Employee Benefits";
	public static final String MEDICAL_INSURANCE = "Medical Insurance";
	public static final String RECRUITMENT = "Recruitment";
	public static final String TRAINING = "Training";

	public static final String RENT = "Rent";
	public static final String GENERAL_RATES = "General Rates";
	public static final String WATER_RATES = "Water Rates";
	public static final String ELECTRICITY = "Electricity";
	public static final String GAS = "Gas";
	public static final String OIL = "Oil";
	public static final String OFFICE_CLEANING = "Office Cleaning";
	public static final String OFFICE_MACHINE_MAINTENANCE = "Office Machine Maintenance";
	public static final String REPAIR_RENEWALS = "Repairs & Renewals";

	public static final String OFFICE_CONSUMABLES = "Office Consumables";
	public static final String BOOKS_ETC = "Books etc";
	public static final String INTERNET = "Internet";
	public static final String POSTAGE = "Postage";
	public static final String PRINTING = "Printing";
	public static final String STATIONERY = "Stationery";
	public static final String SUBSCRIPTIONS = "Subscriptions";
	public static final String TELEPHONE = "Telephone";
	public static final String CONFERENCES_AND_SEMINARS = "Conferences & Seminars";
	public static final String CHARITY_DONATIONS = "Charity Donations";

	public static final String INSURANCES_BUSINESS = "Insurances - Business";

	public static final String ADVERTISING_AND_MARKETING = "Advertising & Marketing";
	public static final String LOCAL_ENTERTAINMENT = "Local Entertainment";
	public static final String OVERSEAS_ENTERTAINMENT = "Overseas Entertainment";
	public static final String INDIRECT_LOCAL_TRAVEL = "Indirect Local Travel";
	public static final String INDIRECT_OVERSEAS_TRAVEL = "Indirect Overseas Travel";
	public static final String SUBSISTENCE = "Subsistence";

	public static final String VECHILE_EXPENSES = "Vehicle Expenses";
	public static final String VECHILE_INSURANCE = "Vehicle Insurance";
	public static final String VECHILE_REPAIRS_AND_SERVICING = "Vehicle Repairs & Servicing";

	public static final String PROFESSIONAL_FEES = "Professional Fees";
	public static final String ACCOUNTANCY_FEES = "Accountancy Fees";
	public static final String CONSULTANY_FEES = "Consultancy Fees";
	public static final String LEGAL_FEES = "Legal Fees";

	public static final String BANK_INTEREST_PAID = "Banks Interest Paid";
	public static final String BANK_CHARGES = "Bank Charges";
	public static final String CREDIT_CHARGES = "Credit Charges";
	public static final String LEASE_PAYMENTS = "Lease Payments";
	public static final String LOAN_INTEREST_PAID = "Loan Interest Paid";
	public static final String CURRENCY_CHARGES = "Currency Charges";
	public static final String EXCHANGE_RATE_VARIANCE = "Exchange Rate Variance";
	public static final String BAD_DEBT_PROVISION = "Bad Debt Provision";
	public static final String BAD_DEBT_WRITE_OFF = "Bad Debt Write Off";

	public static final String DEPRECIATION = "Depreciation";
	public static final String OFFICE_EQUIPMENT_DEPRECIATION = "Office Equipment Depreciation";
	public static final String IT_EQUIPMENT_DEPRECIATION = "IT Equipment Depreciation";
	public static final String FURNITURE_AND_FIXTURES_DEPRECIARION = "Furniture& Fixtures Depreciation";
	public static final String PLANT_OR_MACHINERY_DEPRECIATION = "Plant/Machinery Depreciation";
	public static final String VECHILE_DEPRECIATION = "Vehicle Depreciation";
	public static final String FREEHOLD_BUILDING_DEPRECIATION = "Freehold Building Depreciation";
	public static final String LEASEHOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION = "Leasehold Property Improvements Depreciation";

	public static final String DEBTORS_ACCOUNTS_RECEIVABLE = "Debtors (Accounts-Receivable)";
	public static final String BANK_CURRENT_ACCOUNT = "Bank Current Account";
	public static final String BANK_DEPOSIT_ACCOUNT = "Bank Deposit Account";
	public static final String PETTY_CASH = "Petty Cash";
	public static final String PRE_PAYMENTS = "Pre-Payments";
	public static final String ADVANCES_TO_EMPLOYEES = "Advances to Employees";
	public static final String STOCK = "Stock value";

	public static final String CREDITORS_ACCOUNTS_PAYBLE = "Creditors  (Accounts-Payable)";
	public static final String CREDIT_CARDS = "Credit Cards";
	public static final String PAYEE_EMPLOYEMENT_TAX = "PAYEE (Employment Tax)";
	public static final String NATIONAL_INSURANCE_TAX = "National Insurance tax";
	public static final String TAX_VAT_FILED = "Tax Filed";
	public static final String TAX_VAT_UNFILED = "Tax Unfiled";
	public static final String CORPORATION_RAX = "Corporation Tax";
	public static final String LOANS = "Loans";
	public static final String MORTGAGES = "Mortgages";
	public static final String ACCRUALS = "Accruals";
	public static final String DIRECTORS_CURRENT_ACCOUNT = "Directors Current Account";
	public static final String NET_SALARIES = "Net Salaries";
	public static final String PENSIONS = "Pensions";
	public static final String UNPAID_EXPENSE_CLAIMS = "Unpaid Expense Claims";

	public static final String FREEHOLD_BUILDINGS = "Freehold Buildings";
	public static final String ACCUMULATED_FREEHOLD_BUILDING_DEPRECIATION = "Accumulated Freehold Building Depreciation";
	public static final String LEASEHOLD_PROPERTY_IMPROVEMENTS = "Leasehold Property Improvements";
	public static final String ACCUMULATED_LEASE_HOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION = "Accumulated Leasehold Property Improvements Depreciation";
	public static final String OFFICE_EQUIPMENT = "Office Equipment";
	public static final String ACCUMULATED_OFFICE_EQUIPMENT_DEPRECIATION = "Accumulated Office Equipment Depreciation";
	public static final String IT_EQUIPMENT = "IT Equipment";
	public static final String ACCUMULATED_IT_EQUIPMENT_DEPRECIATION = "Accumulated IT Equipment Depreciation";
	public static final String FURNITURE_AND_FIXTURES = "Furniture & Fixtures";
	public static final String ACCUMULATED_FURNITURE_AND_FIXTURES_DEPRECIATION = "Accumulated Furniture & Fixtures Depreciation";
	public static final String PLANT_AND_MACHINERY = "Plant & Machinery";
	public static final String ACCUMULATED_PLANT_AND_MACHINERY_DEPRECIATION = "Accumulated Plant & Machinery Depreciation";
	public static final String VECHICLES = "Vehicles";
	public static final String ACCUMULATED_VEHICLES_DEPRECIATION = "Accumulated Vehicles Depreciation";
	public static final String INTANGIBLES = "Intangibles";

	public static final String LONG_TERM_LOANS = "Long Term Loans";
	public static final String HIRE_PURCHASE_CREDITORS = "Hire Purchase Creditors";
	public static final String DEFERRED_TAX = "Deferred Tax";
	
	public static final String FEDERAL_WITHHOLDING_TAX = "Federal Withholding Tax";
	public static final String STATE_WITHHOLDING_TAX = " Withholding Tax";
	public static final String SSN_TAX = "Social Security Tax";
	public static final String MEDICARE_TAX = "Medicare Tax";
	public static final String DISABILITY_TAX = " Disability Tax";
	public static final String TRAINING_TAX = " Training Tax";
	public static final String UNEMPLOYEMENT_TAX = " Unemployement Tax";

	public static final String BANK_REVALUATIONS = "Bank Revaluations";
	public static final String HISTORICAL_ADJUSTMENT = "Historical Adjustment";
	public static final String REALISED_CURRENCY_GAINS = "Realised Currency Gains";
	public static final String UNREALISED_CURRENCY_GAINS = "Unrealised Currency Gains";
	public static final String ROUNDING = "Rounding";
	public static final String VAT_ON_IMPORTS = "VAT on Imports";
	public static final String SUSPENSE = "Suspense";

	public static final String ORDINARY_SHARES = "Ordinary Shares";
	public static final String RESERVES_RETAINED_EARNINGS = "Reserves ";
	public static final String P_AND_L_BOUGHT_FORWARD_OR_YTD = "P&L Brought Forward/YTD";
	public static final String DIVIDENDS = "Dividends";

	public static final int MONTH_JANUARY = 0;
	public static final int MONTH_FEBRUARY = 1;
	public static final int MONTH_MARCH = 2;
	public static final int MONTH_APRIL = 3;
	public static final int MONTH_MAY = 4;
	public static final int MONTH_JUNE = 5;
	public static final int MONTH_JULY = 6;
	public static final int MONTH_AUGUST = 7;
	public static final int MONTH_SEPTEMBER = 8;
	public static final int MONTH_OCTOBER = 9;
	public static final int MONTH_NOVEMBER = 10;
	public static final int MONTH_DECEMBER = 11;
	public static final int YTD = 12;

	public static final String FINANCIAL_INDICATOR_SALES = "Sales";
	public static final String FINANCIAL_INDICATOR_DIRECT_COSTS = "Direct Costs";
	public static final String FINANCIAL_INDICATOR_GROSS_PROFIT = "Gross Profit";
	public static final String FINANCIAL_INDICATOR_INDIRECT_COSTS = "Indirect Costs";
	public static final String FINANCIAL_INDICATOR_NET_PROFIT = "Net Profit";
	public static final String FINANCIAL_INDICATOR_BANK_ACCOUNTS = "Bank Accounts";

	// Other Constants

	public static final String LESS_ACCUMULATaED_DEPRECIATION_ON = "Less Accumulated Depreciation on ";
	public static final String JOURNAL_ENTRY_FOR_DEPRECIATION = "Journal Entry for Running Depreciation";
	public static final String CREDIT_CARD_COMPANIES = "Credit Card Companies";

	// Vat Item Constants

	public static final String VAT_ITEM_EC_SALES_SERVICES_ZERO_RATED = "EC Sales Services Zero-Rated";
	public static final String VAT_ITEM_STANDARD_PURCHASES = "Standard Purchases";
	public static final String VAT_ITEM_ZERO_RATED_PURCHASES = "Zero-Rated Purchases";
	public static final String VAT_ITEM_EC_SALES_GOODS_ZERO_RATED = "EC Sales Goods Zero-Rated";

	/**
	 * Expenses default accounts names constants
	 */

	public static final String EXPENSE_ADEVRTISING_AND_MARKETING = "Aderstising and Marketing";
	public static final String EXPENSE_CONSULTING_AND_ACCOUNTING = "Consulting and Accounting";
	public static final String EXPENSE_DEPRICATION = "Deprication";
	public static final String EXPENSE_MEALS_AND_ENTERTAINMENT = "Meals & Entertainment";
	public static final String EXPENSE_GENERAL_EXPENSE = "General Expense";
	public static final String EXPENSE_INSURANCE = "Insurance";
	public static final String EXPENSE_INTEREST_EXPENSE = "Interest Expense";
	public static final String EXPENSE_LEGAL_EXPENSE = "Legal Expense";

	public static final String EXPENSE_MOTOR_VEICHLE = "Motor Veichle Expense";
	public static final String EXPENSE_VEICHEL_FUEL = "Veichle Fuel/Gas Expense";
	public static final String EXPENSE_VEICHLE_LEASE = "Veichle lease Expense";
	public static final String EXPENSE_VEICHLE_REPAIR_MAINTAINANCE = "Veichle Repair and Maintainance Expense";
	public static final String EXPENSE_SALARIES_WAGES = "Salaries and Wages";
	public static final String EXPENSE_SUPERANNUATION = "Superannuation";
	public static final String EXPENSE_SUBSCRIPTION = "Subscription";
	public static final String EXPENSE_BANKREVALUATION = "Back Revaluation";
	public static final String EXPENSE_UNREALISED_CURRENCY_GAIN = "Unrealized Currency Gains";
	public static final String EXPENSE_REALISED_CURRENCY_GAIN = "Realized Currency Gains";
	public static final String EXPENSE_INCOME_TAX_EXPENSE = "Income Tax Expenses";
	public static final String EXPENSE_BAD_DEBIT = "Bad Debit";
	public static final String EXPENSE_BANK_FEE_CHARGE = "Bank Fee Charges";
	public static final String EXPENSE_CREDIT_CARD_CHARGE = "Credit card charges";

	public static final String EXPENSE_TRAVEL = "Travel Expenses";
	public static final String EXPENSE_TRAVEL_INTERNATIONAL = "Travel-International";
	public static final String EXPENSE_TRAVEL_NATIONAL = "Travel-National";

	public static final String EXPENSE_AUTOMOBILE = "Automobile Expense";
	public static final String EXPENSE_POSTAGE = "Postage";

	public static final String EXPENSE_OTHER = "Other Expenses";
	public static final String EXPENSE_GAIN_LOSS = "Explain Gain or Loss";
	public static final String EXPENSE_MISCELLANEOUS = "Miscellaneous";
	public static final String EXPENSE_PENALTIES_SETELLMENTS = "Penalties & Settelments";

	public static final String EXPENSE_UTILITIES = "Utilities";
	public static final String EXPENSE_TAX_LISENSES = "Tax and Lisence";

	public static final String EXPENSE_COST_OF_GOODS_SOLDs = "Cost of Goods Sold";
	public static final String EXPENSE_PRODUCT_PURCHASE_A = "Product/Material purchased Type-A";
	public static final String EXPENSE_PRODUCT_PURCHASE_B = "Product/Material purchased Type-B";
	public static final String EXPENSE_PRODUCT_PURCHASE_C = "Product/Material purchased Type-C";
	public static final String EXPENSE_PRODUCT_PURCHASE_D = "Product/Material purchased Type-D";
	public static final String EXPENSE_PRODUCT_PURCHASE_E = "Product/Material purchased Type-E";
	public static final String EXPENSE_PRODUCT_PURCHASE_F = "Product/Material purchased Type-F";
	public static final String EXPENSE_CARRIAGE = "Carriage";
	public static final String EXPENSE_DISCOUNTS = "Discounts Taken";
	public static final String EXPENSE_IMPORT_DUTY = "Import Duty";
	public static final String EXPENSE_STOCK_VALUE_CHAGE = "Stock Value Change";
	public static final String EXPENSE_FINISHED_GOOD_VALUE = "Fisnished Good Value change";
	public static final String EXPENSE_WORK_IN_PROGRESS_VALUE = "Work in progress Value Change";

	public static final String EXPENSE_OTHER_DIRECT_COST = "Other Direct Costs";
	public static final String EXPENSE_DIRECT_LABOUR = "Direct Labour";
	public static final String EXPENSE_DIRECT_EMPLOYEE_NI = "Direct Employee NI";
	public static final String EXPENSE_DIRECT_EMPLOYEE_RELATED_COSTS = "Other direct Employee Related costs";
	public static final String EXPENSE_DIRECT = "Direct Expenses";
	public static final String EXPENSE_DIRECT_TRAVEL = "Direct Travel";
	public static final String EXPENSE_DIRECT_CONSUMABLE = "Direct Consumable";
	public static final String EXPENSE_MERCHANY_ACCOUNT_FEES = "Merchant Account Fees";
	public static final String EXPENSE_COMISSION_PAID = "Cosmission Paid";

	public static final String EXPENSE_INDIRECT_COSTS = "Indirect Costs";

	/**
	 * income default accounts names constants
	 */
	public static final String INCOME_SALES = "Sales";
	public static final String INCOME_SALES_OF_PRODUCT_INCOME = "Sales of Product income";
	public static final String INCOME_OTHER = "Other Income";
	public static final String INCOME_GENERAL = "General Income";
	public static final String INCOME_INTEREST = "Interest Income";
	public static final String INCOME_LATE_FEE = "Late Fee Income";
	public static final String INCOME_SHIPPING = "Shipping Income";
	public static final String INCOME_REFUNDS_ALLOWANCE = "Refund Allowance";
	public static final String INCOME_FEES_BILLED = "Fees Billed";
	public static final String INCOME_SERVICES = "Services";
	public static final String INCOME_SALES_TYPE_B = "Sales/Income Type B";
	public static final String INCOME_SALES_TYPE_C = "Sales/Income Type C";
	public static final String INCOME_SALES_TYPE_D = "Sales/Income Type D";
	public static final String INCOME_SALES_TYPE_E = "Sales/Income Type E";
	public static final String INCOME_MISCELLANEOUS = "Miscellaneous Income";
	public static final String INCOME_DISTRIBUTION_CARRIAGES = "Distribution & Carriage";
	public static final String INCOME_COMMISION_RECEIVED = "Commissions Received";
	public static final String INCOME_CREDIT_CHARGES = "Credit Charges";
	public static final String INCOME_INSURANCE_CLAIMS = "Insurance Claims";
	public static final String INCOME_RENT = "Rent Income";
	public static final String INCOME_ROYALTIES_RECEIVED = "Royalties Received";
	public static final String INCOME_PROFIT_LOSS_IN_SALE_OF_ASSETS = "Profit/Loss on Sales of Assets";

	/**
	 * assets default accounts names constants
	 */
	public static final String ASSETS_CURRENT = "Current Assets";
	public static final String ASSETS_ACCOUNTS_RECEIVABLES = "Accounts Receivables";
	public static final String ASSETS_PREPAYMENTS = "Prepayments";
	public static final String ASSETS_DEBTORS = "Debtors";
	public static final String ASSETS_DEPOSITS = "Depositis";
	public static final String ASSETS_BANK_CURRENT_ACCOUNT = "Bank Current Accounut";
	public static final String ASSETS_BANK_DEPOSITE_ACCOUNT = "Bank Deposite Account";
	public static final String ASSETS_UNDEPOSTITED_FUNDS = "Un Depostited Funds";
	public static final String ASSETS_CURRENT_PETTY_CASH = "Peety Cash";
	public static final String ASSETS_PRE_PAYMENTS = "Pre Payments";
	public static final String ASSETS_ADVANCES_OF_EMPLOYEES = "Advances to Employees";
	public static final String ASSETS_STOCKVALUES = "Stock values";
	public static final String ASSETS_OFFICE_EQUIPMENTS = "Office Equipments";
	public static final String ASSETS_LAD_OFFICE_EQUIPMENTS = "Less Accumulation Depriciation on Office Equipments";
	public static final String ASSETS_COMPUTER_EQUIPMENTS = "Computer Equipments";
	public static final String ASSETS_LAD_COMPUTER_EQUIPMENTS = "Less Accumulation Depriciation on Computer Equipments";
	public static final String ASSETS_FREEHOLD_BUILDINGS = "Freehold buildings";
	public static final String ASSETS_AF_BUILDING_DEPRICIATION = "Accumulated Freehold Building Depriciation";
	public static final String ASSETS_LEASEHOLD_PROPERTY_IMPROVEMENTS = "Leasehold property improvements";
	public static final String ASSETS_AL_PROPERTY_IMPROVEMENT_DEPRICIATION = "Accumulated Leasehold property improvements Depriciation";
	public static final String ASSETS_IT_EQUIPMENTS = "IT Equipments";
	public static final String ASSETS_A_OFFICE_EQUIPMENTS_DEPRICIATION = "Accumulated Office Equipments Depriciation";
	public static final String ASSETS_A_IT_EQUIPMENTS_DEPRECIATION = "Accumulated IT Equipments Depriciations";
	public static final String ASSETS_FURNITURES_FIXTURES = "Furnitures and Fixtures";
	public static final String ASSETS_A_FURNITURE_FIXTURES_DEPRICIATION = "Accumulated Furniture and Fixture Depriciation";
	public static final String ASSETS_PLANTS_mACHINARY = "Plant and Machinary";
	public static final String ASSETS_A_PLANT_MACHINARY_DEPRICIATION = "Accumulated Plant & MAchinary Depriciation";
	public static final String ASSETS_VEHICLES = "Veichles";
	public static final String ASSETS_A_VEICHLES_DEPRICIATION = "Accumulted Vehicles Depriciations";
	public static final String ASSETS_INTANGIBLES = "Intangibles";
	public static final String ASSETS_FIXED = "Fixed Assets";
	public static final String ASSETS_OTHER_CURRENT = "Other Current Assets";
	public static final String ASSETS_CASH = "Cash";
	public static final String ASSETS_BANK = "Bank";
	public static final String ASSETS_ADVANCE_TAX = "Advance Tax";
	public static final String ASSETS_INVENTORY = "Inventory Assets";
	public static final String ASSETS_PREPAID_EXPENSES = "Prepaid Expenses";
	public static final String ASSETS_UNDEPOSITED_FUNDS = "Undeposited Funds";
	public static final String ASSETS_SERVICE_TAX_INCURED_ON_EXPENSES = "Service Tax incurred on Expenses";
	public static final String ASSETS_UNDEPOSITIED_FUNDS = "Undeposited Funds";
	public static final String ASSETS_CASH_PETTY_CASH = "Petty Cash";

	/**
	 * liabilities fault names constants
	 */
	public static final String EQUITY = "Equity";
	public static final String EQUITY_RETAINED_EARINGS = "Retained Earnings";
	public static final String EQUITY_OWNER_SHARE = "Owner a Share Capital";
	public static final String EQUITY_OPENING_BALANCE_OFFSET = "Opening Balance Offset";
	public static final String EQUITY_GAIN_LOSS_EXCHANGE = "Gain/Loss on Exchange";
	public static final String EQUITY_ORDINARY_SHARES = "Ordinary Shares";
	public static final String EQUITY_OPENING_BALANCE = "Opening Balance";
	public static final String EQUITY_RESERVES = "Reservers";
	public static final String EQUITY_YTD = "P&L Brought Forward/YTD";
	public static final String EQUITY_DIVIDEND = "Dividends";

	public static final String LIABLITY_SECURED_LOAN = "Secured Loans";
	public static final String LIABLITY_UNSECURED_LOAN = "Unsecured Loans";
	public static final String LIABLITY_LOAN = "Loans";
	public static final String LIABLITY_NON_CURRENT = "Non Current Liabilities";
	public static final String LIABLITY_CLIENT_CREDITS = "Client Credits";
	public static final String LIABLITY_LONG_TERM = "Long Term Liabilities";
	public static final String LIABLITY_LONG_TERM_LOANS = "Long Term Loans";
	public static final String LIABLITY_HIRE_PURCHASE_CREDITORS = "Hire Purchase Creditors";
	public static final String LIABLITY_DEFERRED_TAX = "Deffered TAx";
	public static final String LIABLITY_OTHER_BALANCE_SHEET_CATEGORY = "Other Balance Sheet Category";
	public static final String LIABLITY_BANK_REVALUATIONS = "Bank Revaluations";
	public static final String LIABLITY_HISTORICAL_ADJUSTMENTS = "Historical Adjustments";
	public static final String LIABLITY_REALISED_CURRENCY_GAINS = "Realised Currency gains";
	public static final String LIABLITY_UNREALISED_CURRENCY_GAINS = "Unrealised Currency Gains";
	public static final String LIABLITY_ROUNDINGS = "Roundings";
	public static final String LIABLITY_VAT_ON_IMPORTS = "Vat on Import";
	public static final String LIABLITY_SUSPENSES = "Suspenses";

	public static final String LIABLITY_CURRENT = "Current Liabilities";
	public static final String LIABLITY_TAXPAYABLE = "Tax Payables";
	public static final String LIABLITY_VATPAYABLE = "VAT Payable";
	public static final String LIABLITY_CSTPAYABLE = "CST Payable";
	public static final String LIABLITY_EMPLOYEE_TAXPAYABLE = "Employee Tax Payable";
	public static final String LIABLITY_OTHER_CURRENT_LIABILITIES = "Other Current Liablities";
	public static final String LIABLITY_INCOME_TAX_PAYABLE = "Income Tax payables";
	public static final String LIABLITY_SALES_TAX = "Sales Tax";
	public static final String LIABLITY_WAGES_PAYABLE = "Wages Payables";
	public static final String LIABLITY_ACCOUNTS_PAYABLE = "Accounts Payables";
	public static final String LIABLITY_SUPPERANNATION_PAYABLE = "Supperannuations Payables";
	public static final String LIABLITY_UNPAID_EXPENSES_CLAIMS = "Unpaid Expense Claims";
	public static final String LIABLITY_TRACKING_TRANSFERS = "TYracking Transfers";
	public static final String LIABLITY_OWNER_DRAWING = "Owner a Drawings";
	public static final String LIABLITY_OWNER_FUND_INTRODUCED = "Owner a funds Introduced";
	public static final String LIABLITY_CREDIT_CARD = "Credit Cards";
	public static final String LIABLITY_SERVICE_TAXPAYABLE = "Service Tax Payable";
	public static final String LIABLITY_CREDITORS = "Creditors";
	public static final String LIABLITY_PAYEE = "Payee(Employment Tax)";
	public static final String LIABLITY_NAT = "National Insurance Tax";
	public static final String LIABLITY_SALESTAX_UNFILED = "Sales Tax(VAT)Unfiled";
	public static final String LIABLITY_SALESTAX_FILLED = "Sales Tax(VAT)Filed";
	public static final String LIABLITY_CORPORATIONTAX = "Corporation Tax";
	public static final String LIABLITY_LOANS = "Loans";
	public static final String LIABLITY_MORTGAGES = "Mortgages";
	public static final String LIABLITY_ACCURALS = "Accurals";
	public static final String LIABLITY_DIRECTORS_CURRENT_ACCOUNT = "Directors Current Account";
	public static final String LIABLITY_NET_SALARIES = "Net Salaries";
	public static final String LIABLITY_PENSIONS = "Pensions";
	public static final String ddMMyyyy = "dd/MM/yyyy";
	public static final String MMddyyyy = "MM/dd/yyyy";
	public static final String yyyyMMdd = "yyyy/MM/dd";

	public static final String TDS_TAX_PAYABLE = "TDS Tax Payable";
	public static final String TDS_DEDUCTED_BY_OTHERS = "TDS Deducted by Others";
	public static final String SERVICE_TAX_PAYABLE = "Service Tax Payable";
	public static final String CENTRAL_SALES_TAX_PAYABLE = "Central Sales Tax Payable";
	public static final String EXCHANGE_LOSS_OR_GAIN = "Exchange Loss or Gain";
	public static final String GST_TAX_PAYABLE = "GST Tax Payable";
	public static final String QST_TAX_PAYABLE = "QST Tax Payable";
	public static final String CONSUMPTION_TAX_PAYABLE = "Consumption Tax Payable";
	public static final String TYPE_VENDOR_PAYMENT = "Vendor Prepayment";
	public static final String SALARIES_PAYABLE = "Salaries Payable";
	public static final String TYPE_PAY_RUN = "Pay Run";
	public static final String TYPE_PAY_EMPLOYEE = "Pay Employee";
}
