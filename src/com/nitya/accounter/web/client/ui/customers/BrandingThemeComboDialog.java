package com.nitya.accounter.web.client.ui.customers;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientBrandingTheme;
import com.nitya.accounter.web.client.core.ClientInvoice;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.Lists.InvoicesList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.BrandingThemeCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.core.BaseDialog;

public class BrandingThemeComboDialog extends BaseDialog {
	private BrandingThemeCombo brandingThemeTypeCombo;
	private ClientTransaction clientTransaction;
	private ClientBrandingTheme brandingTheme;
	private List<InvoicesList> list;

	public BrandingThemeComboDialog(String title, String desc,
			ClientTransaction clientTransaction) {
		super(title, desc);
		this.getElement().setId("BrandingThemeComboDialog");
		this.clientTransaction = clientTransaction;
		createControls();
	}

	public BrandingThemeComboDialog(String title, String desc,
			List<InvoicesList> list) {
		super(title, desc);
		this.getElement().setId("BrandingThemeComboDialog");
		this.list = list;
		createControls();
	}

	public BrandingThemeComboDialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("BrandingThemeComboDialog");
	}

	private void createControls() {
		brandingThemeTypeCombo = new BrandingThemeCombo(messages.selectTheme());
		brandingTheme = new ClientBrandingTheme();
		brandingThemeTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientBrandingTheme>() {

					@Override
					public void selectedComboBoxItem(
							ClientBrandingTheme selectItem) {
						brandingTheme = selectItem;
					}
				});
		brandingThemeTypeCombo.setThemeDialog(this);
		brandingTheme = Accounter.getCompany().getBrandingTheme().get(1);
		brandingThemeTypeCombo.setComboItem(brandingTheme);

		setBodyLayout(brandingThemeTypeCombo);
	}

	private void print() {
		if (list == null) {
			// for printing individual invoice and customer credit memo pdf
			// documents
			if (ClientTransaction.TYPE_INVOICE == clientTransaction.getType()) {
				UIUtils.downloadAttachment(
						((ClientInvoice) clientTransaction).getID(),
						ClientTransaction.TYPE_INVOICE, brandingTheme.getID());
			} else if (ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO == clientTransaction
					.getType()) {
				UIUtils.downloadAttachment(clientTransaction.getID(),
						ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
						brandingTheme.getID());
			} else if (ClientTransaction.TYPE_ESTIMATE == clientTransaction
					.getType()) {
				UIUtils.downloadAttachment(clientTransaction.getID(),
						ClientTransaction.TYPE_ESTIMATE, brandingTheme.getID());
			} else if (ClientTransaction.TYPE_CASH_SALES == clientTransaction
					.getType()) {
				UIUtils.downloadAttachment(clientTransaction.getID(),
						ClientTransaction.TYPE_CASH_SALES,
						brandingTheme.getID());
			}
			else if (ClientTransaction.TYPE_PURCHASE_ORDER == clientTransaction
					.getType()) {
				UIUtils.downloadAttachment(clientTransaction.getID(),
						ClientTransaction.TYPE_PURCHASE_ORDER,
						brandingTheme.getID());
			}
			
		} else {
			// for printing multiple documents
			StringBuffer ids = new StringBuffer();

			for (int i = 0; i < list.size(); i++) {
				InvoicesList invoice = list.get(i);

				if (invoice.isPrint()) {

					String id = String.valueOf(invoice.getTransactionId());

					ids = ids.append(id + ",");

				}
			}

			String[] arrayIds = ids.toString().split(",");
			int type = 0;
			for (int i = 0; i < list.size(); i++) {
				InvoicesList invoicesList = list.get(i);
				if (invoicesList.isPrint()) {
					if (Integer.parseInt(arrayIds[0]) == invoicesList
							.getTransactionId()) {

						type = invoicesList.getType();
					}
				}

			}

			if (type == ClientTransaction.TYPE_INVOICE) {
				UIUtils.downloadMultipleAttachment(ids.toString(),
						ClientTransaction.TYPE_INVOICE, brandingTheme.getID());

			} else if (type == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
				UIUtils.downloadMultipleAttachment(ids.toString(),
						ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
						brandingTheme.getID());

			} else if (type == ClientTransaction.TYPE_ESTIMATE) {
				UIUtils.downloadMultipleAttachment(ids.toString(),
						ClientTransaction.TYPE_ESTIMATE, brandingTheme.getID());

			} else if (type == ClientTransaction.TYPE_CASH_SALES) {
				UIUtils.downloadMultipleAttachment(ids.toString(),
						ClientTransaction.TYPE_CASH_SALES,
						brandingTheme.getID());
			}else if (type == ClientTransaction.TYPE_PURCHASE_ORDER
					) {
				UIUtils.downloadAttachment(clientTransaction.getID(),
						ClientTransaction.TYPE_PURCHASE_ORDER,
						brandingTheme.getID());
			}
			

		}

	}

	@Override
	protected boolean onOK() {
		if (brandingThemeTypeCombo.getSelectedValue().equals(null)) {
			brandingThemeTypeCombo.setSelected(messages.standardTheme());
		}
		print();
		return true;
	}

	@Override
	public void setFocus() {
		brandingThemeTypeCombo.setFocus();

	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
