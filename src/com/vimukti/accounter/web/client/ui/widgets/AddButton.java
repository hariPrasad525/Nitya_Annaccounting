package com.vimukti.accounter.web.client.ui.widgets;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class AddButton extends ImageButton {

	public AddButton(String title) {
		super(messages.add(), Accounter.getFinanceImages().addIcon(), "add");
		this.setTitle(messages.clickToAdd(title));
	}
}
