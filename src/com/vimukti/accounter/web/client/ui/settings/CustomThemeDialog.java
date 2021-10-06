package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class CustomThemeDialog extends BaseDialog {

	private HTML titleHTML;
	private TextBox titleBox;
	private FlexTable flexTable, subTable;
	private StyledPanel buttonPanel;

	public CustomThemeDialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("CustomThemeDialog");
		createControls();
	}

	private void createControls() {
		subTable = new FlexTable();
		titleHTML = new HTML(messages.yourTitle());
		titleBox = new TextBox();
		subTable.setWidget(0, 0, titleHTML);
		subTable.setWidget(1, 0, titleBox);

		buttonPanel = new StyledPanel("buttonPanel");

		flexTable = new FlexTable();
		flexTable.setWidget(0, 0, subTable);
		flexTable.setWidget(1, 0, buttonPanel);
		setBodyLayout(flexTable);
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setFocus() {
		titleBox.setFocus(true);

	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}

}
