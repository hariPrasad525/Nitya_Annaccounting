package com.vimukti.accounter.mobile.requirements;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.commands.CountryRequirement;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.server.util.CountryPreferenceFactory;
import com.vimukti.accounter.web.server.util.ICountryPreferences;

public class CompanyAddressRequirement extends MultiRequirement<ClientAddress> {

	private static final String ADDRESS1 = "Address1";
	private static final String ADDRESS2 = "address2";
	private static final String CITY = "city";
	private static final String POSTAL_CODE = "Postalcode";
	private static final String COUNTRY = "country";
	private static final String STATE = "state";

	public CompanyAddressRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, true, true);
		setDefaultValue(new ClientAddress());
	}

	@Override
	protected void setDefaultValues() {
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new NameRequirement(ADDRESS1, getMessages().pleaseEnter(
				getMessages().address1()), getMessages().address1(), true, true));
		list.add(new NameRequirement(ADDRESS2, getMessages().pleaseEnter(
				getMessages().address2()), getMessages().address2(), true, true));
		list.add(new NameRequirement(CITY, getMessages().pleaseEnter(
				getMessages().city()), getMessages().city(), true, true));
		list.add(new NameRequirement(POSTAL_CODE, getMessages().pleaseEnter(
				getMessages().postalCode()), getMessages().postalCode(), true,
				true));
		CountryRequirement countryReq = new CountryRequirement(COUNTRY,
				getMessages().pleaseEnter(getMessages().country()),
				getMessages().country(), false, true,
				new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						countrySelected(value);
					}

				});
		countryReq.setEditable(canEdit());
		list.add(countryReq);

		list.add(new StringListRequirement(STATE, getMessages().pleaseSelect(
				getMessages().state()), getMessages().state(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().state());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().state());
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String[] states = getStates();
				if (states != null && states.length > 0) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				List<String> statesAsListForCountry = Arrays
						.asList(getStates());
				return statesAsListForCountry;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
	}

	private String[] getStates() {
		String country = getRequirement(COUNTRY).getValue();
		ICountryPreferences countryPreferences = CountryPreferenceFactory
				.get(country);
		if (countryPreferences != null) {
			return countryPreferences.getStates();
		}
		return null;
	}

	private void countrySelected(String value) {
		if (value == null || value.isEmpty()) {
			return;
		}
		ICountryPreferences countryPreferences = CountryPreferenceFactory
				.get(value);
		if (countryPreferences.getStates() != null
				&& countryPreferences.getStates().length > 0) {
			getRequirement(STATE).setValue(countryPreferences.getStates()[0]);
		} else {
			getRequirement(STATE).setValue("");
		}
		countrySelected(countryPreferences);
	}

	protected void countrySelected(ICountryPreferences countryPreferences) {
	}

	protected boolean canEdit() {
		return false;
	}

	@Override
	protected Result onFinish(Context context) {
		ClientAddress address = getValue();
		address.setAddress1((String) getRequirement(ADDRESS1).getValue());
		address.setStreet((String) getRequirement(ADDRESS2).getValue());
		address.setCity((String) getRequirement(CITY).getValue());
		address.setZipOrPostalCode((String) getRequirement(POSTAL_CODE)
				.getValue());
		address.setCountryOrRegion((String) getRequirement(COUNTRY).getValue());
		address.setStateOrProvinence((String) getRequirement(STATE).getValue());
		return null;
	}

	@Override
	public void setValue(Object value) {
		if (value != null) {
			ClientAddress address = (ClientAddress) value;
			getRequirement(ADDRESS1).setValue(address.getAddress1());
			getRequirement(ADDRESS2).setValue(address.getStreet());
			getRequirement(CITY).setValue(address.getCity());
			getRequirement(POSTAL_CODE).setValue(address.getZipOrPostalCode());
			getRequirement(COUNTRY).setValue(address.getCountryOrRegion());
			getRequirement(STATE).setValue(address.getStateOrProvinence());
		}
		super.setValue(value);
	}

	@Override
	protected String getDisplayValue() {
		ClientAddress address = getValue();
		return address.getAddressForMobile();
	}

}
