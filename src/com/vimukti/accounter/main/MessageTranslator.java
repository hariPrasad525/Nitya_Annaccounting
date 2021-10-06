package com.vimukti.accounter.main;

import java.sql.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.server.translate.Language;
import com.vimukti.accounter.web.server.translate.LocalMessage;
import com.vimukti.accounter.web.server.translate.Message;

public class MessageTranslator {

	public static void main(String[] args) {

		if (args[0] == null) {
			return;
		}
		Session session = HibernateUtil.openSession();
		try {
			ParseFile parseFile = new ParseFile(args[0]);
			Map<String, String> messages = parseFile.parse();
			String message = null;
			User user = AccounterThreadLocal.get();
			Locale locale = ServerLocal.get();
			String language = locale.getISO3Language();
			long clientId = 0;
			if (user != null) {
				clientId = user.getClient().getID();
			}

			Query languageQuery = session.getNamedQuery("getLanguageById")
					.setParameter("code", language);
			Language lan = (Language) languageQuery.uniqueResult();

			for (Map.Entry<String, String> entry : messages.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (key != null) {
					Message mesgobj = (Message) session
							.getNamedQuery("getMessageByValue")
							.setParameter("value", value).uniqueResult();
					if (mesgobj == null) {
						continue;
					}
					if (mesgobj.getValue().equals(value)) {
						continue;
					}
					List list = session.getNamedQuery("getLocalMessageForKey")
							.setString("language", language)
							.setLong("client", clientId).setString("key", key)
							.list();
					if (!list.isEmpty()) {
						message = (String) list.get(0);
					}
					if (message == null) {

						// Creating local Message
						Transaction beginTransaction = session
								.beginTransaction();

						LocalMessage localMessage = new LocalMessage();
						localMessage.setCreatedBy(user.getClient());
						localMessage.setLang(lan);
						mesgobj.setValue(value);
						localMessage.setMessage(mesgobj);
						localMessage.setCreatedDate(new Date(System
								.currentTimeMillis()));
						localMessage.setApproved(true);
						session.save(localMessage);
						beginTransaction.commit();

					} else {

						// Changing the localmessage
						Transaction beginTransaction = session
								.beginTransaction();
						LocalMessage localMessage = (LocalMessage) session
								.getNamedQuery("getLocalMessagesByMessageId")
								.setParameter("messageId", mesgobj.getId())
								.setParameter("lang", lan.getLanguageCode())
								.uniqueResult();
						localMessage.setValue(value);
						session.saveOrUpdate(localMessage);
						beginTransaction.commit();

					}
				}

			}

		} catch (Exception e) {

			if (session != null)
				session.close();
		}
	}
}
