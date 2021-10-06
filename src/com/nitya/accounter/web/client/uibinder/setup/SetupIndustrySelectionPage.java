/**
 * 
 */
package com.nitya.accounter.web.client.uibinder.setup;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.core.AccountsTemplate;
import com.nitya.accounter.web.client.ui.Accounter;

/**
 * @author Administrator
 * 
 */
public class SetupIndustrySelectionPage extends AbstractSetupPage {

	private static SetupIndustrySelectionPageUiBinder uiBinder = GWT
			.create(SetupIndustrySelectionPageUiBinder.class);
	@UiField
	FlowPanel viewPanel;
	@UiField
	ListBox industryList;
	@UiField
	Label customizeAccounter, settingsLater, industry;
	@UiField
	Label selectIndustry;
	@UiField
	Label headerLabel;
	private SetupWizard setupWizard;

	interface SetupIndustrySelectionPageUiBinder extends
			UiBinder<Widget, SetupIndustrySelectionPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 * 
	 * @param setupWizard
	 */
	public SetupIndustrySelectionPage(SetupWizard setupWizard) {
		initWidget(uiBinder.createAndBindUi(this));
		this.setupWizard = setupWizard;
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.selectYourIndustry());

		customizeAccounter.setText(messages.selectIndustryInfoHTML1()
				+ messages.selectIndustryInfoHTML2()
				+ messages.selectIndustryInfoHTML3()
				+ messages.selectIndustryInfoHTML4());
		industryList.setName(messages.industry());
		selectIndustry.setText(messages.selectIndustry());
		industryList.setName(messages.industry());

		industryList.setVisibleItemCount(15);

	}

	public void onLoad() {

		int industryType = preferences.getIndustryType();
		if (industryType <= 0) {
			Map<Integer, AccountsTemplate> allIndustiesWithAccounts = setupWizard
					.getAllIndustiesWithAccounts();
			for (AccountsTemplate template : allIndustiesWithAccounts.values()) {
				industryList.addItem(template.getName());
			}
			return;
		}
		// industryType--;
		// industryList.setSelectedIndex(industryType);
	}

	@Override
	public void onSave() {

		int selectedValue = industryList.getSelectedIndex();
		if (selectedValue < 0) {
			return;
		}
		selectedValue++;
		if (selectedValue != preferences.getIndustryType()) {
			preferences.setIndustryType(selectedValue);
		}
	}

	@Override
	public boolean canShow() {
		return (!isSkip);
	}

	@Override
	protected boolean validate() {
		if (industryList.getSelectedIndex() == -1) {
			Accounter.showError(messages.selectYourIndustry());
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String getViewName() {
		return messages.selectIndustryType();
	}
}
