package com.vimukti.accounter.mobile.commands.preferences;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

public class VendorAndPurchasePreferencesCommand extends
		AbstractCompanyPreferencesCommand {
	private static final String MANAGE_BILLS = "managebills";
	private static final String TRACK_PRICELEVEL = "enablePriceLevel";
	private static final String TRACK_EXPENSEANDPRODUCT = "trackexpenses";
	private static final String BILLABLE_EXPENSES = "billableexpenses";
	private static final String VENDOR_TERMINOLOGY = "vendorterminology";
	private static final String PURCHASE_ORDERS = "purchaseorders";

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new BooleanRequirement(MANAGE_BILLS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().manageBillsYouOwe();
			}

			@Override
			protected String getFalseString() {
				return getMessages().dontManageBillsYouOwe();
			}
		});

		list.add(new BooleanRequirement(TRACK_PRICELEVEL, true) {

			@Override
			protected String getTrueString() {
				return getMessages().trackingPriceLevelenabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().trackingPriceLeveldisabled();
			}
		});

		list.add(new BooleanRequirement(TRACK_EXPENSEANDPRODUCT, true) {

			@Override
			protected String getTrueString() {
				return getMessages()
						.Expenseandproductservicetrackingbycustomer(
								Global.get().Customer());
			}

			@Override
			protected String getFalseString() {
				return getMessages().nottrackProducandServicesbyCustomer();
			}
		});

		list.add(new BooleanRequirement(BILLABLE_EXPENSES, true) {

			@Override
			protected String getTrueString() {
				return getMessages().useBillabelExpenses();
			}

			@Override
			protected String getFalseString() {
				return getMessages().doNotuseBillabelExpenses();
			}
		});

		list.add(new BooleanRequirement(PURCHASE_ORDERS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().enabled() + " "
						+ getMessages().purchaseOrders();
			}

			@Override
			protected String getFalseString() {
				return getMessages().disabled() + " "
						+ getMessages().purchaseOrders();
			}
		});

		list.add(new BooleanRequirement(VENDOR_TERMINOLOGY, true) {

			@Override
			protected String getTrueString() {
				return getMessages().Vendor() + " "
						+ getMessages().terminology() + " :"
						+ getMessages().Supplier();
			}

			@Override
			protected String getFalseString() {
				return getMessages().Vendor() + " "
						+ getMessages().terminology() + " :"
						+ getMessages().Vendor();
			}
		});
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		ClientCompanyPreferences preferences = context.getPreferences();
		get(MANAGE_BILLS).setValue(preferences.isKeepTrackofBills());
		get(TRACK_EXPENSEANDPRODUCT).setValue(
				preferences.isProductandSerivesTrackingByCustomerEnabled());
		get(BILLABLE_EXPENSES).setValue(
				preferences.isBillableExpsesEnbldForProductandServices());
		get(PURCHASE_ORDERS).setValue(preferences.isPurchaseOrderEnabled());
		get(TRACK_PRICELEVEL).setValue(preferences.isPricingLevelsEnabled());
		if (preferences.getReferVendors() == 1) {
			get(VENDOR_TERMINOLOGY).setValue(true);
		}
		return null;

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();
		boolean trackexpenses = get(TRACK_EXPENSEANDPRODUCT).getValue();
		boolean billableexpenses = get(BILLABLE_EXPENSES).getValue();
		boolean ispriceLevelEnabled = get(TRACK_PRICELEVEL).getValue();
		boolean isPurchaseOrderEnabled = get(PURCHASE_ORDERS).getValue();
		preferences.setPricingLevelsEnabled(ispriceLevelEnabled);
		preferences
				.setProductandSerivesTrackingByCustomerEnabled(trackexpenses);
		preferences
				.setBillableExpsesEnbldForProductandServices(billableexpenses);
		boolean supplierTerm = get(VENDOR_TERMINOLOGY).getValue();
		if (supplierTerm) {
			preferences.setReferVendors(1);
		} else {
			preferences.setReferVendors(2);
		}
		preferences.setPurchaseOrderEnabled(isPurchaseOrderEnabled);
		boolean mangebills = get(MANAGE_BILLS).getValue();
		preferences.setKeepTrackofBills(mangebills);
		savePreferences(context, preferences);
		return null;
	}
}
