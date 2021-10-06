package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.CommandsRequirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;

public class VATCodesListCommand extends AbstractCommand {

	private static final String CURRENT_VIEW = "currentView";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(CURRENT_VIEW) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<TAXCode>("vatCodesList", getMessages()
				.pleaseSelect(getMessages().taxCode()), 20) {
			@Override
			protected Record createRecord(TAXCode value) {
				Record record = new Record(value);
				record.add(getMessages().name(), value.getName());
				record.add(getMessages().description(), value.getDescription());
				return record;
			}

			@Override
			protected String onSelection(TAXCode value) {
				return "updateTAXCode " + value.getID();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createVATCode");
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				Set<TAXCode> completeList = getVatCodes(context);
				List<TAXCode> result = new ArrayList<TAXCode>();

				String type = VATCodesListCommand.this.get(CURRENT_VIEW)
						.getValue();

				for (TAXCode taxCode : completeList) {
					if (type.equals("Active")) {
						if (taxCode.isActive())
							result.add(taxCode);
					}
					if (type.equals("In-Active")) {
						if (!taxCode.isActive())
							result.add(taxCode);
					}
				}
				return result;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().vatCodeList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

		});
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isTrackTax()) {
			addFirstMessage(context, getMessages()
					.youDntHavePermissionToDoThis());
			return "cancel";
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
		get(CURRENT_VIEW).setDefaultValue(getMessages().active());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	private Set<TAXCode> getVatCodes(Context context) {
		return context.getCompany().getTaxCodes();

	}

}
