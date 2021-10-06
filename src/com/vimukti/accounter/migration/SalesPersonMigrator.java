package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.SalesPerson;

public class SalesPersonMigrator implements IMigrator<SalesPerson> {

	@Override
	public JSONObject migrate(SalesPerson salesPerson, MigratorContext context) throws JSONException {
		JSONObject salesPersonJSON = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(salesPerson);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			salesPersonJSON.put("com.vimukti.accounter.shared.customer.SalesPerson", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(salesPerson, jsonObject, context);
			jsonObject.put("name", salesPerson.getFirstName());
			jsonObject.put("fileAs", salesPerson.getFileAs());
			jsonObject.put("jobTitle", salesPerson.getJobTitle());
			jsonObject.put("gender", salesPerson.getGender());
			FinanceDate dateOfBirth = salesPerson.getDateOfBirth();
			if (dateOfBirth != null) {
				jsonObject.put("dateOfBirth", dateOfBirth.toEpochDay());
			}
			FinanceDate dateOfHire = salesPerson.getDateOfHire();
			if (dateOfHire != null) {
				jsonObject.put("dateOfHire", dateOfHire.toEpochDay());
			}
			FinanceDate financeDateOfLastReview = salesPerson.getFinanceDateOfLastReview();
			if (financeDateOfLastReview != null) {
				jsonObject.put("dateOfLastReview", financeDateOfLastReview.toEpochDay());
			}
			jsonObject.put("inActive", !salesPerson.isActive());
			FinanceDate dateOfRelease = salesPerson.getDateOfRelease();
			if (dateOfRelease != null) {
				jsonObject.put("dateOfRelease", dateOfRelease.toEpochDay());
			}
			jsonObject.put("phoneNo", salesPerson.getPhoneNo());
			jsonObject.put("email", salesPerson.getEmail());
			jsonObject.put("webPageAddress", salesPerson.getWebPageAddress());
			Address salesPersonAddress = salesPerson.getAddress();
			if (salesPersonAddress != null) {
				JSONObject address = new JSONObject();
				JSONObject jsonAddress = new JSONObject();
				address.put("org.ecgine.core.shared.Address", jsonAddress);
				String salesPersonAddressLocalID = context.getLocalIdProvider().getOrCreate(salesPersonAddress);
				jsonAddress.put("@_lid", salesPersonAddressLocalID);
				jsonAddress.put("@id", salesPersonAddressLocalID);
				jsonAddress.put("street", salesPersonAddress.getStreet());
				jsonAddress.put("city", salesPersonAddress.getCity());
				jsonAddress.put("stateOrProvince", salesPersonAddress.getStateOrProvinence());
				jsonAddress.put("zipOrPostalCode", salesPersonAddress.getZipOrPostalCode());
				jsonAddress.put("country", salesPersonAddress.getCountryOrRegion());
				jsonObject.put("address", address);

			}
		}
		return salesPersonJSON;

	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}