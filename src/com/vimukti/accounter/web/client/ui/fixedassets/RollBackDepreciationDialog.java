package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.DepreciationAction;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public class RollBackDepreciationDialog extends BaseDialog {

	protected ClientFinanceDate lastDepreciationDate;
	protected List<String> depreciationDate = new ArrayList<String>();
	private ListBox dateBox;

	public RollBackDepreciationDialog(ClientFinanceDate lastDepreciationDate2) {
		super(messages.rollBackDepreciation(), "");
		this.getElement().setId("RollBackDepreciationDialog");
		this.addStyleName("depreciation_table");
		this.lastDepreciationDate = lastDepreciationDate2;
		getAllDepreciationDates();
		createControl();
	}

	private void createControl() {

		StyledPanel typeForm = new StyledPanel("typeForm");
		// typeForm.setWidth("100%");

		StyledPanel contentPanel = new StyledPanel("contentPanel");
		contentPanel.setStyleName("margin-b");
		if (lastDepreciationDate != null) {
			String lastDepreciationDateString = UIUtils
					.getDateByCompanyType(lastDepreciationDate);
			Label introLabel = new Label(messages.lastDepreciation() + "  "
					+ lastDepreciationDateString);
			contentPanel.add(introLabel);
		}
		HTML prefixText = new HTML(messages.rollbackDepreciationTo());

		dateBox = new ListBox();
		for (String date : depreciationDate) {
			dateBox.addItem(date);
		}

		// okbtn.setWidth("50px");
		// cancelBtn.setWidth("80px");

		// footerLayout.setCellHorizontalAlignment(okbtn,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		StyledPanel mainVLay = new StyledPanel("mainVLay");
		// mainVLay.setSize("100%", "100%");
		mainVLay.add(contentPanel);
		mainVLay.add(prefixText);
		mainVLay.add(dateBox);

		setBodyLayout(mainVLay);
		// footerLayout.getElement().getStyle().setMarginLeft(29, Unit.PCT);
		// setWidth("300px");
		// mainPanel.setSpacing(3);

	}

	private void getLastDepreciationDate() {
		AccounterAsyncCallback<ClientFinanceDate> callBack = new AccounterAsyncCallback<ClientFinanceDate>() {

			@Override
			public void onException(AccounterException caught) {
				saveFailed(caught);
				return;

			}

			@Override
			public void onResultSuccess(ClientFinanceDate result) {
				lastDepreciationDate = result;
				getAllDepreciationDates();
			}

		};
		Accounter.createHomeService().getDepreciationLastDate(callBack);

	}

	/*
	 * This method will calculate the months from depreciation start date to
	 * depreciation last date
	 */

	private void getAllDepreciationDates() {

		ClientFinanceDate depreciationStartDate = new ClientFinanceDate(
				getCompany().getPreferences().getDepreciationStartDate());
		ClientFinanceDate tempDate = new ClientFinanceDate(
				depreciationStartDate.getDate());

		depreciationDate = new ArrayList<String>();

		if (lastDepreciationDate != null) {

			ClientFinanceDate tempLastDate = new ClientFinanceDate(
					lastDepreciationDate.getDate());
			tempLastDate.setDay(1);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, depreciationStartDate.getMonth() - 1);
			cal.set(Calendar.YEAR, depreciationStartDate.getYear() - 1900);
			while (depreciationStartDate.before(lastDepreciationDate)) {
				cal.set(Calendar.DAY_OF_MONTH, 1);
				depreciationDate.add(DateUtills.getDateAsString(cal.getTime()));
				cal.add(Calendar.MONTH, 1);
				if (depreciationStartDate.getMonth() == 12) {
					depreciationStartDate.setYear(lastDepreciationDate
							.getYear());
				}
				depreciationStartDate
						.setMonth(depreciationStartDate.getMonth() == 12 ? 1
								: depreciationStartDate.getMonth() + 1);
			}

		}

	}

	@Override
	protected void processCancel() {
		removeFromParent();
	}

	/**
	 * Called when Ok button clicked
	 */
	@Override
	protected void processOK() {
		rollBackDepreciation();
		removeFromParent();

	}

	private void rollBackDepreciation() {
		String dateString = dateBox.getValue(dateBox.getSelectedIndex());
		ClientFinanceDate date = DateUtills.getDateFromString(dateString);

		AccounterAsyncCallback<Boolean> callBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				saveFailed(caught);
				return;

			}

			@Override
			public void onResultSuccess(Boolean result) {
				History.newItem(new DepreciationAction().getHistoryToken());
				new DepreciationAction().run(null, false);
			}

		};
		Accounter.createHomeService().rollBackDepreciation(date.getDate(),
				callBack);
	}

	protected void helpClick() {

		Accounter.showInformation(messages.W_111());

	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
