package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.utils.HibernateUtil;

public class WarehouseRequirement extends ListRequirement<Warehouse> {

	public WarehouseRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Warehouse> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		setWareHouseValue();
		return super.run(context, makeResult, list, actions);
	}

	private void setWareHouseValue() {
		Object value = getValue();
		if (value != null) {
			Session currentSession = HibernateUtil.getCurrentSession();
			Warehouse warehouse = (Warehouse) value;
			warehouse = (Warehouse) currentSession.load(Warehouse.class,
					warehouse.getID());
			long id = warehouse.getID();
			super.setValue(warehouse);
		}
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		setWareHouseValue();
	}

	@Override
	protected String getEmptyString() {
		return getMessages().noRecordsToShow();
	}

	@Override
	protected Record createRecord(Warehouse value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(Warehouse value) {
		return value.getName();
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		if (getPreferences().iswareHouseEnabled()) {
			list.add("newWareHouse");
		}
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().wareHouse());
	}

	@Override
	protected boolean filter(Warehouse e, String name) {
		return e.getName().equals(name);
	}

	@Override
	protected List<Warehouse> getLists(Context context) {
		ArrayList<Warehouse> arrayList = new ArrayList<Warehouse>();
		if (!getPreferences().iswareHouseEnabled()) {
			arrayList.add(getCompany().getDefaultWarehouse());
			return arrayList;
		}
		return new ArrayList<Warehouse>(context.getCompany().getWarehouses());
	}

	@Override
	protected String getSetMessage() {
		return null;
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		super.setDefaultValue(defaultValue);
		Object value = getValue();
		if (value != null) {
			Session currentSession = HibernateUtil.getCurrentSession();
			Warehouse warehouse = (Warehouse) value;
			warehouse = (Warehouse) currentSession.load(Warehouse.class,
					warehouse.getID());
			super.setDefaultValue(warehouse);
		}
	}

}
