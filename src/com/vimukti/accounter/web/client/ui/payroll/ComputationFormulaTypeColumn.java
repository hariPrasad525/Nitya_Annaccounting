package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientComputaionFormulaFunction;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboBox;
import com.vimukti.accounter.web.client.ui.edittable.ComboChangeHandler;
import com.vimukti.accounter.web.client.ui.edittable.EditColumn;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;

public class ComputationFormulaTypeColumn extends
		EditColumn<ClientComputaionFormulaFunction> {

	PayHeadDropDownTable payheadDropdown = new PayHeadDropDownTable(false);

	AttendanceDropDownTable attendanceDropdown = new AttendanceDropDownTable();

	public ComputationFormulaTypeColumn() {
	}

	@Override
	public int getWidth() {
		return -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(IsWidget widget,
			RenderContext<ClientComputaionFormulaFunction> context) {
		HorizontalPanel panel = (HorizontalPanel) widget;

		ClientComputaionFormulaFunction row = context.getRow();
		IAccounterCore value = getValue(row);

		ComboBox<ClientComputaionFormulaFunction, ClientPayHead> payheadComboBox = (ComboBox<ClientComputaionFormulaFunction, ClientPayHead>) panel
				.getWidget(0);

		ComboBox<ClientComputaionFormulaFunction, ClientAttendanceOrProductionType> attendanceCombobox = (ComboBox<ClientComputaionFormulaFunction, ClientAttendanceOrProductionType>) panel
				.getWidget(1);

		if (row.getFunctionType() == ClientComputaionFormulaFunction.FUNCTION_ADD_PAY_HEAD
				|| row.getFunctionType() == ClientComputaionFormulaFunction.FUNCTION_SUBSTRACT_PAY_HEAD) {
			payheadComboBox.setValue((ClientPayHead) value);
			payheadComboBox.setDesable(false);
			attendanceCombobox.setDesable(true);
			payheadComboBox.setVisible(true);
			attendanceCombobox.setVisible(false);
		} else if (row.getFunctionType() == ClientComputaionFormulaFunction.FUNCTION_MULTIPLY_ATTENDANCE
				|| row.getFunctionType() == ClientComputaionFormulaFunction.FUNCTION_DIVIDE_ATTENDANCE) {
			attendanceCombobox
					.setValue((ClientAttendanceOrProductionType) value);
			payheadComboBox.setDesable(true);
			attendanceCombobox.setDesable(false);
			payheadComboBox.setVisible(false);
			attendanceCombobox.setVisible(true);
		}
	}

	private IAccounterCore getValue(ClientComputaionFormulaFunction row) {
		if (row.getFunctionType() == ClientComputaionFormulaFunction.FUNCTION_ADD_PAY_HEAD
				|| row.getFunctionType() == ClientComputaionFormulaFunction.FUNCTION_SUBSTRACT_PAY_HEAD) {
			return payheadDropdown.getPayHead(row.getPayHead());
		} else {
			return attendanceDropdown.getAttendance(row.getAttendanceType());
		}
	}

	@Override
	public IsWidget getWidget(
			RenderContext<ClientComputaionFormulaFunction> context) {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		ComboBox<ClientComputaionFormulaFunction, ClientPayHead> payheadComboBox = new ComboBox<ClientComputaionFormulaFunction, ClientPayHead>();
		AbstractDropDownTable<ClientPayHead> payheadDisplayTable = payheadDropdown;
		payheadComboBox.setDropDown(payheadDisplayTable);
		payheadComboBox
				.setComboChangeHandler(new ComboChangeHandler<ClientComputaionFormulaFunction, ClientPayHead>() {

					@Override
					public void onChange(ClientComputaionFormulaFunction row,
							ClientPayHead newValue) {
						setValue(row, newValue);
					}

					@Override
					public void onAddNew(final String text) {

					}
				});
		payheadComboBox.setRow(context.getRow());

		ComboBox<ClientComputaionFormulaFunction, ClientAttendanceOrProductionType> attendanceComboBox = new ComboBox<ClientComputaionFormulaFunction, ClientAttendanceOrProductionType>();
		final AbstractDropDownTable<ClientAttendanceOrProductionType> attendanceDisplayTable = attendanceDropdown;
		attendanceComboBox.setDropDown(attendanceDisplayTable);
		attendanceComboBox
				.setComboChangeHandler(new ComboChangeHandler<ClientComputaionFormulaFunction, ClientAttendanceOrProductionType>() {

					@Override
					public void onChange(ClientComputaionFormulaFunction row,
							ClientAttendanceOrProductionType newValue) {
						setValue(row, newValue);
					}

					@Override
					public void onAddNew(final String text) {

					}
				});
		attendanceComboBox.setRow(context.getRow());

		horizontalPanel.add(payheadComboBox);
		horizontalPanel.add(attendanceComboBox);

		return horizontalPanel;
	}

	protected void setValue(ClientComputaionFormulaFunction row,
			IAccounterCore newValue) {
		if (newValue instanceof ClientPayHead) {
			ClientPayHead payhead = (ClientPayHead) newValue;
			row.setPayHead(payhead.getID());
			row.setClientPayHead(payhead);
		} else {
			ClientAttendanceOrProductionType attendanceType = (ClientAttendanceOrProductionType) newValue;
			row.setAttendanceType(attendanceType.getID());
			row.setClientAttendanceType(attendanceType);
		}
	}

	@Override
	public String getValueAsString(ClientComputaionFormulaFunction row) {
		return getValue(row).toString();
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}

}
