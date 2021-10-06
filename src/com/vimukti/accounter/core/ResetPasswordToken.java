package com.vimukti.accounter.core;

public class ResetPasswordToken {

	long id;
	String token;
	long userId;
	
	public ResetPasswordToken() {
		
	}
	
	public ResetPasswordToken(String token, long userId) {
		this.token = token;
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getID() {
		return id;
	}

	public void setID(long id){
		this.id = id;
	}

}
