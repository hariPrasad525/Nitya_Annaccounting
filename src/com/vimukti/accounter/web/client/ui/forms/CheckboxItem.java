package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;

public class CheckboxItem extends FormItem<Boolean> {

	CheckBoxImpl checkBoxImpl;
	
	public CheckboxItem(String title, String styleName) {
		super(title, styleName);
		checkBoxImpl = GWT.create(CheckBoxImpl.class);
		checkBoxImpl.createControl(title, styleName);
		this.addStyleName("checkBoxImpl");
		this.add(checkBoxImpl);
		
	}

	public CheckboxItem(String title, String styleName, String description) {
		super(title, styleName);
		checkBoxImpl = GWT.create(CheckBoxImpl.class);
		checkBoxImpl.createControl(title, styleName);
		this.addStyleName("checkBoxImpl");
		this.add(checkBoxImpl);
	}

	@Override
	public Boolean getValue() {
		return checkBoxImpl.getValue();
	}

	@Override
	public String getDisplayValue() {
		return checkBoxImpl.getDisplayValue();

	}

	@Override
	public void setValue(Boolean value) {
		checkBoxImpl.setValue(value);

	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		checkBoxImpl.setToolTip(toolTip);
	}


	public void addChangeHandler(ValueChangeHandler<Boolean> ChangeHandler) {
		checkBoxImpl.addChangeHandler(ChangeHandler);
	}

	@Override
	public Widget getMainWidget() {
		return checkBoxImpl.getMainWidget();
	}

	@Override
	public void setEnabled(boolean b) {
		checkBoxImpl.setEnabled(b);
	}

	public boolean isChecked() {
		return getValue();
	}

	public void setTabIndex(int index) {
		checkBoxImpl.setTabIndex(index);
	}

	public void createControl(String title, String styleName) {
		createControl(title, styleName,null);
	}

	public void createControl(String title, String styleName, String description) {
		checkBoxImpl.createControl(title, styleName,description);
	}

}
