package com.vimukti.accounter.web.client.ui.customers;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.company.ManageSupportListAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.vat.NewVatItemAction;

public class TaxDialog extends BaseDialog<ClientTAXItemGroup> {
	RadioGroupItem typeRadio;
	private final String TAXGROUP = messages.taxGroup();
	private final String TAXITEM = messages.taxItem();
	private final String GROUP = messages.group();
	private FormItem actionSource;

	// private ViewConfiguration configuration;
	public TaxDialog() {
		super(messages.tax(), messages.selectOneType());
		this.getElement().setId("TaxDialog");
		setText(messages.tax());
		createControls();
	}

	public <T> TaxDialog(AccounterAsyncCallback<T> callBack) {
		super(messages.tax(), messages.selectOneType());
		this.getElement().setId("TaxDialog");
		setText(messages.tax());
		createControls();
	}

	public <T> TaxDialog(AccounterAsyncCallback<T> callBack,
			FormItem actionSource) {
		super(messages.tax(), messages.selectOneType());
		this.getElement().setId("TaxDialog");
		this.actionSource = actionSource;
		setText(messages.tax());
		createControls();
	}

	private void createControls() {
		typeRadio = new RadioGroupItem("", GROUP);
		typeRadio.setShowTitle(false);
		typeRadio.setValue(TAXGROUP, TAXITEM);
		DynamicForm typeForm = new DynamicForm("typeForm");
		typeForm.add(typeRadio);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(typeForm);

		// okbtn.setWidth("60px");
		// cancelBtn.setWidth("60px");

		setBodyLayout(mainVLay);
		// setWidth("300px");

	}

	public void setFocus() {
		cancelBtn.setFocus(true);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		String radio = typeRadio.getValue().toString();
		if (!radio.equals(TAXGROUP) && !radio.equals(TAXITEM)) {
			result.addError(this, messages.pleaseSelectTaxType());
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();

			if (radio.equals(TAXGROUP)) {
				// try {
				ManageSupportListAction salesTaxGroupAction = ManageSupportListAction
						.salesTaxGroup();
				salesTaxGroupAction.run(null, true);
				salesTaxGroupAction
						.setCallback(new ActionCallback<ClientTAXGroup>() {

							@Override
							public void actionResult(ClientTAXGroup result) {
								setResult(result);
							}
						});
			} else if (radio.equals(TAXITEM)) {
				// try {
				NewVatItemAction action = new NewVatItemAction();
				action.setCallback(new ActionCallback<ClientTAXItem>() {

					@Override
					public void actionResult(ClientTAXItem result) {
						setResult(result);
					}
				});

				action.run(null, true);

			}

		}
		hide();
		return true;
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
