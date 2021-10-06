package com.nitya.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.nitya.accounter.core.Measurement;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.utils.HibernateUtil;

public abstract class MeasurementRequirement extends
		ListRequirement<Measurement> {

	public MeasurementRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Measurement> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		setMeasurementValue();
		if (getPreferences().isUnitsEnabled()) {
			return super.run(context, makeResult, list, actions);
		}
		return null;
	}

	private void setMeasurementValue() {
		Object value = getValue();
		if (value != null) {
			Session currentSession = HibernateUtil.getCurrentSession();
			Measurement measurement = (Measurement) value;
			measurement = (Measurement) currentSession.load(Measurement.class,
					measurement.getID());
			long id = measurement.getID();
			super.setValue(measurement);
		}
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		setMeasurementValue();
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		super.setDefaultValue(defaultValue);
		Session currentSession = HibernateUtil.getCurrentSession();
		Measurement measurement = (Measurement) defaultValue;
		Object value = getValue();
		if (value != null) {
			measurement = (Measurement) currentSession.load(Measurement.class,
					measurement.getID());
			super.setDefaultValue(measurement);
		}
	}

	@Override
	protected String getEmptyString() {
		return getMessages().noRecordsToShow();
	}

	@Override
	protected Record createRecord(Measurement value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().description(), value.getDesctiption());
		return record;
	}

	@Override
	protected String getDisplayValue(Measurement value) {
		return value.getName();
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		if (getPreferences().isUnitsEnabled()) {
			list.add("createMeasurement");
		}
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().measurement());
	}

	@Override
	protected boolean filter(Measurement e, String name) {
		return e.getName().equals(name);
	}

	@Override
	protected List<Measurement> getLists(Context context) {
		if (!getPreferences().isUnitsEnabled()) {
			ArrayList<Measurement> arrayList = new ArrayList<Measurement>();
			arrayList.add(getCompany().getDefaultMeasurement());
			return arrayList;
		}
		return new ArrayList<Measurement>(context.getCompany()
				.getMeasurements());
	}

}
