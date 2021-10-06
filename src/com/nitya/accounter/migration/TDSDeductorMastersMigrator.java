package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.TDSDeductorMasters;

public class TDSDeductorMastersMigrator implements IMigrator<TDSDeductorMasters> {

	@Override
	public JSONObject migrate(TDSDeductorMasters obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.tax.TDSDeductorMasters", jsonObject);
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("deductorName", obj.getDeductorName());
		jsonObject.put("branch", obj.getBranch());
		jsonObject.put("flatNo", obj.getFlatNo());
		jsonObject.put("buildingName", obj.getBuildingName());
		jsonObject.put("area", obj.getArea());
		jsonObject.put("city", obj.getCity());
		jsonObject.put("state", obj.getState());
		jsonObject.put("pinCode", obj.getPinCode());
		jsonObject.put("addressdChanged", obj.isAddressdChanged());
		jsonObject.put("telephoneNumber", String.valueOf(obj.getTelephoneNumber()));
		jsonObject.put("faxNo", String.valueOf(obj.getFaxNo()));
		jsonObject.put("emailID", obj.getEmailID());
		jsonObject.put("deductorType", PicklistUtilMigrator.getDeductorTypeIndentity(obj.getDeductorType()));
		jsonObject.put("govtState", obj.getGovtState());
		jsonObject.put("paoCode", obj.getPaoCode());
		jsonObject.put("paoRegistration", obj.getPaoRegistration());
		jsonObject.put("ddoCode", obj.getDdoCode());
		jsonObject.put("ddoRegistration", obj.getDdoRegistration());
		jsonObject.put("ministryDeptName", obj.getMinistryDeptName());
		jsonObject.put("ministryDeptOtherName", obj.getMinistryDeptOtherName());
		jsonObject.put("tanNumber", obj.getTanNumber());
		jsonObject.put("panNumber", obj.getPanNumber());
		jsonObject.put("stdCode", obj.getStdCode());
		jsonObject.put("isAddressSameForResopsiblePerson", obj.isAddressSameForResopsiblePerson());
		Address taxOfficeAdd = obj.getTaxOfficeAddress();
		if (taxOfficeAdd != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String localId = context.getLocalIdProvider().getOrCreate(taxOfficeAdd);
			jsonAddress.put("@_lid", localId);
			jsonAddress.put("@id", localId);
			jsonAddress.put("street", taxOfficeAdd.getStreet());
			jsonAddress.put("city", taxOfficeAdd.getCity());
			jsonAddress.put("stateOrProvince", taxOfficeAdd.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", taxOfficeAdd.getZipOrPostalCode());
			jsonAddress.put("country", taxOfficeAdd.getCountryOrRegion());
			jsonObject.put("taxOfficeAddress", address);
		}
		// obj.getStatus() return 'Government' or 'Other'
		jsonObject.put("status", obj.getStatus().toUpperCase());

		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}