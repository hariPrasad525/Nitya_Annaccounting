package com.nitya.accounter.web.client.ui.banking;

import java.util.LinkedHashMap;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.customers.NewCustomerAction;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.RadioGroupItem;
import com.nitya.accounter.web.client.ui.vendors.NewVendorAction;

public class SelectPayeeDialog extends BaseDialog<ClientPayee> {

	RadioGroupItem typeRadio;

	// private ClientCompany company;
	public SelectPayeeDialog() {
		super(messages.selectPayeeType(), messages.selectOneOfFollowingPayee());
		this.getElement().setId("SelectPayeeDialog");
		// company = FinanceApplication.getCompany();
		createControls();
	}

	private void createControls() {

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);

		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();

		typeRadio.setValueMap(messages.customer(), Global.get().Vendor());
		typeRadio.setDefaultValue(messages.customer());

		DynamicForm typeForm = new DynamicForm("typeForm");
		// typeForm.setIsGroup(true);
		// typeForm.setGroupTitle("Account Type");
		typeForm.add(typeRadio);
		// typeForm.setWidth("100%");

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		// setWidth("350px");
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (typeRadio.getValue() == null) {
			result.addError(this, messages.pleaseSelecPaymentType());
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		String radio = typeRadio.getValue().toString();
		// FIXME--an action is required here
		// okClick();
		if (radio.equals(Global.get().Vendor())) {
			// new VendorPaymentsAction("Not Issued").run();
			NewVendorAction action = new NewVendorAction();
			action.setCallback(new ActionCallback<ClientVendor>() {

				@Override
				public void actionResult(ClientVendor result) {
					setResult(result);
				}
			});

			action.run(null, true);

		} else if (radio.equals(messages.customer())) {
			NewCustomerAction action = new NewCustomerAction();
			action.setCallback(new ActionCallback<ClientCustomer>() {

				@Override
				public void actionResult(ClientCustomer result) {
					setResult(result);
				}
			});

			action.run(null, true);

		}
		hide();
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
}
