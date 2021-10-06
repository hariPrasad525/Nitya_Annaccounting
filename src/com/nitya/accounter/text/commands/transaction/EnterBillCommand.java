package com.nitya.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.nitya.accounter.core.EnterBill;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.TransactionItem;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * 
 * Number,TransactionDate,vendorName,dueDate,deliveryDate,ItemName,Description,
 * UnitPrice,Quantity, Tax
 * ==========================================================================
 * Number
 * ,TransactionDate,vendorName,dueDate,deliveryDate,AccountName,Description
 * ,Amount,Tax
 * 
 * @author Lingarao.R
 * 
 */
public class EnterBillCommand extends AbstractTransactionCommand {

	private String number;
	private String vendorName;
	private String memo;
	private FinanceDate dueDate;
	private FinanceDate deliveryDate;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {

		// Number
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		this.number = num;
		if (!parseTransactionDate(data, respnse)) {
			return true;
		}
		// vendor
		String vendor = data.nextString("");
		if (vendorName != null && !vendorName.equals(vendor)) {
			return false;
		}
		vendorName = vendor;
		// Due Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		dueDate = data.nextDate(new FinanceDate());
		// Delivery Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		deliveryDate = data.nextDate(new FinanceDate());
		// Transaction Item
		if (!parseTransactionItem(data, respnse)) {
			return true;
		}
		// memo
		memo = data.nextString(null);

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		if (number == null || number.isEmpty()) {
			number = getnextTransactionNumber(Transaction.TYPE_ENTER_BILL);
			respnse.addMessage("You are Not Given Enter Number ,we are creating defaultly with this Number--->"
					+ number);
		}
		EnterBill bill = getObject(EnterBill.class, "number", number);
		if (bill != null) {
			number = getnextTransactionNumber(Transaction.TYPE_ENTER_BILL);
			respnse.addMessage("given Bill  Number already existed,we are creating defaultly with this Number--->"
					+ number);
		}
		EnterBill enterBill = new EnterBill();
		enterBill.setNumber(number);
		enterBill.setDate(transactionDate);
		enterBill.setDueDate(dueDate);
		enterBill.setDeliveryDate(deliveryDate);
		// TODO status
		// TODO payment Terms
		Vendor vendor = getObject(Vendor.class, "name", vendorName);
		if (vendor == null) {
			vendor = new Vendor();
			vendor.setName(vendorName);
			session.save(vendor);
		}
		enterBill.setVendor(vendor);
		// setting the Transaction iTems to Transaction
		ArrayList<TransactionItem> processTransactionItem = processVendorTransactionItem();
		enterBill.setTransactionItems(processTransactionItem);
		// setting the Transaction Total
		double total = getTransactionTotal(processTransactionItem);
		enterBill.setTotal(total);
		enterBill.setNetAmount(total);
		enterBill.setMemo(memo);

		saveOrUpdate(enterBill);
	}

}
