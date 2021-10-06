package com.nitya.accounter.mobile.commands;

import java.util.List;

import com.nitya.accounter.core.Location;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.NameRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientLocation;

public class CreateLocationCommand extends AbstractCommand {

	private static final String LOCATION_NAME = "location name";
	ClientLocation location;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(LOCATION_NAME, getMessages().pleaseEnter(
				getMessages().location()), getMessages().location(), false,
				true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		location.setLocationName((String) get(LOCATION_NAME).getValue());
		create(location, context);
		markDone();
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().location()));
				return "locationlist";
			}
			Location locationByName = CommandUtils.getLocationByName(
					context.getCompany(), string);
			if (locationByName == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().location()));
				return "locationlist " + string;
			}
			location = (ClientLocation) CommandUtils.getClientObjectById(
					locationByName.getID(), AccounterCoreType.LOCATION, context
							.getCompany().getId());
			get(LOCATION_NAME).setValue(location.getName());
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(LOCATION_NAME).setValue(string);
			}
			location = new ClientLocation();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return location.getID() == 0 ? getMessages().create(
				getMessages().location()) : getMessages().updating(
				getMessages().location());
	}

	@Override
	protected String getDetailsMessage() {
		return location.getID() == 0 ? getMessages().readyToCreate(
				getMessages().location()) : getMessages().readyToUpdate(
				getMessages().location());
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return location.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().location()) : getMessages().updateSuccessfully(
				getMessages().location());
	}

}
