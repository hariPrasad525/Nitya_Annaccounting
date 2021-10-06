package com.vimukti.accounter.text.commands.transaction;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorPrePayment;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti10
 * 
 */
public class VendorPrePaymentCommand extends AbstractTransactionCommand {

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
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		// getting transaction By Transaction Number
		VendorPrePayment prePayment = getObject(VendorPrePayment.class,
				"number", number);
		if (prePayment == null) {
			prePayment = new VendorPrePayment();
		}
		// getting the Customer By Customer Name. if it is null Creating New
		// Customer With that Name
		Criteria customerQuery = session.createCriteria(Customer.class);
		customerQuery.add(Restrictions.eq("company", getCompany()));
		customerQuery.add(Restrictions.eq("name", payTo));
		Vendor vendor = getObject(Vendor.class, "name", payTo);
		if (vendor == null) {
			vendor = new Vendor();
			vendor.setName(this.payTo);
			session.save(vendor);
		}
		prePayment.setDate(transactionDate);
		prePayment.setNumber(number);
		prePayment.setVendor(vendor);

		// getting the Account By Account Name. if it is null Creating New
		// Account With that Name

		BankAccount account = getObject(BankAccount.class, "name", payFrom);
		if (account == null) {
			account = new BankAccount();
			account.setName(payFrom);
			session.save(account);
		}
		prePayment.setPayFrom(account);
		prePayment.setPaymentMethod(paymentMethod);
		prePayment.setCheckNumber(chequeorRefNo);
		prePayment.setTotal(amount);

		saveOrUpdate(prePayment);
	}

}
