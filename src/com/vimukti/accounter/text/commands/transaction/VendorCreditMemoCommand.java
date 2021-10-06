package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorCreditMemo;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti10
 * 
 */
public class VendorCreditMemoCommand extends AbstractTransactionCommand {

	private String number;
	private String memo;
	private String vendorName;

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

		VendorCreditMemo vendorCreditMemo = getObject(VendorCreditMemo.class,
				"number", number);
		if (vendorCreditMemo == null) {
			vendorCreditMemo = new VendorCreditMemo();
		}
		vendorCreditMemo.setNumber(number);
		vendorCreditMemo.setDate(transactionDate);
		Vendor vendor = getObject(Vendor.class, "name", vendorName);
		if (vendor == null) {
			vendor = new Vendor();
			vendor.setName(vendorName);
			session.save(vendor);
		}
		vendorCreditMemo.setVendor(vendor);
		// setting the Transaction iTems to Transaction
		ArrayList<TransactionItem> processTransactionItem = processVendorTransactionItem();
		vendorCreditMemo.setTransactionItems(processTransactionItem);
		// setting the Transaction Total
		vendorCreditMemo.setTotal(getTransactionTotal(processTransactionItem));

		vendorCreditMemo.setMemo(memo);

		saveOrUpdate(vendorCreditMemo);
	}

}
