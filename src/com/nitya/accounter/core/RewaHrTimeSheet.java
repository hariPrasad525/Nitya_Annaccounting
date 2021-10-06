package com.nitya.accounter.core;

import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

public class RewaHrTimeSheet extends CreatableObject implements IAccounterServerCore,
INamedObject {

	
	private static final long serialVersionUID = 1L;
	
	private String projectName;
	private int timeSheetId;
	private String projectActivityName;
	private String description;
//	private Date reportedDate;
	private String reportedDate;
	private int duration;
	private int extraDuration;
	private String attachment;
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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

	public int getDuration() {
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

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}
