package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class ConversionDateView extends AbstractBaseView<ClientFinanceDate> {
	private StyledPanel mainPanel, headerPanel, bodyPanel;
	private HTML titleHtml, superTitleHtml, bodyHtml, bodyFooterHtml,
			bodycommentHtml;
	private SelectCombo monthCombo, yearCombo;
	private StyledPanel buttonPanel;
	private String[] monthArray;
	private String[] yearArray;
	private DynamicForm comboForm;
	private List<String> monthList;
	private List<String> yearList;
	private Button saveButton, cancelButton;

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		titleHtml = new HTML(messages.conversionDate());
		superTitleHtml = new HTML(messages.conversionBalanceTitle());

		bodyHtml = new HTML(messages.conversationDateSelectionHTML());

		bodycommentHtml = new HTML(messages.conversionBodyComment());
		bodycommentHtml.setVisible(false);

		mainPanel = new StyledPanel("mainPanel");
		headerPanel = new StyledPanel("headerPanel");
		bodyPanel = new StyledPanel("bodyPanel");
		comboForm = new DynamicForm("viewform");
		buttonPanel = new StyledPanel("buttonPanel");
		saveButton = new Button(messages.save());
		cancelButton = new Button(messages.cancel());

		monthArray = new String[] { DayAndMonthUtil.january(),
				DayAndMonthUtil.february(), DayAndMonthUtil.march(),
				DayAndMonthUtil.april(), DayAndMonthUtil.may_full(),
				DayAndMonthUtil.june(), DayAndMonthUtil.july(),
				DayAndMonthUtil.august(), DayAndMonthUtil.september(),
				DayAndMonthUtil.october(), DayAndMonthUtil.november(),
				DayAndMonthUtil.december() };
		yearArray = new String[] { 2011 + "", 2012 + "" };
		monthCombo = new SelectCombo(messages.month());
		monthList = new ArrayList<String>();
		for (int i = 0; i < monthArray.length; i++) {
			monthList.add(monthArray[i]);
		}
		bodyFooterHtml = new HTML();
		final SafeHtml bodyFooter = new SafeHtml() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String asString() {
				// TODO Auto-generated method stub
				return messages.bodyFooter();
			}
		};
		monthCombo.initCombo(monthList);
		monthCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						monthCombo.setSelected(monthCombo.getSelectedValue());
						bodycommentHtml.setVisible(true);
						bodyFooterHtml.setHTML(bodyFooter
								+ "<b>"
								+ getPreviousMonth(
										monthCombo.getSelectedValue(),
										yearCombo.getSelectedValue())
								+ getYear(monthCombo.getSelectedValue(),
										yearCombo.getSelectedValue()) + "</b>");
						bodyFooterHtml.addStyleName("conversion_date_footer");
					}
				});

		yearCombo = new SelectCombo(messages.year());
		yearList = new ArrayList<String>();
		for (int i = 0; i < yearArray.length; i++) {
			yearList.add(yearArray[i]);
		}
		yearCombo.initCombo(yearList);
		yearCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						yearCombo.setSelected(yearCombo.getSelectedValue());
						bodycommentHtml.setVisible(true);
						bodyFooterHtml.setHTML(bodyFooter
								+ "<b>"
								+ getPreviousMonth(
										monthCombo.getSelectedValue(),
										yearCombo.getSelectedValue())
								+ getYear(monthCombo.getSelectedValue(),
										yearCombo.getSelectedValue())
								+ "</b>  </font></p>");
					}

				});

		superTitleHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new ConversionBalancesAction().run(null, false);
			}
		});

		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String endindDate = getPreviousMonth(
						monthCombo.getSelectedValue(),
						yearCombo.getSelectedValue())
						+ getYear(monthCombo.getSelectedValue(),
								yearCombo.getSelectedValue());
				new ConversionBalancesAction().run(null, false
				/*
				 * ,endindDate, getYear(monthCombo.getSelectedValue(), yearCombo
				 * .getSelectedValue())
				 */);
			}
		});

		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new ConversionBalancesAction().run(null, false);
			}
		});
		headerPanel.add(superTitleHtml);
		headerPanel.add(titleHtml);

		comboForm.add(monthCombo, yearCombo);

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		bodyPanel.add(bodyHtml);
		bodyPanel.add(comboForm);
		bodyPanel.add(bodyFooterHtml);
		bodyPanel.add(bodycommentHtml);
		bodyPanel.add(buttonPanel);

		mainPanel.add(headerPanel);
		mainPanel.add(bodyPanel);

		add(mainPanel);
	}

	private String getYear(String month, String year) {
		String returnValue = null;
		if (month == null || year == null) {
			returnValue = "";
		} else if (month.equals(DayAndMonthUtil.january())) {
			returnValue = String.valueOf(Integer.parseInt(year) - 1);
		} else {
			returnValue = year;
		}
		return returnValue;
	}

	private String getPreviousMonth(String month, String year) {
		String returnValue = null;
		if (month == null || year == null) {
			returnValue = "";
		} else if (month.equals(DayAndMonthUtil.january())) {
			returnValue = messages.datenumber(31) + " "
					+ DayAndMonthUtil.december();
		} else if (month.equals(DayAndMonthUtil.february())) {
			returnValue = messages.datenumber(31) + " "
					+ DayAndMonthUtil.january();
		} else if (month.equals(DayAndMonthUtil.march())) {
			if (Integer.parseInt(year) % 4 == 0) {
				returnValue = messages.datenumber(29) + " "
						+ DayAndMonthUtil.february();
			} else {
				returnValue = messages.datenumber(28) + " "
						+ DayAndMonthUtil.february();
			}

		} else if (month.equals(DayAndMonthUtil.april())) {
			returnValue = messages.datenumber(31) + " "
					+ DayAndMonthUtil.march();
		} else if (month.equals(DayAndMonthUtil.may_full())) {
			returnValue = messages.datenumber(30) + " "
					+ DayAndMonthUtil.april();
		} else if (month.equals(DayAndMonthUtil.june())) {
			returnValue = DayAndMonthUtil.april() + " "
					+ DayAndMonthUtil.may_full();
		} else if (month.equals(DayAndMonthUtil.july())) {
			returnValue = messages.datenumber(30) + " "
					+ DayAndMonthUtil.june();
		} else if (month.equals(DayAndMonthUtil.august())) {
			returnValue = messages.datenumber(31) + " "
					+ DayAndMonthUtil.july();
		} else if (month.equals(DayAndMonthUtil.september())) {
			returnValue = messages.datenumber(31) + " "
					+ DayAndMonthUtil.august();
		} else if (month.equals(DayAndMonthUtil.october())) {
			returnValue = messages.datenumber(30) + " "
					+ DayAndMonthUtil.september();
		} else if (month.equals(DayAndMonthUtil.november())) {
			returnValue = messages.datenumber(31) + " "
					+ DayAndMonthUtil.october();
		} else if (month.equals(DayAndMonthUtil.december())) {
			returnValue = messages.datenumber(31) + " "
					+ DayAndMonthUtil.november();
		}

		return returnValue;

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// it is not using any where
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return messages.conversionDate();
	}

	@Override
	public void setFocus() {
		this.monthCombo.setFocus();

	}

}
