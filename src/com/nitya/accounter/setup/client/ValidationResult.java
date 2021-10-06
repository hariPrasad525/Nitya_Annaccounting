package com.nitya.accounter.setup.client;

import java.util.ArrayList;

public class ValidationResult {
	private ArrayList<Error> errors = new ArrayList<ValidationResult.Error>();
	private ArrayList<String> informations = new ArrayList<String>();

	public void addError(Object obj, String msg) {
		for (Error error : this.errors) {
			if (error.getMessage().equals(msg)) {
				return;
			}
		}

		this.errors.add(new Error(obj, msg));
	}

	public void add(ValidationResult result) {
		if (result != null) {
			errors.addAll(result.errors);
			informations.addAll(result.informations);
		}
	}

	public void addInformation(String msg) {
		informations.add(msg);
	}

	public static class Error {
		private Object source;
		private String message;

		Error(Object source, String message) {
			this.setSource(source);
			this.setMessage(message);
		}

		public Object getSource() {
			return source;
		}

		public void setSource(Object source) {
			this.source = source;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public int hashCode() {
			int code = 7;
			if (message == null) {
				return code;
			}
			return code * message.length();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof Error)) {
				return false;
			}
			Error err = (Error) obj;
			return this.source == err.source
					&& this.message.equalsIgnoreCase(err.message);
		}

	}

	public boolean haveErrors() {
		return !this.errors.isEmpty();
	}

	/**
	 * @return
	 */
	public boolean haveWarnings() {
		return !this.informations.isEmpty();
	}

	/**
	 * @return the errors
	 */
	public ArrayList<Error> getErrors() {
		return errors;
	}

	/**
	 * @return the warnings
	 */
	public ArrayList<String> getInformations() {
		return informations;
	}
}
