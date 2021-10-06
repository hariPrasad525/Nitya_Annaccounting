package com.nitya.accounter.text.commands.transaction;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.BankAccount;
import com.nitya.accounter.core.MakeDeposit;
import com.nitya.accounter.core.TransactionDepositItem;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.utils.HibernateUtil;

/**
 * 
 * @author vimukti10
 * 
 */
public class MakeDepositCommand extends AbstractTransactionCommand {
	private String number;
	private String memo;
	private String paymentMethod;
	private String despositTo;
	private ArrayList<MakeDepositItem> makeDepositItems = new ArrayList<MakeDepositItem>();

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Number
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		if (!parseTransactionDate(data, respnse)) {
			return true;
		}
		// deposit To Account
		despositTo = data.nextString("");
		// paymentMethod
		paymentMethod = data.nextString("");
		// Make Deposit Item
		MakeDepositItem makeDepositItem = new MakeDepositItem();
		makeDepositItem.setPayee(data.nextString(""));
		makeDepositItem.setDepositFrom(data.nextString(""));
		makeDepositItem.setDescription(data.nextString(""));
		if (!data.isDouble()) {
			respnse.addError("Invalid Double for Total field");
			return false;

		}
		makeDepositItem.setTotal(data.nextDouble(0));
		makeDepositItems.add(makeDepositItem);

		memo = data.nextString("");

		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		Session session = HibernateUtil.getCurrentSession();
		MakeDeposit makeDeposit = getObject(MakeDeposit.class, "number", number);
		if (makeDeposit == null) {
			makeDeposit = new MakeDeposit();
		}
		makeDeposit.setDate(transactionDate);
		makeDeposit.setNumber(number);
		BankAccount bAccount = getObject(BankAccount.class, "name", despositTo);
		if (bAccount == null) {
			bAccount = new BankAccount();
			bAccount.setName(despositTo);
			session.save(bAccount);
		}
		makeDeposit.setDepositTo(bAccount);
		makeDeposit.setPaymentMethod(paymentMethod);
		List<TransactionDepositItem> transactionDepositItems = new ArrayList<TransactionDepositItem>();
		for (MakeDepositItem makeDepositItem : makeDepositItems) {
			TransactionDepositItem depositItem = new TransactionDepositItem();
			// TODO Set Payee To Make Deposit Item
			Account account = getObject(Account.class, "name",
					makeDepositItem.getDepositFrom());
			depositItem.setAccount(account);
			depositItem.setDescription(makeDepositItem.getDescription());
			depositItem.setTotal(makeDepositItem.getTotal());
			transactionDepositItems.add(depositItem);
		}
		makeDeposit.setTransactionDepositItems(transactionDepositItems);
		makeDeposit.setMemo(memo);
	}
}

class MakeDepositItem {

	private String payee;
	private String depositFrom;
	private String description;
	private double total;

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getDepositFrom() {
		return depositFrom;
	}

	public void setDepositFrom(String depositFrom) {
		this.depositFrom = depositFrom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
}