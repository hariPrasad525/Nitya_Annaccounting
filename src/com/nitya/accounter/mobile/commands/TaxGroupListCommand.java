package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.TAXGroup;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;

public class TaxGroupListCommand extends AbstractCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<TAXGroup>("taxGroupsList",
				getMessages().pleaseSelect(getMessages().taxGroup()), 20) {
			@Override
			protected Record createRecord(TAXGroup value) {
				Record record = new Record(value);
				record.add(getMessages().product(), value.getName());
				return record;
			}

			@Override
			protected String onSelection(TAXGroup value) {
				return "updateTaxGroup " + value.getID();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newTaxGroup");
			}

			@Override
			protected boolean filter(TAXGroup e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<TAXGroup> getLists(Context context) {
				return new ArrayList<TAXGroup>(vatItemssList(context));
			}

			@Override
			protected String getShowMessage() {
				return getMessages().manageSalesTaxGroups();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

		});
	}

	private Set<TAXGroup> vatItemssList(Context context) {
		return context.getCompany().getTaxGroups();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isTrackTax()) {
			addFirstMessage(context, getMessages()
					.youDntHavePermissionToDoThis());
			return getMessages().cancel();
		}

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
