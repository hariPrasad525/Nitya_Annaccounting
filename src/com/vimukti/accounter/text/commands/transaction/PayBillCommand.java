package com.vimukti.accounter.text.commands.transaction;

import org.hibernate.Session;

import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.TransactionPayBill;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti10
 *         Number,TransactionDate,customer,paymentMethod,CheckNumber,amount
 *         ,DepositIn, Invoice Number,Memo
 */
public abstract class PayBillCommand extends AbstractTransactionCommand {

	private String number;
	private String vendorName;
	private String paymentMethod;
	private String payFromAccountName;
	private String enterBillNumber;
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
		this.number = num;
		// Transaction date
		if (!parseTransactionDate(data, respnse)) {
			return true;
		}
		// customer
		String vendor = data.nextString("");
		if (vendorName != null && !vendorName.equals(vendor)) {
			return false;
		}
		vendorName = vendor;
		// Payment Method
		paymentMethod = data.nextString(getPaymentMethod());
		// checkNumber
		checkNumber = data.nextString("");
		// amount
		amount = data.nextDouble(0);
		// depositIn
		payFromAccountName = data.nextString("");
		// Invoice Number
		enterBillNumber = data.nextString("");
		// memo
		memo = data.nextString("");

		return true;
	}

	public abstract String getPaymentMethod();

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		Vendor vendor = getObject(Vendor.class, "name", vendorName);
		if (vendor == null) {
			respnse.addError("Invalid Vendor Name");
			return;
		}
		// getting the invoice Based On Number
		EnterBill enterBill = getObject(EnterBill.class, "number",
				enterBillNumber);
		if (enterBill == null) {
			respnse.addError("Invalid Enter Bill Number for this Vendor");
			return;
		}
		if (number == null || number.isEmpty()) {
			number = getnextTransactionNumber(com.vimukti.accounter.core.Transaction.TYPE_PAY_BILL);
		}
		PayBill bill = getObject(PayBill.class, "number", number);
		if (bill != null) {
			number = getnextTransactionNumber(com.vimukti.accounter.core.Transaction.TYPE_PAY_BILL);
		}
		PayBill payBill = new PayBill();
		payBill.setDate(transactionDate);
		payBill.setNumber(number);

		payBill.setVendor(vendor);

		BankAccount payFrom = getObject(BankAccount.class, "name",
				payFromAccountName);
		if (payFrom == null) {
			payFrom = new BankAccount();
			payFrom.setNumber("8574");
			payFrom.setIsActive(true);
			payFrom.setName(payFromAccountName);
			payFrom.setCompany(getCompany());
			session.save(payFrom);
		}
		payBill.setPayFrom(payFrom);
		payBill.setPaymentMethod(paymentMethod);
		payBill.setCheckNumber(checkNumber);
		// Preparing the ReceivePayment Item
		TransactionPayBill payment = new TransactionPayBill();
		payment.setEnterBill(enterBill);
		payment.setBillNumber(enterBill.getNumber());
		payment.setOriginalAmount(enterBill.getTotal());
		payment.setDueDate(enterBill.getDueDate());
		payment.setAmountDue(enterBill.getTotal());
		payment.setPayment(amount);
		payBill.getTransactionPayBill().add(payment);
		payBill.setMemo(memo);
		payBill.setTotal(amount);
		payBill.setNetAmount(amount);
		saveOrUpdate(payBill);

	}
}
