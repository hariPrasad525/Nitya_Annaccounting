package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashSet;

public class ValidationResult {
	private HashSet<Error> errors = new HashSet<ValidationResult.Error>();
	private HashSet<Warning> warnings = new HashSet<ValidationResult.Warning>();

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
			warnings.addAll(result.warnings);
		}
	}

	public void addWarning(Object obj, String msg) {
		warnings.add(new Warning(obj, msg));
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

	public static class Warning {
		private Object source;
		private String message;

		/**
		 * Creates new Instance
		 */
		public Warning(Object source, String msg) {
			this.source = source;
			this.message = msg;
		}

		/**
		 * @return the source
		 */
		public Object getSource() {
			return source;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
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
			if (obj == null || !(obj instanceof Warning)) {
				return false;
			}
			Warning war = (Warning) obj;
			return this.source == war.source
					&& this.message.equalsIgnoreCase(war.message);
		}

	}

	public boolean haveErrors() {
		return !this.errors.isEmpty();
	}

	/**
	 * @return
	 */
	public boolean haveWarnings() {
		return !this.warnings.isEmpty();
	}

	/**
	 * @return the errors
	 */
	public HashSet<Error> getErrors() {
		return errors;
	}

	/**
	 * @return the warnings
	 */
	public HashSet<Warning> getWarnings() {
		return warnings;
	}

	/**
	 * @return the warnings
	 */
	public ArrayList<Warning> getWarningsAsList() {
		return new ArrayList<ValidationResult.Warning>(warnings);
	}
}
