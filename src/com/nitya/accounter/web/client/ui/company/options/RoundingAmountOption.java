package com.nitya.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.JNSI;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.AccounterValidator;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Rajavarpu Lingarao
 * 
 */
public class RoundingAmountOption extends AbstractPreferenceOption {

	SelectCombo accountsCombo;
	SelectCombo roundingMethodCombo;
	TextItem roundingLimitItem;
	CheckboxItem trackroundingAmountCheckBox;
	CheckboxItem removeifZeroCheckBox;
	StyledPanel hidePanel;
	StyledPanel mainpanel;

	public RoundingAmountOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return messages.roundingAccount();
	}

	private List<ClientAccount> getAccounts() {
		ArrayList<ClientAccount> filterdaccounts = new ArrayList<ClientAccount>();
		ArrayList<ClientAccount> accounts = getCompany().getAccounts();
		for (ClientAccount clientAccount : accounts) {
			if (filter(clientAccount)) {
				filterdaccounts.add(clientAccount);
			}
		}
		return filterdaccounts;
	}

	private boolean filter(ClientAccount acc) {
		return Arrays.asList(ClientAccount.TYPE_INCOME,
				ClientAccount.TYPE_EXPENSE).contains(acc.getType());

	}

	@Override
	public void onSave() {

		getCompanyPreferences().setEnabledRoundingOptions(
				trackroundingAmountCheckBox.getValue());

		getCompanyPreferences().setRoundingMethod(
				roundingMethodCombo.getSelectedIndex() + 1);

		getCompanyPreferences()
				.setRemoveIfZero(removeifZeroCheckBox.getValue());

		getCompanyPreferences().setRoundingLimit(
				Double.valueOf(roundingLimitItem.getValue()));

		int roudingAccount = accountsCombo.getSelectedIndex();
		if (roudingAccount >= 0) {
			getCompanyPreferences().setRoundingAccount(
					getAccounts().get(roudingAccount).getID());
		}

	}

	@Override
	public String getAnchor() {
		return messages.roundingAccount();
	}

	@Override
	public void createControls() {

		trackroundingAmountCheckBox = new CheckboxItem(
				messages2.trackRoundingAmountEnbleForInvoiceAndCashSale(),
				"header");
		hidePanel = new StyledPanel("roundinghidePanel");
		mainpanel = new StyledPanel("mainPanel");
		// Rounding Account
		accountsCombo = new SelectCombo(messages.roundingAccount());
		List<ClientAccount> accounts = getAccounts();
		for (int i = 0; i < accounts.size(); i++) {
			accountsCombo.addItem(accounts.get(i).getName());
		}
		// Rounding Method
		roundingMethodCombo = new SelectCombo(messages.roundingMethod());
		List<String> roundingMethods = new ArrayList<String>();
		roundingMethods.add("Up");
		roundingMethods.add("Down");
		roundingMethods.add("Normal");
		roundingMethodCombo.initCombo(roundingMethods);
		roundingMethodCombo.setDefaultValue(2);
		// Rounding Limit
		roundingLimitItem = new TextItem(messages2.roundingLimit(),
				"roundinglimit");
		roundingLimitItem.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				try {
					String value = roundingLimitItem.getValue();
					if (value == null)
						return;
					Double amount = DataUtils.getAmountStringAsDouble(JNSI
							.getCalcultedAmount(value.toString()));
					if (!AccounterValidator.isAmountTooLarge(amount)
							&& (!AccounterValidator.isAmountNegative(amount))) {
						setLimit(amount);
					}
				} catch (Exception e) {
					setLimit(0.0);
				}
			}
		});

		// Remove if zero
		removeifZeroCheckBox = new CheckboxItem(messages2.removeifZeo(),
				"removeifzero");
		removeifZeroCheckBox.hide();

		StyledPanel roundingPanel = new StyledPanel("roundingPanel");
		roundingPanel.add(trackroundingAmountCheckBox);

		// Adding Hide Panel TO Rounding Options
		mainpanel.add(roundingPanel);
		hidePanel.add(accountsCombo);
		hidePanel.add(roundingMethodCombo);
		hidePanel.add(roundingLimitItem);
		hidePanel.add(removeifZeroCheckBox);
		mainpanel.add(hidePanel);

		trackroundingAmountCheckBox
				.addChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						hidePanel.setVisible(trackroundingAmountCheckBox
								.getValue());
					}
				});

		add(mainpanel);

	}

	public void setLimit(Double amount) {

		if (amount != null) {
			roundingLimitItem.setValue(DataUtils.getAmountAsStringInCurrency(
					amount, null));
		} else {
			roundingLimitItem.setValue("");
		}

	}

	@Override
	public void initData() {
		trackroundingAmountCheckBox.setValue(getCompanyPreferences()
				.isEnabledRoundingOptions());
		long roundingAccount = getCompanyPreferences().getRoundingAccount();
		Accounter.createGETService().getObjectById(AccounterCoreType.ACCOUNT,
				roundingAccount, new AsyncCallback<ClientAccount>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ClientAccount result) {
						accountsCombo.setSelectedItem(getAccounts().indexOf(
								result));
					}
				});
		roundingLimitItem.setValue(String.valueOf(getCompanyPreferences()
				.getRoundingLimit()));

		roundingMethodCombo.setSelectedItem(getCompanyPreferences()
				.getRoundingMethod() - 1);
		hidePanel
				.setVisible(getCompanyPreferences().isEnabledRoundingOptions());

	}
}
