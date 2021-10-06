package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.MeasurementUnitRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientUnit;

public class CreateMeasurementCommand extends AbstractCommand {

	private static final String MEASUREMENT_NAME = "Measurement Name";
	private static final String MEASUREMENT_DESCRIPTION = "Measurement Description";
	private static final String UNIT = "Unit";

	private ClientMeasurement measurement;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectObjToUpdate(
								getMessages().measurement()));
				return "measurementList";
			}
			ClientMeasurement clientmeasurement = CommandUtils.getMeasurement(
					context.getCompany(), string);
			if (clientmeasurement == null) {
				addFirstMessage(
						context,
						getMessages().selectObjToUpdate(
								getMessages().measurement()));
				return "measurementList " + string;
			}
			measurement = clientmeasurement;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(MEASUREMENT_NAME).setValue(string);
			}
			measurement = new ClientMeasurement();
		}
		return null;
	}

	private void setValues() {
		get(MEASUREMENT_NAME).setValue(measurement.getName());
		get(MEASUREMENT_DESCRIPTION).setValue(measurement.getDesctiption());
		List<Unit> units = new ArrayList<Unit>();
		List<ClientUnit> clientUnits = measurement.getUnits();
		for (ClientUnit unit : clientUnits) {
			units.add(new Unit(unit.getType(), unit.getFactor()));
		}
		get(UNIT).setValue(units);
	}

	@Override
	protected String getWelcomeMessage() {
		if (measurement.getID() == 0) {
			return getMessages().create(getMessages().measurement());
		}
		return getMessages().updating(getMessages().measurement());
	}

	@Override
	protected String getDetailsMessage() {
		if (measurement.getID() == 0) {
			return getMessages().objIsReadyToCreateWitFollowingDetails(
					getMessages().measurement());
		} else {
			return getMessages().objIsReadyToUpdateWitFollowingDetails(
					getMessages().measurement());
		}
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		if (measurement.getID() == 0) {
			return getMessages()
					.createSuccessfully(getMessages().measurement());
		} else {
			return getMessages()
					.updateSuccessfully(getMessages().measurement());
		}
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(MEASUREMENT_NAME, getMessages()
				.pleaseEnter(getMessages().measurementName()), getMessages()
				.measurementName(), false, true) {
			@Override
			public void setValue(Object value) {
				if (isMeasurementExistsWithSameName((String) value)) {
					addFirstMessage(getMessages().alreadyExist());
					return;
				}
				addFirstMessage(getMessages().pleaseEnter(
						getMessages().measurementName()));
				super.setValue(value);
			}
		});

		list.add(new StringRequirement(MEASUREMENT_DESCRIPTION, getMessages()
				.pleaseEnter(getMessages().description()), getMessages()
				.description(), true, true));

		list.add(new MeasurementUnitRequirement(UNIT, getMessages()
				.pleaseSelect(getMessages().unit()), getMessages().unit(),
				true, false, true) {

			@Override
			protected List<Unit> getList() {
				return null;
			}

		});
	}

	protected boolean isMeasurementExistsWithSameName(String value) {
		Set<Measurement> measurements = getCompany().getMeasurements();
		for (Measurement measurement : measurements) {
			if (measurement.getID() != this.measurement.getID()
					&& measurement.getName().equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = get(MEASUREMENT_NAME).getValue();
		String description = get(MEASUREMENT_DESCRIPTION).getValue();
		List<Unit> units = get(UNIT).getValue();
		List<ClientUnit> clientUnits = new ArrayList<ClientUnit>();
		boolean hasDefaultUnit = false;
		for (Unit unit : units) {
			if (!hasDefaultUnit) {
				hasDefaultUnit = unit.isDefault();
			}
			ClientUnit clientUnit = new ClientUnit(unit.getType(),
					unit.getFactor());
			clientUnit.setId(unit.getID());
			clientUnit.setMeasurement(measurement);
			clientUnit.setVersion(unit.getVersion());
			clientUnit.setDefault(unit.isDefault());
			clientUnits.add(clientUnit);
		}
		if (!hasDefaultUnit) {
			return new Result(getMessages().pleaseSelectDefaultUnit());
		}
		measurement.setUnits(clientUnits);
		measurement.setName(name);
		measurement.setDesctiption(description);

		create(measurement, context);
		return null;
	}
}
