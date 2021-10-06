package com.nitya.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientEmployeeAttendance;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;
import com.nitya.accounter.web.client.ui.edittable.DateColumn;
import com.nitya.accounter.web.client.ui.edittable.DeleteColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;

public class EmployeeAttendanceTable extends EditTable<ClientEmployeeAttendance> {
	
	AccounterMessages2 messages2 = Global.get().messages2();
	

	public EmployeeAttendanceTable() {
		super();
		addEmptyRecords();
	}

	@Override
	protected ClientEmployeeAttendance getEmptyRow() {
		return new ClientEmployeeAttendance();
	}
	
	
	@Override
	protected void initColumns() {
		// TODO Auto-generated method stub
		
		
		this.addColumn(new DateColumn<ClientEmployeeAttendance>() {
			
			@Override
			protected String getColumnName() {
				return messages.date();
			}

			@Override
			protected ClientFinanceDate getValue(ClientEmployeeAttendance row) {
				// TODO Auto-generated method stub
				return row.getPayrollDate();
			}

			@Override
			protected void setValue(ClientEmployeeAttendance row, ClientFinanceDate value) {
				// TODO Auto-generated method stub
				row.setPayrollDate(value);
			}

			@Override
			public String getValueAsString(ClientEmployeeAttendance row) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int insertNewLineNumber() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		this.addColumn(new TextEditColumn<ClientEmployeeAttendance>() {
			
			@Override
			protected String getColumnName() {
				return messages2.milesPerHour();
			}

			@Override
			protected String getValue(ClientEmployeeAttendance row) {
				// TODO Auto-generated method stub
				return row.getMileshours();
			}

			@Override
			protected void setValue(ClientEmployeeAttendance row, String value) {
				// TODO Auto-generated method stub
				row.setMileshours(value);
			}

			@Override
			public String getValueAsString(ClientEmployeeAttendance row) {
				// TODO Auto-generated method stub
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		
		this.addColumn(new TextEditColumn<ClientEmployeeAttendance>() {
			
			@Override
			protected String getColumnName() {
				return messages2.advancePaymnts();
			}

			@Override
			protected String getValue(ClientEmployeeAttendance row) {
				// TODO Auto-generated method stub
				return row.getAdvances();
			}

			@Override
			protected void setValue(ClientEmployeeAttendance row, String value) {
				// TODO Auto-generated method stub
				row.setAdvances(value);
				
			}

			@Override
			public String getValueAsString(ClientEmployeeAttendance row) {
				// TODO Auto-generated method stub
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				// TODO Auto-generated method stub
				return 0;
			}
		});	
		this.addColumn(new TextEditColumn<ClientEmployeeAttendance>() {
			
			@Override
			protected String getColumnName() {
				return messages2.foodAllowances();
			}

			@Override
			protected String getValue(ClientEmployeeAttendance row) {
				// TODO Auto-generated method stub
				return row.getFoodAllowances();
			}

			@Override
			protected void setValue(ClientEmployeeAttendance row, String value) {
				// TODO Auto-generated method stub
				row.setFoodAllowances(value);
			}

			@Override
			public String getValueAsString(ClientEmployeeAttendance row) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int insertNewLineNumber() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		
		this.addColumn(new TextEditColumn<ClientEmployeeAttendance>() {
			
			@Override
			protected String getColumnName() {
				return messages2.otherAllowances();
			}

			@Override
			protected String getValue(ClientEmployeeAttendance row) {
				// TODO Auto-generated method stub
				return row.getOtherAllowances();
			}

			@Override
			protected void setValue(ClientEmployeeAttendance row, String value) {
				// TODO Auto-generated method stub
				row.setOtherAllowances(value);
			}

			@Override
			public String getValueAsString(ClientEmployeeAttendance row) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int insertNewLineNumber() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
				
		this.addColumn(new DeleteColumn<ClientEmployeeAttendance>());
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void setAllRows(List<ClientEmployeeAttendance> rows) {
		createColumns();
		for (ClientEmployeeAttendance item : rows) {
			item.setID(0);
		}
		super.setAllRows(rows);

	}

}
