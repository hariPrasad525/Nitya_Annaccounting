package com.nitya.accounter.mobile;

public class Requirement {
	Object value;
	Object defaultValue;
	String name;
	private boolean isEditable;
	RequirementType type;

	boolean isOptional;
	boolean isAllowFromContext;
	private String enterString;
	private String recordName;

	public Requirement(String name, boolean isOptional,
			boolean isAllowFromContext) {
		this.name = name;
		this.isOptional = isOptional;
		this.isAllowFromContext = isAllowFromContext;
		isEditable = true;
	}

	public Requirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		this(requirementName, isOptional2, isAllowFromContext2);
		this.setEnterString(enterString);
		this.recordName = recordName;
	}

	public <T> T getValue() {
		return (T) (value == null ? defaultValue : value);
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public boolean isAllowFromContext() {
		return isAllowFromContext;
	}

	public void setAllowFromContext(boolean isAllowFromContext) {
		this.isAllowFromContext = isAllowFromContext;
	}

	public boolean isDone() {
		if (!isOptional()) {
			return value != null;
		}
		return true;
	}

	public Result process(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		return null;
	}

	public String getRecordName() {
		return recordName;
	}

	public String getEnterString() {
		return enterString;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public void setEnterString(String enterString) {
		this.enterString = enterString;
	}
}
