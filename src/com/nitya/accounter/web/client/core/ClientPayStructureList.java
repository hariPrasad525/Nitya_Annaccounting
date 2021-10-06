package com.nitya.accounter.web.client.core;

import java.util.List;

import com.nitya.accounter.web.client.ui.Accounter;

public class ClientPayStructureList implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientEmployee employee;

	private ClientEmployeeGroup employeeGroup;

	private List<ClientPayStructureItem> items;

	private long id;

	public ClientEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(ClientEmployee employee) {
		this.employee = employee;
	}

	public ClientEmployeeGroup getEmployeeGroup() {
		return employeeGroup;
	}

	public void setEmployeeGroup(ClientEmployeeGroup employeeGroup) {
		this.employeeGroup = employeeGroup;
	}

	public List<ClientPayStructureItem> getItems() {
		return items;
	}

	public void setItems(List<ClientPayStructureItem> items) {
		this.items = items;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	public String getName() {
		String name = null;
		if (employee != null) {
			name = employee.getName();
		} else {
			name = employeeGroup.getName();
		}
		name += " " + Accounter.getMessages().payStructure();
		return name;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
