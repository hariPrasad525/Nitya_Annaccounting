package com.nitya.accounter.web.client.ui.edittable;

import com.nitya.accounter.web.client.core.ClientJob;

public abstract class JobColumn<T> extends ComboColumn<T, ClientJob> {

	JobDropDownTable jobs = new JobDropDownTable();

	@Override
	public AbstractDropDownTable<ClientJob> getDisplayTable(T row) {
		return jobs;
	}

	@Override
	public int getWidth() {
		return 100;
	}

	public void setcustomerId(long customerId) {
		jobs.setCustomerId(customerId);
	}

	@Override
	protected String getColumnName() {
		return messages.job();
	}

}
