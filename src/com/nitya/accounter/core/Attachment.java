package com.nitya.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.main.upload.AttachmentFileServer;
import com.nitya.accounter.main.upload.UploadAttachment;
import com.nitya.accounter.utils.SecureUtils;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;

public class Attachment extends CreatableObject implements IAccounterServerCore {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String attachmentId;
	private String name;
	private long size;

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		boolean onSave = super.onSave(session);
		if (getID() == 0) {
			setCompany(getCreatedBy().getCompany());
			Company company = getCompany();
			UploadAttachment attachment = new UploadAttachment(attachmentId,
					UploadAttachment.CREATE, getName());
			String encryptionKey = company.getEncryptionKey();
			if (encryptionKey == null) {
				company.setEncryptionKey(SecureUtils.createID(16));
				session.saveOrUpdate(company);
			}
			attachment.key = company.getEncryptionKey().getBytes();
			AttachmentFileServer.put(attachment);
		}
		return onSave;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		UploadAttachment attachment = new UploadAttachment(attachmentId,
				UploadAttachment.DELETE, getName());
		AttachmentFileServer.put(attachment);
		return super.onDelete(arg0);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() throws AccounterException {
		if (name == null || name.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().attachments());
		}
	}
}
