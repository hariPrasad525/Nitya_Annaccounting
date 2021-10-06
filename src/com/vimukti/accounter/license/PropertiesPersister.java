package com.vimukti.accounter.license;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.License;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.StringUtils;

public class PropertiesPersister {

	private static final String CONTACT_NAME = "ContactName";
	private static final String EMAIL = "Email";
	private static final String SERVER_ID = "ServerID";
	private static final String ORGANISATION = "Organisation";
	private static final String EXPIRES_ON = "ExpiresOn";
	private static final String PURCHASE_DATE = "PurchaseDate";
	private static final String IS_ACTIVE = "IsActive";
	private static final String NO_OF_USERS = "NoOfUsers";

	public void load(Properties props, Reader reader) throws IOException {
		BufferedReader in = new BufferedReader(reader);

		while (true) {
			String line = in.readLine();
			if (line == null) {
				return;
			}
			line = StringUtils.trimLeadingWhitespace(line);
			if (line.length() > 0) {
				char firstChar = line.charAt(0);
				System.out.println(line);
				if ((firstChar != '#') && (firstChar != '!')) {
					while (endsWithContinuationMarker(line)) {
						String nextLine = in.readLine();
						line = line.substring(0, line.length() - 1);
						if (nextLine != null) {
							line = line
									+ StringUtils
											.trimLeadingWhitespace(nextLine);
						}
					}
					int separatorIndex = line.indexOf("=");
					if (separatorIndex == -1) {
						separatorIndex = line.indexOf(":");
					}
					String key = separatorIndex != -1 ? line.substring(0,
							separatorIndex) : line;
					String value = separatorIndex != -1 ? line
							.substring(separatorIndex + 1) : "";
					key = StringUtils.trimTrailingWhitespace(key);
					value = StringUtils.trimLeadingWhitespace(value);
					props.put(unescape(key), unescape(value));
				}
			}
		}
	}

	protected boolean endsWithContinuationMarker(String line) {
		boolean evenSlashCount = true;
		int index = line.length() - 1;
		while ((index >= 0) && (line.charAt(index) == '\\')) {
			evenSlashCount = !evenSlashCount;
			index--;
		}
		return !evenSlashCount;
	}

	protected String unescape(String str) {
		StringBuffer outBuffer = new StringBuffer(str.length());
		for (int index = 0; index < str.length();) {
			char c = str.charAt(index++);
			if (c == '\\') {
				c = str.charAt(index++);
				if (c == 't') {
					c = '\t';
				} else if (c == 'r') {
					c = '\r';
				} else if (c == 'n') {
					c = '\n';
				} else if (c == 'f') {
					c = '\f';
				}
			}
			outBuffer.append(c);
		}
		return outBuffer.toString();
	}

	public void store(Properties props, OutputStream os, String header)
			throws IOException {
		props.store(os, header);
	}

	public void store(Properties props, Writer writer, String header)
			throws IOException {
		BufferedWriter out = new BufferedWriter(writer);
		if (header != null) {
			out.write("#" + header);
			out.newLine();
		}
		out.write("#" + new Date());
		out.newLine();
		for (Enumeration keys = props.keys(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			String val = props.getProperty(key);
			out.write(escape(key, true) + "=" + escape(val, false));
			out.newLine();
		}
		out.flush();
	}

	protected String escape(String str, boolean isKey) {
		int len = str.length();
		StringBuffer outBuffer = new StringBuffer(len * 2);
		for (int index = 0; index < len; index++) {
			char c = str.charAt(index);
			switch (c) {
			case ' ':
				if ((index == 0) || (isKey)) {
					outBuffer.append('\\');
				}
				outBuffer.append(' ');
				break;
			case '\\':
				outBuffer.append("\\\\");
				break;
			case '\t':
				outBuffer.append("\\t");
				break;
			case '\n':
				outBuffer.append("\\n");
				break;
			case '\r':
				outBuffer.append("\\r");
				break;
			case '\f':
				outBuffer.append("\\f");
				break;
			default:
				if ("=: \t\r\n\f#!".indexOf(c) != -1) {
					outBuffer.append('\\');
				}
				outBuffer.append(c);
			}
		}
		return outBuffer.toString();
	}

	public License load(Reader reader) throws IOException {
		Properties props = new Properties();
		load(props, reader);
		License license = new License();
		String emailID = props.getProperty(EMAIL);
		license.setClient(getClient(emailID));
		license.setServerId(props.getProperty(SERVER_ID));
		license.setOrganisation(props.getProperty(ORGANISATION));
		license.setExpiresOn(toDate(props.getProperty(EXPIRES_ON)));
		license.setNoOfUsers(toNumber(props.getProperty(NO_OF_USERS)));
		license.setPurchasedOn(toDate(props.getProperty(PURCHASE_DATE)));
		license.setActive(toBool(props.getProperty(IS_ACTIVE)));
		return license;
	}

	private boolean toBool(String bool) {
		return Boolean.parseBoolean(bool);
	}

	private Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}

	private int toNumber(String property) {
		try {
			return Integer.parseInt(property);
		} catch (NumberFormatException e) {
		}
		return 1;
	}

	private Date toDate(String mills) {
		try {
			long timeMills = Long.parseLong(mills);
			return new Date(timeMills);
		} catch (NumberFormatException e) {
		}
		return null;
	}

	public String getLicenseAsString(License license) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(out,
				Charset.forName("UTF-8")));
		Client client = license.getClient();
		writter.write(getProperty(CONTACT_NAME, client.getFullName()));
		writter.newLine();
		writter.write(getProperty(EMAIL, client.getEmailId()));
		writter.newLine();
		writter.write(getProperty(SERVER_ID, license.getServerId()));
		writter.newLine();
		writter.write(getProperty(ORGANISATION, license.getOrganisation()));
		writter.newLine();
		writter.write(getProperty(EXPIRES_ON, license.getExpiresOn()));
		writter.newLine();
		writter.write(getProperty(PURCHASE_DATE, license.getPurchasedOn()));
		writter.newLine();
		writter.write(getProperty(IS_ACTIVE, license.isActive()));
		writter.newLine();
		writter.write(getProperty(NO_OF_USERS, license.getNoOfUsers()));
		writter.newLine();
		out.flush();
		out.close();
		writter.flush();
		writter.close();
		return new String(out.toByteArray(), "UTF-8");
	}

	private String getProperty(String propName, String propValue) {
		return propName + "=" + propValue;
	}

	private String getProperty(String propName, boolean propValue) {
		return propName + "=" + propValue;
	}

	private String getProperty(String propName, Number propValue) {
		return propName + "=" + propValue;
	}

	private String getProperty(String propName, Date propValue) {
		return propName + "=" + propValue.getTime();
	}

}
