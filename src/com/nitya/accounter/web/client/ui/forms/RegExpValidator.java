package com.nitya.accounter.web.client.ui.forms;

public class RegExpValidator implements Validator {

	private String expression;

	public void setExpression(String exp) {
		this.expression = exp;

	}

	@Override
	public boolean validate(FormItem formItem) {
		TextItem textItem = (TextItem) formItem;
		if (textItem.getDisplayValue().equals(""))
			return true;
		if (textItem.getValue().toString().matches(this.expression)) {
			return true;
		} else
			return false;
	}

}
