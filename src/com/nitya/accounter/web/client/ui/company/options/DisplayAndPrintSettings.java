/**
 * 
 */
package com.nitya.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

/**
 * @author vimukti2
 * 
 */
public class DisplayAndPrintSettings extends Composite implements HasText {

	private static DisplayAndPrintSettingsUiBinder uiBinder = GWT
			.create(DisplayAndPrintSettingsUiBinder.class);
	@UiField
	Label deafaultDisplayAndPrintSettingsLabel;
	@UiField
	CheckBox deafaultDisplayAndPrintSettingsCheckBox;

	interface DisplayAndPrintSettingsUiBinder extends
			UiBinder<Widget, DisplayAndPrintSettings> {
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
	public DisplayAndPrintSettings() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub

	}

	private void createControls() {
		AccounterMessages messages=Global.get().messages();
		deafaultDisplayAndPrintSettingsLabel.setText(messages
				.getDefaultDisplayAndPrintSettings());
		deafaultDisplayAndPrintSettingsCheckBox.setText(messages.getTurnOnTextWrappinginReportColumns());
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub

	}

}
