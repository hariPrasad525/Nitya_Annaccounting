package com.nitya.accounter.web.client.core;

public class ClientRewaHrTimeSheet  implements IAccounterCore,

	IAccountable {

	
	private static final long serialVersionUID = 1L;

	private String projectName;
	private int timeSheetId;
	private String projectActivityName;
	private String description;
//	private Timestamp reportedDate;
	private String reportedDate;
	private int duration;
	private int extraDuration;
	private String employee_name;
	private String attachment;
	private int empId;
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public int getTimeSheetId() {
		return timeSheetId;
	}

	public void setTimeSheetId(int timeSheetId) {
		this.timeSheetId = timeSheetId;
	}

	public String getProjectActivityName() {
		return projectActivityName;
	}

	public void setProjectActivityName(String projectActivityName) {
		this.projectActivityName = projectActivityName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReportedDate() {
		return reportedDate;
	}

	public void setReportedDate(String reportedDate) {
		this.reportedDate = reportedDate;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	

	public int getExtraDuration() {
		return extraDuration;
	}

	public void setExtraDuration(int extraDuration) {
		this.extraDuration = extraDuration;
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
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return projectName;
	}

	@Override
	public AccounterCoreType getObjectType() {
		
		return AccounterCoreType.REWA_HR_TIME_SHEET;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return this.getTimeSheetId();
	}

	 @Override
	    public boolean equals(Object obj) {
	        if (obj instanceof ClientRewaHrTimeSheet) {
	        	ClientRewaHrTimeSheet item = (ClientRewaHrTimeSheet) obj;
	            if (this.getID() == item.getID())
	                return true;
	        }
	        return false;
	    }

	    public ClientRewaHrTimeSheet clone() {
	    	ClientRewaHrTimeSheet item = this.clone();
	        return item;

	    }

		@Override
		public int getType() {
			// TODO Auto-generated method stub
			return 0;
		}

		public String getAttachment() {
			return attachment;
		}

		public void setAttachment(String attachment) {
			this.attachment = attachment;
		}

		public int getEmpId() {
			return empId;
		}

		public void setEmpId(int empId) {
			this.empId = empId;
		}

	      
}
