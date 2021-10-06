package com.nitya.accounter.web.client.ui.forms;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

public class CheckBoxImpl extends FormItem<Boolean> {

	public CheckBoxImpl(String title, String styleName) {
		super(title, styleName);
	}

	public CheckBoxImpl(String title, String styleName, String description) {
		super(title, styleName);
	}
	
	public CheckBoxImpl() {
		super(null,null);
	}

	private CheckBox checkBox = new CheckBox();

	@Override
	public Boolean getValue() {
		if (checkBox.getValue() == null)
			return false;
		return checkBox.getValue();
	}

	@Override
	public String getDisplayValue() {
		if (checkBox.getText() == null)
			return "";
		return checkBox.getText();

	}

	@Override
	public void setValue(Boolean value) {
		checkBox.setValue(value);

	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		checkBox.setTitle(toolTip);
	}

	public void addChangeHandler(ValueChangeHandler<Boolean> ChangeHandler) {
		this.checkBox.addValueChangeHandler(ChangeHandler);
	}

	@Override
	public Widget getMainWidget() {
		return this.checkBox;
	}

	@Override
	public void setEnabled(boolean b) {
		this.checkBox.setEnabled(b);
	}

	public boolean isChecked() {
		return getValue();
	}

	public void setTabIndex(int index) {
		checkBox.setTabIndex(index);
	}

	public void createControl(String title, String styleName) {
		createControl(title,styleName,null);
	}

	public void createControl(String title, String styleName, String description) {
		this.checkBox = new CheckBox();
		if (description != null)
		this.checkBox.setText(description);
		this.checkBox.addStyleName("checkBox");
		this.add(checkBox);
		this.addStyleName("checkboxItem");

	}

}
