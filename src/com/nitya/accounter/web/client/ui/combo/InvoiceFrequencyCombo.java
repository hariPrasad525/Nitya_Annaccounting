package com.nitya.accounter.web.client.ui.combo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.ClientInvoiceFrequencyGroup;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.vendors.ManageSupportListView;

public class InvoiceFrequencyCombo extends CustomCombo<ClientInvoiceFrequencyGroup> {

	public InvoiceFrequencyCombo(String title) {
		super(title, "InvoiceFrequencyCombo");
		 Accounter.createGETService().getAllInvoiceFrequency(new AsyncCallback<List<ClientInvoiceFrequencyGroup>>() {
				
				@Override
				public void onFailure(Throwable caught) {						
				}

				@Override
				public void onSuccess(List<ClientInvoiceFrequencyGroup> result) {
					initCombo(result);
				}
			});
		
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages2.invoiceGroup();
	}

	@Override
	protected String getDisplayName(ClientInvoiceFrequencyGroup object) {
		return object != null ? object.getName() != null ? object.getName()
				: "" : "";
	}

	@Override
	public void onAddNew() {
		ManageSupportListView priceLevelDialog = new ManageSupportListView(
				IAccounterCore.INVOICE_FREQUENCY);
		priceLevelDialog.setVisible(false);
		priceLevelDialog.setCallback(createAddNewCallBack());
		priceLevelDialog.showAddEditGroupDialog(null);

	}

	@Override
	protected String getColumnData(ClientInvoiceFrequencyGroup object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}
}
