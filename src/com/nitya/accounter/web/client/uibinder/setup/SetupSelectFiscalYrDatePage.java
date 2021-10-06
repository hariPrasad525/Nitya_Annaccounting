/**
 * 
 */
package com.nitya.accounter.web.client.uibinder.setup;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;

/**
 * @author Administrator
 * 
 */
public class SetupSelectFiscalYrDatePage extends AbstractSetupPage {

	private static SetupSelectFiscalYrDatePageUiBinder uiBinder = GWT
			.create(SetupSelectFiscalYrDatePageUiBinder.class);
	@UiField
	FlowPanel viewPanel;
	@UiField
	Label fiscalStarts;
	@UiField
	ListBox fiscalStartsList;
	@UiField
	Label fiscalInfo;
	@UiField
	Label headerLabel;
	private List<String> monthsList;
	String[] monthNames;

	interface SetupSelectFiscalYrDatePageUiBinder extends
			UiBinder<Widget, SetupSelectFiscalYrDatePage> {
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
	public SetupSelectFiscalYrDatePage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.selectFirstMonthOfFiscalYear());

		fiscalStarts.setText(messages.myFiscalYearsStartsIn());
		monthNames = new String[] { DayAndMonthUtil.january(),
				DayAndMonthUtil.february(), DayAndMonthUtil.march(),
				DayAndMonthUtil.april(), DayAndMonthUtil.may_full(),
				DayAndMonthUtil.june(), DayAndMonthUtil.july(),
				DayAndMonthUtil.august(), DayAndMonthUtil.september(),
				DayAndMonthUtil.october(), DayAndMonthUtil.november(),
				DayAndMonthUtil.december() };
		monthsList = new ArrayList<String>();
		// fiscalStartsList = null;
		for (int i = 0; i < monthNames.length; i++) {
			monthsList.add(monthNames[i]);
			fiscalStartsList.addItem(monthNames[i]);
		}
		fiscalInfo.setText(messages.fiscalYearsaemasTaxyear());
	}

	@Override
	public void onLoad() {
		if (monthNames.length > 0)
			fiscalStartsList.setSelectedIndex(preferences
					.getFiscalYearFirstMonth());

	}

	public void onSave() {
		preferences
				.setFiscalYearFirstMonth(fiscalStartsList.getSelectedIndex());
	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	protected boolean validate() {
		if (fiscalStartsList.getSelectedIndex() == -1) {
			Accounter.showError(messages
					.selectFirstMonthOfFiscalYear());
			return false;
		} else {
			return true;
		}

	}

	@Override
	public String getViewName() {
		return messages.fiscalYear();
	}

}
