package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientEmployeeAttendance;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;

public abstract class AbstractEmployeeAttendanceTable extends EditTable<ClientEmployeeAttendance> {
	
	protected String payrollDate;
	protected String employeeId;
	protected String milesPerHour;
	protected String advances;
	protected String foodAllowence;
	protected String otherAllowances;
	protected boolean isCustomerAllowedToAdd;
	
	public AbstractEmployeeAttendanceTable(int rowsPerObject, boolean isCustomerAllowedToAdd, String payrollDate, String employeeId, String milesPerHour,
			String advances, String foodAllowence, String otherAllowances) {
		super(rowsPerObject);
		
		this.payrollDate = payrollDate;
		this.employeeId = employeeId;
		this.milesPerHour = milesPerHour;
		this.isCustomerAllowedToAdd = isCustomerAllowedToAdd;
		this.advances = advances;
		this.foodAllowence = foodAllowence;
		this.otherAllowances = otherAllowances;
	}

	public AbstractEmployeeAttendanceTable(int rowsPerObject, String payrollDate, String employeeId, String milesPerHour,
			String advances, String foodAllowence, String otherAllowances) {
		// TODO Auto-generated constructor stub
		this(rowsPerObject, false, payrollDate, employeeId, milesPerHour, advances, foodAllowence, otherAllowances);
	}


}
