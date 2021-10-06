package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public interface IButtonBar {

	/**
	 * Add a permanent button, this will not be removed when the view is cleared
	 * 
	 * @param widget
	 */
	public void addPermanent(Button btn);

	/**
	 * Add a permanent button, this will not be removed when the view is cleared
	 * 
	 * @param widget
	 */
	public void add(Button widget);

	/**
	 * remove a permanent button
	 * 
	 * @param widget
	 */
	public void remove(Button widget);

	/**
	 * Add a permanent button, With alignment
	 * 
	 * @param widget
	 */
	public void add(Button widget, HorizontalAlignmentConstant alignment);

	/**
	 * Adds this ButtonBar to given Widget
	 * 
	 * @param w
	 */
	public void addTo(HasWidgets w);

	public Widget asWidget();

	public void setDisabled(boolean disable);

	/**
	 * remove a view button
	 * 
	 * @param widget
	 */
	public void removeButton(Panel parent, Button child);

	/**
	 * Add a view button to the group
	 * 
	 * @param widget
	 */
	public void addButton(Panel parent, Button child);

	/**
	 * Clear View buttons of the group
	 * 
	 * @param widget
	 */
	public void clear(Panel group);

	/**
	 * Clear everything exist in ButtonBar
	 * 
	 * @param widget
	 */
	public void clear();

	/**
	 * Clear View buttons that are not added to Any group
	 */
	public void clearDirectButtons();

	public void show();

	public void addPermanent(Panel parent, Button child);

	public void remove();
}
