package com.vimukti.accounter.web.client.ui.company;

import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class ChangeStartDateDialog extends CustomDialog {

	public ChangeStartDateDialog(String title, String desc) {
		super(false, true);
		this.getElement().setId("ChangeStartDateDialog");
		createControls();
	}

	private void createControls() {

		// setHeight(150);

		StyledPanel bodyLayout = new StyledPanel("bodyLayout");
		DynamicForm form = new DynamicForm("form");
		// form.setWidth(250);
		DateItem dateItem = new DateItem(messages.startDate(), "dateItem");
		dateItem.setRequired(true);
		// dateItem.setUseTextField(true);
		form.add(dateItem);
		bodyLayout.add(form);
		add(bodyLayout);

	}

	// @Override
	// public Object getGridColumnValue(IsSerializable obj, int index) {
	// return null;
	// }
	//
	// @Override
	// public void deleteFailed(AccounterException caught) {
	//
	// }
	//
	// @Override
	// public void deleteSuccess(IAccounterCore result){
	//
	// }
	//
	// @Override
	// public void saveSuccess(IAccounterCore object) {
	// }
	//
	// @Override
	// public void saveFailed(AccounterException exception) {
	//
	// }
	//
	//
	// @Override
	// protected boolean onOK() {
	// // TODO Auto-generated method stub
	// return false;
	// }

}
