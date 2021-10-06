package com.nitya.accounter.mobile.commands;

import java.util.List;

import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.web.client.core.ClientPayStructureList;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;

public class PayStructureListCommand extends AbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<ClientPayStructureList>(getMessages()
				.payStructureList(), getMessages().pleaseSelect(
				getMessages().payStructure()), 20) {

			@Override
			protected String onSelection(ClientPayStructureList value) {
				return "updatePayStructure #" + value.getId();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().payStructureList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(ClientPayStructureList value) {
				return PayStructureListCommand.this.createRecord(value);
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				PayStructureListCommand.this.setCreateCommand(list);
			}

			@Override
			protected boolean filter(ClientPayStructureList e, String name) {
				String val = e.getEmployee() != null ? e.getEmployee()
						.getName() : e.getEmployeeGroup().getName();
				return val.startsWith(name)
						|| String.valueOf(e.getID()).startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<ClientPayStructureList> getLists(Context context) {
				return getPayStructuresList(context);
			}

		});

	}

	protected Record createRecord(ClientPayStructureList value) {
		Record record = new Record(value);
		record.add(getMessages().employeeOrGroup(),
				value.getEmployee() != null ? value.getEmployee().getName()
						: value.getEmployeeGroup().getName());
		return record;
	}

	protected PaginationList<ClientPayStructureList> getPayStructuresList(
			Context context) {
		PaginationList<ClientPayStructureList> payrollStructuresList = null;
		try {
			payrollStructuresList = new FinanceTool().getPayrollManager()
					.getPayrollStructuresList(0, -1,
							context.getCompany().getID());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return payrollStructuresList;
	}

	protected void setCreateCommand(CommandList list) {
		list.add("newPayStructure");
	}
}
