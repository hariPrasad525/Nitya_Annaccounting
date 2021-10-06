package com.nitya.accounter.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public abstract class Command {
	MobileConstants constants;
	private Map<Object, Object> attributes = new HashMap<Object, Object>();
	private List<Requirement> requirements = new ArrayList<Requirement>();
	private boolean isDone;
	private String successMessage;
	private boolean canTrackRequirements = true;
	private String selectionName;

	AccounterMessages messages;

	public Command() {
		messages = Global.get().messages();
		init();
	}

	public AccounterMessages getMessages() {
		return messages;
	}

	public void init() {
		addRequirements(requirements);
	}

	public abstract String getId();

	protected abstract void addRequirements(List<Requirement> list);

	protected Requirement get(String name) {
		return getRequirement(name);
	}

	protected Requirement getRequirement(String name) {
		for (Requirement requirement : requirements) {
			if (requirement.getName().equals(name)) {
				return requirement;
			}
		}
		return null;
	}

	public abstract Result run(Context context);

	public MobileConstants constants() {
		return constants;

	}

	public boolean isAllRequirementsFulfilled() {
		// TODO
		return true;
	}

	/**
	 * @return
	 */
	public boolean isDone() {
		return this.isDone;
	}

	public void markDone() {
		this.isDone = true;
	}

	/**
	 * 
	 * Sets the SuccessMessage
	 * 
	 * @param message
	 */
	public void setSuccessMessage(String message) {
		this.successMessage = message;
	}

	/**
	 * 
	 * Returns the SuccessMessage
	 * 
	 * @return
	 */
	public String getSuccessMessage() {
		return successMessage;
	}

	/**
	 * @return
	 */
	public Object getResultObject() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Requirement> getRequirements() {
		return requirements;
	}

	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public Object removeAttribute(String name) {
		return attributes.remove(name);
	}

	public boolean isCanTrackRequirements() {
		return canTrackRequirements;
	}

	public void setCanTrackRequirements(boolean canTrackRequirements) {
		this.canTrackRequirements = canTrackRequirements;
	}

	public String getSelectionName() {
		return selectionName;
	}

	public void setSelectionName(String selectionName) {
		this.selectionName = selectionName;
	}
}
