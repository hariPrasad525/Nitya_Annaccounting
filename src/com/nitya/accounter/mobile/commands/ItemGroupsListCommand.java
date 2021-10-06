package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.ItemGroup;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;

public class ItemGroupsListCommand extends AbstractCommand {

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

		list.add(new ShowListRequirement<ItemGroup>(getMessages().itemGroup(),
				"", 20) {
			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// ItemGroup value) {
			// commandList.add(new UserCommand("Update Item Group", value
			// .getName()));
			// commandList.add(new UserCommand("Delete Item Group", value
			// .getName()));
			// }

			@Override
			protected String onSelection(ItemGroup value) {
				return "updateItemGroup " + value.getName();
			}

			@Override
			protected String getShowMessage() {

				return getMessages().payeeList(getMessages().itemGroup());
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(getMessages().itemGroup());

			}

			@Override
			protected Record createRecord(ItemGroup value) {
				Record itemGroupRec = new Record(value);
				itemGroupRec.add(getMessages().name(), value.getName());
				return itemGroupRec;

			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createItemGroup");

			}

			@Override
			protected boolean filter(ItemGroup e, String name) {
				return false;

			}

			@Override
			protected List<ItemGroup> getLists(Context context) {
				return getItemGroups(context);
			}

		});

	}

	private List<ItemGroup> getItemGroups(Context context) {
		Set<ItemGroup> itemGroups = context.getCompany().getItemGroups();
		List<ItemGroup> result = new ArrayList<ItemGroup>(itemGroups);
		return result;
	}
}
