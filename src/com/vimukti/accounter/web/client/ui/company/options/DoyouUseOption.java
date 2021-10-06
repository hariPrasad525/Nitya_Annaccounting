package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class DoyouUseOption extends AbstractPreferenceOption {

	LabelItem doYouLabelItem;

	CheckboxItem useCustomerNo;

	CheckboxItem useVendorNo;

	CheckboxItem useAccountNo;

	interface DoyouUseOptionUiBinder extends UiBinder<Widget, DoyouUseOption> {
	}

	public DoyouUseOption() {
		super("");
		createControls();
		initData();
	}

	public void createControls() {
		doYouLabelItem = new LabelItem(messages.Numbers(), "header");

		useCustomerNo = new CheckboxItem(messages.usePayeeId(Global.get()
				.customer()), "useCustomerNo");
		useVendorNo = new CheckboxItem(messages.usePayeeId(Global.get()
				.vendor()), "useVendorNo");
		useAccountNo = new CheckboxItem(messages.useAccountNos(),
				"useAccountNo");
		add(doYouLabelItem);
		add(useCustomerNo);
		add(useVendorNo);
		add(useAccountNo);
	}

	@Override
	public String getTitle() {
		return messages.Numbers();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setUseCustomerId(useCustomerNo.getValue());
		getCompanyPreferences().setUseVendorId(useVendorNo.getValue());
		getCompanyPreferences().setUseAccountNumbers(useAccountNo.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.Numbers();
	}

	@Override
	public void initData() {
		useCustomerNo.setValue(getCompanyPreferences().getUseCustomerId());
		useVendorNo.setValue(getCompanyPreferences().getUseVendorId());
		useAccountNo.setValue(getCompanyPreferences().getUseAccountNumbers());

	}

}
