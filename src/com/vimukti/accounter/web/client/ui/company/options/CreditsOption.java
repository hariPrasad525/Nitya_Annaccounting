package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

/**
 * 
 * @author Sai Prasd N
 * 
 */
public class CreditsOption extends AbstractPreferenceOption {

	interface CreditsOptionUiBinder extends UiBinder<Widget, CreditsOption> {
	}

	public CreditsOption() {
		super("");
		createControls();
		initData();
	}

	CheckboxItem creditsApplyAutomatic;

	@Override
	public void onSave() {
		getCompanyPreferences().setCreditsApplyAutomatically(
				creditsApplyAutomatic.getValue());

	}

	@Override
	public String getAnchor() {
		return messages.company();
	}

	@Override
	public void createControls() {
		creditsApplyAutomatic = new CheckboxItem(
				messages.automaticallyApplycredits(), "header");
		add(creditsApplyAutomatic);
		LabelItem descItem = new LabelItem(messages.creditsOptionLabelTxt(),
				"organisation_comment");
		add(descItem);
	}

	@Override
	public void initData() {
		creditsApplyAutomatic.setValue(getCompanyPreferences()
				.isCreditsApplyAutomaticEnable());

	}

	@Override
	public String getTitle() {
		return messages.creditsApply();
	}

}
