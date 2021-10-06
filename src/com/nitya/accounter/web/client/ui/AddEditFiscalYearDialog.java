package com.nitya.accounter.web.client.ui;

import com.nitya.accounter.web.client.core.ClientFiscalYear;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DateItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

public class AddEditFiscalYearDialog extends BaseDialog<ClientFiscalYear> {
	DateItem startDate;

	public AddEditFiscalYearDialog(String title, String desc,
			ClientFiscalYear fiscalYear) {
		super(title, desc);
		this.getElement().setId("AddEditFiscalYearDialog");
		createControls(fiscalYear);
	}

	private void createControls(ClientFiscalYear fiscalYear) {

		startDate = new DateItem(messages.startOfFiscalYear(), "startDate");

		DateItem closeDate = new DateItem(messages.closeOfFiscalYear(),
				"closeDate");
		StyledPanel bodyLayout = new StyledPanel("bodyLayout");
		DynamicForm form = new DynamicForm("form");
		form.add(startDate, closeDate);
		bodyLayout.add(form);
		setBodyLayout(bodyLayout);

	}

	@Override
	public Object getGridColumnValue(ClientFiscalYear obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(AccounterException exception) {

	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setFocus() {
		startDate.setFocus();

	}

	@Override
	public boolean isViewDialog() {
		return false;
	}

}
