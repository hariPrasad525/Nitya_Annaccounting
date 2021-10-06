package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.UserPermissions;

public class PermissionSetMigrator implements IMigrator<User> {

	@Override
	public JSONObject migrate(User obj, MigratorContext context)
			throws JSONException {
		User createdBy = context.getCompany().getCreatedBy();
		if (createdBy == obj) {
			return null;
		}
		JSONObject permissionSet = new JSONObject();
		UserPermissions permissions = obj.getPermissions();
		JSONArray permissionArray = new JSONArray();
		JSONObject permission1 = new JSONObject();
		if (permissions.getInvoicesAndPayments() != 0) {
			permission1.put("identity", "InvoicesReceivePayments");
			permissionArray.put(permission1);
		}
		JSONObject permission2 = new JSONObject();
		if (permissions.getTypeOfBankReconcilation() != 0) {
			permission2.put("identity", "BankingReconciliation");
			permissionArray.put(permission2);
		}
		JSONObject permission3 = new JSONObject();
		if (permissions.getTypeOfInventoryWarehouse() != 0) {
			permission3.put("identity", "InventoryWarehouse");
			permissionArray.put(permission3);
		}
		JSONObject permission4 = new JSONObject();
		if (permissions.getTypeOfInvoicesBills() != 0) {
			permission4.put("identity", "CreateInvoiceBills");
			permissionArray.put(permission4);
		}
		JSONObject permission5 = new JSONObject();
		if (permissions.getTypeOfManageAccounts() != 0) {
			permission5.put("identity", "ManageAccountsDoJournalEntries");
			permissionArray.put(permission5);
		}
		JSONObject permission6 = new JSONObject();
		if (permissions.getTypeOfPayBillsPayments() != 0) {
			permission6.put("identity", "PayBillsReceivePayments");
			permissionArray.put(permission6);
		}
		JSONObject permission7 = new JSONObject();
		if (permissions.getTypeOfCompanySettingsLockDates() != 0) {
			permission7.put("identity", "ChangeCompanySettingsLockDates");
			permissionArray.put(permission7);
		}
		JSONObject permission8 = new JSONObject();
		if (permissions.getTypeOfViewReports() != 0) {
			permission8.put("identity", "ViewReports");
			permissionArray.put(permission8);
		}
		JSONObject permission9 = new JSONObject();
		if (permissions.getTypeOfSaveasDrafts() != 0) {
			permission9.put("identity", "SaveAsDraft");
			permissionArray.put(permission9);
		}
		permissionSet.put("permissions", permissionArray);
		return permissionSet;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub
		
	}
}
