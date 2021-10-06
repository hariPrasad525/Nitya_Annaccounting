package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.nitya.accounter.web.client.core.IAccounterCore;
/**
 * For History Management
 * 
 * @author Raj Vimal
 * 
 */
public class History {

	private static HandlerManager handlers = new HandlerManager(null);
	private static HasValueChangeHandlers<String> instance = new HasValueChangeHandlers<String>() {

		@Override
		public void fireEvent(GwtEvent<?> event) {
			// ValueChangeEvent.fire(this,);
			handlers.fireEvent(event);
		}

		@Override
		public HandlerRegistration addValueChangeHandler(
				ValueChangeHandler<String> handler) {
			return handlers.addHandler(ValueChangeEvent.getType(), handler);
		}
	};

	AbstractView<?> view;

	Action action;

	IAccounterCore data;

	boolean isDependent;

	private static String token = "";

	public History() {

	}

	public History(AbstractView<?> view, IAccounterCore input, Action action,
			boolean dependent) {

		setView(view);
		setData(input);
		setAction(action);
		setDependent(dependent);
	}

	/**
	 * @return view
	 */

	public AbstractView getView() {
		return view;
	}

	/**
	 * ViewOrData to set
	 * 
	 * @param view
	 * 
	 */

	public void setView(AbstractView viewOrData) {
		this.view = viewOrData;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Setter for Action
	 * 
	 * @param action
	 * 
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * Check for views Dependent property
	 * 
	 * @return isDependent
	 */
	public boolean isDependent() {
		return isDependent;
	}

	/**
	 * Setter for isDependent
	 * 
	 * @param isDependent
	 */
	public void setDependent(boolean isDependent) {
		this.isDependent = isDependent;
	}

	/**
	 * Setter for the Data hold by View
	 * 
	 * @param data
	 */
	public void setData(IAccounterCore data) {
		this.data = data;
	}

	/**
	 * Getter for the Data hold by View
	 * 
	 * @return
	 */
	public IAccounterCore getData() {
		return data;
	}

	/**
	 * Update for History if Form data/ View data is not null for the Data hold
	 * by View
	 * 
	 * @param input
	 */
	public void updateHistory(Object input) {
		if (input instanceof IAccounterCore) {
			setData((IAccounterCore) input);
		}
	}

	public static void newItem(String historyToken) {
		newItem(historyToken, true);
	}

	public final static void newItem(String historyToken, boolean issueEvent) {
		historyToken = (historyToken == null) ? "" : historyToken;
		if (!historyToken.equals(getToken())) {
			setToken(historyToken);
			if (issueEvent) {
				fireHistoryChangedImpl(historyToken);
			}
		}
	}

	protected static void setToken(String token) {
		History.token = token;
	}

	/**
	 * Fires the {@link ValueChangeEvent} to all handlers with the given tokens.
	 */
	public static void fireHistoryChangedImpl(String newToken) {
		ValueChangeEvent.fire(instance, newToken);
	}

	public static String getToken() {
		return (token == null) ? "" : token;
	}

}
