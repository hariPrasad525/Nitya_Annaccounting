package com.nitya.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.PurchaseOrder;
import com.nitya.accounter.core.TransactionItem;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;

public class PurchaseOrderCommand extends AbstractTransactionCommand {

	private String number;
	private String memo;
	private String vendorName;
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
		// Transaction Item
		if (!parseTransactionItem(data, respnse)) {
			return true;
		}
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

		PurchaseOrder purchaseOrder = getObject(PurchaseOrder.class, "number",
				number);
		if (purchaseOrder == null) {
			purchaseOrder = new PurchaseOrder();
		}
		purchaseOrder.setNumber(number);
		purchaseOrder.setDate(transactionDate);
		Vendor vendor = getObject(Vendor.class, "name", vendorName);
		if (vendor == null) {
			vendor = new Vendor();
			vendor.setName(vendorName);
			session.save(vendor);
		}
		purchaseOrder.setVendor(vendor);
		purchaseOrder.setDeliveryDate(deliveryDate);
		// setting the Transaction iTems to Transaction
		ArrayList<TransactionItem> processTransactionItem = processVendorTransactionItem();
		purchaseOrder.setTransactionItems(processTransactionItem);
		// setting the Transaction Total
		purchaseOrder.setTotal(getTransactionTotal(processTransactionItem));

		purchaseOrder.setMemo(memo);

		saveOrUpdate(purchaseOrder);
	}

}
