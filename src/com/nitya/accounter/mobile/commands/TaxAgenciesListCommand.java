package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.CommandsRequirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;

public class TaxAgenciesListCommand extends AbstractCommand {

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
		get(VIEW_BY).setDefaultValue(getMessages().active());

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
		list.add(new CommandsRequirement(VIEW_BY) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<TAXAgency>(getMessages().taxAgency(),
				"", 20) {
			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// TAXAgency value) {
			// commandList.add(new UserCommand("Delete Taxagency", value
			// .getID()));
			// commandList.add(new UserCommand("update Tax Agency ", value
			// .getName()));
			// }

			@Override
			protected String onSelection(TAXAgency value) {
				return "updateTAXAgency " + value.getName();
			}

			@Override
			protected String getShowMessage() {

				return getMessages().payeeList(getMessages().taxAgency());
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(getMessages().taxAgency());

			}

			@Override
			protected Record createRecord(TAXAgency value) {
				Record taxAgencyrec = new Record(value);
				taxAgencyrec.add(getMessages().name(), value.getName());
				// TODO balance by dates
				taxAgencyrec.add(getMessages().balanceAsOf(),
						getAmountWithCurrency(value.getBalance()));
				return taxAgencyrec;

			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createTAXAgency");

			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
			}

			@Override
			protected List<TAXAgency> getLists(Context context) {

				return getTaxAgencies(context);
			}

		});

	}

	private List<TAXAgency> getTaxAgencies(Context context) {
		String isActive = get(VIEW_BY).getValue();
		Set<TAXAgency> tAXAgencys = context.getCompany().getTaxAgencies();
		List<TAXAgency> result = new ArrayList<TAXAgency>();
		for (TAXAgency taxagency : tAXAgencys) {
			if (isActive.equals(getMessages().active())) {
				if (taxagency.isActive()) {
					result.add(taxagency);
				}

			} else {
				if (!taxagency.isActive()) {
					result.add(taxagency);
				}

			}
		}

		return result;
	}
}
