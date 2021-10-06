package com.nitya.accounter.text.commands.transaction;

import org.hibernate.Session;

import com.nitya.accounter.core.BankAccount;
import com.nitya.accounter.core.TransferFund;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.utils.HibernateUtil;

public class TransferFundCommand extends AbstractTransactionCommand {

	private String number;
	private String transerFromAccountName;
	private String transerToAccountName;
	private double amount;
	private String memo;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {

		// Number
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		// Transaction date
		if (!parseTransactionDate(data, respnse)) {
			return true;
		}
		// Transfer From*
		transerFromAccountName = data.nextString("");
		// Transfer To*
		transerToAccountName = data.nextString("");
		// Amount(INR)
		amount = data.nextDouble(0);
		// memo
		memo = data.nextString("");

		return true;
	}

	@Override
	public void process(ITextResponse respnse) {

		Session session = HibernateUtil.getCurrentSession();
		TransferFund transferFund = getObject(TransferFund.class, "number",
				number);
		if (transferFund == null) {
			transferFund = new TransferFund();
		}
		transferFund.setDate(transactionDate);
		transferFund.setNumber(number);
		BankAccount trasferFrom = getObject(BankAccount.class, "name",
				transerFromAccountName);
		if (trasferFrom == null) {
			trasferFrom = new BankAccount();
			trasferFrom.setName(transerFromAccountName);
			session.save(trasferFrom);
		}
		transferFund.setDepositFrom(trasferFrom);

		BankAccount transferTo = getObject(BankAccount.class, "name",
				transerToAccountName);
		if (transferTo == null) {
			transferTo = new BankAccount();
			transferTo.setName(transerToAccountName);
			session.save(transferTo);
		}
		transferFund.setDepositIn(transferTo);

		transferFund.setTotal(amount);

		transferFund.setMemo(memo);

	}
}
