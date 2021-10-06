package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.Validator;

/**
 * Custom validation Class for all FormItems.give array of strings those values
 * should not be selected in formItem,
 * 
 * @author kumar kasimala
 * 
 */
public class Validate implements Validator {
	public String[] list;

	public Validate(String... list) {
		this.list = list;
	}

	@Override
	public boolean validate(FormItem formItem) {
		if (formItem.getValue() == null)
			return false;
		String str = formItem.getValue().toString();
		if (str.equals("-1") || str.equals("none") || str.isEmpty())
			return false;
		if (list != null) {
			for (String val : list)
				if (val == str)
					return false;
		}
		return true;
	}

}
