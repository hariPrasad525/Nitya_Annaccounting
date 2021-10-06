package com.vimukti.accounter.core;

import java.util.Set;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Job extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Customer customer;

	private String jobName;

	private Job parent;

	private String jobStatus;

	private FinanceDate startDate;

	private FinanceDate projectEndDate;

	private FinanceDate endDate;

	private long jobType;

	private boolean isActive = Boolean.TRUE;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Job getParent() {
		return parent;
	}

	public void setParent(Job parent) {
		this.parent = parent;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public FinanceDate getStartDate() {
		return startDate;
	}

	public void setStartDate(FinanceDate startDate) {
		this.startDate = startDate;
	}

	public FinanceDate getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(FinanceDate projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public FinanceDate getEndDate() {
		return endDate;
	}

	public void setEndDate(FinanceDate endDate) {
		this.endDate = endDate;
	}

	@Override
	public String getName() {
		return jobName;
	}

	@Override
	public void setName(String name) {
		this.getName();
	}

	@Override
	public int getObjType() {
		return IAccounterCore.JOB;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public long getJobType() {
		return jobType;
	}

	public void setJobType(long jobType) {
		this.jobType = jobType;
	}

	@Override
	public void selfValidate() throws AccounterException {

		if (this.jobName == null || this.jobName.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().jobName());
		}
		if (this.jobStatus == null || this.jobStatus.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().jobStatus());
		}
		if (this.customer == null) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().customer());
		}

		Set<Job> jobs = getCompany().getJobs();
		for (Job job : jobs) {
			if (job.getName().equalsIgnoreCase(getJobName())
					&& (job.getID() != this.getID())) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_ALREADY_EXIST, Global
								.get().messages().jobName());
			}
		}

	}

}
