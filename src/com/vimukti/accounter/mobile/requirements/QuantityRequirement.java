package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientQuantity;

public abstract class QuantityRequirement extends
		MultiRequirement<ClientQuantity> {

	private static final String VALUE = "itemvalue";
	private static final String UNIT = "itemunit";

	public QuantityRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional) {
		super(requirementName, enterString, recordName, isOptional, true);
		ClientQuantity clientQuantity = new ClientQuantity();
		clientQuantity.setValue(1.0);
		setDefaultValue(clientQuantity);
	}

	@Override
	protected void setDefaultValues() {
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new AmountRequirement(VALUE, getMessages().pleaseEnter(
				getValueRecordName()), getValueRecordName(), false, true));

		list.add(new ListRequirement<Unit>(UNIT, getMessages().pleaseSelect(
				getMessages().unit()), getMessages().unit(), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				setUnitValue();
				return super.run(context, makeResult, list, actions);
			}

			private void setUnitValue() {
				Object value = getValue();
				if (value != null) {
					Session currentSession = HibernateUtil.getCurrentSession();
					Unit unit = (Unit) value;
					unit = (Unit) currentSession.load(Unit.class, unit.getID());
					long id = unit.getID();
					super.setValue(unit);
				}
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				setUnitValue();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().units());
			}

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected Record createRecord(Unit value) {
				Record record = new Record(value);
				record.add(getMessages().unitName(), value.getType());
				return record;
			}

			@Override
			protected String getDisplayValue(Unit value) {
				return value == null ? " " : value.getType();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().unit());
			}

			@Override
			protected boolean filter(Unit e, String name) {
				return false;
			}

			@Override
			protected List<Unit> getLists(Context context) {
				return QuantityRequirement.this.getLists(context);
			}
		});
	}

	protected String getValueRecordName() {
		return getMessages().adjustedQty();
	}

	protected abstract List<Unit> getLists(Context context);

	@Override
	protected String getDisplayValue() {
		Unit clientUnit = getRequirement(UNIT).getValue();
		return getRequirement(VALUE).getValue() + " "
				+ (clientUnit == null ? " " : clientUnit.getType());
	}

	@Override
	protected Result onFinish(Context context) {
		ClientQuantity quantity = getValue();
		Unit unit = getRequirement(UNIT).getValue();
		quantity.setUnit(unit == null ? 0 : unit.getID());
		quantity.setValue((Double) getRequirement(VALUE).getValue());
		return null;
	}

	public void setDefaultUnit(Unit defaultUnit) {
		getRequirement(UNIT).setValue(defaultUnit);
	}

	@Override
	public void setEditable(boolean isEditable) {
		getRequirement(UNIT).setEditable(isEditable);
		getRequirement(VALUE).setEditable(isEditable);
		super.setEditable(isEditable);
	}

	@Override
	public void setValue(Object value) {
		ClientQuantity clientQuantity = (ClientQuantity) value;
		if (clientQuantity != null) {
			getRequirement(UNIT).setValue(
					CommandUtils.getServerObjectById(clientQuantity.getUnit(),
							AccounterCoreType.UNIT));
			getRequirement(VALUE).setValue(clientQuantity.getValue());
		}
		super.setValue(value);
	}
}
