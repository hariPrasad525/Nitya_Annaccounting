package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.ui.JNSI;

public abstract class QuantityColumn<T> extends TextEditColumn<T> {

	@Override
	protected String getValue(T row) {
		ClientQuantity value = getQuantity(row);
		if (value != null)
			return String.valueOf(value.getValue());
		else
			return "";
	}

	protected abstract ClientQuantity getQuantity(T row);

	@Override
	protected void setValue(T row, String value) {
		try {
			if (value.isEmpty()) {
				value = "1";
			}
			ClientQuantity quantity = getQuantity(row);
			quantity.setValue(Double.parseDouble(JNSI.getCalcultedAmount(value)));
			setQuantity(row, quantity);
		} catch (NumberFormatException e) {
		}
	}

	protected abstract void setQuantity(T row, ClientQuantity d);

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected String getColumnName() {
		return messages.qty();
	}

	@Override
	protected void configure(TextBoxBase textBox) {
		super.configure(textBox);
		textBox.addStyleName("quantity");
	}
}
