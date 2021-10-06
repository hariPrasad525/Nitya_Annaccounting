package com.nitya.accounter.text.commands.objectlists;

import java.util.ArrayList;

import org.hibernate.Session;

import com.nitya.accounter.core.CashPurchase;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.Invoice;
import com.nitya.accounter.core.TransactionItem;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.text.commands.transaction.AbstractTransactionCommand;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti10
 * 
 */
public class CreditCardExpensesCommand extends AbstractTransactionCommand {

	private FinanceDate transactionDate;
	private String number;
	private String vendorName;
	private String payfrom;
	private String memo;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {

		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}

		if (!parseTransactionDate(data, respnse)) {
			return true;
		}

		vendorName = data.nextString("");

		payfrom = data.nextString("");

		if (!parseTransactionItem(data, respnse)) {
			return true;
		}
		memo = data.nextString(null);

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		CashPurchase expense = getObject(Invoice.class, "number", number);
		if (expense == null) {
			expense = new CashPurchase();
		}

		Vendor vendor = getObject(Vendor.class, "name", vendorName);
		if (vendor == null) {
			vendor = new Vendor();
			vendor.setName(this.vendorName);
			session.save(vendor);
		}
		expense.setType(CashPurchase.TYPE_CREDIT_CARD_EXPENSE);
		expense.setNumber(number);
		expense.setDate(transactionDate);
		ArrayList<TransactionItem> processTransactionItem = processVendorTransactionItem();
		expense.setTransactionItems(processTransactionItem);
		if (memo != null) {
			expense.setMemo(memo);
		}
		// TODO PayFrom
		// getting Transaction
		double total = getTransactionTotal(processTransactionItem);
		expense.setTotal(total);

		saveOrUpdate(expense);
	}

}
