package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.EmployeeGroup;
import com.vimukti.accounter.core.PayStructureDestination;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.EmployeeAndEmployeeGroupRequirement;
import com.vimukti.accounter.mobile.requirements.PayStructureTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;

public class CreatePayStructureCommand extends AbstractCommand {

	ClientPayStructure payStructure;

	private static final String EMPLOYEE = "employee";

	private static final String STRUCTURE_TABLE = "structureTable";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages().selectATransactionToUpdate(getMessages().invoice()));
				return "payStructureList";
			}
			long numberFromString = getNumberFromString(string);
			payStructure = (ClientPayStructure) CommandUtils.getClientObjectById(numberFromString,
					AccounterCoreType.PAY_STRUCTURE, getCompanyId());
			if (payStructure == null) {
				return "payStructureList" + string;
			}
			setValues();
		} else {
			payStructure = new ClientPayStructure();
		}
		return null;
	}

	private void setValues() {
		long employee = payStructure.getEmployee();
		if (employee == 0) {
			employee = payStructure.getEmployeeGroup();
		}
		get(EMPLOYEE).setValue(payStructure.getEmployee() != 0 ? getServerObject(Employee.class, employee)
				: getServerObject(EmployeeGroup.class, employee));
		get(STRUCTURE_TABLE).setValue(payStructure.getItems());
	}

	@Override
	protected String getWelcomeMessage() {
		return payStructure.getID() == 0 ? getMessages().creating(getMessages().payStructure())
				: getMessages().updating(getMessages().payStructure());
	}

	@Override
	protected String getDetailsMessage() {
		return payStructure.getID() == 0 ? getMessages().readyToCreate(getMessages().payStructure())
				: getMessages().readyToUpdate(getMessages().payStructure());
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return payStructure.getID() == 0 ? getMessages().createSuccessfully(getMessages().payStructure())
				: getMessages().updateSuccessfully(getMessages().payStructure());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new EmployeeAndEmployeeGroupRequirement(EMPLOYEE,
				getMessages().pleaseSelect(getMessages().employeeOrGroup()), getMessages().employeeOrGroup(), null));

		list.add(new PayStructureTableRequirement(STRUCTURE_TABLE, getMessages().pleaseSelect(getMessages().item()),
				getMessages().payStructure() + " " + getMessages().item(), true, false, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		PayStructureDestination destination = get(EMPLOYEE).getValue();
		payStructure.setEmployee(destination instanceof Employee ? destination.getID() : 0);
		payStructure.setEmployeeGroup(destination instanceof EmployeeGroup ? destination.getID() : 0);
		List<ClientPayStructureItem> items = get(STRUCTURE_TABLE).getValue();
		payStructure.setItems(items);
		create(payStructure, context);
		return null;
	}
}
