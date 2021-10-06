/**
 * 
 */
package com.nitya.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.CoreUtils;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.LabelItem;

/**
 * @author Administrator
 * 
 */
public class CompanyCurrencyOption extends AbstractPreferenceOption {

	SelectCombo primaryCurrencyListBox;

	CheckboxItem isEnableMultiCurrencyCheckBox;

	LabelItem currencyCommentLabel;

	private List<ClientCurrency> currenciesList = new ArrayList<ClientCurrency>();

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public CompanyCurrencyOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public void createControls() {
		primaryCurrencyListBox = new SelectCombo(messages.primaryCurrency());
		primaryCurrencyListBox.addStyleName("header");
		currenciesList = CoreUtils.getCurrencies(getCompany().getCurrencies());
		for (ClientCurrency currency : currenciesList) {
			primaryCurrencyListBox.addItem(currency.getFormalName() + "\t"
					+ currency.getDisplayName());
		}
		isEnableMultiCurrencyCheckBox = new CheckboxItem("",
				"organisation_comment");
		currencyCommentLabel = new LabelItem("", "organisation_comment");

		boolean hasPermission = Accounter
				.hasPermission(Features.MULTI_CURRENCY);
		isEnableMultiCurrencyCheckBox.setVisible(hasPermission);
		if (hasPermission) {
			currencyCommentLabel.setTitle(messages.changingCurrencyComment());
			isEnableMultiCurrencyCheckBox.setTitle(messages
					.isMultiCurrencyEnable());
		}

		if (!getCompany().hasOtherCountryCurrency()
				|| !getCompanyPreferences().isEnableMultiCurrency()) {
			isEnableMultiCurrencyCheckBox.setEnabled(true);
		} else {
			isEnableMultiCurrencyCheckBox.setEnabled(false);
		}
		setEnable(false);
		add(primaryCurrencyListBox);
		add(isEnableMultiCurrencyCheckBox);
		add(currencyCommentLabel);
	}

	@Override
	public String getAnchor() {
		return messages.company();
	}

	@Override
	public String getTitle() {
		return messages.currency();
	}

	@Override
	public void initData() {
		for (int i = 0; i < currenciesList.size(); i++) {
			if (getCompanyPreferences().getPrimaryCurrency() != null) {
				if (getCompanyPreferences().getPrimaryCurrency()
						.getFormalName()
						.equals(currenciesList.get(i).getFormalName())) {
					primaryCurrencyListBox.setValue(currenciesList.get(i)
							.getFormalName());
				}
			} else {
				if (getCompany().getCountryPreferences().getPreferredCurrency()
						.equals(currenciesList.get(i).getFormalName())) {
					primaryCurrencyListBox.setValue(currenciesList.get(i)
							.getFormalName());
				}
			}

		}
		isEnableMultiCurrencyCheckBox.setValue(getCompanyPreferences()
				.isEnableMultiCurrency());
	}

	@Override
	public void onSave() {
		if (primaryCurrencyListBox.getSelectedIndex() != -1) {
			getCompanyPreferences().setPrimaryCurrency(
					currenciesList.get(primaryCurrencyListBox
							.getSelectedIndex()));
		}
		getCompanyPreferences().setEnableMultiCurrency(
				isEnableMultiCurrencyCheckBox.getValue());
	}

	public void setEnable(boolean isEnable) {
		primaryCurrencyListBox.setEnabled(isEnable);
	}
}
