package com.vimukti.accounter.web.client.ui.company.options;

import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class ManageBillsOption extends AbstractPreferenceOption {

	LabelItem managingBillLabelItem;

	RadioGroupItem managingBillRadioGroup;

	LabelItem managingBilldescritionLabel;

	CheckboxItem isPriceLevelsEnabled;

	CheckboxItem purchaseOrderCheckBox;

	public ManageBillsOption() {
		super("");
		createControls();
		initData();
	}

	public void initData() {
		if (getCompanyPreferences().isKeepTrackofBills()) {
			managingBillRadioGroup.setValue(messages.yes());
		} else {
			managingBillRadioGroup.setValue(messages.no());
		}
		if (getCompanyPreferences().isPricingLevelsEnabled()) {
			isPriceLevelsEnabled.setValue(true);
		} else {
			isPriceLevelsEnabled.setValue(false);
		}
		purchaseOrderCheckBox.setValue(getCompanyPreferences()
				.isPurchaseOrderEnabled());
	}

	public void createControls() {
		managingBillLabelItem = new LabelItem(messages.managingBills(),
				"header");
		managingBilldescritionLabel = new LabelItem(
				messages.managingBillDescription(), "organisation_comment");

		managingBillRadioGroup = new RadioGroupItem();
		managingBillRadioGroup.setGroupName("managingBillRadioGroup");
		managingBillRadioGroup.setShowTitle(false);
		managingBillRadioGroup.addStyleName("manageBillsOweRadioButtonsPanel");

		managingBillRadioGroup.setValueMap(messages.yes(), messages.no());
		managingBillRadioGroup.setDefaultValue(messages.ageingforduedate());

		isPriceLevelsEnabled = new CheckboxItem(messages.enabled() + " "
				+ messages.priceLevel(), "header");
		purchaseOrderCheckBox = new CheckboxItem(
				messages.enablePreference(messages.purchaseOrder()), "header");
		purchaseOrderCheckBox.setVisible(Accounter
				.hasPermission(Features.PURCHASE_ORDER));
		add(managingBillLabelItem);
		add(managingBilldescritionLabel);
		add(managingBillRadioGroup);
		add(isPriceLevelsEnabled);
		add(purchaseOrderCheckBox);
	}

	@Override
	public String getTitle() {
		return messages.managingBillsTitle();
	}

	@Override
	public void onSave() {
		if (managingBillRadioGroup.getValue().equals(messages.yes())) {
			getCompanyPreferences().setKeepTrackofBills(true);
		} else {
			getCompanyPreferences().setKeepTrackofBills(false);
		}
		if (isPriceLevelsEnabled.getValue()) {
			getCompanyPreferences().setPricingLevelsEnabled(true);
		} else {
			getCompanyPreferences().setPricingLevelsEnabled(false);
		}
		getCompanyPreferences().setPurchaseOrderEnabled(
				purchaseOrderCheckBox.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.managingBillsTitle();
	}

}
