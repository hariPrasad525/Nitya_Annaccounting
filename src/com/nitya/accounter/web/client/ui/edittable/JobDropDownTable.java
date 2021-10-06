package com.nitya.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientJob;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.customers.NewJobAction;

public class JobDropDownTable extends AbstractDropDownTable<ClientJob> {
	private long customerId;

	public JobDropDownTable() {
		super(getjobs(0), true);
	}

	@Override
	public List<ClientJob> getTotalRowsData() {
		return getjobs(getCustomerId());
	}

	public static List<ClientJob> getjobs(long customerId) {
		List<ClientJob> clientJobs = new ArrayList<ClientJob>();
		ClientCustomer customer = Accounter.getCompany()
				.getCustomer(customerId);
		if (customer != null) {
			ArrayList<ClientJob> jobs = customer.getJobs();
			for (ClientJob clientJob : jobs) {
				if (clientJob.isActive()) {
					clientJobs.add(clientJob);
				}
			}
		}
		return clientJobs;
	}

	@Override
	protected ClientJob getAddNewRow() {
		ClientJob clientJob = new ClientJob();
		clientJob.setJobName("Add New Job");
		return clientJob;
	}

	@Override
	public void initColumns() {
		this.addColumn(new TextColumn<ClientJob>() {

			@Override
			public String getValue(ClientJob object) {
				return object.getJobName();
			}
		});

	}

	@Override
	protected boolean filter(ClientJob t, String string) {
		return t.getJobName().toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientJob value) {
		return value.getJobName();
	}

	@Override
	protected Class<?> getType() {
		return ClientJob.class;
	}

	@Override
	protected void addNewItem(String text) {

		final ClientCompany company = Accounter.getCompany();
		if (getCustomerId() != 0) {
			ClientCustomer customer = company.getCustomer(getCustomerId());
			NewJobAction action = new NewJobAction(customer);
			action.setCallback(new ActionCallback<ClientJob>() {

				@Override
				public void actionResult(ClientJob result) {
					if (result.getDisplayName() != null) {
						selectRow(result);
					}
				}
			});
			action.run(null, true);
			// NewJobDialog jobDialog = new NewJobDialog(null, messages.job(),
			// "",
			// customer);
			// jobDialog.addSuccessCallback(newJobtHandler);

		}
	}

	@Override
	protected void addNewItem() {
		addNewItem("");

	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
		reInitData();
	}

}
