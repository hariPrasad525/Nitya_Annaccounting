package com.nitya.accounter.web.client.ui.payroll;

import java.util.HashMap;
import java.util.Map;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientEmployeeCompsensation;
import com.nitya.accounter.web.client.core.ClientListBox;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;
import com.nitya.accounter.web.client.ui.edittable.AmountColumn;
import com.nitya.accounter.web.client.ui.edittable.DeleteColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;

public class EmployeCompensationTable extends EditTable<ClientEmployeeCompsensation> {
	
	AccounterMessages2 messages2 = Global.get().messages2();
	

	public EmployeCompensationTable() {
		super();
		addEmptyRecords();
	}

	@Override
	protected ClientEmployeeCompsensation getEmptyRow() {
		return new ClientEmployeeCompsensation();
	}
	
	private Map<Integer, String> getCompType(){
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(ClientEmployeeCompsensation.COMP_TYPE_DEDUCTIONS, messages2.compTypeDeduct());
		map.put(ClientEmployeeCompsensation.COMP_TYPE_EARNINGS, messages2.compTypeEarnings());
		return map;
	}
	
	private Map<Integer, String> getPayType(){
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(ClientEmployeeCompsensation.PAY_TYPE_DAILY, messages2.payTypeDaily());
		map.put(ClientEmployeeCompsensation.PAY_TYPE_ANNUAL, messages.annual());
		map.put(ClientEmployeeCompsensation.PAY_TYPE_HOURLY, messages2.payTypeHourly());
		map.put(ClientEmployeeCompsensation.PAY_TYPE_MILES, messages2.payTypeMiles());
		map.put(ClientEmployeeCompsensation.PAY_TYPE_PER_WORK, messages2.payTypePerWork());
		map.put(ClientEmployeeCompsensation.PAY_TYPE_PER_PERIOD, messages2.payTypePerPeriod());
		return map;
	}
	
	private Map<Integer, String> getPayFrequency(){
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(ClientEmployeeCompsensation.PAY_FREQUENCY_WEEKLY, messages2.payFrequencyWeekly());
		map.put(ClientEmployeeCompsensation.PAY_FREQUENCY_BI_WEEKLY, messages2.payFrequencyBiWeekly());
		map.put(ClientEmployeeCompsensation.PAY_FREQUENCY_SEMI_MONTHLY, messages2.payFrequencySemiMonthly());
		map.put(ClientEmployeeCompsensation.PAY_FREQUENCY_MONTHLY, messages2.payFrequencyMonthly());
		return map;
	}

	@Override
	protected void initColumns() {
		
		
		this.addColumn(new CustomComboColumn<ClientEmployeeCompsensation>(getCompType()) {
			@Override
			protected String getColumnName() {
				return messages2.compType();
			}
			
			@Override
			protected void setValue(ClientEmployeeCompsensation row, ClientListBox newValue) {
				super.setValue(row, newValue);
				row.setCompType((int) newValue.getID());
			}
		});
		
		
		this.addColumn(new CustomComboColumn<ClientEmployeeCompsensation>(getPayType()) {
			@Override
			protected String getColumnName() {
				return messages2.payType();
			}
			
			@Override
			protected void setValue(ClientEmployeeCompsensation row, ClientListBox newValue) {
				super.setValue(row, newValue);
				row.setPayType((int) newValue.getID());
			}
		});

		this.addColumn(new CustomComboColumn<ClientEmployeeCompsensation>(getPayFrequency()) {
			@Override
			protected String getColumnName() {
				return messages2.payFrequency();
			}
			
			@Override
			protected void setValue(ClientEmployeeCompsensation row, ClientListBox newValue) {
				super.setValue(row, newValue);
				row.setPayFrequency((int) newValue.getID());
			}
		});

		this.addColumn(new AmountColumn<ClientEmployeeCompsensation>(null, false) {

			@Override
			protected Double getAmount(ClientEmployeeCompsensation row) {
				return row.getSalary();
			}

			@Override
			protected void setAmount(ClientEmployeeCompsensation row, Double value) {
				row.setSalary(value);
			}

			@Override
			protected String getColumnName() {
				return messages2.salary();
			}

			@Override
			public int getWidth() {
				return 50;
			}

			@Override
			public String getValueAsString(ClientEmployeeCompsensation row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		
		this.addColumn(new AmountColumn<ClientEmployeeCompsensation>(null, false) {

			@Override
			protected Double getAmount(ClientEmployeeCompsensation row) {
				return row.getAdditionalAmount();
			}

			@Override
			protected void setAmount(ClientEmployeeCompsensation row, Double value) {
				row.setAdditionalAmount(value);
			}

			@Override
			protected String getColumnName() {
				return messages2.additionalAmount();
			}

			@Override
			public int getWidth() {
				return 50;
			}

			@Override
			public String getValueAsString(ClientEmployeeCompsensation row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		
		this.addColumn(new DeleteColumn<ClientEmployeeCompsensation>());

	}

	@Override
	protected boolean isInViewMode() {
		return false;
	}

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		for (ClientEmployeeCompsensation row : getAllRows()) {
			if (row.getPayType() == 0) {
				result.addError(row, "Pay type should not be selected");
				return result;
			}
		}
		return result;
	}

}
