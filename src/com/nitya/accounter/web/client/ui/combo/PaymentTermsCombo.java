/**
 * 
 */
package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.core.ClientPaymentTerms;
import com.nitya.accounter.web.client.ui.PaymentTermListDialog;

/**
 * @author vimukti16
 * 
 */
public class PaymentTermsCombo extends CustomCombo<ClientPaymentTerms> {

	/**
	 * @param title
	 */
	public PaymentTermsCombo(String title) {
		super(title, "PaymentTermsCombo");
		initCombo(getCompany().getPaymentsTerms());
	}

	public PaymentTermsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1, "PaymentTermsCombo");
		initCombo(getCompany().getPaymentsTerms());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.paymentTerms();
	}

	@Override
	protected String getDisplayName(ClientPaymentTerms object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		PaymentTermListDialog vendorGroup = new PaymentTermListDialog();
		vendorGroup.setVisible(false);
		vendorGroup.init();
		vendorGroup.setCallback(createAddNewCallBack());
		vendorGroup.showAddEditTermDialog(null);
	}

	@Override
	protected String getColumnData(ClientPaymentTerms object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
