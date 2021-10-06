package com.nitya.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientComputaionFormulaFunction;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.core.Validate;
import com.nitya.accounter.web.client.ui.edittable.DeleteColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.StringListColumn;

public class ComputationFormulaTable extends
		EditTable<ClientComputaionFormulaFunction> {

	String[] functionTypes = { "Add Pay Head", "Subtract Pay Head",
			"Divide Attendance", "Multiply Attendance" };
	List<String> functionTypesList = new ArrayList<String>();

	public ComputationFormulaTable() {
		for (int i = 0; i < functionTypes.length; i++) {
			functionTypesList.add(functionTypes[i]);
		}
		addEmptyRecords();
	}

	@Override
	protected int getDefaultEmptyRowsSize() {
		return 1;
	}

	@Override
	protected ClientComputaionFormulaFunction getEmptyRow() {
		return new ClientComputaionFormulaFunction();
	}

	@Override
	protected void initColumns() {
		this.addColumn(new StringListColumn<ClientComputaionFormulaFunction>() {

			@Override
			protected String getValue(ClientComputaionFormulaFunction row) {
				if (row.getFunctionType() == 0) {
					String string = functionTypesList.get(0);
					row.setFunctionType(functionTypesList.indexOf(string) + 1);
					return string;
				}
				return functionTypesList.get(row.getFunctionType() - 1);
			}

			@Override
			protected List<String> getData() {
				return functionTypesList;
			}

			@Override
			protected void setValue(ClientComputaionFormulaFunction row,
					String newValue) {
				row.setFunctionType(functionTypesList.indexOf(newValue) + 1);
				update(row);
			}

			@Override
			protected String getColumnName() {
				return messages.functionType();
			}

			@Override
			public String getValueAsString(ClientComputaionFormulaFunction row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new ComputationFormulaTypeColumn() {
			@Override
			protected String getColumnName() {
				return "";
			}
		});

		this.addColumn(new DeleteColumn<ClientComputaionFormulaFunction>());
	}

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		for (ClientComputaionFormulaFunction row : getAllRows()) {
			if (row.getFunctionType() != 0 && row.getPayHead() == 0
					&& row.getAttendanceType() == 0) {
				result.addError(row, messages.payheadOrAttendanceNotEmpty());
			}
		}
		return result;
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
