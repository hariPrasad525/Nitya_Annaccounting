/**
 * 
 */
package com.nitya.accounter.web.client.ui.company.options;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

/**
 * @author vimukti36
 * 
 */
public class CompanyEinOption extends AbstractPreferenceOption {

	LabelItem EindescriptionLabel;

	TextItem EINTextBox;

	interface CompanyEinOptionUiBinder extends
			UiBinder<Widget, CompanyEinOption> {
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
	public CompanyEinOption() {
		super("");
		createControls();
		initData();
	}

	public void initData() {
		String taxId = getCompany().getTaxId();
		EINTextBox.setValue(taxId);
	}

	public void createControls() {
		EindescriptionLabel = new LabelItem(messages.EINDescription(),
				"einDescPanel");
		EINTextBox = new TextItem(messages.taxId(), "header");
		add(EINTextBox);
		add(EindescriptionLabel);
	}

	@Override
	public String getTitle() {
		return messages.taxId();
	}

	@Override
	public void onSave() {
		if (EINTextBox.getValue() != null)
			getCompany().setTaxId(EINTextBox.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.company();
	}
}
