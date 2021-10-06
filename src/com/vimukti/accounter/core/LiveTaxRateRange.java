package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class LiveTaxRateRange implements IAccounterServerCore, Lifecycle {
	
	private static final long serialVersionUID = 1L;
	
	private int version;
	
	long id;
	
	LiveTaxRate taxRate;

	double start;
	double end;
	double rate;
	boolean plusMore = false;

	public boolean isPlusMore() {
		return plusMore;
	}

	public void setPlusMore(boolean plusMore) {
		this.plusMore = plusMore;
	}

	transient private boolean isOnSaveProccessed;

	public LiveTaxRate getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(LiveTaxRate taxRate) {
		this.taxRate = taxRate;
	}

	public double getStart() {
		return start;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
      this.version = version;		
	}

	@Override
	public boolean onSave(Session s) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {
		return true;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {
		return true;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject, boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		
	}

	@Override
	public void selfValidate() throws AccounterException {
		
	}

}
