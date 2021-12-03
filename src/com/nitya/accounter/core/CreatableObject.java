package com.nitya.accounter.core;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

public abstract class CreatableObject implements Lifecycle {

	private long id;

	private User createdBy;
	private User lastModifier;
	private Timestamp createdDate;
	private Timestamp lastModifiedDate;
	private int version;
	private Company company;
	transient public boolean isOnSaveProccessed;

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getCreatedBy() {
		return this.createdBy;
	}

	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	public User getLastModifier() {
		return this.lastModifier;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (id == 0 && this.createdBy == null) {
			this.createdBy = AccounterThreadLocal.get();
			this.createdDate = AccounterThreadLocal.currentTime();
		}
		this.lastModifier = AccounterThreadLocal.get();
		this.lastModifiedDate = AccounterThreadLocal.currentTime();
		if(this.lastModifier == null)
		{
			this.lastModifier = this.createdBy;
		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		this.lastModifier = AccounterThreadLocal.get();
		this.lastModifiedDate = AccounterThreadLocal.currentTime();
		return false;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// if (AccounterThreadLocal.get() == null
		// || AccounterThreadLocal.get().getCompany() == null
		// || getCompany() == null) {
		// return;
		// }
		// if (getCompany().getId() != AccounterThreadLocal.get().getCompany()
		// .getId()) {
		// throw new RuntimeException("Illegal Access for the Object");
		// }
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
