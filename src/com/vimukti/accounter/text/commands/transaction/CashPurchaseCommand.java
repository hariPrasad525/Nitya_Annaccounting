package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class CashPurchaseCommand extends AbstractTransactionCommand {

	private String number;
	private String memo;
	private String vendorName;
	private String paymentMethod;
	private String checqueNumber;
	private String payfrom;
	private FinanceDate deliveryDate;

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
		vendorName = data.nextString("");
		// Payment Method
		paymentMethod = data.nextString("");
		// Transaction Item
		if (!parseTransactionItem(data, respnse)) {
			return true;
		}
		// Cheque No
		checqueNumber = data.nextString("");
		// Pay From
		payfrom = data.nextString("");
		// Delivery Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		deliveryDate = data.nextDate(new FinanceDate());
		// memo
		memo = data.nextString(null);
		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		CashPurchase cashPurchase = getObject(CashPurchase.class, "number",
				number);
		if (cashPurchase == null) {
			cashPurchase = new CashPurchase();
		}
		cashPurchase.setNumber(number);
		cashPurchase.setDate(transactionDate);
		Vendor vendor = getObject(Vendor.class, "name", vendorName);
		if (vendor == null) {
			vendor = new Vendor();
			vendor.setName(vendorName);
			session.save(vendor);
		}
		cashPurchase.setVendor(vendor);
		cashPurchase.setPaymentMethod(paymentMethod);
		cashPurchase.setCheckNumber(checqueNumber);
		cashPurchase.setDeliveryDate(deliveryDate);
		BankAccount payFromBankAccount = getObject(BankAccount.class, "name",
				payfrom);
		if (payFromBankAccount == null) {
			payFromBankAccount = new BankAccount();
			payFromBankAccount.setName(this.payfrom);
			session.save(payFromBankAccount);
		}
		cashPurchase.setPayFrom(payFromBankAccount);
		// setting the Transaction iTems to Transaction
		ArrayList<TransactionItem> processTransactionItem = processVendorTransactionItem();
		cashPurchase.setTransactionItems(processTransactionItem);
		// setting the Transaction Total
		cashPurchase.setTotal(getTransactionTotal(processTransactionItem));

		cashPurchase.setMemo(memo);

		saveOrUpdate(cashPurchase);
	}

}
