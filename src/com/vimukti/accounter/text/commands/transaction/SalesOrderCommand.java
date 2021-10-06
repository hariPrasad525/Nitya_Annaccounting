package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.SalesOrder;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class SalesOrderCommand extends AbstractTransactionCommand {

	private String number;
	private String customerName;
	private String memo;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		customerName = data.nextString("");
		if (!parseTransactionDate(data, respnse)) {
			return true;
		}
		// Transaction Item
		if (!parseTransactionItem(data, respnse)) {
			return true;
		}
		memo = data.nextString(null);

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		SalesOrder salesOrder = getObject(SalesOrder.class, "number", number);
		if (salesOrder == null) {
			salesOrder = new SalesOrder();
		}
		salesOrder.setNumber(number);

		Customer customer = getObject(Customer.class, "name", customerName);
		if (customer == null) {
			customer = new Customer();
			customer.setName(customerName);
			session.save(customer);
		}
		salesOrder.setCustomer(customer);

		ArrayList<TransactionItem> processTransactionItems = processCustomerTransactionItem();

		salesOrder.setTransactionItems(processTransactionItems);

		if (memo != null) {
			salesOrder.setMemo(memo);
		}
		// getting Transaction Total
		salesOrder.setTotal(getTransactionTotal(processTransactionItems));

		saveOrUpdate(salesOrder);
	}

}
