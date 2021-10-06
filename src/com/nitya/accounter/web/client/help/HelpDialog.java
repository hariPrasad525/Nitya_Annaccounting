package com.nitya.accounter.web.client.help;

import com.google.gwt.dom.client.Element;
import com.nitya.accounter.web.client.ui.forms.CustomDialog;

public class HelpDialog extends CustomDialog {

	public HelpDialog(HelpPanel helpPannel) {
//		this.setWidth("100%");
//		this.setHeight("100%");
		this.setModal(false);
		this.getElement().setId("helpDialog");
		Element td = getCellElement(0, 1);
		td.setInnerHTML("");
		td.appendChild(helpPannel.getHorizontalPannel().getElement());
		this.setPopupPosition(470, 200);
		this.add(helpPannel);
	}
}
