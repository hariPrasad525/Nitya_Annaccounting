package com.nitya.accounter.mobile.requirements;

import java.util.List;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.web.client.core.ClientAddress;

public class AddressRequirement extends MultiRequirement<ClientAddress> {
	private static final String ADDRESS1 = "address1";
	private static final String CITY = "city";
	private static final String STREET = "street";
	private static final String STATE = "stateOrProvinence";
	private static final String COUNTRY = "countryOrRegion";

	public AddressRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		setDefaultValue(new ClientAddress());
	}

	@Override
	protected void setDefaultValues() {
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new StringRequirement(ADDRESS1, getMessages().pleaseEnter(
				getMessages().address1()), getMessages().address1(), true, true));
		list.add(new StringRequirement(CITY, getMessages().pleaseEnter(
				getMessages().city()), getMessages().city(), true, true));
		list.add(new StringRequirement(STREET, getMessages().pleaseEnter(
				getMessages().streetName()), getMessages().streetName(), true,
				true));
		list.add(new StringRequirement(STATE, getMessages().pleaseEnter(
				getMessages().state()), getMessages().state(), true, true));
		list.add(new StringRequirement(COUNTRY, getMessages().pleaseEnter(
				getMessages().country()), getMessages().country(), true, true));
	}

	@Override
	protected Result onFinish(Context context) {
		ClientAddress address = getValue();
		address.setAddress1((String) getRequirement(ADDRESS1).getValue());
		address.setCity((String) getRequirement(CITY).getValue());
		address.setStreet((String) getRequirement(STREET).getValue());
		address.setStateOrProvinence((String) getRequirement(STATE).getValue());
		address.setCountryOrRegion((String) getRequirement(COUNTRY).getValue());
		return null;
	}

	@Override
	protected String getDisplayValue() {
		ClientAddress address = getValue();
		return address.getAddressForMobile();
	}

}
