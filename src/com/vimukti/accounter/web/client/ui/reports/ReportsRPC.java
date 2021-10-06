package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBuildAssembly;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientChequeLayout;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.ClientCustomerPrePayment;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientItemReceipt;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientPayEmployee;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayRun;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayTAX;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientReceiveVAT;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientStockAdjustment;
import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.core.ClientTAXAdjustment;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.ClientVendorPrePayment;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.CreditCardChargeAction;
import com.vimukti.accounter.web.client.ui.banking.MakeDepositAction;
import com.vimukti.accounter.web.client.ui.banking.NewReconcileAccountAction;
import com.vimukti.accounter.web.client.ui.banking.WriteChecksAction;
import com.vimukti.accounter.web.client.ui.company.CheckPrintSettingAction;
import com.vimukti.accounter.web.client.ui.company.CreditRatingListAction;
import com.vimukti.accounter.web.client.ui.company.InventoryActions;
import com.vimukti.accounter.web.client.ui.company.ManageSupportListAction;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.company.NewBudgetAction;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.company.NewJournalEntryAction;
import com.vimukti.accounter.web.client.ui.company.NewSalesperSonAction;
import com.vimukti.accounter.web.client.ui.company.NewTAXAgencyAction;
import com.vimukti.accounter.web.client.ui.company.WarehouseActions;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.PayRollActions;
import com.vimukti.accounter.web.client.ui.core.RecurringTransactionDialogAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundAction;
import com.vimukti.accounter.web.client.ui.customers.NewCashSaleAction;
import com.vimukti.accounter.web.client.ui.customers.NewCreditsAndRefundsAction;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;
import com.vimukti.accounter.web.client.ui.customers.NewInvoiceAction;
import com.vimukti.accounter.web.client.ui.customers.NewQuoteAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivePaymentAction;
import com.vimukti.accounter.web.client.ui.fixedassets.NewFixedAssetAction;
import com.vimukti.accounter.web.client.ui.settings.InviteUserAction;
import com.vimukti.accounter.web.client.ui.settings.NewBrandThemeAction;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.client.ui.vat.AdjustTAXAction;
import com.vimukti.accounter.web.client.ui.vat.NewTAXCodeAction;
import com.vimukti.accounter.web.client.ui.vat.NewVatItemAction;
import com.vimukti.accounter.web.client.ui.vat.PayTAXAction;
import com.vimukti.accounter.web.client.ui.vat.ReceiveVATAction;
import com.vimukti.accounter.web.client.ui.vat.TDSChalanDetailsAction;
import com.vimukti.accounter.web.client.ui.vendors.CashExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.DepositAction;
import com.vimukti.accounter.web.client.ui.vendors.EmployeeExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.EnterBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.IssuePaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCashPurchaseAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCreditMemoAction;
import com.vimukti.accounter.web.client.ui.vendors.NewItemReceiptAction;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;
import com.vimukti.accounter.web.client.ui.vendors.PayBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsAction;

public class ReportsRPC {

	protected static <T extends IAccounterCore, A extends Action> void initCallBack(
			T c, final A a, final long id) {
		AccounterAsyncCallback<T> callback = new AccounterAsyncCallback<T>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showMessage(Global.get().messages().sessionExpired());
			}

			@Override
			public void onResultSuccess(T result) {
				if (result != null) {
					UIUtils.runAction(result, a, true);
				}
			}

		};
		Accounter.createGETService().getObjectById(c.getObjectType(), id,
				callback);
	}

	public static void getTaxAgency(long id) {
		UIUtils.runAction(Accounter.getCompany().getTaxAgency(id),
				new NewTAXAgencyAction());

	}

	public static void getVendor(String name) {
		UIUtils.runAction(Accounter.getCompany().getVendorByName(name),
				new NewVendorAction());
	}

	public static void openTransactionView(int transactionType,
			long transactionId) {
		if (Accounter.getUser().getUserRole()
				.equalsIgnoreCase(RolePermissions.READ_ONLY)) {
			return;
		}
		switch (transactionType) {

		case ClientTransaction.TYPE_PAY_BILL:
			initCallBack(new ClientPayBill(), new PayBillsAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			initCallBack(new ClientVendorPrePayment(),
					new VendorPaymentsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			initCallBack(new ClientTransferFund(), new MakeDepositAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			initCallBack(new ClientEnterBill(), new EnterBillsAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			initCallBack(new ClientCashPurchase(), new NewCashPurchaseAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			initCallBack(new ClientCashSales(), new NewCashSaleAction(),
					transactionId);
		case IAccounterCore.RECURING_TRANSACTION:
			initCallBack(new ClientRecurringTransaction(),
					new RecurringTransactionDialogAction(), transactionId);
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			initCallBack(new ClientWriteCheck(), new WriteChecksAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			initCallBack(new ClientCustomerRefund(),
					new CustomerRefundAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			initCallBack(new ClientCustomerCreditMemo(),
					new NewCreditsAndRefundsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			initCallBack(new ClientReceivePayment(),
					new ReceivePaymentAction(), transactionId);
			break;
		case ClientTransaction.TYPE_INVOICE:
			initCallBack(new ClientInvoice(), new NewInvoiceAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			initCallBack(new ClientCreditCardCharge(),
					new CreditCardChargeAction(), transactionId);
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			initCallBack(new ClientEstimate(), new NewQuoteAction(0),
					transactionId);
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			initCallBack(new ClientIssuePayment(), new IssuePaymentsAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			initCallBack(new ClientVendorCreditMemo(),
					new NewCreditMemoAction(), transactionId);
			break;
		case ClientTransaction.TYPE_PAY_TAX:

			initCallBack(new ClientPayTAX(), new PayTAXAction(), transactionId);
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			initCallBack(new ClientJournalEntry(), new NewJournalEntryAction(),
					transactionId);
			break;

		case ClientTransaction.TYPE_PURCHASE_ORDER:
			initCallBack(new ClientPurchaseOrder(), new PurchaseOrderAction(),
					transactionId);
			break;

		case ClientTransaction.TYPE_ITEM_RECEIPT:
			initCallBack(new ClientItemReceipt(), new NewItemReceiptAction(),
					transactionId);
			break;

		case ClientTransaction.TYPE_CASH_EXPENSE:
			initCallBack(new ClientCashPurchase(), new CashExpenseAction(),
					transactionId);
			break;

		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			initCallBack(new ClientCreditCardCharge(),
					new CreditCardExpenseAction(), transactionId);
			break;

		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			initCallBack(new ClientCashPurchase(), new EmployeeExpenseAction(),
					transactionId);
			break;

		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			initCallBack(new ClientCustomerPrePayment(),
					new CustomerPaymentsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_RECEIVE_TAX:
			initCallBack(new ClientReceiveVAT(), new ReceiveVATAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_ADJUST_SALES_TAX:
		case ClientTransaction.TYPE_ADJUST_VAT_RETURN:
			initCallBack(new ClientTAXAdjustment(), new AdjustTAXAction(2),
					transactionId);
			break;
		case ClientTransaction.TYPE_TDS_CHALLAN:
			initCallBack(new ClientTDSChalanDetail(),
					new TDSChalanDetailsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			initCallBack(new ClientMakeDeposit(), new DepositAction(),
					transactionId);
			break;

		// These cases were included to open the views other than transactions.
		case IAccounterCore.ACCOUNT:
			initCallBack(new ClientAccount(), new NewAccountAction(),
					transactionId);
			break;
		case IAccounterCore.TAXGROUP:
			initCallBack(new ClientTAXGroup(),
					ManageSupportListAction.salesTaxGroups(), transactionId);
			break;
		case IAccounterCore.TAXITEM:
			initCallBack(new ClientTAXItem(), new NewVatItemAction(),
					transactionId);
			break;
		case IAccounterCore.TAXAGENCY:
			initCallBack(new ClientTAXAgency(), new NewTAXAgencyAction(),
					transactionId);
			break;
		case IAccounterCore.CUSTOMER_GROUP:
			initCallBack(new ClientCustomerGroup(),
					ManageSupportListAction.customerGroups(), transactionId);
			break;
		case IAccounterCore.VENDOR_GROUP:
			initCallBack(new ClientVendorGroup(),
					ManageSupportListAction.vendorGroups(), transactionId);
			break;
		case IAccounterCore.PAYMENT_TERMS:
			initCallBack(new ClientPaymentTerms(),
					ManageSupportListAction.paymentTerms(), transactionId);
			break;
		case IAccounterCore.SHIPPING_METHOD:
			initCallBack(new ClientShippingMethod(),
					ManageSupportListAction.shippingMethods(), transactionId);
			break;
		case IAccounterCore.SHIPPING_TERMS:
			initCallBack(new ClientShippingTerms(),
					ManageSupportListAction.shippingTerms(), transactionId);
			break;
		case IAccounterCore.ITEM_GROUP:
			initCallBack(new ClientItemGroup(),
					ManageSupportListAction.itemGroups(), transactionId);
			break;
		case IAccounterCore.CREDIT_RATING:
			initCallBack(new ClientCreditRating(),
					new CreditRatingListAction(), transactionId);
			break;
		case IAccounterCore.CURRENCY:
			initCallBack(new ClientCurrency(),
					ManageSupportListAction.currencyGroups(), transactionId);
			break;
		case IAccounterCore.ITEM:
			initCallBack(new ClientItem(), new NewItemAction(true),
					transactionId);
			break;
		case IAccounterCore.ASSEMBLY:
			initCallBack(new ClientInventoryAssembly(),
					InventoryActions.newAssembly(), transactionId);
			break;
		case IAccounterCore.VENDOR:
			initCallBack(new ClientVendor(), new NewVendorAction(),
					transactionId);
			break;
		case IAccounterCore.CUSTOMER:
			initCallBack(new ClientCustomer(), new NewCustomerAction(),
					transactionId);
			break;
		case IAccounterCore.SALES_PERSON:
			initCallBack(new ClientSalesPerson(), new NewSalesperSonAction(),
					transactionId);
			break;
		case IAccounterCore.TAXCODE:
			initCallBack(new ClientTAXCode(), new NewTAXCodeAction(),
					transactionId);
			break;
		case IAccounterCore.STOCK_ADJUSTMENT:
			initCallBack(new ClientStockAdjustment(),
					InventoryActions.stockAdjustment(), transactionId);
			break;
		case IAccounterCore.WAREHOUSE:
			initCallBack(new ClientWarehouse(),
					WarehouseActions.newWarehouse(), transactionId);
			break;
		case IAccounterCore.STOCK_TRANSFER:
			initCallBack(new ClientStockTransfer(),
					WarehouseActions.warehouseTransfer(), transactionId);
			break;
		case IAccounterCore.MEASUREMENT:
			initCallBack(new ClientMeasurement(),
					InventoryActions.measurement(), transactionId);
			break;
		case IAccounterCore.USER:
			initCallBack(new ClientUser(), new InviteUserAction(),
					transactionId);
			break;
		case IAccounterCore.BRANDING_THEME:
			initCallBack(new ClientBrandingTheme(), new NewBrandThemeAction(),
					transactionId);
			break;
		case IAccounterCore.LOCATION:
			initCallBack(new ClientLocation(),
					ManageSupportListAction.locations(), transactionId);
			break;
		case IAccounterCore.ACCOUNTER_CLASS:
			initCallBack(new ClientAccounterClass(),
					ManageSupportListAction.classes(), transactionId);
			break;
		case IAccounterCore.BANK_ACCOUNT:
			initCallBack(new ClientBankAccount(), new NewAccountAction(
					ClientAccount.TYPE_BANK), transactionId);
			break;
		case IAccounterCore.FIXED_ASSET:
			initCallBack(new ClientFixedAsset(), new NewFixedAssetAction(),
					transactionId);
			break;
		case IAccounterCore.BUDGET:
			initCallBack(new ClientBudget(), new NewBudgetAction(),
					transactionId);
			break;
		case IAccounterCore.RECONCILIATION:
			initCallBack(new ClientReconciliation(),
					new NewReconcileAccountAction(), transactionId);
			break;
		case IAccounterCore.TDSCHALANDETAIL:
			initCallBack(new ClientTDSChalanDetail(),
					new TDSChalanDetailsAction(), transactionId);
			break;
		case IAccounterCore.EMPLOYEE:
			initCallBack(new ClientEmployee(),
					PayRollActions.newEmployeeAction(), transactionId);
			break;
		case ClientTransaction.TYPE_STOCK_ADJUSTMENT:
			initCallBack(new ClientStockAdjustment(),
					InventoryActions.stockAdjustment(), transactionId);
			break;
		case ClientTransaction.TYPE_BUILD_ASSEMBLY:
			initCallBack(new ClientBuildAssembly(),
					InventoryActions.buildAssembly(), transactionId);
			break;
		case IAccounterCore.CHECK_LAYOUT:
			initCallBack(new ClientChequeLayout(),
					new CheckPrintSettingAction(), transactionId);
			break;
		case IAccounterCore.PAYROLL_UNIT:
			initCallBack(new ClientPayrollUnit(),
					PayRollActions.newPayRollUnitAction(), transactionId);
			break;
		case IAccounterCore.PAY_HEAD:
			initCallBack(new ClientPayHead(),
					PayRollActions.newPayHeadAction(), transactionId);
			break;
		case IAccounterCore.PAY_STRUCTURE:
			initCallBack(new ClientPayStructure(),
					PayRollActions.newPayStructureAction(), transactionId);
			break;
		case ClientTransaction.TYPE_PAY_RUN:
			initCallBack(new ClientPayRun(), PayRollActions.newPayRunAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_PAY_EMPLOYEE:
			initCallBack(new ClientPayEmployee(),
					PayRollActions.newPayEmployeeAction(), transactionId);
			break;
		case IAccounterCore.ATTENDANCE_PRODUCTION_TYPE:
			PayRollActions newAttendanceProductionTypeAction = PayRollActions
					.newAttendanceProductionTypeAction();
			newAttendanceProductionTypeAction
					.setCallback(new ActionCallback<ClientAttendanceOrProductionType>() {

						@Override
						public void actionResult(
								ClientAttendanceOrProductionType result) {
						}
					});
			initCallBack(new ClientAttendanceOrProductionType(),
					newAttendanceProductionTypeAction, transactionId);
			break;
		case IAccounterCore.EMPLOYEE_GROUP:
			initCallBack(new ClientEmployeeGroup(),
					PayRollActions.newEmployeeGroupAction(), transactionId);
			break;
		case IAccounterCore.JOB:
			initCallBack(new ClientJob(), ActionFactory.getNewJobAction(null),
					transactionId);
			break;
		}

	}

	public static void openTransactionView(ClientTransaction transaction) {
		if (Accounter.getUser().getUserRole().equals(RolePermissions.READ_ONLY)) {
			return;
		}
		switch (transaction.getType()) {

		case ClientTransaction.TYPE_TRANSFER_FUND:
			new MakeDepositAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			new EnterBillsAction().run((ClientEnterBill) transaction, false);
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			new NewCashPurchaseAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			new NewCashSaleAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			new WriteChecksAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			new NewCreditsAndRefundsAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_INVOICE:
			new NewInvoiceAction().run((ClientInvoice) transaction, false);
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			new NewQuoteAction(0).run(transaction, false);
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			new NewCreditMemoAction().run(transaction, false);
			break;

		case ClientTransaction.TYPE_CASH_EXPENSE:
			new CashExpenseAction().run(transaction, false);
			break;

		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			new CreditCardExpenseAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			new NewJournalEntryAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			new CustomerRefundAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			new DepositAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_PURCHASE_ORDER:
			new PurchaseOrderAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_BUILD_ASSEMBLY:
			InventoryActions.buildAssembly().run(transaction, false);
			break;
		case ClientTransaction.TYPE_PAY_RUN:
			PayRollActions.newPayRunAction().run((ClientPayRun) transaction,
					false);
			break;
		case ClientTransaction.TYPE_PAY_EMPLOYEE:
			PayRollActions.newPayEmployeeAction().run(transaction, false);
			break;
		}
	}
}
