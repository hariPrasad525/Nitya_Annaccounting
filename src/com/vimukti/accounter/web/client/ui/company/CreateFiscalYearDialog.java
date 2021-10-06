package com.vimukti.accounter.web.client.ui.company;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class CreateFiscalYearDialog extends BaseDialog<ClientFiscalYear> {

	private LabelItem createFiscalYearLabel;
	private LabelItem descriptionLabel;
	private DateItem startOfFiscalYear;
	private DateItem endOfFiscalYear;
	private DynamicForm dynamicForm;
	private StyledPanel mainVlayout;
	private FiscalYearListGrid listOfFiscalYear;
	private String title;

	public CreateFiscalYearDialog(String title, String desc,
			FiscalYearListGrid listOfperiods) {
		super(title, desc);
		this.getElement().setId("CreateFiscalYearDialog");
		this.title = title;
		this.listOfFiscalYear = listOfperiods;
		createControls();
		if (title.equalsIgnoreCase(messages.editFiscalYear())) {
			initData();
		} else {
			initNewFiscalYearData();
		}
	}

	private void initNewFiscalYearData() {
		ClientFinanceDate startdate = null, presentDate, endDate;
		if (listOfFiscalYear.getRecords() != null
				&& listOfFiscalYear.getRecords().size() != 0) {
			for (ClientFiscalYear fiscalYear : listOfFiscalYear.getRecords()) {
				if (startdate == null)
					startdate = fiscalYear.getEndDate();
				if (startdate.before(fiscalYear.getEndDate()))
					startdate = fiscalYear.getEndDate();
			}
			startdate.setDay(startdate.getDay() + 1);
			endDate = new ClientFinanceDate(startdate.getDate());
			endDate.setYear(endDate.getYear() + 1);
			endDate.setDay(endDate.getDay() - 1);
		} else {
			presentDate = new ClientFinanceDate();
			startdate = new ClientFinanceDate(presentDate.getYear(), 01, 01);
			endDate = new ClientFinanceDate(presentDate.getYear(), 12, 31);
		}

		startOfFiscalYear.setDatethanFireEvent(startdate);
		endOfFiscalYear.setDatethanFireEvent(endDate);

	}

	private void initData() {
		ClientFiscalYear fiscalYear = listOfFiscalYear.getSelection();
		startOfFiscalYear.setDatethanFireEvent(fiscalYear.getStartDate());
		endOfFiscalYear.setDatethanFireEvent(fiscalYear.getEndDate());
		// if (listOfFiscalYear.getRecordByIndex(0) == listOfFiscalYear
		// .getSelection()
		// && listOfFiscalYear.getRecords().size() == 1) {
		// startOfFiscalYear.setDisabled(true);
		// endOfFiscalYear.setDisabled(false);
		// }
		// if (listOfFiscalYear
		// .getRecordByIndex(listOfFiscalYear.getRowCount() - 1) ==
		// listOfFiscalYear
		// .getSelection()
		// && listOfFiscalYear.getRecords().size() != 1) {
		// startOfFiscalYear.setDisabled(true);
		// endOfFiscalYear.setDisabled(false);
		// }
		// if (listOfFiscalYear.getRecordByIndex(0) == listOfFiscalYear
		// .getSelection()
		// && listOfFiscalYear.getRecords().size() != 1) {
		// startOfFiscalYear.setDisabled(true);
		// endOfFiscalYear.setDisabled(true);
		// }

	}

	private void createControls() {
		createFiscalYearLabel = new LabelItem(messages.createFascalYear(),
				"createFiscalYearLabel");
		descriptionLabel = new LabelItem(messages.enterAppropriateFiscalYear(),
				"descriptionLabel");
		startOfFiscalYear = new DateItem(messages.startOfFiscalYear(),
				"startOfFiscalYear");
		startOfFiscalYear.setRequired(true);
		// startOfFiscalYear.setDisabled(true);
		endOfFiscalYear = new DateItem(messages.endOfFiscalYear(),
				"endOfFiscalYear");
		endOfFiscalYear.setRequired(true);
		dynamicForm = new DynamicForm("dynamicForm");
		dynamicForm.add(startOfFiscalYear, endOfFiscalYear);
		mainVlayout = new StyledPanel("mainVlayout");
		// mainVlayout.add(createFiscalYearLabel);
		mainVlayout.add(descriptionLabel);
		mainVlayout.add(dynamicForm);
		setBodyLayout(mainVlayout);

	}

	protected ClientFiscalYear getEditFiscalYear() {
		ClientFiscalYear selectedFiscalYear = listOfFiscalYear.getSelection();
		if (selectedFiscalYear != null) {
			selectedFiscalYear.setStartDate(startOfFiscalYear.getDate()
					.getDate());
			selectedFiscalYear.setEndDate(endOfFiscalYear.getDate().getDate());
		}
		return selectedFiscalYear;
	}

	public ClientFiscalYear getNewFiscalYear() {
		ClientFiscalYear clientFiscalYear = new ClientFiscalYear();
		clientFiscalYear.setStartDate(startOfFiscalYear.getDate().getDate());
		clientFiscalYear.setEndDate(endOfFiscalYear.getDate().getDate());
		clientFiscalYear.setStatus(ClientFiscalYear.STATUS_OPEN);
		return clientFiscalYear;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		listOfFiscalYear.removeAllRecords();
		for (ClientFiscalYear clientFiscalYear : Accounter.getCompany()
				.getFiscalYears()) {
			listOfFiscalYear.addData(clientFiscalYear);
		}
		// if (Utility.getObject(this.listOfFiscalYear.getRecords(), object
		// .getID()) != null)
		// listOfFiscalYear.updateData((ClientFiscalYear) object);
		// else
		// listOfFiscalYear.addData((ClientFiscalYear) object);
	}

	@Override
	public void saveFailed(AccounterException exception) {

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (startOfFiscalYear.getDateBox().getValue().isEmpty()) {
			result.addError(startOfFiscalYear, messages.startFiscalHTML());
		} else if (endOfFiscalYear.getDateBox().getValue().isEmpty()) {
			result.addError(startOfFiscalYear, messages.endFiscalHTML());
		} else if (endOfFiscalYear.getDate()
				.before(startOfFiscalYear.getDate())) {
			result.addError(this, messages.fiscalStartEndCompreHTML());
		} else if (checkFiscalYearExists()) {
			result.addError(this, messages.fiscalYearAlreadyExists());
		} else if (checkStartDateExists()) {
			result.addError(this, messages.fiscalYearStartDateAlreadyExists());
		} else if (checkEndDateExists()) {
			result.addError(this, messages.fiscalYearEndDateAlreadyExists());
		}
		return result;
	}

	private boolean checkEndDateExists() {
		for (ClientFiscalYear fiscalyear : listOfFiscalYear.getRecords()) {
			if (endOfFiscalYear.getDate().equals(fiscalyear.getEndDate())) {
				return true;
			}
		}
		return false;
	}

	private boolean checkStartDateExists() {
		for (ClientFiscalYear fiscalyear : listOfFiscalYear.getRecords()) {
			if (startOfFiscalYear.getDate().equals(fiscalyear.getStartDate())) {
				return true;
			}
		}
		return false;
	}

	private boolean checkFiscalYearExists() {
		for (ClientFiscalYear fiscalyear : listOfFiscalYear.getRecords()) {
			if (startOfFiscalYear.getDate().equals(fiscalyear.getStartDate())
					&& endOfFiscalYear.getDate()
							.equals(fiscalyear.getEndDate())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean onOK() {
		ClientFiscalYear fiscalYear = null;
		if (title.equalsIgnoreCase(messages.editFiscalYear())) {
			fiscalYear = getEditFiscalYear();
		} else if (title.equalsIgnoreCase(messages.createFascalYear())) {
			fiscalYear = getNewFiscalYear();
		}
		saveOrUpdate(fiscalYear);
		return true;
	}

	@Override
	public void setFocus() {
		startOfFiscalYear.setFocus();

	}
}
