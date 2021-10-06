/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

/**
 * @author vimukti36
 * 
 */
public class OrganisationTypeOption {

	public OrganisationTypeOption(String styleName) {

		// TODO Auto-generated constructor stub
	}
	//
	// private static TaxFormOptionUiBinder uiBinder = GWT
	// .create(TaxFormOptionUiBinder.class);
	// @UiField
	// Label radioButtonHeaderLabel;
	// @UiField
	// FlowPanel headerLabelPanel;
	// @UiField
	// FlowPanel radioButtonsMainPanel;
	// @UiField
	// FlowPanel viewPanel;
	//
	// RadioButton propriterShip;
	// RadioButton partnership;
	// RadioButton lLC;
	// ListBox lLCCombo;
	// RadioButton corporation;
	// RadioButton sCorporation;
	// RadioButton nonProfit;
	// RadioButton other;
	// HTML organizationLink;
	//
	// interface TaxFormOptionUiBinder extends
	// UiBinder<Widget, OrganisationTypeOption> {
	// }
	//
	// /**
	// * Because this class has a default constructor, it can be used as a
	// binder
	// * template. In other words, it can be used in other *.ui.xml files as
	// * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	// * xmlns:g="urn:import:**user's package**">
	// * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note
	// that
	// * depending on the widget that is used, it may be necessary to implement
	// * HasHTML instead of HasText.
	// */
	// public OrganisationTypeOption() {
	// super("");
	// createControls();
	// initData();
	// }
	//
	// public void createControls() {
	//
	// radioButtonHeaderLabel.setText(messages.howIsYourCompanyOrganized());
	// propriterShip = new RadioButton(messages.organisation(),
	// messages.soleProprietorship());
	// partnership = new RadioButton(messages.organisation(),
	// messages.partnershipOrLLP());
	// lLC = new RadioButton(messages.organisation(), messages.LLC());
	// corporation = new RadioButton(messages.organisation(),
	// messages.corporation());
	// sCorporation = new RadioButton(messages.organisation(),
	// messages.sCorporation());
	// nonProfit = new RadioButton(messages.organisation(),
	// messages.nonProfit());
	// other = new RadioButton(messages.organisation(), messages.otherNone());
	//
	// Label organizeText = new Label(messages.howIsYourCompanyOrganizedDesc());
	// Label uninCorporated = new Label(messages.soleProprietorshipDesc());
	// Label morePartners = new Label(messages.partnershipOrLLPDesc());
	// Label lLCText = new Label(messages.LLCDesc());
	// Label corporationText = new Label(messages.sCorporationDesc());
	// Label sCorporationText = new Label(messages.corporationDesc());
	// Label nonProfitText = new Label(messages.nonProfitDesc());
	//
	// organizeText.addStyleName("organisation_comment");
	// uninCorporated.addStyleName("organisation_comment");
	// morePartners.addStyleName("organisation_comment");
	// lLCText.addStyleName("organisation_comment");
	// corporationText.addStyleName("organisation_comment");
	// sCorporationText.addStyleName("organisation_comment");
	// nonProfitText.addStyleName("organisation_comment");
	// lLCCombo = new ListBox();
	// lLCCombo.addStyleName("organisation_combo");
	// lLCCombo.addItem(messages.llcSingleMemberForm());
	// lLCCombo.addItem(messages.llcMultiMemberForm());
	//
	// viewPanel.add(organizeText);
	// viewPanel.add(propriterShip);
	// viewPanel.add(uninCorporated);
	// viewPanel.add(partnership);
	// viewPanel.add(morePartners);
	// viewPanel.add(lLC);
	// viewPanel.add(lLCText);
	// viewPanel.add(lLCCombo);
	// viewPanel.add(corporation);
	// viewPanel.add(corporationText);
	// viewPanel.add(sCorporation);
	// viewPanel.add(sCorporationText);
	// viewPanel.add(nonProfit);
	// viewPanel.add(nonProfitText);
	// viewPanel.add(other);
	// // HTML organizationLink;
	//
	// propriterShip.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent arg0) {
	// lLCCombo.setEnabled(false);
	// }
	// });
	// partnership.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent arg0) {
	// lLCCombo.setEnabled(false);
	// }
	// });
	//
	// lLCCombo.setEnabled(false);
	// lLC.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent arg0) {
	// lLCCombo.setEnabled(true);
	// }
	// });
	// corporation.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent arg0) {
	// lLCCombo.setEnabled(false);
	// }
	// });
	// sCorporation.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent arg0) {
	// lLCCombo.setEnabled(false);
	// }
	// });
	// nonProfit.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent arg0) {
	// lLCCombo.setEnabled(false);
	// }
	// });
	// other.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent arg0) {
	// lLCCombo.setEnabled(false);
	// }
	// });
	// }
	//
	// public void initData() {
	// switch (getCompanyPreferences().getOrganizationType()) {
	// case OrganizationTypeConstants.SOLE_PROPRIETORSHIP:
	// propriterShip.setValue(true);
	// break;
	// case OrganizationTypeConstants.CORPORATION:
	// partnership.setValue(true);
	// break;
	// case OrganizationTypeConstants.S_CORPORATION:
	// sCorporation.setValue(true);
	// break;
	// case OrganizationTypeConstants.LLC:
	// lLC.setValue(true);
	// break;
	// case OrganizationTypeConstants.PARTNERSHIP:
	// partnership.setValue(true);
	// break;
	// case OrganizationTypeConstants.NON_PROFIT:
	// nonProfit.setValue(true);
	// break;
	// case OrganizationTypeConstants.OTHER:
	// other.setValue(true);
	// }
	// }
	//
	// public OrganisationTypeOption(String firstName) {
	// initWidget(uiBinder.createAndBindUi(this));
	//
	// }
	//
	// @Override
	// public String getTitle() {
	// return messages.companyOrganization();
	// }
	//
	// @Override
	// public void onSave() {
	// if (propriterShip.getValue()) {
	// getCompanyPreferences().setOrganizationType(
	// OrganizationTypeConstants.SOLE_PROPRIETORSHIP);
	// } else if (partnership.getValue()) {
	// getCompanyPreferences().setOrganizationType(
	// OrganizationTypeConstants.CORPORATION);
	// } else if (sCorporation.getValue()) {
	// getCompanyPreferences().setOrganizationType(
	// OrganizationTypeConstants.S_CORPORATION);
	// } else if (lLC.getValue()) {
	// getCompanyPreferences().setOrganizationType(
	// OrganizationTypeConstants.LLC);
	// } else if (partnership.getValue()) {
	// getCompanyPreferences().setOrganizationType(
	// OrganizationTypeConstants.PARTNERSHIP);
	// } else if (nonProfit.getValue()) {
	// getCompanyPreferences().setOrganizationType(
	// OrganizationTypeConstants.NON_PROFIT);
	// } else {
	// getCompanyPreferences().setOrganizationType(
	// OrganizationTypeConstants.OTHER);
	// }
	// }
	//
	// @Override
	// public String getAnchor() {
	// return messages.company();
}
