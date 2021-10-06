package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class CustomerGroupListCommand extends AbstractCommand {

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

		list.add(new ShowListRequirement<CustomerGroup>(getMessages()
				.customer() + " " + getMessages().group(), "", 20) {
			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// CustomerGroup value) {
			// commandList.add(new UserCommand("Update Customer Group", value
			// .getName()));
			// commandList.add(new UserCommand("Delete CustomerGroup ", value
			// .getName()));
			// }

			@Override
			protected String onSelection(CustomerGroup value) {
				return "updatecustomergroup " + value.getName();
			}

			@Override
			protected String getShowMessage() {

				return getMessages().payeeList(
						getMessages().customer() + " " + getMessages().group());
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(
						getMessages().customer() + " " + getMessages().group());

			}

			@Override
			protected Record createRecord(CustomerGroup value) {
				Record customerGroupRec = new Record(value);
				customerGroupRec.add(getMessages().name(), value.getName());
				return customerGroupRec;

			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createcustomergroup");

			}

			@Override
			protected boolean filter(CustomerGroup e, String name) {
				return false;

			}

			@Override
			protected List<CustomerGroup> getLists(Context context) {
				return getCustomerGroups(context);
			}

		});

	}

	private List<CustomerGroup> getCustomerGroups(Context context) {
		Set<CustomerGroup> customerGroups = context.getCompany()
				.getCustomerGroups();
		List<CustomerGroup> result = new ArrayList<CustomerGroup>(
				customerGroups);
		return result;
	}
}
