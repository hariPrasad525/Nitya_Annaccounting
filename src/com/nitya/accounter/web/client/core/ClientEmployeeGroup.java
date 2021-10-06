package com.nitya.accounter.web.client.core;

import java.util.List;

public class ClientEmployeeGroup implements ClientPayStructureDestination {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private List<ClientEmployee> employees;

	private long id;
	
	ClientEmployeeCompsensation compsensation;

	public ClientEmployeeCompsensation getCompsensation() {
		return compsensation;
	}

	public void setCompsensation(ClientEmployeeCompsensation compsensation) {
		this.compsensation = compsensation;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the employees
	 */
	public List<ClientEmployee> getEmployees() {
		return employees;
	}

	/**
	 * @param employees
	 *            the employees to set
	 */
	public void setEmployees(List<ClientEmployee> employees) {
		this.employees = employees;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMPLOYEE_GROUP;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientEmployeeGroup) {
			ClientEmployeeGroup group = (ClientEmployeeGroup) obj;
			return this.getID() == group.getID() ? true : false;
		}
		return false;
	}
}
