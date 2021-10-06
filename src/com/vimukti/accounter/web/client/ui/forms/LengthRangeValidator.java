package com.vimukti.accounter.web.client.ui.forms;

public class LengthRangeValidator implements Validator {

	private Integer maxLength;
	private Integer minLength;

	public void setMax(Integer maxLength) {
		this.maxLength = maxLength;

	}

	public void setMin(Integer minLength) {
		this.minLength = minLength;

	}

	@Override
	public boolean validate(FormItem formItem) {

		TextItem textItem = (TextItem) formItem;
		int lenght = textItem.getValue().toString().length();
		if ((lenght >= this.maxLength) && (lenght <= this.minLength)) {
			return true;
		} else
			return false;

	}

}
