/**
 * 
 */
package com.nitya.accounter.server.imports;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientAttachment;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ImportField;
import com.nitya.accounter.web.client.imports.DoubleField;
import com.nitya.accounter.web.client.imports.FinanceDateField;
import com.nitya.accounter.web.client.imports.LongField;
import com.nitya.accounter.web.client.imports.StringField;

/**
 * @author vimukti23
 * 
 */
public abstract class TransactionImporter<T> extends
		AbstractImporter<IAccounterCore> {
	protected void getTransactionData(ClientTransaction transaction) {
		transaction.setLocation(getLong(messages.location()));
		transaction.setTransactionDate(getFinanceDate(
				messages.transactionDate()).getDate());
		transaction.setNumber(getString(messages.number()));
		transaction.setPaymentMethod(getString(messages.paymentMethod()));
		transaction.setAccounterClass(getAccounterClass(messages.className())
				.getID());
		transaction.setAllLineTotal(getDouble(messages.lineTotal()));
		transaction.setAllNonTaxableLineTotal(getDouble(messages.nonTaxable()));
		transaction.setAllTaxableLineTotal(getDouble(messages.taxable()));
		transaction.setAttachments(getAttachments(messages.attachments()));
		transaction.setMemo(getString(messages.memo()));
		transaction.setNetAmount(getDouble(messages.netAmount()));
		transaction.setRecurringTransaction(getLong(messages
				.recurringTransactions()));
		transaction.setReference(getString(messages.reference()));
		transaction.setTotal(getDouble(messages.total()));
		transaction.setTransactionItems(getTransactionItem(messages
				.transactionItem()));

	}

	private List<ClientTransactionItem> getTransactionItem(
			String transactionItem) {
		List<ClientTransactionItem> items = new ArrayList<ClientTransactionItem>();
		ClientTransactionItem item = new ClientTransactionItem();
		item.setAccount(getLong(messages.account()));
		item.setItem(getLong(messages.itemName()));
		item.setDescription(getString(messages.description()));
		// item.setQuantity(getClientQty(messages.quantity()));
		item.setUnitPrice(getDouble(messages.unitPrice()));
		item.setTaxCode(getLong(messages.taxCode()));
		item.setDiscount(getDouble(messages.discount()));
		item.setVATfraction(getDouble(messages.vat()));
		item.setCustomer(getLong(messages.customer()));
		items.add(item);
		return items;
	}

	protected Set<ClientAttachment> getAttachments(String attachments) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ImportField> getAllFields() {
		List<ImportField> fields = new ArrayList<ImportField>();
		fields.add(new LongField(messages.location(), messages
				.locationName(Global.get().Location())));
		fields.add(new FinanceDateField(messages.transactionDate(), messages
				.transactionDate(), true));
		fields.add(new StringField(messages.number(), messages.number()));
		fields.add(new StringField(messages.paymentMethod(), messages
				.paymentMethod()));
		fields.add(new StringField(messages.className(), messages.className()));
		fields.add(new DoubleField(messages.lineTotal(), messages.lineTotal()));
		fields.add(new DoubleField(messages.lineTotal(), messages.lineTotal()));
		fields.add(new DoubleField(messages.nonTaxable(), messages.nonTaxable()));
		fields.add(new DoubleField(messages.taxable(), messages.taxable()));
		fields.add(new DoubleField(messages.taxable(), messages.taxable()));
		fields.add(new StringField(messages.memo(), messages.memo()));
		fields.add(new DoubleField(messages.netAmount(), messages.netAmount()));
		fields.add(new LongField(messages.receivedTransactions(), messages
				.receivedTransactions()));
		fields.add(new DoubleField(messages.receivedTransactions(), messages
				.receivedTransactions()));
		fields.add(new DoubleField(messages.total(), messages.total()));
		fields.add(new StringField(messages.account(), messages.name(), true));
		fields.add(new StringField(messages.itemName(), messages.name(), true));
		fields.add(new StringField(messages.description(), messages
				.description()));
		fields.add(new LongField(messages.quantity(), messages.quantity(), true));
		fields.add(new LongField(messages.unit(), messages.unitPrice(), true));
		fields.add(new LongField(messages.taxCode(), messages.taxCode(), true));
		fields.add(new DoubleField(messages.discount(), messages.discount()));
		fields.add(new DoubleField(messages.vat(), messages.tax()));
		fields.add(new StringField(messages.customer(), Global.get().Customer()));
		return fields;
	}

	private ClientAccounterClass getAccounterClass(String className) {
		// TODO Auto-generated method stub
		return null;
	}

}
