package com.nitya.accounter.core;

public enum ActivityType {
	LOGIN(0),

	LOGOUT(1),

	ADD(2),

	EDIT(3),

	DELETE(4),

	UPDATE_PREFERENCE(5),

	NOTE(6),

	VOID(7),

	MERGE(8);

	private int type;

	private ActivityType(int type) {
		this.type = type;
	}

	public int getValue() {
		return type;
	}

	public static ActivityType getType(int i) {
		switch (i) {
		case 0:
			return LOGIN;
		case 1:
			return LOGOUT;
		case 2:
			return ADD;
		case 3:
			return EDIT;
		case 4:
			return DELETE;
		case 5:
			return UPDATE_PREFERENCE;
		case 6:
			return NOTE;
		case 7:
			return VOID;
		case 8:
			return MERGE;
		}
		return null;
	}
}
