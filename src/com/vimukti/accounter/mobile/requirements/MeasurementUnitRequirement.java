package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;

public abstract class MeasurementUnitRequirement extends
		AbstractTableRequirement<Unit> {

	private static final String UNIT_TYPE = "unitType";
	private static final String UNIT_FACTOR = "unitFactor";
	private static final String IS_DEFAULT = "isdefault";

	public MeasurementUnitRequirement(String requirementName,
			String enterString, String recordName, boolean isCreatable,
			boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isCreatable,
				isOptional, isAllowFromContext);
		setDefaultValue(new ArrayList<Unit>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new StringRequirement(UNIT_TYPE, getMessages().pleaseEnter(
				getMessages().unitName()), getMessages().unitName(), false,
				true));

		list.add(new AmountRequirement(UNIT_FACTOR, getMessages().pleaseEnter(
				getMessages().factor()), getMessages().factor(), true, true));

		list.add(new BooleanRequirement(IS_DEFAULT, true) {

			@Override
			public void setValue(Object value) {
				Boolean isDefault = (Boolean) value;
				if (isDefault != null && isDefault) {
					MeasurementUnitRequirement.this
							.setOtherRecordsDefalutValue();
				}
				super.setValue(value);
			}

			@Override
			protected String getTrueString() {
				return getMessages().unitDefault();
			}

			@Override
			protected String getFalseString() {
				return getMessages().unitNotDefault();
			}
		});
	}

	protected void setOtherRecordsDefalutValue() {
		List<Unit> units = getValue();
		for (Unit unit : units) {
			if (unit.getType() != null
					&& !unit.getType().equals(currentValue.getType())) {
				unit.setDefault(false);
			}
		}
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().units());
	}

	@Override
	protected void getRequirementsValues(Unit obj) {
		String unitName = get(UNIT_TYPE).getValue();
		obj.setType(unitName);
		double factor = get(UNIT_FACTOR).getValue();
		obj.setFactor(factor);
		Boolean isDefault = get(IS_DEFAULT).getValue();
		obj.setDefault(isDefault);
	}

	@Override
	protected void setRequirementsDefaultValues(Unit obj) {
		get(UNIT_TYPE).setDefaultValue(obj.getType());
		get(UNIT_FACTOR).setDefaultValue(obj.getFactor());
		get(IS_DEFAULT).setDefaultValue(obj.isDefault());
	}

	@Override
	protected Unit getNewObject() {
		Unit unit = new Unit();
		unit.setFactor(1.0);
		return unit;
	}

	@Override
	protected Record createFullRecord(Unit t) {
		Record record = new Record(t);
		record.add(getMessages().unitName(), t.getType());
		record.add(getMessages().factor(), t.getFactor());
		record.add(getMessages().defaultWare(), t.isDefault());
		return record;
	}

	@Override
	protected Record createRecord(Unit t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<Unit> values = getValue();
		return values.isEmpty() ? getMessages().addOf(getMessages().units())
				: getMessages().addMore(getMessages().units());
	}

}
