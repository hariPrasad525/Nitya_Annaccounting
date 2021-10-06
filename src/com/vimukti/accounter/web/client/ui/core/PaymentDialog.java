package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.customers.CustomerPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivePaymentAction;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class PaymentDialog extends BaseDialog {
	RadioGroupItem typeRadio;
	private final String RECEIVE_PAYMENT = messages.receivePayment();
	private final String CUSTOMER_PREPAYMENT = messages.payeePrePayment(Global
			.get().Customer());

	public PaymentDialog() {
		super(messages.payments(), "");
		this.getElement().setId("PaymentDialog");
		setText(messages.payments());
		createControls();
	}

	public PaymentDialog(AccounterAsyncCallback<IAccounterCore> callBack) {
		super(messages.payments(), "");
		this.getElement().setId("PaymentDialog");
		setText(messages.payments());
		createControls();
		ViewManager.getInstance().showDialog(this);
	}

	public void createControls() {
		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);

		typeRadio.setValue(RECEIVE_PAYMENT, CUSTOMER_PREPAYMENT);
		typeRadio.setDefaultValue(RECEIVE_PAYMENT);
		DynamicForm typeForm = new DynamicForm("typeForm");
		typeForm.add(typeRadio);

		setBodyLayout(typeForm);

	}

	public void setFocus() {
		cancelBtn.setFocus(true);
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();
			if (radio.equals(RECEIVE_PAYMENT)) {
				new ReceivePaymentAction().run(null, false);
			} else if (radio.equals(CUSTOMER_PREPAYMENT)) {
				new CustomerPaymentsAction().run(null, false);
			}
		}
		removeFromParent();
		return true;
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
