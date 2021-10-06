package com.nitya.accounter.web.client.ui.fixedassets;

import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.core.ClientFixedAsset;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;

public class NoteDialog extends BaseDialog {

	public TextAreaItem noteArea;
	private ClientFixedAsset asset;

	public NoteDialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("NoteDialog");
		createControl();
	}

	private void createControl() {
		// mainPanel.setSpacing(3);
		Label noteLbl = new Label(messages.note());
		noteArea = new TextAreaItem("", "noteArea");
		// noteArea.setWidth(100);
		DynamicForm noteForm = new DynamicForm("noteForm");
		// noteForm.setWidth("100%");
		noteForm.add(noteArea);
		// noteForm.removeCell(0, 0);
		StyledPanel notePanel = new StyledPanel("notePanel");
		// notePanel.setWidth("100%");
		notePanel.add(noteLbl);
		notePanel.add(noteForm);
		setBodyLayout(notePanel);
		okbtn.setText(messages.save());
		// setWidth("350px");
	}

	@Override
	protected boolean onOK() {
		if (noteArea.getValue().length() != 0) {
			return executeUpdate(asset);
		} else {
			return false;
		}
	}

	public boolean executeUpdate(ClientFixedAsset asset) {
		setAsset(asset);
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (noteArea.getValue().length() == 0) {
			result.addError(noteArea, messages.pleaseenterthenote());
		}
		return result;
	}

	public ClientFixedAsset getAsset() {
		return asset;
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	public void setAsset(ClientFixedAsset asset) {
		this.asset = asset;
	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}
}
