package com.nitya.accounter.web.client.ui;

import com.nitya.accounter.web.client.core.ClientTDSChalanDetail;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.RadioGroupItem;
import com.nitya.accounter.web.client.ui.vat.TDSChalanDetailsAction;

public class SelectChallanTypeDialog extends BaseDialog<ClientTDSChalanDetail> {
	RadioGroupItem typeRadio;
	public static final int Form26Q = 1;
	public static final int Form27Q = 2;
	public static final int Form27EQ = 3;
	private DynamicForm typeForm;
	private boolean isDependent = true;

	public SelectChallanTypeDialog() {
		super(messages.selectItemType(), messages.selectOneOfItem());
		this.getElement().setId("SelectChallanTypeDialog");
		createControls();
	}

	private void createControls() {

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);

		typeRadio.setValueMap(messages.form26Q(), messages.form27Q());
		// ,messages.form27EQ());
		typeRadio.setDefaultValue(messages.form26Q());

		typeForm = new DynamicForm("typeForm");
		typeForm.add(typeRadio);
		// typeForm.setWidth("100%");

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		// setWidth("320px");
		ViewManager.getInstance().showDialog(this);
	}

	@Override
	protected ValidationResult validate() {
		return typeForm.validate();
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();

			TDSChalanDetailsAction action = new TDSChalanDetailsAction();
			action.setDependent(isDependent);
			if (radio.equals(messages.form26Q())) {
				action.setType(ClientTDSChalanDetail.Form26Q);
			} else if (radio.equals(messages.form27Q())) {
				action.setType(ClientTDSChalanDetail.Form27Q);
			} else if (radio.equals(messages.form27EQ())) {
				action.setType(ClientTDSChalanDetail.Form27EQ);
			}
			action.setCallback(getCallback());
			action.run();
		}

		return true;
	}

	public void setDependent(boolean isDependent) {
		this.isDependent = isDependent;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}
}
