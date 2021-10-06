package com.vimukti.accounter.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.Unit;

public class MeasurementMigrator implements IMigrator<Measurement> {

	@Override
	public JSONObject migrate(Measurement obj, MigratorContext context) throws JSONException {
		JSONObject measurement = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			Map<String, List<Long>> childrenMap = context.getChildrenMap();
			context.putChilderName("Measurement", "units");
			String key = "com.vimukti.accounter.shared.inventory.Unit";
			List<Long> list = childrenMap.get(key);
			if (list == null) {
				list = new ArrayList<Long>();
				childrenMap.put(key, list);
			}
			measurement.put("com.vimukti.accounter.shared.inventory.Measurement", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			JSONArray units = new JSONArray();
			JSONObject unitsObj = new JSONObject();
			unitsObj.put("com.vimukti.accounter.shared.inventory.Unit", units);
			for (Unit unit : obj.getUnits()) {
				JSONObject unitObject = new JSONObject();
				unitObject.put("name", unit.getType());
				String unitlid = context.getLocalIdProvider().getOrCreate(unit);
				unitObject.put("@id", unitlid);
				unitObject.put("@_lid", unitlid);
				unitObject.put("factor", unit.getFactor());
				list.add(unit.getID());
				units.put(unitObject);
				if (unit.isDefault()) {
					JSONObject defaultUnitObject = new JSONObject();
					{
						JSONObject json = new JSONObject();
						json.put("@id", unitlid);
						json.put("@_lid", unitlid);
						defaultUnitObject.put("com.vimukti.accounter.shared.inventory.Unit", json);
					}
					jsonObject.put("defaultUnit", defaultUnitObject);
				}
			}
			jsonObject.put("units", unitsObj);
			jsonObject.put("name", obj.getName());
		}
		return measurement;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
