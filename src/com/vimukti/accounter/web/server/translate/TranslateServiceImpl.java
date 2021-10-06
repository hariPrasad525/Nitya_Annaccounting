package com.vimukti.accounter.web.server.translate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.ClientLocalMessage;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.translate.ClientLanguage;
import com.vimukti.accounter.web.client.translate.ClientMessage;
import com.vimukti.accounter.web.client.translate.TranslateService;
import com.vimukti.accounter.web.server.AccounterWindowsRPCBaseServiceImpl;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.i18n.ServerSideMessages;

public abstract class TranslateServiceImpl extends AccounterWindowsRPCBaseServiceImpl
		implements TranslateService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String EMAIL_ID = "emailId";
	private ArrayList<String> serverMatchList;

	// @Override
	// public ArrayList<Status> getStatus() {
	// String userEmail = getUserEmail();
	// if (userEmail == null) {
	// return null;
	// }
	// return new FinanceTool().getTranslationStatus();
	// }

	// @Override
	// public ClientMessage getNext(String lang, int lastMessageId) {
	// String userEmail = getUserEmail();
	// if (userEmail == null) {
	// return null;
	// }
	//
	// try {
	// return new FinanceTool().getNextMessage(lang, lastMessageId);
	// } catch (AccounterException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	@Override
	public ClientLocalMessage addTranslation(long id, String lang, String value) {
		String userEmail = getUserEmail();
		if (userEmail == null) {
			return null;
		}
		return new FinanceTool().addTranslation(userEmail, id, lang, value);
	}

	@Override
	public boolean vote(long localMessageId) {
		String userEmail = getUserEmail();
		if (userEmail == null) {
			return false;
		}
		return new FinanceTool().addVote(localMessageId, userEmail);
	}

	@Override
	public boolean setApprove(long localMessageId, boolean isApprove) {
		String userEmail = getUserEmail();
		if (userEmail == null) {
			return false;
		}
		return new FinanceTool().setApprove(localMessageId, isApprove);
	}

	protected String getUserEmail() {
		return (String) getThreadLocalRequest().getSession().getAttribute(
				EMAIL_ID);
	}

	@Override
	public PaginationList<ClientMessage> getMessages(String lang, int status,
			int from, int to, String searchTerm) {
		String userEmail = getUserEmail();
		if (userEmail == null) {
			return null;
		}
		return new FinanceTool().getMessages(status, lang, userEmail, from, to,
				searchTerm);
	}

	public ArrayList<ClientLanguage> getLanguages() {
		return (ArrayList<ClientLanguage>) new FinanceTool().getLanguages();
	}

	public ClientLanguage getLocalLanguage() {
		ArrayList<ClientLanguage> languages = (ArrayList<ClientLanguage>) new FinanceTool()
				.getLanguages();
		for (ClientLanguage clientLanguage : languages) {
			String language = "";
			try {
				language = ServerLocal.get().getISO3Language();
			} catch (MissingResourceException e) {
				language = "eng";
			}
			if (clientLanguage.getLanguageCode().equals(language)) {
				return clientLanguage;
			}

		}
		return languages.get(0);
	}

	public boolean validateUserValue(ClientMessage clientMessage, String data) {
		serverMatchList = new ArrayList<String>();
		Pattern regex = Pattern.compile("\\{([^}]*)\\}");
		Matcher serverMatcher = regex.matcher(clientMessage.getValue());
		while (serverMatcher.find()) {
			serverMatchList.add(serverMatcher.group());
		}

		ArrayList<String> userMatchList = new ArrayList<String>();
		Matcher userMatcher = regex.matcher(data);
		while (userMatcher.find()) {
			userMatchList.add(userMatcher.group());
		}
		if (serverMatchList.size() == userMatchList.size()) {
			for (int i = 0; i < serverMatchList.size(); i++) {
				if (!serverMatchList.get(i).equals(userMatchList.get(i))) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public Set<String> getServerMatchList() {
		Set<String> matchList = new HashSet<String>();
		matchList.addAll(serverMatchList);
		return matchList;
	}

	@Override
	public boolean canApprove(String lang) {
		String userEmail = getUserEmail();
		if (userEmail == null) {
			return false;
		}
		return new FinanceTool().canApprove(userEmail, lang);
	}

	@Override
	public boolean updateMessgaeStats(ArrayList<String> byOrder,
			HashMap<String, Integer> byCount) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			ArrayList<String> list = new ArrayList<String>(
					ServerSideMessages.usageByOrder);
			for (String name : byOrder) {
				if (!list.contains(name)) {
					list.add(name);
				}
			}
			for (String keyName : list) {
				Key key = (Key) session.getNamedQuery("getKeyByValue")
						.setParameter("value", keyName).list().get(0);
				int index = list.indexOf(keyName) + 1;
				key.setUsageOrder(index);
				Integer count = byCount.get(keyName);
				key.setUsageCount(count == null ? 0 : count);
				session.update(key);
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			throw new AccounterException();
		}
		return false;
	}
}
