package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundAction;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsAction;

/**
 * 
 * @author Mandeep Singh
 * 
 */

public class SelectPaymentTypeDialog extends BaseDialog {
	RadioGroupItem typeRadio;

	public SelectPaymentTypeDialog() {
		super(messages.selectPaymentType(), messages.selectPaymentType());
		this.getElement().setId("SelectPaymentTypeDialog");
		createControls();
	}

	private void createControls() {
		setText(messages.selectPaymentType());

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);
		String paymentType;
		paymentType = messages.payeePayment(Global.get().Vendor());

		typeRadio.setValueMap(paymentType,
				messages.customerRefund(Global.get().Customer()));

		typeRadio.setDefaultValue(paymentType);
		final DynamicForm typeForm = new DynamicForm("typeForm");
		typeForm.add(typeRadio);
		// typeForm.setWidth("100%");

		typeForm.add(typeRadio);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(typeForm);

		// okbtn.setWidth("60px");
		// cancelBtn.setWidth("60px");

		setBodyLayout(mainVLay);
		// setWidth("300px");
		ViewManager.getInstance().showDialog(this);
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();
			String paymentType;
			paymentType = messages.payeePayment(Global.get().Vendor());
			if (radio.equals(paymentType)) {
				new VendorPaymentsAction().run(null, false);
			} else if (radio.equals(messages.customerRefund(Global.get()
					.Customer()))) {

				new CustomerRefundAction().run(null, false);
			}
		}
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}

}
