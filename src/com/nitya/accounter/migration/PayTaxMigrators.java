package com.nitya.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Employee;
import com.nitya.accounter.core.PayrollPayTax;
import com.nitya.accounter.core.PayrollTransactionPayTax;

public class PayTaxMigrators extends TransactionMigrator<PayrollPayTax> {

	@Override
	public JSONObject migrate(PayrollPayTax obj, MigratorContext context) throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("@class", "com.nitya.accounter.shared.payroll.PayTax");
		Account payAccount = obj.getPayAccount();
		if (payAccount != null) {
			jsonObj.put("payFrom", context.get("BankAccount", payAccount.getID()));
		}
		{
			List<PayrollTransactionPayTax> transactionPayTax = obj.getTransactionPayTax();
			JSONArray array = new JSONArray();
			for (PayrollTransactionPayTax item : transactionPayTax) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("@class", "com.nitya.accounter.shared.payroll.PayTaxItem");
				jsonObj.put("payRun", context.get("PayRun", item.getPayRun().getID()));
				array.put(jsonObject);
			}
			jsonObj.put("payTaxItems", array);
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


