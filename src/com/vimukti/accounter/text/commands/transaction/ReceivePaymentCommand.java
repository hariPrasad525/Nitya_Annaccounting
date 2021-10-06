package com.vimukti.accounter.text.commands.transaction;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.TransactionReceivePayment;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * Number,TransactionDate,customer,paymentMethod,CheckNumber,amount,DepositIn,
 * Invoice Number,Memo
 * 
 * @author vimukti10
 * 
 */
public abstract class ReceivePaymentCommand extends AbstractTransactionCommand {

	private String number;
	private String customerName;
	private String paymentMethod;
	private String depositInAccounName;
	private String invoiceNumber;
	private String memo;
	private String checkNumber;
	private double amount;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Number
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		number = num;
		// Transaction date
		if (!parseTransactionDate(data, respnse)) {
			return true;
		}
		// customer
		String customer = data.nextString("");
		if (customerName != null && !customerName.equals(customer)) {
			return false;
		}
		customerName = customer;
		// Payment Method
		paymentMethod = data.nextString(getPaymentMethod());
		// checkNumber
		checkNumber = data.nextString("");
		// amount
		amount = data.nextDouble(0);
		// depositIn
		depositInAccounName = data.nextString("");
		// Invoice Number
		invoiceNumber = data.nextString("");
		// memo
		memo = data.nextString("");

		return true;
	}

	public abstract String getPaymentMethod();

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		Customer customer = getObject(Customer.class, "name", customerName);
		if (customer == null) {
			respnse.addError("Invalid Customer Name");
			return;
		}
		// getting the invoice Based On Number
		Invoice invoice = getObject(Invoice.class, "number", invoiceNumber);
		if (invoice == null) {
			respnse.addError("Invalid Invoice Number for this Customer");
			return;
		}
		if (number == null || number.isEmpty()) {
			number = getnextTransactionNumber(com.vimukti.accounter.core.Transaction.TYPE_RECEIVE_PAYMENT);
		}
		ReceivePayment recPayment = getObject(ReceivePayment.class, "number",
				number);
		if (recPayment != null) {
			number = getnextTransactionNumber(com.vimukti.accounter.core.Transaction.TYPE_RECEIVE_PAYMENT);
		}
		ReceivePayment receivePayment = new ReceivePayment();
		receivePayment.setDate(transactionDate);
		receivePayment.setNumber(number);

		receivePayment.setCustomer(customer);
		BankAccount depositIn = getObject(BankAccount.class, "name",
				depositInAccounName);
		if (depositIn == null) {
			depositIn = new BankAccount();
			String nextAccountNumber = NumberUtils.getNextAccountNumber(
					getCompany().getId(), Account.TYPE_BANK);
			depositIn.setNumber(nextAccountNumber);
			depositIn.setIsActive(true);
			depositIn.setType(Account.TYPE_BANK);
			depositIn.setName(depositInAccounName);
			depositIn.setCompany(getCompany());
			session.save(depositIn);
		}
		receivePayment.setDepositIn(depositIn);
		receivePayment.setPaymentMethod(paymentMethod);
		receivePayment.setCheckNumber(checkNumber);
		// Preparing the ReceivePayment Item
		TransactionReceivePayment payment = new TransactionReceivePayment();
		payment.setInvoice(invoice);
		payment.setNumber(invoice.getNumber());
		payment.setInvoiceAmount(invoice.getTotal());
		payment.setDueDate(invoice.getDueDate());
		payment.setPayment(amount);
		receivePayment.getTransactionReceivePayment().add(payment);
		receivePayment.setAmount(amount);
		receivePayment.setTotal(amount);
		receivePayment.setMemo(memo);
		saveOrUpdate(receivePayment);

	}
}
