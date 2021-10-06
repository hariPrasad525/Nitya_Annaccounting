package com.nitya.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MigrationStatus implements IsSerializable, Serializable {

	private static final long serialVersionUID = 1L;

	public static final int IN_PROGRESS = 1;
	public static final int FAILED = 2;
	public static final int FINISED = 3;

	public enum Status {
		IN_PROGRESS, FAILED, FINISED
	}

	private String info;

	private int status;

	private long companyId;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

}
