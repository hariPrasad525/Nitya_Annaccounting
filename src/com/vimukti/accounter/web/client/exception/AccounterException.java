/**
 * 
 */
package com.vimukti.accounter.web.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccounterException extends Exception implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Used to tell that same number is already used for another Transaction or
	 * Core object
	 */
	public static final int ERROR_NUMBER_CONFLICT = 1;
	/**
	 * Used to tell that same name is already used for another core object
	 */
	public static final int ERROR_NAME_CONFLICT = 2;
	/**
	 * Used to tell that this transaction is already done, and can not be done
	 * again
	 */
	public static final int ERROR_TRAN_CONFLICT = 3;

	/**
	 * Sent when user does not have sufficient permission to do this operation
	 */
	public static final int ERROR_PERMISSION_DENIED = 4;

	/**
	 * Used to tell that Exception in Database
	 */
	public static final int ERROR_INTERNAL = 5;

	/**
	 * Sent When Arguments given by the Client is Wrong
	 */
	public static final int ERROR_ILLEGAL_ARGUMENT = 6;
	public static final int ERROR_INVALID_USER_SESSION = 8;

	/**
	 * Used to tell that the object was deleted already.
	 */
	public static final int ERROR_NO_SUCH_OBJECT = 9;

	/**
	 * You can't void or edit because it has been deposited from Undeposited
	 * Funds
	 */
	public static final int ERROR_DEPOSITED_FROM_UNDEPOSITED_FUNDS = 10;

	/**
	 * You can't Edit it
	 */
	public static final int ERROR_CANT_EDIT = 11;

	/**
	 * You can't void it
	 */
	public static final int ERROR_CANT_VOID = 12;

	/**
	 * In the ReceivePayment write off or discount is used for this Invoice.
	 */
	public static final int ERROR_RECEIVE_PAYMENT_DISCOUNT_USED = 13;

	public static final int ERROR_OBJECT_IN_USE = 14;

	public static final int ERROR_VERSION_MISMATCH = 15;

	public static final int ERROR_EDITING_TRANSACTION_RECONCILIED = 16;

	public static final int USED_IN_INVOICE = 17;

	public static final int INVOICE_PAID_VOID_IT = 18;

	public static final int ERROR_CANT_EDIT_DELETE = 19;

	public static final int ERROR_CUSTOMER_NULL = 20;

	public static final int ERROR_VENDOR_NULL = 21;

	public static final int ERROR_TAX_CODE_NULL = 22;

	public static final int ERROR_ACCOUNT_NULL = 23;

	public static final int ERROR_TRANSACTION_ITEM_NULL = 24;

	public static final int ERROR_TRANSACTION_TOTAL_ZERO = 25;

	public static final int ERROR_AMOUNT_ZERO = 26;

	public static final int ERROR_PAY_FROM_NULL = 27;

	public static final int ERROR_PAY_TO_NULL = 28;

	public static final int ERROR_DEPOSIT_FROM_NULL = 29;

	public static final int ERROR_DEPOSIT_TO_NULL = 30;

	public static final int ERROR_PAYMENT_METHOD_NULL = 31;

	public static final int ERROR_BANK_ACCOUNT_NULL = 32;

	public static final int ERROR_CREDIT_DEBIT_TOTALS_NOT_EQUAL = 33;

	public static final int ERROR_INCOME_ACCOUNT_NULL = 34;

	public static final int ERROR_EXPENSE_ACCOUNT_NULL = 35;

	public static final int ERROR_CUSTOMER_NAME_EMPTY = 36;

	public static final int ERROR_CUSTOMER_NUMBER_EMPTY = 37;

	public static final int ERROR_VENDOR_NAME_EMPTY = 38;

	public static final int ERROR_VENDOR_NUMBER_EMPTY = 39;

	public static final int ERROR_THERE_IS_NO_TRANSACTION_ITEMS = 40;

	public static final int ERROR_ITEM_NAME_NULL = 41;

	public static final int ERROR_TRANSACTION_ITEM_TOTAL_0 = 42;

	public static final int ERROR_THERE_IS_NO_TRANSACTIONS_TO_ISSUE = 43;

	public static final int ERROR_NAME_NULL = 44;

	public static final int ERROR_NUMBER_NULL = 45;

	public static final int WRITECHECK_PAID_VOID_IT = 46;

	public static final int ERROR_INVOICE_USED_IN_ESTIMATES = 47;

	public static final int ERROR_DONT_HAVE_PERMISSION = 48;

	public static final int ERROR_ALREADY_DELETED = 49;

	public static final int ERROR_TAX_AGENCY_NULL = 50;

	public static final int ERROR_TAX_ENTRIES_EMPTY = 51;

	public static final int ERROR_NO_FILED_VAT_ENTRIES_TO_SAVE = 52;

	public static final int ERROR_VOIDING_TRANSACTION_RECONCILIED = 53;

	public static final int ERROR_DELETING_TRANSACTION_RECONCILIED = 54;

	public static final int ERROR_DELETING_DEFAULT_TAX_ITEM = 55;

	public static final int ERROR_DONT_HAVE_ANOTHER_ADMIN = 56;

	/**
	 * Codes belongs to Importers.
	 */

	public static final int ERROR_PLEASE_ENTER_OR_MAP = 57;

	public static final int ERROR_NAME_ALREADY_EXIST = 58;

	public static final int ERROR_NUMBER_ALREADY_EXIST = 59;

	public static final int ERROR_DOES_NOT_EXIST_WITH_THIS_NUMBER = 60;

	public static final int ERROR_DOES_NOT_EXIST_WITH_THIS_NAME = 61;

	public static final int ERROR_PARENT_ACCOUNT_SHOULD_BE_SAME = 62;

	public static final int ERROR_PARENT_ACCOUNT_CURRENCY_SHOULD_BE_SAME = 63;

	public static final int ERROR_INVALID_EMAIL_ID = 64;

	public static final int ERROR_NEGATIVE_AMOUNT = 65;

	public static final int ERROR_CANT_CREATE_MORE_TRANSACTIONS = 66;

	public static final int ERROR_CANT_EDIT_MULTI_CURRENCY_TRANSACTION = 67;

	public static final int ERROR_OBJECT_NULL = 68;

	public static final int ERROR_QUANTITY_ZERO_OR_NEGATIVE = 69;

	public static final int ERROR_WAREHOUSE_CODE_NULL = 75;

	public static final int ERROR_TRANSACTION_PAYBILLS_NULL = 70;

	public static final int ERROR_DISCOUNT_GREATER_THAN_100 = 71;

	public static final int ERROR_MAKE_DEPOSIT_NULL = 72;

	public static final int ERROR_TRANSACTION_PAY_TAX_NULL = 73;

	public static final int ERROR_TRANSACTION_RECEIVE_VAT = 74;

	public static final int ERROR_LIST_EMPTY = 76;

	public static final int ERROR_RECIVE_PAYMENT_TOTAL_AMOUNT = 77;

	public static final int ERROR_NO_RECORDS_TO_SAVE = 78;

	public static final int ERROR_PLEASE_ENTER = 79;

	public static final int ERROR_SHOULD_NOT_SELECT_SAME_ACCOUNT_MULTIPLE_TIMES = 80;

	public static final int ERROR_NO_TRANSACTIONS_TO_FILE = 81;

	public static final int ERROR_AMOUNT_TO_PAY_ZERO = 82;

	public static final int ERROR_SELECT_PROPER_BANK_ACCOUNT = 83;

	public static final int ERROR_DUPLICATE_CONTACTS = 84;

	public static final int ERROR_PLEASE_SELECT = 85;

	public static final int ERROR_PERCENTAGE_GRATER_100 = 86;

	public static final int ERROR_PERCENTAGE_LESSTHAN_0 = 87;

	public static final int ERROR_DEPOSIT_AND_TRANSFER_SHOULD_DIFF = 88;

	public static final int ERROR_CURRENCY_MUST_BE_SAME = 89;

	public static final int ERROR_MUST_SELECT_CUSTOMER_FOR_BILLABLE = 90;

	public static final int ERROR_YOU_CANNOT_BUILD_WITH_OUT_COMPONENTS = 91;

	public static final int ERROR_ONLY_SELLABLE_ITEMS_CANBE_MARKED_AS_BILLABLE = 92;

	public static final int ERROR_PURCHASE_ORDERS_USED = 93;

	public static final int ERROR_DELETING_SYSTEM_ACCOUNT = 94;

	public static final int ERROR_ACTIVE_CUSTOMER = 95;

	public static final int ERROR_ACTIVE_VENDOR = 96;

	public static final int ERROR_ACTIVE_ACCOUNT = 97;

	public static final int ERROR_ACTIVE_ITEM = 98;

	public static final int ERROR_DATA_WRONG = 99;

	public static final int ERROR_CANT_CREATE_PAYRUN_DRAFT_OR_TEMPLATE = 100;

	public static final int ERROR_EMPLOYEE_NULL = 101;

	protected int errorCode;

	// private long id;

	/**
	 * Creates new Instance
	 */
	public AccounterException() {
	}

	/**
	 * Creates new Instance
	 */
	public AccounterException(String message) {
		super(message);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterException(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Creates new Instance
	 */
	public AccounterException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Creates new Instance
	 */
	public AccounterException(Throwable t, String message) {
		super(message, t);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterException(Throwable t) {
		super(t);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterException(int errorCode, Throwable e) {
		super(e);
		this.errorCode = errorCode;
	}

	/**
	 * @return
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param id
	 */
	// public void setID(long id) {
	// this.id = id;
	// }

	/**
	 * @return
	 */
	// public long getID() {
	// return this.id;
	// }
}
