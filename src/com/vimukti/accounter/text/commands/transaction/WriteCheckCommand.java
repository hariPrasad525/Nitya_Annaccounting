package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

//pay cheque,date,vendor,invoice number,amount,chequeno,bankname,account number
public class WriteCheckCommand extends AbstractTransactionCommand {

	private FinanceDate transactionDate;
	private String number;
	private String payTo;
	private String bankAccountName;
	private String checkNumber;
	private ArrayList<TransctionItem> items = new ArrayList<TransctionItem>();
	private String memo;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}

		if (!parseTransactionDate(data, respnse)) {
			return true;
		}
		payTo = data.nextString("");
		bankAccountName = data.nextString("");
		checkNumber = data.nextString("");
		if (!parseTransactionItem(data, respnse)) {
			return true;
		}
		memo = data.nextString(null);
		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		WriteCheck writeCheck = getObject(WriteCheck.class, "number", number);
		if (writeCheck == null) {
			writeCheck = new WriteCheck();
		}

		Payee payee = getObject(Payee.class, "name", payTo);
		if (payee == null) {
			payee = new Vendor();
			payee.setName(payTo);
			session.save(payee);
		}
		writeCheck.setNumber(number);
		writeCheck.setDate(transactionDate);

		BankAccount bankAccount = getObject(BankAccount.class, "name",
				bankAccountName);
		if (bankAccount == null) {
			bankAccount = new BankAccount();
			bankAccount.setName(this.bankAccountName);
			session.save(bankAccount);
		}
		writeCheck.setBankAccount(bankAccount);
		writeCheck.setCheckNumber(checkNumber);
		ArrayList<TransactionItem> processTransactionItem = processVendorTransactionItem();
		writeCheck.setTransactionItems(processTransactionItem);
		if (memo != null) {
			writeCheck.setMemo(memo);
		}
		// getting Transaction Total
		double total = getTransactionTotal(processTransactionItem);
		writeCheck.setTotal(total);
		saveOrUpdate(writeCheck);
	}

}
