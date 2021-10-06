package com.nitya.accounter.web.client.ui;


class Section {
	String title;
	String[] heads;
	String[][] options;

	Section(String t, String[] h, String[][] o) {
		title = t;
		heads = h;
		options = o;
	}
}

// public interface IOptionsAccordionSections {
// AccounterMessages messages = messages;
// final String COMPANY = messages.company();
// final String COMPANY_NEW_ACCOUNT = messages.newAccount();
//
// final String CUSTOMERS = messages.payees(
// Global.get().customers());
// final String CUSTOMERS_HOME = messages.payeesHome(
// Global.get().customers());
// final String CUSTOMERS_NEW = messages.newPayee(
// Global.get().Customer());
// final String CUSTOMERS_NEW_ITEM = messages.newItem();
// final String CUSTOMERS_NEW_QUOTE = messages.newQuote();
// final String CUSTOMERS_NEW_INVOICE = messages.newInvoice();
// final String CUSTOMER_RECEIVED_PAYMENT = messages.receivedPayment();
// final String CUSTOMER_NEW_CASH_SALE = messages.newCashSale();
// final String CUSTOMER_NEW_CREDITS_AND_REFUNDS = messages.creditAndRefunds();
// final String CUSTOMER_CREDIT_CARD_PROCESSING = messages
// .creditCardProcessing();
// final String CUSTOMER_FIND_CUSTOMERS = messages.payees(
// Global.get().customers());
// final String CUSTOMER_FIND_ITEMS = messages.products();
// final String CUSTOMER_FIND_QUOTES = messages.quotes();
// final String CUSTOMER_FIND_INVOICES = messages.invoices();
// final String CUSTOMER_FIND_RECEIVED_PAYMENTS = messages.receivedPayments();
// final String CUSTOMER_FIND_CUSTOMER_REFUNDS = messages
// .customerRefunds(Global.get().customer());
//
// final String VENDORS = Global.get().messages()
// .payees(Global.get().vendors());
// final String VENDORS_HOME = Global.get().messages().payeesHome(
// Global.get().vendors());
// final String VENDORS_NEW_VENDOR = Global.get().messages().newPayee(
// Global.get().Vendor());
// final String VENDORS_NEW_ITEM = messages.newProduct();
// final String VENDORS_ENTER_BILLS = messages.enterBills();
// final String VENDORS_PAY_BILLS = messages.payBills();
// final String VENDORS_NEW_CASH_PURCHASE = messages.newCashPurchase();
// final String VENDORS_ISSUE_PAYMENT = messages.issuePayment();
// final String VENDORS_NEW_CREDIT_MEMO = messages.newCredit();
// final String VENDORS_FIND_VENDORS = Global.get().messages().payees(
// Global.get().vendors());
// final String VENDORS_FIND_ITEMS = messages.products();
// final String VENDORS_FIND_BILLS = messages.bills();
// final String VENDORS_FIND_VENDOR_PAYMENTS = Global.get().messages()
// .payeePayment(Global.get().Vendor());
//
// final String BANKING = messages.banking();
// final String BANKING_HOME = messages.bankingHome();
// final String BANKING_NEW_BANK_ACCOUNT = messages
// .newBankAccount();
// final String BANKING_WRITE_CHECKS = messages.writeChecks();
// final String BANKING_MAKE_DEPOSIT = messages.makeDeposit();
// final String BANKING_TRANSFER_FUNDS = messages.transferFunds();
// final String BANKING_PAY_BILLS = messages.payBills();
// final String BANKING_CREDITCARD_CHARGE = messages.creditCardCharge();
// final String BANKING_FIND_CHART_OF_ACCOUNTS = messages
// .chartOfAccounts();
// final String BANKING_FIND_PAYMENTS = messages.payments();
//
// final String START_NEW_TASK = messages.startanewTask();
// final String HOME = messages.companyHome();
// final String FIND = messages.find();
//
// final Section[] sections = {
//
// new Section(COMPANY, new String[] { START_NEW_TASK },
// new String[][] { { HOME, COMPANY_NEW_ACCOUNT } }),
//
// new Section(CUSTOMERS, new String[] { START_NEW_TASK, FIND },
// new String[][] {
// { CUSTOMERS_HOME, CUSTOMERS_NEW,
// CUSTOMERS_NEW_ITEM, CUSTOMERS_NEW_QUOTE,
// CUSTOMERS_NEW_INVOICE,
// CUSTOMER_RECEIVED_PAYMENT,
// CUSTOMER_NEW_CASH_SALE,
// CUSTOMER_NEW_CREDITS_AND_REFUNDS,
// CUSTOMER_CREDIT_CARD_PROCESSING },
// { CUSTOMER_FIND_CUSTOMERS, CUSTOMER_FIND_ITEMS,
// CUSTOMER_FIND_QUOTES,
// CUSTOMER_FIND_INVOICES,
// CUSTOMER_FIND_RECEIVED_PAYMENTS,
// CUSTOMER_FIND_CUSTOMER_REFUNDS } }),
//
// new Section(VENDORS, new String[] { START_NEW_TASK, FIND },
// new String[][] {
// { VENDORS_HOME, VENDORS_NEW_VENDOR,
// VENDORS_NEW_ITEM, VENDORS_ENTER_BILLS,
// VENDORS_PAY_BILLS,
// VENDORS_NEW_CASH_PURCHASE,
// VENDORS_ISSUE_PAYMENT,
// VENDORS_NEW_CREDIT_MEMO },
// { VENDORS_FIND_VENDORS, VENDORS_FIND_ITEMS,
// VENDORS_FIND_BILLS,
// VENDORS_FIND_VENDOR_PAYMENTS } }),
//
// // new Section("Employees",
// // new String[] { "Start a new Task", "Find" },
// // new String[][] {
// // { "Employee Empty", "Employee Blah",
// // "Employee Temployee" },
// // { "Find tis", "Find tat", "Find bla" } }),
//
// // new Section("Animals", new String[] { "Domestic", "Wild",
// // "Whatever" }, new String[][] { { "Dog", "Cat" },
// // { "Lion", "Anaconda", "Tiger" },
// // { "Mouse", "Elephant", "Snake" } }), };
//
// new Section(BANKING, new String[] { START_NEW_TASK, FIND },
// new String[][] {
// { BANKING_HOME, BANKING_NEW_BANK_ACCOUNT,
// BANKING_WRITE_CHECKS, BANKING_MAKE_DEPOSIT,
// BANKING_TRANSFER_FUNDS, BANKING_PAY_BILLS,
// BANKING_CREDITCARD_CHARGE },
// { BANKING_FIND_CHART_OF_ACCOUNTS,
// BANKING_FIND_PAYMENTS
//
// }
//
// }), };
//
// final String[] sectionLabels = {
// messages.payees(Global.get().customers()),
// Global.get().messages().payees(Global.get().vendors()),
// messages.employees(), messages.sectionhash1(),
// messages.sectionhash2(), messages.sectionhash3(),
// messages.sectionhash4() };
//
// final String[][][] labels = {
//
// };
// final String[] custStartLabels = {
// messages.newPayee(Global.get().Customer()),
// messages.newProduct(), messages.newQuote(), messages.newInvoice(),
// messages.receivedPayment(), messages.newCashSale(),
// messages.newCreditsAndRefunds(), messages.creditCardProcessing() };
//
// final String[] custFindLabels = {
// messages.payees(Global.get().customers()),
// messages.products() };
//
// // Vendors Section
// final String[] vendStartLabels = {
// Global.get().messages().vendorBendor(Global.get().Vendor()),
// Global.get().messages().vendorBlah(Global.get().Vendor()),
// Global.get().messages().vendorWhendor(Global.get().Vendor()) };
//
// final String[] vendFindLabels = { messages.findtis(), messages.findtat(),
// messages.findbla() };
//
// // Employees Section
// final String[] empStartLabels = { messages.employeeEmpty(),
// messages.employeeBlah(), messages.employeeTemployee() };
//
// final String[] empFindLabels = { messages.findtis(), messages.findtat(),
// messages.findbla() };
//
// // ...
// final String[] startLabels = { messages.emptyexlematorymark1(),
// messages.emptyexlematorymark2(), messages.enuffexlematorymark() };
//
// }
