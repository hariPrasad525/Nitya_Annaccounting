package com.vimukti.accounter.web.client.ui.company.options;

import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class DoyouUseShipingsOption extends AbstractPreferenceOption {

	LabelItem shippingmedescritionLabel;
	CheckboxItem useShipMethods;

	public DoyouUseShipingsOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return messages.Doyoudoshipping();
	}

	@Override
	public void onSave() {
		getCompanyPreferences()
				.setDoProductShipMents(useShipMethods.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.Doyoudoshipping();
	}

	@Override
	public void createControls() {

		useShipMethods = new CheckboxItem(messages.Doyoudoshipping(), "header");
		shippingmedescritionLabel = new LabelItem(
				messages.Thisoptioncanbeusedtoenable(), "organisation_comment");
		add(useShipMethods);
		add(shippingmedescritionLabel);
	}

	@Override
	public void initData() {
		useShipMethods.setValue(getCompanyPreferences().isDoProductShipMents());

	}

}
