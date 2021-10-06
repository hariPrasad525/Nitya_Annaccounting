package com.nitya.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.AddNewButton;
import com.nitya.accounter.web.client.core.ClientComputaionFormulaFunction;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

public class ComputationFormulaDialog extends
		BaseDialog<ClientComputaionFormulaFunction> {

	private DynamicForm mainForm;

	private ValueCallBack<List<ClientComputaionFormulaFunction>> successCallback;
	private ComputationFormulaTable table;
	private AddNewButton itemTableButton;

	public ComputationFormulaDialog(String string) {
		super(string);
		this.getElement().setId("ComputationFormulaDialog");
		getWidget().getParent().addStyleName("group_dialogue_list");
		createControls();
	}

	public void createControls() {
		mainForm = new DynamicForm("mainForm");

		table = new ComputationFormulaTable();

		StyledPanel attendanceTablePanel = new StyledPanel(
				"ComputationFormulaTablePanel");
		Label attTableTitle = new Label(Global.get().messages2()
				.table(messages.attendance()));
		attTableTitle.setStyleName("editTableTitle");
		attendanceTablePanel.add(attTableTitle);
		attendanceTablePanel.add(table);
		mainForm.add(attendanceTablePanel);

		bodyLayout.add(mainForm);
	}

	@Override
	protected void createButtons(StyledPanel footer) {
		super.createButtons(footer);

		itemTableButton = new AddNewButton();
		itemTableButton.getElement().setAttribute("data-icon", "add");
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientComputaionFormulaFunction row = new ClientComputaionFormulaFunction();
				table.add(row);
			}
		});

		addButton(footer, itemTableButton);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result = table.validate();
		return result;
	}

	@Override
	protected boolean onOK() {
		if (successCallback != null) {
			successCallback.execute(createFunctions());
		}
		return true;
	}

	public void addCallback(
			ValueCallBack<List<ClientComputaionFormulaFunction>> successCallback) {
		this.successCallback = successCallback;
	}

	private List<ClientComputaionFormulaFunction> createFunctions() {
		List<ClientComputaionFormulaFunction> functions = new ArrayList<ClientComputaionFormulaFunction>();
		functions = table.getAllRows();
		return functions;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setData(List<ClientComputaionFormulaFunction> formulas) {
		if (table == null) {
			return;
		}
		table.setAllRows(formulas);
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

}
