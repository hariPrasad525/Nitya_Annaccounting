/**
 * 
 */
package com.nitya.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Administrator
 * 
 */
public class SetupComplitionPage extends AbstractSetupPage {

	private static SetupComplitionPageUiBinder uiBinder = GWT
			.create(SetupComplitionPageUiBinder.class);
	@UiField
	FlowPanel viewPanel;
	@UiField
	Label congratulationLabel2;
	@UiField
	Label congratulationLabel1;
	@UiField
	Label congratulationInfo;
	@UiField
	Label headerLabel;

	interface SetupComplitionPageUiBinder extends
			UiBinder<Widget, SetupComplitionPage> {
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
	public SetupComplitionPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.congratulations());
		congratulationInfo.setText(messages.setupComplitionDesc());
	}

	@Override
	public boolean canShow() {
		return !isPayrollSetup;
	}

	@Override
	protected boolean validate() {
		return true;
	}

	@Override
	public boolean isShowProgressPanel() {
		return false;
	}

	@Override
	public String getViewName() {
		return messages.finish();
	}

}
