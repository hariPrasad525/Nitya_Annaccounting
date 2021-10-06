package com.nitya.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.Estimate;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.TransactionItem;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;

public class EstimateCommand extends AbstractTransactionCommand {

	private String number;
	private String memo;
	private String customerName;
	private FinanceDate expirationDate;
	private FinanceDate deliveryDate;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Number
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		// Transaction Date
		if (!parseTransactionDate(data, respnse)) {
			return true;
		}
		// Expiration Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		expirationDate = data.nextDate(new FinanceDate());
		// Status
		// TODO
		// Payment Terms
		// TODO
		// Delivery Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		deliveryDate = data.nextDate(new FinanceDate());
		// customer
		customerName = data.nextString("");
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

		Estimate estimate = getObject(Estimate.class, "number", number);
		if (estimate == null) {
			estimate = new Estimate();
		}
		estimate.setNumber(number);
		estimate.setDate(transactionDate);
		estimate.setExpirationDate(expirationDate);
		estimate.setDeliveryDate(deliveryDate);
		// TODO status
		// TODO payment Terms
		Customer customer = getObject(Customer.class, "name", customerName);
		if (customer == null) {
			customer = new Customer();
			customer.setName(customerName);
			session.save(customer);
		}
		estimate.setCustomer(customer);
		// setting the Transaction iTems to Transaction
		ArrayList<TransactionItem> processTransactionItem = processCustomerTransactionItem();
		estimate.setTransactionItems(processTransactionItem);
		// setting the Transaction Total
		double transactionTotal = getTransactionTotal(processTransactionItem);
		estimate.setTotal(transactionTotal);

		estimate.setMemo(memo);

		saveOrUpdate(estimate);
	}
}
