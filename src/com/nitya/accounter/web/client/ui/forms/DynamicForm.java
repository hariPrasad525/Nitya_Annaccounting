package com.nitya.accounter.web.client.ui.forms;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.combo.ItemCombo;
import com.nitya.accounter.web.client.ui.widgets.DatePicker;

@SuppressWarnings("rawtypes")
public class DynamicForm extends FlowPanel {

	protected static final AccounterMessages messages = Global.get().messages();

	public DynamicForm(String style) {
		super();
		setStyleName(style);
	}

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		for (Widget item : getChildren()) {
			if (item instanceof FormItem) {
				FormItem fi = (FormItem) item;
				if (!fi.validate()) {
					result.addError(item, messages.pleaseEnter(item.getTitle()));
				}
			}
		}
		return result;
	}

	public void add(FormItem... items) {
		for (FormItem item : items) {
			add(item);
		}
	}

	public void setEnabled(boolean isEnabled) {
		for (Widget child : this.getChildren()) {
			if (child instanceof HasEnabled) {
				((HasEnabled) child).setEnabled(isEnabled);
			}
		}
	}

	public static ValidationResult validate(DynamicForm... dynamicForms) {
		ValidationResult result = new ValidationResult();
		for (DynamicForm form : dynamicForms) {
			if (form != null) {
				result.add(form.validate());
			}
		}
		return result;
	}

	public void add(ItemCombo itemCombo, DatePicker fromDateValue, DatePicker toDateValue, TextItem hours) {
		add(itemCombo);
		add(fromDateValue);
		add(toDateValue);
		add(hours);
	}
}
