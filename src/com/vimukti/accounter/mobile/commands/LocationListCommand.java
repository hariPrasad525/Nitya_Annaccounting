package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.Global;

public class LocationListCommand extends AbstractCommand {

	private static final String LOCATIONS = "Locations";

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
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<Location>(LOCATIONS, null, 20) {

			@Override
			protected String onSelection(Location value) {
				return "updateLocation " + value.getName();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().locationsList(Global.get().Location());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().location());
			}

			@Override
			protected Record createRecord(Location value) {
				Record record = new Record(value);
				record.add(getMessages().locationName(Global.get().Location()),
						value.getName());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createLocation");
			}

			@Override
			protected boolean filter(Location e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
			}

			@Override
			protected List<Location> getLists(Context context) {
				return new ArrayList<Location>(context.getCompany()
						.getLocations());
			}
		});

	}
}
