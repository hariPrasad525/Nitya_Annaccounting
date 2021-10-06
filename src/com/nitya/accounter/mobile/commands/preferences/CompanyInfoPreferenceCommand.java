package com.nitya.accounter.mobile.commands.preferences;

import java.util.List;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.commands.URLRequirement;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.CompanyAddressRequirement;
import com.nitya.accounter.mobile.requirements.EmailRequirement;
import com.nitya.accounter.mobile.requirements.NameRequirement;
import com.nitya.accounter.mobile.requirements.PhoneRequirement;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.server.util.CountryPreferenceFactory;
import com.nitya.accounter.web.server.util.ICountryPreferences;

public class CompanyInfoPreferenceCommand extends
		AbstractCompanyPreferencesCommand {
	private static final String COMPANY_NAME = "companyname";
	private static final String IS_DIFFERENT_NAME = "isdiffrentname";
	private static final String LEGAL_NAME = "legalname";
	private static final String TRADING_ADRESS = "tradingAdress";
	private static final String HAS_DIFF_ADDRESS = "hasDiffrentAddr";
	private static final String REGISTERED_ADDRESS = "registeredAddress";
	private static final String EMAIL = "email";
	private static final String WEBSITE = "website";
	private static final String PHONE_NUMBER = "phonenumber";
	private static final String TAX_ID = "taxid";
	private static final String IS_MULTI_CURRENCY_ENBLED = "ismultiCurrecnyEnbled";
	private static final String CHARGE_TRACK_TAX = "chargetracktax";
	private static final String TAXITEM_TRANSACTIONS = "taxitemtransactions";
	private static final String ENABLE_TRACKING_TAXPAID = "enabletracktax";
	private static final String ENABLE_TDS = "enabletds";

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(COMPANY_NAME, getMessages().pleaseEnter(
				getMessages().companyName()), getMessages().companyName(),
				true, true));

		list.add(new BooleanRequirement(IS_DIFFERENT_NAME, true) {

			@Override
			protected String getTrueString() {

				return getMessages().getDifferentLegalName();
			}

			@Override
			protected String getFalseString() {
				return getMessages().getNoDifferentLegalName();
			}
		});

		list.add(new NameRequirement(LEGAL_NAME, getMessages().pleaseEnter(
				getMessages().legalCompanyName()), getMessages()
				.legalCompanyName(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean difname = get(IS_DIFFERENT_NAME).getValue();
				if (difname) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new CompanyAddressRequirement(TRADING_ADRESS, getMessages()
				.tradingAddress(), getMessages().tradingAddress()));

		list.add(new BooleanRequirement(HAS_DIFF_ADDRESS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().getDifferentLegalName();
			}

			@Override
			protected String getFalseString() {
				return getMessages().dontHaveAnotherAddress();
			}
		});

		list.add(new CompanyAddressRequirement(REGISTERED_ADDRESS,
				getMessages().registeredAddress(), getMessages()
						.registeredAddress()) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Boolean hasDifftAdr = get(HAS_DIFF_ADDRESS).getValue();
				if (hasDifftAdr) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new URLRequirement(WEBSITE, getMessages().pleaseEnter(
				getMessages().webSite()), getMessages().webSite(), true, true));

		list.add(new PhoneRequirement(PHONE_NUMBER, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));

		list.add(new StringRequirement(TAX_ID, getMessages().pleaseEnter(
				getMessages().taxId()), getMessages().taxId(), true, true));

		list.add(new BooleanRequirement(IS_MULTI_CURRENCY_ENBLED, true) {

			@Override
			protected String getTrueString() {
				return getMessages().multiCurrencyEnabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().multiCurrencyNotEnabled();
			}
		});

		list.add(new BooleanRequirement(CHARGE_TRACK_TAX, true) {

			@Override
			protected String getTrueString() {
				return getMessages().chargeOrTrackTax();
			}

			@Override
			protected String getFalseString() {
				return getMessages().donotChargeOrTrackTax();
			}
		});

		list.add(new BooleanRequirement(TAXITEM_TRANSACTIONS, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean ischargingtax = get(CHARGE_TRACK_TAX).getValue();
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

		list.add(new BooleanRequirement(ENABLE_TRACKING_TAXPAID, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean ischargingtax = get(CHARGE_TRACK_TAX).getValue();
				if (ischargingtax) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().trackingTaxPaid();
			}

			@Override
			protected String getFalseString() {
				return getMessages().donotTrackingTaxPaid();
			}
		});

		list.add(new BooleanRequirement(ENABLE_TDS, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean ischargingtax = get(CHARGE_TRACK_TAX).getValue();
				if (ischargingtax) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().enabledTDS();
			}

			@Override
			protected String getFalseString() {
				return getMessages().disabledTDS();
			}
		});
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String country = context.getUser().getClient().getCountry();
		ClientCompanyPreferences preferences = context.getPreferences();
		get(COMPANY_NAME).setValue(preferences.getTradingName());
		get(IS_DIFFERENT_NAME).setValue(preferences.isShowLegalName());
		get(LEGAL_NAME).setValue(preferences.getLegalName());
		get(EMAIL).setValue(preferences.getCompanyEmail());
		get(WEBSITE).setValue(preferences.getWebSite());
		get(PHONE_NUMBER).setValue(preferences.getPhone());
		get(TAX_ID).setValue(preferences.getTaxId());
		get(IS_MULTI_CURRENCY_ENBLED).setValue(
				preferences.isEnableMultiCurrency());
		get(CHARGE_TRACK_TAX).setValue(preferences.isTrackTax());
		get(TAXITEM_TRANSACTIONS).setValue(preferences.isTaxPerDetailLine());
		get(ENABLE_TRACKING_TAXPAID).setValue(preferences.isTrackPaidTax());
		get(ENABLE_TDS).setValue(preferences.isTDSEnabled());
		get(HAS_DIFF_ADDRESS).setValue(preferences.isShowRegisteredAddress());
		ClientAddress tradingAddress = preferences.getTradingAddress();
		if (tradingAddress.getCountryOrRegion() == null
				|| tradingAddress.getCountryOrRegion().isEmpty()) {
			tradingAddress.setCountryOrRegion(country);
			ICountryPreferences countryPreferences = CountryPreferenceFactory
					.get(country);
			tradingAddress
					.setStateOrProvinence(countryPreferences.getStates()[0]);
		}
		get(TRADING_ADRESS).setValue(tradingAddress);
		Address registeredAddress = context.getCompany().getRegisteredAddress();
		if (registeredAddress.getCountryOrRegion() == null
				|| registeredAddress.getCountryOrRegion().isEmpty()) {
			registeredAddress.setCountryOrRegion(country);
			ICountryPreferences countryPreferences = CountryPreferenceFactory
					.get(country);
			registeredAddress.setStateOrProvinence(countryPreferences
					.getStates()[0]);
		}
		get(REGISTERED_ADDRESS).setValue(toClientAddress(registeredAddress));
		return null;

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();
		String name = get(COMPANY_NAME).getValue();
		boolean isdiffname = get(IS_DIFFERENT_NAME).getValue();
		String legelname = get(LEGAL_NAME).getValue();
		String email = get(EMAIL).getValue();
		String website = get(WEBSITE).getValue();
		String phno = get(PHONE_NUMBER).getValue();
		Boolean ismultiCurrencyEnabled = get(IS_MULTI_CURRENCY_ENBLED)
				.getValue();
		String taxid = get(TAX_ID).getValue();
		preferences.setTradingName(name);
		preferences.setShowLegalName(isdiffname);
		preferences.setLegalName(legelname);
		preferences.setPhone(phno);
		preferences.setCompanyEmail(email);
		preferences.setWebSite(website);
		preferences.setTaxId(taxid);
		preferences.setEnableMultiCurrency(ismultiCurrencyEnabled);
		boolean tax = get(CHARGE_TRACK_TAX).getValue();
		if (tax) {
			boolean taxitemline = get(TAXITEM_TRANSACTIONS).getValue();
			boolean tracktaxpaid = get(ENABLE_TRACKING_TAXPAID).getValue();
			boolean enabletds = get(ENABLE_TDS).getValue();
			preferences.setTaxPerDetailLine(taxitemline);
			preferences.setTrackPaidTax(tracktaxpaid);
			preferences.setTDSEnabled(enabletds);
		}
		boolean hasDiffAddr = get(HAS_DIFF_ADDRESS).getValue();
		preferences.setShowRegisteredAddress(hasDiffAddr);
		ClientAddress tradingAddress = get(TRADING_ADRESS).getValue();
		preferences.setTradingAddress(tradingAddress);
		preferences.setTaxTrack(tax);
		savePreferences(context, preferences);
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	protected ClientAddress getRegisteredAddress() {
		return get(REGISTERED_ADDRESS).getValue();
	}
}
