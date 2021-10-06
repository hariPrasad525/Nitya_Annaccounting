package com.nitya.accounter.web.client.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.web.client.core.reports.BaseReport;

public class ClientEmployeeTimeSheet extends BaseReport implements IAccounterCore,
	 
		IAccountable
  {

	
	private static final long serialVersionUID = 1L;
	
	private boolean isBilled;
	public boolean isBilled() {
		return isBilled;
	}

	public void setBilled(boolean isBilled) {
		this.isBilled = isBilled;
	}
	
	private Set<ClientAttachment> attachments = new HashSet<ClientAttachment>();
	private String consultantName;
	private long fromDate;
	private long toDate;
	private long hours;
	private long id;
	private long companyId;
	private long itemId;
	private List<ClientAttachment> uploadFile;

	public Set<ClientAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<ClientAttachment> attachments) {
		this.attachments = attachments;
	}
	
	public List<ClientAttachment> getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(List<ClientAttachment> uploadFile) {
		this.uploadFile = uploadFile;
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

	public String getConsultantName() {
		return consultantName;
	}

	public void setConsultantName(String consultantName) {
		this.consultantName = consultantName;
	}

	public long getFromDate() {
		return fromDate;
	}

	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}

	public long getToDate() {
		return toDate;
	}

	public void setToDate(long toDate) {
		this.toDate = toDate;
	}

	public long getHours() {
		return hours;
	}

	public void setHours(long hours) {
		this.hours = hours;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	   
	   
	  @Override
	    public boolean equals(Object obj) {
	        if (obj instanceof ClientEmployeeTimeSheet) {
	        	ClientEmployeeTimeSheet item = (ClientEmployeeTimeSheet) obj;
	            if (this.getID() == item.getID())
	                return true;
	        }
	        return false;
	    }

	    public ClientEmployeeTimeSheet clone() {
	    	ClientEmployeeTimeSheet item = this.clone();
	        return item;

	    }
	    
	    
	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
	
		
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		
		return consultantName;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		  return AccounterCoreType.EMPLOYEE_TIME_SHEET;
	}

	@Override
	public void setID(long id) {
		this.id = id;
		
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return this.id;
	}

}