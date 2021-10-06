package com.vimukti.accounter.text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <pre>
 * {
 *   "From": "myUser@theirDomain.com",
 *   "FromFull": {
 *     "Email": "myUser@theirDomain.com",
 *     "Name": "John Doe"
 *   },
 *   "To": "451d9b70cf9364d23ff6f9d51d870251569e+ahoy@inbound.postmarkapp.com",
 *   "ToFull": [
 *     {
 *       "Email": "451d9b70cf9364d23ff6f9d51d870251569e+ahoy@inbound.postmarkapp.com",
 *       "Name": ""
 *     }
 *   ],
 *   "CC": "\"John Sample\" <sample.cc@emailDomain.com>, \"Mike Sample\" <another.cc@emailDomain.com>",
 *   "CcFull": [
 *     {
 *       "Email": "sample.cc@emailDomain.com",
 *       "Name": "John Sample"
 *     },
 *     {
 *       "Email": "another.cc@emailDomain.com",
 *       "Name": "Mike Sample"
 *     }
 *   ],
 *   "ReplyTo": "myUsersReplyAddress@theirDomain.com",
 *   "Subject": "This is an inbound message",
 *   "MessageID": "22c74902-a0c1-4511-804f2-341342852c90",  
 *   "Date": "Thu, 5 Apr 2012 16:59:01 +0200",
 *   "MailboxHash": "ahoy",
 *   "TextBody": "[ASCII]",
 *   "HtmlBody": "[HTML(encoded)]",
 *   "Tag": "",
 *   "Headers": [
 *     {
 *       "Name": "MIME-Version",
 *       "Value": "1.0"
 *     },
 *     {
 *       "Name": "X-Spam-Status",
 *       "Value": "No"
 *     },
 *     {
 *       "Name": "X-Spam-Score",
 *       "Value": "-0.8"
 *     },
 *     {
 *       "Name": "X-Spam-Tests",
 *       "Value": "DKIM_SIGNED,DKIM_VALID,DKIM_VALID_AU,RCVD_IN_DNSWL_LOW"
 *     },
 *     {
 *       "Name": "Received-SPF",
 *       "Value": "Pass (sender SPF authorized) identity=mailfrom; client-ip=209.85.160.42; helo=mail-pw0-f42.google.com; envelope-from=support@postmarkapp.com; receiver=451d9b70cf9364d23ff6f9d51d870251569e+ahoy@inbound.postmarkapp.com"
 *     }
 *   ],
 *   "Attachments": [
 *     {
 *       "Content-Length": 4096,
 *       "Content": "[BASE64]",
 *       "ContentType": "image\/png",
 *       "Name": "myimage.png"
 *     },
 *     {
 *       "Content-Length": 16384,
 *       "Content": "[BASE64]",
 *       "ContentType": "application\/msword",
 *       "Name": "mypaper.doc"
 *     }
 *   ]
 * }
 * </pre>
 * 
 * @author PrasannaKumar
 * 
 */
public class Mail {

	class Attachment {

		long length;

		byte[] content;

		String contentType;

		String name;

	}

	private String from;

	private String to;

	private ArrayList<String> cc = new ArrayList<String>();

	private String replyTo;

	private String subject;

	private String messageID;

	private String textBody;

	private String htmlBody;

	private HashMap<String, String> headers = new HashMap<String, String>();

	private String tag;

	private ArrayList<Attachment> attachments = new ArrayList<Attachment>();

	private String mailboxHash;

	private Date date;

	private String fromName;

	private String toName;

	Mail() {

	}

	public static Mail parse(String request) throws IOException {
		try {
			JSONObject json = new JSONObject(request);
			Mail mail = new Mail();
			mail.load(json);
			return mail;
		} catch (JSONException e) {
			throw new IOException("Error while parsing request", e);
		} catch (ParseException e) {
			throw new IOException("Error while parsing request", e);
		}
	}

	private void load(JSONObject json) throws JSONException, ParseException {
		this.from = json.getString("From");
		JSONObject fromFull = json.getJSONObject("FromFull");
		this.fromName = fromFull.getString("Name");

		JSONArray toFull = json.getJSONArray("ToFull");
		JSONObject first = toFull.getJSONObject(0);
		this.toName = first.getString("Name");
		this.to = first.getString("Email");

		readCC(json);
		this.replyTo = json.getString("ReplyTo");
		this.subject = json.getString("Subject");
		this.messageID = json.getString("MessageID");
		readDate(json);
		this.mailboxHash = json.getString("MailboxHash");
		this.textBody = json.getString("TextBody");
		this.htmlBody = json.getString("HtmlBody");
		this.tag = json.getString("Tag");
		readHeaders(json);
		readAttachments(json);

	}

	/**
	 * 
	 * "Date": "Thu, 5 Apr 2012 16:59:01 +0200",
	 * 
	 * @param json
	 * @throws ParseException
	 * @throws JSONException
	 */
	private void readDate(JSONObject json) throws ParseException, JSONException {
		String dateString = json.getString("Date");
		try {
			this.date = DateFormat.getDateInstance().parse(dateString);
		} catch (ParseException e) {
			// Unable Parse Date
		}
	}

	/**
	 * 
	 * <pre>
	 * "CC": "\"John Sample\" <sample.cc@emailDomain.com>, \"Mike Sample\" <another.cc@emailDomain.com>",
	 * </pre>
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void readCC(JSONObject json) throws JSONException {
		if (!json.has("CC")) {
			return;
		}
		String cc = json.getString("CC");

		String[] split = cc.split(",");
		for (String email : split) {
			this.cc.add(email);
		}
	}

	/**
	 * <pre>
	 *   "Attachments": [
	 *     {
	 *       "Content-Length": 4096,
	 *       "Content": "[BASE64]",
	 *       "ContentType": "image\/png",
	 *       "Name": "myimage.png"
	 *     },
	 *     {
	 *       "Content-Length": 16384,
	 *       "Content": "[BASE64]",
	 *       "ContentType": "application\/msword",
	 *       "Name": "mypaper.doc"
	 *     }
	 *   ]
	 * </pre>
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void readAttachments(JSONObject json) throws JSONException {
		JSONArray atts = json.getJSONArray("Attachments");

		// All Attachment
		for (int x = 0; x < atts.length(); x++) {
			JSONObject attachment = atts.getJSONObject(x);
			// Read Attachment
			readAttachment(attachment);
		}

	}

	/**
	 * 
	 * <pre>
	 *  {
	 *       "Content-Length": 16384,
	 *       "Content": "[BASE64]",
	 *       "ContentType": "application\/msword",
	 *       "Name": "mypaper.doc"
	 *     }
	 * </pre>
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void readAttachment(JSONObject json) throws JSONException {
		Attachment attachment = new Attachment();

		// Length
		long length = json.getLong("Content-Length");
		attachment.length = length;

		// Content
		String stringContent = json.getString("Content");
		byte[] content = Base64.decodeBase64(stringContent.getBytes());
		attachment.content = content;

		// Content Type
		String cType = json.getString("ContentType");
		attachment.contentType = cType;

		// Name
		String name = json.getString("Name");
		attachment.name = name;

		this.attachments.add(attachment);

	}

	/**
	 * <pre>
	 * "Headers": [
	 *     {
	 *       "Name": "MIME-Version",
	 *       "Value": "1.0"
	 *     },
	 *     {
	 *       "Name": "X-Spam-Status",
	 *       "Value": "No"
	 *     },
	 *     {
	 *       "Name": "X-Spam-Score",
	 *       "Value": "-0.8"
	 *     },
	 *     {
	 *       "Name": "X-Spam-Tests",
	 *       "Value": "DKIM_SIGNED,DKIM_VALID,DKIM_VALID_AU,RCVD_IN_DNSWL_LOW"
	 *     },
	 *     {
	 *       "Name": "Received-SPF",
	 *       "Value": "Pass (sender SPF authorized) identity=mailfrom; client-ip=209.85.160.42; helo=mail-pw0-f42.google.com; envelope-from=support@postmarkapp.com; receiver=451d9b70cf9364d23ff6f9d51d870251569e+ahoy@inbound.postmarkapp.com"
	 *     }
	 *   ],
	 * </pre>
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void readHeaders(JSONObject json) throws JSONException {
		JSONArray header = json.getJSONArray("Headers");
		for (int x = 0; x < header.length(); x++) {
			JSONObject object = header.getJSONObject(x);
			String name = object.getString("Name");
			String value = object.getString("Value");
			headers.put(name, value);
		}
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @return the cc
	 */
	public ArrayList<String> getCc() {
		return cc;
	}

	/**
	 * @return the replyTo
	 */
	public String getReplyTo() {
		return replyTo;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @return the messageID
	 */
	public String getMessageID() {
		return messageID;
	}

	/**
	 * @return the textBody
	 */
	public String getTextBody() {
		return textBody;
	}

	/**
	 * @return the htmlBody
	 */
	public String getHtmlBody() {
		return htmlBody;
	}

	/**
	 * @return the headers
	 */
	public HashMap<String, String> getHeaders() {
		return headers;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @return the attachments
	 */
	public ArrayList<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @return the mailboxHash
	 */
	public String getMailboxHash() {
		return mailboxHash;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the fromName
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * @return the toName
	 */
	public String getToName() {
		return toName;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @param textBody
	 *            the textBody to set
	 */
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}

	public String shortForm() {
		return "From : " + from + " To : " + to + " Subject : " + subject;
	}
}
