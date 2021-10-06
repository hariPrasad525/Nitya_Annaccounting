package com.nitya.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.company.DepreciationAction;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.widgets.DateUtills;

public class StartDateDialog extends BaseDialog {

	protected List<ClientFinanceDate> startDateList;
	private ListBox dateBox;
	private String initialDate;
	protected boolean isDateChanged;
	private final DateTimeFormat format = DateTimeFormat.getFormat(getCompany()
			.getPreferences().getDateFormat());

	public StartDateDialog() {
		super(messages.startDate(), "");
		this.getElement().setId("StartDateDialog");
		getStartdates();
		Timer timer = new Timer() {
			@Override
			public void run() {
				createControl();
				ViewManager.getInstance().showDialog(StartDateDialog.this);
			}
		};
		timer.schedule(300);
	}

	private String getStartDateString() {
		ClientFinanceDate startDate = new ClientFinanceDate(Accounter
				.getCompany().getPreferences().getDepreciationStartDate());
		String formatDate = format.format(startDate.getDateAsObject());

		return formatDate;
	}

	private void createControl() {
		Label introLabel = new Label(messages.currentStartDateIs()
				+ getStartDateString());

		Label infoLabel = new Label();
		infoLabel.setText(messages.startDateForManagingFixedAsset());
		HTML prefixText = new HTML(messages.newStartDate());
		dateBox = new ListBox();
		dateBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String changedDate = dateBox.getValue(dateBox
						.getSelectedIndex());
				if (initialDate != null && changedDate != null
						&& initialDate.equals(changedDate)) {
					isDateChanged = false;
				} else {
					isDateChanged = true;
					initialDate = changedDate;
				}
			}
		});
		fillDateCombo();
		initialDate = dateBox.getValue(dateBox.getSelectedIndex());

		// okbtn.setWidth("50px");
		// cancelBtn.setWidth("80px");
		// footerLayout.setCellWidth(okbtn, "80%");

		StyledPanel contentPanel = new StyledPanel("contentPanel");
		contentPanel.add(introLabel);
		contentPanel.add(infoLabel);
		contentPanel.add(prefixText);
		contentPanel.add(dateBox);
		bodyLayout.add(contentPanel);
		// setWidth("350px");
		// mainPanel.setSpacing(20);
	}

	private void fillDateCombo() {
		if (startDateList != null && (!startDateList.isEmpty())) {
			for (ClientFinanceDate date : startDateList) {
				dateBox.addItem(DateUtills.getDateAsString(date));
			}
		} else {
			dateBox.addItem(getStartDateString());
		}

		setDefaultStartDate();
	}

	public void setDefaultStartDate() {
		String startDate = getStartDateString();
		for (int i = 0; i < dateBox.getItemCount(); i++) {
			if (dateBox.getItemText(i).equalsIgnoreCase(startDate)) {
				dateBox.setItemSelected(i, true);
				return;
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
		if (isDateChanged)
			changeStartDate();
		removeFromParent();

	}

	private void changeStartDate() {
		String dateString = dateBox.getValue(dateBox.getSelectedIndex());
		ClientFinanceDate date = DateUtills.getDateFromString(dateString);

		AccounterAsyncCallback callBack = new AccounterAsyncCallback() {

			@Override
			public void onException(AccounterException caught) {
				saveFailed(caught);
				return;

			}

			@Override
			public void onResultSuccess(Object result) {
				History.newItem(new DepreciationAction().getHistoryToken());
				// ActionFactory.getDepriciationAction().run(null, true);
			}

		};
		Accounter.createHomeService().changeDepreciationStartDateTo(
				date.getDate(), callBack);
	}

	protected void helpClick() {

		Accounter.showInformation(messages.W_111());

	}

	private void getStartdates() {
		AccounterAsyncCallback<ArrayList<ClientFinanceDate>> callBack = new AccounterAsyncCallback<ArrayList<ClientFinanceDate>>() {

			@Override
			public void onException(AccounterException caught) {
				saveFailed(caught);
				return;

			}

			@Override
			public void onResultSuccess(ArrayList<ClientFinanceDate> result) {
				startDateList = result;
			}

		};
		Accounter.createHomeService().getFinancialYearStartDates(callBack);

	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}

}
