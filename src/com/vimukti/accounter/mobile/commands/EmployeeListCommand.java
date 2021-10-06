package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class EmployeeListCommand extends AbstractCommand {

	private static final String EMPLOYEE_TYPE = "employeeType";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(EMPLOYEE_TYPE).setDefaultValue(getMessages().active());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(EMPLOYEE_TYPE) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<ClientEmployee>("employeeList",
				getMessages().employee(), 20) {

			@Override
			protected String onSelection(ClientEmployee value) {
				return "updateEmployee #" + value.getID();
			}

			@Override
			protected String getShowMessage() {
				return "Employee List";
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(ClientEmployee value) {
				Record record = new Record(value);
				record.add(value.getName());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newEmployee");
			}

			@Override
			protected boolean filter(ClientEmployee e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<ClientEmployee> getLists(Context context) {
				return getEmployeeList(context);
			}

		});
	}

	protected List<ClientEmployee> getEmployeeList(Context context) {
		FinanceTool financeTool = new FinanceTool();
		String isActive = get(EMPLOYEE_TYPE).getValue();
		PaginationList<ClientEmployee> employees = null;
		try {
			employees = financeTool.getPayrollManager().getEmployees(
					(isActive.equals(getMessages().active()) ? true : false),
					0, -1, context.getCompany().getID());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return employees;
	}
}
