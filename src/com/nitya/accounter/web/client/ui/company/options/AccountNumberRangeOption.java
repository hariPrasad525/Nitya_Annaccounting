package com.nitya.accounter.web.client.ui.company.options;

import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.LabelItem;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class AccountNumberRangeOption extends AbstractPreferenceOption {

	public AccountNumberRangeOption() {
		super("");
		createControls();
		initData();
	}

	CheckboxItem accountNumberRangeCheck;

	@Override
	public String getTitle() {

		return messages.accounNumberRange();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setIsAccountNumberRangeCheck(
				accountNumberRangeCheck.getValue());

	}

	@Override
	public String getAnchor() {
		return messages.company();
	}

	@Override
	public void createControls() {
		accountNumberRangeCheck = new CheckboxItem(
				messages.accountNumberRangeCheck(), "header");
		LabelItem item = new LabelItem(
				messages.enableOrDisableTheAccountNumberRangeChecking(),
				"organisation_comment");
		add(accountNumberRangeCheck);
		add(item);
	}

	@Override
	public void initData() {
		accountNumberRangeCheck.setValue(getCompanyPreferences()
				.isAccountnumberRangeCheckEnable());

	}

}
