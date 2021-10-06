package com.nitya.accounter.web.client.ui.settings;

import com.nitya.accounter.web.client.core.ClientUnit;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;

public abstract class AddUnitsGrid extends EditTable<ClientUnit> {

	public AddUnitsGrid() {
	}

	@Override
	protected ClientUnit getEmptyRow() {
		return new ClientUnit();
	}

	@Override
	protected void initColumns() {
		this.addColumn(new TextEditColumn<ClientUnit>() {

			@Override
			protected String getValue(ClientUnit row) {
				return row.getType();
			}

			@Override
			protected void setValue(ClientUnit row, String value) {
				row.setType(value);
				updateUnits(row);
			}

			@Override
			protected String getColumnName() {
				return messages.unitName();
			}

			@Override
			public String getValueAsString(ClientUnit row) {
				return messages.unitName() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		this.addColumn(new TextEditColumn<ClientUnit>() {

			@Override
			protected String getValue(ClientUnit row) {
				return String.valueOf(row.getFactor());
			}

			@Override
			protected void setValue(ClientUnit row, String value) {
				row.setFactor(Double.parseDouble(value));
				updateUnits(row);
			}

			@Override
			protected String getColumnName() {
				return messages.getFactorName();
			}

			@Override
			public String getValueAsString(ClientUnit row) {
				return messages.getFactorName() + "  : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

		});
	}

	public abstract void updateUnits(ClientUnit clientUnit);
}
