package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class EstimatesByJob extends BaseReport implements IsSerializable,
		Serializable {

	private static final long serialVersionUID = 1L;

	private String customerName;

	private String jobName;

	private long estimateId;

	private int transactionType;

	private ClientFinanceDate estimateDate;

	private String num;

	private String memo;

	private double amount;

	private long jobId;

	private int estimateType;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(long estimateId) {
		this.estimateId = estimateId;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public ClientFinanceDate getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(ClientFinanceDate estimateDate) {
		this.estimateDate = estimateDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public int getEstimateType() {
		return estimateType;
	}

	public void setEstimateType(int estimateType) {
		this.estimateType = estimateType;
	}
}
