package com.nitya.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.CountryPreferences;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.CoreUtils;
import com.nitya.accounter.web.client.ui.Header;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;

public class CompanyInfoOption extends AbstractPreferenceOption {

	TextItem companyNameTextBox;

	TextItem legalNameTextBox;

	LabelItem tradingAddressTitle;

	LabelItem tradingAddressDescription;

	TextItem tAddress1TextBox;

	TextItem tAddress2TextBox;

	TextItem tCityTextBox;

	TextItem tPostalCodeTextBox;

	SelectCombo tStateCombo;

	SelectCombo tCountryCombo;

	LabelItem registeredAddressTitle;

	LabelItem registeredAddressDescription;

	LabelItem rAddress1Label;

	TextItem rAddress1TextBox;

	TextItem rAddress2TextBox;

	TextItem rCityTextBox;

	TextItem rPostalCodeTextbox;

	SelectCombo rCountryCombo;

	SelectCombo rStateCombo;

	// Email
	TextItem companyEmailTextBox;

	CheckboxItem isShowRegisteredAddressCheckBox;

	TextItem companyWebsiteTextBox;

	// Name

	CheckboxItem isShowLegalName;

	// Phone Number

	TextItem companyPhoneNumberTextBox;

	SelectCombo fiscalStartsCombo;

	SelectCombo fiscalStartsList;
	private List<String> monthsList;
	String[] monthNames;
	protected CountryPreferences countryPreferences;

	StyledPanel mainPanel;

	StyledPanel registeredAddressSubPanel;

	StyledPanel tradingAddressSubPanel;

	StyledPanel tradingAddressPanel;

	StyledPanel registeredAddressPanel;

	interface CompanyInfoOptionUiBinder extends
			UiBinder<Widget, CompanyInfoOption> {
	}

	public CompanyInfoOption() {
		super("companyInfoPanel");
		createControls();
		initData();
	}

	public void initData() {
		ClientAddress tradingAddress = getCompany().getTradingAddress();
		if (tradingAddress != null) {
			tAddress1TextBox.setValue(tradingAddress.getAddress1());
			tAddress2TextBox.setValue(tradingAddress.getStreet());
			tCityTextBox.setValue(tradingAddress.getCity());
			tPostalCodeTextBox.setValue(tradingAddress.getZipOrPostalCode());
		}
		ClientAddress registeredAddress = getCompany().getRegisteredAddress();
		if (registeredAddress == null) {
			registeredAddress = tradingAddress;
		}
		if (registeredAddress == null) {
			return;
		}
		this.rAddress1TextBox.setValue(registeredAddress.getAddress1());
		this.rAddress2TextBox.setValue(registeredAddress.getStreet());
		this.rCityTextBox.setValue(registeredAddress.getCity());
		this.rPostalCodeTextbox
				.setValue(registeredAddress.getZipOrPostalCode());
		this.isShowRegisteredAddressCheckBox.setValue(getCompany()
				.isShowRegisteredAddress());
		registeredAddressPanel.setVisible(getCompany()
				.isShowRegisteredAddress());
		// Website
		companyWebsiteTextBox.setValue(getCompany().getWebSite());
		// Email
		companyEmailTextBox.setValue(getCompanyPreferences().getCompanyEmail());
		// Name
		isShowLegalName.setValue(getCompanyPreferences().isShowLegalName());
		legalNameTextBox.setValue(getCompanyPreferences().getLegalName());
		legalNameTextBox.setVisible(getCompanyPreferences().isShowLegalName());
		companyNameTextBox.setValue(getCompany().getDisplayName());
		// Phone Number
		companyPhoneNumberTextBox.setValue(getCompany().getPhone());
		fiscalStartsCombo.setSelectedItem(getCompanyPreferences()
				.getFiscalYearFirstMonth());
		tCountryChanged();
	}

	public void createControls() {
		List<String> countriesList = CoreUtils.getCountriesAsList();

		companyNameTextBox = new TextItem(messages.companyName(), "header");

		isShowLegalName = new CheckboxItem(messages.getDifferentLegalName(),
				"isShowLegalName");

		legalNameTextBox = new TextItem(messages.legalName(), "header");

		mainPanel = new StyledPanel("CompanyInfoOption");
		mainPanel.add(companyNameTextBox);
		mainPanel.add(isShowLegalName);
		mainPanel.add(legalNameTextBox);

		isShowLegalName.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				legalNameTextBox.setVisible(isShowLegalName.getValue());
			}
		});

		tradingAddressTitle = new LabelItem(messages.tradingAddress(), "header");

		tradingAddressDescription = new LabelItem(
				messages.tradingAddressDescription(), "organisation_comment");
		tradingAddressPanel = new StyledPanel("address_panel");
		tradingAddressPanel.add(tradingAddressTitle);
		tradingAddressPanel.add(tradingAddressDescription);

		mainPanel.add(tradingAddressPanel);

		tAddress1TextBox = new TextItem(messages.address1(), "tAddress1TextBox");

		tAddress2TextBox = new TextItem(messages.address2(), "tAddress2TextBox");

		tCityTextBox = new TextItem(messages.city(), "tCityTextBox");

		tPostalCodeTextBox = new TextItem(messages.postalCode(),
				"tPostalCodeTextBox");

		tCountryCombo = new SelectCombo(messages.country());
		tCountryCombo.initCombo(countriesList);
		tCountryCombo.setEnabled(false);
		tStateCombo = new SelectCombo(messages.state());

		tradingAddressSubPanel = new StyledPanel("tradingAddressSubPanel");
		tradingAddressSubPanel.add(tAddress1TextBox);
		tradingAddressSubPanel.add(tAddress2TextBox);
		tradingAddressSubPanel.add(tCityTextBox);
		tradingAddressSubPanel.add(tPostalCodeTextBox);
		tradingAddressSubPanel.add(tStateCombo);
		tradingAddressSubPanel.add(tCountryCombo);

		mainPanel.add(tradingAddressSubPanel);
		isShowRegisteredAddressCheckBox = new CheckboxItem(
				messages.registeredAddressComment(),
				"isShowRegisteredAddressCheckBox");

		tradingAddressSubPanel.add(isShowRegisteredAddressCheckBox);

		registeredAddressTitle = new LabelItem(messages.registeredAddress(),
				"header");

		registeredAddressDescription = new LabelItem(
				messages.registeredAddresDescription(),
				"registeredAddressDescription");
		registeredAddressPanel = new StyledPanel("registeredAddressPanel");

		registeredAddressPanel.add(registeredAddressTitle);
		registeredAddressPanel.add(registeredAddressDescription);

		mainPanel.add(registeredAddressPanel);

		rAddress1TextBox = new TextItem(messages.address1(), "tAddress1TextBox");

		rAddress2TextBox = new TextItem(messages.address2(), "tAddress2TextBox");

		rCityTextBox = new TextItem(messages.city(), "tCityTextBox");

		rPostalCodeTextbox = new TextItem(messages.postalCode(),
				"rPostalCodeTextbox");
		rStateCombo = new SelectCombo(messages.state());

		rCountryCombo = new SelectCombo(messages.country());
		rCountryCombo.initCombo(countriesList);
		rCountryCombo.setEnabled(false);

		registeredAddressSubPanel = new StyledPanel("registeredAddressSubPanel");
		registeredAddressSubPanel.add(rAddress1TextBox);
		registeredAddressSubPanel.add(rAddress2TextBox);
		registeredAddressSubPanel.add(rCityTextBox);
		registeredAddressSubPanel.add(rPostalCodeTextbox);
		registeredAddressSubPanel.add(rStateCombo);
		registeredAddressSubPanel.add(rCountryCombo);

		isShowRegisteredAddressCheckBox
				.addChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						registeredAddressPanel
								.setVisible(isShowRegisteredAddressCheckBox
										.getValue());
					}
				});

		registeredAddressPanel.add(registeredAddressSubPanel);

		if (getCompany().getTradingAddress() != null
				&& getCompany().getTradingAddress().getCountryOrRegion() != null) {
			tCountryCombo.setComboItem(getCompany().getTradingAddress()
					.getCountryOrRegion());
		}

		if (getCompany().getRegisteredAddress() != null
				&& getCompany().getRegisteredAddress().getCountryOrRegion() != null) {
			rCountryCombo.setComboItem(getCompany().getRegisteredAddress()
					.getCountryOrRegion());
		}

		// website
		companyWebsiteTextBox = new TextItem(messages.webSite(),
				"companyWebsiteTextBox");
		// Email
		companyEmailTextBox = new TextItem(messages.email(),
				"companyEmailTextBox");
		companyEmailTextBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				String email = companyEmailTextBox.getValue();
				if (email != null && !email.isEmpty()
						&& !UIUtils.isValidEmail(email)) {
					companyEmailTextBox.setValue("");
					Accounter.showError(messages.invalidEmail());
				}
			}
		});

		mainPanel.add(companyEmailTextBox);

		// Phone Number
		companyPhoneNumberTextBox = new TextItem(messages.phoneNumber(),
				"companyPhoneNumberTextBox");
		mainPanel.add(companyPhoneNumberTextBox);
		// Fiscal year
		fiscalStartsCombo = new SelectCombo(
				messages.selectFirstMonthOfFiscalYear());
		fiscalStartsCombo.addStyleName("header");
		monthNames = new String[] { DayAndMonthUtil.january(),
				DayAndMonthUtil.february(), DayAndMonthUtil.march(),
				DayAndMonthUtil.april(), DayAndMonthUtil.may_full(),
				DayAndMonthUtil.june(), DayAndMonthUtil.july(),
				DayAndMonthUtil.august(), DayAndMonthUtil.september(),
				DayAndMonthUtil.october(), DayAndMonthUtil.november(),
				DayAndMonthUtil.december() };
		monthsList = new ArrayList<String>();
		// fiscalStartsList = null;
		for (int i = 0; i < monthNames.length; i++) {
			monthsList.add(monthNames[i]);
			fiscalStartsCombo.addItem(monthNames[i]);
		}

		mainPanel.add(fiscalStartsCombo);
		add(mainPanel);

	}

	Map<String, TextItem> itemsField = new HashMap<String, TextItem>();

	private void addFields(Map<String, String> fields) {
		itemsField.clear();
		DynamicForm form = new DynamicForm("form");
		for (String key : fields.keySet()) {
			String value = fields.get(key);
			TextItem item = new TextItem(key, "item");
			item.setValue(value);
			item.setTitle(key);
			form.add(item);
			itemsField.put(key, item);
		}
		mainPanel.add(form);
		form.addStyleName("company_fields");
	}

	private void tCountryChanged() {
		int selectedCountry = tCountryCombo.getSelectedIndex();
		if (selectedCountry < 0) {
			return;
		}
		String countryName = tCountryCombo.getSelectedValue();

		Accounter.createCompanyInitializationService().getCountryPreferences(
				countryName, new AsyncCallback<CountryPreferences>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(CountryPreferences result) {
						countryPreferences = result;
						updateRCountry();
					}
				});

	}

	protected void updateRCountry() {
		Map<String, String> fields = new HashMap<String, String>();
		for (String fieldName : countryPreferences.getCompanyFields()) {
			fields.put(fieldName, "");
		}
		fields.putAll(getCompany().getPreferences().getCompanyFields());
		addFields(fields);

		String[] states = countryPreferences.getStates();
		if (countryPreferences.getStates() != null) {
			states = countryPreferences.getStates();
		} else {
			states = new String[] { "" };
		}
		List<String> statesList = Arrays.asList(states);
		tStateCombo.clear();
		for (String state : statesList) {
			tStateCombo.addItem(state);
			rStateCombo.addItem(state);
		}
		ClientAddress tradingAddress = getCompanyPreferences()
				.getTradingAddress();
		if (tradingAddress != null
				&& tradingAddress.getStateOrProvinence() != null) {
			if (statesList.contains(tradingAddress.getStateOrProvinence())) {
				tStateCombo.setSelectedItem(statesList.indexOf(tradingAddress
						.getStateOrProvinence()));
			}
		}

		ClientAddress address = getCompany().getRegisteredAddress();
		if (address != null && address.getStateOrProvinence() != null) {
			if (statesList.contains(address.getStateOrProvinence())) {
				rStateCombo.setSelectedItem(statesList.indexOf(address
						.getStateOrProvinence()));
			}
		}
	}

	@Override
	public String getTitle() {
		return messages.nameAddress();
	}

	@Override
	public void onSave() {
		ClientAddress tAddress = getCompany().getPreferences()
				.getTradingAddress();
		if (tAddress == null) {
			tAddress = new ClientAddress();
		}
		tAddress.setAddress1(tAddress1TextBox.getValue());
		tAddress.setStreet(tAddress2TextBox.getValue());
		tAddress.setCity(tCityTextBox.getValue());
		// if (tStateCombo.getSelectedIndex() > 0) {
		tAddress.setStateOrProvinence(tStateCombo.getSelectedValue());
		// }
		tAddress.setZipOrPostalCode(tPostalCodeTextBox.getValue());
		if (tCountryCombo.getSelectedIndex() > 0) {
			tAddress.setCountryOrRegion(tCountryCombo.getSelectedValue());
		}
		ClientAddress rAddress = getCompany().getRegisteredAddress();
		if (rAddress == null) {
			rAddress = new ClientAddress();
		}
		rAddress.setAddress1(rAddress1TextBox.getValue());
		rAddress.setStreet(rAddress2TextBox.getValue());
		rAddress.setCity(rCityTextBox.getValue());
		rAddress.setZipOrPostalCode(rPostalCodeTextbox.getValue());

		this.rPostalCodeTextbox.setValue(rAddress.getZipOrPostalCode());
		if (rStateCombo.getSelectedIndex() > 0) {
			rAddress.setStateOrProvinence(rStateCombo.getSelectedValue());
		}
		if (rCountryCombo.getSelectedIndex() > 0) {
			rAddress.setCountryOrRegion(rCountryCombo.getSelectedValue());
		}
		getCompany().setRegisteredAddress(rAddress);
		getCompany().setShowRegisteredAddress(
				isShowRegisteredAddressCheckBox.getValue());
		// Website
		getCompany().setWebSite(companyWebsiteTextBox.getValue());
		// Email
		getCompanyPreferences().setCompanyEmail(companyEmailTextBox.getValue());
		// Name
		getCompany().setLegalName(legalNameTextBox.getValue());
		getCompany().setTradingName(companyNameTextBox.getValue());
		getCompanyPreferences().setShowLegalName(isShowLegalName.getValue());
		Header.companyNameLabel.setText(companyNameTextBox.getValue());
		// Phone Number
		getCompany().setPhone(companyPhoneNumberTextBox.getValue());

		HashMap<String, String> companyFields = new HashMap<String, String>();
		for (String fieldName : itemsField.keySet()) {
			companyFields.put(fieldName, itemsField.get(fieldName).getValue());
		}
		getCompany().getPreferences().setCompanyFields(companyFields);
		getCompany().getPreferences().setFiscalYearFirstMonth(
				fiscalStartsCombo.getSelectedIndex());

	}

	@Override
	public boolean isValidate() {
		if (companyNameTextBox.getValue().length() == 0) {
			Accounter.showError(messages.pleaseEnter(messages.companyName()));
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String getAnchor() {
		return messages.nameAddress();
	}
}
