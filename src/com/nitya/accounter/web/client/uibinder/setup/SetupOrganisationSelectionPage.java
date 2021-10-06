/**
 * 
 */
package com.nitya.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.company.options.OrganizationTypeConstants;

/**
 * @author Administrator
 * 
 */
public class SetupOrganisationSelectionPage extends AbstractSetupPage {

	private static SetupOrganisationSelectionPageUiBinder uiBinder = GWT
			.create(SetupOrganisationSelectionPageUiBinder.class);
	@UiField
	StyledPanel viewPanel;
	@UiField
	Label organizeText;
	@UiField
	RadioButton propriterShip;
	@UiField
	Label uninCorporated;
	@UiField
	RadioButton partnership;
	@UiField
	Label morePartners;
	@UiField
	RadioButton lLC;
	@UiField
	Label lLCText;
	@UiField
	RadioButton corporation;
	@UiField
	Label corporationText;
	@UiField
	RadioButton sCorporation;
	@UiField
	Label sCorporationText;
	@UiField
	RadioButton nonProfit;
	@UiField
	Label nonProfitText;
	@UiField
	RadioButton other;
	@UiField
	Label organizationLink;
	@UiField
	ListBox lLCCombo;
	@UiField
	Label headerLabel;

	interface SetupOrganisationSelectionPageUiBinder extends
			UiBinder<Widget, SetupOrganisationSelectionPage> {
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
	public SetupOrganisationSelectionPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.howIsYourCompanyOrganized());

		organizeText.setText(messages
				.howIsYourCompanyOrganizedDesc());
		propriterShip.setText(messages.soleProprietorship());
		partnership.setText(messages.partnershipOrLLP());
		lLC.setText(messages.LLC());
		corporation.setText(messages.corporation());
		sCorporation.setText(messages.sCorporation());
		nonProfit.setText(messages.nonProfit());
		other.setText(messages.otherNone());

		organizeText.setText(messages
				.howIsYourCompanyOrganizedDesc());
		uninCorporated.setText(messages.soleProprietorshipDesc());
		morePartners.setText(messages.partnershipOrLLPDesc());
		lLCText.setText(messages.LLCDesc());
		corporationText.setText(messages.sCorporationDesc());
		sCorporationText.setText(messages.corporationDesc());
		nonProfitText.setText(messages.nonProfitDesc());

		organizeText.addStyleName("organisation_comment");
		uninCorporated.addStyleName("organisation_comment");
		morePartners.addStyleName("organisation_comment");
		lLCText.addStyleName("organisation_comment");
		corporationText.addStyleName("organisation_comment");
		sCorporationText.addStyleName("organisation_comment");
		nonProfitText.addStyleName("organisation_comment");

		// HTML organizationLink;

		propriterShip.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});
		partnership.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});

		lLCCombo.addStyleName("organisation_combo");

		lLCCombo.addItem(messages.llcSingleMemberForm());
		lLCCombo.addItem(messages.llcMultiMemberForm());
		lLCCombo.setEnabled(false);
		lLC.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(true);
			}
		});
		corporation.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});
		sCorporation.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});
		nonProfit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});
		other.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});
	}

	public void onLoad() {

		switch (preferences.getOrganizationType()) {
		case OrganizationTypeConstants.SOLE_PROPRIETORSHIP:
			propriterShip.setValue(true);
			break;
		case OrganizationTypeConstants.CORPORATION:
			partnership.setValue(true);
			break;
		case OrganizationTypeConstants.S_CORPORATION:
			sCorporation.setValue(true);
			break;
		case OrganizationTypeConstants.LLC:
			lLC.setValue(true);
			break;
		case OrganizationTypeConstants.PARTNERSHIP:
			partnership.setValue(true);
			break;
		case OrganizationTypeConstants.NON_PROFIT:
			nonProfit.setValue(true);
			break;
		case OrganizationTypeConstants.OTHER:
			other.setValue(true);
		}
	}

	@Override
	public void onSave() {
		if (propriterShip.getValue()) {
			preferences
					.setOrganizationType(OrganizationTypeConstants.SOLE_PROPRIETORSHIP);
		} else if (partnership.getValue()) {
			preferences
					.setOrganizationType(OrganizationTypeConstants.CORPORATION);
		} else if (sCorporation.getValue()) {
			preferences
					.setOrganizationType(OrganizationTypeConstants.S_CORPORATION);
		} else if (lLC.getValue()) {
			preferences.setOrganizationType(OrganizationTypeConstants.LLC);
		} else if (partnership.getValue()) {
			preferences
					.setOrganizationType(OrganizationTypeConstants.PARTNERSHIP);
		} else if (nonProfit.getValue()) {
			preferences
					.setOrganizationType(OrganizationTypeConstants.NON_PROFIT);
		} else {
			preferences.setOrganizationType(OrganizationTypeConstants.OTHER);
		}

	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	protected boolean validate() {
		return true;
	}

	@Override
	public String getViewName() {
		return messages.companyOrganization();
	}

}
