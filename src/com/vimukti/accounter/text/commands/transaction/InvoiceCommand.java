package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * number,customer,date,itemname,unit price,quantity,tax,item description,my
 * comments
 * 
 * @author Umasree
 * 
 */
public class InvoiceCommand extends AbstractTransactionCommand {

	private String number;
	private String customerName;
	private Address billingAddress;
	private FinanceDate dueDate;
	private FinanceDate deliveryDate;
	private FinanceDate discountDate;
	private String memo;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		number = num;
		String customer = data.nextString("");
		if (customerName != null && !customerName.equals(customer)) {
			return false;
		}
		customerName = customer;
		if (!parseTransactionDate(data, respnse)) {
			return true;
		}
		// Due Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		dueDate = data.nextDate(new FinanceDate());
		// delivery Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		deliveryDate = data.nextDate(new FinanceDate());
		// Discount Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		discountDate = data.nextDate(new FinanceDate());
		// Billing Adress
		billingAddress = data.nextAddress(null);
		if (billingAddress != null) {
			billingAddress.setType(Address.TYPE_BILL_TO);
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
		if (number == null || number.isEmpty()) {
			number = getnextTransactionNumber(Transaction.TYPE_INVOICE);
			respnse.addMessage("You are Not Given Invoice Number ,we are creating defaultly with this Number--->"
					+ number);
		}

		Invoice inv = getObject(Invoice.class, "number", number);
		if (inv != null) {
			number = getnextTransactionNumber(Transaction.TYPE_INVOICE);
			respnse.addMessage("given nvoice Number already existed,we are creating defaultly with this Number--->"
					+ number);
		}
		Invoice invoice = new Invoice();
		invoice.setNumber(number);

		Customer customer = getObject(Customer.class, "name", customerName);
		if (customer == null) {
			customer = new Customer();
			customer.setName(customerName);
			session.save(customer);
		}
		invoice.setCustomer(customer);
		invoice.setDate(transactionDate);
		invoice.setDueDate(dueDate);
		invoice.setDeliverydate(deliveryDate);
		invoice.setDiscountDate(discountDate);
		if (billingAddress != null) {
			invoice.setBillingAddress(billingAddress);
		}
		ArrayList<TransactionItem> processTransactionItems = processCustomerTransactionItem();
		invoice.setTransactionItems(processTransactionItems);
		if (memo != null) {
			invoice.setMemo(memo);
		}
		// getting Transaction Total
		double total = getTransactionTotal(processTransactionItems);
		invoice.setNetAmount(total);
		invoice.setTotal(total);
		saveOrUpdate(invoice);
	}
}
