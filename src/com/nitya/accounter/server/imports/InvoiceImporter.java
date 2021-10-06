package com.nitya.accounter.server.imports;

import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientInvoice;
import com.nitya.accounter.web.client.core.ImportField;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.imports.FinanceDateField;
import com.nitya.accounter.web.client.imports.LongField;
import com.nitya.accounter.web.client.imports.StringField;

/**
 * @author Prasanna Kumar G
 * 
 */
public class InvoiceImporter extends TransactionImporter<ClientInvoice> {

	@Override
	public List<ImportField> getAllFields() {
		List<ImportField> fields = super.getAllFields();
		fields.add(new StringField(messages.customer(), messages
				.payeeName(Global.get().Customer()), true));
		fields.add(new StringField(messages.number(), messages.invoiceNo()));
		fields.add(new LongField(messages.paymentTerm(), messages.paymentTerm()));
		fields.add(new FinanceDateField(messages.dueDate(), messages.dueDate()));
		fields.add(new StringField(messages.orderNo(), messages.orderNo()));
		fields.add(new StringField(messages.memo(), messages.memo()));
		return fields;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClientInvoice getData() {
		ClientInvoice invoice = new ClientInvoice();
		getTransactionData(invoice);
		invoice.setCustomer(getPayeeByName(messages.customer()));
		invoice.setNumber(getString(messages.number()));
		invoice.setPaymentTerm(getLong(messages.paymentTerm()));
		invoice.setDueDate(getFinanceDate(messages.dueDate()).getDate());
		invoice.setOrderNum(getString(messages.orderNo()));
		invoice.setMemo(getString(messages.memo()));
		return invoice;
	}

	@Override
	protected void validate(List<AccounterException> exceptions) {
		// TODO Auto-generated method stub

	}
}
