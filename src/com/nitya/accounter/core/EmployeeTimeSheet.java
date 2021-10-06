package com.nitya.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

public class EmployeeTimeSheet  extends CreatableObject implements IAccounterServerCore,
INamedObject{

	private static final long serialVersionUID = 1L;
	
	private String consultantName;
	private long companyId;
	private long itemId;
	private long toDate;
	private long fromDate;
	private long hours;
	private boolean isBilled;

	Set<Attachment> attachments = new HashSet<Attachment>();

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	
	public String getConsultantName() {
		return consultantName;
	}

	public void setConsultantName(String consultantName) {
		this.consultantName = consultantName;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public long getToDate() {
		return toDate;
	}

	public void setToDate(long toDate) {
		this.toDate = toDate;
	}

	public long getFromDate() {
		return fromDate;
	}

	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}

	public long getHours() {
		return hours;
	}

	public void setHours(long hours) {
		this.hours = hours;
	}

	public boolean getIsBilled() {
		return isBilled;
	}

	public void setIsBilled(boolean isBilled) {
		this.isBilled = isBilled;
	}

	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getObjType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject, boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub
		
	}

	
}
