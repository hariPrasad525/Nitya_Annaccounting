package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class AgeingAndSellingDetailsOption extends AbstractPreferenceOption {
	LabelItem ageingLabel;

	RadioGroupItem ageingRadioGroup;

	LabelItem ageingdescriptionLabel;

	CheckboxItem salesOrderCheckBox;

	StyledPanel mainPanel;

	interface AgeingAndSellingDetailsOptionUiBinder extends
			UiBinder<Widget, AgeingAndSellingDetailsOption> {
	}

	public AgeingAndSellingDetailsOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public void initData() {
		if (getCompanyPreferences().getAgeingFromTransactionDateORDueDate() == 1) {
			ageingRadioGroup.setValue(messages.ageingforduedate());
		} else {

			ageingRadioGroup.setValue(messages.ageingforduedate());
		}
		salesOrderCheckBox.setValue(getCompanyPreferences()
				.isSalesOrderEnabled());
	}

	@Override
	public void createControls() {

		ageingRadioGroup = new RadioGroupItem("", "salesOrderandCheckBoxPanel");
		ageingRadioGroup.setShowTitle(false);
		ageingRadioGroup.setValueMap(messages.ageingforduedate(),
				messages.ageingfortransactiondate());
		ageingRadioGroup.setDefaultValue(messages.ageingforduedate());

		ageingLabel = new LabelItem(messages.ageingDetails(), "header");
		ageingdescriptionLabel = new LabelItem(
				messages.agingDetailsDescription(), "organisation_comment");

		salesOrderCheckBox = new CheckboxItem(
				messages.enablePreference(messages.salesOrder()), "header");
		salesOrderCheckBox.setVisible(Accounter
				.hasPermission(Features.SALSE_ORDER));
		mainPanel = new StyledPanel("ageingLabelAndDescPanel");
		mainPanel.add(ageingRadioGroup);
		mainPanel.add(salesOrderCheckBox);
		add(mainPanel);
	}

	@Override
	public String getTitle() {
		return messages.ageingAndSellingDetails();
	}

	@Override
	public void onSave() {
		if (ageingRadioGroup.getValue().equals(messages.ageingforduedate()))
			getCompanyPreferences().setAgeingFromTransactionDateORDueDate(1);
		else
			getCompanyPreferences().setAgeingFromTransactionDateORDueDate(2);
		getCompanyPreferences().setSalesOrderEnabled(
				salesOrderCheckBox.getValue());

	}

	@Override
	public String getAnchor() {
		return messages.ageingAndSellingDetails();
	}

}
