package com.nitya.accounter.web.client.ui.payroll;

import com.nitya.accounter.web.client.core.ClientAttendancePayHead;
import com.nitya.accounter.web.client.core.ClientComputionPayHead;
import com.nitya.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientFlatRatePayHead;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.edittable.AmountColumn;
import com.nitya.accounter.web.client.ui.edittable.DateColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;

public class EmployeePayHeadComponentTable extends
		EditTable<ClientEmployeePayHeadComponent> {

	private AmountColumn<ClientEmployeePayHeadComponent> rateColumn;

	public EmployeePayHeadComponentTable() {
		super();
	}

	@Override
	protected ClientEmployeePayHeadComponent getEmptyRow() {
		return new ClientEmployeePayHeadComponent();
	}

	@Override
	protected void initColumns() {
		this.addColumn(new DateColumn<ClientEmployeePayHeadComponent>() {

			@Override
			protected ClientFinanceDate getValue(
					ClientEmployeePayHeadComponent row) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void setValue(ClientEmployeePayHeadComponent row,
					ClientFinanceDate value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.effectiveFrom();
			}

			@Override
			public String getValueAsString(ClientEmployeePayHeadComponent row) {
				return getValue(row).toString();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

		});

		this.addColumn(new TextEditColumn<ClientEmployeePayHeadComponent>() {

			@Override
			protected String getValue(ClientEmployeePayHeadComponent row) {
				ClientPayHead payHead = row.getClientPayHead();
				if (payHead != null) {
					return payHead.getName();
				}
				return "";
			}

			@Override
			protected void setValue(ClientEmployeePayHeadComponent row,
					String value) {
				// TODO Auto-generated method stub
			}

			@Override
			protected String getColumnName() {
				return messages.payhead();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public String getValueAsString(ClientEmployeePayHeadComponent row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		rateColumn = new AmountColumn<ClientEmployeePayHeadComponent>(null,
				false) {

			@Override
			protected Double getAmount(ClientEmployeePayHeadComponent row) {
				return row.getRate();
			}

			@Override
			protected void setAmount(ClientEmployeePayHeadComponent row,
					Double value) {
				row.setRate(value);
			}

			@Override
			protected String getColumnName() {
				return messages.rate();
			}

			@Override
			public String getValueAsString(ClientEmployeePayHeadComponent row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(rateColumn);

		this.addColumn(new TextEditColumn<ClientEmployeePayHeadComponent>() {

			@Override
			protected String getValue(ClientEmployeePayHeadComponent row) {
				ClientPayHead payHead = row.getClientPayHead();
				if (payHead == null) {
					return "";
				}
				int type = 0;
				if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE) {
					ClientAttendancePayHead payhead = ((ClientAttendancePayHead) payHead);
					type = payhead.getCalculationPeriod();
				} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
					ClientComputionPayHead payhead = ((ClientComputionPayHead) payHead);
					type = payhead.getCalculationPeriod();
				} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_FLAT_RATE) {
					ClientFlatRatePayHead payhead = ((ClientFlatRatePayHead) payHead);
					type = payhead.getCalculationPeriod();
				} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_TAX) {
					type = ClientPayHead.CALCULATION_PERIOD_DAYS;
				}
				return ClientPayHead.getCalculationPeriod(type);
			}

			@Override
			protected void setValue(ClientEmployeePayHeadComponent row,
					String value) {
				// TODO Auto-generated method stub
			}

			@Override
			protected String getColumnName() {
				return messages.calculationPeriod();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 120;
			}

			@Override
			public String getValueAsString(ClientEmployeePayHeadComponent row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new TextEditColumn<ClientEmployeePayHeadComponent>() {

			@Override
			protected String getValue(ClientEmployeePayHeadComponent row) {
				ClientPayHead payHead = row.getClientPayHead();
				if (payHead != null) {
					return ClientPayHead.getPayHeadType(payHead.getType());
				}
				return "";
			}

			@Override
			protected void setValue(ClientEmployeePayHeadComponent row,
					String value) {
				// TODO Auto-generated method stub
			}

			@Override
			protected String getColumnName() {
				return messages.payHeadType();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 180;
			}

			@Override
			public String getValueAsString(ClientEmployeePayHeadComponent row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new TextEditColumn<ClientEmployeePayHeadComponent>() {

			@Override
			protected String getValue(ClientEmployeePayHeadComponent row) {
				ClientPayHead payHead = row.getClientPayHead();
				if (payHead != null) {
					return ClientPayHead.getCalculationType(payHead
							.getCalculationType());
				}
				return "";
			}

			@Override
			protected void setValue(ClientEmployeePayHeadComponent row,
					String value) {
				// TODO Auto-generated method stub
			}

			@Override
			protected String getColumnName() {
				return messages.calculationType();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 130;
			}

			@Override
			public String getValueAsString(ClientEmployeePayHeadComponent row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new TextEditColumn<ClientEmployeePayHeadComponent>() {

			@Override
			protected String getValue(ClientEmployeePayHeadComponent row) {
				ClientPayHead payHead = row.getClientPayHead();
				if (payHead != null
						&& payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
					ClientComputionPayHead payhead = (ClientComputionPayHead) payHead;
					if (payhead.getComputationType() != ClientComputionPayHead.COMPUTATE_ON_SPECIFIED_FORMULA) {
						return ClientComputionPayHead
								.getComputationType(payhead
										.getComputationType());
					} else {
						return UIUtils.prepareFormula(payhead
								.getFormulaFunctions());
					}
				}
				return "";
			}

			@Override
			protected void setValue(ClientEmployeePayHeadComponent row,
					String value) {
				// TODO Auto-generated method stub
			}

			@Override
			protected String getColumnName() {
				return messages.computedOn();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			public String getValueAsString(ClientEmployeePayHeadComponent row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

	}

	@Override
	protected boolean isInViewMode() {
		return false;
	}

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		for (ClientEmployeePayHeadComponent row : getAllRows()) {
			if (row.getRate() == 0) {
				result.addError(row, "Rate should not be zero");
				return result;
			}
		}
		return result;
	}

}
