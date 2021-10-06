package com.nitya.accounter.web.client.ui.core;

import com.nitya.accounter.web.client.ui.forms.FormItem;
import com.nitya.accounter.web.client.ui.forms.Validator;

public class IntegerRangeValidator implements Validator {

	private int minimum = 0;
	private int maximum = 0;
	private boolean isMinValidate = false;
	private boolean isMaxValidate = false;

	@Override
	public boolean validate(FormItem formItem) {
		try {
			if (formItem.getValue()!=null && !formItem.getValue().toString().isEmpty()) {
				int ingval = Integer.parseInt(formItem.getValue().toString());
				if (isMinValidate) {
					if (!(ingval < minimum))
						return true;
					else
						return false;
				} else if (isMaxValidate) {
					if (!(ingval > maximum))
						return true;
					else
						return false;
				}
				return true;
			} else
				return true;	
		} catch (Exception e) {
			return false;
		}

	}

	public void setMin(int min) {
		// A flag to indicate minimum-range validation is about to check
		isMinValidate = true;
		minimum = min;
	}

	public void setMax(int max) {
		// A flag to indicate maximum-range validation is about to check
		isMaxValidate = true;
		maximum = max;
	}
}
