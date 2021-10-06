/**
 * 
 */
package com.nitya.accounter.mobile;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.User;
import com.nitya.accounter.main.CompanyPreferenceThreadLocal;
import com.nitya.accounter.mobile.store.Output;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;
import com.nitya.accounter.web.server.countries.UnitedKingdom;
import com.nitya.accounter.web.server.util.ICountryPreferences;

/**
 * @author Prasanna Kumar G
 * 
 */
public class PatternResult extends Result {
	public String condition;
	public boolean login;
	public List<Output> outputs;

	/**
	 * @return
	 */
	public CommandList getCommands() {
		CommandList commandList = new CommandList();
		for (Object obj : resultParts) {
			if (obj instanceof CommandList) {
				commandList.addAll((Collection<? extends UserCommand>) obj);
			}
		}
		return commandList;
	}

	public void add(Object obj) {
		resultParts.add(obj);
	}

	public void setOutputs(List<Output> outputs) {
		this.outputs = outputs;
	}

	public Result render(boolean isAuthenticated, Company company) {
		if (login ? !isAuthenticated : false) {
			Result result = new Result();
			result.add(Global.get().messages().youCannotDoThisBeforeLogin());
			result.add(Global.get().messages().pleaseLogin());
			result.setNextCommand("login");
			return result;
		}
		if (condition != null && !checkCondition(condition, company)) {
			Result result = new Result();
			result.add(Global.get().messages()
					.youDoNotHavePermissionToDoThisAction());
			result.add(Global.get().messages()
					.youCanChangePermissionsFromCompanyPreferences());
			CommandList list = new CommandList();
			list.add("companyPreferences");
			result.add(list);
			result.setNextCommand("back");
			return result;
		}
		PatternResult result = new PatternResult();
		for (Output output : outputs) {
			output.add(result, company);
		}
		return result;
	}

	public boolean checkCondition(String condition, Company company) {
		ClientCompanyPreferences preferences = CompanyPreferenceThreadLocal
				.get();
		User user = AccounterThreadLocal.get();
		Set<String> features = user.getClient().getClientSubscription()
				.getSubscription().getFeatures();
		boolean sellServices = preferences.isSellServices();
		boolean sellProducts = preferences.isSellProducts();

		if (condition.equals("trackTax")) {
			return preferences.isTrackTax();
		} else if (condition.equals("trackingQuotes")) {
			return preferences.isDoyouwantEstimates();
		} else if (condition.equals("delayedCharges")) {
			return preferences.isDelayedchargesEnabled();
		} else if (condition.equals("manageBills")) {
			return preferences.isKeepTrackofBills();
		} else if (condition.equals("isNotUK")) {
			ICountryPreferences countryPreferences = company
					.getCountryPreferences();
			return !(countryPreferences instanceof UnitedKingdom);
		} else if (condition.equals("isUK")) {
			ICountryPreferences countryPreferences = company
					.getCountryPreferences();
			return countryPreferences instanceof UnitedKingdom;
		} else if (condition.equals("inventoryEnabled")) {
			return preferences.isInventoryEnabled();
		} else if (condition.equals("salesOrderEnable")) {
			return preferences.isSalesOrderEnabled();
		} else if (condition.equals("iswareHouseEnabled")) {
			return preferences.iswareHouseEnabled();
		} else if (condition.equals("isUnitsEnalbled")) {
			return preferences.isUnitsEnabled();
		} else if (condition.equals("canChangeSettings")) {
			return user.getPermissions().getTypeOfCompanySettingsLockDates() == RolePermissions.TYPE_YES;
		} else if (condition.equals("canDoUserManagement")) {
			return user.isCanDoUserManagement();
		} else if (condition.equals("purchaseorderEnabled")) {
			return preferences.isPurchaseOrderEnabled();
		} else if (condition.equals("serviceItem")) {
			return sellServices;
		} else if (condition.equals("productItem")) {
			return sellProducts && !preferences.isInventoryEnabled();
		} else if (condition.equals("inventoryItem")
				|| condition.equals("nonInventoryItem")) {
			return sellProducts && preferences.isInventoryEnabled();
		} else if (condition.equals("assemblyItem")) {
			return sellProducts && sellServices
					&& preferences.isInventoryEnabled();
		} else if (condition.equals("payrollEnabled")) {
			return features.contains(Features.PAY_ROLL);
		}
		return true;
	}
}
