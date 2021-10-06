package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.Warehouse;

public class WarehouseMigrator implements IMigrator<Warehouse> {

	@Override
	public JSONObject migrate(Warehouse obj, MigratorContext context) throws JSONException {
		JSONObject warehouse = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@_lid", _lid);
			jsonObject.put("@id", _lid);
			warehouse.put("com.nitya.accounter.shared.inventory.Warehouse", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
			jsonObject.put("warehouseCode", obj.getWarehouseCode());
			jsonObject.put("warehouseName", obj.getName());
			Contact contact = obj.getContact();
			if (contact != null) {
				jsonObject.put("contactName", contact.getName());
				jsonObject.put("contactNumber", contact.getBusinessPhone());
			}
			jsonObject.put("mobileNumber", obj.getMobileNumber());
			jsonObject.put("dDInumber", obj.getDDINumber());
			jsonObject.put("defaultWarehouse", obj.isDefaultWarehouse());
			// Setting Address reference
			Address wareHouseAddress = obj.getAddress();
			{
				JSONObject address = new JSONObject();
				JSONObject jsonAddress = new JSONObject();
				address.put("org.ecgine.core.shared.Address", jsonAddress);
				String wareHouseAddressLocalID = context.getLocalIdProvider().getOrCreate(wareHouseAddress);
				jsonAddress.put("@_lid", wareHouseAddressLocalID);
				jsonAddress.put("@id", wareHouseAddressLocalID);
				jsonAddress.put("street", wareHouseAddress.getStreet());
				jsonAddress.put("city", wareHouseAddress.getCity());
				jsonAddress.put("stateOrProvince", wareHouseAddress.getStateOrProvinence());
				jsonAddress.put("zipOrPostalCode", wareHouseAddress.getZipOrPostalCode());
				jsonAddress.put("country", wareHouseAddress.getCountryOrRegion());
				jsonObject.put("address", address);
			}
		}
		return warehouse;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}