package com.vimukti.accounter.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.translate.Key;
import com.vimukti.accounter.web.server.translate.Message;

public class MessageLoader {
	private InputStreamReader reader;
	private static List<Message> oldMessages;

	public MessageLoader(InputStream inputStream) throws Exception {
		oldMessages = new FinanceTool().getAllMessages();
		this.reader = new InputStreamReader(inputStream, "utf-8");
	}

	public void loadMessages() throws Exception {
		List<String> allKeys = new ArrayList<String>();
		Map<String, Message> messages = new HashMap<String, Message>();
		List<Message> allMessages = new ArrayList<Message>();

		BufferedReader br = new BufferedReader(reader);
		String line = null;
		StringBuilder comment = null;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("#")) {
				if (comment == null) {
					comment = new StringBuilder(line.substring(1));
				} else {
					comment.append('\n').append(line.substring(1));
				}
				continue;
			}
			String commentStr = comment == null ? "" : comment.toString()
					.trim();
			comment = null;
			String[] split = line.split("=");
			if (split.length < 2) {
				continue;
			}
			String keyValue = split[0];
			String value = split[1];

			Key key = new Key();
			key.setKey(keyValue.trim());

			Message message = new Message();
			message.setValue(value);
			message.setComment(commentStr);

			Set<Key> keyset = new HashSet<Key>();
			keyset.add(key);
			message.setKeys(keyset);

			int index = allKeys.indexOf(keyValue);
			if (index != -1) {// Not exists
				// System.out.println("Duplicate key: '" + line + "'");
				// continue;
				throw new AccounterException("Duplicate key '" + keyValue + "'");
			}

			Message m = messages.get(value);
			if (m != null) {
				// System.out.println("Duplicate Value: '" + line + "'");
				// continue;
				throw new AccounterException("Duplicate Value '" + value + "'");
			}

			allMessages.add(message);
			messages.put(value, message);
			allKeys.add(keyValue);
		}
		mergeMessages(allMessages);
	}

	private static void mergeMessages(List<Message> allMessages) {
		Session session = HibernateUtil.getCurrentSession();
		List<Message> removed = new ArrayList<Message>();
		List<Message> addedMessages = new ArrayList<Message>();
		List<Key> removedKeys = new ArrayList<Key>();
		for (Message message : allMessages) {
			Set<Key> keys = message.getKeys();
			Iterator<Key> iterator = keys.iterator();
			Key key = null;
			if (iterator.hasNext()) {
				key = iterator.next();
			}
			boolean insert = false;
			Message messageByValue = getMessageByValue(message.getValue());
			if (messageByValue != null) {
				messageByValue.setComment(message.getComment());
				boolean hasKey = false;
				for (Key k : messageByValue.getKeys()) {
					if (k.equals(key)) {
						keys.add(k);
						keys.remove(key);
						hasKey = true;
						break;
					}
				}
				if (!hasKey) {
					Key k = getKeyFromDB(key.getKey());
					if (k != null) {
						key = k;
					}
					messageByValue.getKeys().add(key);
				}
			} else {
				insert = true;
			}

			List<Message> messageByKey = getMessageByKey(key);
			if (insert) {// "no old message" or "old message with different key"
							// ( not both are same)
				for (Message msg : messageByKey) {
					if (msg.getLocalMessages().isEmpty()) {
						if (!msg.getValue().equals(message.getValue())) {
							for (Key k : msg.getKeys()) {
								if (k.equals(key)) {
									keys.add(k);// replace this key
									keys.remove(key);
									msg.getKeys().remove(k);
									break;
								}
							}
							if (msg.getLocalMessages() == null
									|| msg.getLocalMessages().isEmpty()) {
								removed.add(msg);
								removeKeys(msg, removedKeys);
							}
						}
					} else {
						for (Key k : msg.getKeys()) {
							if (k.equals(key)) {
								msg.getKeys().remove(k);
								keys.add(k);// replace this key
								keys.remove(key);
								break;
							}
						}
					}
				}
			} else {
				// Has duplicate messages for this key
				if (messageByKey.size() > 1) {
					// find original one and delete all others
					for (Message msg : messageByKey) {
						if (!msg.getValue().equals(message.getValue())) {
							if (msg.getLocalMessages() == null
									|| msg.getLocalMessages().isEmpty()) {
								removed.add(msg);
								removeKeys(msg, removedKeys);
								System.out
										.println("Dulpicate messages with same key :"
												+ msg);
							} else {
								msg.getKeys().remove(key);
							}
						}
					}
				}
			}
			if (insert) {
				addedMessages.add(message);
			}

		}

		for (Message message : oldMessages) {
			boolean contains = false;
			for (Message newMessage : allMessages) {
				if (newMessage.getValue().equals(message.getValue())) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				message.setNotUsed(true);
			}
		}
		oldMessages.addAll(addedMessages);

		org.hibernate.Transaction transaction = session.beginTransaction();
		for (Message message : removed) {
			if (message != null) {
				session.delete(message);
				oldMessages.remove(message);
			}
		}
		transaction.commit();
		transaction = session.beginTransaction();
		for (Key key : removedKeys) {
			session.delete(key);
		}
		transaction.commit();
		transaction = session.beginTransaction();
		for (Message message : oldMessages) {
			session.saveOrUpdate(message);
		}
		transaction.commit();

	}

	private static Key getKeyFromDB(String key) {
		Session currentSession = HibernateUtil.getCurrentSession();
		return (Key) currentSession.getNamedQuery("getKeyByValue")
				.setString("value", key).uniqueResult();
	}

	private static void removeKeys(Message message, List<Key> removedKeys) {
		for (Key key : message.getKeys()) {
			List<Message> messageByKey = getMessageByKey(key);
			messageByKey.remove(message);
			if (messageByKey.isEmpty()) {
				removedKeys.add(key);
			}
		}
	}

	private static List<Message> getMessageByKey(Key key) {
		List<Message> oldMessagesByKey = new ArrayList<Message>();
		for (Message message : oldMessages) {
			Iterator<Key> iterator = message.getKeys().iterator();
			while (iterator.hasNext()) {
				Key next = iterator.next();
				if (next.getKey().equals(key.getKey())) {
					oldMessagesByKey.add(message);
				}
			}
		}
		return oldMessagesByKey;
	}

	private static Message getMessageByValue(String value) {
		for (Message message : oldMessages) {
			if (message.getValue().equals(value)) {
				return message;
			}
		}
		return null;
	}
}
