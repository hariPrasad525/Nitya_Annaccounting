package com.nitya.accounter.text;

import java.util.HashMap;

import com.nitya.accounter.text.commands.CustomerCommand;
import com.nitya.accounter.text.commands.ITextCommand;
import com.nitya.accounter.text.commands.ItemCommand;
import com.nitya.accounter.text.commands.VendorCommand;
import com.nitya.accounter.text.commands.objectlists.CustomersCommand;
import com.nitya.accounter.text.commands.objectlists.InvoicesCommand;
import com.nitya.accounter.text.commands.objectlists.ReceivePaymentsCommand;
import com.nitya.accounter.text.commands.objectlists.VendorPaymentsCommand;
import com.nitya.accounter.text.commands.objectlists.VendorsCommand;
import com.nitya.accounter.text.commands.reports.BalanceSheetCommand;
import com.nitya.accounter.text.commands.reports.ProfitAndLossCommand;
import com.nitya.accounter.text.commands.reports.TrailBalanceCommand;
import com.nitya.accounter.text.commands.transaction.CashExpenseCommand;
import com.nitya.accounter.text.commands.transaction.CashSaleCommand;
import com.nitya.accounter.text.commands.transaction.CompanyCommand;
import com.nitya.accounter.text.commands.transaction.EnterBillCommand;
import com.nitya.accounter.text.commands.transaction.InvoiceCommand;
import com.nitya.accounter.text.commands.transaction.PayBillCommand;
import com.nitya.accounter.text.commands.transaction.PayCashCommand;
import com.nitya.accounter.text.commands.transaction.PayChequeCommand;
import com.nitya.accounter.text.commands.transaction.ReceiveCashCommand;
import com.nitya.accounter.text.commands.transaction.ReceiveChequeCommand;
import com.nitya.accounter.text.commands.transaction.ReceivePaymentCommand;

public class CommandsFactory {

	public static final String CUSTOMER = "customer";
	public static final String INVOICE = "invoice";
	public static final String VENDOR = "vendor";
	public static final String BILL = "bill";
	public static final String ITEM = "item";
	public static final String CASH_EXPENSE = "cashexpense";
	public static final String ACCOUNT = "account";

	static HashMap<String, Class<? extends ITextCommand>> commands = new HashMap<String, Class<? extends ITextCommand>>();
	static {
		init();
	}

	/**
	 * Getting the Command on Name
	 * 
	 * @param name
	 * @return {@link ITextCommand}
	 */
	public static Class<? extends ITextCommand> getCommand(String name) {
		if (name == null) {
			return null;
		}
		name = name.replace(" ", "").toLowerCase().trim();
		return commands.get(name);
	}

	private static void init() {

		addCommand(CustomerCommand.class, "customer", "createcustomer",
				"newcustomer", "updatecustomer");
		addCommand(InvoiceCommand.class, "invoice", "createinvoice",
				"newinvoice", "updateinvoice");
		addCommand(VendorCommand.class, "vendor", "createvendor", "newvendor",
				"updatevendor");
		addCommand(ItemCommand.class, "item", "createitem", "neweitem",
				"updateitem");
		addCommand(CashExpenseCommand.class, "cashexpense",
				"createcashexpense", "newcashexpense", "updatecashexpense");
		addCommand(CompanyCommand.class, "company", "createcompany",
				"newcompany", "updatecompany");
		addCommand(EnterBillCommand.class, "enterbill", "createenterbill",
				"newenterbill", "updateenterbill");
		addCommand(ReceivePaymentCommand.class, "receivepayment",
				"createreceivepayment", "newreceivepayment",
				"updatereceivepayment");
		addCommand(PayBillCommand.class, "paybill", "createpaybill",
				"newpaybill", "updatepaybill");
		addCommand(PayCashCommand.class, "paycash", "createpaycash",
				"newpaycash", "updatepaycash");
		addCommand(PayChequeCommand.class, "paycheque", "createpaycheque",
				"newpaycheque", "updatepaycheque");
		addCommand(ReceiveCashCommand.class, "receivecash",
				"createreceivecash", "newreceivecash", "updatereceivecash");
		addCommand(ReceiveChequeCommand.class, "receivecheque",
				"createreceivecheque", "newreceivecheque",
				"updatereceivecheque");
		// Reports
		addCommand(TrailBalanceCommand.class, "trailbalance");
		addCommand(ProfitAndLossCommand.class, "profitandloss");
		addCommand(BalanceSheetCommand.class, "balancesheet");
		// Object Lists
		addCommand(CustomersCommand.class, "customers");
		addCommand(VendorsCommand.class, "vendors");
		addCommand(VendorPaymentsCommand.class, "vendorpayments");
		addCommand(InvoicesCommand.class, "invoices");
		addCommand(ReceivePaymentsCommand.class, "receivepayments");
		addCommand(CashSaleCommand.class, "cashsale", "updatecashsale",
				"createcashsale");

	}

	private static void addCommand(Class<? extends ITextCommand> cls,
			String... keys) {
		for (String key : keys) {
			commands.put(key, cls);
		}
	}
}
