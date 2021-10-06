package com.vimukti.accounter.mobile.commands.preferences;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

public class CustomerAndSalesPreferencesCommand extends
		AbstractCompanyPreferencesCommand {
	private static final String SERVICE_PRODUCTS_BOTH = "serprobothlist";
	private static final String INVENTORY_TRACKING = "inventorytracking";
	private static final String MULTIPLE_WAREHOUSES = "multiplewarehouses";
	private static final String AGING_DETAILS = "aagingdetails";
	private static final String ENABLE_DISCOUNTS = "enablediscounts";
	private static final String ENABLE_UNITS = "enableunits";
	private static final String INCLUDE_ESTIMATES = "includeestimates";
	private static final String INVENTORY_SCHEME = "inventoryschemes";
	private static final String USE_DELAYED_CHARGES = "usedelayedcharges";
	private static final String DO_YOU_Do_SHIPPING = "doyoudoshipping";
	private static final String CUSTOMER_TERMINOLOGY = "customerterminalogy";
	private static final String SALES_ORDERS = "salesorders";
	private static final String TAXITEM_TRANSACTIONS = "taxitemtransactions";
	private static final String DISCOUNT_ACCOUNT = "discountaccount";

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new StringListRequirement(SERVICE_PRODUCTS_BOTH, getMessages()
				.productAndService(), getMessages().productAndService(), true,
				true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().productAndService());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getServiceProductBothList();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().productAndServices());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new BooleanRequirement(INVENTORY_TRACKING, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String serviceProductBoth = get(SERVICE_PRODUCTS_BOTH)
						.getValue();
				Integer servProBoth = getServiceProductBothList().indexOf(
						serviceProductBoth);
				if (servProBoth != 0) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().inventoryTracking() + " "
						+ getMessages().enabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().inventoryTracking() + " "
						+ getMessages().disabled();
			}
		});

		list.add(new BooleanRequirement(MULTIPLE_WAREHOUSES, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String serviceProductBoth = get(SERVICE_PRODUCTS_BOTH)
						.getValue();
				Integer servProBoth = getServiceProductBothList().indexOf(
						serviceProductBoth);
				boolean inventoryTracking = get(INVENTORY_TRACKING).getValue();
				if (inventoryTracking && servProBoth != 0) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().hadMultipleWarehouses();
			}

			@Override
			protected String getFalseString() {
				return getMessages().nothadMultipleWarehouses();
			}
		});

		list.add(new BooleanRequirement(ENABLE_UNITS, true) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String serviceProductBoth = get(SERVICE_PRODUCTS_BOTH)
						.getValue();
				Integer servProBoth = getServiceProductBothList().indexOf(
						serviceProductBoth);
				boolean inventoryTracking = get(INVENTORY_TRACKING).getValue();
				if (inventoryTracking && servProBoth != 0) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().enabled() + " " + getMessages().units();
			}

			@Override
			protected String getFalseString() {
				return getMessages().disabled() + " " + getMessages().units();
			}
		});

		list.add(new StringListRequirement(INVENTORY_SCHEME, getMessages()
				.acceptEstimate(), getMessages().inventoryScheme(), true, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String serviceProductBoth = get(SERVICE_PRODUCTS_BOTH)
						.getValue();
				Integer servProBoth = getServiceProductBothList().indexOf(
						serviceProductBoth);
				boolean inventoryTracking = get(INVENTORY_TRACKING).getValue();
				if (inventoryTracking && servProBoth != 0) {
					return super.run(context, makeResult, list, actions);
				}
				return null;

			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().inventoryScheme());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getInventorySchemesList();
			}

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new BooleanRequirement(AGING_DETAILS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().ageingforduedate();
			}

			@Override
			protected String getFalseString() {
				return getMessages().ageingfortransactiondate();
			}
		});

		list.add(new BooleanRequirement(ENABLE_DISCOUNTS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().trackingDiscountsEnabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().trackingDiscountsDisabled();
			}
		});

		list.add(new BooleanRequirement(TAXITEM_TRANSACTIONS, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean ischargingtax = get(ENABLE_DISCOUNTS).getValue();
				if (ischargingtax) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().oneperdetailline();
			}

			@Override
			protected String getFalseString() {
				return getMessages().onepertransaction();
			}
		});

		list.add(new StringListRequirement(DISCOUNT_ACCOUNT, getMessages()
				.discountAccount(), getMessages().discountAccount(), true,
				true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean discountsTracking = get(ENABLE_DISCOUNTS).getValue();
				if (discountsTracking) {
					return super.run(context, makeResult, list, actions);
				}
				return null;

			}

			@Override
			protected String getEmptyString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseEnter(
						getMessages().discountAccount());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getDiscountAccounts(context);
			}
		});
		list.add(new BooleanRequirement(SALES_ORDERS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().enabled() + " "
						+ getMessages().salesOrders();
			}

			@Override
			protected String getFalseString() {
				return getMessages().disabled() + " "
						+ getMessages().salesOrders();
			}
		});

		list.add(new StringListRequirement(INCLUDE_ESTIMATES, getMessages()
				.acceptEstimate(), getMessages().acceptEstimate(), true, true,
				null) {
			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().acceptEstimate());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getAcceptEstimateList();
			}

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new BooleanRequirement(USE_DELAYED_CHARGES, true) {

			@Override
			protected String getTrueString() {
				return getMessages().delayedCharges();

			}

			@Override
			protected String getFalseString() {
				return getMessages().donotUsedelayedCharges();
			}
		});

		list.add(new BooleanRequirement(DO_YOU_Do_SHIPPING, true) {

			@Override
			protected String getTrueString() {
				return getMessages().iDoShipping();

			}

			@Override
			protected String getFalseString() {
				return getMessages().iDontdoShipping();
			}
		});

		list.add(new StringListRequirement(CUSTOMER_TERMINOLOGY, getMessages()
				.customer() + " " + getMessages().terminology(), getMessages()
				.customer() + " " + getMessages().terminology(), true, true,
				null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().customer() + " "
								+ getMessages().terminology());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCustomerTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().customer() + " "
								+ getMessages().terminology());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
	}

	protected List<String> getDiscountAccounts(Context context) {
		return new ArrayList<String>();
	}

	protected List<String> getInventorySchemesList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(getMessages().firstInfirstOut());
		arrayList.add(getMessages().lastInfirstOut());
		arrayList.add(getMessages().average());
		return arrayList;
	}

	private List<String> getServiceProductBothList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(getMessages().services_labelonly());
		arrayList.add(getMessages().products_labelonly());
		arrayList.add(getMessages().bothservicesandProduct_labelonly());
		return arrayList;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		ClientCompanyPreferences preferences = context.getPreferences();
		String string = null;
		if (preferences.isSellProducts()) {
			string = getMessages().products_labelonly();
		}

		if (preferences.isSellServices()) {
			string = getMessages().services_labelonly();
		}

		if (preferences.isSellProducts() && preferences.isSellServices()) {
			string = getMessages().bothservicesandProduct_labelonly();
		}

		get(SERVICE_PRODUCTS_BOTH).setValue(string);

		if (preferences.isSellProducts()) {
			get(INVENTORY_TRACKING).setValue(preferences.isInventoryEnabled());
		}

		if (preferences.isInventoryEnabled()) {
			get(MULTIPLE_WAREHOUSES).setValue(preferences.iswareHouseEnabled());
		}

		if (preferences.isUnitsEnabled()) {
			get(ENABLE_UNITS).setValue(preferences.isUnitsEnabled());
		}
		if (preferences.getAgeingFromTransactionDateORDueDate() == 1) {
			get(AGING_DETAILS).setValue(true);
		} else {
			get(AGING_DETAILS).setValue(false);
		}

		if (preferences.isSalesOrderEnabled()) {
			get(SALES_ORDERS).setValue(preferences.isSalesOrderEnabled());
		}

		string = null;
		if (!preferences.isDoyouwantEstimates()) {
			string = getMessages().doNottrackingEstimates();
		}
		if (preferences.isDontIncludeEstimates()) {
			string = getMessages().dontWantToIncludeEstimates();
		} else if (preferences.isIncludeAcceptedEstimates()) {
			string = getMessages().includeAcceptedEstimates();
		} else if (preferences.isIncludePendingAcceptedEstimates()) {
			string = getMessages().includePendingAndAcceptedEstimates();
		}

		get(INCLUDE_ESTIMATES).setValue(string);

		get(USE_DELAYED_CHARGES)
				.setValue(preferences.isDelayedchargesEnabled());
		get(DO_YOU_Do_SHIPPING).setValue(preferences.isDoProductShipMents());
		get(ENABLE_DISCOUNTS).setValue(preferences.isTrackDiscounts());
		get(CUSTOMER_TERMINOLOGY)
				.setValue(
						getCustomerTerminologies().get(
								preferences.getReferCustomers()));
		get(INVENTORY_SCHEME).setValue(
				getInventorySchemesList().get(
						preferences.getActiveInventoryScheme() - 1));
		get(TAXITEM_TRANSACTIONS).setValue(preferences.isTaxPerDetailLine());
		return null;

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();
		String serviceProductBoth = get(SERVICE_PRODUCTS_BOTH).getValue();
		Integer servProBoth = getServiceProductBothList().indexOf(
				serviceProductBoth);
		if (servProBoth == 0) {
			preferences.setSellServices(true);
			preferences.setSellProducts(false);
		} else if (servProBoth == 1) {
			preferences.setSellProducts(true);
			preferences.setSellServices(false);
		} else {
			preferences.setSellServices(true);
			preferences.setSellProducts(true);
		}

		boolean trackquotes = get(MULTIPLE_WAREHOUSES).getValue();
		boolean trackUnits = get(ENABLE_UNITS).getValue();
		if (servProBoth != 0) {
			boolean inventoryTrack = get(INVENTORY_TRACKING).getValue();
			preferences.setInventoryEnabled(inventoryTrack);
			if (inventoryTrack) {
				preferences.setwareHouseEnabled(trackquotes);
				preferences.setUnitsEnabled(trackUnits);
			}
		}
		boolean agingdetails = get(AGING_DETAILS).getValue();
		String includeestimates = get(INCLUDE_ESTIMATES).getValue();
		Integer incluestimate = getAcceptEstimateList().indexOf(
				includeestimates);
		if (incluestimate == 0) {// setDontIncludeEstimates
			preferences.setDontIncludeEstimates(true);
			preferences.setIncludeAcceptedEstimates(false);
			preferences.setIncludePendingAcceptedEstimates(false);
			preferences.setDoyouwantEstimates(true);
		} else if (incluestimate == 1) {// setIncludeAcceptedEstimates
			preferences.setIncludeAcceptedEstimates(true);
			preferences.setDontIncludeEstimates(false);
			preferences.setIncludePendingAcceptedEstimates(false);
			preferences.setDoyouwantEstimates(true);
		} else if (incluestimate == 2) {// setIncludePendingAcceptedEstimates
			preferences.setIncludePendingAcceptedEstimates(true);
			preferences.setDontIncludeEstimates(false);
			preferences.setIncludeAcceptedEstimates(false);
			preferences.setDoyouwantEstimates(true);
		} else {
			preferences.setIncludePendingAcceptedEstimates(false);
			preferences.setDontIncludeEstimates(false);
			preferences.setIncludeAcceptedEstimates(false);
			preferences.setDoyouwantEstimates(false);
		}

		String inventoryScheme = get(INVENTORY_SCHEME).getValue();
		int scheme = getInventorySchemesList().indexOf(inventoryScheme);
		preferences.setActiveInventoryScheme(scheme);
		boolean isSalesOrderEnabled = get(SALES_ORDERS).getValue();
		boolean delayedcharges = get(USE_DELAYED_CHARGES).getValue();
		boolean doyoushipping = get(DO_YOU_Do_SHIPPING).getValue();
		if (agingdetails) {
			preferences.setAgeingFromTransactionDateORDueDate(1);
		} else {
			preferences.setAgeingFromTransactionDateORDueDate(2);
		}
		preferences.setDelayedchargesEnabled(delayedcharges);
		preferences.setDoProductShipMents(doyoushipping);
		Boolean trackDiscounts = get(ENABLE_DISCOUNTS).getValue();
		preferences.setTrackDiscounts(trackDiscounts);
		preferences.setSalesOrderEnabled(isSalesOrderEnabled);
		String customerTerm = get(CUSTOMER_TERMINOLOGY).getValue();
		preferences.setReferCustomers(getCustomerTerminologies().indexOf(
				customerTerm));

		if (trackDiscounts) {
			boolean taxitemline = get(TAXITEM_TRANSACTIONS).getValue();
			preferences.setDiscountPerDetailLine(taxitemline);
		}
		savePreferences(context, preferences);
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	private List<String> getAcceptEstimateList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(getMessages().dontWantToIncludeEstimates());
		arrayList.add(getMessages().includeAcceptedEstimates());
		arrayList.add(getMessages().includePendingAndAcceptedEstimates());
		arrayList.add(getMessages().doNottrackingEstimates());
		return arrayList;
	}

	protected List<String> getCustomerTerminologies() {
		List<String> customerTerms = new ArrayList<String>();
		customerTerms.add(getMessages().customer());
		customerTerms.add(getMessages().Client());
		customerTerms.add(getMessages().Tenant());
		customerTerms.add(getMessages().Donar());
		customerTerms.add(getMessages().Guest());
		customerTerms.add(getMessages().Member());
		customerTerms.add(getMessages().Patient());
		return customerTerms;
	}
}
