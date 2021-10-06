package com.nitya.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.Employee;
import com.nitya.accounter.core.EmployeeGroup;
import com.nitya.accounter.core.PayStructureDestination;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.utils.HibernateUtil;

public class EmployeeAndEmployeeGroupRequirement extends
		ListRequirement<PayStructureDestination> {

	public EmployeeAndEmployeeGroupRequirement(String requirementName,
			String enterString, String recordName,
			ChangeListner<PayStructureDestination> listener) {
		super(requirementName, enterString, recordName, false, true, listener);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().employeeOrGroup());
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getMessages().employeeOrGroup());
	}

	@Override
	protected Record createRecord(PayStructureDestination value) {
		Record record = new Record(value);
		record.add(getMessages().name(), getDisplayValue(value));
		return record;
	}

	@Override
	protected String getDisplayValue(PayStructureDestination value) {
		if (value instanceof EmployeeGroup) {
			EmployeeGroup employeeGroup = (EmployeeGroup) value;
			return employeeGroup.getName();
		} else {
			Employee employee = (Employee) value;
			return employee.getName();
		}
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("newEmployee");
		list.add("newEmployeeGroup");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().employeeOrGroup());
	}

	@Override
	protected boolean filter(PayStructureDestination e, String name) {
		return getDisplayValue(e).equals(name);
	}

	@Override
	protected List<PayStructureDestination> getLists(Context context) {
		List<PayStructureDestination> list = new ArrayList<PayStructureDestination>();
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("list.All.Employees")
				.setParameter("isActive", true)
				.setEntity("company", getCompany());
		List<Employee> employees = query.list();
		if (employees != null) {
			list.addAll(employees);
		}
		query = session.getNamedQuery("list.EmployeeGroups").setEntity(
				"company", getCompany());
		List<EmployeeGroup> groups = query.list();
		if (groups != null) {
			list.addAll(groups);
		}
		return list;
	}
}
