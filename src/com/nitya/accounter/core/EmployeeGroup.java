package com.nitya.accounter.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

/**
 * An Employee Group allows you to group/classify the employees in a logical
 * manner as required.
 * 
 * The Salary structure can be defined at the Employee Group level. For example,
 * you can create the Salary structure based on the department or function such
 * as Production, Sales, Administration and so on, or by designation such as
 * Managers, Supervisors, Workers and so on.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class EmployeeGroup extends CreatableObject implements
		PayStructureDestination, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private Set<Employee> employees = new HashSet<Employee>();
	
	EmployeeCompsensation compsensation;

	public EmployeeCompsensation getCompsensation() {
		return compsensation;
	}

	public void setCompsensation(EmployeeCompsensation compsensation) {
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
	public Set<Employee> getEmployees() {
		return employees;
	}

	/**
	 * @param employees
	 *            the employees to set
	 */
	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (!goingToBeEdit) {
			Session session = HibernateUtil.getCurrentSession();

			EmployeeGroup employeeGroup = (EmployeeGroup) clientObject;
			Query query = session
					.getNamedQuery("getEmployeeGroup.by.Name")
					.setParameter("name", employeeGroup.name,
							EncryptedStringType.INSTANCE)
					.setParameter("id", employeeGroup.getID())
					.setEntity("company", employeeGroup.getCompany());
			List list = query.list();
			if (list != null && list.size() > 0) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
			}
		}
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		w.put(messages.name(), this.name);
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
	}

	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(getID());
		accounterCore.setObjectType(AccounterCoreType.EMPLOYEE_GROUP);
		ChangeTracker.put(accounterCore);
		return super.onDelete(arg0);
	}

	@Override
	public int getObjType() {
		// TODO Auto-generated method stub
		return IAccounterCore.EMPLOYEE_GROUP;
	}
}
