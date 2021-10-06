package com.nitya.accounter.web.client.images;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.MenuBar.Resources;

public interface FinanceMenuImages extends Resources {

	// Images in 'Company' Menu
	ImageResource Accounts();

	ImageResource items();

	@Source("new_journal_entry.png")
	ImageResource newJournalEntry();

	@Source("new_account.png")
	ImageResource newAccount();

	ImageResource preferences();

	ImageResource customers();

	ImageResource vendors();

	@Source("Manage_Sales_Tax_Group.png")
	ImageResource manageSalesTaxGroup();

	@Source("New_Tax_Agency.png")
	ImageResource newTaxAgency();

	@Source("Pay_Sales_Tax.png")
	ImageResource paySalesTax();

	@Source("Sales_Tax.png")
	ImageResource salesTaxCode();

	@Source("Payment_term.png")
	ImageResource paymentTermsList();

	@Source("Shipping-Method.png")
	ImageResource shippingMethodsList();

	@Source("Shipping_term_icon.png")
	ImageResource shippingTermList();

	@Source("Price_Level_list.png")
	ImageResource priceLevelList();

	@Source("Creadit_rating_List.png")
	ImageResource creditRatingList();

	@Source("Jonoul_entries.png")
	ImageResource journalEntriesList();

	@Source("item_Recpit.png")
	ImageResource itemReciept();

	@Source("manage_fiscal_year.png")
	ImageResource manageFiscalYear();

	@Source("chart_of_accounts.png")
	ImageResource cahrtOfAccounts();

	ImageResource payments();

	// VATMenu Images

	@Source("Creating_taxes.png")
	ImageResource createTaxes();

	@Source("VATReturn.png")
	ImageResource vatReturn();

	@Source("File_vat.png")
	ImageResource fileVAT();

	@Source("Vat_adjustment.png")
	ImageResource vatAdjustment();

	@Source("Vat_group.png")
	ImageResource vatGroup();

	@Source("Vat_item.png")
	ImageResource newVatItem();

	// Images in 'Customer' menu
	@Source("customers_home.png")
	ImageResource customersHome();

	@Source("new_customer.png")
	ImageResource newCustomer();

	@Source("new_item.png")
	ImageResource newItem();

	@Source("new_quote.png")
	ImageResource newQuote();

	@Source("enter_Invoice.png")
	ImageResource enterInvoice();

	@Source("new_invoice.png")
	ImageResource newInvoice();

	@Source("new_cash_sale.png")
	ImageResource newCashSale();

	@Source("new_credit_and_refunds.png")
	ImageResource newCreditsAndRefunds();

	@Source("recive_payments.png")
	ImageResource newReceivePayment();

	@Source("customer_refunds.png")
	ImageResource newCustomerRefund();

	ImageResource quotes();

	ImageResource invoices();

	@Source("receivePayment.png")
	ImageResource receivedPayment();

	@Source("recived_payment_list.png")
	ImageResource receivedPayments();

	@Source("customer_credit.png")
	ImageResource customerCredit();

	@Source("customer_refunds_list.png")
	ImageResource customerRefundsList();

	// Images for 'Vendor' Menu
	@Source("vendor_home.png")
	ImageResource vendorHome();

	@Source("new_vendor.png")
	ImageResource newVendor();

	@Source("new_cash_purchase.png")
	ImageResource newCashPurchage();

	@Source("supplierCredit.png")
	ImageResource newSupplierCredit();

	@Source("new_credit_memo.png")
	ImageResource newVendorCreditMemo();

	@Source("new_check.png")
	ImageResource newCheck();

	@Source("EnterBill.png")
	ImageResource newEnterBill();

	@Source("enter_bills.png")
	ImageResource newBill();

	@Source("payBill.png")
	ImageResource newPayBill();

	@Source("pay_bills.png")
	ImageResource payBill();

	@Source("issue_payment.png")
	ImageResource issuePayment();

	@Source("vendor_payments.png")
	ImageResource newVendorPayment();

	@Source("enterExpenses.png")
	ImageResource enterExpenses();

	@Source("record_expenses.png")
	ImageResource recordExpenses();

	ImageResource bills();

	ImageResource vendorpayments();

	// Images for 'Banking' menu

	@Source("banking_home.png")
	ImageResource bankingHome();

	@Source("new_bank_account.png")
	ImageResource newBankAccount();

	@Source("bank_Deposit.png")
	ImageResource bankDeposit();

	@Source("make_deposit.png")
	ImageResource makeDeposit();

	@Source("transfer_funds.png")
	ImageResource transerFunds();

	@Source("Creditcardpurchases.png")
	ImageResource creditCardPurchases();

	@Source("credit_card_charge.png")
	ImageResource newCreditCardCharge();

	// Images for FixedAsset

	@Source("New Fixed Asset.png")
	ImageResource newFixedAsset();

	ImageResource Depreciation();

	@Source("pending_items_list.png")
	ImageResource pendingItemsList();

	@Source("Registered_Items_List.png")
	ImageResource registeredItemsList();

	@Source("Sold_Disposed_Items_List.png")
	ImageResource soldDisposedItemsList();

	// Images for 'Reports' menu

	@Source("report_home.png")
	ImageResource reportsHome();

	@Source("profit_loss.png")
	ImageResource profitAndLose();

	ImageResource reports();

	ImageResource comments();

	ImageResource salestax();

	ImageResource delete();

	@Source("find.png")
	ImageResource accounterRegisterIcon();

	@Source("Purchase-order-list.png")
	ImageResource purchaseOrderList();

	@Source("Purchase-order.png")
	ImageResource purchaseOrder();

	@Source("purchaseOrderReport.png")
	ImageResource purchaseOrderReport();

	@Source("Sales-order-list.png")
	ImageResource salesOrderList();

	@Source("Sales-order.png")
	ImageResource salesOrder();

	@Source("salesOrderReport.png")
	ImageResource salesOrderReport();

	@Source("Vat_item.png")
	ImageResource newTaxItem();
}
