/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.CountryPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * @author Administrator
 * 
 */
public class SetupCompanyInfoPage extends AbstractSetupPage {

	private static SetupCompanyInfoPageUiBinder uiBinder = GWT
			.create(SetupCompanyInfoPageUiBinder.class);
	@UiField
	FlowPanel viewPanel;
	@UiField
	FlowPanel companyInfoField;
	@UiField
	TextBox companyName;
	@UiField
	TextBox legalName;
	@UiField
	TextBox taxId;
	@UiField
	TextBox streetAddress1;
	@UiField
	TextBox streetAdress2;
	@UiField
	ListBox country;
	@UiField
	TextBox phone;
	@UiField
	TextBox fax;
	@UiField
	TextBox emailAddress;
	@UiField
	TextBox webSite;
	@UiField
	TextBox zip;
	@UiField
	ListBox stateListBox;
	@UiField
	TextBox cityTextBox;
	@UiField
	Label displayNameLabel;
	@UiField
	Label legalNameLabel;
	@UiField
	Label taxIDLabel;
	@UiField
	Label streetAddress2Label;
	@UiField
	Label streetAdreess1Label;
	@UiField
	Label cityLabel;
	@UiField
	Label stateLabel;
	@UiField
	Label zipLabel;
	@UiField
	Label countryLabel;
	@UiField
	Label phoneLabel;
	@UiField
	Label faxLabel;
	@UiField
	Label emailAdressLabel;
	@UiField
	Label webSiteLabel;
	@UiField
	Label useFormat;
	@UiField
	Label headerLabel;
	@UiField
	Label timezone;
	@UiField
	ListBox timezoneslistbox;

	private ClientAddress address;
	private List<String> countries, statesList, timezones;
	private boolean isValidCompanyname;

	interface SetupCompanyInfoPageUiBinder extends
			UiBinder<Widget, SetupCompanyInfoPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public SetupCompanyInfoPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.enterYourCompanyInfo());

		// if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		taxIDLabel.setText(messages.taxId());
		// } else if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		// taxIDLabel.setText(messages.vatNo());
		// } else if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_INDIA) {
		// taxIDLabel.setText(messages.panNumber(
		// Global.get().Account()));
		// }

		displayNameLabel.setText(messages.companyName());
		legalNameLabel.setText(messages.legalName());
		streetAddress2Label.setText(messages.streetAddress2());
		streetAdreess1Label.setText(messages.streetAddress1());
		cityLabel.setText(messages.city());
		stateLabel.setText(messages.state());
		zipLabel.setText(messages.zipCode());
		countryLabel.setText(messages.country());
		phoneLabel.setText(messages.phone());
		phone.setTitle(messages.phoneNumberOf(messages.company()));
		faxLabel.setText(messages.fax());
		emailAdressLabel.setText(messages.email());
		webSiteLabel.setText(messages.webSite());
		useFormat.setText("");
		timezone.setText(messages.timezone());

		countries = CoreUtils.getCountriesAsList();
		for (int i = 0; i < countries.size(); i++) {
			country.addItem(countries.get(i));
		}

		country.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (stateListBox.getItemCount() != 0) {
					for (int i = 0; i < stateListBox.getItemCount(); i++) {
						stateListBox.removeItem(i);
					}
				}
				countryChanged();
			}
		});

		this.timezones = CoreUtils.getTimeZonesAsList();
		for (String tz : timezones) {
			timezoneslistbox.addItem(tz);
		}

		String defalutTzOffset = getDefaultTzOffsetStr();
		for (String tz : timezones) {
			if (tz.startsWith(defalutTzOffset)) {
				timezoneslistbox.setSelectedIndex(timezones.indexOf(tz));
				break;
			}
		}

		companyFieldsPanel = new StyledPanel("companyFieldsPanel");
		viewPanel.add(companyFieldsPanel);

		countryChanged();
		if (preferences.getTradingName() == null
				|| preferences.getTradingName().length() == 0) {
			companyName.addBlurHandler(new BlurHandler() {

				@Override
				public void onBlur(BlurEvent event) {
					verifyCompanyName(companyName.getText());
				}
			});
		} else {
			isValidCompanyname = true;
			companyName.setEnabled(false);
		}
	}

	/**
	 * 
	 */
	protected void countryChanged() {
		int selectedCountry = country.getSelectedIndex();
		if (selectedCountry < 0) {
			return;
		}
		final String countryName = country.getItemText(selectedCountry);
		setCountry(countryName);

		Accounter.createCompanyInitializationService().getCountryPreferences(
				countryName, new AsyncCallback<CountryPreferences>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(CountryPreferences result) {
						if (result != null) {
							update(result);
						} else {
							System.err.println(countryName);
						}
					}
				});

	}

	protected void update(final CountryPreferences countryPreferences) {
		if (countryPreferences.getStates() != null) {
			setStates(countryPreferences.getStates());
		} else {
			setStates(new String[] { "" });
		}
		List<ClientCurrency> currenciesList = CoreUtils
				.getCurrencies(new ArrayList<ClientCurrency>());
		for (int i = 0; i < currenciesList.size(); i++) {
			if (countryPreferences.getPreferredCurrency().trim()
					.equals(currenciesList.get(i).getFormalName())) {
				preferences.setPrimaryCurrency(currenciesList.get(i));
			}
		}
		List<String> monthNames = CoreUtils.getMonthNames();
		stateChanged(countryPreferences);
		preferences
				.setFiscalYearFirstMonth(monthNames.indexOf(countryPreferences
						.getDefaultFiscalYearStartingMonth()));
		stateListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				stateChanged(countryPreferences);
			}
		});
	}

	Map<String, TextItem> itemsField = new HashMap<String, TextItem>();

	StyledPanel companyFieldsPanel;

	private void addFields(Map<String, String> companyFields) {
		itemsField.clear();
		companyFieldsPanel.clear();
		DynamicForm form = new DynamicForm("form");
		for (String key : companyFields.keySet()) {
			String value = companyFields.get(key);
			TextItem item = new TextItem(key, "item");
			item.setValue(value);
			form.add(item);
			itemsField.put(key, item);
		}
		companyFieldsPanel.add(form);
	}

	private void stateChanged(CountryPreferences countryPreferences) {
		int selectedIndex = stateListBox.getSelectedIndex();
		if (selectedIndex < 0) {
			return;
		}
		String selectedState = stateListBox.getItemText(stateListBox
				.getSelectedIndex());
		int selectedTimeZone = timezones.indexOf(countryPreferences
				.getDefaultTimeZone(selectedState));
		timezoneslistbox.setSelectedIndex(selectedTimeZone);
	}

	private String getDefaultTzOffsetStr() {
		Date date = new Date();
		DateTimeFormat tzFormat = DateTimeFormat.getFormat("z");
		return tzFormat.format(date);
	}

	private void setStates(String[] states) {
		statesList = new ArrayList<String>();
		stateListBox.clear();
		for (int i = 0; i < states.length; i++) {
			statesList.add(states[i]);
			stateListBox.addItem(states[i]);
		}
	}

	public void onLoad() {

		if (preferences != null) {
			companyName.setValue(preferences.getTradingName());
			legalName.setValue(preferences.getLegalName());
			this.taxId.setValue(preferences.getTaxId());
			this.fax.setValue(preferences.getFax());
			this.phone.setValue(preferences.getPhone());
			this.webSite.setValue(preferences.getWebSite());
			this.emailAddress.setValue(preferences.getCompanyEmail());
			address = preferences.getTradingAddress();
			if (address != null) {
				this.streetAddress1.setValue(address.getAddress1());
				this.streetAdress2.setValue(address.getStreet());
				this.cityTextBox.setValue(address.getCity());

				if (address.getCountryOrRegion() != ""
						&& address.getCountryOrRegion() != null
						&& address.getCountryOrRegion().length() != 0) {
					this.country.setSelectedIndex(countries.indexOf(address
							.getCountryOrRegion()));
					countryChanged();
				}

				if (address.getStateOrProvinence() != ""
						&& address.getStateOrProvinence() != null
						&& address.getStateOrProvinence().length() != 0) {
					this.stateListBox.setSelectedIndex(statesList
							.indexOf(address.getStateOrProvinence()));
				}

				if (preferences.getTimezone() != ""
						&& preferences.getTimezone() != null) {
					this.timezoneslistbox.setSelectedIndex(timezones
							.indexOf(preferences.getTimezone()));
				}
			}
		}
	}

	@Override
	public void onSave() {

		address = new ClientAddress();
		preferences.setTradingName(companyName.getValue().toString());

		if (legalName.getValue().toString().trim() != null) {
			preferences.setShowLegalName(true);
		} else {
			preferences.setShowLegalName(false);
		}
		preferences.setLegalName(legalName.getValue().toString());

		preferences.setPhone(phone.getValue().toString());
		preferences.setCompanyEmail(emailAddress.getValue().toString());
		preferences.setTaxId(taxId.getValue().toString());
		preferences.setFax(fax.getValue().toString());
		preferences.setWebSite(webSite.getValue().toString());
		address.setAddress1(streetAddress1.getValue());
		address.setStreet(streetAdress2.getValue());
		address.setCity(cityTextBox.getValue());
		address.setZipOrPostalCode(zip.getValue());
		if (stateListBox.getSelectedIndex() != -1) {
			address.setStateOrProvinence(statesList.get(stateListBox
					.getSelectedIndex()));
		}
		if (country.getSelectedIndex() != -1)
			address.setCountryOrRegion(countries.get(country.getSelectedIndex()));
		preferences.setTradingAddress(address);

		if (timezoneslistbox.getSelectedIndex() != -1)
			preferences.setTimezone(timezones.get(timezoneslistbox
					.getSelectedIndex()));
		HashMap<String, String> companyFields = new HashMap<String, String>();
		for (String fieldName : itemsField.keySet()) {
			companyFields.put(fieldName, itemsField.get(fieldName).getValue());
		}
		preferences.setCompanyFields(companyFields);
		preferences.setPayrollOnly(isPayrollSetup);
	}

	// private String getTimezoneId(String selectedTimezone){
	// return selectedTimezone.substring(selectedTimezone.indexOf(' ')+1);
	// }

	@Override
	public boolean canShow() {
		return true;
	}

	public static boolean checkIfNotNumber(String in) {
		try {
			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return true;
		}
		return false;
	}

	private void verifyCompanyName(String companyName) {
		if (companyName == null || companyName.isEmpty()) {
			return;
		}
		companyName = companyName.trim();
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {
				isValidCompanyname = false;
				Accounter.showError(messages.companyNameAlreadyExits());
				SetupCompanyInfoPage.this.companyName.setText("");
			}

			@Override
			public void onResultSuccess(Boolean isExists) {
				if (isExists) {
					onException(null);
				} else {
					isValidCompanyname = true;
				}
			}
		};
		Accounter.createCompanyInitializationService().isCompanyNameExists(
				companyName, callback);
	}

	@Override
	protected boolean validate() {
		String companyName = this.companyName.getText();
		if (companyName != null && !companyName.isEmpty()) {
			return isValidCompanyname;
		} else {
			Accounter
					.showError(messages.pleaseEnter(displayNameLabel.getText()));
			return false;
		}

	}

	@Override
	public String getViewName() {
		return messages.comapnyInfo();
	}

}
