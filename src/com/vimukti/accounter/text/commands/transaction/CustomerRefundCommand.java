package com.vimukti.accounter.text.commands.transaction;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.CreateOrUpdateCommand;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * transactionDate,number,Pay To,pay From ,amount,paymentMethod,chequeorRefNo
 * 
 * @author Lingarao.R
 * 
 */
public class CustomerRefundCommand extends CreateOrUpdateCommand {

	private FinanceDate transactionDate;
	private String number;
	private String payTo;
	private String payFrom;
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
		payTo = data.nextString("");
		// Transaction Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		transactionDate = data.nextDate(new FinanceDate());
		// deposit in Account
		payFrom = data.nextString("");
		// amount
		amount = data.nextDouble(0);
		// Payment Method
		paymentMethod = data.nextString("Cash");
		// chequeERefnumber
		chequeorRefNo = data.nextString("");
		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		Session session = HibernateUtil.getCurrentSession();
		// getting transaction By Transaction Number
		Criteria query = session.createCriteria(CustomerRefund.class);
		query.add(Restrictions.eq("company", getCompany()));
		query.add(Restrictions.eq("number", number));

		CustomerRefund customerRefund = (CustomerRefund) query.uniqueResult();
		if (customerRefund == null) {
			customerRefund = new CustomerRefund();
		}
		// getting the Customer By Customer Name. if it is null Creating New
		// Customer With that Name
		Criteria customerQuery = session.createCriteria(Customer.class);
		customerQuery.add(Restrictions.eq("company", getCompany()));
		customerQuery.add(Restrictions.eq("name", payTo));
		Customer customer = (Customer) query.uniqueResult();
		if (customer == null) {
			customer = new Customer();
			customer.setName(this.payTo);
			session.save(customer);
		}
		customerRefund.setDate(transactionDate);
		customerRefund.setNumber(number);
		customerRefund.setPayTo(customer);

		// getting the Account By Account Name. if it is null Creating New
		// Account With that Name

		Criteria bankAccountQuery = session.createCriteria(BankAccount.class);
		bankAccountQuery.add(Restrictions.eq("company", getCompany()));
		bankAccountQuery.add(Restrictions.eq("name", customer));
		BankAccount bankAccount = (BankAccount) query.uniqueResult();
		if (bankAccount == null) {
			bankAccount = new BankAccount();
			bankAccount.setName(payFrom);
			session.save(bankAccount);
		}
		customerRefund.setPayFrom(bankAccount);
		customerRefund.setPaymentMethod(paymentMethod);
		customerRefund.setCheckNumber(chequeorRefNo);
		customerRefund.setTotal(amount);
	}

}
