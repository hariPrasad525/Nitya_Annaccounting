package com.nitya.accounter.web.client.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.Calendar;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.InvalidEntryException;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;
import com.nitya.accounter.web.client.util.ChangeType;
import com.nitya.accounter.web.client.util.CoreEvent;

public class Utility implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static AccounterMessages messages = Global.get().messages();

	int version;
	static boolean isDelete;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public static String getTransactionName(int transactionType) {

		String transactionName = null;
		switch (transactionType) {
		case 0:
			transactionName = messages.openingBalance();
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			transactionName = messages.cashSale();
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			transactionName = messages.cashPurchase();
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			transactionName = messages.creditCardCharge();
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			transactionName = messages.customerCredit();
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			transactionName = messages.customerRefund(Global.get().Customer());
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			transactionName = messages.vendorBill(Global.get().Vendor());
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			transactionName = messages.quote();
			break;
		case ClientTransaction.TYPE_INVOICE:
			transactionName = messages.invoice();
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			transactionName = messages.issuePayment();
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			transactionName = messages.makeDeposit();
			break;
		case ClientTransaction.TYPE_PAY_BILL:
			transactionName = messages.payeePayment(Global.get().Vendor());
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			transactionName = messages.payeePrePayment(Global.get().Vendor());
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			transactionName = messages.customerPayment();
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			transactionName = messages.transferFund();
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			transactionName = messages.payeeCredit(Global.get().Vendor());
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			transactionName = messages.writeCheck();
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			transactionName = messages.journalEntry();
			break;
		case ClientTransaction.TYPE_PAY_TAX:
			transactionName = messages.payTax();
			break;
		case ClientTransaction.TYPE_RECEIVE_TAX:
			transactionName = messages.receiveTAX();
			break;
		case ClientTransaction.TYPE_PURCHASE_ORDER:
			transactionName = messages.purchaseOrder();
			break;
		case ClientTransaction.TYPE_ITEM_RECEIPT:
			transactionName = messages.itemReceipt();
			break;
		case ClientTransaction.TYPE_CASH_EXPENSE:
			transactionName = messages.cashExpense();
			break;
		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			transactionName = messages.employeeExpense();
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			transactionName = messages.creditCardExpense();
			break;
		case ClientTransaction.TYPE_TAX_RETURN:
			transactionName = messages.vatReturn();
			break;
		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			transactionName = messages.customerprePayment();
			break;
		case ClientTransaction.TYPE_ADJUST_SALES_TAX:
		case ClientTransaction.TYPE_ADJUST_VAT_RETURN:
			// transactionName = messages.taxAdjustment();
			transactionName = messages.taxAdjustment();
			break;
		case ClientTransaction.TYPE_STOCK_ADJUSTMENT:
			transactionName = messages.stockAdjustment();
			break;
		case ClientTransaction.TYPE_TDS_CHALLAN:
			transactionName = messages.tdsChallan();
			break;
		case ClientTransaction.TYPE_BUILD_ASSEMBLY:
			transactionName = messages.buildAssembly();
			break;
		case ClientTransaction.TYPE_PAY_RUN:
			transactionName = messages.payrun();
			break;
		case ClientTransaction.TYPE_PAY_EMPLOYEE:
			transactionName = messages.payEmployee();
			break;
		}
		return transactionName;
	}

	public static boolean isUserHavePermissions(int transactionType) {
		ClientUser user = Accounter.getUser();
		if (user.isAdmin()) {
			return true;
		}
		ClientUserPermissions permissions = user.getPermissions();

		boolean isAllowed = false;
		switch (transactionType) {
		case ClientTransaction.TYPE_PAY_TAX:
		case ClientTransaction.TYPE_ADJUST_VAT_RETURN:
		case ClientTransaction.TYPE_TDS_CHALLAN:
		case IAccounterCore.TAXGROUP:
		case IAccounterCore.TDSCHALANDETAIL:
		case IAccounterCore.TAXITEM:
		case IAccounterCore.TAXAGENCY:
		case IAccounterCore.TAXCODE:
		case IAccounterCore.FIXED_ASSET:
		case IAccounterCore.BUDGET:
		case IAccounterCore.TDSDEDUCTORMASTER:
		case IAccounterCore.TDSRESPONSIBLEPERSON:
		case ClientTransaction.TYPE_PAY_RUN:
		case ClientTransaction.TYPE_PAY_EMPLOYEE:
			isAllowed = user.getUserRole().equals(
					RolePermissions.FINANCIAL_ADVISER);
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			isAllowed = permissions.getTypeOfBankReconcilation() == RolePermissions.TYPE_YES
					|| permissions.getTypeOfManageAccounts() == RolePermissions.TYPE_YES;
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
		case ClientTransaction.TYPE_PAY_BILL:
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			isAllowed = (permissions.getTypeOfPayBillsPayments() == RolePermissions.TYPE_YES || permissions
					.getInvoicesAndPayments() == RolePermissions.TYPE_YES);
			break;
		case ClientTransaction.TYPE_INVOICE:
		case ClientTransaction.TYPE_ESTIMATE:
		case ClientTransaction.TYPE_ENTER_BILL:
		case ClientTransaction.TYPE_PURCHASE_ORDER:
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
		case ClientTransaction.TYPE_ITEM_RECEIPT:
		case IAccounterCore.VENDOR:
		case IAccounterCore.CUSTOMER_GROUP:
		case IAccounterCore.VENDOR_GROUP:
		case IAccounterCore.CUSTOMER:
		case IAccounterCore.JOB:
		case IAccounterCore.RECURING_TRANSACTION:
		case ClientTransaction.TYPE_CASH_EXPENSE:
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
		case ClientTransaction.TYPE_CASH_PURCHASE:
		case ClientTransaction.TYPE_CASH_SALES:
			isAllowed = (permissions.getTypeOfInvoicesBills() == RolePermissions.TYPE_YES || permissions
					.getInvoicesAndPayments() == RolePermissions.TYPE_YES);
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			isAllowed = permissions.getTypeOfManageAccounts() == RolePermissions.TYPE_YES;
			break;
		case IAccounterCore.ACCOUNT:
			isAllowed = permissions.getTypeOfManageAccounts() == RolePermissions.TYPE_YES;
			break;
		case IAccounterCore.BANK_ACCOUNT:
		case ClientTransaction.TYPE_WRITE_CHECK:
		case IAccounterCore.BANK:
			isAllowed = permissions.getTypeOfBankReconcilation() == RolePermissions.TYPE_YES;
			break;
		case IAccounterCore.ITEM:
		case IAccounterCore.ITEM_GROUP:
		case IAccounterCore.INVOICE_FREQUENCY:
		case IAccounterCore.MEASUREMENT:
			isAllowed = permissions.getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES
					|| permissions.getTypeOfInvoicesBills() == RolePermissions.TYPE_YES
					|| permissions.getInvoicesAndPayments() == RolePermissions.TYPE_YES;
			break;
		case IAccounterCore.STOCK_ADJUSTMENT:
		case IAccounterCore.WAREHOUSE:
		case IAccounterCore.STOCK_TRANSFER:
		case ClientTransaction.TYPE_BUILD_ASSEMBLY:
			isAllowed = permissions.getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES;
			break;
		case IAccounterCore.USER:
			isAllowed = user.isCanDoUserManagement();
			break;
		case IAccounterCore.BRANDING_THEME:
		case IAccounterCore.PAYMENT_TERMS:
		case IAccounterCore.SHIPPING_METHOD:
		case IAccounterCore.SHIPPING_TERMS:
		case IAccounterCore.CREDIT_RATING:
		case IAccounterCore.CURRENCY:
		case IAccounterCore.LOCATION:
		case IAccounterCore.ACCOUNTER_CLASS:
		case IAccounterCore.SALES_PERSON:
			isAllowed = permissions.getTypeOfCompanySettingsLockDates() == RolePermissions.TYPE_YES;
			break;
		case IAccounterCore.RECONCILIATION:
			isAllowed = permissions.getTypeOfManageAccounts() == RolePermissions.TYPE_YES
					|| user.getUserRole().equals(
							RolePermissions.FINANCIAL_ADVISER);
			break;
		}
		return isAllowed;
	}

	public static String getItemType(int itemType) {
		String itemTypeName = null;
		switch (itemType) {
		case ClientTransactionItem.TYPE_ITEM:
			itemTypeName = messages.productOrServiceItem();
			break;
		case ClientTransactionItem.TYPE_ACCOUNT:
			itemTypeName = messages.Account();
			break;
		default:
			break;
		}
		return itemTypeName;
	}

	public static String getAccountTypeString(int accountType) {

		String accountTypeName = null;
		switch (accountType) {
		case ClientAccount.TYPE_INCOME:
			accountTypeName = messages.income();
			break;
		case ClientAccount.TYPE_OTHER_INCOME:
			accountTypeName = messages.otherIncome();
			break;
		case ClientAccount.TYPE_EXPENSE:
			accountTypeName = messages.expense();
			break;
		case ClientAccount.TYPE_OTHER_EXPENSE:
			accountTypeName = messages.otherExpense();
			break;
		case ClientAccount.TYPE_COST_OF_GOODS_SOLD:
			accountTypeName = messages.costofGoodsSold();
			break;
		case ClientAccount.TYPE_CASH:
			accountTypeName = messages.cash();
			break;
		case ClientAccount.TYPE_BANK:
			accountTypeName = messages.bank();
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
			accountTypeName = messages.otherCurrentAsset();
			break;
		case ClientAccount.TYPE_INVENTORY_ASSET:
			accountTypeName = messages.inventoryAsset();
			break;
		case ClientAccount.TYPE_OTHER_ASSET:
			accountTypeName = messages.otherAssets();
			break;
		case ClientAccount.TYPE_FIXED_ASSET:
			accountTypeName = messages.fixedAsset();
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			accountTypeName = messages.creditCard();
			break;
		case ClientAccount.TYPE_PAYPAL:
			accountTypeName = messages.paypal();
			break;
		case ClientAccount.TYPE_PAYROLL_LIABILITY:
			accountTypeName = messages.payrollLiability();
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_LIABILITY:
			accountTypeName = messages.currentLiability();
			break;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
			accountTypeName = messages.longTermLiability();
			break;
		case ClientAccount.TYPE_EQUITY:
			accountTypeName = messages.equity();
			break;
		case ClientAccount.TYPE_ACCOUNT_RECEIVABLE:
			accountTypeName = messages.accountsReceivable();
			break;
		case ClientAccount.TYPE_ACCOUNT_PAYABLE:
			accountTypeName = messages.accountsPayable();
			break;

		}
		return accountTypeName;
	}

	public static int getAccountType(String accountTypeName) {
		if (accountTypeName.equals(messages.income())) {
			return ClientAccount.TYPE_INCOME;
		} else if (accountTypeName.equals(messages.otherIncome())) {
			return ClientAccount.TYPE_OTHER_INCOME;
		} else if (accountTypeName.equals(messages.expense())) {
			return ClientAccount.TYPE_EXPENSE;
		} else if (accountTypeName.equals(messages.otherExpense())) {
			return ClientAccount.TYPE_OTHER_EXPENSE;
		} else if (accountTypeName.equals(messages.costofGoodsSold())) {
			return ClientAccount.TYPE_COST_OF_GOODS_SOLD;
		} else if (accountTypeName.equals(messages.cash())) {
			return ClientAccount.TYPE_CASH;
		} else if (accountTypeName.equals(messages.bank())) {
			return ClientAccount.TYPE_BANK;
		} else if (accountTypeName.equals(messages.otherCurrentAsset())) {
			return ClientAccount.TYPE_OTHER_CURRENT_ASSET;
		} else if (accountTypeName.equals(messages.inventoryAsset())) {
			return ClientAccount.TYPE_INVENTORY_ASSET;
		} else if (accountTypeName.equals(messages.otherAssets())) {
			return ClientAccount.TYPE_OTHER_ASSET;
		} else if (accountTypeName.equals(messages.fixedAsset())) {
			return ClientAccount.TYPE_FIXED_ASSET;
		} else if (accountTypeName.equals(messages.creditCard())) {
			return ClientAccount.TYPE_CREDIT_CARD;
		} else if (accountTypeName.equals(messages.paypal())) {
			return ClientAccount.TYPE_PAYPAL;
		} else if (accountTypeName.equals(messages.payrollLiability())) {
			return ClientAccount.TYPE_PAYROLL_LIABILITY;
		} else if (accountTypeName.equals(messages.currentLiability())) {
			return ClientAccount.TYPE_OTHER_CURRENT_LIABILITY;
		} else if (accountTypeName.equals(messages.longTermLiability())) {
			return ClientAccount.TYPE_LONG_TERM_LIABILITY;
		} else if (accountTypeName.equals(messages.equity())) {
			return ClientAccount.TYPE_EQUITY;
		} else if (accountTypeName.equals(messages.accountsReceivable())) {
			return ClientAccount.TYPE_ACCOUNT_RECEIVABLE;
		} else if (accountTypeName.equals(messages.accountsPayable())) {
			return ClientAccount.TYPE_ACCOUNT_PAYABLE;
		}
		return 0;
	}

	public static String getCashFlowCategoryName(int type) {
		String CashFlowCategoryName = null;
		switch (type) {
		case ClientAccount.CASH_FLOW_CATEGORY_FINANCING:
			CashFlowCategoryName = messages.financing();
			break;
		case ClientAccount.CASH_FLOW_CATEGORY_INVESTING:
			CashFlowCategoryName = messages.investing();
			break;
		case ClientAccount.CASH_FLOW_CATEGORY_OPERATING:
			CashFlowCategoryName = messages.operating();
			break;
		}
		return CashFlowCategoryName;
	}

	public static String getBankAccountType(int type) {
		String bankAccountName = null;
		switch (type) {
		case ClientAccount.BANK_ACCCOUNT_TYPE_NONE:
			bankAccountName = messages.none();
			break;
		case ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING:
			bankAccountName = messages.checking();
			break;
		case ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET:
			bankAccountName = messages.moneyMarket();
			break;
		case ClientAccount.BANK_ACCCOUNT_TYPE_SAVING:
			bankAccountName = messages.saving();
			break;
		}
		return bankAccountName;
	}

	public static String getHierarchy(ClientAccount account) {

		if (account == null)
			return "";

		return getHierarchy(Accounter.getCompany().getAccount(
				account.getParent()))
				+ account.getName() + " > ";
	}

	public static ClientFinanceDate getCalculatedDiscountDate(
			ClientFinanceDate transactionDate, ClientPaymentTerms paymentTerm) {

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

	public static ClientFinanceDate getPaymentTermsDate(
			ClientPaymentTerms paymentTerm) {
		ClientFinanceDate dueDate = new ClientFinanceDate(new Date());

		int dueDays = paymentTerm.getDueDays();
		int day = dueDate.getDay();
		int month = dueDate.getMonth();
		int year = dueDate.getYear();

		int month_days;
		if (dueDays == 0) {
			return new ClientFinanceDate(new Date());
		} else {

			if (month == 1 || month == 3 || month == 5 || month == 7
					|| month == 8 || month == 10 || month == 12) {
				month_days = 31;
				day += dueDays;
				if (day > month_days) {
					do {

						day = day - month_days;
						month += 1;
						month_days = getNumberOfDays(month, year);
						if (month > 12) {
							year += 1;
							month = 1;
						}
					} while (day > month_days);

				}
			} else if (month == 4 || month == 6 || month == 9 || month == 11) {
				month_days = 30;
				day += dueDays;
				if (day > month_days) {
					do {
						day = day - month_days;
						month += 1;
						month_days = getNumberOfDays(month, year);
						if (month > 12) {
							year += 1;
							month = 1;
						}
					} while (day > month_days);

				}
			} else if (month == 2) {
				int no_of_days;
				if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)) {
					month_days = 29;

				} else {
					month_days = 28;
				}
				day += dueDays;
				if (day > month_days) {
					do {

						day = day - month_days;
						month += 1;
						month_days = getNumberOfDays(month, year);
						if (month > 12) {
							year += 1;
							month = 1;
						}
					} while (day > month_days);

				}
			}
			System.err.println("mon" + month + "day" + day + "year" + year);

			dueDate = new ClientFinanceDate();
			dueDate.setDay(day - 1);
			dueDate.setMonth(month);
			dueDate.setYear(year);
			return dueDate;
		}

	}

	private static int getNumberOfDays(int month, int year) {

		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			return 31;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		} else if (month == 2
				&& (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))) {
			return 29;

		} else {
			return 28;
		}

	}

	public static ClientFinanceDate getCalculatedDueDate(
			ClientFinanceDate transactionDate, ClientPaymentTerms paymentTerm) {
		if (transactionDate.getDate() == 0) {
			return new ClientFinanceDate();
		}

		try {

			ClientFinanceDate dueDate = new ClientFinanceDate();
			dueDate = transactionDate;

			if (paymentTerm != null) {

				int due = 0;
				int dueDays = 0;
				int month = 0;
				due = paymentTerm.getDue();
				if (paymentTerm.getDueDays() > 0)
					dueDays = paymentTerm.getDueDays();
				Calendar cal = Calendar.getInstance();
				cal.setTime(dueDate.getDateAsObject());

				cal.add(Calendar.DAY_OF_MONTH, dueDays);
				// switch (due) {
				// case ClientPaymentTerms.DUE_NONE:
				// if (dueDays > 0) {
				//
				// cal.add(Calendar.DAY_OF_MONTH, dueDays);
				// }
				// break;
				// case ClientPaymentTerms.DUE_CURRENT_MONTH:
				// cal.set(Calendar.DAY_OF_MONTH, 01);
				// cal.add(Calendar.MONTH, 1);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				// break;
				// case ClientPaymentTerms.DUE_CURRENT_QUARTER:
				// cal.set(Calendar.DAY_OF_MONTH, 01);
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
				// case ClientPaymentTerms.DUE_CURRENT_HALF_YEAR:
				// cal.set(Calendar.DAY_OF_MONTH, 01);
				// if (month == 1 || month == 2 || month == 3 || month == 4
				// || month == 5 || month == 6) {
				// cal.set(Calendar.MONTH, 7);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				// } else {
				// cal.add(Calendar.YEAR, 1);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				// }
				// break;
				// case ClientPaymentTerms.DUE_CURRENT_YEAR:
				// cal.set(Calendar.DAY_OF_MONTH, 01);
				// cal.add(Calendar.YEAR, 1);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				// break;
				//
				// }
				dueDate = new ClientFinanceDate(cal.getTime());
				return dueDate;
			} else
				throw (new Exception());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDate;
	}

	public static double getCalculatedSalesTax(
			ClientFinanceDate transactionDate, double taxableLineTotal,
			ClientTAXItemGroup taxItemGroup) {

		Double salesTaxAmount = 0D;
		Double calculatedTaxRate = 0D;

		if (taxItemGroup == null || taxItemGroup.equals(messages.none())) {
			return salesTaxAmount;
		}

		if (taxItemGroup instanceof ClientTAXItem)
			calculatedTaxRate += ((ClientTAXItem) taxItemGroup).getTaxRate();
		if (taxItemGroup instanceof ClientTAXGroup)
			calculatedTaxRate += ((ClientTAXGroup) taxItemGroup).getGroupRate();

		if (DecimalUtil.isGreaterThan(calculatedTaxRate, 0.0)) {
			salesTaxAmount = (taxableLineTotal * calculatedTaxRate) / 100;
		}

		return salesTaxAmount;
	}

	// @SuppressWarnings( { "unchecked", "unused" })
	// private static Double getLatestTaxRate(ClientTaxCode taxCode,
	// ClientFinanceDate transactionDate) {
	//
	// Set<ClientTaxRates> taxRates = taxCode.getTaxRates();
	// int numberOfTaxRates = taxRates.size();
	// Iterator taxRateIterator = taxRates.iterator();
	// ClientFinanceDate latestDate = null;
	//
	// ClientFinanceDate taxDate[] = new ClientFinanceDate[numberOfTaxRates];
	// int index = 0;
	// while (taxRateIterator.hasNext()) {
	// ClientTaxRates taxRate = (ClientTaxRates) taxRateIterator.next();
	// taxDate[index] = new ClientFinanceDate(taxRate.getAsOf());
	// index++;
	// }
	// latestDate = taxDate[0];
	// for (ClientFinanceDate date : taxDate) {
	// if (date.after(latestDate)) {
	// latestDate = date;
	// }
	//
	// }
	// taxRateIterator = taxRates.iterator();
	// while (taxRateIterator.hasNext()) {
	// ClientTaxRates taxRate = (ClientTaxRates) taxRateIterator.next();
	// if (taxRate == null)
	// continue;
	//
	// ClientFinanceDate getAsOf = new ClientFinanceDate(taxRate.getAsOf());
	//
	// if (getAsOf == null)
	// continue;
	//
	// if (latestDate.equals(getAsOf)) {
	// return taxRate.getRate();
	// }
	// }
	// return null;

	// Double rate=null;
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
	// }

	// To display the PayFrom Account combo box of Creating Customer Refund,
	// PayBill, Vendor Payment, Cash Purchase, Credit Card Charge,Write Check
	// and Issue Payment

	// public static List<ClientAccount> getPayFromAccounts(ClientCompany
	// company) {
	//
	// List<ClientAccount> payFromAccounts = new ArrayList<ClientAccount>();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type == ClientAccount.TYPE_CASH
	// || account.type == ClientAccount.TYPE_BANK
	// || account.type == ClientAccount.TYPE_CREDIT_CARD
	// || account.type == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
	// || account.type == ClientAccount.TYPE_LONG_TERM_LIABILITY) {
	// payFromAccounts.add(account);
	// }
	// }
	// return payFromAccounts;
	//
	// }
	//
	// // To display the DepositIn Account combo box of Creating Cash Sale,
	// // ReceivePayment
	// public static List<ClientAccount> getDepositInAccounts(ClientCompany
	// company) {
	//
	// List<ClientAccount> depositInAccounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type == ClientAccount.TYPE_CASH
	// || account.type == ClientAccount.TYPE_BANK
	// || account.type == ClientAccount.TYPE_CREDIT_CARD) {
	// depositInAccounts.add(account);
	// }
	// }
	// return depositInAccounts;
	//
	// }
	//
	// // To Display the Account Combo box of Transaction Item Lines.
	// public static List<ClientAccount> getGridAccounts(ClientCompany company)
	// {
	// List<ClientAccount> gridAccounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type != ClientAccount.TYPE_CASH
	// && account.type != ClientAccount.TYPE_BANK
	// && account.type != ClientAccount.TYPE_INVENTORY_ASSET
	// && account.type != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
	// && account.type != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
	// gridAccounts.add(account);
	// }
	// }
	// return gridAccounts;
	// }
	//
	// public static List<ClientAccount> getCashBackAccounts(ClientCompany
	// company) {
	// List<ClientAccount> cashBackAccounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type != ClientAccount.TYPE_INVENTORY_ASSET
	// && account.type != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
	// && account.type != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
	// cashBackAccounts.add(account);
	// }
	// }
	// return cashBackAccounts;
	// }
	//
	// public static List<ClientAccount> getIncomeAndExpenseAccounts(
	// ClientCompany company) {
	// List<ClientAccount> incomeAndExpenseAccounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type != ClientAccount.TYPE_INVENTORY_ASSET
	// && account.type != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
	// && account.type != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
	// incomeAndExpenseAccounts.add(account);
	// }
	// }
	// return incomeAndExpenseAccounts;
	// }
	//
	// // To display the Bank Account combo box of Creating Make Deposit
	// public static List<ClientAccount> getBankAccounts(ClientCompany company)
	// {
	//
	// List<ClientAccount> bankAccounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type == ClientAccount.TYPE_BANK) {
	// bankAccounts.add(account);
	// }
	// }
	// return bankAccounts;
	//
	// }
	//
	// public static List<ClientAccount> getBankingAccountSummary(
	// ClientCompany company) {
	//
	// List<ClientAccount> bankingAccountSummary = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type == ClientAccount.TYPE_BANK
	// && (account.bankAccountType == ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING
	// || account.bankAccountType ==
	// ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET || account.bankAccountType
	// == ClientAccount.BANK_ACCCOUNT_TYPE_SAVING)) {
	// bankingAccountSummary.add(account);
	// }
	// }
	// return bankingAccountSummary;
	//
	// }

	// public static ClientAccount getAccount(ClientCompany company, Long id) {
	//
	// ClientAccount account = null;
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// account = (ClientAccount) iterator.next();
	//
	// if (account.id == id) {
	// return account;
	// }
	// }
	// return null;
	//
	// }
	//
	// public static List<ClientAccount> getAccounts(ClientCompany company,
	// int type) {
	//
	// List<ClientAccount> accounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type == type) {
	// accounts.add(account);
	// }
	// }
	//
	// return accounts;
	//
	// }
	//
	// public static List<ClientAccount> getAccounts(ClientCompany company) {
	//
	// List<ClientAccount> accounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// accounts.add(account);
	// }
	// return accounts;
	//
	// }

	public static double getCalculatedItemUnitPriceForPriceLevel(
			ClientItem item, ClientPriceLevel priceLevel,
			boolean isVendorTransaction) {

		double amt;

		if (item == null)
			return 0.00D;

		if (isVendorTransaction) {

			amt = item.getPurchasePrice();

		} else {
			amt = item.getSalesPrice();
		}

		if (priceLevel == null)
			return amt;

		try {

			double percentage = priceLevel.getPercentage()
					* (priceLevel.isPriceLevelDecreaseByThisPercentage() ? -1
							: 1);

			amt += amt * (percentage / 100);

			return amt;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return amt;
	}

	private static final String[] tensNames = { "", " ten", " twenty",
			" thirty", " forty", " fifty", " sixty", " seventy", " eighty",
			" ninety" };

	private static final String[] numNames = { "", " one", " two", " three",
			" four", " five", " six", " seven", " eight", " nine", " ten",
			" eleven", " twelve", " thirteen", " fourteen", " fifteen",
			" sixteen", " seventeen", " eighteen", " nineteen" };

	private static String convertLessThanOneThousand(int number) {
		String soFar;

		if (number % 100 < 20) {
			soFar = numNames[number % 100];
			number /= 100;
		} else {
			soFar = numNames[number % 10];
			number /= 10;

			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0)
			return soFar;
		return numNames[number] + " hundred" + soFar;
	}

	public static String convert(double number) {
		// 0 to 999 999 999 999
		if (number == 0) {
			return "zero";
		}

		String snumber = Double.toString(number);

		// pad with "0"
		int dotIndex = snumber.indexOf('.');
		if (dotIndex < 0) {
			dotIndex = snumber.length();
		}
		for (; dotIndex < 12; dotIndex++) {
			snumber = "0" + snumber;
		}

		// XXXnnnnnnnnn
		int billions = Integer.parseInt(snumber.substring(0, 3));
		// nnnXXXnnnnnn
		int millions = Integer.parseInt(snumber.substring(3, 6));
		// nnnnnnXXXnnn
		int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
		// nnnnnnnnnXXX
		int thousands = Integer.parseInt(snumber.substring(9, 12));

		String tradBillions;
		switch (billions) {
		case 0:
			tradBillions = "";
			break;
		case 1:
			tradBillions = convertLessThanOneThousand(billions) + " billion ";
			break;
		default:
			tradBillions = convertLessThanOneThousand(billions) + " billion ";
		}
		String result = tradBillions;

		String tradMillions;
		switch (millions) {
		case 0:
			tradMillions = "";
			break;
		case 1:
			tradMillions = convertLessThanOneThousand(millions) + " million ";
			break;
		default:
			tradMillions = convertLessThanOneThousand(millions) + " million ";
		}
		result = result + tradMillions;

		String tradHundredThousands;
		switch (hundredThousands) {
		case 0:
			tradHundredThousands = "";
			break;
		case 1:
			tradHundredThousands = "one thousand ";
			break;
		default:
			tradHundredThousands = convertLessThanOneThousand(hundredThousands)
					+ " thousand ";
		}
		result = result + tradHundredThousands;

		String tradThousand;
		tradThousand = convertLessThanOneThousand(thousands);
		result = result + tradThousand;

		// remove extra spaces!
		result = result.replaceAll("^\\s+", "")
				.replaceAll("\\b\\s{2,}\\b", " ");
		return result.toUpperCase();
	}

	public static String getNumberInWords(Object object) {

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
		// Double amountEntered = number;
		String amount = object.toString();
		// StringTokenizer amt = new StringTokenizer(amount, ".");
		int i = 0;
		char c[] = new char[amount.length()];
		amount.getChars(0, amount.length(), c, 0);
		Boolean isDecimal = Boolean.FALSE;
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

		// String faceValue = "";
		//
		// String subPlaceValue = "";
		//
		// String subFaceValue = "";

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
				pos = messages.THOUSAND();
				break;
			case 3:
				pos = messages.MILLION();
				break;
			case 4:
				pos = messages.BILLION();
				break;
			case 5:
				pos = messages.TRILLION();
				break;
			}
		}
		return pos;

	}

	public static String readNumber(String subString) {

		String faceValue = "";
		String numberInWords = "";

		int length = subString.length();
		Boolean mid = false;

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

	public static Object[] getFaceValue(String lhs, Boolean mid) {

		String start = "";
		int length = 0;
		if (lhs != null) {
			length = lhs.length();
		}
		if (length == 1 && !mid) {
			char c = lhs.charAt(0);
			switch (c) {
			case '0':
				start = messages.ZERO();
				break;
			case '1':
				start = messages.ONE();
				break;
			case '2':
				start = messages.TWO();
				break;
			case '3':
				start = messages.THREE();
				break;
			case '4':
				start = messages.FOUR();
				break;
			case '5':
				start = messages.FIVE();
				break;
			case '6':
				start = messages.SIX();
				break;
			case '7':
				start = messages.SEVEN();
				break;
			case '8':
				start = messages.EIGHT();
				break;
			case '9':
				start = messages.NINE();
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
					start = messages.TEN();
				else if (modLhs.equals("11"))
					start = messages.ELEVEN();
				else if (modLhs.equals("12"))
					start = messages.TWELVE();
				else if (modLhs.equals("13"))
					start = messages.THIRTEEN();
				else if (modLhs.equals("14"))
					start = messages.TOURTEEN();
				else if (modLhs.equals("15"))
					start = messages.TIFTEEN();
				else if (modLhs.equals("16"))
					start = messages.TIXTEEN();
				else if (modLhs.equals("17"))
					start = messages.SEVENTEEN();
				else if (modLhs.equals("18"))
					start = messages.EIGHTEEN();
				else if (modLhs.equals("19"))
					start = messages.NINETEEN();

				else if (modLhs.equals("20"))
					start = messages.TWENTY();
				else if (modLhs.equals("30"))
					start = messages.THIRTY();
				else if (modLhs.equals("40"))
					start = messages.FORTY();
				else if (modLhs.equals("50"))
					start = messages.FIFTY();
				else if (modLhs.equals("60"))
					start = messages.SIXTY();
				else if (modLhs.equals("70"))
					start = messages.SEVENTY();
				else if (modLhs.equals("80"))
					start = messages.EIGHTY();
				else if (modLhs.equals("90"))
					start = messages.NINTY();

				return new Object[] { start, mid };
			} else if (length > 2) {

				switch (length) {
				case 3:
					start = messages.HUNDRED();
					break;
				}
				return new Object[] { start, mid };

			}
		}

		return new Object[] { "", mid };
	}

	public static String getStatus(int transactionType, int status) {
		StringBuffer buffer = new StringBuffer();
		String statusString = getTransactionStatus(transactionType, status);

		if (statusString.trim().length() > 1) {
			buffer.append(statusString);
		} else
			buffer.append(statusString);

		return buffer.toString();
	}

	public static String getTransactionStatus(int transactionType, int status) {

		StringBuffer buffer = new StringBuffer();

		switch (transactionType) {

		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.unApplied());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(messages.partiallyApplied());
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.applied());
				break;
			}
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.notIssued());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.issued());
				break;
			}
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.notPaid());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(messages.partiallyPaid());
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.paid());
				break;
			}
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			switch (status) {
			case ClientEstimate.STATUS_ACCECPTED:
				buffer.append(messages.accepted());
				break;
			case ClientEstimate.STATUS_REJECTED:
				buffer.append(messages.rejected());
				break;
			case ClientEstimate.STATUS_OPEN:
				buffer.append(messages.open());
				break;

			}
			break;
		case ClientTransaction.TYPE_INVOICE:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.notPaid());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(messages.partiallyPaid());
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.paid());
				break;
			}
			break;

		case ClientTransaction.TYPE_PAY_BILL:
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.notIssued());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.issued());
				break;
			}
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.unApplied());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(messages.partiallyApplied());
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.applied());
				break;
			}
			break;

		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.unApplied());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(messages.partiallyApplied());
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.applied());
				break;
			}
			break;

		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.new1());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.posted());
				break;
			}
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.notIssued());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.issued());
				break;
			}
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.notIssued());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.issued());
				break;
			}
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			break;
		case ClientTransaction.TYPE_PURCHASE_ORDER:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.notReceived());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(messages.partiallyReceived());
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.received());
				break;
			}
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.notIssued());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.issued());
				break;
			}
			break;
		case ClientTransaction.TYPE_PAY_TAX:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.notIssued());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.issued());
				break;
			}
			break;

		case ClientTransaction.TYPE_RECEIVE_TAX:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(messages.notIssued());
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(messages.issued());
				break;
			}
			break;
		}

		return buffer.toString();

	}

	public static String dateToString(ClientFinanceDate date) {
		try {
			DateTimeFormat dateFormatter = DateTimeFormat
					.getFormat("yyyy-MM-dd");
			String format = dateFormatter.format(date.getDateAsObject());
			return format;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T extends IAccounterCore> long getID(T source) {

		return source != null ? source.getID() : 0;

	}

	public static long getTime(ClientFinanceDate date) {

		return date != null ? date.getDate() : 0l;
	}

	public static <T extends IAccounterCore> void updateClientList(
			T objectInList, List<T> objectsList) {

		if (objectInList == null || objectsList == null)
			return;
		boolean change = false;
		T existObj = getObject(objectsList, objectInList.getID());
		if (existObj != null) {
			objectsList.remove(existObj);
			if (isDelete)
				return;
			change = true;
		}
		if (objectInList.getName() != null)
			UIUtils.updateComboItemsInSortedOrder(objectInList, objectsList);
		else {
			objectsList.add(objectInList);
		}
		if (change) {
			Accounter.getEventBus().fireEvent(
					new CoreEvent<T>(ChangeType.CHANGE, objectInList));
		} else {
			Accounter.getEventBus().fireEvent(
					new CoreEvent<T>(ChangeType.ADD, objectInList));
		}
	}

	public static <T> ArrayList<T> filteredList(ListFilter<T> filter,
			List<T> objectsList) {
		ArrayList<T> filteredList = new ArrayList<T>();
		for (T obj : objectsList) {
			if (filter.filter(obj)) {
				filteredList.add(obj);
			}
		}

		return filteredList;
	}

	public static <S extends IAccounterCore> S getObject(List<S> list, long id) {
		if (list == null)
			return null;
		for (S s : list) {
			if (s != null && s.getID() == id) {
				return s;
			}
		}
		return null;
	}

	public static <S extends IsSerializable> S getObjectFromList(List<S> list,
			long id) {
		if (list == null)
			return null;
		for (S s : list) {
			if (s != null && ((IAccounterCore) s).getID() != id) {
				return s;
			}
		}
		return null;
	}

	public static <S extends IAccounterCore> S getObjectByName(List<S> list,
			String name) {
		if (list == null)
			return null;
		for (S s : list)
			if (s.getName().equalsIgnoreCase(name))
				return s;
		return null;
	}

	public static <S extends IAccounterCore> boolean isNumberCorrect(
			IAccounterCore iAccounterCore) {
		try {
			if (iAccounterCore instanceof ClientAccount) {
				if (checkIfNotNumber(((ClientAccount) iAccounterCore)
						.getNumber())) {
					throw new InvalidEntryException(
							messages.AAccountNumbershoublebeanumber());
				}
				if (Integer.parseInt(((ClientAccount) iAccounterCore)
						.getNumber()) < 1) {
					throw new InvalidEntryException(
							messages.AAccountNumbershoublebepositive());

				}
			} else if (iAccounterCore instanceof ClientVendor) {
				if (((ClientVendor) iAccounterCore).getVendorNumber()
						.equals("")) {
					((ClientVendor) iAccounterCore).setVendorNumber(String
							.valueOf(0));
				}
				if (checkIfNotNumber(((ClientVendor) iAccounterCore)
						.getVendorNumber())) {
					throw new InvalidEntryException(
							messages.ASupplierAccountNumbershoublebeanumber());
				}
				if (Integer.parseInt(((ClientVendor) iAccounterCore)
						.getVendorNumber()) < 1) {
					throw new InvalidEntryException(
							messages.ASupplierAccountNumbershoublebepositive());
				}

			}
		} catch (InvalidEntryException e) {
			return true;
		}
		return false;
	}

	public static <S extends IAccounterCore> boolean isObjectExist(
			List<S> list, String name) {
		if (list == null || list.isEmpty())
			return false;
		for (S s : list) {
			if (s.getName() != null
					&& s.getName().toLowerCase().equals(name.toLowerCase())) {
				return true;
			}

		}
		return false;
	}

	public static boolean checkIfNotNumber(String in) {
		try {
			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return true;
		}
		return false;
	}

	public static <S extends IAccounterCore> S getObject(Set<S> set, long id) {
		if (set == null)
			return null;

		for (S s : set)
			if (s.getID() == id)
				return s;
		return null;
	}

	public static String getItemTypeText(ClientItem item) {

		String itemText = "";

		if (item == null)
			return itemText;

		switch (item.getType()) {

		case ClientItem.TYPE_SERVICE:

			itemText = messages.serviceItem();
			break;

		case ClientItem.TYPE_INVENTORY_PART:

			itemText = messages.inventoryPart();

			break;

		case ClientItem.TYPE_DISCOUNT:

			itemText = messages.discount();
			break;

		case ClientItem.TYPE_GROUP:

			itemText = messages.group();
			break;

		case ClientItem.TYPE_INVENTORY_ASSEMBLY:

			itemText = messages.inventoryAssembly();
			break;

		case ClientItem.TYPE_NON_INVENTORY_PART:

			itemText = messages.NonInventory();
			break;

		case ClientItem.TYPE_PAYMENT:

			itemText = messages.payment();

			break;

		case ClientItem.TYPE_SALES_TAX_GROUP:

			itemText = messages.salesTaxGroups();

			break;

		case ClientItem.TYPE_SALES_TAX_ITEM:

			itemText = messages.salesTaxItem();

			break;

		case ClientItem.TYPE_SUBTOTAL:

			itemText = messages.SubTotal();

			break;
		default:
			break;
		}

		return itemText;

	}

	public static <L> List<L> getArrayList(List<L> list) {
		if (list == null)
			return new ArrayList<L>();
		return list;
	}

	public static String getDescription(int boxType) {

		String description = null;
		switch (boxType) {
		case 1:
			description = messages.box1Description();
			break;
		case 2:
			description = messages.box2Description();
			break;

		}
		return description;
	}

	public static List<String> getStatesList() {

		List<String> statesName = new ArrayList<String>();
		statesName.add("ANDAMAN AND NICOBAR ISLANDS");
		statesName.add("ANDHRA PRADESH");
		statesName.add("ARUNACHAL PRADESH");
		statesName.add("ASSAM");
		statesName.add("BIHAR");
		statesName.add("CHANDIGARH");
		statesName.add("DADRA & NAGAR HAVELI");
		statesName.add("DAMAN & DIU");
		statesName.add("DELHI");
		statesName.add("GOA");
		statesName.add("GUJARAT");
		statesName.add("HARYANA");
		statesName.add("HIMACHAL PRADESH");
		statesName.add("JAMMU & KASHMIR");
		statesName.add("KARNATAKA");
		statesName.add("KERALA");
		statesName.add("LAKSHWADEEP");
		statesName.add("MADHYA PRADESH");
		statesName.add("MAHARASHTRA");
		statesName.add("MANIPUR");
		statesName.add("MEGHALAYA");
		statesName.add("MIZORAM");
		statesName.add("NAGALAND");
		statesName.add("ORISSA");
		statesName.add("PONDICHERRY");
		statesName.add("PUNJAB");
		statesName.add("RAJASTHAN");
		statesName.add("SIKKIM");
		statesName.add("TAMILNADU");
		statesName.add("TRIPURA");
		statesName.add("UTTAR PRADESH");
		statesName.add("WEST BENGAL");
		statesName.add("CHHATISHGARH");
		statesName.add("UTTARANCHAL");
		statesName.add("JHARKHAND");
		statesName.add("OTHERS");

		return statesName;
	}

	public static List<String> getMinistryType() {
		List<String> names = new ArrayList<String>();
		names.add("Agriculture");
		names.add("Atomic Energy");
		names.add("Fertilizers");
		names.add("Chemicals and Petrochemicals");
		names.add("Civil Aviation and Tourism");
		names.add("Coal");
		names.add("Consumer Affairs, Food and Public Distribution");
		names.add("Commerce and Textiles");
		names.add("Environment and Forests and Ministry of Earth Science");
		names.add("External Affairs and Overseas Indian Affairs");
		names.add("Finance");
		names.add("Central Board of Direct Taxes");
		names.add("Central Board of Excise and Customs");
		names.add("Contoller of Aid Accounts and Audit");
		names.add("Central Pension Accounting Office");
		names.add("Food Processing Industries");
		names.add("Health and Family Welfare");
		names.add("Home Affairs and Development of North Eastern Region");
		names.add("Human Resource Development");
		names.add("Industry");
		names.add("Information and Broadcasting");
		names.add("Telecommunication and Information Technology");
		names.add("Labour");
		names.add("Law and Justice and Company Affairs");
		names.add("Personnel, Public Grievances and Pesions");
		names.add("Petroleum and Natural Gas");
		names.add("Plannning, Statistics and Programme Implementation");
		names.add("Power");
		names.add("New and Renewable Energy");
		names.add("Rural Development and Panchayati Raj");
		names.add("Science And Technology");
		names.add("Space");
		names.add("Steel");
		names.add("Mines");
		names.add("Social Justice and Empowerment");
		names.add("Tribal Affairs");
		names.add("D/o Commerce (Supply Division)");
		names.add("Shipping and Road Transport and Highways");
		names.add("Urban Development, Urban Employment and Poverty Alleviation");
		names.add("Water Resources");
		names.add("President's Secretariat");
		names.add("Lok Sabha Secretariat");
		names.add("Rajya Sabha secretariat");
		names.add("Election Commission");
		names.add("Ministry of Defence (Controller General of Defence Accounts)");
		names.add("Ministry of Railways");
		names.add("Department of Posts");
		names.add("Department of Telecommunications");
		names.add("Andaman and Nicobar Islands Administration");
		names.add("Chandigarh Administration");
		names.add("Dadra and Nagar Haveli");
		names.add("Goa, Daman and Diu");
		names.add("Lakshadweep");
		names.add("Pondicherry Administration");
		names.add("Pay and Accounts Officers (Audit)");
		names.add("Ministry of Non-conventional energy sources");
		names.add("Government Of NCT of Delhi");
		names.add("Others");
		return names;
	}

	public static List<String> getDeductorTypes() {

		List<String> names = new ArrayList<String>();

		names.add("Central Government");
		names.add("State Government");
		names.add("Statutory body (Central Govt.)");
		names.add("Statutory body (State Govt.)");
		names.add("Autonomous body (Central Govt.)");
		names.add("Autonomous body (State Govt.)");
		names.add("Local Authority (Central Govt.)");
		names.add("Local Authority (State Govt.)");
		names.add("Company");
		names.add("Branch / Division of Company");
		names.add("Association of Person (AOP)");
		names.add("Association of Person (Trust)");
		names.add("Artificial Juridical Person");
		names.add("Body of Individuals");
		names.add("Individual/HUF");
		names.add("Firm");

		return names;

	}

	public static List<String> get26QSectionNames() {

		List<String> names = new ArrayList<String>();

		names.add("193");
		names.add("194");
		names.add("194A");
		names.add("194B");
		names.add("194BB");
		names.add("194C");
		names.add("194D");
		names.add("194EE");
		names.add("194F");
		names.add("194G");
		names.add("194H");
		names.add("194I");
		names.add("194J");
		names.add("194LA");

		return names;

	}

	public static List<String> get26QSectionCodes() {

		List<String> names = new ArrayList<String>();

		names.add("193");
		names.add("194");
		names.add("94A");
		names.add("94B");
		names.add("4BB");
		names.add("94C");
		names.add("94D");
		names.add("4EE");
		names.add("94F");
		names.add("94G");
		names.add("94H");
		names.add("94I");
		names.add("94J");
		names.add("94L");

		return names;

	}

	public static List<String> getDeductorCodes() {

		List<String> names = new ArrayList<String>();

		names.add("A");
		names.add("S");
		names.add("D");
		names.add("E");
		names.add("G");
		names.add("H");
		names.add("L");
		names.add("N");
		names.add("K");
		names.add("M");
		names.add("P");
		names.add("T");
		names.add("J");
		names.add("B");
		names.add("Q");
		names.add("F");

		return names;

	}

	public static double roundTo2Digits(double amount) {

		long number2 = Math.round(amount * 100);
		return (double) (number2 / 100);
	}

	public static boolean isUserHavePermissions(
			AccounterCoreType transactionType) {
		ClientUser user = Accounter.getUser();
		if (user.isAdmin()) {
			return true;
		}
		if (user.getUserRole().equals(RolePermissions.READ_ONLY)) {
			return false;
		}
		ClientUserPermissions permissions = user.getPermissions();
		boolean isAllowed = false;
		switch (transactionType) {
		case VENDOR:
		case CUSTOMER_GROUP:
		case VENDOR_GROUP:
		case CUSTOMER:
		case JOB:
			isAllowed = (permissions.getTypeOfInvoicesBills() == RolePermissions.TYPE_YES || permissions
					.getInvoicesAndPayments() == RolePermissions.TYPE_YES);
			break;
		case TAX_GROUP:
		case TDSCHALANDETAIL:
		case TAXITEM:
		case TAXAGENCY:
		case TAX_CODE:
		case FIXEDASSET:
		case FIXEDASSETHISTORY:
		case BUDGET:
		case TDSDEDUCTORMASTER:
		case TDSRESPONSIBLEPERSON:
		case PAY_HEAD:
		case PAY_STRUCTURE:
		case PAY_RUN:
		case EMPLOYEE:
		case EMPLOYEE_GROUP:
		case ATTENDANCE_PRODUCTION_TYPE:
		case PAY_ROLL_UNIT:
		case TYPE_PAYEMPLOYEE:
			isAllowed = user.getUserRole().equals(
					RolePermissions.FINANCIAL_ADVISER);
			break;
		case ITEM:
			isAllowed = permissions.getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES
					|| permissions.getTypeOfInvoicesBills() == RolePermissions.TYPE_YES
					|| permissions.getInvoicesAndPayments() == RolePermissions.TYPE_YES;
			break;
		case STOCK_ADJUSTMENT:
		case WAREHOUSE:
		case STOCK_TRANSFER:
			isAllowed = permissions.getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES;
			break;
		case MEASUREMENT:
		case UNIT:
			isAllowed = permissions.getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES
					|| permissions.getTypeOfInvoicesBills() == RolePermissions.TYPE_YES
					|| permissions.getInvoicesAndPayments() == RolePermissions.TYPE_YES;
			break;
		case ACCOUNT:
			isAllowed = permissions.getTypeOfManageAccounts() == RolePermissions.TYPE_YES;
			break;
		case USER:
			isAllowed = user.isCanDoUserManagement();
			break;
		case BRANDINGTHEME:
		case PAYMENT_TERM:
		case SHIPPING_METHOD:
		case SHIPPING_TERM:
		case CREDIT_RATING:
		case CURRENCY:
		case LOCATION:
		case ACCOUNTER_CLASS:
		case ITEM_GROUP:
		case INVOICE_FREQUENCY:
		case SALES_PERSON:
			isAllowed = permissions.getTypeOfCompanySettingsLockDates() == RolePermissions.TYPE_YES;
			break;
		case RECONCILIATION:
			isAllowed = permissions.getTypeOfBankReconcilation() == RolePermissions.TYPE_YES
					|| user.getUserRole().equals(
							RolePermissions.FINANCIAL_ADVISER);
			break;
		case BANK:
		case BANK_ACCOUNT:
			isAllowed = permissions.getTypeOfBankReconcilation() == RolePermissions.TYPE_YES;
			break;
		}
		return isAllowed;
	}

	public static ClientFinanceDate[] getFinancialQuarter(int quarter) {

		ClientFinanceDate startDate;
		ClientFinanceDate endDate;

		ClientFinanceDate start = Accounter.getCompany()
				.getCurrentFiscalYearStartDate();

		switch (quarter) {
		case 1:
			startDate = start;
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(start.getDateAsObject());
			endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 3);
			endCal.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal.getTime());
			break;

		case 2:
			startDate = start;
			startDate.setMonth(start.getMonth() + 3);
			Calendar endCal2 = Calendar.getInstance();
			endCal2.setTime(startDate.getDateAsObject());
			endCal2.set(Calendar.MONTH, endCal2.get(Calendar.MONTH) + 3);
			endCal2.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal2.getTime());
			break;

		case 3:
			startDate = start;
			startDate.setMonth(start.getMonth() + 6);
			Calendar endCal3 = Calendar.getInstance();
			endCal3.setTime(startDate.getDateAsObject());
			endCal3.set(Calendar.MONTH, endCal3.get(Calendar.MONTH) + 3);
			endCal3.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal3.getTime());
			break;
		default:
			startDate = start;
			startDate.setMonth(start.getMonth() + 9);
			Calendar endCal4 = Calendar.getInstance();
			endCal4.setTime(startDate.getDateAsObject());
			endCal4.set(Calendar.MONTH, endCal4.get(Calendar.MONTH) + 3);
			endCal4.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal4.getTime());
			break;
		}
		ClientFinanceDate[] dates = new ClientFinanceDate[] { startDate,
				endDate };
		return dates;
	}
}