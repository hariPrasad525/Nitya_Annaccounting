package com.nitya.accounter.text.commands;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * name,customerSince,openingBalance,balanceasOf,address,webaddress,email,phone,
 * fax
 * 
 * @author Umasree
 * 
 */
public class CustomerCommand extends CreateOrUpdateCommand {

	private String customerName;
	private FinanceDate customerSince;

	private double openingBalance;
	private FinanceDate balanceAsOf;
	private Address address;
	private String webAddress;
	private String email;
	private String phone;
	private String fax;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Name
		String name = data.nextString("");
		if (customerName != null && !customerName.equals(name)) {
			return false;
		}
		customerName = name;
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// Customer Since
		customerSince = data.nextDate(new FinanceDate());
		if (!data.isDouble()) {
			respnse.addError("Invalid Double for Opening Balance");
			return false;
		}
		// Opening Balance
		openingBalance = data.nextDouble(0);
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// balance as of
		balanceAsOf = data.nextDate(new FinanceDate());
		// address
		address = data.nextAddress(null);
		if (address != null) {
			address.setType(Address.TYPE_BILL_TO);
		}
		// web Address
		webAddress = data.nextString("");
		// email
		email = data.nextString("");
		// phone
		phone = data.nextString("");
		// fax
		fax = data.nextString("");
		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Criteria query = session.createCriteria(Customer.class);
		query.add(Restrictions.eq("company", getCompany()));
		query.add(Restrictions.eq("name", customerName));
		Customer customer = (Customer) query.uniqueResult();
		if (customer == null) {
			customer = new Customer();
			customer.setType(Payee.TYPE_CUSTOMER);
		}
		customer.setName(customerName);
		customer.setPayeeSince(customerSince);
		customer.setBalanceAsOf(balanceAsOf);
		customer.setOpeningBalance(openingBalance);
		if (address != null) {
			customer.getAddress().add(address);
		}
		customer.setWebPageAddress(webAddress);
		customer.setEmail(email);
		customer.setPhoneNo(phone);
		customer.setFaxNo(fax);

		saveOrUpdate(customer);
	}

}
