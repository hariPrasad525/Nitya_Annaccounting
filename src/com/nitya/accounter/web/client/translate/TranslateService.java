package com.nitya.accounter.web.client.translate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.nitya.accounter.web.client.ClientLocalMessage;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;

public interface TranslateService extends RemoteService {

	// ArrayList<Status> getStatus();

	// ClientMessage getNext(String lang, int lastMessageId);

	ClientLocalMessage addTranslation(long id, String lang, String value);

	boolean vote(long localMessageId);

	PaginationList<ClientMessage> getMessages(String lang, int status,
			int from, int to, String searchTerm);

	boolean setApprove(long localMessageId, boolean isApprove);

	ArrayList<ClientLanguage> getLanguages();

	ClientLanguage getLocalLanguage();

	public boolean validateUserValue(ClientMessage clientMessage, String data);

	public Set<String> getServerMatchList();

	boolean canApprove(String lang);

	boolean updateMessgaeStats(ArrayList<String> byOrder,
			HashMap<String, Integer> byCount) throws AccounterException;
}
