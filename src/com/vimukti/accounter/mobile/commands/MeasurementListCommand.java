package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.utils.HibernateUtil;

public class MeasurementListCommand extends AbstractCommand {

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
		list.add(new ShowListRequirement<Measurement>(getMessages()
				.measurementList(), getMessages()
				.pleaseSelecAnyWareHouseTransferToViewDetails(), 40) {

			@Override
			protected String onSelection(Measurement value) {
				return "updateMeasurement " + value.getName();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().measurementList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(Measurement value) {
				Record record = new Record(value);
				record.add(getMessages().measurementName(), value.getName());
				record.add(getMessages().measurementDescription(),
						value.getDesctiption());
				record.add(getMessages().unitName(),
						MeasurementListCommand.this.getDefaultUnitName(value));
				record.add(getMessages().factor(), getDefaultUnitFactor(value));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createMeasurement");
			}

			@Override
			protected boolean filter(Measurement e, String name) {
				return false;
			}

			@Override
			protected ArrayList<Measurement> getLists(Context context) {
				return getMeasurementList();
			}
		});
	}

	protected String getDefaultUnitName(Measurement value) {
		for (Unit unit : value.getUnits()) {
			if (unit.isDefault()) {
				return unit.getType();
			}
		}
		return "";
	}

	private double getDefaultUnitFactor(Measurement value) {
		for (Unit unit : value.getUnits()) {
			if (unit.isDefault()) {
				return unit.getFactor();
			}
		}
		return 1;
	}

	protected ArrayList<Measurement> getMeasurementList() {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("list.All.Units").setEntity(
				"company", getCompany());
		return (ArrayList<Measurement>) query.list();

	}

}
