package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.server.FinanceTool;

public class MergeVendorsCommand extends AbstractCommand {

	private static final String VENDOR_FROM = "vendorFrom";
	private static final String VENDOR_TO = "vendorTo";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().merging(Global.get().Vendors());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToMerge(Global.get().Vendors());
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().mergingCompleted(Global.get().Vendors());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new VendorRequirement(VENDOR_FROM, getMessages().payeeFrom(
				Global.get().vendor()), Global.get().Vendor(), false, true,
				null) {

			@Override
			protected List<Vendor> getLists(Context context) {
				return getVendors();
			}

			@Override
			public void setValue(Object value) {
				Vendor vendorTo = get(VENDOR_TO).getValue();
				Vendor vendorFrom = (Vendor) value;
				String checkDifferentVendors = null;
				if (vendorFrom != null && vendorTo != null) {
					checkDifferentVendors = checkDifferentVendors(vendorFrom,
							vendorTo);
				}
				if (checkDifferentVendors != null) {
					addFirstMessage(checkDifferentVendors);
					super.setValue(null);
					return;
				}
				super.setValue(value);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().payeeFrom(Global.get().Vendor()));
			}

			@Override
			protected String getSetMessage() {
				Vendor value = getValue();
				if (value != null) {
					return getMessages().selectedAs(value.getName(),
							getMessages().payeeFrom(Global.get().Vendor()));
				}
				return null;
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new VendorRequirement(VENDOR_TO, getMessages().payeeTo(
				Global.get().vendor()), Global.get().Vendor(), false, true,
				null) {

			@Override
			protected List<Vendor> getLists(Context context) {
				return getVendors();
			}

			@Override
			public void setValue(Object value) {
				Vendor customerFrom = get(VENDOR_FROM).getValue();
				Vendor customerTo = (Vendor) value;
				String checkDifferentVendors = null;
				if (customerFrom != null && customerTo != null) {
					checkDifferentVendors = checkDifferentVendors(customerFrom,
							customerTo);
				}
				if (checkDifferentVendors != null) {
					addFirstMessage(checkDifferentVendors);
					super.setValue(null);
					return;
				}
				super.setValue(value);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().payeeTo(Global.get().Vendor()));
			}

			@Override
			protected String getSetMessage() {
				Vendor value = getValue();
				if (value != null) {
					return getMessages().selectedAs(value.getName(),
							getMessages().payeeTo(Global.get().Vendor()));
				}
				return null;
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().startsWith(name);
			}
		});

	}

	protected String checkDifferentVendors(Vendor customerFrom,
			Vendor customerTo) {
		if (customerFrom.getID() == customerTo.getID()) {
			return getMessages().notMove(Global.get().vendors());
		}
		if (getPreferences().isEnableMultiCurrency()) {
			long from = customerFrom.getCurrency().getID();
			long to = customerTo.getCurrency().getID();
			if (from != to) {
				return getMessages().currenciesOfTheBothCustomersMustBeSame(
						Global.get().vendors());
			}
		}
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientConvertUtil clientConvertUtil = new ClientConvertUtil();
		Vendor vendorFrom = get(VENDOR_FROM).getValue();
		Vendor vendorTo = get(VENDOR_TO).getValue();

		try {
			ClientVendor clientFrom = clientConvertUtil.toClientObject(
					vendorFrom, ClientVendor.class);
			ClientVendor clientTo = clientConvertUtil.toClientObject(
					vendorTo, ClientVendor.class);

			new FinanceTool().getVendorManager().mergeVendor(clientFrom,
					clientTo, getCompany().getID());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.onCompleteProcess(context);
	}
}
