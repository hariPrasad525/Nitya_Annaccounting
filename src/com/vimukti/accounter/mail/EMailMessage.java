package com.vimukti.accounter.mail;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class EMailMessage {

	public class Attachment {
		File file;

		public File getFile() {
			return file;
		}

		public String getFileName() {
			return fileName;
		}

		String fileName;

		public Attachment(File file, String fileName) {
			this.file = file;
			this.fileName = fileName;
		}
	}

	private String from;

	public String subject;

	public String content;

	Set<Attachment> attachment = new HashSet<EMailMessage.Attachment>();

	public Set<String> recepeiants = new HashSet<String>();
	public Set<String> ccrecepeiants = new HashSet<String>();

	private String replayTO;

	public boolean isPlain;

	public Set<String> getRecipeants() {
		return recepeiants;
	}

	public void setRecepeants(Set<String> to) {
		this.recepeiants = to;
	}

	public void setRecepeant(String to) {
		this.recepeiants.add(to);
	}

	public void setccRecepeant(String cc) {
		this.ccrecepeiants.add(cc);
	}

	public Set<String> getccRecipeants() {
		return ccrecepeiants;
	}

	public void setccRecepeants(Set<String> cc) {
		this.ccrecepeiants = cc;
	}

	public Set<Attachment> getAttachments() {
		return attachment;
	}

	public void setAttachment(File file) {
		setAttachment(file, file.getName());
	}

	public void setAttachment(File file, String name) {
		this.attachment.add(new Attachment(file, name));
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setReplayTO(String replayTO) {
		this.replayTO = replayTO;
	}

	public String getReplayTO() {
		return replayTO;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
