package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class ClientTransaction implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_ALL = 1000;
	public static final int MEMO_OPENING_BALANCE = 0;
	public static final int TYPE_CASH_SALES = 1;
	public static final int TYPE_CASH_PURCHASE = 2;
	public static final int TYPE_CREDIT_CARD_CHARGE = 3;
	public static final int TYPE_CUSTOMER_CREDIT_MEMO = 4;
	public static final int TYPE_CUSTOMER_REFUNDS = 5;
	public static final int TYPE_ENTER_BILL = 6;
	public static final int TYPE_ESTIMATE = 7;

	public static final int TYPE_INVOICE = 8;
	public static final int TYPE_ISSUE_PAYMENT = 9;
	public static final int TYPE_TRANSFER_FUND = 10;
	public static final int TYPE_PAY_BILL = 11;
	public static final int TYPE_RECEIVE_PAYMENT = 12;
	public static final int TYPE_VENDOR_CREDIT_MEMO = 14;
	public static final int TYPE_WRITE_CHECK = 15;
	public static final int TYPE_JOURNAL_ENTRY = 16;
	public static final int TYPE_PAY_TAX = 17;
	public static final int TYPE_EXPENSE = 18;
	public static final int TYPE_PAY_EXPENSE = 19;
	public static final int TYPE_TAX_RETURN = 20;

	// public static final int TYPE_SALES_ORDER = 21;
	public static final int TYPE_PURCHASE_ORDER = 22;
	public static final int TYPE_ITEM_RECEIPT = 23;

	public static final int TYPE_ADJUST_VAT_RETURN = 24;

	public static final int TYPE_CASH_EXPENSE = 26;
	public static final int TYPE_CREDIT_CARD_EXPENSE = 27;
	public static final int TYPE_EMPLOYEE_EXPENSE = 28;
	public static final int TYPE_CUSTOMER_PREPAYMENT = 29;
	public static final int TYPE_ADJUST_SALES_TAX = 32;
	public static final int TYPE_MAKE_DEPOSIT = 35;

	public static final int TYPE_STOCK_ADJUSTMENT = 36;
	public static final int TYPE_BUILD_ASSEMBLY = 37;

	/*
	 * There is no seperate transaction for VendorPayment.VendorPayment saved as
	 * a Paybill.So,to open the VendorPaymentView in edit mode we use this
	 * transaction.This constant not exited @ server side.
	 */
	public static final int TYPE_VENDOR_PAYMENT = 25;
	public static final int TYPE_RECEIVE_TAX = 31;
	public static final int TYPE_TDS_CHALLAN = 34;

	public static final int STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED = 0;
	public static final int STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED = 1;
	public static final int STATUS_PAID_OR_APPLIED_OR_ISSUED = 2;
	public static final int STATUS_DELETED = 3;

	public static final int STATUS_DRAFT = 201;
	public static final int STATUS_TEMPLATE = 202;
	public static final int STATUS_APPROVE = 203;
	public static final int STATUS_VOID = 204;

	public static final int STATUS_OPEN = 101;
	public static final int STATUS_COMPLETED = 102;
	public static final int STATUS_CANCELLED = 103;

	public static final int CATEGORY_CUSTOMER = 1;
	public static final int CATEGORY_VENDOR = 2;
	public static final int CATEGORY_BANKING = 3;
	public static final int CATEGORY_EMPLOYEE = 4;

	public static final int TYPE_MISC_SAMPLE_FORM = 33;

	public static final int VIEW_ALL = 0;
	public static final int VIEW_VOIDED = 3;
	public static final int VIEW_OVERDUE = 2;
	public static final int VIEW_OPEN = 1;
	public static final int VIEW_DRAFT = 4;

	public static final int TYPE_PAY_RUN = 39;
	public static final int TYPE_PAY_EMPLOYEE = 40;

	/**
	 * in Edit mode of transaction, if any transaction has reference in
	 * VATRETURN(FILE VAT), then edit should be disabled.user cant edit them,
	 * this variable value be assign at time of conversion server to client
	 * object. by default can edit all transactions.
	 */
	private long job;
	public boolean canEdit = true;
	private long location;
	int version;
	public long id;
	int type;
	long transactionDate;
	String number = "";
	// String checkNumber = "";
	private ClientStatementRecord statementRecord;

	List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
	List<ClientTransactionPayBill> transactionPayBill = new ArrayList<ClientTransactionPayBill>();
	List<ClientTransactionReceivePayment> transactionReceivePayment = new ArrayList<ClientTransactionReceivePayment>();
	List<ClientTransactionIssuePayment> transactionIssuePayment;
	List<ClientTransactionPayTAX> transactionPaySalesTax;
	List<ClientWareHouseAllocation> wareHouseAllocations;

	// ClientCreditsAndPayments creditsAndPayments;

	// List<ClientEntry> entry;

	int status = 0;
	boolean canVoidOrEdit = true;
	boolean isDeposited = false;

	/**
	 * To set the status of transaction whether it is approved or saved as
	 * draft.
	 */
	private int saveStatus = 0;

	// boolean isVoid;
	boolean isDeleted;
	boolean isEdited;
	private boolean isValidated;
	double total;
	double netAmount;

	double previousTotal;
	String paymentMethod;
	protected String memo;
	String reference;

	// For Sales Tax Liability Report
	double subTotal;
	double totalTaxableAmount;
	double totalNonTaxableAmount;

	private long recurringTransaction;

	long transactionMakeDepositEntries;

	private long currency;
	/**
	 * Multiplication factor to convert selected currency to base currency
	 */
	private double currencyFactor = 1D;

	private long accounterClass;

	private ClientActivity lastActivity;

	/**
	 * This is temporary variable used to delete the reminder.
	 */
	private long tobeDeleteReminder;

	private Set<ClientAttachment> attachments = new HashSet<ClientAttachment>();

	// boolean showPricesWithVATOrVATInclusive;
	// boolean EUVATExemptPayee;
	// boolean isVATreturned;
	//
	// double netAmount;
	// double totalVAT;
	//
	//
	//
	// public double getNetAmount() {
	// return netAmount;
	// }
	//
	// public void setNetAmount(double netAmount) {
	// this.netAmount = netAmount;
	// }
	//
	// public double getTotalVAT() {
	// return totalVAT;
	// }
	//
	// public void setTotalVAT(double totalVAT) {
	// this.totalVAT = totalVAT;
	// }

	public ClientTransaction() {
		transactionItems = new ArrayList<ClientTransactionItem>();
	}

	// public ClientCreditsAndPayments getCreditsAndPayments() {
	// return creditsAndPayments;
	// }
	//
	// public void setCreditsAndPayments(
	// ClientCreditsAndPayments creditsAndPayments) {
	// this.creditsAndPayments = creditsAndPayments;
	// }

	public double getAllLineTotal() {
		return subTotal;
	}

	public List<ClientTransactionPayTAX> getTransactionPaySalesTax() {
		return transactionPaySalesTax;
	}

	public void setTransactionPaySalesTax(
			List<ClientTransactionPayTAX> transactionPaySalesTax) {
		this.transactionPaySalesTax = transactionPaySalesTax;
	}

	public void setAllLineTotal(double allLineTotal) {
		this.subTotal = allLineTotal;
	}

	public double getAllTaxableLineTotal() {
		return totalTaxableAmount;
	}

	public void setAllTaxableLineTotal(double allTaxableLineTotal) {
		this.totalTaxableAmount = allTaxableLineTotal;
	}

	public double getAllNonTaxableLineTotal() {
		return totalNonTaxableAmount;
	}

	public void setAllNonTaxableLineTotal(double allNonTaxableLineTotal) {
		this.totalNonTaxableAmount = allNonTaxableLineTotal;
	}

	// public boolean isShowPricesWithVATOrVATInclusive() {
	// return showPricesWithVATOrVATInclusive;
	// }
	//
	// public void setShowPricesWithVATOrVATInclusive(
	// boolean showPricesWithVATOrVATInclusive) {
	// this.showPricesWithVATOrVATInclusive = showPricesWithVATOrVATInclusive;
	// }
	//
	// public boolean isEUVATExemptPayee() {
	// return EUVATExemptPayee;
	// }
	//
	// public void setEUVATExemptPayee(boolean exemptPayee) {
	// EUVATExemptPayee = exemptPayee;
	// }
	//
	// public boolean isVATreturned() {
	// return isVATreturned;
	// }
	//
	// public void setVATreturned(boolean isVATreturned) {
	// this.isVATreturned = isVATreturned;
	// }

	public long getTransactionMakeDepositEntries() {
		return transactionMakeDepositEntries;
	}

	public void setTransactionMakeDepositEntries(
			long transactionMakeDepositEntries) {
		this.transactionMakeDepositEntries = transactionMakeDepositEntries;
	}

	public String getPaymentMethod() {
		return UIUtils.getpaymentMethodCheckBy_CompanyType(paymentMethod);
	}

	public String getPaymentMethodForCommands() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @return the netAmount
	 */
	public double getNetAmount() {
		return netAmount;
	}

	/**
	 * @param netAmount
	 *            the netAmount to set
	 */
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	/**
	 * @return the isVoid
	 */
	public boolean isVoid() {
		return saveStatus == STATUS_VOID;
	}

	/**
	 * @return the isVoidBefore
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	/**
	 * @param isVoidBefore
	 *            the isVoidBefore to set
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the version
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the date
	 */
	public ClientFinanceDate getDate() {
		if (transactionDate == 0.0)
			return null;
		else
			return new ClientFinanceDate(this.transactionDate);
	}

	/**
	 * @param date2
	 *            the date to set
	 */
	public void setDate(long date) {
		this.transactionDate = date;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	// /**
	// * @return the checkNumber
	// */
	//
	// public String getCheckNumber(){
	// return checkNumber;
	// }
	//
	// /**
	// * @param checkNumber
	// * the checkNumber to set
	// */
	// public void setCheckNumber(String checkNumber){
	// this.checkNumber=checkNumber;
	// }

	/**
	 * @return the transactionItems
	 */
	public List<ClientTransactionItem> getTransactionItems() {
		return transactionItems;
	}

	/**
	 * @param transactionItems
	 *            the transactionItems to set
	 */
	public void setTransactionItems(List<ClientTransactionItem> transactionItems) {
		this.transactionItems = transactionItems;

		if (transactionItems == null)
			return;

		for (ClientTransactionItem transactionItem : transactionItems) {
			transactionItem.setTransaction(this);
		}
	}

	/**
	 * @return the transactionPayBill
	 */
	public List<ClientTransactionPayBill> getTransactionPayBill() {
		return transactionPayBill;
	}

	/**
	 * @param transactionPayBill
	 *            the transactionPayBill to set
	 */
	public void setTransactionPayBill(
			List<ClientTransactionPayBill> transactionPayBill) {
		this.transactionPayBill = transactionPayBill;
	}

	/**
	 * @return the transactionReceivePayment
	 */
	public List<ClientTransactionReceivePayment> getTransactionReceivePayment() {
		return this.transactionReceivePayment;
	}

	/**
	 * @param transactionReceivePayment
	 *            the transactionReceivePayment to set
	 */
	public void setTransactionReceivePayment(
			List<ClientTransactionReceivePayment> transactionReceivePayment) {
		this.transactionReceivePayment = transactionReceivePayment;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the transactionIssuePayment
	 */
	public List<ClientTransactionIssuePayment> getTransactionIssuePayment() {
		return transactionIssuePayment;
	}

	/**
	 * @param transactionIssuePayment
	 *            the transactionIssuePayment to set
	 */
	public void setTransactionIssuePayment(
			List<ClientTransactionIssuePayment> transactionIssuePayment) {
		this.transactionIssuePayment = transactionIssuePayment;
	}

	/**
	 * @return the isDeposited
	 */
	public Boolean getIsDeposited() {
		return isDeposited;
	}

	/**
	 * @param isDeposited
	 *            the isDeposited to set
	 */
	public void setIsDeposited(boolean isDeposited) {
		this.isDeposited = isDeposited;
	}

	/**
	 * @return the canVoidOrEdit
	 */
	public boolean getCanVoidOrEdit() {
		return canVoidOrEdit;
	}

	/**
	 * @param canVoidOrEdit
	 *            the canVoidOrEdit to set
	 */
	public void setCanVoidOrEdit(boolean canVoidOrEdit) {
		this.canVoidOrEdit = canVoidOrEdit;
	}

	protected double getDiscountTotalSum() {

		double discountAmt = 0.00D;

		if (transactionItems == null) {
			return discountAmt;
		}

		for (ClientTransactionItem item : transactionItems) {

			if (item.getType() != ClientTransactionItem.TYPE_ITEM)
				continue;

			if (item.getDiscount() != null) {
				discountAmt = discountAmt + item.getDiscount();
			}

		}

		return discountAmt;
	}

	protected double getTaxableLineTotalSum() {

		double amount = 0.00D;

		if (transactionItems == null) {
			return amount;
		}

		for (ClientTransactionItem txItem : transactionItems) {

			if (txItem.getType() == ClientTransactionItem.TYPE_COMMENT)
				continue;
			// FIXME ::: check it
			// if (txItem.getItemTax() != null &&
			// txItem.getItemTax().isTaxable()) {
			//
			// amount += ((txItem.getLineTotal() != null) ? txItem
			// .getLineTotal() : 0.00D);
			//
			// }

		}

		return amount;

	}

	protected double getLineTotalSum() {

		double amount = 0.00D;

		if (transactionItems == null) {
			return amount;
		}

		for (ClientTransactionItem txItem : transactionItems) {

			if (txItem.getType() == ClientTransactionItem.TYPE_COMMENT)
				continue;
			// FIXME :: check it
			// amount += ((txItem.getLineTotal() != null) ?
			// txItem.getLineTotal()
			// : 0.00D);

		}

		return amount;

	}

	public boolean isNotNull() {

		return this != null;
	}

	public boolean isInvoice() {

		return this != null && this instanceof ClientInvoice;
	}

	public boolean isQuote() {

		return this != null && this instanceof ClientEstimate;
	}

	public boolean isCashSale() {

		return this != null && this instanceof ClientCashSales;
	}

	public boolean isCashPurchase() {

		return this != null && this instanceof ClientCashPurchase;
	}

	public boolean isEnterBill() {

		return this != null && this instanceof ClientEnterBill;
	}

	public boolean isVendorCreditMemo() {

		return this != null && this instanceof ClientVendorCreditMemo;
	}

	public boolean isPayBill() {

		return this != null && this instanceof ClientPayBill;
	}

	public boolean isCreditCardCharge() {

		return this != null && this instanceof ClientCreditCardCharge;
	}

	public boolean isWriteCheck() {

		return this != null && this instanceof ClientWriteCheck;

	}

	public boolean isTransferFund() {

		return this != null && this instanceof ClientTransferFund;
	}

	protected boolean isBecameVoid() {
		return isVoid() && !this.isDeleted;
	}

	public boolean isReceivePayment() {

		return this != null && this instanceof ClientReceivePayment;
	}

	public boolean isPaySalesTax() {

		return this != null && this instanceof ClientPayTAX;
	}

	public boolean isPayVAT() {

		return this != null && this instanceof ClientPayVAT;
	}

	public boolean isReceiveVAT() {

		return this != null && this instanceof ClientReceiveVAT;
	}

	public long getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrencyFactor(double currencyFactor) {
		this.currencyFactor = currencyFactor;
	}

	public double getCurrencyFactor() {
		return currencyFactor;
	}

	public ClientTransaction clone() {
		ClientTransaction clientTransactionClone = this.clone();
		// transactionItems list
		List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem clientTransactionItem : this.transactionItems) {
			transactionItems.add(clientTransactionItem.clone());
		}
		clientTransactionClone.transactionItems = transactionItems;

		// transactionPayBill list
		List<ClientTransactionPayBill> transactionPayBillList = new ArrayList<ClientTransactionPayBill>();
		for (ClientTransactionPayBill clientTransactionPayBill : this.transactionPayBill) {
			transactionPayBillList.add(clientTransactionPayBill.clone());
		}
		clientTransactionClone.transactionPayBill = transactionPayBillList;

		// transactionReceivePayment list
		List<ClientTransactionReceivePayment> transactionReceivePaymentList = new ArrayList<ClientTransactionReceivePayment>();
		for (ClientTransactionReceivePayment clientTransactionReceivePayment : this.transactionReceivePayment) {
			transactionReceivePaymentList.add(clientTransactionReceivePayment
					.clone());
		}
		clientTransactionClone.transactionReceivePayment = transactionReceivePaymentList;
		// transactionIssuePayment list
		List<ClientTransactionIssuePayment> transactionIssuePayment = new ArrayList<ClientTransactionIssuePayment>();
		for (ClientTransactionIssuePayment clientTransactionIssuePayment : this.transactionIssuePayment) {
			transactionIssuePayment.add(clientTransactionIssuePayment.clone());
		}
		clientTransactionClone.transactionIssuePayment = transactionIssuePayment;
		// transactionPaySalestax list
		List<ClientTransactionPayTAX> transactionPaySalesTax = new ArrayList<ClientTransactionPayTAX>();
		for (ClientTransactionPayTAX clientTransactionPaySalesTax : this.transactionPaySalesTax) {
			transactionPaySalesTax.add(clientTransactionPaySalesTax.clone());
		}
		clientTransactionClone.transactionPaySalesTax = transactionPaySalesTax;

		return clientTransactionClone;
	}

	public void setRecurringTransaction(long recurringTransaction) {
		this.recurringTransaction = recurringTransaction;
	}

	public long getRecurringTransaction() {
		return recurringTransaction;
	}

	public long getLocation() {
		return location;
	}

	public void setLocation(long location) {
		this.location = location;
	}

	public int getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(int saveStatus) {
		this.saveStatus = saveStatus;
	}

	public long getAccounterClass() {
		return accounterClass;
	}

	public void setAccounterClass(long accounterClass) {
		this.accounterClass = accounterClass;
	}

	/**
	 * @return the lastActivity
	 */
	public ClientActivity getLastActivity() {
		return lastActivity;
	}

	/**
	 * @param lastActivity
	 *            the lastActivity to set
	 */
	public void setLastActivity(ClientActivity lastActivity) {
		this.lastActivity = lastActivity;
	}

	/**
	 * This method tells if we are using different tax codes for each item or
	 * not. We need to depend on it while editing irrespective of the preference
	 * 
	 * @return
	 */
	public boolean usesDifferentTaxCodes() {
		if (transactionItems == null) {
			return false;
		}
		long taxCode = -1;
		for (ClientTransactionItem item : this.transactionItems) {
			if (item.referringTransactionItem != 0) {
				continue;
			}
			long code = item.getTaxCode();
			if (taxCode == -1) {
				taxCode = code;
				continue;
			}
			if (code != taxCode) {
				return true;
			}
		}
		return false;
	}

	/**
	 * For Class Tracking
	 * 
	 * @return
	 */
	public boolean usesDifferentclasses() {
		if (transactionItems == null) {
			return false;
		}
		long classCode = -1;
		for (ClientTransactionItem item : this.transactionItems) {
			if (item.referringTransactionItem != 0) {
				continue;
			}
			long code = item.getAccounterClass();
			if (classCode == -1) {
				classCode = code;
				continue;
			}
			if (code != classCode) {
				return true;
			}
		}
		return false;
	}

	public boolean haveTax() {
		if (transactionItems == null) {
			return false;
		}
		for (ClientTransactionItem item : this.transactionItems) {
			long code = item.getTaxCode();
			if (code != 0 && item.getReferringTransactionItem() == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean haveClass() {
		if (transactionItems == null) {
			return false;
		}
		for (ClientTransactionItem item : this.transactionItems) {
			long classCode = item.getAccounterClass();
			if (classCode != 0) {
				return true;
			}
		}
		return false;
	}

	public boolean hasJob() {
		return job != 0;
	}

	/**
	 * 
	 * @return
	 */
	public boolean haveDiscount() {
		if (transactionItems == null) {
			return false;
		}
		for (ClientTransactionItem item : this.transactionItems) {
			Double discount = item.getDiscount();
			if (discount != null && discount != 0
					&& item.getReferringTransactionItem() == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean usesDifferentDiscounts() {
		if (transactionItems == null) {
			return false;
		}
		Double discount = null;
		for (ClientTransactionItem item : this.transactionItems) {
			Double code = item.getDiscount();
			if (discount == null || item.getReferringTransactionItem() != 0) {
				discount = code;
				continue;
			}

			if (code != null && !DecimalUtil.isEquals(code, discount)) {
				return true;
			}
		}
		return false;
	}

	public List<ClientWareHouseAllocation> getWareHouseAllocations() {
		return wareHouseAllocations;
	}

	public void setWareHouseAllocations(
			List<ClientWareHouseAllocation> wareHouseAllocations) {
		this.wareHouseAllocations = wareHouseAllocations;
	}

	public long getTobeDeleteReminder() {
		return tobeDeleteReminder;
	}

	public void setTobeDeleteReminder(long tobeDeleteReminder) {
		this.tobeDeleteReminder = tobeDeleteReminder;
	}

	public Set<ClientAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<ClientAttachment> attachments) {
		this.attachments = attachments;
	}

	public boolean isTemplate() {
		return this.saveStatus == STATUS_TEMPLATE;
	}

	public ClientStatementRecord getStatementRecord() {
		return statementRecord;
	}

	public void setStatementRecord(ClientStatementRecord statementRecord) {
		this.statementRecord = statementRecord;
	}

	public boolean isDraft() {
		return this.saveStatus == STATUS_DRAFT;
	}

	public boolean isValidated() {
		return isValidated;
	}

	public void setValidated(boolean isValidated) {
		this.isValidated = isValidated;
	}

	public long getJob() {
		return job;
	}

	public void setJob(long job) {
		this.job = job;
	}

}
