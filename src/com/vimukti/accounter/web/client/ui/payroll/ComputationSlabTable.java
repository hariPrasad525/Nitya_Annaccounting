package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientComputationSlab;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.DateColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.StringListColumn;

public class ComputationSlabTable extends EditTable<ClientComputationSlab> {

	private List<String> slabTypes = new ArrayList<String>();

	public ComputationSlabTable() {
		super();
		slabTypes.add("Percentage");
		slabTypes.add("Value");
		addEmptyRecords();
	}

	@Override
	protected void initColumns() {
		this.addColumn(new DateColumn<ClientComputationSlab>() {

			@Override
			protected ClientFinanceDate getValue(ClientComputationSlab row) {
				ClientFinanceDate date = new ClientFinanceDate(row
						.getEffectiveFrom());
				setValue(row, date);
				return date;
			}

			@Override
			protected void setValue(ClientComputationSlab row,
					ClientFinanceDate value) {
				row.setEffectiveFrom(value.getDate());
			}

			@Override
			protected String getColumnName() {
				return messages.effectiveFrom();
			}

			@Override
			public String getValueAsString(ClientComputationSlab row) {
				return "";
			}

			@Override
			public int insertNewLineNumber() {
				return 0;
			}

		});

		this.addColumn(new AmountColumn<ClientComputationSlab>(null, false) {

			@Override
			protected Double getAmount(ClientComputationSlab row) {
				return row.getFromAmount();
			}

			@Override
			protected void setAmount(ClientComputationSlab row, Double value) {
				row.setFromAmount(value);
			}

			@Override
			protected String getColumnName() {
				return messages.fromAmount();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 160;
			}

			@Override
			public String getValueAsString(ClientComputationSlab row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new AmountColumn<ClientComputationSlab>(null, false) {

			@Override
			protected Double getAmount(ClientComputationSlab row) {
				return row.getToAmount();
			}

			@Override
			protected void setAmount(ClientComputationSlab row, Double value) {
				row.setToAmount(value);
				update(row);
				List<ClientComputationSlab> rows = getAllRows();
				int index = rows.indexOf(row);
				if (index < rows.size() - 1) {
					ClientComputationSlab slab = rows.get(index + 1);
					slab.setFromAmount(value);
					update(slab);
				}
			}

			@Override
			protected String getColumnName() {
				return messages.amountUpto();
			}

			@Override
			public int getWidth() {
				return 160;
			}

			@Override
			public String getValueAsString(ClientComputationSlab row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new StringListColumn<ClientComputationSlab>() {

			@Override
			protected String getValue(ClientComputationSlab row) {
				if (row.getSlabType() == 0) {
					String string = slabTypes.get(0);
					setValue(row, string);
					return string;
				}
				return slabTypes.get(row.getSlabType() - 1);
			}

			@Override
			protected List<String> getData() {
				return slabTypes;
			}

			@Override
			protected void setValue(ClientComputationSlab row, String newValue) {
				row.setSlabType(slabTypes.indexOf(newValue) + 1);
			}

			@Override
			protected String getColumnName() {
				return messages.slabType();
			}

			@Override
			public String getValueAsString(ClientComputationSlab row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new AmountColumn<ClientComputationSlab>(null, false) {

			@Override
			protected Double getAmount(ClientComputationSlab row) {
				return row.getValue();
			}

			@Override
			protected void setAmount(ClientComputationSlab row, Double value) {
				row.setValue(value);
			}

			@Override
			protected String getColumnName() {
				return messages.valueBasis();
			}

			@Override
			public int getWidth() {
				return 160;
			}

			@Override
			public String getValueAsString(ClientComputationSlab row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new DeleteColumn<ClientComputationSlab>());
	}

	@Override
	public void delete(ClientComputationSlab row) {
		List<ClientComputationSlab> rows = getAllRows();
		int index = rows.indexOf(row);
		if (index < rows.size() - 1) {
			ClientComputationSlab slab = rows.get(index + 1);
			slab.setFromAmount(row.getFromAmount());
			update(slab);
		}
		super.delete(row);
	}

	@Override
	protected int getDefaultEmptyRowsSize() {
		return 1;
	}

	@Override
	protected ClientComputationSlab getEmptyRow() {
		return new ClientComputationSlab();
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<ClientComputationSlab> getRows() {
		List<ClientComputationSlab> rows = new ArrayList<ClientComputationSlab>();
		for (ClientComputationSlab row : getAllRows()) {
			if (!row.isEmpty()) {
				rows.add(row);
			}
		}
		return rows;
	}

	public void addRow(ClientComputationSlab row) {
		List<ClientComputationSlab> rows = getAllRows();
		if (rows.size() > 0) {
			ClientComputationSlab previous = rows.get(rows.size() - 1);
			row.setFromAmount(previous.getToAmount() + 1);
		}
		add(row);
	}

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		List<ClientComputationSlab> rows = getAllRows();
		for (ClientComputationSlab slab : rows) {
			// if (slab.getToAmount() <= slab.getFromAmount()) {
			// result.addError(slab,
			// "To amount should not be lessthan or equal to from amount");
			// return result;
			// }
		}
		return null;
	}
}
