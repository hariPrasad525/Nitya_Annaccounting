package com.nitya.accounter.web.client.ui.edittable;

import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class NewItemDialog extends BaseDialog<ClientItem> {

	public NewItemDialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("NewItemDialog");
		// setWidth("300px");
		ViewManager.getInstance().showDialog(this);
	}

	@Override
	protected boolean onOK() {
		return false;
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
