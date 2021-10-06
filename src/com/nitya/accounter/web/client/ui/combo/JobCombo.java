package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientJob;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.customers.NewJobAction;

public class JobCombo extends CustomCombo<ClientJob> {
	private ValueCallBack<ClientJob> newJobtHandler;
	private ClientCustomer customer;

	public JobCombo(String title, boolean isNewRequired) {
		super(title, isNewRequired, 1, "JobCombo");
	}

	@Override
	protected String getDisplayName(ClientJob object) {
		if (object != null)
			return object.getJobName() != null ? object.getJobName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientJob object, int col) {
		switch (col) {
		case 0:
			return object.getJobName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.job();
	}

	/**
	 * @param valueCallBack
	 */

	public void addNewJobHandler(ValueCallBack<ClientJob> newJobtHandler) {
		this.newJobtHandler = newJobtHandler;
	}

	@Override
	public void onAddNew() {
		NewJobAction action = new NewJobAction(customer);
		action.setCallback(new ActionCallback<ClientJob>() {

			@Override
			public void actionResult(ClientJob result) {
				if (result.getDisplayName() != null && result.isActive())
					addItemThenfireEvent(result);
			}
		});
		action.run(null, true);
		// NewJobDialog jobDialog = new NewJobDialog(null, messages.job(), "",
		// customer);
		// jobDialog.addSuccessCallback(newJobtHandler);
	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer;
		setComboItem(null);
		List<ClientJob> clientJobs = new ArrayList<ClientJob>();
		ArrayList<ClientJob> jobs = customer.getJobs();
		for (ClientJob clientJob : jobs) {
			if (clientJob.isActive()) {
				clientJobs.add(clientJob);
			}
		}
		initCombo(clientJobs);
	}
}
