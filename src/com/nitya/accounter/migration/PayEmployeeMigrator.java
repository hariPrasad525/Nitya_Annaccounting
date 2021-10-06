package com.nitya.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Employee;
import com.nitya.accounter.core.PayEmployee;
import com.nitya.accounter.core.TransactionPayEmployee;

public class PayEmployeeMigrator extends TransactionMigrator<PayEmployee> {
	@Override
	public JSONObject migrate(PayEmployee obj, MigratorContext context) throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("@class", "com.nitya.accounter.shared.payroll.PayEmployee");
		Account payAccount = obj.getPayAccount();
		if (payAccount != null) {
			jsonObj.put("payFrom", context.get("BankAccount", payAccount.getID()));
		}
		{
			List<TransactionPayEmployee> transactionPayEmployee = obj.getTransactionPayEmployee();
			JSONArray array = new JSONArray();
			for (TransactionPayEmployee item : transactionPayEmployee) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("@class", "com.nitya.accounter.shared.payroll.PayEmployeeItem");
				jsonObj.put("payRun", context.get("PayRun", item.getPayRun().getID()));
				array.put(jsonObject);
			}
			jsonObj.put("payEmployeeItems", array);
		}
		Employee employee = obj.getEmployee();
		if (employee != null) {
			jsonObj.put("type", "Employee");
			jsonObj.put("employee", context.get("Employee", obj.getEmployee().getID()));
		} else {
			jsonObj.put("type", "EmployeeGroup");
			jsonObj.put("employeeGroup", context.get("EmployeeGroup", obj.getEmployeeGroup().getID()));
		}
		return jsonObj;
	}
}
