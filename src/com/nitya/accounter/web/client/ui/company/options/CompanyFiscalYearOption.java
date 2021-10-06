package com.nitya.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;

public class CompanyFiscalYearOption extends AbstractPreferenceOption {

	SelectCombo monthNameComboBox;

	LabelItem fiscalYearDescriptionLabel;

	LabelItem dateItemLable;

	DateBox dateItem;
	List<String> monthNam = new ArrayList<String>();
	String[] monthNames;

	StyledPanel mainPanel;

	public CompanyFiscalYearOption() {
		super("");
		createControls();
		initData();
	}

	public void createControls() {
		monthNameComboBox = new SelectCombo(
				messages.selectFirstMonthOfFiscalYear());
		fiscalYearDescriptionLabel.setValue(messages.FiscalYearDescription());
		fiscalYearDescriptionLabel.setStyleName("organisation_comment");
		monthNames = new String[] { DayAndMonthUtil.january(),
				DayAndMonthUtil.february(), DayAndMonthUtil.march(),
				DayAndMonthUtil.april(), DayAndMonthUtil.may_full(),
				DayAndMonthUtil.june(), DayAndMonthUtil.july(),
				DayAndMonthUtil.august(), DayAndMonthUtil.september(),
				DayAndMonthUtil.october(), DayAndMonthUtil.november(),
				DayAndMonthUtil.december() };

		for (int i = 0; i < monthNames.length; i++) {
			monthNameComboBox.addItem(monthNames[i]);
			monthNam.add(monthNames[i]);
		}

		dateItemLable.setValue(messages.preventPostBefore());
		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat(getCompanyPreferences().getDateFormat());
		dateItem.setFormat(new DateBox.DefaultFormat(dateFormat));
		mainPanel = new StyledPanel("CompanyFiscalYearOption");
		mainPanel.add(monthNameComboBox);
		mainPanel.add(fiscalYearDescriptionLabel);
		mainPanel.add(dateItemLable);
		add(mainPanel);
	}

	public void initData() {
		if (monthNam.size() > 0)
			monthNameComboBox.setSelectedItem(getCompanyPreferences()
					.getFiscalYearFirstMonth());
		if (getCompanyPreferences().getPreventPostingBeforeDate() != 0) {
			dateItem.setValue(new ClientFinanceDate(getCompanyPreferences()
					.getPreventPostingBeforeDate()).getDateAsObject());
		}

	}

	@Override
	public String getTitle() {
		return messages.FiscalTaxYear();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setFiscalYearFirstMonth(
				(monthNameComboBox.getSelectedIndex()));
		if (dateItem.getValue() != null) {
			getCompanyPreferences().setPreventPostingBeforeDate(
					new ClientFinanceDate(dateItem.getValue()).getDate());
		} else {
			getCompanyPreferences().setPreventPostingBeforeDate(0);
		}
	}

	@Override
	public String getAnchor() {
		return messages.company();
	}

}
