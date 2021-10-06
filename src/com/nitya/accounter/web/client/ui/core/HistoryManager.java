package com.nitya.accounter.web.client.ui.core;

import java.util.ArrayList;

/**
 * For History Management
 * 
 * @author Raj Vimal
 * 
 */
public class HistoryManager {

	private ArrayList<History> reportHistory;

	public HistoryManager() {

		reportHistory = new ArrayList<History>();

	}

	/**
	 * Provides Previous History object
	 * 
	 * @return History
	 */
	public History getPreviousHistory(Action action) {
		for (int i = 0; i < reportHistory.toArray().length; i++) {
			if (reportHistory.get(i).getAction().getText().equals(
					action.getText())) {
				return reportHistory.get(i - 1);
			}
		}
		return reportHistory.get(reportHistory.size() - 1);
	}

	/**
	 * Provides next History object
	 * 
	 * @return History
	 */
	public History getNextHistory(Action action) {
		for (int i = 0; i < reportHistory.toArray().length; i++) {
			if (reportHistory.get(i).getAction().equals(action)) {
				return reportHistory.get(i + 1);
			}
		}
		return null;
	}
	
	/**
	 * adding History object to History List depending on
	 * 
	 * @param history
	 */
	public void addToReportHistory(History history) {
		if (history != null) {
			if (checkActionReplication(history)) {
//				clearAllNextHistory();
			}
			reportHistory.add(history);
		}

	}

	/**
	 * Removing History object from History List depending on History object
	 * provided
	 * 
	 * @param history
	 */
	public void removeHistory(History history) {

		if (reportHistory.size() > 0 && history != null) {
			reportHistory.remove(history);
		}
	}

	/**
	 * Removing History object from History List depending on provided action
	 * 
	 * @param index
	 */
	public void removeByAction(Action action) {
		
	}
	
	/**
	 * Getter for History List
	 * 
	 * @return ArrayList<History>
	 */
	public ArrayList<History> getHistoryList() {
		return reportHistory;
	}

	/**
	 * Setter for History List
	 * 
	 * @param historyList
	 */
	public void setReportHistoryList(ArrayList<History> historyList) {
		this.reportHistory = historyList;
	}

	public void clearReportHistory() {
		reportHistory.clear();
	}

	public boolean isHistoryEmpty() {

		return reportHistory.isEmpty();
	}

//	public void clearAllNextHistory() {
//
//		while (reportHistory.size() > index) {
//			reportHistory.remove(index);
//		}
//	}

	public boolean checkActionReplication(History history) {

		for (int i = 0; i < reportHistory.toArray().length; i++) {
			if (reportHistory.get(i).getAction().equals(history.getAction())) {
				return true;
			} else {
				continue;
			}
		}
		return false;

	}
	
	public History getHistoryByAction(Action action) {
		
		for (int i = 0; i < reportHistory.toArray().length; i++) {
			if (reportHistory.get(i).getAction().getText().equals(action.getText())) {
				return reportHistory.get(i);
			} else {
				continue;
			}
		}
		
		return null;
		
	}

}