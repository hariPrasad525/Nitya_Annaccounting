package com.nitya.accounter.web.client.ui.settings;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseDialog;

public class AutomaticSequenceDialog extends BaseDialog {

	private HTML paraHTML;
	private FlexTable optionsTable;
	private Label invoicePrefixlabel, creditNoteLabel, nextNumberLabel;
	private TextBox invoiceBox, creditBox, nextBox;
	private StyledPanel subLayoutPanel, invoicePanel, creditPanel, nextPanel;

	public AutomaticSequenceDialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("AutomaticSequenceDialog");
		createControls();
	}

	private void createControls() {
		subLayoutPanel = new StyledPanel("subLayoutPanel");
		paraHTML = new HTML(messages.automaticSequencingData());

		invoicePanel = new StyledPanel("invoicePanel");
		invoicePrefixlabel = new Label(messages.invoicePrefix());
		invoiceBox = new TextBox();
		invoiceBox.setText(messages.invoicePrefixValue());
		invoicePanel.add(invoicePrefixlabel);
		invoicePanel.add(invoiceBox);

		creditPanel = new StyledPanel("creditPanel");
		creditNoteLabel = new Label(messages.creditNotePrefix());
		creditBox = new TextBox();
		creditBox.setText(messages.creditNotePrefixValue());
		creditPanel.add(creditNoteLabel);
		creditPanel.add(creditBox);

		nextPanel = new StyledPanel("nextPanel");
		nextNumberLabel = new Label(messages.nextNumber());
		nextBox = new TextBox();
		nextBox.setText(messages.nextNumberValue());
		nextPanel.add(nextNumberLabel);
		nextPanel.add(nextBox);

		optionsTable = new FlexTable();
		optionsTable.setWidget(0, 0, invoicePanel);
		optionsTable.setWidget(0, 1, creditPanel);
		optionsTable.setWidget(0, 2, nextPanel);

		okbtn.setText(messages.save());

		subLayoutPanel.add(paraHTML);
		subLayoutPanel.add(optionsTable);
		setBodyLayout(subLayoutPanel);
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}

}
