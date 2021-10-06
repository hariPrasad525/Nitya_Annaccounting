package com.nitya.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.core.ClientPayrollUnit;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class NewClientAttendanceOrProductionDialog extends
		BaseDialog<ClientAttendanceOrProductionType> {

	private DynamicForm form;
	private TextItem name;
	private ClientAttendanceOrProductionType selectedAttendanceOrProductionType;
	private SelectCombo leaveType;
	private LabelItem daysType;
	private PayRollUnitCombo payrollUnitCombo;
	private List<String> attendanceOrProductionType;
	private DynamicForm periodForm;

	public NewClientAttendanceOrProductionDialog(String text,
			ClientAttendanceOrProductionType clientAttendanceOrProductionType) {
		super(text);
		this.getElement().setId("NewClientAttendanceOrProductionDialog");
		this.selectedAttendanceOrProductionType = clientAttendanceOrProductionType;
		if (this.selectedAttendanceOrProductionType == null) {
			selectedAttendanceOrProductionType = new ClientAttendanceOrProductionType();
		}
		createControl();
	}

	private void createControl() {
		form = new DynamicForm("newClientAttendanceForm");
		name = new TextItem(messages.name(), "attendanceName");
		name.setRequired(true);

		StyledPanel layout = new StyledPanel("layout");
		form.add(name);

		leaveType = new SelectCombo(messages.attendanceOrProductionType());
		leaveType
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						selectionChanged(selectItem);
					}
				});
		leaveType.initCombo(getAttendanceTypeLIst());
		leaveType.setRequired(true);
		form.add(leaveType);

		periodForm = new DynamicForm("periodForm");

		daysType = new LabelItem(messages.period(), "daysTypeCombo");
		daysType.setValue(messages.days());

		payrollUnitCombo = new PayRollUnitCombo(messages.payrollUnit(),
				"payrollUnitsListCombo",
				selectedAttendanceOrProductionType.getUnit());
		payrollUnitCombo.setRequired(true);

		form.add(periodForm);

		layout.add(form);
		setBodyLayout(layout);
		String typeName = ClientAttendanceOrProductionType
				.getTypeName(selectedAttendanceOrProductionType.getType());

		if (typeName != null) {
			leaveType.setSelected(typeName);
			selectionChanged(typeName);
		}

		if (this.selectedAttendanceOrProductionType.getID() != 0) {
			leaveType.setEnabled(false);
			name.setValue(selectedAttendanceOrProductionType.getName());
			if (selectedAttendanceOrProductionType.getType() != ClientAttendanceOrProductionType.TYPE_PRODUCTION) {
				daysType.setValue(ClientPayHead
						.getCalculationPeriod(selectedAttendanceOrProductionType
								.getPeriodType()));
			} else {
				payrollUnitCombo.setEnabled(false);
			}
		}
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = exception;
		String errorString = AccounterExceptions
				.getErrorString(accounterException);
		Accounter.showError(errorString);
	}

	protected void selectionChanged(String selectItem) {
		periodForm.clear();
		if (selectItem.equals(messages.productionType())) {
			periodForm.add(payrollUnitCombo);
		} else {
			periodForm.add(daysType);
		}
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		result.add(periodForm.validate());
		return result;
	}

	private List<String> getAttendanceTypeLIst() {
		attendanceOrProductionType = new ArrayList<String>();
		// attendanceOrProductionType.add(messages.leaveWithPay());
		// attendanceOrProductionType.add(messages.leaveWithoutPay());
		attendanceOrProductionType.add(messages.productionType());
		attendanceOrProductionType.add(messages.userDefinedCalendar());
		return attendanceOrProductionType;
	}

	@Override
	protected boolean onOK() {
		createorUpdateObject();
		return true;
	}

	private void createorUpdateObject() {
		selectedAttendanceOrProductionType.setName(name.getValue());
		selectedAttendanceOrProductionType
				.setType(leaveType.getSelectedIndex() + 1);
		if (selectedAttendanceOrProductionType.getType() != ClientAttendanceOrProductionType.TYPE_PRODUCTION) {
			selectedAttendanceOrProductionType
					.setPeriodType(ClientPayHead.CALCULATION_PERIOD_DAYS);
		} else {
			ClientPayrollUnit selectedValue = payrollUnitCombo
					.getSelectedValue();
			if (selectedValue != null) {
				selectedAttendanceOrProductionType.setUnit(selectedValue
						.getID());
			}
		}
		AccounterAsyncCallback<Long> callback = new AccounterAsyncCallback<Long>() {

			@Override
			public void onException(AccounterException exception) {
				AccounterException accounterException = exception;
				String errorString = AccounterExceptions
						.getErrorString(accounterException);
				Accounter.showError(errorString);
			}

			@Override
			public void onResultSuccess(Long result) {
				selectedAttendanceOrProductionType.setID(result);
				ActionCallback<ClientAttendanceOrProductionType> callback = getCallback();
				if (callback != null) {
					callback.actionResult(selectedAttendanceOrProductionType);
				}
			}
		};
		if (selectedAttendanceOrProductionType.getID() == 0) {
			Accounter.createCRUDService().create(
					selectedAttendanceOrProductionType, callback);
		} else {
			Accounter.createCRUDService().update(
					selectedAttendanceOrProductionType, callback);
		}

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onCancel() {
		if (getCallback() == null) {
			super.onCancel();
		}
		return true;
	}

}
