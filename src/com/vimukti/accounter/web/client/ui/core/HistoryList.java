package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to keep track of what views we have opened last. We will keep the size
 * of this limited;
 * 
 * @author rajesh
 * 
 */
public class HistoryList {
	private int size;
	List<HistoryItem> list = new ArrayList<HistoryItem>();

	public HistoryList(int size) {
		this.size = size;
	}

	public HistoryList() {
		this(50);
	}

	public void add(HistoryItem val) {
		if (list.contains(val)) {
			list.remove(val);
		}
		list.add(val);
		if (list.size() > size) {
			list.remove(0);
		}
	}

	public static class HistoryItem {
		Action action;
		String token;

		HistoryItem(AbstractView<?> view, Action action, String token) {
			this.view = view;
			this.action = action;
			this.token = token;
		}

		AbstractView<?> view;
	}

	public HistoryItem getView(String token) {
		for (HistoryItem item : list) {
			if (item.token.equals(token)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Return previous view
	 * 
	 */

	public HistoryItem previous() {
		list.remove(list.size() - 1);
		if (list.size() > 0) {
			return list.remove(list.size() - 1);
		} else {
			return null;
		}

	}

	public void clear() {
		for (HistoryItem item : list) {
			item.view = null;
		}
	}

	public HistoryItem current() {
		return list.get(list.size() - 1);
	}
}
