package com.nitya.accounter.web.client.ui;

import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.RadioGroupItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class AddItemTaxDialog extends BaseDialog<ClientTAXItem> {

	public TextItem taxText;
	public RadioGroupItem taxableRadio;
	public DynamicForm taxElementform;

	public AddItemTaxDialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("AddItemTaxDialog");
		initiliase();
		ViewManager.getInstance().showDialog(this);
	}

	private void initiliase() {
		taxText = new TextItem(messages.itemTax(), "taxText");
		taxText.setRequired(true);

		taxableRadio = new RadioGroupItem();
		taxableRadio.setShowTitle(false);
		taxableRadio.setValueMap(messages.taxable(), messages.nonTaxable());

		taxElementform = new DynamicForm("taxElementform");
		taxElementform.add(taxText, taxableRadio);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(taxElementform);
		setBodyLayout(mainVLay);

	}

	@Override
	public Object getGridColumnValue(ClientTAXItem obj, int index) {
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
		taxText.setFocus();

	}

	@Override
	public boolean isViewDialog() {
		return false;
	}

}
