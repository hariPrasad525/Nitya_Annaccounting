package com.vimukti.accounter.core;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class Utility_R {

	int version;

	public Utility_R() {

	}

	public int getVersion() {
		return version;
	}

	public static String getTransactionName(int transactionType) {

		String transactionName = null;
		switch (transactionType) {
		case Transaction.TYPE_CASH_SALES:
			transactionName = AccounterServerConstants.TYPE_CASH_SALES;
			break;
		case Transaction.TYPE_CASH_PURCHASE:
			transactionName = AccounterServerConstants.TYPE_CASH_PURCHASE;
			break;
		case Transaction.TYPE_CREDIT_CARD_CHARGE:
			transactionName = AccounterServerConstants.TYPE_CREDIT_CARD_CHARGE;
			break;
		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			transactionName = AccounterServerConstants.TYPE_CUSTOMER_CREDIT_MEMO;
			break;
		case Transaction.TYPE_CUSTOMER_REFUNDS:
			transactionName = AccounterServerConstants.TYPE_CUSTOMER_REFUNDS;
			break;
		case Transaction.TYPE_CUSTOMER_PRE_PAYMENT:
			transactionName = AccounterServerConstants.TYPE_CUSTOMER_PRE_PAYMENT;
			break;
		case Transaction.TYPE_ENTER_BILL:
			transactionName = AccounterServerConstants.TYPE_ENTER_BILL;
			break;
		case Transaction.TYPE_ESTIMATE:
			transactionName = AccounterServerConstants.TYPE_ESTIMATE;
			break;
		case Transaction.TYPE_INVOICE:
			transactionName = AccounterServerConstants.TYPE_INVOICE;
			break;
		case Transaction.TYPE_ISSUE_PAYMENT:
			transactionName = AccounterServerConstants.TYPE_ISSUE_PAYMENT;
			break;
		case Transaction.TYPE_MAKE_DEPOSIT:
			transactionName = AccounterServerConstants.TYPE_MAKE_DEPOSIT;
			break;
		case Transaction.TYPE_PAY_BILL:
			transactionName = AccounterServerConstants.TYPE_PAY_BILL;
			break;
		case Transaction.TYPE_RECEIVE_PAYMENT:
			transactionName = AccounterServerConstants.TYPE_RECEIVE_PAYMENT;
			break;
		case Transaction.TYPE_TRANSFER_FUND:
			transactionName = AccounterServerConstants.TYPE_TRANSFER_FUND;
			break;
		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
			transactionName = AccounterServerConstants.TYPE_VENDOR_CREDIT_MEMO;
			break;
		case Transaction.TYPE_WRITE_CHECK:
			transactionName = AccounterServerConstants.TYPE_WRITE_CHECK;
			break;
		case Transaction.TYPE_JOURNAL_ENTRY:
			transactionName = AccounterServerConstants.TYPE_JOURNAL_ENTRY;
			break;
		case Transaction.TYPE_PAY_TAX:
			transactionName = AccounterServerConstants.TYPE_PAY_TAX;
			break;
		case Transaction.TYPE_TAX_RETURN:
			transactionName = AccounterServerConstants.TYPE_VAT_RETURN;
			break;
		case Transaction.TYPE_RECEIVE_TAX:
			transactionName = AccounterServerConstants.TYPE_RECEIVE_VAT;
			break;
		case Transaction.TYPE_ADJUST_VAT_RETURN:
			transactionName = AccounterServerConstants.TYPE_VAT_ADJUSTMENT;
			break;
		case Transaction.TYPE_PURCHASE_ORDER:
			transactionName = AccounterServerConstants.TYPE_PURCHASE_ORDER;
			break;
		case Transaction.TYPE_ITEM_RECEIPT:
			transactionName = AccounterServerConstants.TYPE_ITEM_RECEIPT;
			break;
		case Transaction.TYPE_CASH_EXPENSE:
			transactionName = AccounterServerConstants.TYPE_CASH_EXPENSE;
			break;
		case Transaction.TYPE_CREDIT_CARD_EXPENSE:
			transactionName = AccounterServerConstants.TYPE_CREDIT_CARD_EXPENSE;
			break;
		case Transaction.TYPE_EMPLOYEE_EXPENSE:
			transactionName = AccounterServerConstants.TYPE_EMPLOYEE_EXPENSE;
			break;
		case Transaction.TYPE_STOCK_ADJUSTMENT:
			transactionName = AccounterServerConstants.TYPE_EMPLOYEE_EXPENSE;
			break;
		case Transaction.TYPE_PAY_RUN:
			transactionName = AccounterServerConstants.TYPE_PAY_RUN;
			break;
		case Transaction.TYPE_PAY_EMPLOYEE:
			transactionName = AccounterServerConstants.TYPE_PAY_EMPLOYEE;
			break;
		}
		return transactionName;
	}

	public static String getAccountTypeString(int accountingType,
			int accountType) {

		String accountTypeName = null;
		switch (accountType) {
		case Account.TYPE_INCOME:
			if (accountingType == Company.ACCOUNTING_TYPE_US) {
				accountTypeName = AccounterServerConstants.TYPE_INCOME;
			} else if (accountingType == Company.ACCOUNTING_TYPE_UK) {
				accountTypeName = AccounterServerConstants.TYPE_REVENUE;
			}
			break;
		case Account.TYPE_OTHER_INCOME:
			accountTypeName = AccounterServerConstants.TYPE_OTHER_INCOME;
			break;
		case Account.TYPE_EXPENSE:
			if (accountingType == Company.ACCOUNTING_TYPE_US) {
				accountTypeName = AccounterServerConstants.TYPE_EXPENSE;
			} else if (accountingType == Company.ACCOUNTING_TYPE_UK) {
				accountTypeName = AccounterServerConstants.TYPE_INDIRECT_COSTS;
			}

			break;
		case Account.TYPE_OTHER_EXPENSE:
			if (accountingType == Company.ACCOUNTING_TYPE_US) {
				accountTypeName = AccounterServerConstants.TYPE_OTHER_EXPENSE;
			} else if (accountingType == Company.ACCOUNTING_TYPE_UK) {
				accountTypeName = AccounterServerConstants.TYPE_OTHER_DIRECT_COSTS;
			}

			break;
		case Account.TYPE_COST_OF_GOODS_SOLD:
			if (accountingType == Company.ACCOUNTING_TYPE_US) {
				accountTypeName = AccounterServerConstants.TYPE_COST_OF_GOODS_SOLD;
			} else if (accountingType == Company.ACCOUNTING_TYPE_UK) {
				accountTypeName = AccounterServerConstants.TYPE_DIRECT_PRODUCTS_AND_MATERIAL_COSTS;
			}
			break;
		case Account.TYPE_CASH:
			accountTypeName = AccounterServerConstants.TYPE_CASH;
			break;
		case Account.TYPE_BANK:
			accountTypeName = AccounterServerConstants.TYPE_BANK;
			break;
		case Account.TYPE_OTHER_CURRENT_ASSET:
			if (accountingType == Company.ACCOUNTING_TYPE_US) {
				accountTypeName = AccounterServerConstants.TYPE_OTHER_CURRENT_ASSET;
			} else if (accountingType == Company.ACCOUNTING_TYPE_UK) {
				accountTypeName = AccounterServerConstants.TYPE_CURRENT_ASSET;
			}
			break;
		case Account.TYPE_INVENTORY_ASSET:
			if (accountingType == Company.ACCOUNTING_TYPE_US)
				accountTypeName = AccounterServerConstants.TYPE_INVENTORY_ASSET;
			else if (accountingType == Company.ACCOUNTING_TYPE_UK)
				accountTypeName = AccounterServerConstants.TYPE_STOCK_ASSET;
			break;
		case Account.TYPE_OTHER_ASSET:
			accountTypeName = AccounterServerConstants.TYPE_OTHER_ASSET;
			break;
		case Account.TYPE_FIXED_ASSET:
			accountTypeName = AccounterServerConstants.TYPE_FIXED_ASSET;
			break;
		case Account.TYPE_CREDIT_CARD:
			accountTypeName = AccounterServerConstants.TYPE_CREDIT_CARD;
			break;
		case Account.TYPE_PAYROLL_LIABILITY:
			accountTypeName = AccounterServerConstants.TYPE_PAYROLL_LIABILITY;
			break;
		case Account.TYPE_OTHER_CURRENT_LIABILITY:
			if (accountingType == Company.ACCOUNTING_TYPE_US) {
				accountTypeName = AccounterServerConstants.TYPE_OTHER_CURRENT_LIABILITY;
			} else if (accountingType == Company.ACCOUNTING_TYPE_UK) {
				accountTypeName = AccounterServerConstants.TYPE_CURRENT_LIABILITY;
			}
			break;
		case Account.TYPE_LONG_TERM_LIABILITY:
			accountTypeName = AccounterServerConstants.TYPE_LONG_TERM_LIABILITY;
			break;
		case Account.TYPE_EQUITY:
			if (accountingType == Company.ACCOUNTING_TYPE_US) {
				accountTypeName = AccounterServerConstants.TYPE_EQUITY;
			} else if (accountingType == Company.ACCOUNTING_TYPE_UK) {
				accountTypeName = AccounterServerConstants.TYPE_CAPITAL_AND_RESERVES;
			}
			break;
		case Account.TYPE_ACCOUNT_RECEIVABLE:
			if (accountingType == Company.ACCOUNTING_TYPE_US)
				accountTypeName = AccounterServerConstants.TYPE_ACCOUNT_RECEIVABLE;
			else if (accountingType == Company.ACCOUNTING_TYPE_UK)
				accountTypeName = AccounterServerConstants.TYPE_SALES_LEDGER;
			break;
		case Account.TYPE_ACCOUNT_PAYABLE:
			if (accountingType == Company.ACCOUNTING_TYPE_US)
				accountTypeName = AccounterServerConstants.TYPE_ACCOUNT_PAYABLE;
			else if (accountingType == Company.ACCOUNTING_TYPE_US)
				accountTypeName = AccounterServerConstants.TYPE_PURCHASE_LEDGER;
			break;
		}
		return accountTypeName;
	}

	public static int getAccountSubBaseType(int accountType) {

		switch (accountType) {

		case Account.TYPE_CASH:
			return Account.SUBBASETYPE_CURRENT_ASSET;
		case Account.TYPE_BANK:
			return Account.SUBBASETYPE_CURRENT_ASSET;
		case Account.TYPE_ACCOUNT_RECEIVABLE:
			return Account.SUBBASETYPE_CURRENT_ASSET;
		case Account.TYPE_OTHER_CURRENT_ASSET:
			return Account.SUBBASETYPE_CURRENT_ASSET;
		case Account.TYPE_INVENTORY_ASSET:
			return Account.SUBBASETYPE_CURRENT_ASSET;
		case Account.TYPE_FIXED_ASSET:
			return Account.SUBBASETYPE_FIXED_ASSET;
		case Account.TYPE_OTHER_ASSET:
			return Account.SUBBASETYPE_OTHER_ASSET;
		case Account.TYPE_ACCOUNT_PAYABLE:
			return Account.SUBBASETYPE_CURRENT_LIABILITY;
		case Account.TYPE_CREDIT_CARD:
			return Account.SUBBASETYPE_CURRENT_LIABILITY;
		case Account.TYPE_OTHER_CURRENT_LIABILITY:
			return Account.SUBBASETYPE_CURRENT_LIABILITY;
		case Account.TYPE_PAYROLL_LIABILITY:
			return Account.SUBBASETYPE_CURRENT_LIABILITY;
		case Account.TYPE_LONG_TERM_LIABILITY:
			return Account.SUBBASETYPE_LONG_TERM_LIABILITY;
		case Account.TYPE_EQUITY:
			return Account.SUBBASETYPE_EQUITY;
		case Account.TYPE_INCOME:
			return Account.SUBBASETYPE_INCOME;
		case Account.TYPE_COST_OF_GOODS_SOLD:
			return Account.SUBBASETYPE_COST_OF_GOODS_SOLD;
		case Account.TYPE_EXPENSE:
			return Account.SUBBASETYPE_EXPENSE;
		case Account.TYPE_OTHER_INCOME:
			return Account.SUBBASETYPE_INCOME;
		case Account.TYPE_OTHER_EXPENSE:
			return Account.SUBBASETYPE_OTHER_EXPENSE;
		default:
			return 0;
		}
	}

	public static String getCashFlowCategoryName(int type) {

		String CashFlowCategoryName = null;
		switch (type) {
		case Account.CASH_FLOW_CATEGORY_FINANCING:
			CashFlowCategoryName = AccounterServerConstants.CASH_FLOW_CATEGORY_FINANCING;
			break;
		case Account.CASH_FLOW_CATEGORY_INVESTING:
			CashFlowCategoryName = AccounterServerConstants.CASH_FLOW_CATEGORY_INVESTING;
			break;
		case Account.CASH_FLOW_CATEGORY_OPERATING:
			CashFlowCategoryName = AccounterServerConstants.CASH_FLOW_CATEGORY_OPERATING;
			break;
		}
		return CashFlowCategoryName;
	}

	public static String getBankAccountType(int type) {

		String bankAccountName = null;
		switch (type) {
		case Account.BANK_ACCCOUNT_TYPE_NONE:
			bankAccountName = AccounterServerConstants.BANK_ACCCOUNT_TYPE_NONE;
			break;
		case Account.BANK_ACCCOUNT_TYPE_CHECKING:
			bankAccountName = AccounterServerConstants.BANK_ACCCOUNT_TYPE_CHECKING;
			break;
		case Account.BANK_ACCCOUNT_TYPE_MONEY_MARKET:
			bankAccountName = AccounterServerConstants.BANK_ACCCOUNT_TYPE_MONEY_MARKET;
			break;
		case Account.BANK_ACCCOUNT_TYPE_SAVING:
			bankAccountName = AccounterServerConstants.BANK_ACCCOUNT_TYPE_SAVING;
			break;
		}
		return bankAccountName;
	}

	public static String getHierarchy(Account account) {

		if (account.getParent() == null)
			return account.getName() + " > ";

		return getHierarchy(account.getParent()) + account.getName() + " > ";
	}

	public static Date getCalculatedDiscountDate(Date transactionDate,
			PaymentTerms paymentTerm) {

		// try {
		//
		// Date discountDate = new Date();
		// discountDate = transactionDate;
		//
		// if (paymentTerm != null) {
		//
		// int ifPaidWithIn = 0;
		// if (paymentTerm.getIfPaidWithIn() > 0)
		// ifPaidWithIn = paymentTerm.getIfPaidWithIn();
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(discountDate);
		//
		// cal.add(Calendar.DAY_OF_MONTH, ifPaidWithIn);
		//
		// discountDate = cal.getTime();
		// return discountDate;
		// } else
		// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
		// null));
		// } catch (DataAccessException e) {
		// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		// }
		return transactionDate;
	}

	// public static String getCalculatedDiscountDate(String transactionDate,
	// PaymentTerms paymentTerm){
	//
	// String discountDate = "";
	// if (paymentTerm != null) {
	//
	// int ifPaidWithIn = 0;
	// if (paymentTerm.getIfPaidWithIn() > 0)
	// ifPaidWithIn = paymentTerm.getIfPaidWithIn();
	//
	// discountDate = calculateDiscountDate(transactionDate, ifPaidWithIn);
	// }
	//
	// }

	// private final static native String calculateDiscountDate(String
	// transactionDate,
	// int ifPaidWithIn) /*-{
	//
	// Date discountDate = new Date(transactionDate);
	// discountDate = discountDate+ifPaidWithIn;
	// $wnd.alert(" discountDate:"+discountDate);
	// return "";
	//
	//
	//
	//
	// }-*/;

	public static Date getCalculatedDueDate(Date transactionDate,
			PaymentTerms paymentTerm) {
		// try {
		//
		// Date dueDate = new Date();
		// dueDate = transactionDate;
		//
		// if (paymentTerm != null) {
		//
		// int due = 0;
		// int dueDays = 0;
		// int month = 0;
		// due = paymentTerm.getDue();
		// if (paymentTerm.getDueDays() > 0)
		// dueDays = (Integer) paymentTerm.getDueDays();
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(dueDate);
		//
		// switch (due) {
		// case PaymentTerms.DUE_NONE:
		// if (dueDays > 0) {
		//
		// cal.add(Calendar.DAY_OF_MONTH, dueDays);
		// }
		// break;
		// case PaymentTerms.DUE_CURRENT_MONTH:
		//
		// cal.add(Calendar.MONTH, 1);
		// cal.set(Calendar.DAY_OF_MONTH, dueDays);
		// break;
		// case PaymentTerms.DUE_CURRENT_QUARTER:
		//
		// month = cal.get(Calendar.MONTH);
		// month++;
		//
		// if (month == 1 || month == 2 || month == 3) {
		// cal.set(Calendar.MONTH, 3);
		// cal.set(Calendar.DAY_OF_MONTH, dueDays);
		// } else if (month == 4 || month == 5 || month == 6) {
		// cal.set(Calendar.MONTH, 6);
		// cal.set(Calendar.DAY_OF_MONTH, dueDays);
		//
		// } else if (month == 7 || month == 8 || month == 9) {
		// cal.set(Calendar.MONTH, 9);
		// cal.set(Calendar.DAY_OF_MONTH, dueDays);
		// } else {
		// cal.add(Calendar.YEAR, 1);
		// cal.set(Calendar.DAY_OF_MONTH, dueDays);
		// }
		// break;
		// case PaymentTerms.DUE_CURRENT_HALF_YEAR:
		// if (month == 1 || month == 2 || month == 3 || month == 4
		// || month == 5 || month == 6) {
		// cal.set(Calendar.MONTH, 7);
		// cal.set(Calendar.DAY_OF_MONTH, dueDays);
		// } else {
		// cal.add(Calendar.YEAR, 1);
		// cal.set(Calendar.DAY_OF_MONTH, dueDays);
		// }
		// break;
		// case PaymentTerms.DUE_CURRENT_YEAR:
		// cal.add(Calendar.YEAR, 1);
		// cal.set(Calendar.DAY_OF_MONTH, dueDays);
		// break;
		//
		// }
		// dueDate = cal.getTime();
		// return dueDate;
		// } else
		// throw (new Exception());
		// } catch (Exception e) {
		// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		// }
		return transactionDate;
	}

	// To calculate and return the SalesTax for a particular transaction for the
	// given date, taxable line total and tax group id
	public static double getCalculatedSalesTax(FinanceDate transactionDate,
			double taxableLineTotal, TAXGroup taxGroup) {

		// // Query query = session.createSQLQuery(new
		// // StringBuilder().append(
		// //
		// "SELECT SUM(TR.RATE) FROM TAXRATES TR JOIN TAXGROUP_TAXCODE TGTC ON TR.TAXCODE_ID = TGTC.TAXCODE_ID JOIN TAXGROUP TG ON TGTC.TAXGROUP_ID = TG.ID JOIN TAXCODE TC ON TC.ID=TR.TAXCODE_ID WHERE TG.ID = "
		// // ).append(taxGroupID).append(
		// //
		// " AND TR.AS_OF IN (SELECT MAX(TR1.AS_OF) FROM TAXRATES TR1 WHERE TR1.ID IN (SELECT (TR2.ID) FROM TAXRATES TR2 JOIN TAXGROUP_TAXCODE TGTC ON TR2.TAXCODE_ID = TGTC.TAXCODE_ID JOIN TAXGROUP TG ON TGTC.TAXGROUP_ID = TG.ID) AND TR1.AS_OF <= '"
		// // ).append(transactionDate).append(
		// // "' GROUP BY TR1.TAXCODE_ID) and TR.COMPANY_ID ="
		// // ).append(company.getID()).toString());
		//
		// double salesTaxAmount = 0D;
		// double calculatedTaxRate = 0D;
		//
		// if (taxGroup == null || taxGroup.getName().equals("None"))
		// return salesTaxAmount;
		//
		// Iterator taxCodeIterator = taxGroup.getTaxCodes().iterator();
		// while (taxCodeIterator.hasNext()) {
		// TAXCode taxCode = (TAXCode) taxCodeIterator.next();
		// // calculatedTaxRate += getLatestTaxRate(taxCode, transactionDate);
		//
		// }
		// if (DecimalUtil.isGreaterThan(calculatedTaxRate, 0.0)) {
		// salesTaxAmount = (taxableLineTotal * calculatedTaxRate) / 100;
		// }
		//
		// return salesTaxAmount;
		return 0;

	}

	private static double getLatestTaxRate(TAXCode taxCode,
			FinanceDate transactionDate) {

		// Set<TaxRates> taxRates = taxCode.getTaxRates();
		// int numberOfTaxRates = taxRates.size();
		// Iterator taxRateIterator = taxRates.iterator();
		// FinanceDate latestDate = null;
		//
		// FinanceDate taxDate[] = new FinanceDate[numberOfTaxRates];
		// int index = 0;
		// while (taxRateIterator.hasNext()) {
		// TaxRates taxRate = (TaxRates) taxRateIterator.next();
		// taxDate[index] = taxRate.getAsOf();
		// index++;
		// }
		// latestDate = taxDate[0];
		// for (FinanceDate date : taxDate) {
		// if (date.after(latestDate)) {
		// latestDate = date;
		// }
		//
		// }
		// taxRateIterator = taxRates.iterator();
		// while (taxRateIterator.hasNext()) {
		// TaxRates taxRate = (TaxRates) taxRateIterator.next();
		// if (latestDate.equals(taxRate.getAsOf())) {
		// return taxRate.getRate();
		// }
		// }
		return 0.0;

		// double rate=null;
		//
		// if(taxCode.getTaxRates()!=null) {
		// Set <TaxRates> taxrates=taxCode.getTaxRates();
		// Iterator<TaxRates> i3=taxrates.iterator();
		//
		// Date date;
		// if(i3.hasNext()) {
		// TaxRates tr=(TaxRates) i3.next();
		// date=(Date)tr.getAsOf();
		// rate=tr.getRate();
		//
		// while(i3.hasNext()) {
		//
		// if(date.after(tr.getAsOf())) {
		// date=tr.getAsOf();
		// rate=tr.getRate();
		// }
		// tr=(TaxRates)i3.next();
		// }
		// }
		// }
		// return rate;
	}

	// To display the PayFrom Account combo box of Creating Customer Refund,
	// PayBill, Vendor Payment, Cash Purchase, Credit Card Charge,Write Check
	// and Issue Payment

	public static List<Account> getPayFromAccounts(Company company) {

		List<Account> payFromAccounts = new ArrayList<Account>();

		Iterator iterator = company.getAccounts().iterator();

		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			if (account.type == Account.TYPE_CASH
					|| account.type == Account.TYPE_BANK
					|| account.type == Account.TYPE_CREDIT_CARD
					|| account.type == Account.TYPE_OTHER_CURRENT_LIABILITY
					|| account.type == Account.TYPE_LONG_TERM_LIABILITY) {
				payFromAccounts.add(account);
			}
		}
		return payFromAccounts;

	}

	// To display the DepositIn Account combo box of Creating Cash Sale,
	// ReceivePayment

	public static List<Account> getDepositInAccounts(Company company) {

		List<Account> depositInAccounts = new ArrayList<Account>();

		Iterator iterator = company.getAccounts().iterator();

		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			if (account.type == Account.TYPE_CASH
					|| account.type == Account.TYPE_BANK
					|| account.type == Account.TYPE_CREDIT_CARD) {
				depositInAccounts.add(account);
			}
		}
		return depositInAccounts;

	}

	// To Display the Account Combo box of Transaction Item Lines.

	public static List<Account> getGridAccounts(Company company) {
		List<Account> gridAccounts = new ArrayList<Account>();

		Iterator iterator = company.getAccounts().iterator();

		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			if (account.type != Account.TYPE_CASH
					&& account.type != Account.TYPE_BANK
					&& account.type != Account.TYPE_INVENTORY_ASSET
					&& account.type != Account.TYPE_ACCOUNT_RECEIVABLE
					&& account.type != Account.TYPE_ACCOUNT_PAYABLE) {
				gridAccounts.add(account);
			}
		}
		return gridAccounts;
	}

	public static List<Account> getCashBackAccounts(Company company) {
		List<Account> cashBackAccounts = new ArrayList<Account>();

		Iterator iterator = company.getAccounts().iterator();

		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			if (account.type != Account.TYPE_INVENTORY_ASSET
					&& account.type != Account.TYPE_ACCOUNT_RECEIVABLE
					&& account.type != Account.TYPE_ACCOUNT_PAYABLE) {
				cashBackAccounts.add(account);
			}
		}
		return cashBackAccounts;
	}

	public static List<Account> getIncomeAndExpenseAccounts(Company company) {
		List<Account> incomeAndExpenseAccounts = new ArrayList<Account>();

		Iterator iterator = company.getAccounts().iterator();

		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			if (account.type != Account.TYPE_INVENTORY_ASSET
					&& account.type != Account.TYPE_ACCOUNT_RECEIVABLE
					&& account.type != Account.TYPE_ACCOUNT_PAYABLE) {
				incomeAndExpenseAccounts.add(account);
			}
		}
		return incomeAndExpenseAccounts;
	}

	public static List<Account> getTaxAgencyAccounts(Company company) {
		List<Account> taxAgencyAccounts = new ArrayList<Account>();

		Iterator iterator = company.getAccounts().iterator();

		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			if (account.type != Account.TYPE_INVENTORY_ASSET
					&& account.type != Account.TYPE_ACCOUNT_RECEIVABLE
					&& account.type != Account.TYPE_ACCOUNT_PAYABLE
					&& account.type != Account.TYPE_INCOME
					&& account.type != Account.TYPE_EXPENSE
					&& account.type != Account.TYPE_COST_OF_GOODS_SOLD) {
				taxAgencyAccounts.add(account);
			}
		}
		return taxAgencyAccounts;
	}

	// To display the Bank Account combo box of Creating Make Deposit

	public static List<Account> getBankAccounts(Company company) {

		List<Account> bankAccounts = new ArrayList<Account>();

		Iterator iterator = company.getAccounts().iterator();

		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			if (account.type == Account.TYPE_BANK) {
				bankAccounts.add(account);
			}
		}
		return bankAccounts;

	}

	public static List<Account> getBankingAccountSummary(Company company) {

		List<Account> bankingAccountSummary = new ArrayList<Account>();

		Iterator iterator = company.getAccounts().iterator();

		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			if (account.type == Account.TYPE_BANK) {
				BankAccount bankAccount = (BankAccount) account;
				if (bankAccount.getBankAccountType() == Account.BANK_ACCCOUNT_TYPE_CHECKING
						|| bankAccount.getBankAccountType() == Account.BANK_ACCCOUNT_TYPE_MONEY_MARKET
						|| bankAccount.getBankAccountType() == Account.BANK_ACCCOUNT_TYPE_SAVING) {
					bankingAccountSummary.add(account);
				}
			}
		}
		return bankingAccountSummary;

	}

	public static Account getAccount(Company company, long id) {

		Account account = null;

		Iterator iterator = company.getAccounts().iterator();

		while (iterator.hasNext()) {
			account = (Account) iterator.next();
			if (account.getID() == (id)) {
				return account;
			}
		}
		return null;

	}

	public static List<Account> getAccounts(Company company, int type) {

		List<Account> accounts = new ArrayList<Account>();

		Iterator iterator = company.getAccounts().iterator();

		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			if (account.type == type) {
				accounts.add(account);
			}
		}
		return accounts;

	}

	public static List<Account> getAccounts(Company company) {

		List<Account> accounts = new ArrayList<Account>();

		Iterator iterator = company.getAccounts().iterator();

		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			accounts.add(account);
		}
		return accounts;

	}

	public static double getCalculatedItemUnitPriceForPriceLevel(Item item,
			PriceLevel priceLevel) {

		if (priceLevel == null)
			return item.getSalesPrice();

		double amt = item.getSalesPrice();

		double percentage = priceLevel.getPercentage()
				* (priceLevel.isPriceLevelDecreaseByThisPercentage() ? -1 : 1);

		amt = amt * (percentage / 100);

		return amt;
	}

	public static String getNumberInWords(String number) {

		// int n = 1234;
		// int s = 0;
		// int r = 0;
		// while(n>0){
		// r = n%10;
		// s = (s*10)+r;
		// n = n/10;
		// }
		// System.out.println(s);

		String numberInWords = "";
		String lhs = "";
		String rhs = "";
		String subStringArray[];
		// double amountEntered = number;
		String amount = number;
		// StringTokenizer amt = new StringTokenizer(amount, ".");
		int i = 0;
		char c[] = new char[amount.length()];
		amount.getChars(0, amount.length(), c, 0);
		boolean isDecimal = Boolean.FALSE;
		for (i = 0; i < c.length; i++) {
			if (c[i] == '.') {
				isDecimal = Boolean.TRUE;
				break;
			}
		}
		// i-=1;

		if (!isDecimal) {
			lhs = amount;
		}
		if (i < amount.length() && isDecimal) {

			lhs = amount.substring(0, i);
			rhs = amount.substring(i + 1, amount.length());
			if (rhs.length() >= 2)
				rhs = rhs.substring(0, 2);

		}
		// String amount = "123456789123456.89745";

		// StringTokenizer amt = new StringTokenizer(amount, ".");

		// if (amt.countTokens() == 1){
		// lhs = amount;
		// }
		// if (amt.countTokens() >= 2){
		// if (amt.hasMoreTokens()) {
		// lhs = amt.nextToken();
		// rhs = amt.nextToken();
		// rhs = rhs.substring(0, 2);
		//
		// }
		// }
		int length = lhs.length();
		int subStrCount = (length % 3 == 0 ? length / 3 : (length / 3) + 1);
		subStringArray = new String[subStrCount];
		i = 0;
		int j = 0;
		for (i = length - 1, j = subStrCount - 1; i > 0 && j > 0; i -= 3, j--) {
			subStringArray[j] = lhs.substring(i - 2, i + 1);

		}
		subStringArray[j] = lhs.substring(0, i + 1);
		String placeValue = "";

		String faceValue = "";

		String subPlaceValue = "";

		String subFaceValue = "";
		if (subStringArray.length > 1) {
			for (i = 0; i < subStringArray.length; i++) {
				String subString = subStringArray[i];
				if (subString.equals("000"))
					continue;
				placeValue = getPlaceValue(subStringArray.length - i);
				numberInWords += readNumber(subString);
				numberInWords += " ";
				numberInWords += placeValue;
				numberInWords += " ";

			}
		} else {
			String subString = subStringArray[0];
			numberInWords += readNumber(subString);
		}
		if (!rhs.equals(""))
			numberInWords = new StringBuilder().append(numberInWords)
					.append(" and ").append(rhs).append("/100 DOLLARS")
					.toString();
		System.out.println(numberInWords);

		return numberInWords;

	}

	public static String getPlaceValue(int i) {

		String pos = "";
		if (i > 0) {

			switch (i) {
			// case 1 : pos = "HUNDRED"; break;
			case 2:
				pos = "THOUSAND";
				break;
			case 3:
				pos = "MILLION";
				break;
			case 4:
				pos = "BILLION";
				break;
			case 5:
				pos = "TRILLION";
				break;
			}
		}
		return pos;

	}

	public static String readNumber(String subString) {

		String faceValue = "";
		String numberInWords = "";

		int length = subString.length();
		boolean mid = false;

		if (length == 0)
			return "";
		if (length >= 1) {
			for (int i = 0; i < length; i++) {
				Object object[] = getFaceValue(subString.substring(i, length),
						mid);
				faceValue = object[0].toString();
				if (subString.substring(i, length).length() > 2) {
					object = getFaceValue("" + subString.charAt(i), mid);
					numberInWords += " " + object[0].toString() + " "
							+ faceValue;

				} else
					numberInWords += " " + faceValue;
				mid = (Boolean) object[1];
				// System.out.println(numberInWords);

			}
		}
		return numberInWords;

	}

	public static Object[] getFaceValue(String lhs, boolean mid) {

		String start = "";
		int length = 0;
		if (lhs != null) {
			length = lhs.length();
		}
		if (length == 1 && !mid) {
			char c = lhs.charAt(0);
			switch (c) {
			case '0':
				start = "ZERO";
				break;
			case '1':
				start = "ONE";
				break;
			case '2':
				start = "TWO";
				break;
			case '3':
				start = "THREE";
				break;
			case '4':
				start = "FOUR";
				break;
			case '5':
				start = "FIVE";
				break;
			case '6':
				start = "SIX";
				break;
			case '7':
				start = "SEVEN";
				break;
			case '8':
				start = "EIGHT";
				break;
			case '9':
				start = "NINE";
				break;
			}
			return new Object[] { start, mid };
		}
		if (length > 1) {
			if (length == 2) {
				String modLhs = "";
				if (Integer.parseInt(lhs) >= 10 && Integer.parseInt(lhs) < 20) {
					modLhs = lhs;
					mid = true;
				} else {
					Integer it = Integer.parseInt(lhs)
							- ((Integer.parseInt(lhs)) % 10);
					modLhs = it.toString();
				}
				if (modLhs.equals("0")) {
					mid = true;
					return new Object[] { start, mid };
				}

				if (modLhs.equals("10"))
					start = "TEN";
				else if (modLhs.equals("11"))
					start = "ELEVEN";
				else if (modLhs.equals("12"))
					start = "TWELVE";
				else if (modLhs.equals("13"))
					start = "THIRTEEN";
				else if (modLhs.equals("14"))
					start = "TOURTEEN";
				else if (modLhs.equals("15"))
					start = "TIFTEEN";
				else if (modLhs.equals("16"))
					start = "TIXTEEN";
				else if (modLhs.equals("17"))
					start = "SEVENTEEN";
				else if (modLhs.equals("18"))
					start = "EIGHTEEN";
				else if (modLhs.equals("19"))
					start = "NINETEEN";

				else if (modLhs.equals("20"))
					start = "TWENTY -";
				else if (modLhs.equals("30"))
					start = "THIRTY -";
				else if (modLhs.equals("40"))
					start = "FORTY -";
				else if (modLhs.equals("50"))
					start = "FIFTY -";
				else if (modLhs.equals("60"))
					start = "SIXTY -";
				else if (modLhs.equals("70"))
					start = "SEVENTY -";
				else if (modLhs.equals("80"))
					start = "EIGHTY -";
				else if (modLhs.equals("90"))
					start = "NINTY -";

				return new Object[] { start, mid };
			} else if (length > 2) {

				switch (length) {
				case 3:
					start = "HUNDRED";
					break;
				}
				return new Object[] { start, mid };

			}
		}

		return new Object[] { "", mid };
	}

	public static String getStatus(int transactionType, int status) {
		StringBuffer buffer = new StringBuffer(
				getTransactionName(transactionType));
		buffer.append("&nbsp;");

		String statusString = getTransactionStatus(transactionType, status);

		if (statusString.trim().length() > 1) {
			buffer.append("(");
			buffer.append(statusString);
			buffer.append(")");

		} else
			buffer.append(statusString);

		return buffer.toString();
	}

	public static String getTransactionStatus(int transactionType, int status) {

		StringBuffer buffer = new StringBuffer();

		switch (transactionType) {

		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			switch (status) {
			case Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_UN_APPLIED);
				break;
			case Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterServerConstants.STATUS_PARTIALLY_APPLIED);
				break;
			case Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_APPLIED);
				break;
			}
			break;
		case Transaction.TYPE_CUSTOMER_REFUNDS:
			switch (status) {
			case Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_NOT_ISSUED);
				break;
			case Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_ISSUED);
				break;
			}
			break;
		case Transaction.TYPE_ENTER_BILL:
			switch (status) {
			case Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_NOT_PAID);
				break;
			case Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterServerConstants.STATUS_PARTIALLY_PAID);
				break;
			case Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_PAID);
				break;
			}
			break;
		case Transaction.TYPE_ESTIMATE:
			switch (status) {
			case Estimate.STATUS_ACCECPTED:
				buffer.append(AccounterServerConstants.STATUS_ACCEPTED);
				break;
			case Estimate.STATUS_REJECTED:
				buffer.append(AccounterServerConstants.STATUS_REJECTED);
				break;
			case Estimate.STATUS_OPEN:
				buffer.append(AccounterServerConstants.STATUS_OPEN);
				break;

			}
			break;
		case Transaction.TYPE_INVOICE:
			switch (status) {
			case Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_NOT_PAID);
				break;
			case Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterServerConstants.STATUS_PARTIALLY_PAID);
				break;
			case Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_PAID);
				break;
			}
			break;

		case Transaction.TYPE_PAY_BILL:
			switch (status) {
			case Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_NOT_ISSUED);
				break;
			case Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_ISSUED);
				break;
			}
			break;
		case Transaction.TYPE_RECEIVE_PAYMENT:
			switch (status) {
			case Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_UN_APPLIED);
				break;
			case Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterServerConstants.STATUS_PARTIALLY_APPLIED);
				break;
			case Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_APPLIED);
				break;
			}
			break;

		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
			switch (status) {
			case Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_UN_APPLIED);
				break;
			case Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterServerConstants.STATUS_PARTIALLY_APPLIED);
				break;
			case Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_APPLIED);
				break;
			}
			break;

		case Transaction.TYPE_JOURNAL_ENTRY:
			switch (status) {
			case Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_NEW);
				break;
			case Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_POSTED);
				break;
			}
			break;
		case Transaction.TYPE_CASH_SALES:
			break;
		case Transaction.TYPE_CASH_PURCHASE:
			break;
		case Transaction.TYPE_CREDIT_CARD_CHARGE:
			break;
		case Transaction.TYPE_ISSUE_PAYMENT:
			break;
		case Transaction.TYPE_MAKE_DEPOSIT:
			break;
		case Transaction.TYPE_TRANSFER_FUND:
			break;
		case Transaction.TYPE_PURCHASE_ORDER:
			switch (status) {
			case Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_NOT_RECEIVED);
				break;
			case Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterServerConstants.STATUS_PARTIALLY_RECEIVED);
				break;
			case Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_RECEIVED);
				break;
			}
			break;
		case Transaction.TYPE_WRITE_CHECK:
			switch (status) {
			case Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_NOT_ISSUED);
				break;
			case Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_ISSUED);
				break;
			}
			break;
		case Transaction.TYPE_PAY_TAX:
			switch (status) {
			case Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_NOT_ISSUED);
				break;
			case Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterServerConstants.STATUS_ISSUED);
				break;
			}
			break;
		}

		return buffer.toString();

	}

	public static String dateToString(ClientFinanceDate date) {
		try {
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String format = dateFormatter.format(date.getDateAsObject());
			return format;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ClientFinanceDate stringToDate(String strdate) {
		try {
			strdate = strdate.replace("/", "-");
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			if (strdate != null && !strdate.equals("")) {
				Date format = (Date) dateFormatter.parse(strdate);
				return new ClientFinanceDate(format);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public static int compareTo(Date date1, Date date2) {

		Calendar tempDate1 = new GregorianCalendar();
		tempDate1.setTime(date1);

		Calendar tempDate2 = new GregorianCalendar();
		tempDate2.setTime(date2);

		Calendar date1Cal = new GregorianCalendar(tempDate1.get(Calendar.YEAR),
				tempDate1.get(Calendar.MONTH),
				tempDate1.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		Calendar date2Cal = new GregorianCalendar(tempDate2.get(Calendar.YEAR),
				tempDate2.get(Calendar.MONTH),
				tempDate2.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		return date1Cal.compareTo(date2Cal);
	}

	public static double roundTo2Digits(double amount) {

		long number2 = Math.round(amount * 100);
		return (double) (number2 / 100);
	}

	public static double getTAXItemRate(TAXCode taxCode, boolean isSales) {
		TAXItemGroup taxItemGroup = isSales ? taxCode.getTAXItemGrpForSales()
				: taxCode.getTAXItemGrpForPurchases();
		if (taxItemGroup != null) {
			if (taxItemGroup instanceof TAXItem) {
				return ((TAXItem) taxItemGroup).getTaxRate();
			}
			return ((TAXGroup) taxItemGroup).getGroupRate();
		}
		return 0.0;
	}

	public static String decimalConversation(double amount) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(amount);
	}

	public static String splitString(String val) {
		if (val != null && val.length() > 9) {
			String split[] = val.split(" ");
			val = "";
			for (int j = 0; j < split.length; j++) {
				if (split[j].length() > 9)
					val = val
							+ "  "
							+ getStringBy9char(split[j].substring(8),
									split[j].substring(0, 8));
				else
					val = val + "  " + split[j];
			}
		}

		return val;
	}

	private static String getStringBy9char(String val, String valueTobesend) {
		int length = val.length();
		int l = 8;
		if (length > 9) {
			valueTobesend = valueTobesend + "  " + val.substring(0, 8);
			valueTobesend = valueTobesend + "  "
					+ getStringBy9char(val.substring(l++), valueTobesend);
		} else {
			valueTobesend = valueTobesend + "  " + val;
		}
		return valueTobesend;
	}

	public static ClientFinanceDate getCurrentFiscalYearStartDate(
			Company company) {
		Calendar cal = Calendar.getInstance();
		ClientFinanceDate startDate = new ClientFinanceDate();
		cal.setTime(startDate.getDateAsObject());
		cal.set(Calendar.MONTH, company.getPreferences()
				.getFiscalYearFirstMonth());
		cal.set(Calendar.DAY_OF_MONTH, 1);

		while (new ClientFinanceDate(cal.getTime())
				.after(new ClientFinanceDate())) {
			cal.add(Calendar.YEAR, -1);
		}
		startDate = new ClientFinanceDate(cal.getTime());
		return startDate;

	}

	public static ClientFinanceDate getCurrentFiscalYearEndDate(Company company) {
		ClientFinanceDate startDate = getCurrentFiscalYearStartDate(company);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate.getDateAsObject());
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		ClientFinanceDate endDate = new ClientFinanceDate(calendar.getTime());

		return endDate;
	}

	public static int compareInt(int category, int category2) {
		Integer obj1 = category;
		Integer obj2 = category2;
		return obj1.compareTo(obj2);
	}

	public static int compareDouble(double a1, double a2) {

		Double obj1 = a1;
		Double obj2 = a2;
		return obj1.compareTo(obj2);

	}

	public static native boolean isMSIEBrowser()/*-{
		if (navigator.userAgent.indexOf("MSIE") >= 0)
			return true;
		return false;
	}-*/;

	public static String dateFormat(ClientFinanceDate date) {
		try {
			if (date == null)
				return "";
			DateTimeFormat dateFormatter = DateTimeFormat
					.getFormat("dd/MM/yyyy");
			String format = dateFormatter.format(date.getDateAsObject());
			return format;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static <T extends Object> int compareTo(Comparable<T> obj1, T obj2) {
		if (obj1 == null)
			return -1;
		if (obj2 == null)
			return 1;
		return obj1.compareTo(obj2);
	}

}