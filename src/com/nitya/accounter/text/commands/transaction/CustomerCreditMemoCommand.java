package com.nitya.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.CustomerCreditMemo;
import com.nitya.accounter.core.TransactionItem;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * 
 * Number,TransactionDate,CustomerName,ItemName,Description,UnitPrice,Quantity,
 * Tax
 * ==========================================================================
 * Number,TransactionDate,CustomerName,AccountName,Description,Amount,Tax
 * 
 * @author Lingarao.R
 * 
 */
public class CustomerCreditMemoCommand extends AbstractTransactionCommand {

	private String number;
	private String memo;
	private String customerName;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Number
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		// Transaction Date
		if (!parseTransactionDate(data, respnse)) {
			return false;
		}
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

		CustomerCreditMemo creditMemo = getObject(CustomerCreditMemo.class,
				"number", number);
		if (creditMemo == null) {
			creditMemo = new CustomerCreditMemo();
		}
		creditMemo.setNumber(number);
		creditMemo.setDate(transactionDate);
		Customer customer = getObject(Customer.class, "name", customerName);
		if (customer == null) {
			customer = new Customer();
			customer.setName(customerName);
			session.save(customer);
		}
		creditMemo.setCustomer(customer);
		// setting the Transaction iTems to Transaction
		ArrayList<TransactionItem> processTransactionItem = processCustomerTransactionItem();
		creditMemo.setTransactionItems(processTransactionItem);
		// setting the Transaction Total
		double transactionTotal = getTransactionTotal(processTransactionItem);
		creditMemo.setTotal(transactionTotal);

		creditMemo.setMemo(memo);

		saveOrUpdate(creditMemo);
	}

}
