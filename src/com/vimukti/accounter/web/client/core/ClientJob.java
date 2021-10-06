package com.vimukti.accounter.web.client.core;

public class ClientJob implements IAccounterCore {

	private static final long serialVersionUID = 1L;

	private long customer;

	private String jobName;

	private String jobStatus;

	private ClientFinanceDate startDate;

	private ClientFinanceDate projectEndDate;

	private boolean isActive = Boolean.TRUE;

	private ClientFinanceDate endDate;
	long id;
	private int version;

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return getJobName();
	}

	@Override
	public String getDisplayName() {
		return this.jobName;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.JOB;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public long getCustomer() {
		return customer;
	}

	public void setCustomer(long customer) {
		this.customer = customer;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public ClientFinanceDate getStartDate() {
		return startDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(ClientFinanceDate projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ClientJob)) {
			return false;
		}

		ClientJob job = (ClientJob) obj;
		return job.getID() == this.getID()
				&& job.getJobName().equals(this.jobName);
	}
}
