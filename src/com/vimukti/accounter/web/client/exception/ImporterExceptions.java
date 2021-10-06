package com.vimukti.accounter.web.client.exception;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * @author Devaraju.K
 * 
 */
public class ImporterExceptions {

	public static AccounterMessages accounterMessages = (AccounterMessages) GWT
			.create(AccounterMessages.class);

	public static String getErrorString(int errorCode) {
		switch (errorCode) {
		case AccounterException.ERROR_NUMBER_CONFLICT:
			return accounterMessages.numberConflict();

		case AccounterException.ERROR_NAME_CONFLICT:
			return accounterMessages.nameConflict();

		case AccounterException.ERROR_TRAN_CONFLICT:
			return accounterMessages.transactionConflict();

		case AccounterException.ERROR_PERMISSION_DENIED:
			return accounterMessages.permissionDenied();

		case AccounterException.ERROR_INTERNAL:
			return accounterMessages.internal();

		case AccounterException.ERROR_ILLEGAL_ARGUMENT:
			return accounterMessages.illegalArgument();

		case AccounterException.ERROR_NO_SUCH_OBJECT:
			return accounterMessages.cantVoid();

		case AccounterException.ERROR_DEPOSITED_FROM_UNDEPOSITED_FUNDS:
			return accounterMessages.depositedFromUndepositedFunds();

		case AccounterException.ERROR_CANT_EDIT:
			return accounterMessages.cantEdit();

		case AccounterException.ERROR_CANT_VOID:
			return accounterMessages.cantVoid();

		case AccounterException.ERROR_RECEIVE_PAYMENT_DISCOUNT_USED:
			return accounterMessages.receivePaymentDiscountUsed();

		case AccounterException.ERROR_OBJECT_IN_USE:
			return accounterMessages.objectInUse();

		case AccounterException.ERROR_VERSION_MISMATCH:
			return accounterMessages.objectModified();
		case AccounterException.ERROR_VOIDING_TRANSACTION_RECONCILIED:
			return accounterMessages.errorVoidingTransactionReconcilied();
		case AccounterException.USED_IN_INVOICE:
			return accounterMessages.usedinInvoiceSoYoucantEdit();
		case AccounterException.INVOICE_PAID_VOID_IT:
			return accounterMessages.usedinReceivepayYoucantEdit();

		case AccounterException.ERROR_CANT_EDIT_DELETE:
			return accounterMessages.cantEditOrDelete();

		case AccounterException.ERROR_CUSTOMER_NULL:
			return accounterMessages.pleaseSelect(Global.get().Customer());

		case AccounterException.ERROR_VENDOR_NULL:
			return accounterMessages.pleaseSelect(Global.get().Vendor());

		case AccounterException.ERROR_TAX_CODE_NULL:
			return accounterMessages.pleaseSelect(accounterMessages.taxCode());

		case AccounterException.ERROR_ACCOUNT_NULL:
			return accounterMessages.pleaseSelect(accounterMessages.Account());

		case AccounterException.ERROR_TRANSACTION_ITEM_NULL:
			return accounterMessages.pleaseSelect(accounterMessages
					.transactionItem());

		case AccounterException.ERROR_TRANSACTION_TOTAL_ZERO:
			return accounterMessages.transactionitemtotalcannotbe0orlessthan0();

		case AccounterException.ERROR_AMOUNT_ZERO:
			return accounterMessages
					.shouldNotbeZero(accounterMessages.amount());

		case AccounterException.ERROR_PAY_FROM_NULL:
			return accounterMessages.pleaseSelect(accounterMessages.payFrom());

		case AccounterException.ERROR_PAY_TO_NULL:
			return accounterMessages.pleaseSelect(accounterMessages.payTo());

		case AccounterException.ERROR_DEPOSIT_FROM_NULL:
			return accounterMessages.pleaseSelect(accounterMessages
					.transferFrom());

		case AccounterException.ERROR_DEPOSIT_TO_NULL:
			return accounterMessages.pleaseSelect(accounterMessages
					.transferTo());

		case AccounterException.ERROR_PAYMENT_METHOD_NULL:
			return accounterMessages.pleaseSelect(accounterMessages
					.paymentMethod());

		case AccounterException.ERROR_BANK_ACCOUNT_NULL:
			return accounterMessages.pleaseSelect(accounterMessages
					.bankAccount());

		case AccounterException.ERROR_CREDIT_DEBIT_TOTALS_NOT_EQUAL:
			return accounterMessages.totalMustBeSame();

		case AccounterException.ERROR_INCOME_ACCOUNT_NULL:
			return accounterMessages.pleaseSelect(accounterMessages
					.incomeAccount());

		case AccounterException.ERROR_EXPENSE_ACCOUNT_NULL:
			return accounterMessages.pleaseSelect(accounterMessages
					.expenseAccount());

		case AccounterException.ERROR_CUSTOMER_NAME_EMPTY:
			return accounterMessages.pleaseEnterName(Global.get().Customer());

		case AccounterException.ERROR_CUSTOMER_NUMBER_EMPTY:
			return accounterMessages.pleaseEnter(accounterMessages
					.payeeNumber(Global.get().Customer()));

		case AccounterException.ERROR_VENDOR_NAME_EMPTY:
			return accounterMessages.pleaseEnterName(Global.get().Vendor());

		case AccounterException.ERROR_VENDOR_NUMBER_EMPTY:
			return accounterMessages.pleaseEnter(accounterMessages
					.payeeNumber(Global.get().Vendor()));
		case AccounterException.ERROR_THERE_IS_NO_TRANSACTION_ITEMS:
			return accounterMessages.thereAreNoTransactionItemsToSave();

		case AccounterException.ERROR_ITEM_NAME_NULL:
			return accounterMessages.pleaseEnterName(accounterMessages.item());

		case AccounterException.ERROR_TRANSACTION_ITEM_TOTAL_0:
			return accounterMessages.transactionitemtotalcannotbe0orlessthan0();

		case AccounterException.WRITECHECK_PAID_VOID_IT:
			return accounterMessages.writeCheckPaid();

		case AccounterException.ERROR_INVOICE_USED_IN_ESTIMATES:
			return accounterMessages.invoiceUsedInEstimates();

		case AccounterException.ERROR_NAME_NULL:
			return accounterMessages.nameShouldnotbeempty();

		case AccounterException.ERROR_NUMBER_NULL:
			return accounterMessages.numberShouldNotBeEmptyOr0();

		case AccounterException.ERROR_DONT_HAVE_PERMISSION:
			return accounterMessages.youDontHavePermission();

		case AccounterException.ERROR_ALREADY_DELETED:
			return accounterMessages.objectAlreadyDeleted();

		case AccounterException.ERROR_TAX_AGENCY_NULL:
			return accounterMessages.pleaseSelectTAXAgencyToPayTAX();

		case AccounterException.ERROR_TAX_ENTRIES_EMPTY:
			return accounterMessages.pleaseSelectAtLeastOneRecord();

		case AccounterException.ERROR_NO_FILED_VAT_ENTRIES_TO_SAVE:
			return accounterMessages.errorNoFiledVatEntriesToSave();

		case AccounterException.ERROR_DELETING_TRANSACTION_RECONCILIED:
			return accounterMessages.errorDeletingTransactionReconcilied();

		case AccounterException.ERROR_EDITING_TRANSACTION_RECONCILIED:
			return accounterMessages.errorEditingTransactionReconcilied();

		case AccounterException.ERROR_DELETING_DEFAULT_TAX_ITEM:
			return accounterMessages.youcannotEditDeleteDefaultTaxItem();

		case AccounterException.ERROR_CANT_CREATE_MORE_TRANSACTIONS:
			return accounterMessages.youcannotCreateMoreTransactions();

		case AccounterException.ERROR_CANT_EDIT_MULTI_CURRENCY_TRANSACTION:
			return accounterMessages.youcannotEditMultiCurrencyTransaction();

		default:
			return null;
		}

	}
}
