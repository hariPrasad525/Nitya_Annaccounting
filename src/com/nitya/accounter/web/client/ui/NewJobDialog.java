package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientJob;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.combo.CustomerCombo;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class NewJobDialog extends BaseDialog<ClientJob> {

	private DateField startDate, projectEndDate, endDate;
	private List<String> jobstatusList;
	private ValueCallBack<ClientJob> successCallback;
	private TextItem jobNameText;
	private ClientJob job;

	private CheckboxItem statusCheck;

	private CustomerCombo customerCombo;

	private SelectCombo jobstatusCombo;
	private DynamicForm jobForm;
	private ClientCustomer customer;

	public NewJobDialog(ClientJob job, String title, String desc,
			ClientCustomer customer) {
		super(title, desc);
		this.getElement().setId("NewJobDialog");
		// setWidth("300px");
		this.job = job;
		this.customer = customer;
		createControls();
	}

	private void createControls() {

		StyledPanel mainLayout = new StyledPanel("mainLayout");

		jobForm = new DynamicForm("jobForm");
		jobNameText = new TextItem(messages.jobName(), "jobNameText");
		jobNameText.setToolTip(messages.jobName());
		jobNameText.setRequired(true);

		customerCombo = new CustomerCombo(Global.get().Customer());
		customerCombo.setRequired(true);
		customerCombo.setSelected(customer != null ? customer.getName() : "");
		customerCombo.setEnabled(false);

		jobstatusCombo = createJobStatusSelectItem();

		startDate = new DateField(messages.startDate(), "startDate");
		ClientFinanceDate start_date = new ClientFinanceDate();
		start_date.setDay(start_date.getDay());
		startDate.setDatethanFireEvent(start_date);

		projectEndDate = new DateField(messages.projectendDate(),
				"projectEndDate");
		ClientFinanceDate projectEnd_date = new ClientFinanceDate();
		projectEnd_date.setDay(projectEnd_date.getDay());
		projectEndDate.setDatethanFireEvent(projectEnd_date);

		endDate = new DateField(messages.endDate(), "endDate");
		ClientFinanceDate end_date = new ClientFinanceDate();
		end_date.setDay(end_date.getDay());
		endDate.setDatethanFireEvent(end_date);

		statusCheck = new CheckboxItem(messages.active(), "statusCheck");
		statusCheck.setValue(true);

		jobForm.add(jobNameText, jobstatusCombo, customerCombo, statusCheck,
				startDate, projectEndDate, endDate);

		mainLayout.add(jobForm);

		setBodyLayout(mainLayout);

		ViewManager.getInstance().showDialog(this);
	}

	/**
	 * 
	 * @return
	 */
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

		return jobStatusSelect;

	}

	@Override
	protected boolean onOK() {
		if (successCallback != null) {
			successCallback.execute(createJobObj());
		}
		return true;
	}

	/**
	 * 
	 * @param newjobHandler
	 */
	public void addSuccessCallback(ValueCallBack<ClientJob> newjobHandler) {
		this.successCallback = newjobHandler;
	}

	private ClientJob createJobObj() {
		if (job == null) {
			job = new ClientJob();
		}
		job.setJobName(jobNameText.getValue());
		job.setJobStatus(jobstatusCombo.getSelectedValue());
		job.setCustomer(customerCombo.getSelectedValue().getID());
		job.setStartDate(startDate.getEnteredDate());
		job.setProjectEndDate(projectEndDate.getEnteredDate());
		job.setEndDate(endDate.getEnteredDate());
		job.setActive(statusCheck.getValue());
		return job;
	}

	@Override
	public void setFocus() {
		jobNameText.setFocus();
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = super.validate();
		String itemName = jobNameText.getValue();
		if (itemName == null || itemName.equals("")) {
			result.addError(jobNameText, messages.pleasEnterJobName());
		} else {
			ArrayList<ClientJob> jobs = customer.getJobs();
			for (ClientJob clientJob : jobs) {
				if (clientJob.getName().toLowerCase()
						.equals(itemName.toLowerCase())
						&& (job == null ? true : job.getID() != clientJob
								.getID())) {
					result.addError(jobNameText, messages.alreadyExist());
				}
			}
		}
		return result;
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
