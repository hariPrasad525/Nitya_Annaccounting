package com.nitya.accounter.migration;

import java.util.Set;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Employee;
import com.nitya.accounter.core.EmployeeGroup;

public class EmployeeMigrator implements IMigrator<Employee> {

	@Override
	public JSONObject migrate(Employee obj, MigratorContext context) throws JSONException {
		JSONObject employee = new JSONObject();
		employee.put("@class", "com.nitya.accounter.shared.payroll.Employee");
		CommonFieldsMigrator.migrateCommonFields(obj, employee, context);
		// PANorEIN
		employee.put("pANorEIN", obj.getPANno());
		// EmployeeGroup
		EmployeeGroup group = obj.getGroup();
		if (group != null) {
			employee.put("employeeGroup", context.get("EmployeeGroup", group.getID()));
		}

		employee.put("designation", obj.getDesignation());
		employee.put("workingLocation", obj.getLocation());
		// RelationShip field
		employee.put("identification", obj.getNumber());
		employee.put("name", obj.getName());
		employee.put("comments", obj.getMemo());
		employee.put("email", obj.getEmail());
		employee.put("phone", obj.getPhoneNo());
		employee.put("fax", obj.getFaxNo());

		JSONObject jsonAddress = new JSONObject();
		jsonAddress.put("@class", "org.ecgine.core.shared.Address");
		Set<Address> address = obj.getAddress();
		for (Address primaryAddress : address) {
			if (primaryAddress.isSelected()) {
				jsonAddress.put("street", primaryAddress.getStreet());
				jsonAddress.put("city", primaryAddress.getCity());
				jsonAddress.put("stateOrProvince", primaryAddress.getStateOrProvinence());
				jsonAddress.put("zipOrPostalCode", primaryAddress.getZipOrPostalCode());
				jsonAddress.put("country", primaryAddress.getCountryOrRegion());
			}
		}
		if (address != null) {
			employee.put("address", jsonAddress);
		}
		employee.put("inActive", !obj.isActive());
		// bankName
		employee.put("bankName", obj.getBankName());
		// bankAccountNumber
		employee.put("bankAccountNumber", obj.getBankAccountNo());
		// bankBranch
		employee.put("bankBranch", obj.getBankBranch());

		return employee;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
