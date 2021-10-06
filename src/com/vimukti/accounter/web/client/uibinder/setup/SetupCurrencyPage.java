/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.StyledPanel;

/**
 * @author Administrator
 * 
 */
public class SetupCurrencyPage extends AbstractSetupPage {

	private static SetupCurrencyPageUiBinder uiBinder = GWT
			.create(SetupCurrencyPageUiBinder.class);
	@UiField
	Label headerLabel;
	@UiField
	Label primaryCurrenyLabel;
	@UiField
	ListBox primaryCurrencyListBox;
	@UiField
	CheckBox isMultiCurrencyAllowed;
	// @UiField
	StyledPanel currencyListGridPanel;
	// private CurrenciesGrid currenciesGrid;
	// private Set<ClientCurrency> currencySet;
	private List<ClientCurrency> currenciesList = new ArrayList<ClientCurrency>();

	// private String selectedCuntry;

	interface SetupCurrencyPageUiBinder extends
			UiBinder<Widget, SetupCurrencyPage> {
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
	public SetupCurrencyPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		// currenciesList = UIUtils.getCurrenciesList();
		headerLabel.setText(messages.selectCurrency());
		primaryCurrenyLabel.setText(messages.primaryCurrency());
		currenciesList = CoreUtils
				.getCurrencies(new ArrayList<ClientCurrency>());
		for (ClientCurrency currency : currenciesList) {
			primaryCurrencyListBox.addItem(currency.getFormalName() + "\t"
					+ currency.getDisplayName());
		}
		isMultiCurrencyAllowed.setVisible(Accounter
				.hasPermission(Features.MULTI_CURRENCY));
		isMultiCurrencyAllowed.setText(messages.isMultiCurrencyEnable());
		// currenciesGrid = new CurrenciesGrid();
		// currenciesGrid.init();
		// currenciesGrid.setRecords(currenciesList);
		// currencyListGridPanel.add(currenciesGrid);
	}

	@Override
	public boolean canShow() {
		return (!isSkip && !isPayrollSetup);
	}

	@Override
	protected void onLoad() {
		ClientCurrency primaryCurrency = preferences.getPrimaryCurrency();
		if (primaryCurrency != null) {
			for (int index = 0; index < currenciesList.size(); index++) {
				if (currenciesList.get(index).getFormalName()
						.equals(primaryCurrency.getFormalName())) {
					primaryCurrencyListBox.setSelectedIndex(index);
				}
			}
		}
		isMultiCurrencyAllowed.setValue(preferences.isEnableMultiCurrency());
	}

	@Override
	protected void onSave() {
		if (primaryCurrencyListBox.getSelectedIndex() != -1) {
			preferences.setPrimaryCurrency(currenciesList
					.get(primaryCurrencyListBox.getSelectedIndex()));
		}
		preferences.setEnableMultiCurrency(isMultiCurrencyAllowed.getValue());
	}

	@Override
	protected boolean validate() {
		if (primaryCurrencyListBox.getSelectedIndex() == -1) {
			Accounter.showError(messages
					.pleaseselectvalidtransactionGrid(messages.currency()));
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String getViewName() {
		return messages.currency();
	}

}
