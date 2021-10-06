package com.vimukti.accounter.main.upload;

public class UploadAttachment {
	public static final int CREATE = 1;
	public static final int DELETE = 2;
	public int processType;
	public String attachmentId;
	public String fileName;
	public byte[] key;

	public UploadAttachment(String attachmentId, int processType,
			String fileName) {
		this.processType = processType;
		this.attachmentId = attachmentId;
		this.fileName = fileName;
	}
}
