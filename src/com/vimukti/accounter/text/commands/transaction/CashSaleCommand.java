package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

//number,customerName,paymentMethod,desposit in
public class CashSaleCommand extends AbstractTransactionCommand {

	private String number;
	private String customerName;
	private String memo;
	private String paymentmethod;
	private String despositIn;

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
		// paymentMethod
		paymentmethod = data.nextString("");
		// desposit in Account
		despositIn = data.nextString("");
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
			number = getnextTransactionNumber(Transaction.TYPE_CASH_SALES);
			respnse.addMessage("You are Not Given cash sale Number ,we are creating defaultly with this Number--->"
					+ number);
		}

		CashSales cashSales = getObject(CashSales.class, "number", number);
		if (cashSales != null) {
			respnse.addMessage("given Cash sale Number already existed,we are creating defaultly with this Number--->"
					+ number);
		}

		if (cashSales == null) {
			cashSales = new CashSales();
		}
		cashSales.setNumber(number);
		Customer customer = getObject(Customer.class, "name", customerName);
		if (customer == null) {
			customer = new Customer();
			customer.setName(customerName);
			customer.setCompany(getCompany());
			session.save(customer);
		}
		cashSales.setCustomer(customer);

		cashSales.setDate(transactionDate);

		BankAccount depositIn = getObject(BankAccount.class, "name", despositIn);
		if (depositIn == null) {
			depositIn = new BankAccount();
			String nextAccountNumber = NumberUtils.getNextAccountNumber(
					getCompany().getId(), Account.TYPE_BANK);
			depositIn.setNumber(nextAccountNumber);
			depositIn.setIsActive(true);
			depositIn.setName(despositIn);
			depositIn.setCompany(getCompany());
			depositIn.setType(Account.TYPE_BANK);
			session.save(depositIn);
		}

		cashSales.setDepositIn(depositIn);
		// Processed Transaction Items
		ArrayList<TransactionItem> processTransactionItems = processCustomerTransactionItem();

		cashSales.getTransactionItems().addAll(processTransactionItems);
		// geting the transaction Total
		double transactionTotal = getTransactionTotal(processTransactionItems);
		cashSales.setDeliverydate(new FinanceDate());
		cashSales.setNetAmount(cashSales.getNetAmount() + transactionTotal);
		cashSales.setTotal(cashSales.getTotal() + transactionTotal);
		cashSales.setMemo(memo);
		cashSales.setPaymentMethod(paymentmethod);
		saveOrUpdate(cashSales);
	}
}
