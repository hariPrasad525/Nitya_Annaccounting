package com.nitya.accounter.mail;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

import com.sun.mail.imap.IMAPFolder;

public class EMailMonitor extends Thread {
	private static EMailMonitor instance;

	@Override
	public void run() {
		try {
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore();
			// store.connect("imap.gmail.com",
			// ServerConfiguration.getUploadAttachmentEmailId(),
			// ServerConfiguration.getUploadAttachmentPassword());
			store.connect("imap.gmail.com", "test@test.com",
					"test");
			IMAPFolder inbox = (IMAPFolder) store.getFolder("Inbox");
			inbox.addMessageCountListener(new MessageCountListener() {

				@Override
				public void messagesRemoved(MessageCountEvent arg0) {
					System.out.println("Removed. - "
							+ arg0.getMessages().length);
					// Message[] messages = arg0.getMessages();
					// for (Message message : messages) {
					// try {
					// String subject = message.getSubject();
					// System.out.println("subject-" + subject);
					// Address[] from = message.getFrom();
					// for (Address addr : from) {
					// System.out.println(addr);
					// }
					// String description = message.getDescription();
					// System.out.println("MSG-" + description);
					// } catch (MessagingException e) {
					// e.printStackTrace();
					// }
					// }
				}

				@Override
				public void messagesAdded(MessageCountEvent arg0) {
					System.out.println("Added. - " + arg0.getMessages().length);
					Message[] messages = arg0.getMessages();
					for (Message message : messages) {
						String subject;
						try {
							subject = message.getSubject();
							System.out.println("subject-" + subject);
							Address[] from = message.getFrom();
							for (Address addr : from) {
								System.out.println(addr);
							}
						} catch (MessagingException e) {
							e.printStackTrace();
						}
						// try {
						// readMessage(message);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
					}
				}
			});
			inbox.open(IMAPFolder.READ_ONLY);
			System.out.println("Inbox opend " + inbox.getMessageCount());
			while (true) {
				inbox.idle();
				System.out.println("re-idle");
			}
		} catch (Exception e) {
		}
	}

	// protected void readMessage(Message message) throws Exception {
	// MimeMultipart content = (MimeMultipart) message.getContent();
	// int count = content.getCount();
	// for (int i = 0; i < count; i++) {
	// BodyPart bodyPart = content.getBodyPart(i);
	// String disposition = bodyPart.getDisposition();
	// if (disposition != null
	// && disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
	// String attachId = SecureUtils.createID(16);
	// File file = new File(ServerConfiguration.getAttachmentsDir()
	// + File.separator + attachId);
	// readFile(file, bodyPart);
	//
	// // UploadFileServer.put(new UploadAttachment(attachId,
	// // UploadAttachment.CREATE));
	//
	// String fileName = decodeName(bodyPart.getFileName());
	// int size = bodyPart.getSize();
	// System.out.println("Attachment " + fileName + " file->"
	// + file.getAbsolutePath());
	// // Attachment attachment = new Attachment();
	// // attachment.setAttachmentId(attachId);
	// // attachment.setName(fileName);
	// // attachment.setSize(size);
	//
	// // Save this Attachment in transaction
	// }
	// }
	// }

	protected int readFile(File saveFile, Part part) throws Exception {

		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(saveFile));

		byte[] buff = new byte[2048];
		InputStream is = part.getInputStream();
		int ret = 0, count = 0;
		while ((ret = is.read(buff)) > 0) {
			bos.write(buff, 0, ret);
			count += ret;
		}
		bos.close();
		is.close();
		return count;
	}

	protected String decodeName(String name) throws Exception {
		if (name == null || name.length() == 0) {
			return "unknown";
		}
		String ret = java.net.URLDecoder.decode(name, "UTF-8");

		// also check for a few other things in the string:
		ret = ret.replaceAll("=\\?utf-8\\?q\\?", "");
		ret = ret.replaceAll("\\?=", "");
		ret = ret.replaceAll("=20", " ");

		return ret;
	}

	public static EMailMonitor getInstance() {
		if (instance == null) {
			instance = new EMailMonitor();
		}
		return instance;
	}

	public static void main(String[] args) {
		new EMailMonitor().start();
	}
}
