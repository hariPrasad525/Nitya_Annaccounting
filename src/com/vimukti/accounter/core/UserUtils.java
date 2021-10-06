package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class UserUtils {

	private static boolean canDoManageAccounts;
	private static boolean canDoBankReconcialiation;
	private static boolean canDoCompanySettings;
	private static boolean canDoInvoiceBills;
	private static boolean canDoTaxTransactions;
	private static boolean canDoPayBillsPayments;
	private static boolean canDoManageUsers;
	private static boolean canDoInvendoryWarehouse;
	private static boolean canDoSaveAsDrafts;
	private static boolean canDoInvoiceandPayments;

	public static boolean canDoThis(Class<?> clas) {
		User user = AccounterThreadLocal.get();

		initFileds(user);

		if (clas.equals(Account.class)) {
			if (canDoManageAccounts) {
				return true;
			}
		} else if (clas.equals(BankAccount.class)) {
			if (canDoBankReconcialiation) {
				return true;
			}
		} else if (clas.equals(AccounterClass.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(AccountTransaction.class)) {
			if (canDoManageAccounts) {
				return true;
			}
		} else if (clas.equals(Bank.class)) {
			if (canDoBankReconcialiation) {
				return true;
			}
		} else if (clas.equals(BrandingTheme.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(Budget.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(Currency.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(CustomerGroup.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(CustomField.class)) {
			if (canDoInvoiceBills || canDoInvoiceandPayments) {
				return true;
			}
		} else if (clas.equals(FiscalYear.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(FixedAsset.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(FixedAssetHistory.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(Item.class)) {
			if (canDoInvoiceBills || canDoInvoiceandPayments
					|| canDoInvendoryWarehouse) {
				return true;
			}
		} else if (clas.equals(ItemGroup.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(Location.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(Measurement.class)) {
			if (canDoInvoiceBills || canDoInvendoryWarehouse) {
				return true;
			}
		} else if (clas.equals(Customer.class)) {
			if (canDoInvoiceBills || canDoInvoiceandPayments) {
				return true;
			}
		} else if (clas.equals(Vendor.class)) {
			if (canDoInvoiceBills) {
				return true;
			}
		} else if (clas.equals(TAXAgency.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(PaymentTerms.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(Reconciliation.class)) {
			if (canDoManageAccounts) {
				return true;
			}
		} else if (clas.equals(ReconciliationItem.class)) {
			if (canDoManageAccounts) {
				return true;
			}
		} else if (clas.equals(RecurringTransaction.class)) {
			if (canDoInvoiceBills) {
				return true;
			}
		} else if (clas.equals(SalesPerson.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(ShippingMethod.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(ShippingTerms.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(StockAdjustment.class)) {
			if (canDoInvendoryWarehouse) {
				return true;
			}
		} else if (clas.equals(StockTransfer.class)) {
			if (canDoInvendoryWarehouse) {
				return true;
			}
		} else if (clas.equals(TAXCode.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(TAXItemGroup.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(TAXGroup.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(TAXItem.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(TDSChalanDetail.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(TDSTransactionItem.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(CashPurchase.class)) {
			if (canDoInvoiceBills || canDoInvoiceandPayments) {
				return true;
			}
		} else if (clas.equals(CashSales.class)) {
			if (canDoInvoiceBills || canDoInvoiceandPayments) {
				return true;
			}
		} else if (clas.equals(CreditCardCharge.class)) {
			if (canDoBankReconcialiation || canDoManageAccounts
					|| canDoSaveAsDrafts) {
				return true;
			}
		} else if (clas.equals(CustomerCreditMemo.class)) {
			if (canDoInvoiceBills || canDoInvoiceandPayments) {
				return true;
			}
		} else if (clas.equals(CustomerPrePayment.class)) {
			if (canDoPayBillsPayments || canDoInvoiceandPayments) {
				return true;
			}
		} else if (clas.equals(CustomerRefund.class)) {
			if (canDoPayBillsPayments || canDoSaveAsDrafts
					|| canDoInvoiceandPayments) {
				return true;
			}
		} else if (clas.equals(MakeDeposit.class)) {
			if (canDoBankReconcialiation || canDoManageAccounts) {
				return true;
			}
		} else if (clas.equals(EnterBill.class)) {
			if (canDoInvoiceBills) {
				return true;
			}
		} else if (clas.equals(Estimate.class)) {
			if (canDoInvoiceBills || canDoInvoiceandPayments) {
				return true;
			}
		} else if (clas.equals(Expense.class)) {
			if (canDoPayBillsPayments) {
				return true;
			}
		} else if (clas.equals(Invoice.class)) {
			if (canDoInvoiceBills || canDoInvoiceandPayments) {
				return true;
			}
		} else if (clas.equals(ItemReceipt.class)) {
			if (canDoInvoiceBills || canDoInvoiceandPayments) {
				return true;
			}
		} else if (clas.equals(JournalEntry.class)) {
			if (canDoManageAccounts) {
				return true;
			}
		} else if (clas.equals(TransferFund.class)) {
			if (canDoBankReconcialiation || canDoManageAccounts) {
				return true;
			}
		} else if (clas.equals(PayBill.class)) {
			if (canDoPayBillsPayments) {
				return true;
			}
		} else if (clas.equals(PayTAX.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(PayTDS.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(ReceivePayment.class)) {
			if (canDoPayBillsPayments || canDoInvoiceandPayments) {
				return true;
			}
		} else if (clas.equals(ReceiveVAT.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(TAXAdjustment.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(TAXReturn.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(VendorCreditMemo.class)) {
			if (canDoInvoiceBills) {
				return true;
			}
		} else if (clas.equals(WriteCheck.class)) {
			if (canDoBankReconcialiation) {
				return true;
			}
		} else if (clas.equals(TransactionPayBill.class)) {
			if (canDoPayBillsPayments) {
				return true;
			}
		} else if (clas.equals(Unit.class)) {
			if (canDoInvoiceBills || canDoInvendoryWarehouse) {
				return true;
			}
		} else if (clas.equals(User.class)) {
			if (canDoManageUsers) {
				return true;
			}
		} else if (clas.equals(VATReturnBox.class)) {
			if (canDoTaxTransactions) {
				return true;
			}
		} else if (clas.equals(VendorGroup.class)) {
			if (canDoCompanySettings) {
				return true;
			}
		} else if (clas.equals(Warehouse.class)) {
			if (canDoInvendoryWarehouse) {
				return true;
			}
		} else if (clas.equals(PurchaseOrder.class)) {
			if (canDoInvoiceBills) {
				return true;
			}
		}
		return false;
	}

	private static void initFileds(User user) {
		UserPermissions permissions = user.getPermissions();
		canDoManageAccounts = (permissions.getTypeOfManageAccounts() == RolePermissions.TYPE_YES);
		canDoBankReconcialiation = (permissions.getTypeOfBankReconcilation() == RolePermissions.TYPE_YES);
		canDoCompanySettings = (permissions.getTypeOfCompanySettingsLockDates() == RolePermissions.TYPE_YES);
		canDoInvoiceBills = (permissions.getTypeOfInvoicesBills() == RolePermissions.TYPE_YES || permissions
				.getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES);
		canDoTaxTransactions = (user.getUserRole()
				.equals(RolePermissions.ADMIN) || user.getUserRole().equals(
				RolePermissions.FINANCIAL_ADVISER));
		canDoPayBillsPayments = (permissions.getTypeOfPayBillsPayments() == RolePermissions.TYPE_YES);
		canDoManageUsers = user.isCanDoUserManagement();
		canDoInvendoryWarehouse = (permissions.getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES);
		canDoSaveAsDrafts = (permissions.getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES);
		canDoInvoiceandPayments = (permissions.getInvoicesAndPayments() == RolePermissions.TYPE_YES);
	}
}
