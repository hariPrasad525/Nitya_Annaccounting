package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.web.client.core.ClientEmployeeGroup;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;

public class EmployeeGroupListCommand extends AbstractCommand {

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
		// TODO Auto-generated method stub

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

		list.add(new ShowListRequirement<ClientEmployeeGroup>(
				"employeeGroupList", getMessages().pleaseSelect(
						getMessages().creditRating()), 20) {
			@Override
			protected Record createRecord(ClientEmployeeGroup value) {
				Record record = new Record(value);
				record.add(value.getName());
				return record;
			}

			@Override
			protected String onSelection(ClientEmployeeGroup value) {
				return "editEmployeeGroup " + value.getName();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("editEmployeeGroup");
			}

			@Override
			protected boolean filter(ClientEmployeeGroup e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<ClientEmployeeGroup> getLists(Context context) {
				return getEmployeeGroupList(context);
			}

			@Override
			protected String getShowMessage() {
				return getMessages().employeeGroupList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

		});
	}

	protected ArrayList<ClientEmployeeGroup> getEmployeeGroupList(
			Context context) {
		FinanceTool tool = new FinanceTool();
		ArrayList<ClientEmployeeGroup> employeeGroups = null;
		try {
			employeeGroups = tool.getPayrollManager().getEmployeeGroups(
					context.getCompany().getID());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return employeeGroups;
	}
}
