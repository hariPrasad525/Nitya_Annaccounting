package com.nitya.accounter.web.client.ui.company.options;

import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.RadioGroupItem;

public class TrackDiscountsOption extends AbstractPreferenceOption {

	CheckboxItem trackDiscountCheckBox;

	LabelItem discountLabel;

	LabelItem oneperTransactionLabel;
	LabelItem oneperdetaillineLabel;

	RadioGroupItem oneperdetaillineTransctiongroup;

	StyledPanel hidePanel;

	LabelItem selectDiscountAccount;

	StyledPanel mainpanel;

	SelectCombo accountsCombo;

	public TrackDiscountsOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return messages.trackDiscount();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setTrackDiscounts(
				trackDiscountCheckBox.getValue());
		if (oneperdetaillineTransctiongroup.getValue().equals(
				messages.oneperdetailline())) {
			getCompanyPreferences().setDiscountPerDetailLine(true);
		} else {
			getCompanyPreferences().setDiscountPerDetailLine(false);
		}

	}

	@Override
	public String getAnchor() {
		return messages.trackDiscount();
	}

	@Override
	public void createControls() {

		trackDiscountCheckBox = new CheckboxItem(messages.trackDiscount(),
				"header");
		discountLabel = new LabelItem(messages.discountInAllTransaction(),
				"discountLabel");

		oneperTransactionLabel = new LabelItem(messages.onepertransaction()
				+ " : " + messages.onePerTransactionDescription(),
				"organisation_comment");
		oneperdetaillineLabel = new LabelItem(messages.oneperdetailline()
				+ " : " + messages.onePerDetailLineDescription(),
				"organisation_comment");

		oneperdetaillineTransctiongroup = new RadioGroupItem();
		oneperdetaillineTransctiongroup
				.setGroupName("oneperdetaillineTransctiongroup");
		oneperdetaillineTransctiongroup.setShowTitle(false);

		oneperdetaillineTransctiongroup.setValueMap(
				messages.onepertransaction(), messages.oneperdetailline());
		oneperdetaillineTransctiongroup.setDefaultValue(messages
				.onepertransaction());

		selectDiscountAccount = new LabelItem(
				messages.selectDiscountAccountDesc(), "organisation_comment");

		accountsCombo = new SelectCombo(messages.discountAccount());
		List<ClientAccount> accounts = getAccounts();
		for (int i = 0; i < accounts.size(); i++) {
			accountsCombo.addItem(accounts.get(i).getName());
		}
		if (getCompany().getTradingAddress() != null
				&& getCompany().getTradingAddress().getCountryOrRegion() != null) {
			accountsCombo.setSelectedItem(accounts.indexOf(getCompany()
					.getCashDiscountAccount()));
		} else {
			accountsCombo.setSelectedItem(0);
		}
		mainpanel = new StyledPanel("discountsMainPanel");
		hidePanel = new StyledPanel("discountshidePanel");
		StyledPanel discountPanel = new StyledPanel("discountPanel");
		discountPanel.add(trackDiscountCheckBox);
		mainpanel.add(discountPanel);
		mainpanel.add(discountLabel);
		hidePanel.add(oneperTransactionLabel);
		hidePanel.add(oneperdetaillineLabel);
		hidePanel.add(oneperdetaillineTransctiongroup);
		hidePanel.add(selectDiscountAccount);
		hidePanel.add(accountsCombo);
		mainpanel.add(hidePanel);

		trackDiscountCheckBox
				.addChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						hidePanel.setVisible(trackDiscountCheckBox.getValue());
					}
				});
		add(mainpanel);

	}

	private List<ClientAccount> getAccounts() {
		return getCompany().getAccounts();
	}

	@Override
	public void initData() {

		trackDiscountCheckBox.setValue(getCompanyPreferences()
				.isTrackDiscounts());
		hidePanel.setVisible(getCompanyPreferences().isTrackDiscounts());
		if (getCompanyPreferences().isDiscountPerDetailLine())
			oneperdetaillineTransctiongroup.setValue(messages
					.oneperdetailline());
		else
			oneperdetaillineTransctiongroup.setValue(messages
					.onepertransaction());

		ClientAccount account = getCompany().getAccount(
				getCompany().getCashDiscountAccount());

		AsyncCallback<ClientAccount> asyncCallback = new AsyncCallback<ClientAccount>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ClientAccount result) {
				accountsCombo.setSelectedItem(getAccounts().indexOf(result));

			}
		};
		if (account == null) {
			Accounter.createGETService().getObjectByName(
					AccounterCoreType.ACCOUNT, "Cash Discount Given",
					asyncCallback);
		} else {
			accountsCombo.setSelectedItem(getAccounts().indexOf(account));
		}

	}
}
