package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PaySheet extends BaseReport implements IsSerializable,
		Serializable {

	private String employee;
	private long employeeId;
	private Map<Long, Double> map = new HashMap<Long, Double>();
	private long payheadId;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getEmployee() {
		return this.employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public Map<Long, Double> getMap() {
		return map;
	}

	public void setMap(Map<Long, Double> map) {
		this.map = map;
	}

	public long getPayheadId() {
		return this.payheadId;
	}

	public void setPayheadId(long payheadId) {
		this.payheadId = payheadId;
	}

}
