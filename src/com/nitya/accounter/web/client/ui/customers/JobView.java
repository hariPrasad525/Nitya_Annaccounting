package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientJob;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.CustomerCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class JobView extends BaseView<ClientJob> {

	private TextItem jobNameText;
	private List<String> jobstatusList;
	private SelectCombo jobstatusCombo;
	private DateField startDate, projectEndDate, endDate;
	private DynamicForm form, jobForm;
	private CheckboxItem statusCheck;
	CustomerCombo customerCombo;
	private ClientCustomer customer;

	public JobView() {
		super();
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("JobView");
		createControls();
	}

	@Override
	public void initData() {
		if (data == null) {
			setData(new ClientJob());
		}
		jobNameText.setValue(data.getName());
		jobstatusCombo.setComboItem(data.getJobStatus());
		startDate.setEnteredDate(data.getStartDate());
		projectEndDate.setEnteredDate(data.getProjectEndDate());
		endDate.setEnteredDate(data.getEndDate());
		statusCheck.setValue(data.isActive());
		if (getCustomer() != null) {
			customerCombo.setComboItem(getCustomer());
		} else {
			customerCombo.setComboItem(getCompany().getCustomer(
					data.getCustomer()));
		}
		super.initData();
	}

	private void createControls() {

		StyledPanel mainVlay = new StyledPanel("mainVlay");
		Label titleLabel = new Label(messages.job());
		titleLabel.setStyleName("label-title");

		jobForm = new DynamicForm("jobForm");
		form = new DynamicForm("form");

		jobNameText = new TextItem(messages.jobName(), "jobNameText");
		jobNameText.setRequired(true);
		jobNameText.setEnabled(!isInViewMode());

		customerCombo = new CustomerCombo(Global.get().Customer());
		customerCombo.setRequired(true);
		customerCombo.setEnabled(!isInViewMode());

		jobstatusCombo = createJobStatusSelectItem();

		startDate = new DateField(messages.startDate(), "startDate");
		ClientFinanceDate start_date = new ClientFinanceDate();
		start_date.setDay(start_date.getDay());
		startDate.setDatethanFireEvent(start_date);
		startDate.setEnabled(!isInViewMode());
		projectEndDate = new DateField(messages.projectendDate(),
				"projectEndDate");
		ClientFinanceDate projectEnd_date = new ClientFinanceDate();
		projectEnd_date.setDay(projectEnd_date.getDay());
		projectEndDate.setDatethanFireEvent(projectEnd_date);
		projectEndDate.setEnabled(!isInViewMode());
		endDate = new DateField(messages.endDate(), "endDate");
		ClientFinanceDate end_date = new ClientFinanceDate();
		end_date.setDay(end_date.getDay());
		endDate.setDatethanFireEvent(end_date);
		endDate.setEnabled(!isInViewMode());
		statusCheck = new CheckboxItem(messages.active(), "statusCheck");
		statusCheck.setValue(true);
		statusCheck.setEnabled(!isInViewMode());

		jobForm.add(jobNameText, statusCheck, jobstatusCombo, customerCombo);
		form.add(startDate, projectEndDate, endDate);

		StyledPanel leftVLay = new StyledPanel("leftVLay");

		leftVLay.add(jobForm);

		StyledPanel rightVLay = new StyledPanel("rightVLay");
		rightVLay.add(form);

		StyledPanel contHLay = new StyledPanel("contHLay");

		mainVlay.add(titleLabel);

		StyledPanel topHLay = getTopLayout();
		if (topHLay != null) {
			topHLay.add(leftVLay);
			topHLay.add(rightVLay);
			mainVlay.add(topHLay);
		} else {
			mainVlay.add(leftVLay);
			mainVlay.add(rightVLay);
		}

		mainVlay.add(contHLay);
		// mainVlay.setWidth("100%");
		this.add(mainVlay);
	}

	protected StyledPanel getTopLayout() {
		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.addStyleName("main-panel");
		return topHLay;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	private void save() {

	}

	@Override
	public void onEdit() {

		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));

				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.JOB, data.getID(),
				editCallBack);

	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		jobNameText.setEnabled(!isInViewMode());
		statusCheck.setEnabled(!isInViewMode());
		jobstatusCombo.setEnabled(!isInViewMode());
		startDate.setEnabled(!isInViewMode());
		endDate.setEnabled(!isInViewMode());
		projectEndDate.setEnabled(!isInViewMode());
		customerCombo.setEnabled(!isInViewMode());
		super.onEdit();

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			ClientJob job = (ClientJob) result;
			if (getMode() == EditMode.CREATE) {
				// job.setBalance(job.getOpeningBalance());
			}
			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}
	}

	@Override
	public ClientJob saveView() {
		ClientJob saveView = super.saveView();
		if (saveView != null) {
			updateData();
		}
		return saveView;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		result.add(jobForm.validate());

		return result;
	}

	private void updateData() {

		data.setJobName(jobNameText.getValue());
		data.setJobStatus(jobstatusCombo.getSelectedValue());
		if (customerCombo.getSelectedValue() != null) {
			data.setCustomer(customerCombo.getSelectedValue().getID());
		}
		data.setStartDate(startDate.getEnteredDate());
		data.setProjectEndDate(projectEndDate.getEnteredDate());
		data.setEndDate(endDate.getEnteredDate());
		data.setActive(statusCheck.isChecked());

	}

	@Override
	protected String getViewTitle() {
		return messages.job();
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

	public SelectCombo createJobStatusSelectItem() {
		jobstatusList = new ArrayList<String>();
		String jobstatusArray[] = new String[] { messages.none(),
				messages.pending(), messages.awarded(), messages.inprogress(),
				messages.closed(), messages.notAwarded() };

		for (int i = 0; i < jobstatusArray.length; i++) {
			jobstatusList.add(jobstatusArray[i]);
		}

		final SelectCombo jobStatusSelect = new SelectCombo(
				messages.jobStatus());

		jobStatusSelect.setRequired(true);
		jobStatusSelect.initCombo(jobstatusList);
		jobStatusSelect.setDefaultToFirstOption(true);
		messages.none();

		jobStatusSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						jonStatusSelected(jobStatusSelect.getSelectedValue());

					}

				});
		jobStatusSelect.setEnabled(!isInViewMode());

		return jobStatusSelect;

	}

	private void jonStatusSelected(String selectedValue) {
		jobstatusCombo.setSelected(selectedValue);
	}

	public ClientCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer;
	}
}
