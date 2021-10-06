package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.ui.core.Action;

public class InvoiceListViewAction extends Action {

	public InvoiceListViewAction() {
		super();

	}

	@Override
	public void run() {
		runAsync(listData);
	}

	private void runAsync(List<InvoicesList> listData) {

		for (InvoicesList invoice : listData) {

			// if (invoice.getType() == Invoice.TYPE_INVOICE) {
			// UIUtils.downloadAttachment(invoice.getTransactionId(),
			// ClientTransaction.TYPE_INVOICE, 1);
			// }

		}

	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		return "";
	}

}
