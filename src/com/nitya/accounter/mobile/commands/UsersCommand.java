package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.web.client.core.ClientUserInfo;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;

public class UsersCommand extends AbstractCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<ClientUserInfo>("UsersList",
				getMessages().pleaseSelect(getMessages().users()), 20) {

			@Override
			protected String onSelection(ClientUserInfo value) {
				return "updateUser " + value.getEmail();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().users();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(ClientUserInfo value) {
				Record record = new Record(value);
				record.add(getMessages().email(), value.getEmail());
				record.add(getMessages().userRole(), value.getUserRole());
				record.add(getMessages().firstName(), value.getFirstName());
				record.add(getMessages().lastName(), value.getLastName());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newUser");
			}

			@Override
			protected boolean filter(ClientUserInfo e, String name) {
				return e.getFirstName().equalsIgnoreCase(name)
						|| e.getLastName().equalsIgnoreCase(name);
			}

			@Override
			protected List<ClientUserInfo> getLists(Context context) {
				return getUsersList(context);
			}
		});
	}

	private List<ClientUserInfo> getUsersList(Context context) {
		try {
			return new FinanceTool().getUserManager().getAllUsers(
					getCompanyId());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return new ArrayList<ClientUserInfo>();
	}

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

}
