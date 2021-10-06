package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Array;

import com.vimukti.accounter.web.client.ui.Accounter;

public class ClientPayStructure implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long employee;

	private long employeeGroup;

	private List<ClientPayStructureItem> items = new ArrayList<ClientPayStructureItem>();

	private long id;

	/**
	 * @return the items
	 */
	public List<ClientPayStructureItem> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<ClientPayStructureItem> items) {
		this.items = items;
	}

	/**
	 * @return the employeeGroup
	 */
	public long getEmployeeGroup() {
		return employeeGroup;
	}

	/**
	 * @param employeeGroup
	 *            the employeeGroup to set
	 */
	public void setEmployeeGroup(long employeeGroup) {
		this.employeeGroup = employeeGroup;
	}

	/**
	 * @return the employee
	 */
	public long getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 *            the employee to set
	 */
	public void setEmployee(long employee) {
		this.employee = employee;
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
		return Accounter.getMessages().payStructure();
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAY_STRUCTURE;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

}
