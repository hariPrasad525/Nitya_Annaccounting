package com.nitya.accounter.text.commands.transaction;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.nitya.accounter.core.BankAccount;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.CustomerPrePayment;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.text.commands.CreateOrUpdateCommand;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * transactionDate,number,customer,depositIn,amount,paymentMethod,chequeorRefNo
 * 
 * @author Lingarao.R
 * 
 */
public class CustomerPrePaymentCommand extends CreateOrUpdateCommand {

	private FinanceDate transactionDate;
	private String number;
	private String customer;
	private String depositIn;
	private double amount;
	private String paymentMethod;
	private String chequeorRefNo;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Number
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		// Customer
		customer = data.nextString("");
		// Transaction Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		transactionDate = data.nextDate(new FinanceDate());
		// deposit in Account
		depositIn = data.nextString("");
		// amount
		amount = data.nextDouble(0);
		// Payment Method
		paymentMethod = data.nextString("Cash");
		// chequeERefnumber
		chequeorRefNo = data.nextString("");
		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		// getting transaction By Transaction Number
		Criteria query = session.createCriteria(CustomerPrePayment.class);
		query.add(Restrictions.eq("company", getCompany()));
		query.add(Restrictions.eq("number", number));

		CustomerPrePayment prePayment = (CustomerPrePayment) query
				.uniqueResult();
		if (prePayment == null) {
			prePayment = new CustomerPrePayment();
		}
		// getting the Customer By Customer Name. if it is null Creating New
		// Customer With that Name
		Criteria customerQuery = session.createCriteria(Customer.class);
		customerQuery.add(Restrictions.eq("company", getCompany()));
		customerQuery.add(Restrictions.eq("name", customer));
		Customer customer = (Customer) query.uniqueResult();
		if (customer == null) {
			customer = new Customer();
			customer.setName(this.customer);
			session.save(customer);
		}
		prePayment.setDate(transactionDate);
		prePayment.setNumber(number);
		prePayment.setCustomer(customer);

		// getting the Account By Account Name. if it is null Creating New
		// Account With that Name

		Criteria bankAccountQuery = session.createCriteria(BankAccount.class);
		bankAccountQuery.add(Restrictions.eq("company", getCompany()));
		bankAccountQuery.add(Restrictions.eq("name", customer));
		BankAccount bankAccount = (BankAccount) query.uniqueResult();
		if (bankAccount == null) {
			bankAccount = new BankAccount();
			bankAccount.setName(depositIn);
			session.save(bankAccount);
		}
		prePayment.setDepositIn(bankAccount);
		prePayment.setPaymentMethod(paymentMethod);
		prePayment.setCheckNumber(chequeorRefNo);
		prePayment.setTotal(amount);
		saveOrUpdate(prePayment);
	}

}
